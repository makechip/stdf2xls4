package com.makechip.stdf2xls4.stdfapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.makechip.stdf2xls4.stdf.MultipleResultParametricRecord;
import com.makechip.stdf2xls4.stdf.ParametricTestRecord;
import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.PinTestID;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestIdDatabase;
import com.makechip.stdf2xls4.stdf.TestRecord;
import com.makechip.stdf2xls4.stdf.FunctionalTestRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

public class TestRecordDatabase
{
	private final TestIdDatabase tdb;
	private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
	private Map<PageHeader, Map<DeviceHeader, Map<TestHeader, TestResult>>> devMap;
	private Map<PageHeader, Map<TestHeader, Map<DeviceHeader, TestResult>>> testMap;

	public TestRecordDatabase(TestIdDatabase tdb, Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap)
	{
		this.tdb = tdb;
		this.dynamicLimitMap = dynamicLimitMap;
		devMap = new HashMap<>();
		testMap = new HashMap<>();
	}
	
	public void addRecords(boolean sortDevices, PageHeader hdr, DeviceHeader dh, List<TestRecord> list)
	{
		final Map<DeviceHeader, Map<TestHeader, TestResult>> m1a = 
			(devMap.get(hdr) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : devMap.get(hdr);
		if (m1a.size() == 0) devMap.put(hdr, m1a);
		final Map<TestHeader, Map<DeviceHeader, TestResult>> m1b = (testMap.get(hdr) == null) ? new LinkedHashMap<>() : testMap.get(hdr);
		if (m1b.size() == 0) testMap.put(hdr, m1b);
		final Map<TestHeader, TestResult> m2a = (m1a.get(dh) == null) ? new LinkedHashMap<>() : m1a.get(dh);
		if (m2a.size() == 0) m1a.put(dh, m2a);
		list.stream().forEach(r -> add(sortDevices, hdr, r, dh, m1b, m2a));
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
	
	private void build1(PageHeader p, StringBuilder sb)
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m1 = devMap.get(p);
		sb.append(p.toString()).append(Log.eol);
		m1.keySet().stream().forEach(q -> build2(q, m1.get(q), sb));
	}
	
	private void build2(DeviceHeader dh, Map<TestHeader, TestResult> m, StringBuilder sb)
	{
		sb.append(dh.toString()).append(Log.eol);
		m.keySet().stream().forEach(p -> build3(p, m.get(p), sb));
	}
	
	private void build3(TestHeader th, TestResult tr, StringBuilder sb)
	{
		sb.append("    ").append(th.toString()).append(" ").append(tr.toString()).append(Log.eol);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		devMap.keySet().stream().forEach(p -> build1(p, sb));
		return(sb.toString());
	}
	
	private TestResult getTestResult(StdfRecord r)
	{
		TestResult tr = null;
		switch (r.type)
		{
		case DTR: tr = new DatalogTestResult(((DatalogTextRecord) r).text); break;
		case FTR: tr = new TestResult(((FunctionalTestRecord) r).testFlags); break;
		case PTR: tr = new ParametricTestResult(((ParametricRecord) r).testFlags, ((ParametricTestRecord) r).result); break;
		case MPR: MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
		tr = new ParametricTestResult(mpr.testFlags, mpr.getResults()[0]);
		break;
		default: throw new RuntimeException("Unknown test record type: " + r.type);
		}
		return(tr);		
	}
	
	private void add(boolean sortDevices, PageHeader hdr, TestRecord r, DeviceHeader dh, Map<TestHeader, Map<DeviceHeader, TestResult>> m1b, Map<TestHeader, TestResult> m2a)
	{
		List<TestHeader> th = getTestHeader(hdr, r);
		if (th.size() == 1)
		{
			TestHeader h = th.get(0);
			TestResult tr = getTestResult(r);
			m2a.put(h, tr);
            Map<DeviceHeader, TestResult> m2b = 
            	(m1b.get(r.getTestId()) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : m1b.get(r.getTestId());
            m2b.put(dh, tr);
		}
		else // either PTR with dynamic limits, MPR, or MPR with dynamic limits
		{
			int i = 0;
			TestHeader hlh = null;
			if (th.get(i) instanceof LoLimitHeader)
			{
				TestHeader h = th.get(i);
				ParametricRecord pr = ParametricRecord.class.cast(r);
				TestResult tr = new LoLimitResult(pr.getLoLimit());
				m2a.put(h, tr);
				TestID lid = TestID.createTestID(tdb, r.getTestId().testNumber, r.getTestId().testName + "_lo");
                Map<DeviceHeader, TestResult> m2b = (m1b.get(lid) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : m1b.get(r.getTestId());
			    m2b.put(dh, tr);	
				i++;
			}
			if (r.type == Record_t.PTR)
			{
				TestHeader h = th.get(i);
				TestResult tr = getTestResult(r);
				m2a.put(h,  tr);
                Map<DeviceHeader, TestResult> m2b = (m1b.get(r.getTestId()) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : m1b.get(r.getTestId());
			    m2b.put(dh, tr);	
			    i++;
			}
			else
			{
				final int a = i;
				MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
			    mpr.getPinNames().forEach(pin -> {
			    	    int b = a;
			            TestID pid = PinTestID.getTestID(tdb, mpr.getTestId(), pin);
			            TestResult tr = new ParametricTestResult(mpr.testFlags, mpr.getResult(pin));
			            m2a.put(th.get(b), tr);
                        Map<DeviceHeader, TestResult> m2b = (m1b.get(pid) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : m1b.get(pid);
			            m2b.put(dh, tr);	
			            b++;	
			        });
			    if (i < th.size())
			    {
			        TestHeader h = th.get(i);
			        if (h instanceof HiLimitHeader) hlh = h;
			        else Log.fatal("Program Bug - expected HiLimitHeader, but got " + h.getClass().getSimpleName());
			    }
			}
			if (hlh != null)
			{
				ParametricRecord pr = ParametricRecord.class.cast(r);
				TestResult tr = new HiLimitResult(pr.getHiLimit());
				m2a.put(th.get(i), tr);
				TestID hid = TestID.createTestID(tdb, r.getTestId().testNumber, r.getTestId().testName + "_hi");
                Map<DeviceHeader, TestResult> m2b = (m1b.get(hid) == null) ? (sortDevices ? new TreeMap<>() : new LinkedHashMap<>()) : m1b.get(hid);
			    m2b.put(dh, tr);	
			}
		}
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
	
	public Set<DeviceHeader> getDeviceIds(PageHeader hdr) 
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m = devMap.get(hdr);
		if (m == null) return(new LinkedHashSet<DeviceHeader>());
		return(m.keySet());
	}
	
	public Set<TestHeader> getTestHeaders(PageHeader hdr)
	{
		Map<TestHeader, Map<DeviceHeader, TestResult>> m = testMap.get(hdr);
		if (m == null) return(new LinkedHashSet<TestHeader>());
		return(m.keySet());
	}

	public Set<TestHeader> getTestHeaders(PageHeader hdr, DeviceHeader dh)
	{
		Map<DeviceHeader, Map<TestHeader, TestResult>> m1 = devMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<TestHeader>());
		Map<TestHeader, TestResult> m2 = m1.get(dh);
		if (m2 == null) return(new LinkedHashSet<TestHeader>());
		return(m2.keySet());
	}

	public Set<DeviceHeader> getDeviceIds(PageHeader hdr, TestHeader id)
	{
		Map<TestHeader, Map<DeviceHeader, TestResult>> m1 = testMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<DeviceHeader>());
		Map<DeviceHeader, TestResult> m2 = m1.get(id);
		if (m2 == null) return(new LinkedHashSet<DeviceHeader>());
		return(m2.keySet());
	}
	
	private List<TestHeader> getTestHeader(PageHeader hdr, TestRecord r)
	{
		List<TestHeader> list = new ArrayList<>();
		switch (r.type)
		{
		case FTR: list.add(new TestHeader(r.getTestId())); break;
		case PTR: ParametricTestRecord ptr = ParametricTestRecord.class.cast(r);
		          if (hasDynamicLimits(hdr, ptr.getTestId())) list.add(new LoLimitHeader(ptr.getTestId()));  
		          if (ptr.hasLoLimit() && ptr.hasHiLimit())
		          {
		               list.add(new ParametricTestHeader(ptr.getTestId(), ptr.units, ptr.loLimit, ptr.hiLimit));
		          }
		          else if (ptr.hasLoLimit())
		          {
		           	  list.add(new ParametricTestHeader(ptr.getTestId(), ptr.units, ptr.loLimit));
		          }
		          else if (ptr.hasHiLimit())
		          {
		          	  list.add(new ParametricTestHeader(ptr.getTestId(), ptr.hiLimit, ptr.units));
		          }
		          if (hasDynamicLimits(hdr, ptr.getTestId())) list.add(new HiLimitHeader(ptr.getTestId()));
		          break;
		case MPR: MultipleResultParametricRecord mpr = MultipleResultParametricRecord.class.cast(r);
				  if (hasDynamicLimits(hdr, mpr.getTestId())) list.add(new LoLimitHeader(mpr.getTestId()));
				  if (mpr.hasLoLimit() && mpr.hasHiLimit())
				  {
					  mpr.getPinNames().
					      forEach(pin -> { 
					    	  				  PinTestID pid = PinTestID.getTestID(tdb, mpr.getTestId(), pin); 
					    	  				  list.add(new MultiParametricTestHeader(pid, mpr.units, mpr.loLimit, mpr.hiLimit)); 
					    	  			 });
				  }
				  else if (mpr.hasLoLimit())
				  {
					  mpr.getPinNames().
					      forEach(pin -> {
			        	                     PinTestID pid = PinTestID.getTestID(tdb, mpr.getTestId(), pin);
			         	                     list.add(new MultiParametricTestHeader(pid, mpr.units, mpr.loLimit));
			                             });
				  }
				  else if (mpr.hasHiLimit())
				  {
					  mpr.getPinNames().
					      forEach(pin -> {
			           	                     PinTestID pid = PinTestID.getTestID(tdb, mpr.getTestId(), pin);
			           	                     list.add(new MultiParametricTestHeader(pid, mpr.hiLimit, mpr.units));
					                     });
				  }
				  if (hasDynamicLimits(hdr, mpr.getTestId())) list.add(new HiLimitHeader(mpr.getTestId()));
				  break;
		case DTR: list.add(new TestHeader(r.getTestId())); break; 
	    default: throw new RuntimeException("Unknown Test Record type: " + r.type);
		}
		return(list);
	}
	
}
