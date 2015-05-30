package com.makechip.stdf2xls4.stdf;

import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

public abstract class ParametricRecord extends TestRecord 
{
	public final Set<TestFlag_t> testFlags;
	public final Set<ParamFlag_t> paramFlags;
	
	protected ParametricRecord(Record_t type, int sequenceNumber, byte[] data)
	{
		super(type, sequenceNumber, data);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(getByte()));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(getByte()));
		
	}
	
	protected ParametricRecord(Record_t type, 
			                   int sequenceNumber, 
			                   long testNumber, 
			                   short headNumber, 
			                   short siteNumber,
			                   byte testFlags,
			                   byte paramFlags)
	{
		super(type, sequenceNumber, testNumber, headNumber, siteNumber);
		this.testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(testFlags));
		this.paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(paramFlags));
	}
	
	protected byte setByte(byte missingValue, byte streamValue, TestID id, TObjectByteHashMap<TestID> map)
	{
		byte rval = missingValue;
		if (streamValue != missingValue)
		{
			rval = streamValue;
			if (map.get(id) == missingValue) map.put(id, rval);
		}
		else
		{
			rval = map.get(id);
		}
		return(rval);
	}
	
	protected float setFloat(float missingValue, float streamValue, TestID id, TObjectFloatHashMap<TestID> map)
	{
		float rval = missingValue;
		if (streamValue != missingValue)
		{
			rval = streamValue;
			if (map.get(id) == missingValue) map.put(id, rval);
		}
		else
		{
			rval = map.get(id);
		}
		return(rval);
	}
	
	protected String setString(String missingValue, String streamValue, TestID id, IdentityHashMap<TestID, String> map)
	{
		String rval = missingValue;
		if (!streamValue.equals(missingValue))
		{
			rval = streamValue;
			if (map.get(id) == null) map.put(id, rval);
		}
		else
		{
			rval = map.get(id);
		}
		return(rval);
	}

	public abstract TestID getTestId();
	
	protected abstract void setTestName(String testName);
	
	public abstract String getAlarmName();
	
	public abstract Set<OptFlag_t> getOptFlags();
	
	public abstract byte getResScal();
	
	public abstract byte getLlmScal();
	
	public abstract byte getHlmScal();
	
	public abstract float getLoLimit();
	
	public abstract float getHiLimit();
	
	public abstract String getUnits();
	

}
