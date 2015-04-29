package com.makechip.stdf2xls4.stdf;

import java.util.EnumSet;

import com.makechip.util.Log;

class RequiredParametricFields extends RequiredTestFields
{
	private final EnumSet<ParamFlag_t> paramFlags;

	public RequiredParametricFields(StdfRecord rec)
	{
		super(rec);
		paramFlags = ParamFlag_t.getBits(rec.getByte());
	}

	public RequiredParametricFields(long testNumber, int headNumber, int siteNumber, 
			         EnumSet<TestFlag_t> testFlags, EnumSet<ParamFlag_t> paramFlags)
	{
		super(testNumber, headNumber, siteNumber, testFlags);
		this.paramFlags = EnumSet.noneOf(ParamFlag_t.class);
		for (ParamFlag_t p : paramFlags) this.paramFlags.add(p);
	}
	
	public EnumSet<ParamFlag_t> getParamFlags() { return(paramFlags); }
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("    paramFlags:");
		for (ParamFlag_t p : paramFlags)
		{
			sb.append(" ");
			sb.append(p.toString());
		}
		sb.append(Log.eol);
		return(sb.toString());
	}

}
