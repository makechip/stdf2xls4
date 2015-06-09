package com.makechip.stdf2xls4.stdfapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdf.MultipleResultParametricRecord;
import com.makechip.stdf2xls4.stdf.ParametricTestRecord;
import com.makechip.stdf2xls4.stdf.PinTestID;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestRecord;

public class TestRecordDatabase
{
	private final Map<PageHeader, Boolean> cxTester;
	private final DefaultValueDatabase idb;
	private final Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap;
	private Map<PageHeader, Map<DeviceHeader, Map<TestHeader, DeviceResult>>> devMap;
	private Map<PageHeader, Map<TestHeader, Map<DeviceHeader, DeviceResult>>> testMap;

	public TestRecordDatabase(Map<PageHeader, Boolean> cxTester, DefaultValueDatabase idb, Map<PageHeader, Map<TestID, Boolean>> dynamicLimitMap)
	{
		this.cxTester = cxTester;
		this.idb = idb;
		this.dynamicLimitMap = dynamicLimitMap;
		devMap = new HashMap<>();
		testMap = new HashMap<>();
	}
	
	public void addRecords(PageHeader hdr, DeviceHeader dh, List<TestRecord> list)
	{
		final Map<DeviceHeader, Map<TestHeader, DeviceResult>> m1a = (devMap.get(hdr) == null) ? new LinkedHashMap<>() : devMap.get(hdr);
		if (m1a.size() == 0) devMap.put(hdr, m1a);
		final Map<TestHeader, Map<DeviceHeader, DeviceResult>> m1b = (testMap.get(hdr) == null) ? new LinkedHashMap<>() : testMap.get(hdr);
		if (m1b.size() == 0) testMap.put(hdr, m1b);
		final Map<TestHeader, DeviceResult> m2a = (m1a.get(dh) == null) ? new LinkedHashMap<>() : m1a.get(dh);
		if (m2a.size() == 0) m1a.put(dh, m2a);
		list.stream().forEach(r -> add(hdr, r, dh, m1b, m2a));
	}
	
	private boolean hasDynamicLimits(PageHeader hdr, TestID id)
	{
	    Map<TestID, Boolean> m = dynamicLimitMap.get(hdr);
	    if (m == null) return(false);
	    Boolean b = m.get(id);
	    if (b == null) return(false);
	    return(b.booleanValue());
	}
	
	private void add(PageHeader hdr, TestRecord r, DeviceHeader dh, Map<TestHeader, Map<DeviceHeader, DeviceResult>> m1b, Map<TestHeader, DeviceResult> m2a)
	{
		List<TestHeader> th = getTestHeader(hdr, r);
		if (th.size() == 1)
		{
			TestHeader h = th.get(0);
			m2a.put(h, r);
            Map<DeviceHeader, TestRecord> m2b = (m1b.get(r.getTestId()) == null) ? new LinkedHashMap<>() : m1b.get(r.getTestId());
            m2b.put(dh, r);
		}
		else
		{
			int i = 0;
			if (th.get(i) instanceof LoLimitHeader)
			{
				
			}
		}
	}
	
	public TestRecord getRecord(PageHeader hdr, DeviceHeader dh, TestHeader id)
	{
		Map<DeviceHeader, Map<TestHeader, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(null);
		Map<TestHeader, TestRecord> m2 = m1.get(dh);
		if (m2 == null) return(null);
		return(m2.get(id));
	}
	
	public TestRecord getRecord(PageHeader hdr, TestHeader id, DeviceHeader dh)
	{
		Map<TestHeader, Map<DeviceHeader, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(null);
		Map<DeviceHeader, TestRecord> m2 = m1.get(id);
		if (m2 == null) return(null);
		return(m2.get(dh));
	}
	
	public Set<PageHeader> getPageHeaders() { return(devMap.keySet()); }
	
	public Set<DeviceHeader> getDeviceIds(PageHeader hdr) 
	{
		Map<DeviceHeader, Map<TestHeader, TestRecord>> m = devMap.get(hdr);
		if (m == null) return(new LinkedHashSet<DeviceHeader>());
		return(m.keySet());
	}
	
	public Set<TestHeader> getTestHeaders(PageHeader hdr)
	{
		Map<TestHeader, Map<DeviceHeader, TestRecord>> m = testMap.get(hdr);
		if (m == null) return(new LinkedHashSet<TestHeader>());
		return(m.keySet());
	}

	public Set<TestHeader> getTestHeaders(PageHeader hdr, DeviceHeader dh)
	{
		Map<DeviceHeader, Map<TestHeader, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<TestHeader>());
		Map<TestHeader, TestRecord> m2 = m1.get(dh);
		if (m2 == null) return(new LinkedHashSet<TestHeader>());
		return(m2.keySet());
	}

	public Set<DeviceHeader> getDeviceIds(PageHeader hdr, TestHeader id)
	{
		Map<TestHeader, Map<DeviceHeader, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<DeviceHeader>());
		Map<DeviceHeader, TestRecord> m2 = m1.get(id);
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
		          Boolean t = cxTester.get(hdr);
		          boolean fusionCx = (t == true);
				  if (hasDynamicLimits(hdr, mpr.getTestId())) list.add(new LoLimitHeader(mpr.getTestId()));
				  if (mpr.hasLoLimit() && mpr.hasHiLimit())
				  {
			          int[] idx = mpr.getRtnIndex();			  
			          for (int i : idx)
			          {
			         	  String pin = (fusionCx) ? idb.getPhysicalPinName(mpr.siteNumber, i) : idb.getChannelName(mpr.siteNumber, i);
			          	  PinTestID pid = PinTestID.getTestID(idb, mpr.getTestId(), pin);
			           	  list.add(new MultiParametricTestHeader(pid, mpr.units, mpr.loLimit, mpr.hiLimit));
			          }
				  }
				  else if (mpr.hasLoLimit())
				  {
			          int[] idx = mpr.getRtnIndex();			  
			          for (int i : idx)
			          {
			        	  String pin = (fusionCx) ? idb.getPhysicalPinName(mpr.siteNumber, i) : idb.getChannelName(mpr.siteNumber, i);
			        	  PinTestID pid = PinTestID.getTestID(idb, mpr.getTestId(), pin);
			         	  list.add(new MultiParametricTestHeader(pid, mpr.units, mpr.loLimit));
			          }
				  }
				  else if (mpr.hasHiLimit())
				  {
			          int[] idx = mpr.getRtnIndex();			  
			          for (int i : idx)
			          {
			         	  String pin = (fusionCx) ? idb.getPhysicalPinName(mpr.siteNumber, i) : idb.getChannelName(mpr.siteNumber, i);
			           	  PinTestID pid = PinTestID.getTestID(idb, mpr.getTestId(), pin);
			           	  list.add(new MultiParametricTestHeader(pid, mpr.hiLimit, mpr.units));
			          }
				  }
				  if (hasDynamicLimits(hdr, mpr.getTestId())) list.add(new HiLimitHeader(mpr.getTestId()));
				  break;
		case DTR: list.add(new TestHeader(r.getTestId())); break; 
	    default: throw new RuntimeException("Unknown Test Record type: " + r.type);
		}
		return(list);
	}
	
}
