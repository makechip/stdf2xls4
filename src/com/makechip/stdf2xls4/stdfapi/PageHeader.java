package com.makechip.stdf2xls4.stdfapi;

import java.util.Map;
import java.util.Set;
import com.makechip.util.Log;

public class PageHeader
{
	private final Map<String, String> hdrMap;

	public PageHeader(Map<String, String> hdr)
	{
		hdrMap = hdr;
	}
	
	public String get(String name)
	{
		return(hdrMap.get(name));
	}
	
	public boolean contains(String name)
	{
		return(hdrMap.containsKey(name));
	}
	
	public int getNumFields()
	{
		return(hdrMap.size());
	}
	
	public Set<String> getNames()
	{
		return(hdrMap.keySet());
	}
	
	void setDates(String startDate, String stopDate)
	{
	    hdrMap.put("START_DATE", startDate);
	    hdrMap.put("STOP_DATE", stopDate);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		hdrMap.entrySet().stream().forEach(e -> sb.append(e.toString()).append(Log.eol));
		sb.append(Log.eol);
		return(sb.toString());
	}
	
	@Override
    public boolean equals(Object o)
    {
    	if (o instanceof PageHeader)
    	{
    		PageHeader p = PageHeader.class.cast(o);
    		return(hdrMap.equals(p.hdrMap));
    	}
    	return(false);
    }
    
    @Override
    public int hashCode()
    {
    	return(hdrMap.hashCode());
    }
}
