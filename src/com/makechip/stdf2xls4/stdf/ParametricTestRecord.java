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

import gnu.trove.list.array.TByteArrayList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import static com.makechip.stdf2xls4.stdf.enums.OptFlag_t.*;
/**
 *  This class holds the fields the a Parametric Test Record.
 *  @author eric
 */
public class ParametricTestRecord extends ParametricRecord
{
	/**
	 *  This is the RESULT field
	 */
    public final float result; 
    /**
     *  This is the ALARM_ID field
     */
    public final String alarmName;
    /**
     *  This is the OPT_FLAG field
     */
    public final Set<OptFlag_t> optFlags; 
    /**
     *  This is the RES_SCAL field
     */
    public final byte resScal;
    /**
     *  This is the LLM_SCAL field
     */
    public final byte llmScal;
    /**
     *  This is the HLM_SCAL field
     */
    public final byte hlmScal; 
    /**
     *  This is the LO_LIMIT field
     */
    public final float loLimit; 
    /**
     *  This is the HI_LIMIT field
     */
    public final float hiLimit;
    /**
     *  This is the UNITS field
     */
    public final String units;
    /**
     *  This is the C_RESFMT field
     */
    public final String resFmt;
    /**
     *  This is the C_LLMFMT field
     */
    public final String llmFmt;
    /**
     *  This is the C_HLMFMT field
     */
    public final String hlmFmt;
    /**
     *  This is the LO_SPEC field
     */
    public final float loSpec;
    /**
     *  This is the HI_SPEC field
     */
    public final float hiSpec;
    /**
     *  This field holds the TEST_NUM and TEST_TXT fields. 
     */
    public final TestID id;
	/**
	 *  This field holds a normalized LO_LIMIT such that
	 *  the value in conjunction with the scaledUnits
	 *  does not need to use scientific notation. It
	 *  is not part of the STDF specification
	 */
    public final float scaledLoLimit;
	/**
	 *  This field holds a normalized HI_LIMIT such that
	 *  the value in conjunction with the scaledUnits
	 *  does not need to use scientific notation. It
	 *  is not part of the STDF specification
	 */
    public final float scaledHiLimit;
	/**
	 *  This field holds a normalized value of the UNITS
	 *  field.  It should be used with the scaledLoLimit,
	 *  the scaledHiLimit, and the scaledResults.  This is
	 *  not part of the STDF specification.
	 */
    public final String scaledUnits;
    /**
     *  This field holds a normalized value of the RESULT
     *  field.  It should be used with the scaledLoLimit, the
     *  scaledHiLimit, and the scaledUnits. This is not part
     *  of the STDF specification.
     */
    public final float scaledResult;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This parameter is used for tracking the Test ID.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public ParametricTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
    	super(Record_t.PTR, dvd.getCpuType(), data);
    	long testNumber = dvd.getCpuType().getU4(data[0], data[1], data[2], data[3]);
        result = getR4(MISSING_FLOAT);
        String testName = getCn(); 
        id = TestID.createTestID(tdb, testNumber, testName); 
        alarmName = getCn();
        byte oflags = getByte();
        if (oflags != MISSING_BYTE)
        {
        	optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(oflags));
        	if (dvd.optDefaults.get(id) == null) dvd.optDefaults.put(id, optFlags);
        }
        else
        {
        	optFlags = dvd.optDefaults.get(id);
        }
        resScal = setByte(MISSING_BYTE, getI1(MISSING_BYTE), id, dvd.resScalDefaults);
        llmScal = setByte(MISSING_BYTE, getI1(MISSING_BYTE), id, dvd.llmScalDefaults);
        hlmScal = setByte(MISSING_BYTE, getI1(MISSING_BYTE), id, dvd.hlmScalDefaults);
        if (optFlags != null && optFlags.contains(OptFlag_t.NO_LO_LIMIT))
        {
        	loLimit = MISSING_FLOAT;
        	getR4(MISSING_FLOAT);
        }
        else loLimit = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loLimDefaults);
        if (optFlags != null && optFlags.contains(OptFlag_t.NO_HI_LIMIT))
        {
        	hiLimit = MISSING_FLOAT;
        	getR4(MISSING_FLOAT);
        }
        else hiLimit = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiLimDefaults);
        units = setString(MISSING_STRING, getCn(), id , dvd.unitDefaults);
        
        // scale limits and units here:
        if (dvd.scaledLoLimits.get(id) == MISSING_FLOAT)
        {
            scaledLoLimit = scaleValue(loLimit, findScale(dvd, id));	
            dvd.scaledLoLimits.put(id, scaledLoLimit);
        }
        else scaledLoLimit = dvd.scaledLoLimits.get(id);
        if (dvd.scaledHiLimits.get(id) == MISSING_FLOAT)
        {
        	scaledHiLimit = scaleValue(hiLimit, findScale(dvd, id));
        	dvd.scaledHiLimits.put(id, scaledHiLimit);
        }
        else scaledHiLimit = dvd.scaledHiLimits.get(id);
        if (dvd.scaledUnits.get(id) == null)
        {
        	scaledUnits = scaleUnits(units, findScale(dvd, id));
        	dvd.scaledUnits.put(id, scaledUnits);
        }
        else scaledUnits = dvd.scaledUnits.get(id);
        scaledResult = scaleValue(result, findScale(dvd, id));
        
        resFmt = setString(MISSING_STRING, getCn(), id , dvd.resFmtDefaults);
        llmFmt = setString(MISSING_STRING, getCn(), id , dvd.llmFmtDefaults);
        hlmFmt = setString(MISSING_STRING, getCn(), id , dvd.hlmFmtDefaults);
        loSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loSpecDefaults);
        hiSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiSpecDefaults);
    }
    
    /**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param tdb The TestIdDatabase is needed to get the TestID.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     * @param paramFlags The PARM_FLG field.
     * @param result     The RESULT field.
     * @param testName   The TEST_TXT field.
     * @param alarmName  The ALARM_ID field.
     * @param optFlags   The OPT_FLAG field.
     * @param resScal    The RES_SCAL field.
     * @param llmScal    The LLM_SCAL field.
     * @param hlmScal    The HLM_SCAL field.
     * @param loLimit    The LO_LIMIT field.
     * @param hiLimit    The HI_LIMIT field.
     * @param units      The UNITS field.
     * @param resFmt     The C_RESFMT field.
     * @param llmFmt     The C_LLMFMT field.
     * @param hlmFmt     The C_HLMFMT field.
     * @param loSpec     The LO_SPEC field.
     * @param hiSpec     The HI_SPEC field.
     */
    public ParametricTestRecord(
            final TestIdDatabase tdb,
            final DefaultValueDatabase dvd,
            final long testNumber,
            final short headNumber,
            final short siteNumber,
            final byte testFlags,
            final byte paramFlags,
    	    final float result, 
    	    final String testName,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
    	    final byte resScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final byte llmScal,
    	    final byte hlmScal, 
    	    final float loLimit, 
    	    final float hiLimit,
    	    final String units,
    	    final String resFmt,
    	    final String llmFmt,
    	    final String hlmFmt,
    	    final float loSpec,
    	    final float hiSpec)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), testNumber, headNumber, siteNumber, testFlags, 
    		 paramFlags, result, testName, alarmName, optFlags, resScal, llmScal, hlmScal, 
    		 loLimit, hiLimit, units, resFmt, llmFmt, hlmFmt, loSpec, hiSpec));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
    @Override
    protected void toBytes()
    {
    	bytes = toBytes(
            cpuType,
            id.testNumber,
            headNumber,
            siteNumber,
            (byte) testFlags.stream().mapToInt(b -> b.bit).sum(),
            (byte) paramFlags.stream().mapToInt(b -> b.bit).sum(),
    	    result, 
    	    id.testName,
    	    alarmName,
    	    optFlags, 
    	    resScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    llmScal,
    	    hlmScal, 
    	    loLimit, 
    	    hiLimit,
    	    units,
    	    resFmt,
    	    llmFmt,
    	    hlmFmt,
    	    loSpec,
    	    hiSpec);
    }
    
    private static byte[] toBytes(
            final Cpu_t cType,
            final long tNumber,
            final short hNumber,
            final short sNumber,
            final byte tFlags,
            final byte pFlags,
    	    final float rslt, 
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final byte rScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final byte lScal,
    	    final byte hScal, 
    	    final float lLimit, 
    	    final float hLimit,
    	    final String uts,
    	    final String rFmt,
    	    final String lFmt,
    	    final String hFmt,
    	    final float lSpec,
    	    final float hSpec)
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(cType.getU4Bytes(tNumber));
        list.addAll(getU1Bytes(hNumber));
        list.addAll(getU1Bytes(sNumber));
        list.add(tFlags);
        list.add(pFlags);
        list.addAll(cType.getR4Bytes((float) rslt));
        list.addAll(getCnBytes(tName));
        list.addAll(getCnBytes(aName));
        if (oFlags != null)
        {
        	list.add((byte) oFlags.stream().mapToInt(b -> b.bit).sum());
            list.addAll(getI1Bytes(rScal));
            list.addAll(getI1Bytes(lScal));
            list.addAll(getI1Bytes(hScal));
            list.addAll(cType.getR4Bytes(lLimit));
            list.addAll(cType.getR4Bytes(hLimit));
            list.addAll(getCnBytes(uts));
            list.addAll(getCnBytes(rFmt));
            list.addAll(getCnBytes(lFmt));
            list.addAll(getCnBytes(hFmt));
            list.addAll(cType.getR4Bytes(lSpec));
            list.addAll(cType.getR4Bytes(hSpec));
        }
        return(list.toArray());
    }
    	    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getAlarmName()
	 */
	@Override
	public String getAlarmName()
	{
		return alarmName;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getOptFlags()
	 */
	@Override
	public Set<OptFlag_t> getOptFlags()
	{
		return optFlags;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getResScal()
	 */
	@Override
	public byte getResScal()
	{
		return resScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getLlmScal()
	 */
	@Override
	public byte getLlmScal()
	{
		if (optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) return(MISSING_BYTE);
		return llmScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getHlmScal()
	 */
	@Override
	public byte getHlmScal()
	{
		if (optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) return(MISSING_BYTE);
		return hlmScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getLoLimit()
	 */
	@Override
	public float getLoLimit()
	{
		if (optFlags.contains(NO_LO_LIMIT) || optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) return(MISSING_FLOAT);
		return loLimit;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getHiLimit()
	 */
	@Override
	public float getHiLimit()
	{
		if (optFlags.contains(NO_HI_LIMIT) || optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) return(MISSING_FLOAT);
		return hiLimit;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getUnits()
	 */
	@Override
	public String getUnits()
	{
		return units;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ParametricTestRecord [result=").append(result);
		builder.append(", alarmName=").append(alarmName);
		builder.append(", id=").append(id);
		builder.append(", optFlags=").append(optFlags);
		builder.append(", resScal=").append(resScal);
		builder.append(", llmScal=").append(llmScal);
		builder.append(", hlmScal=").append(hlmScal);
		builder.append(", loLimit=").append(loLimit);
		builder.append(", hiLimit=").append(hiLimit);
		builder.append(", units=").append(units);
		builder.append(", resFmt=").append(resFmt);
		builder.append(", llmFmt=").append(llmFmt);
		builder.append(", hlmFmt=").append(hlmFmt);
		builder.append(", loSpec=").append(loSpec);
		builder.append(", hiSpec=").append(hiSpec);
		builder.append(", scaledLoLimit=").append(scaledLoLimit);
		builder.append(", scaledHiLimit=").append(scaledHiLimit);
		builder.append(", scaledUnits=").append(scaledUnits);
		builder.append(", scaledResult=").append(scaledResult);
		builder.append(", testFlags=").append(testFlags);
		builder.append(", paramFlags=").append(paramFlags);
		builder.append(", headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + alarmName.hashCode();
		result = prime * result + Float.floatToIntBits(hiLimit);
		result = prime * result + Float.floatToIntBits(hiSpec);
		result = prime * result + hlmFmt.hashCode();
		result = prime * result + hlmScal;
		result = prime * result + id.hashCode();
		result = prime * result + llmFmt.hashCode();
		result = prime * result + llmScal;
		result = prime * result + Float.floatToIntBits(loLimit);
		result = prime * result + Float.floatToIntBits(loSpec);
		result = prime * result + optFlags.hashCode();
		result = prime * result + resFmt.hashCode();
		result = prime * result + resScal;
		result = prime * result + Float.floatToIntBits(this.result);
		result = prime * result + Float.floatToIntBits(scaledHiLimit);
		result = prime * result + Float.floatToIntBits(scaledLoLimit);
		result = prime * result + Float.floatToIntBits(scaledResult);
		result = prime * result + scaledUnits.hashCode();
		result = prime * result + units.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof ParametricTestRecord)) return false;
		ParametricTestRecord other = (ParametricTestRecord) obj;
		if (!alarmName.equals(other.alarmName)) return false;
		if (Float.floatToIntBits(hiLimit) != Float .floatToIntBits(other.hiLimit)) return false;
		if (Float.floatToIntBits(hiSpec) != Float.floatToIntBits(other.hiSpec)) return false;
		if (!hlmFmt.equals(other.hlmFmt)) return false;
		if (hlmScal != other.hlmScal) return false;
		if (id != other.id) return false;
		if (!llmFmt.equals(other.llmFmt)) return false;
		if (llmScal != other.llmScal) return false;
		if (Float.floatToIntBits(loLimit) != Float .floatToIntBits(other.loLimit)) return false;
		if (Float.floatToIntBits(loSpec) != Float.floatToIntBits(other.loSpec)) return false;
		if (!optFlags.equals(other.optFlags)) return false;
		if (!resFmt.equals(other.resFmt)) return false;
		if (resScal != other.resScal) return false;
		if (Float.floatToIntBits(result) != Float.floatToIntBits(other.result)) return false;
		if (!units.equals(other.units)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getTestId()
	 */
	@Override
	public TestID getTestId()
	{
		return(id);
	}
	


}