package com.makechip.stdf2xls4.stdf;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

public abstract class TestRecord extends StdfRecord 
{
	public final long testNumber;
	public final short headNumber;
	public final short siteNumber;
	
	protected TestRecord(Record_t type, Cpu_t cpuType, byte[] data)
	{
		super(type, cpuType, data);
		testNumber = getU4(MISSING_LONG);
		headNumber = getU1(MISSING_SHORT);
		siteNumber = getU1(MISSING_SHORT);
	}
	
	public abstract TestID getTestId();
	
	protected abstract void setTestName(String testName);
	
	protected abstract void setText(String text);
	
}
