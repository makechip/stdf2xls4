package com.makechip.stdf2xls4.stdf;

import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

/**
 * This is the superclass for any StdfRecord that represents a test result.
 * @author eric
 *
 */
public abstract class TestRecord extends StdfRecord 
{
	/**
	 *  This is the parent class for all records that contain test results.
	 */
	protected TestRecord()
	{
		super();
	}
	
	public abstract long getTestNumber();
	
	public abstract String getTestName();
	
	public abstract Set<TestFlag_t> getTestFlags();
	
	public abstract TestID getTestID();

}
