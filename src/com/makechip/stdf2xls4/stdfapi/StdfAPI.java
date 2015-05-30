package com.makechip.stdf2xls4.stdfapi;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.stream.Collector;

import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.IdentityDatabase;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.RecordBytes;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
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
	private IdentityDatabase idb;
    public static final String TEXT_DATA = RecordBytes.TEXT_DATA;
    public static final String SERIAL_MARKER = RecordBytes.SERIAL_MARKER;
	private static Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
	{
	    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
	                        (l, elem) -> { l.get(l.size()-1).add(elem); if(sep.test(elem)) l.add(new ArrayList<>()); },
	                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
	}
    private HashMap<Map<String, String>, Map<SnOrXy, Map<TestID, StdfRecord>>> recordMap;
    //private HashMap<HashMap<String, String>, Map<SnOrXy, DeviceResult>> deviceMap;
	private List<String> stdfFiles;
	private boolean timeStampedFiles;
	private boolean wafersort;

	public StdfAPI(List<String> stdfFiles)
	{
		idb = new IdentityDatabase();
		this.stdfFiles = stdfFiles;
		new ArrayList<StdfRecord>();
		timeStampedFiles = !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
		recordMap = new HashMap<>();
//		deviceMap = new HashMap<>();
	}
	
	public void initialize()
	{
		HeaderUtil hdr = new HeaderUtil();
		HashMap<HashMap<String, String>, List<List<StdfRecord>>> devList = new HashMap<>();
		// load the stdf records
	    // group records by device	
		    stdfFiles.stream()
			.map(p -> new StdfReader(idb, p, timeStampedFiles))
			.flatMap(rdr -> rdr.read().stream())
			.map(s -> s.createRecord())
			.collect(splitBySeparator(r -> r instanceof PartResultsRecord))
			.stream()
			.forEach(p -> createHeaders(hdr, devList, p));
		// now create the TestIDs, and for each header build a map testID -> test record
		devList.keySet().stream().forEach(p -> mapTests(p, devList.get(p)));
		// find default values
		for (Map<String, String> h : recordMap.keySet())
		{
			Map<SnOrXy, Map<TestID, StdfRecord>> m1 = recordMap.get(h);
			for (SnOrXy sn : m1.keySet())
			{
				Map<TestID, StdfRecord> m3 = m1.get(sn);
				for (TestID id : m3.keySet())
				{
					StdfRecord r = m3.get(id);
					if (r instanceof ParametricRecord)
					{
						ParametricRecord pr = (ParametricRecord) r;
						if (pr.getResScal() != StdfRecord.MISSING_BYTE && !pr.getOptFlags().contains(OptFlag_t.RES_SCAL_INVALID))
						{
						    break;
						}
					}
				}
			}
		}
		// scale units, limits and values
		
		// break MultipleResultParametricRecords into per-pin result objects
		
		
		
		// create result objects, adding default values
		
		
	}
	
	private void mapTests(HashMap<String, String> hdr, List<List<StdfRecord>> devList)
	{
		wafersort = hdr.containsKey(HeaderUtil.WAFER_ID);
		devList.stream().forEach(p -> buildList(hdr, p));
	}
	
	private void buildList(HashMap<String, String> hdr, List<StdfRecord> list)
	{
		StdfRecord r = list.stream().filter(p -> p instanceof PartResultsRecord).findFirst().orElse(null);
		if (r == null) return;
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
		    if (timeStampedFiles) snxy = TimeXY.getTimeXY(mir.timeStamp, x, y);	
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
				if (timeStampedFiles) snxy = TimeSN.getTimeSN(mir.timeStamp, sn);
				else snxy = SN.getSN(sn);
			}
			else
			{
				Log.msg("prr.id = " + prr.getPartID());
				if (timeStampedFiles) snxy = TimeSN.getTimeSN(mir.timeStamp, prr.getPartID());
				else snxy = SN.getSN(prr.getPartID());
			}
		}
		// Now build map: header -> SnOrXy -> TestID -> test record
		Map<SnOrXy, Map<TestID, StdfRecord>> m1 = recordMap.get(hdr);
		if (m1 == null)
		{
			m1 = new LinkedHashMap<>();
			recordMap.put(hdr, m1);
		}
		final Map<TestID, StdfRecord> m2 = (m1.get(snxy) == null) ? new LinkedHashMap<>() : m1.get(snxy);
		m1.put(snxy, m2);
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
		try (FileWriter fw = new FileWriter("stdfapi.log")) 
		{
			for (Map<String, String> h : api.recordMap.keySet())
			{
				fw.write("H: " + h.hashCode() + " " + h.toString());
				fw.write(Log.eol);
				Map<SnOrXy, Map<TestID, StdfRecord>> tm = api.recordMap.get(h);
				for (SnOrXy sn : tm.keySet())
				{
					fw.write("    sn: " + sn.hashCode() + " " + sn.toString());
     				fw.write(Log.eol);
					Map<TestID, StdfRecord> lm = tm.get(sn);
					for (TestID id : lm.keySet())
					{
						fw.write("        id: " + id.hashCode() + " " + id);
        				fw.write(Log.eol);
					}
				}
			}
		}
		catch (Exception e) { Log.fatal(e); }
	}
	
}
