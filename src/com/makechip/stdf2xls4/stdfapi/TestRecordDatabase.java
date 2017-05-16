package com.makechip.stdf2xls4.stdfapi;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.MultipleResultParametricRecord;
import com.makechip.stdf2xls4.stdf.ParametricTestRecord;
import com.makechip.stdf2xls4.stdf.DatalogTestRecord;
import com.makechip.stdf2xls4.stdf.FloatList;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestIdDatabase;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.stdf2xls4.stdf.FunctionalTestRecord;
import com.makechip.stdf2xls4.stdf.IntList;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.TestID.PinTestID;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import com.makechip.util.Log;

public class TestRecordDatabase
{
	private final TestIdDatabase tdb;
	private final CliOptions options;
	private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
	private Map<PageHeader, Map<DeviceHeader, Map<TestHeader, TestResult>>> devMap;
	private Map<PageHeader, Map<TestHeader, Map<DeviceHeader, TestResult>>> testMap;

	public TestRecordDatabase(CliOptions options, TestIdDatabase tdb, Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap)
	{
		this.tdb = tdb;
		this.dynamicLimitMap = dynamicLimitMap;
		devMap = new HashMap<>();
		testMap = new HashMap<>();
		this.options = options;
	}
	
	public void addRecords(DefaultValueDatabase dvd, boolean sortDevices, PageHeader hdr, DeviceHeader dh, List<TestRecord> list)
	{
		final Map<DeviceHeader, Map<TestHeader, TestResult>> m1a = 
			(devMap.get(hdr) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : devMap.get(hdr);
		if (m1a.size() == 0) devMap.put(hdr, m1a);
		final Map<TestHeader, Map<DeviceHeader, TestResult>> m1b = (testMap.get(hdr) == null) ? new LinkedHashMap<>() : testMap.get(hdr);
		if (m1b.size() == 0) testMap.put(hdr, m1b);
		final Map<TestHeader, TestResult> m2a = (m1a.get(dh) == null) ? new LinkedHashMap<>() : m1a.get(dh);
		if (m2a.size() == 0) m1a.put(dh, m2a);
		list.stream().forEach(r -> add(dvd, sortDevices, hdr, r, dh, m1b, m2a));
	}
	
	private boolean hasDynamicLimits(PageHeader hdr, TestID id)
	{
		if (dynamicLimitMap == null) return(false);
	    Map<TestID, Boolean> m = dynamicLimitMap.get(hdr);
	    if (m == null) return(false);
	    Boolean b = m.get(id);
	    if (b == null) return(false);
	    return(b.booleanValue());
	}
	
	private void build1(PageHeader pageHdr, StringBuilder sb)
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m1 = devMap.get(pageHdr);
		sb.append(pageHdr.toString()).append(Log.eol);
		m1.keySet().stream().forEach(deviceHdr -> build2(deviceHdr, m1.get(deviceHdr), sb));
	}
	
	private void build2(DeviceHeader deviceHdr, Map<TestHeader, TestResult> m, StringBuilder sb)
	{
		sb.append(deviceHdr.toString()).append(Log.eol);
		m.keySet().stream().forEach(testHdr -> build3(testHdr, m.get(testHdr), sb));
	}
	
	private void build3(TestHeader testHdr, TestResult testRslt, StringBuilder sb)
	{
		sb.append("    ").append(testHdr.toString()).append(" ").append(testRslt.toString()).append(Log.eol);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		devMap.keySet().stream().forEach(pageHdr -> build1(pageHdr, sb));
		return(sb.toString());
	}
	
