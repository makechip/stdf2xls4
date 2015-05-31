package com.makechip.stdf2xls4.stdfapi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestRecord;

public class TestRecordDatabase
{
	private Map<PageHeader, Map<SnOrXy, Map<TestID, TestRecord>>> devMap;
	private Map<PageHeader, Map<TestID, Map<SnOrXy, TestRecord>>> testMap;

	public TestRecordDatabase()
	{
		devMap = new HashMap<>();
		testMap = new HashMap<>();
	}
	
	public void addRecords(PageHeader hdr, SnOrXy snxy, List<TestRecord> list)
	{
		final Map<SnOrXy, Map<TestID, TestRecord>> m1a = (devMap.get(hdr) == null) ? new LinkedHashMap<>() : devMap.get(hdr);
		if (m1a.size() == 0) devMap.put(hdr, m1a);
		final Map<TestID, Map<SnOrXy, TestRecord>> m1b = (testMap.get(hdr) == null) ? new LinkedHashMap<>() : testMap.get(hdr);
		if (m1b.size() == 0) testMap.put(hdr, m1b);
		final Map<TestID, TestRecord> m2a = (m1a.get(snxy) == null) ? new LinkedHashMap<>() : m1a.get(snxy);
		if (m2a.size() == 0) m1a.put(snxy, m2a);
		list.stream().forEach(r -> add(r, snxy, m1b, m2a));
	}
	
	private void add(TestRecord r, SnOrXy snxy, Map<TestID, Map<SnOrXy, TestRecord>> m1b, Map<TestID, TestRecord> m2a)
	{
        m2a.put(r.getTestId(), r);
        Map<SnOrXy, TestRecord> m2b = (m1b.get(r.getTestId()) == null) ? new LinkedHashMap<>() : m1b.get(r.getTestId());
        if (m2b.size() == 0) m1b.put(r.getTestId(), m2b);
        m2b.put(snxy, r);
	}
	
	public TestRecord getRecord(PageHeader hdr, SnOrXy snxy, TestID id)
	{
		Map<SnOrXy, Map<TestID, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(null);
		Map<TestID, TestRecord> m2 = m1.get(snxy);
		if (m2 == null) return(null);
		return(m2.get(id));
	}
	
	public TestRecord getRecord(PageHeader hdr, TestID id, SnOrXy snxy)
	{
		Map<TestID, Map<SnOrXy, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(null);
		Map<SnOrXy, TestRecord> m2 = m1.get(id);
		if (m2 == null) return(null);
		return(m2.get(snxy));
	}
	
	public Set<PageHeader> getPageHeaders() { return(devMap.keySet()); }
	
	public Set<SnOrXy> getDeviceIds(PageHeader hdr) 
	{
		Map<SnOrXy, Map<TestID, TestRecord>> m = devMap.get(hdr);
		if (m == null) return(new LinkedHashSet<SnOrXy>());
		return(m.keySet());
	}
	
	public Set<TestID> getTestIds(PageHeader hdr)
	{
		Map<TestID, Map<SnOrXy, TestRecord>> m = testMap.get(hdr);
		if (m == null) return(new LinkedHashSet<TestID>());
		return(m.keySet());
	}

	public Set<TestID> getTestIds(PageHeader hdr, SnOrXy snxy)
	{
		Map<SnOrXy, Map<TestID, TestRecord>> m1 = devMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<TestID>());
		Map<TestID, TestRecord> m2 = m1.get(snxy);
		if (m2 == null) return(new LinkedHashSet<TestID>());
		return(m2.keySet());
	}

	public Set<SnOrXy> getDeviceIds(PageHeader hdr, TestID id)
	{
		Map<TestID, Map<SnOrXy, TestRecord>> m1 = testMap.get(hdr);
		if (m1 == null) return(new LinkedHashSet<SnOrXy>());
		Map<SnOrXy, TestRecord> m2 = m1.get(id);
		if (m2 == null) return(new LinkedHashSet<SnOrXy>());
		return(m2.keySet());
	}
	
}
