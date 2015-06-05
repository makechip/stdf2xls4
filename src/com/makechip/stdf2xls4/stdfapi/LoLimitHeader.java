package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class LoLimitHeader extends TestHeader
{

	public LoLimitHeader(TestID id)
	{
		super(id);
	}
	
	@Override
	public String getTestName()
	{
		return(id.testName + "_LoLimit");
	}
	
    @Override
    public boolean equals(Object o)
    {
    	if (o instanceof LoLimitHeader)
    	{
    		LoLimitHeader l = LoLimitHeader.class.cast(o);
    		if (id.testNumber != l.getTestNumber()) return(false);
    		return(getTestName().equals(l.getTestName()));
    	}
    	return(false);
    }
    
    @Override
    public int hashCode()
    {
    	return(id.hashCode() ^ "_LoLimit".hashCode());
    }

}
