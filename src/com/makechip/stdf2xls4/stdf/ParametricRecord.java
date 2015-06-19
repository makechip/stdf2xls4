package com.makechip.stdf2xls4.stdf;

import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

public abstract class ParametricRecord extends TestRecord 
{
	public final Set<TestFlag_t> testFlags;
	public final Set<ParamFlag_t> paramFlags;
	
	protected ParametricRecord(Record_t type, Cpu_t cpuType, byte[] data)
	{
		super(type, cpuType, data);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(getByte()));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(getByte()));
		
	}
	
	protected byte setByte(byte missingValue, byte streamValue, TestID id, TObjectByteHashMap<TestID> map)
	{
		byte rval = missingValue;
		if (streamValue != missingValue)
		{
			rval = streamValue;
			if (map.get(id) == missingValue) map.put(id, rval);
		}
		else rval = map.get(id);
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
		else rval = map.get(id);
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
		else rval = map.get(id);
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
	
	public boolean hasLoLimit()
	{
		if (getOptFlags() == null) return(true);
		return(!getOptFlags().contains(OptFlag_t.NO_LO_LIMIT) && !getOptFlags().contains(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID));
	}
	
	public boolean hasHiLimit()
	{
		if (getOptFlags() == null) return(true);
		return(!getOptFlags().contains(OptFlag_t.NO_HI_LIMIT) && !getOptFlags().contains(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID));
	}

}
