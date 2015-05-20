package com.makechip.stdf2xls4.stdfapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.StdfReader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
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
	private static Collector<StdfRecord, List<List<StdfRecord>>, List<List<StdfRecord>>> splitBySeparator(Predicate<StdfRecord> sep) 
	{
	    return Collector.of(() -> new ArrayList<List<StdfRecord>>(Arrays.asList(new ArrayList<>())),
	                        (l, elem) -> {if(sep.test(elem)){l.add(new ArrayList<>());} else l.get(l.size()-1).add(elem);},
	                        (l1, l2) -> {l1.get(l1.size() - 1).addAll(l2.remove(0)); l1.addAll(l2); return l1;}); 
	}

	private List<String> stdfFiles;
	private List<StdfRecord> allRecords;
	@SuppressWarnings("unused")
	private boolean timeStampedFiles;

	public StdfAPI(List<String> stdfFiles)
	{
		this.stdfFiles = stdfFiles;
		allRecords = new ArrayList<StdfRecord>();
		timeStampedFiles = !stdfFiles.stream().filter(p -> !hasTimeStamp(p)).findFirst().isPresent();
	}
	
	public void initialize()
	{
		// load the stdf records
		stdfFiles.stream().forEach(p -> { 
				StdfReader r = new StdfReader(p); 
				try { r.read(); }
				catch (Exception e) { Log.fatal(e); }
			    r.stream().forEach(s -> allRecords.add(s.createRecord()));	
		});
		createTestIds();
		// put the records for each device into separate lists
		List<List<StdfRecord>> ll = allRecords.stream().collect(splitBySeparator(r -> r instanceof PartResultsRecord));
		
	}
	
	private void createTestIds()
	{
		
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
		//StdfAPI api = new StdfAPI(files);
	}
	
    private static boolean hasTimeStamp(String name)
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
    	if (timeStamp < 20000000000000L || timeStamp > 21000000000000L) return(false);
    	return(true);
    }
	
}