	private TestResult getTestResult(DefaultValueDatabase dvd, StdfRecord r)
	{
		TestResult tr = null;
		switch (r.type)
		{
		case DTRX: 
		    Object o = ((DatalogTestRecord) r).value; 
		    tr = new DatalogTestResult(o.toString()); break;
		case FTR: tr = new TestResult(((FunctionalTestRecord) r).testFlags); break;
		case PTR: 
			ParametricTestRecord ptr = (ParametricTestRecord) r;
			tr = new ParametricTestResult(ptr.testFlags, dvd.getScaledResult(ptr)); 
			break;
		case MPR: 
			MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
		    tr = new ParametricTestResult(mpr.testFlags, dvd.getScaledResults(mpr).get(0));
		break;
		default: throw new RuntimeException("Unknown test record type: " + r.type);
		}
		return(tr);		
	}
	
	private void add(DefaultValueDatabase dvd,
			         boolean sortDevices, 
			         PageHeader hdr, 
			         TestRecord r, 
			         DeviceHeader dh, 
			         Map<TestHeader, Map<DeviceHeader, TestResult>> m1b, 
			         Map<TestHeader, TestResult> m2a)
	{
		List<TestHeader> th = getTestHeader(dvd, hdr, r);
		if (th.size() == 1)
		{
			TestHeader h = th.get(0);
			TestResult tr = getTestResult(dvd, r);
			m2a.put(h, tr);
            Map<DeviceHeader, TestResult> m2b = m1b.get(h);
            if (m2b == null)
            {
            	m2b = sortDevices ? new TreeMap<>() : new LinkedHashMap<>();
            	m1b.put(h, m2b);
            }
            m2b.put(dh, tr);
		}
		else // either PTR with dynamic limits, MPR, or MPR with dynamic limits
		{
			int i = 0;
			TestHeader hlh = null;
			if (th.get(i).isLoLimitHeader())
			{
				TestHeader h = th.get(i);
				ParametricRecord pr = ParametricRecord.class.cast(r);
				TestResult tr = new LoLimitResult(pr.getLoLimit());
				m2a.put(h, tr);
                Map<DeviceHeader, TestResult> m2b = m1b.get(h);
                if (m2b == null)
                {
                	m2b = sortDevices ? new TreeMap<>() : new LinkedHashMap<>();
                	m1b.put(h,  m2b);
                }
			    m2b.put(dh, tr);	
				i++;
			}
			if (r.type == Record_t.PTR)
			{
				TestHeader h = th.get(i);
				TestResult tr = getTestResult(dvd, r);
				m2a.put(h,  tr);
                Map<DeviceHeader, TestResult> m2b = m1b.get(h);
                if (m2b == null)
                {
                	m2b = sortDevices ? new TreeMap<>() : new LinkedHashMap<>();
                	m1b.put(h, m2b);
                }
			    m2b.put(dh, tr);	
			    i++;
			}
			else
			{
				final int a = i;
				MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
				final FloatList sresults = dvd.getScaledResults(mpr);
				IntList lst = (mpr.rtnIndex == null) ? dvd.getDefaultRtnIndex(r.getTestID()) : mpr.rtnIndex;
				IntStream.range(0, lst.size()).forEach(j -> {
				        Float rslt = mpr.results.get(j);
			    	    int b = a;
			    	    // Need to check limits and set testFlags so that only failing pins are flagged as failures.
			    	    // Must consider the following:
			    	    // 1. Is loLimit valid?
			    	    // 2. is hiLimit valid?
			    	    // 3. if (result == loLimit) is it a pass?
			    	    // 4. if (result == hiLimit) is it a pass?
			    	    boolean fail = false;
			    	    if (mpr.loLimit != null)
			    	    {
			    	        if (mpr.paramFlags.contains(ParamFlag_t.LO_LIMIT_EQ_PASS)) 
			    	        {
			    	        	if (rslt < mpr.loLimit) fail = true;
			    	        }
			    	        else
			    	        {
			    	        	if (rslt <= mpr.loLimit) fail = true;
			    	        }
			    	    }
			    	    if (mpr.hiLimit != null)
			    	    {
			    	    	if (mpr.paramFlags.contains(ParamFlag_t.HI_LIMIT_EQ_PASS))
			    	    	{
			    	    		if (rslt > mpr.hiLimit) fail = true;
			    	    	}
			    	    	else
			    	    	{
			    	    		if (rslt >= mpr.hiLimit) fail = true;
			    	    	}
			    	    }
			            TestResult tr = new ParametricTestResult(getTestFlags(mpr.testFlags, fail), sresults.get(j));
			            TestHeader TH = th.get(b+j);
			            m2a.put(TH, tr);
                        Map<DeviceHeader, TestResult> m2b = m1b.get(TH);
                        if (m2b == null) 
                        {
                        	m2b = sortDevices ? new TreeMap<>() : new LinkedHashMap<>();
                        	m1b.put(TH, m2b);
                        }
			            m2b.put(dh, tr);	
			        });
			    i += sresults.size();
			    if (i < th.size())
			    {
			        TestHeader h = th.get(i);
			        if (h.isHiLimitHeader()) hlh = h;
			        else throw new RuntimeException("Program Bug - expected HiLimitHeader, but got " + h.getClass().getSimpleName());
			    }
			}
			if (hlh != null)
			{
				ParametricRecord pr = ParametricRecord.class.cast(r);
				TestResult tr = new HiLimitResult(pr.getHiLimit());
				m2a.put(th.get(i), tr);
                Map<DeviceHeader, TestResult> m2b = m1b.get(hlh); 
                if (m2b == null)
                {
                	m2b = sortDevices ? new TreeMap<>() : new LinkedHashMap<>();
                	m1b.put(hlh,  m2b);
                }
			    m2b.put(dh, tr);	
			}
		}
	}
	
