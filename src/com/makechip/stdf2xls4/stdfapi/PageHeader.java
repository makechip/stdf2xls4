package com.makechip.stdf2xls4.stdfapi;

import java.util.HashMap;
import java.util.stream.Stream;

import com.makechip.util.Log;

public class PageHeader
{
	private HashMap<String, String> hdrMap;

	public PageHeader()
	{
		hdrMap = new HashMap<>();
	}
	
	public void add(String name, String value)
	{
		hdrMap.put(name, value);
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
	
	public Stream<String> getNames()
	{
		return(hdrMap.keySet().stream());
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
