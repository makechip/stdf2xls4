package com.makechip.stdf2xls4.stdfapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.io.DataInputStream;
import java.io.FileInputStream;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.ByteInputStream;
import com.makechip.stdf2xls4.stdf.DatalogTestRecord;
import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.FunctionalTestRecord;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.PinMapRecord;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestIdDatabase;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

import com.makechip.util.Log;
import com.makechip.util.ValuePair;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

/**
 * This is not a general purpose API for STDF.  It is mainly to
 * process the STDF records into structures that are usable for
 * converting the data into spreadsheet format.  It does the following:
 * 1. Assign TestIDs to all test records.
 * 2. normalizes the result values and units
 * 3. Packages tests by device
 * 4. builds map of device data.
 * @author eric
 *
 */
public final class StdfAPI
{
	private TestIdDatabase tiddb;
	private TestRecordDatabase tdb;
	private final CliOptions options;
    private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
    private final Map<PageHeader, Boolean> wafersortMap;
	private final List<File> stdfFiles;
	public final boolean timeStampedFiles;
	private String startDate;
	private String stopDate;
	private HashMap<PageHeader, TObjectIntHashMap<String>> snDupMap;
	private HashMap<PageHeader, TShortObjectHashMap<TShortIntHashMap>> xyDupMap;
	private String defaultTemp;
	private String defaultStep;

//	private Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
//	{
//	    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
//	                        (l, elem) -> { l.get(l.size()-1).add(elem); if(sep.test(elem)) l.add(new ArrayList<>()); },
//	                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
//	}

	public StdfAPI(final CliOptions options)
	{
		tiddb = new TestIdDatabase();
		snDupMap = new HashMap<>();
		xyDupMap = new HashMap<>();
		this.options = options;
		if (options.dynamicLimits) dynamicLimitMap = new HashMap<>(); else dynamicLimitMap = null;
		this.stdfFiles = options.stdfFiles;
		new ArrayList<StdfRecord>();
		timeStampedFiles = options.timestamps; // !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
		wafersortMap = new HashMap<>();
	}
	
	public boolean wafersort(PageHeader hdr)
	{
		return(wafersortMap.get(hdr));
	}
	
	private void checkLimits(PageHeader hdr, Map<TestID, Float> llmap, Map<TestID, Float> ulmap, List<ValuePair<DefaultValueDatabase, List<StdfRecord>>> list)
	{
		list.stream().flatMap(p -> p.value2.stream()).
			filter(r -> r instanceof ParametricRecord).
			map(s -> ParametricRecord.class.cast(s)).forEach(q -> checkLimit(q, llmap, ulmap, hdr));
	}
	
	private void checkLimit(ParametricRecord r, Map<TestID, Float> ll, Map<TestID, Float> ul, PageHeader hdr)
	{
	    //if (r.getTestName().startsWith("IDD_StdBy_Current_T020_MIN:IDD@VDD_P")) Log.msg(r.toString());
		Map<TestID, Boolean> m1 = dynamicLimitMap.get(hdr);
		if (m1 != null)
		{
			if (m1.get(r.getTestID()) != null) return;
		}
        if (r.getLoLimit() != null)
        {
        	float rlim = r.getLoLimit();
            Float ll1 = ll.get(r.getTestID());
        	if (ll1 == null) ll.put(r.getTestID(), r.getLoLimit());
        	else
        	{
        		if (rlim < (ll1 - Math.abs(0.001 * ll1)) || rlim > (ll1 + Math.abs(0.001 * ll1)))
        		{
        			if (m1 == null)
        			{
        				m1 = new IdentityHashMap<>();
        				dynamicLimitMap.put(hdr, m1);
        			}
        			m1.put(r.getTestID(), true);
        		}
        	}
        }
        if (r.getHiLimit() != null)
        {
            float rlim = r.getHiLimit();
            Float ul1 = ul.get(r.getTestID());
            if (ul1 == null) ul.put(r.getTestID(), r.getHiLimit());
            else
            {
            	if (rlim < (ul1 - Math.abs(0.001 * ul1)) || rlim > (ul1 + Math.abs(0.001 * ul1)))
            	{
            		if (m1 == null)
            		{
            			m1 = new IdentityHashMap<>();
            			dynamicLimitMap.put(hdr, m1);
            		}
            		m1.put(r.getTestID(), true);
            	}
            }
        }
	}
	
	public Calendar convertTz(long timeInSeconds)
	{
        long timeInms = timeInSeconds * 1000L;
        Date d = new Date(timeInms);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getOffset(timeInms);
        cal.add(Calendar.SECOND, -offset / 1000);
        return(cal);
	}
	
