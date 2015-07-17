package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TObjectFloatHashMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.StdfException;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.stdf.StdfRecord.*;

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
    public static final String TEXT_DATA = StdfRecord.TEXT_DATA;
    public static final String SERIAL_MARKER = StdfRecord.SERIAL_MARKER;
    private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
    private final Map<PageHeader, Boolean> wafersortMap;
	private final List<File> stdfFiles;
	private boolean timeStampedFiles;

	//private Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
	//{
	//    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
	//                        (l, elem) -> { l.get(l.size()-1).add(elem); if(sep.test(elem)) l.add(new ArrayList<>()); },
	//                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
	//}

	public StdfAPI(final CliOptions options)
	{
		tiddb = new TestIdDatabase();
		this.options = options;
		if (options.dynamicLimits) dynamicLimitMap = new HashMap<>(); else dynamicLimitMap = null;
		this.stdfFiles = options.stdfFiles;
		new ArrayList<StdfRecord>();
		timeStampedFiles = !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
		wafersortMap = new HashMap<>();
	}
	
	public boolean wafersort(PageHeader hdr)
	{
		return(wafersortMap.get(hdr));
	}
	
	private void checkLimits(PageHeader hdr, TObjectFloatHashMap<TestID> lmap, List<List<StdfRecord>> list, OptFlag_t missingLimitFlag)
	{
		list.stream().flatMap(p -> p.stream()).
			filter(r -> r instanceof ParametricRecord).
			map(s -> ParametricRecord.class.cast(s)).forEach(q -> checkLimit(q, lmap, hdr, missingLimitFlag));
	}
	
	private void checkLimit(ParametricRecord r, TObjectFloatHashMap<TestID> m, PageHeader hdr, OptFlag_t missingLimitFlag)
	{
		Map<TestID, Boolean> m1 = dynamicLimitMap.get(hdr);
		if (m1 != null)
		{
			if (m1.get(r.getTestId()) == true) return;
		}
        if (!r.getOptFlags().contains(missingLimitFlag))
        {
        	float recLimit = (missingLimitFlag == OptFlag_t.NO_LO_LIMIT) ? r.getLoLimit() : r.getHiLimit();
            float ll1 = m.get(r.getTestId());
        	if (ll1 == MISSING_FLOAT) m.put(r.getTestId(), recLimit);
        	else
        	{
        		if (recLimit < ll1 - 0.0001 * ll1 || recLimit > ll1 + 0.0001 * ll1)
        		{
        			if (m1 == null)
        			{
        				m1 = new IdentityHashMap<>();
        				dynamicLimitMap.put(hdr, m1);
        			}
        			m1.put(r.getTestId(), true);
        		}
        	}
        }
	}
	
	public void initialize() throws StdfException, IOException
	{
		HashMap<PageHeader, List<List<StdfRecord>>> devList = new HashMap<>();
		// load the stdf records; group records by device	
		for (File f : stdfFiles)
		{
			StdfReader rdr = new StdfReader(tiddb, f, timeStampedFiles);
			rdr.read();
			List<StdfRecord> l = new ArrayList<>();
			for (StdfRecord rec : rdr.getRecords())
			{
			    if (options.dump) Log.msg(rec.toString());
			    if (rec instanceof PartResultsRecord)
			    {
			    	l.add(rec);
			        createHeaders(new HeaderUtil(), devList, l);
			        l = new ArrayList<>();
			    }
			    else l.add(rec);
			}
		}
		if (options.dynamicLimits) // check for dynamicLimits
		{
			devList.keySet().stream().forEach(p -> checkLimits(p, new TObjectFloatHashMap<TestID>(100, 0.7f, MISSING_FLOAT), devList.get(p), OptFlag_t.NO_LO_LIMIT));
		    devList.keySet().stream().forEach(p -> checkLimits(p, new TObjectFloatHashMap<TestID>(100, 0.7f, MISSING_FLOAT), devList.get(p), OptFlag_t.NO_HI_LIMIT));	
		}	
		tdb = new TestRecordDatabase(options, tiddb, dynamicLimitMap);
	    // now build TestRecord database:
		devList.keySet().stream().forEach(p -> mapTests(options.sort, p, devList.get(p)));
	}
	
	private void createHeaders(HeaderUtil hdr, HashMap<PageHeader, List<List<StdfRecord>>> devList, List<StdfRecord> l)
	{
		l.stream().forEach(r -> hdr.setHeader(r));
		List<List<StdfRecord>> dl = devList.get(hdr.getHeader());
		if (dl == null) devList.put(hdr.getHeader(), dl = new ArrayList<>());
		dl.add(l);
	}
	
	private void mapTests(boolean sortDevices, PageHeader hdr, List<List<StdfRecord>> devList)
	{
		wafersortMap.put(hdr, hdr.contains(HeaderUtil.WAFER_ID));
		devList.stream().forEach(p -> buildList(sortDevices, hdr, p));
	}
	
	private void buildList(boolean sortDevices, PageHeader hdr, List<StdfRecord> list)
	{
		PartResultsRecord prr = list.stream()
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
			mir = (MasterInformationRecord) list.stream().filter(p -> p instanceof MasterInformationRecord).findFirst().orElse(null);
		}
		if (wafersortMap.get(hdr))
		{
		    if (timeStampedFiles) snxy = TimeXY.getTimeXY(mir.getTimeStamp(), prr.xCoord, prr.yCoord);	
		    else snxy = XY.getXY(prr.xCoord, prr.yCoord);
		}
		else
		{
			// first check if the SN is in a text record
			StdfRecord rt = list.stream().filter(p -> p instanceof DatalogTextRecord)
					.filter(p -> isSn((DatalogTextRecord) p)).findFirst().orElse(null);
			if (rt != null)
			{
				DatalogTextRecord dtr = (DatalogTextRecord) rt;
				StringTokenizer st = new StringTokenizer(dtr.text, ": \t");
				st.nextToken(); // burn "TEXT_DATA"
				st.nextToken(); // burn "S/N"
				String sn = st.nextToken();
				snxy = timeStampedFiles ? TimeSN.getTimeSN(mir.getTimeStamp(), sn) : SN.getSN(sn);
			}
			else
			{
				snxy = timeStampedFiles ? TimeSN.getTimeSN(mir.getTimeStamp(), prr.partID) : SN.getSN(prr.partID);
			}
		}
		List<TestRecord> l = list.stream()
			.filter(p -> p instanceof TestRecord)
			.map(p -> TestRecord.class.cast(p))
			.collect(Collectors.toList());
		String temperature = hdr.get(HeaderUtil.TEMPERATURE);
		if (temperature == null) temperature = mir.temperature;
		DeviceHeader dh = new DeviceHeader(snxy, hwBin, swBin, fail, abnormalEOT, noPassFailIndication,temperature);
		tdb.addRecords(sortDevices, hdr, dh, l);
	}
	
	
	private boolean isSn(DatalogTextRecord r)
	{
		return(r.text.contains(TEXT_DATA) && r.text.contains(":") && r.text.contains(SERIAL_MARKER));
	}
	
    private boolean hasTimeStamp(File name)
    {
    	String s = name.toString().toLowerCase();
    	int dotIndex = (s.endsWith(".std")) ? s.length() - 4 : (s.endsWith(".stdf")) ? s.length() - 5 : 0;
    	if (dotIndex < 1) return(false);
    	long timeStamp = 0L;
    	try { timeStamp = Long.parseLong(s.substring(dotIndex - 14, dotIndex)); }
    	catch (Exception e) { return(false); }
    	if (timeStamp < 19000000000000L || timeStamp > 22000000000000L) return(false); // the timestamp must belong to recent centuries
    	return(true);
    }
    
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
    
    public Set<DeviceHeader> getDeviceHeaders(PageHeader hdr, TestHeader id)
    {
    	return(tdb.getDeviceHeaders(hdr, id));
    }
    
    public Set<DeviceHeader> getDeviceHeaders(PageHeader hdr) 
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
    
}