	private Set<TestFlag_t> getTestFlags(Set<TestFlag_t> flags, boolean fail)
	{
		Set<TestFlag_t> set = EnumSet.noneOf(TestFlag_t.class);
		for (TestFlag_t f : flags)
		{
			if (!fail)
			{
				if (f == TestFlag_t.FAIL) continue;
			}
			set.add(f);
		}
		return(set);
	}
	
	public TestResult getRecord(PageHeader hdr, DeviceHeader dh, TestHeader id)
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m1 = devMap.get(hdr);
		if (m1 == null) return(null);
		Map<TestHeader, TestResult> m2 = m1.get(dh);
		if (m2 == null) return(null);
		return(m2.get(id));
	}
	
	public TestResult getRecord(PageHeader hdr, TestHeader id, DeviceHeader dh)
	{
		Map<TestHeader, Map<DeviceHeader, TestResult>> m1 = testMap.get(hdr);
		if (m1 == null) return(null);
		Map<DeviceHeader, TestResult> m2 = m1.get(id);
		if (m2 == null) return(null);
		return(m2.get(dh));
	}
	
	public Set<PageHeader> getPageHeaders() { return(devMap.keySet()); }
	
	public List<DeviceHeader> getDeviceHeaders(PageHeader hdr) 
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m = devMap.get(hdr);
		if (m == null) return(new ArrayList<DeviceHeader>());
		List<DeviceHeader> list = m.keySet().stream().collect(Collectors.toList());
		return(list);
	}
	
	public List<TestHeader> getTestHeaders(PageHeader hdr)
	{
		Map<TestHeader, Map<DeviceHeader, TestResult>> m = testMap.get(hdr);
		if (m == null) return(new ArrayList<TestHeader>());
		List<TestHeader> list = m.keySet().stream().collect(Collectors.toList());
		return(list);
	}

	public List<TestHeader> getTestHeaders(PageHeader hdr, DeviceHeader dh)
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m1 = devMap.get(hdr);
		if (m1 == null) return(new ArrayList<TestHeader>());
		Map<TestHeader, TestResult> m2 = m1.get(dh);
		if (m2 == null) return(new ArrayList<TestHeader>());
		List<TestHeader> list = m2.keySet().stream().collect(Collectors.toList());
		return(list);
	}

	public List<DeviceHeader> getDeviceHeaders(PageHeader hdr, TestHeader id)
	{
		Map<TestHeader, Map<DeviceHeader, TestResult>> m1 = testMap.get(hdr);
		if (m1 == null) return(new ArrayList<DeviceHeader>());
		Map<DeviceHeader, TestResult> m2 = m1.get(id);
		if (m2 == null) return(new ArrayList<DeviceHeader>());
		List<DeviceHeader> list = m2.keySet().stream().collect(Collectors.toList());
		return(list);
	}
	
	private List<TestHeader> getTestHeader(DefaultValueDatabase dvd, PageHeader hdr, TestRecord r)
	{
		List<TestHeader> list = new ArrayList<>();
		switch (r.type)
		{
		case FTR: list.add(new TestHeader(r.getTestID())); break;
		case PTR: 
			ParametricTestRecord ptr = ParametricTestRecord.class.cast(r);
		    String sunits = dvd.getScaledUnits(ptr);
		    Float sLoLimit = dvd.getScaledLoLimit(ptr);
		    Float sHiLimit = dvd.getScaledHiLimit(ptr);
		    if (hasDynamicLimits(hdr, ptr.getTestID())) list.add(new MultiParametricTestHeader(ptr.getTestID(), sunits, Limit_t.LO_LIMIT));  
		    if (options.pinSuffix && ptr.id.testName.indexOf(options.delimiter) > 0)
		    {
		    	int didx = ptr.id.testName.lastIndexOf(options.delimiter);
		        String pin = ptr.id.testName.substring(didx+1);
		        String tname = ptr.id.testName.substring(0, didx);
		        list.add(new MultiParametricTestHeader(tname, ptr.id.testNumber, ptr.id.dupNum, pin, sunits, sLoLimit, sHiLimit));  
		    }
		    else
		    {
		        ParametricTestHeader pth = new ParametricTestHeader(ptr.getTestID(), sunits, sLoLimit, sHiLimit);
		        Log.msg("pth = " + pth);
		        list.add(pth);
		    }
		    if (hasDynamicLimits(hdr, ptr.getTestID())) list.add(new MultiParametricTestHeader(ptr.getTestID(), sunits, Limit_t.HI_LIMIT));
		    break;
		case MPR: 
			MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
	        sunits = dvd.getScaledUnits(mpr);
	        sLoLimit = dvd.getScaledLoLimit(mpr);
	        sHiLimit = dvd.getScaledHiLimit(mpr);
			if (hasDynamicLimits(hdr, mpr.getTestID())) list.add(new MultiParametricTestHeader(mpr.getTestID(), sunits, Limit_t.LO_LIMIT));
			IntList rtn = (mpr.rtnIndex == null) ? dvd.getDefaultRtnIndex(mpr.id) : mpr.rtnIndex;
			if (rtn == null) Log.msg("RTN_INDEX is NULL test number is " + r.getTestID().testNumber);
			rtn.stream().mapToObj(x -> dvd.getPinName(mpr.siteNumber, mpr.headNumber, x)).forEach(pin -> { 
				 				  PinTestID pid = TestID.PinTestID.getTestID(tdb, mpr.getTestID(), pin); 
				  				  list.add(new MultiParametricTestHeader(pid, sunits, sLoLimit, sHiLimit)); });
		    if (hasDynamicLimits(hdr, mpr.getTestID())) list.add(new MultiParametricTestHeader(mpr.getTestID(), sunits, Limit_t.HI_LIMIT));
		    break;
		case DTRX: 
			DatalogTestRecord dtr = (DatalogTestRecord) r;
			list.add(new ParametricTestHeader(r.getTestID(), dtr.units, null, null)); break; 
	    default: throw new RuntimeException("Unknown Test Record type: " + r.type);
		}
		return(list);
	}
	
}