	// Find out if tester is fusionCx
	// if (sortByTimeStamp && allFilesHaveTimeStamps) use TreeMaps instead of LinkedHashMaps
	// Create DatalogTestRecords
	// Set up pinMaps
	// locate default values
	// Check for dynamic limits.
	// Create headers and TestResultDatabase
	public void initialize()
	{
	    List<Long> jobDates = new ArrayList<>();
		HashMap<PageHeader, List<ValuePair<DefaultValueDatabase, List<StdfRecord>>>> devList = new HashMap<>();
		// load the stdf records; group records by device	
		if (options.xlsName != null)
		{
		    stdfFiles.stream().forEach(file ->
		    {
		        StdfReader rdr = new StdfReader();
		        try (DataInputStream is = new DataInputStream(new FileInputStream(file)))
		        {
		            byte[] b = new byte[is.available()];
		            is.readFully(b);
		            rdr.read(tiddb, options.modifiers, new ByteInputStream(b), options);
		        }
		        catch (IOException e)
		        {
		            Log.warning("Error reading file: " + file.getName());	
		            Log.msg(e.getMessage());
		        }
				MasterInformationRecord mir = (MasterInformationRecord) rdr.getRecords().stream().
						filter(r -> r instanceof MasterInformationRecord).findFirst().orElse(null);
				if (mir != null) jobDates.add(mir.jobDate);
		    });
		    Collections.sort(jobDates);
		    Calendar cal1 = convertTz(jobDates.get(0));
		    int n = jobDates.size() - 1;
		    Calendar cal2 = convertTz(jobDates.get(n));
		    startDate = cal1.getTime().toString();
		    stopDate = cal2.getTime().toString();
		}
		stdfFiles.stream().forEach(file ->
		{
            //long timeStamp = timeStampedFiles ? getTimeStamp(file.getName()) : 0L;
			StdfReader rdr = new StdfReader();
			try (DataInputStream is = new DataInputStream(new FileInputStream(file)))
			{
	            byte[] b = new byte[is.available()];
	            is.readFully(b);
			    rdr.read(tiddb, options.modifiers, new ByteInputStream(b), options);
			}
			catch (IOException e)
			{
			    Log.warning("Error reading file: " + file.getName());	
			    Log.msg(e.getMessage());
			}
			if (options.dump)
			{
				rdr.getRecords().stream().forEach(r -> Log.msg(r.toString()));
			}
			if (options.xlsName != null)
			{
				// check tester type;
				MasterInformationRecord mir = (MasterInformationRecord) rdr.getRecords().stream().
						filter(r -> r instanceof MasterInformationRecord).findFirst().orElse(null);
				Calendar cal = convertTz(mir.testDate);
				String year = "" + cal.get(Calendar.YEAR);
				String month = cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH)+1) : "" + (cal.get(Calendar.MONTH)+1);
				String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : "" + cal.get(Calendar.DAY_OF_MONTH);
				String hour = cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + cal.get(Calendar.HOUR_OF_DAY) : "" + cal.get(Calendar.HOUR_OF_DAY);
				String minute = cal.get(Calendar.MINUTE) < 10 ? "0" + cal.get(Calendar.MINUTE) : "" + cal.get(Calendar.MINUTE);
				String second = cal.get(Calendar.SECOND) < 10 ? "0" + cal.get(Calendar.SECOND) : "" + cal.get(Calendar.SECOND);
				String ts = year + month + day + hour + minute + second;
				long timeStamp = 0L;
				try { timeStamp = Long.parseLong(ts); }
				catch (Exception e) { Log.fatal("Unknown timeStamp"); }
				boolean fusionCx = mir.testerType.equalsIgnoreCase("fusion_cx") || mir.testerType.equalsIgnoreCase("CTX");
				if (mir.execSoftwareVersion.indexOf("Smartest : s/w rev. 8") >= 0) fusionCx = true;
				defaultTemp = mir.temperature;
				defaultStep = mir.stepCode;
				DefaultValueDatabase dvd = new DefaultValueDatabase(fusionCx, timeStamp);
				// Set up pin maps:
				rdr.getRecords().stream().filter(r -> r instanceof PinMapRecord).forEach(r -> dvd.setPinName((PinMapRecord) r));	
				// Scan for DatalogTestRecords:
				List<StdfRecord> records = rdr.getRecords().stream().map(r -> convertDtrs(r)).collect(Collectors.toList());	
				// locate default values
				// Note that all records must be scanned for default values
				// because if the first device fails, then default values
				// for un-executed tests will be stored in records for subsequent devices.
				records.stream().filter(r -> r instanceof ParametricRecord).forEach(r -> dvd.loadDefaults((ParametricRecord) r));	
				// Group records by device:
				//List<List<StdfRecord>> list = records.stream().collect(splitBySeparator(r -> r instanceof PartResultsRecord));
				
				TIntObjectHashMap<List<StdfRecord>> listmap = new TIntObjectHashMap<>();
				List<List<StdfRecord>> list = new ArrayList<>();
				for (StdfRecord r : records)
				{
				    if (r instanceof PartResultsRecord)
				    {
				        PartResultsRecord prr = (PartResultsRecord) r;
				        int site = prr.siteNumber;
				        List<StdfRecord> l = listmap.get(site);
				        if (l == null)
				        {
				            l = new ArrayList<>();
				            listmap.put(site, l);
				        }
				        l.add(r);
				        list.add(l);
				        l = new ArrayList<>();
				        listmap.put(site, l);
				    }
				    else 
				    {
				        if (r instanceof FunctionalTestRecord)
				        {
				            FunctionalTestRecord ftr = (FunctionalTestRecord) r;
				            int site = ftr.siteNumber;
				            List<StdfRecord> l = listmap.get(site);
				            if (l == null)
				            {
				                l = new ArrayList<>();
				                listmap.put(site, l);
				            }
				            l.add(r);
				        }
				        else if (r instanceof ParametricRecord)
				        {
				            ParametricRecord pr = (ParametricRecord) r;
				            int site = pr.siteNumber;
				            List<StdfRecord> l = listmap.get(site);
				            if (l == null)
				            {
				                l = new ArrayList<>();
				                listmap.put(site, l);
				            }
				            l.add(r);
				        }
				        else if (r instanceof DatalogTestRecord)
				        {
				            DatalogTestRecord pr = (DatalogTestRecord) r;
				            int site = pr.siteNumber;
				            List<StdfRecord> l = listmap.get(site);
				            if (l == null)
				            {
				                l = new ArrayList<>();
				                listmap.put(site, l);
				            }
				            l.add(r);
				        }
				        else if (r instanceof DatalogTextRecord)
				        { // Note: DTR serial numbers generally are not used in multisite mode
				        	DatalogTextRecord dtr = (DatalogTextRecord) r;
				        	if (isSn(dtr))
				        	{
	                            StringTokenizer st = new StringTokenizer(dtr.text, ": \t");
	                            String site = "1";
	                            st.nextToken(); // burn "TEXT_DATA"
	                            if (st.hasMoreTokens())
	                            {
	                                st.nextToken(); // burn "S/N"
	                                if (st.hasMoreTokens())
	                                {
	                                    st.nextToken(); // burn the actual serial number
	                                    if (st.hasMoreTokens())
	                                    {
	                                        st.nextToken(); // burn the 0 (head number?)
	                                        if (st.hasMoreTokens())
	                                        {    
	                                            site = st.nextToken();
	                                        }
	                                    }
	                                }
	                            }
	                            @SuppressWarnings("deprecation")
                                Integer i = new Integer(site);
				        		List<StdfRecord> l = listmap.get(i);
				        		if (l == null)
				        		{
				        			l = new ArrayList<>();
				        			listmap.put(i, l);
				        		}
				        		l.add(r);
				        	}
				        }
				    }
				}
				
				// Create page headers:
				//list.stream().forEach(l -> createHeaders(new HeaderUtil(), devList, l));
				HeaderUtil hdr = new HeaderUtil(defaultStep, defaultTemp);
				hdr.setStartDate(startDate);
				hdr.setStopDate(stopDate);
				records.stream().forEach(r -> hdr.setHeader(r));
				for(List<StdfRecord> x : list)
				{
					Log.msg("record list sie = " + x.size());
				    List<ValuePair<DefaultValueDatabase, List<StdfRecord>>> l1 = devList.get(hdr.getHeader());
				    if (l1 == null)
				    {
				        l1 = new ArrayList<ValuePair<DefaultValueDatabase, List<StdfRecord>>>();
				        devList.put(hdr.getHeader(), l1);
				    }
				    l1.add(new ValuePair<>(dvd, x));
				}
				tdb = new TestRecordDatabase(options, tiddb, dynamicLimitMap);
				// now build TestRecord database:
			}
		});
		if (options.dynamicLimits)
		{
		    devList.keySet().stream()
		    .forEach(p -> checkLimits(p, new IdentityHashMap<TestID, Float>(), new IdentityHashMap<TestID, Float>(), devList.get(p)));
		}
		for (PageHeader p : devList.keySet())
		{
			Log.msg("PageHeader = " + p);
		    List<ValuePair<DefaultValueDatabase, List<StdfRecord>>> l = devList.get(p);
		    mapTests(options.sort, p, l);
		}
		//tdb.dump();
	}
	
	private StdfRecord convertDtrs(StdfRecord r)
	{
	    if (r instanceof DatalogTextRecord)
	    {
	    	DatalogTextRecord dtr = (DatalogTextRecord) r;
	    	if (dtr.text.contains(DatalogTextRecord.TEXT_DATA) && !dtr.text.contains(DatalogTextRecord.SERIAL_MARKER))
	    	{
	    		DatalogTestRecord dsr = new DatalogTestRecord(tiddb, dtr.text, options);
	    		return(dsr);
	    	}
	    }
	    else if (r.type == Record_t.PRR) tiddb.clearIdDups();
	    return(r);
	}

	private void mapTests(boolean sortDevices, PageHeader hdr, List<ValuePair<DefaultValueDatabase, List<StdfRecord>>> devList)
	{
		wafersortMap.put(hdr, hdr.contains(HeaderUtil.WAFER_ID));
		devList.stream().forEach(p -> buildList(sortDevices, hdr, p));
	}
	
	private void buildList(boolean sortDevices, PageHeader hdr, ValuePair<DefaultValueDatabase, List<StdfRecord>> list)
	{
	    DefaultValueDatabase dvd = list.value1;
		PartResultsRecord prr = list.value2.stream()
			.filter(p -> p instanceof PartResultsRecord)
			.map(s -> PartResultsRecord.class.cast(s))
			.findFirst()
			.orElse(null);
		if (prr == null) return;
		int hwBin = prr.hwBinNumber;
		int swBin = prr.swBinNumber;
		boolean fail = prr.partInfoFlags.contains(PartInfoFlag_t.PART_FAILED);
		boolean abnormalEOT = prr.partInfoFlags.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST);
		boolean noPassFailIndication = prr.partInfoFlags.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION);
		MasterInformationRecord mir = null;
		SnOrXy snxy = null;
		if (timeStampedFiles)
		{
			mir = (MasterInformationRecord) list.value2.stream().filter(p -> p instanceof MasterInformationRecord).findFirst().orElse(null);
		}
		if (wafersortMap.get(hdr))
		{
		    if (timeStampedFiles) snxy = TimeXY.getTimeXY(dvd.timeStamp, prr.xCoord, prr.yCoord);	
		    else 
		    {
		        if (options.noOverwrite)
		        {
		            TShortObjectHashMap<TShortIntHashMap> m1 = xyDupMap.get(hdr);
		            if (m1 == null)
		            {
		                m1 = new TShortObjectHashMap<>();
		                xyDupMap.put(hdr, m1);
		            }
		            TShortIntHashMap m2 = m1.get(prr.xCoord);
		            if (m2 == null)
		            {
		                m2 = new TShortIntHashMap(100, 0.7f, (short) 0, 0);
		                m1.put(prr.xCoord, m2);
		            }
		            int xyDupNum = m2.get(prr.yCoord);
		            snxy = XY.getXY(prr.xCoord, prr.yCoord, xyDupNum);
		            xyDupNum++;
		            m2.put(prr.yCoord, xyDupNum);
		        }
		        else
		        {
		            snxy = XY.getXY(prr.xCoord, prr.yCoord, 0);
		        }
		    }
		}
		else
		{
			// first check if the SN is in a text record
			StdfRecord rt = list.value2.stream().filter(p -> p instanceof DatalogTextRecord)
					.filter(p -> isSn((DatalogTextRecord) p)).findFirst().orElse(null);
			if (rt != null)
			{
				DatalogTextRecord dtr = (DatalogTextRecord) rt;
				StringTokenizer st = new StringTokenizer(dtr.text, ": \t");
				st.nextToken(); // burn "TEXT_DATA"
				st.nextToken(); // burn "S/N"
				String sn = st.nextToken();
				if (timeStampedFiles)
				{
				    snxy = TimeSN.getTimeSN(dvd.timeStamp, sn); 
				}
				else
				{
				    if (options.noOverwrite)
				    {
				        TObjectIntHashMap<String> m1 = snDupMap.get(hdr);
				        if (m1 == null)
				        {
				            m1 = new TObjectIntHashMap<String>(100, 0.7f, 0);
				            snDupMap.put(hdr, m1);
				        }
				        int snDupNum = m1.get(sn);
				        snxy = SN.getSN(sn, snDupNum);
				        snDupNum++;
				        m1.put(sn, snDupNum);
				    }
				    else snxy = SN.getSN(sn, 0);
				}
			}
			else
			{
			    if (timeStampedFiles)
			    {
				    snxy = TimeSN.getTimeSN(dvd.timeStamp, prr.partID); 
			    }
			    else
			    {
				    if (options.noOverwrite)
				    {
				        TObjectIntHashMap<String> m1 = snDupMap.get(hdr);
				        if (m1 == null)
				        {
				            m1 = new TObjectIntHashMap<String>(100, 0.7f, 0);
				            snDupMap.put(hdr, m1);
				        }
				        int snDupNum = m1.get(prr.partID);
				        snxy = SN.getSN(prr.partID, snDupNum);
				        snDupNum++;
				        m1.put(prr.partID, snDupNum);
				    }
				    else snxy = SN.getSN(prr.partID, 0);
			    }
			}
		}
		List<TestRecord> l = null;
		if (options.dontSkipSearchFails)
		{
		    l = list.value2.stream()
			.filter(p -> p instanceof TestRecord)
			.map(p -> TestRecord.class.cast(p))
			.collect(Collectors.toList());
		}
		else
		{
		    l = list.value2.stream()
			.filter(p -> p instanceof TestRecord)
			.filter(p -> !((TestRecord) p).getTestID().testName.endsWith("Search Failed")) 
			.map(p -> TestRecord.class.cast(p))
			.collect(Collectors.toList());
		} // no datalog test records in list
		String temperature = hdr.get(HeaderUtil.TEMPERATURE);
		if (temperature == null && mir != null) temperature = mir.temperature;
		DeviceHeader dh = new DeviceHeader(snxy, hwBin, swBin, fail, abnormalEOT, noPassFailIndication,temperature, timeStampedFiles);
		tdb.addRecords(dvd, sortDevices, hdr, dh, l);
	}
	
	
	private boolean isSn(DatalogTextRecord r)
	{
		return(r.text.contains(DatalogTextRecord.TEXT_DATA) && r.text.contains(":") && r.text.contains(DatalogTextRecord.SERIAL_MARKER));
	}
	
