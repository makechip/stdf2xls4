package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class HiLimitHeader extends TestHeader
{

	public HiLimitHeader(TestID id)
	{
		super(id);
	}
	
	@Override
	public String getTestName()
	{
		return(id.testName + "_HiLimit");
	}
	
    @Override
    public boolean equals(Object o)
    {
    	if (o instanceof HiLimitHeader)
    	{
    		HiLimitHeader l = HiLimitHeader.class.cast(o);
    		if (id.testNumber != l.getTestNumber()) return(false);
    		return(getTestName().equals(l.getTestName()));
    	}
    	return(false);
    }
    
    @Override
    public int hashCode()
    {
    	return(id.hashCode() ^ "_HiLimit".hashCode());
    }

}
