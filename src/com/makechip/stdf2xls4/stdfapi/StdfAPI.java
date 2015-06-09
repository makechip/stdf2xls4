package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TObjectFloatHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.RecordBytes;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;

import static com.makechip.stdf2xls4.stdf.StdfRecord.*;

/**
 * This is not a general purpose API for STDF.  It is mainly to
 * process the STDF records into structures that are usable for
 * converting the data into spreadsheet format.  It does the following:
 * 1. Assign TestIDs to all test records.
 * 2. Locates default values for missing record data.
 * 3. normalizes the result values and units
 * 4. Packages tests by device
 * 5. builds list of device data.
 * @author eric
 *
 */
public final class StdfAPI
{
	private final DefaultValueDatabase idb;
	private final Map<PageHeader, Boolean> tester;
	private TestRecordDatabase tdb;
	private final boolean dynamicLimits;
    public static final String TEXT_DATA = RecordBytes.TEXT_DATA;
    public static final String SERIAL_MARKER = RecordBytes.SERIAL_MARKER;
    private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
	private Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
	{
	    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
	                        (l, elem) -> { l.get(l.size()-1).add(elem); if(sep.test(elem)) l.add(new ArrayList<>()); },
	                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
	}
	private final List<String> stdfFiles;
	private boolean timeStampedFiles;
	private boolean wafersort;

	public StdfAPI(List<String> stdfFiles, boolean dynamicLimits)
	{
		this.dynamicLimits = dynamicLimits;
		if (dynamicLimits) dynamicLimitMap = new HashMap<>(); else dynamicLimitMap = null;
		idb = new DefaultValueDatabase();
		this.stdfFiles = stdfFiles;
		new ArrayList<StdfRecord>();
		timeStampedFiles = !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
		tester = new HashMap<>();
	}
	
	private void checkLoLimits(PageHeader hdr, TObjectFloatHashMap<TestID> lmap, List<List<StdfRecord>> list)
	{
		list.stream().flatMap(p -> p.stream()).
			filter(r -> r instanceof ParametricRecord).
			map(s -> ParametricRecord.class.cast(s)).forEach(q -> checkLoLimit(q, lmap, hdr));
	}
	