//    private boolean hasTimeStamp(File name)
//    {
//    	String s = name.toString().toLowerCase();
//    	int dotIndex = (s.endsWith(".std")) ? s.length() - 4 : (s.endsWith(".stdf")) ? s.length() - 5 : 0;
//    	if (dotIndex < 1) return(false);
//    	long timeStamp = 0L;
//    	try { timeStamp = Long.parseLong(s.substring(dotIndex - 14, dotIndex)); }
//    	catch (Exception e) { return(false); }
//    	if (timeStamp < 19000000000000L || timeStamp > 22000000000000L) return(false); // the timestamp must belong to recent centuries
//    	return(true);
//    }
    
    public Set<PageHeader> getPageHeaders()
    {
    	return(tdb.getPageHeaders());
    }
    
    public List<TestHeader> getTestHeaders(PageHeader hdr)
    {
    	return(tdb.getTestHeaders(hdr));
    }
    
    public List<TestHeader> getTestHeaders(PageHeader hdr, DeviceHeader dh)
    {
    	return(tdb.getTestHeaders(hdr, dh));
    }
    
    public List<DeviceHeader> getDeviceHeaders(PageHeader hdr, TestHeader id)
    {
    	return(tdb.getDeviceHeaders(hdr, id));
    }
    
    public List<DeviceHeader> getDeviceHeaders(PageHeader hdr) 
    {
    	return(tdb.getDeviceHeaders(hdr));
    }
    
    public TestResult getRecord(PageHeader hdr, TestHeader id, DeviceHeader dh)
    {
    	return(tdb.getRecord(hdr, id, dh));
    }
    
    public TestResult getRecord(PageHeader hdr, DeviceHeader dh, TestHeader id)
    {
    	return(tdb.getRecord(hdr, dh, id));
    }
    
    @Override
    public String toString()
    {
    	return(tdb.toString());
    }

//    private long getTimeStamp(String name)
//    {
//    	int dotIndex = (name.toLowerCase().endsWith(".std")) ? name.length() - 4 : name.length() - 5;
//    	int bIndex = dotIndex - 14;
//    	String stamp = name.substring(bIndex, dotIndex);
//    	long timeStamp = 0L;
//    	try { timeStamp = Long.parseLong(stamp); }
//    	catch (Exception e) { Log.fatal("Program bug: timeStamp is in filename, but will not parse correctly"); }
//    	return(timeStamp);
//    }
    
   
}
