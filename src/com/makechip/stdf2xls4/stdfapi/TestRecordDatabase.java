package com.makechip.stdf2xls4.stdfapi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.TestRecord;
import static com.makechip.stdf2xls4.stdf.enums.Record_t.*;

public class TestRecordDatabase
{
	private final boolean dynamicLimits;
	private Map<PageHeader, Map<DeviceHeader, Map<TestHeader, TestRecord>>> devMap;
	private Map<PageHeader, Map<TestHeader, Map<DeviceHeader, TestRecord>>> testMap;

	public TestRecordDatabase(boolean dynamicLimits)
	{
		devMap = new HashMap<>();
		testMap = new HashMap<>();
		this.dynamicLimits = dynamicLimits;
	}
	
	public void addRecords(PageHeader hdr, DeviceHeader dh, List<TestRecord> list)
	{
		final Map<DeviceHeader, Map<TestHeader, TestRecord>> m1a = (devMap.get(hdr) == null) ? new LinkedHashMap<>() : devMap.get(hdr);
		if (m1a.size() == 0) devMap.put(hdr, m1a);
		final Map<TestHeader, Map<DeviceHeader, TestRecord>> m1b = (testMap.get(hdr) == null) ? new LinkedHashMap<>() : testMap.get(hdr);
		if (m1b.size() == 0) testMap.put(hdr, m1b);
		final Map<TestHeader, TestRecord> m2a = (m1a.get(dh) == null) ? new LinkedHashMap<>() : m1a.get(dh);
		if (m2a.size() == 0) m1a.put(dh, m2a);
		list.stream().forEach(r -> add(r, dh, m1b, m2a));
	}
	
	private void add(TestRecord r, DeviceHeader dh, Map<TestHeader, Map<DeviceHeader, TestRecord>> m1b, Map<TestHeader, TestRecord> m2a)
	{
		TestHeader th = getTestHeader(r);
        m2a.put(th, r);
        Map<DeviceHeader, TestRecord> m2b = (m1b.get(r.getTestId()) == null) ? new LinkedHashMap<>() : m1b.get(r.getTestId());
        if (m2b.size() == 0) m1b.put(th, m2b);
        m2b.put(dh, r);
	}
	
	public TestRecord getRecord(PageHeader hdr, DeviceHeader dh, TestID id)
	{
		Map<DeviceHeader, Map<TestID, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(null);
		Map<TestID, TestRecord> m2 = m1.get(dh);
		if (m2 == null) return(null);
		return(m2.get(id));
	}
	
	public TestRecord getRecord(PageHeader hdr, TestID id, DeviceHeader dh)
	{
		Map<TestID, Map<DeviceHeader, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(null);
		Map<DeviceHeader, TestRecord> m2 = m1.get(id);
		if (m2 == null) return(null);
		return(m2.get(dh));
	}
	
	public Set<PageHeader> getPageHeaders() { return(devMap.keySet()); }
	
	public Set<DeviceHeader> getDeviceIds(PageHeader hdr) 
	{
		Map<DeviceHeader, Map<TestID, TestRecord>> m = devMap.get(hdr);
		if (m == null) return(new LinkedHashSet<DeviceHeader>());
		return(m.keySet());
	}
	
	public Set<TestID> getTestIds(PageHeader hdr)
	{
		Map<TestID, Map<DeviceHeader, TestRecord>> m = testMap.get(hdr);
		if (m == null) return(new LinkedHashSet<TestID>());
		return(m.keySet());
	}

	public Set<TestID> getTestIds(PageHeader hdr, DeviceHeader dh)
	{
		Map<DeviceHeader, Map<TestID, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<TestID>());
		Map<TestID, TestRecord> m2 = m1.get(dh);
		if (m2 == null) return(new LinkedHashSet<TestID>());
		return(m2.keySet());
	}

	public Set<DeviceHeader> getDeviceIds(PageHeader hdr, TestID id)
	{
		Map<TestID, Map<DeviceHeader, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<DeviceHeader>());
		Map<DeviceHeader, TestRecord> m2 = m1.get(id);
		if (m2 == null) return(new LinkedHashSet<DeviceHeader>());
		return(m2.keySet());
	}
	
	private List<TestHeader> getTestHeader(TestRecord r)
	{
		List<TestHe>
		switch (r.type)
		{
		case FTR:
		case PTR:
		case MPR:
		case DTR:
	    default: throw new RuntimeException("Unknown Test Record type: " + r.type);
		}
	}
	
}