	private void checkLoLimit(ParametricRecord r, TObjectFloatHashMap<TestID> m, PageHeader hdr)
	{
		Map<TestID, Boolean> m1 = dynamicLimitMap.get(hdr);
		if (m1 != null)
		{
			if (m1.get(r.getTestId()) == true) return;
		}
        if (!r.getOptFlags().contains(OptFlag_t.NO_LO_LIMIT))
        {
            float ll1 = m.get(r.getTestId());
        	if (ll1 == MISSING_FLOAT)
        	{
        	    ll1 = r.getLoLimit();
        	    m.put(r.getTestId(), ll1);
        	}
        	else
        	{
        		if (r.getLoLimit() < ll1 - 0.0001 * ll1 || r.getLoLimit() > ll1 + 0.0001 * ll1)
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
	
	private void checkHiLimits(PageHeader hdr, TObjectFloatHashMap<TestID> hmap, List<List<StdfRecord>> list)
	{
		list.stream().flatMap(p -> p.stream()).
			filter(r -> r instanceof ParametricRecord).
			map(s -> ParametricRecord.class.cast(s)).forEach(q -> checkHiLimit(q, hmap, hdr));
	}
	
	private void checkHiLimit(ParametricRecord r, TObjectFloatHashMap<TestID> m, PageHeader hdr)
	{
		Map<TestID, Boolean> m1 = dynamicLimitMap.get(hdr);
		if (m1 != null)
		{
			if (m1.get(r.getTestId()) == true) return;
		}
        if (!r.getOptFlags().contains(OptFlag_t.NO_HI_LIMIT))
        {
            float hl1 = m.get(r.getTestId());
        	if (hl1 == MISSING_FLOAT)
        	{
        	    hl1 = r.getHiLimit();
        	    m.put(r.getTestId(), hl1);
        	}
        	else
        	{
        		if (r.getHiLimit() < hl1 - 0.0001 * hl1 || r.getHiLimit() > hl1 + 0.0001 * hl1)
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
	
	private void identifyTester(PageHeader hdr, List<List<StdfRecord>> list)
	{
		MasterInformationRecord r = list.stream().
				                        flatMap(p -> p.stream()).
				                        filter(q -> q instanceof MasterInformationRecord).
				                        map(s -> MasterInformationRecord.class.cast(s)).
				                        findFirst().orElse(null);
		if (r == null) return;
		boolean fusion = false;
		if (r.testerType.equalsIgnoreCase("Fusion_CX")) fusion = true;
		if (r.testerType.equalsIgnoreCase("CXT")) fusion = true;
        tester.put(hdr, fusion);
	}
	
	public void initialize()
	{
		HashMap<PageHeader, List<List<StdfRecord>>> devList = new HashMap<>();
		// load the stdf records; group records by device	
		    stdfFiles.stream()
			.map(p -> new StdfReader(idb, p, timeStampedFiles))
			.flatMap(rdr -> rdr.read().stream())
			.map(s -> s.createRecord())
			.collect(splitBySeparator(r -> r instanceof PartResultsRecord))
			.stream()
			.forEach(p -> createHeaders(new HeaderUtil(), devList, p));
		// determine tester type:
		devList.keySet().stream().forEach(p -> identifyTester(p, devList.get(p)));
		// now check for dynamicLimits
		if (dynamicLimits)
		{
			devList.keySet().stream().
			    forEach(p -> checkLoLimits(p, new TObjectFloatHashMap<TestID>(100, 0.7f, MISSING_FLOAT), devList.get(p)));
		    devList.keySet().stream().
		        forEach(p -> checkHiLimits(p, new TObjectFloatHashMap<TestID>(100, 0.7f, MISSING_FLOAT), devList.get(p)));	
		}	
		tdb = new TestRecordDatabase(tester, idb, dynamicLimitMap);
	    // now build TestRecord database:
		devList.keySet().stream().forEach(p -> mapTests(p, devList.get(p)));
	}
	
	private void mapTests(PageHeader hdr, List<List<StdfRecord>> devList)
	{
		wafersort = hdr.contains(HeaderUtil.WAFER_ID);
		devList.stream().forEach(p -> buildList(hdr, p));
	}
	
	private void buildList(PageHeader hdr, List<StdfRecord> list)
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
		if (wafersort)
		{
		    if (timeStampedFiles) snxy = TimeXY.getTimeXY(mir.timeStamp, prr.xCoord, prr.yCoord);	
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
				snxy = timeStampedFiles ? TimeSN.getTimeSN(mir.timeStamp, sn) : SN.getSN(sn);
			}
			else
			{
				snxy = timeStampedFiles ? TimeSN.getTimeSN(mir.timeStamp, prr.getPartID()) : SN.getSN(prr.getPartID());
			}
		}
		List<TestRecord> l = list.stream()
			.filter(p -> p instanceof TestRecord)
			.map(p -> TestRecord.class.cast(p))
			.collect(Collectors.toList());
		String temperature = hdr.get(HeaderUtil.TEMPERATURE);
		if (temperature == null) temperature = mir.temperature;
		DeviceHeader dh = new DeviceHeader(snxy, hwBin, swBin, fail, abnormalEOT, noPassFailIndication,temperature);
		tdb.addRecords(hdr, dh, l);
	}
	
	
	private boolean isSn(DatalogTextRecord r)
	{
		return(r.text.contains(TEXT_DATA) && r.text.contains(":") && r.text.contains(SERIAL_MARKER));
	}
	
	private void createHeaders(HeaderUtil hdr, HashMap<PageHeader, List<List<StdfRecord>>> devList, List<StdfRecord> l)
	{
		l.stream().forEach(r -> hdr.setHeader(r));
		List<List<StdfRecord>> dl = devList.get(hdr.getHeader());
		if (dl == null) devList.put(hdr.getHeader(), dl = new ArrayList<>());
		dl.add(l);
	}
	
    private boolean hasTimeStamp(String name)
    {
    	int dotIndex = 0;
    	if (name.toLowerCase().endsWith(".std")) dotIndex = name.length() - 4;
    	else if (name.toLowerCase().endsWith(".stdf")) dotIndex = name.length() - 5;
    	else return(false);
    	int bIndex = dotIndex - 14;
    	if (bIndex < 1) return(false);
    	String stamp = name.substring(bIndex, dotIndex);
    	long timeStamp = 0L;
    	try { timeStamp = Long.parseLong(stamp); }
    	catch (Exception e) { return(false); }
    	// the timestamp must belong to the 21st century.
    	if (timeStamp < 20000000000000L || timeStamp > 21000000000000L) return(false);
    	return(true);
    }
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java com.makechip.stdf2xls.stdf.StdfAPI <stdfFiles>");
			System.exit(1);
		}
		List<String> files = new ArrayList<String>();
	    Arrays.stream(args).forEach(p -> files.add(p));	
		StdfAPI api = new StdfAPI(files, false);
		api.initialize();
	}
	
}
