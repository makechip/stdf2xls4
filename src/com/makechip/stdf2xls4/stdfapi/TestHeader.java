package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class TestHeader
{
	protected final TestID id;

	public TestHeader(TestID id)
	{
		this.id = id;
	}
	
	public String getTestName() { return(id.testName); }
	
	public long getTestNumber() { return(id.testNumber); }

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof TestHeader)
		{
			TestHeader t = TestHeader.class.cast(o);
			return(t.id == id);
		}
		return(false);
	}
	
	@Override
	public int hashCode()
	{
		return(id.hashCode());
	}
	
}
