/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4.stdf;

import java.util.Collections;
import java.util.Set;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

/**
 * This class is the superclass of the ParametricTestRecord and the MultipleResultParametricRecord.
 * It is is just used to group some of the fields that are common to both record types.
 * @author eric
 */
public abstract class ParametricRecord extends TestRecord 
{
	/**
	 *  This is the TEST_NUM field.
	 */
	public final long testNumber;
	/**
	 *  This is the HEAD_NUM field of the ParametricTestRecord and MultipleResultParametricRecord.
	 */
	public final short headNumber;
	/**
	 *  This is the SITE_NUM field of the ParametricTestRecord and MultipleResultParametricRecord.
	 */
	public final short siteNumber;
	/**
	 *  This is the TEST_FLG field of the ParametricTestRecord and MultipleResultParametricRecord.
	 */
	public final Set<TestFlag_t> testFlags;
	/**
	 *  This is the PARM_FLG field of the ParametricTestRecord and MultipleResultParametricRecord.
	 */
	public final Set<ParamFlag_t> paramFlags;
	
	/**
	 * This is the constructor for initializing the class with binary data.
	 * @param type  The record type.
	 * @param cpuType  The CPU type.
	 * @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
	 */
	protected ParametricRecord(Cpu_t cpu, int recLen, ByteInputStream is)
	{
		super();
		testNumber = cpu.getU4(is); // skip over test number;
		headNumber = cpu.getU1(is);
		siteNumber = cpu.getU1(is);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(cpu.getI1(is)));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(cpu.getI1(is)));
		
	}
	
	protected ParametricRecord(long testNumber, short headNumber, short siteNumber, byte testFlags, byte paramFlags)
	{
		this.testNumber = testNumber;
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
		this.testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(testFlags));
		this.paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(paramFlags));
	}
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getAlarmName()
	 */
	public abstract String getAlarmName();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getOptFlags()
	 */
	public abstract Set<OptFlag_t> getOptFlags();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getResScal()
	 */
	public abstract Byte getResScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getLlmScal()
	 */
	public abstract Byte getLlmScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getHlmScal()
	 */
	public abstract Byte getHlmScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getLoLimit()
	 */
	public abstract Float getLoLimit();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getHiLimit()
	 */
	public abstract Float getHiLimit();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getUnits()
	 */
	public abstract String getUnits();
	
	/**
	 * Determines if this record has a lo limit.
	 * @return True if optFlags does not contain NO_LO_LIMIT and does not contain LO_LIMIT_LLM_SCAL_INVALID.
	 */
	public boolean hasLoLimit()
	{
		return(!getOptFlags().contains(OptFlag_t.NO_LO_LIMIT) && !getOptFlags().contains(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID));
	}
	
	/**
	 * Determines if this record has a hi limit.
	 * @return True if optFlags does not contain NO_HI_LIMIT and does not contain HI_LIMIT_HLM_SCAL_INVALID.
	 */
	public boolean hasHiLimit()
	{
		return(!getOptFlags().contains(OptFlag_t.NO_HI_LIMIT) && !getOptFlags().contains(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID));
	}
	
	public abstract TestID getTestID();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + headNumber;
		result = prime * result + paramFlags.hashCode();
		result = prime * result + siteNumber;
		result = prime * result + testFlags.hashCode();
		result = prime * result + (int) (testNumber ^ (testNumber >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ParametricRecord)) return false;
		ParametricRecord other = (ParametricRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (!paramFlags.equals(other.paramFlags)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!testFlags.equals(other.testFlags)) return false;
		if (testNumber != other.testNumber) return false;
		return true;
	}


}
