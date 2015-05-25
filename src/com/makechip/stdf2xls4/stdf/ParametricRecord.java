package com.makechip.stdf2xls4.stdf;

import java.util.Collections;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

public abstract class ParametricRecord extends TestRecord 
{
	public final Set<TestFlag_t> testFlags;
	public final Set<ParamFlag_t> paramFlags;
	
	protected ParametricRecord(Record_t type, int sequenceNumber, int devNum, byte[] data)
	{
		super(type, sequenceNumber, devNum, data);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(getByte()));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(getByte()));
		
	}
	
	protected ParametricRecord(Record_t type, 
			                   int sequenceNumber, 
			                   int devNum, 
			                   long testNumber, 
			                   short headNumber, 
			                   short siteNumber,
			                   byte testFlags,
			                   byte paramFlags)
	{
		super(type, sequenceNumber, devNum, testNumber, headNumber, siteNumber);
		this.testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(testFlags));
		this.paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(paramFlags));
	}
	
	public abstract String getTestName();
	
	abstract void setTestName(String testName);
	
	public abstract String getAlarmName();
	
	public abstract Set<OptFlag_t> getOptFlags();
	
	public abstract byte getResScal();
	
	public abstract byte getLlmScal();
	
	public abstract byte getHlmScal();
	
	public abstract float getLoLimit();
	
	public abstract float getHiLimit();
	
	public abstract String getUnits();
	

}
