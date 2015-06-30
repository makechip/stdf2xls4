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

/**
 * This class is the superclass of the ParametricTestRecord and the MultipleResultParametricRecord.
 * It is is just used to group some of the fields that are common to both record types.
 * @author eric
 */
public abstract class ParametricRecord extends TestRecord 
{
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
	protected ParametricRecord(Record_t type, Cpu_t cpuType, byte[] data)
	{
		super(type, cpuType, data);
		getU4(MISSING_LONG); // skip over test number;
		headNumber = getU1(MISSING_SHORT);
		siteNumber = getU1(MISSING_SHORT);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(getByte()));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(getByte()));
		
	}
	
	/**
	 * Helper function for setting a byte value, and getting/setting the default value.
	 * @param missingValue  Value to use if the field is missing from the binary stream.
	 * @param streamValue The value from the stream.
	 * @param id The test ID; used to locate the default value.
	 * @param map The default value map.
	 * @return The byte value which will either be the stream value, or the value from the default value map.
	 */
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
	
	/**
	 * Helper function for setting a float value, and getting/setting the default value.
	 * @param missingValue  Value to use if the field is missing from the binary stream.
	 * @param streamValue The value from the stream.
	 * @param id The test ID; used to locate the default value.
	 * @param map The default value map.
	 * @return The byte value which will either be the stream value, or the value from the default value map.
	 */
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
	
	/**
	 * Helper function for setting a String value, and getting/setting the default value.
	 * @param missingValue  Value to use if the field is missing from the binary stream.
	 * @param streamValue The value from the stream.
	 * @param id The test ID; used to locate the default value.
	 * @param map The default value map.
	 * @return The byte value which will either be the stream value, or the value from the default value map.
	 */
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

	/**
	 * Function used to determine scaled values.
	 * @param value  The value to scale so that it can be displayed in a "0.000" format.
	 * @param scale The value obtained from the findScale() method.
	 * @return  The scaled value.
	 */
    protected float scaleValue(float value, int scale)
    {
        if (value == MISSING_FLOAT) return(value);
        switch (scale)
        {
        case -9: value /= 1E9f; break;
        case -6: value /= 1E6f; break;
        case -3: value /= 1E3f; break;
        case  3: value *= 1E3f; break;
        case  6: value *= 1E6f; break;
        case  9: value *= 1E9f; break;
        case 12: value *= 1E12f; break;
        default:
        }
        return(value);
    }
   
    /**
     * This function scales units.  It adds one of the following prefixes
     * to the units value: p, n, i, m, k, M, G.
     * @param units The units value from the UNITS field.
     * @param scale  The scale value from the findScale() method.
     * @return The scaled units String.
     */
    protected String scaleUnits(String units, int scale)
    {
        String u = units;
        switch (scale)
        {
        case -9: u = "G" + units; break;
        case -6: u = "M" + units; break;
        case -3: u = "k" + units; break;
        case  3: u = "m" + units; break;
        case  6: u = "u" + units; break;
        case  9: u = "n" + units; break;
        case 12: u = "p" + units; break;
        default:
        } 
        return(u);
    }

    /**
     * This method finds the scale magnitude based upon the upper and lower
     * limits used by the first instance of this record.
     * @param dvd The defaultValueDatabase.
     * @return The scale exponent.
     */
    protected int findScale(DefaultValueDatabase dvd, TestID id)
    {
        float llim = (float) Math.abs(dvd.loLimDefaults.get(id));
        float hlim = (float) Math.abs(dvd.hiLimDefaults.get(id));
        float val = 0.0f;
        if (getOptFlags().contains(OptFlag_t.NO_LO_LIMIT)) val = hlim;
        else if (getOptFlags().contains(OptFlag_t.NO_HI_LIMIT)) val = llim;
        else val = (hlim > llim) ? hlim : llim;
        int scale = 0;
        if (val <= 1.0E-6f) scale = 9;
        else if (val <= 0.001f) scale = 6;
        else if (val <= 1.0f) scale = 3;
        else if (val <= 1000.0f) scale = 0;
        else if (val <= 1000000.0f) scale = -3;
        else if (val <= 1E9f) scale = -6;
        else scale = -9;
        return(scale);
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
	public abstract byte getResScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getLlmScal()
	 */
	public abstract byte getLlmScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getHlmScal()
	 */
	public abstract byte getHlmScal();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getLoLimit()
	 */
	public abstract float getLoLimit();
	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getHiLimit()
	 */
	public abstract float getHiLimit();
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + headNumber;
		result = prime * result + paramFlags.hashCode();
		result = prime * result + siteNumber;
		result = prime * result + testFlags.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ParametricRecord)) return false;
		ParametricRecord other = (ParametricRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (!paramFlags.equals(other.paramFlags)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!testFlags.equals(other.testFlags)) return false;
		return true;
	}


}
