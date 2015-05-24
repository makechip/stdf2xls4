package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collector;

import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.RecordBytes;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.util.Log;

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
public class StdfAPI
{
    public static final String TEXT_DATA = RecordBytes.TEXT_DATA;
    public static final String SERIAL_MARKER = RecordBytes.SERIAL_MARKER;
	private static Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
	{
	    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
	                        (l, elem) -> { l.get(l.size()-1).add(elem); if(sep.test(elem)) l.add(new ArrayList<>()); },
	                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
	}
	private final TLongObjectHashMap<String> tnameMap;
    private HashMap<HashMap<String, String>, TreeMap<SnOrXy, LinkedHashMap<TestID, StdfRecord>>> map;
	private List<String> stdfFiles;
	private List<StdfRecord> allRecords;
	private boolean timeStampedFiles;
	private boolean wafersort;
	private IdentityDatabase idb;

	public StdfAPI(List<String> stdfFiles)
	{
		this.stdfFiles = stdfFiles;
		allRecords = new ArrayList<StdfRecord>();
		timeStampedFiles = !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
		tnameMap = new TLongObjectHashMap<>();
		idb = new IdentityDatabase();
		map = new HashMap<>();
	}
	
	public void initialize()
	{
		// load the stdf records
		if (timeStampedFiles)
		{
		    stdfFiles.stream().forEach(p -> new StdfReader(p).read().stream().forEach(s -> allRecords.add(s.createRecord(tnameMap, getTimeStamp(p)))));
		}
		else
		{
		    stdfFiles.stream().forEach(p -> new StdfReader(p).read().stream().forEach(s -> allRecords.add(s.createRecord(tnameMap))));
		}
		// put the records for each device into separate lists
		List<List<StdfRecord>> ll = allRecords.stream().collect(splitBySeparator(r -> r instanceof PartResultsRecord));
		// Map each device test list by header
		HashMap<String, String> header = new HashMap<>();
		HeaderUtil hdr = new HeaderUtil(header);
		HashMap<HashMap<String, String>, List<List<StdfRecord>>> devList = new HashMap<>();
		ll.stream().forEach(p -> createHeaders(hdr, devList, p));
		// now create the TestIDs, and for each header build a map testID -> test record
		devList.keySet().stream().forEach(p -> mapTests(p, devList.get(p)));
	}
	
	private void mapTests(HashMap<String, String> hdr, List<List<StdfRecord>> devList)
	{
		wafersort = hdr.containsKey(HeaderUtil.WAFER_ID);
		devList.stream().forEach(p -> buildList(hdr, p));
	}
	
	private void buildList(HashMap<String, String> hdr, List<StdfRecord> list)
	{
		StdfRecord r = list.stream().filter(p -> p instanceof PartResultsRecord).findFirst().orElse(null);
		assert r != null;
		PartResultsRecord prr = PartResultsRecord.class.cast(r);
		MasterInformationRecord mir = null;
		SnOrXy snxy = null;
		if (timeStampedFiles)
		{
			mir = (MasterInformationRecord) list.stream().filter(p -> p instanceof MasterInformationRecord).findFirst().orElse(null);
		}
		if (wafersort)
		{
	    	short x = prr.xCoord;
	    	short y = prr.yCoord;
		    if (timeStampedFiles) snxy = TimeXY.getTimeXY(mir.fileTimeStamp, x, y);	
		    else snxy = XY.getXY(x, y);
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
				if (timeStampedFiles) snxy = TimeSN.getTimeSN(mir.fileTimeStamp, sn);
				else snxy = SN.getSN(sn);
			}
			else
			{
				if (timeStampedFiles) snxy = TimeSN.getTimeSN(mir.fileTimeStamp, prr.getPartID());
				else snxy = SN.getSN(prr.getPartID());
			}
		}
		// Now build map: header -> SnOrXy -> TestID -> test record
		TreeMap<SnOrXy, LinkedHashMap<TestID, StdfRecord>> m1 = map.get(hdr);
		if (m1 == null)
		{
			m1 = new TreeMap<>();
			map.put(hdr, m1);
		}
		final LinkedHashMap<TestID, StdfRecord> m2 = (m1.get(snxy) == null) ? new LinkedHashMap<>() : m1.get(snxy);
		m1.put(snxy, m2);
		list.stream().filter(p -> p instanceof TestRecord).map(p -> TestRecord.class.cast(p)).forEach(p -> addRecord(m2, p));
		idb.testIdDupMap.clear();
	}
	
	private void addRecord(LinkedHashMap<TestID, StdfRecord> m, TestRecord p)
	{
		TestID id = TestID.createTestID(idb, p.testNumber, p.getTestName());
		m.put(id, p);
	}
	
	private boolean isSn(DatalogTextRecord r)
	{
		return(r.text.contains(TEXT_DATA) && r.text.contains(":") && r.text.contains(SERIAL_MARKER));
	}
	
	private void createHeaders(HeaderUtil hdr, HashMap<HashMap<String, String>, List<List<StdfRecord>>> devList, List<StdfRecord> l)
	{
		l.stream().forEach(r -> hdr.setHeader(r));
		List<List<StdfRecord>> dl = devList.get(hdr.header);
		if (dl == null) devList.put(hdr.header, dl = new ArrayList<>());
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
	
    private long getTimeStamp(String name)
    {
    	int dotIndex = 0;
    	if (name.toLowerCase().endsWith(".std")) dotIndex = name.length() - 4;
    	else dotIndex = name.length() - 5;
    	int bIndex = dotIndex - 14;
    	String stamp = name.substring(bIndex, dotIndex);
    	long timeStamp = 0L;
    	try { timeStamp = Long.parseLong(stamp); }
    	catch (Exception e) { Log.fatal("Program bug: timeStamp is in filename, but will not parse correctly"); }
    	return(timeStamp);
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
		StdfAPI api = new StdfAPI(files);
		api.initialize();
	}
	
}
