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
import gnu.trove.map.hash.TObjectFloatHashMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

import static com.makechip.stdf2xls4.stdf.enums.OptFlag_t.*;
/**
 *  This class holds the fields for a Multiple Result Parametric Record.
 *  @author eric
 */
public final class MultipleResultParametricRecord extends ParametricRecord
{
	private final TObjectFloatHashMap<String> rsltMap;
	private final TObjectFloatHashMap<String> scaledRsltMap;
	private TestID id;
	/**
	 *  This is the ALARM_ID field of the MultipleResultParametricRecord.
	 */
	public final String alarmName;
	/**
	 *  This is the OPT_FLAG field of the MultipleResultParametricRecord.
	 */
	public final Set<OptFlag_t> optFlags;
	/**
	 *  This is the RES_SCAL field of the MultipleResultParametricRecord.
	 */
	public final byte resScal;
	/**
	 *  This is the LLM_SCAL  field of the MultipleResultParametricRecord.
	 */
	public final byte llmScal;
	/**
	 *  This is the HLM_SCAL field of the MultipleResultParametricRecord.
	 */
	public final byte hlmScal;
	/**
	 *  This is the LO_LIMIT field of the MultipleResultParametricRecord.
	 */
	public final float loLimit;
	/**
	 *  This is the HI_LIMIT field of the MultipleResultParametricRecord.
	 */
	public final float hiLimit;
	/**
	 *  This is the START_IN field of the MultipleResultParametricRecord.
	 */
    public final float startIn;
	/**
	 *  This is the INCR_IN field of the MultipleResultParametricRecord.
	 */
    public final float incrIn;
    private final byte[] rtnState;
    private final int[] rtnIndex;
    private final float[] results;
	/**
	 *  This is the UNITS field of the MultipleResultParametricRecord.
	 */
    public final String units;
	/**
	 *  This is the UNITS_IN field of the MultipleResultParametricRecord.
	 */
    public final String unitsIn;
	/**
	 *  This is the C_RESFMT field of the MultipleResultParametricRecord.
	 */
    public final String resFmt;
	/**
	 *  This is the C_LLMFMT field of the MultipleResultParametricRecord.
	 */
    public final String llmFmt;
	/**
	 *  This is the C_HLMFMT field of the MultipleResultParametricRecord.
	 */
    public final String hlmFmt;
	/**
	 *  This is the LO_SPEC field of the MultipleResultParametricRecord.
	 */
    public final float loSpec;
	/**
	 *  This is the HI_SPEC field of the MultipleResultParametricRecord.
	 */
    public final float hiSpec;
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
	 *  the scaledHiLimit, and the scaledResults. 
	 */
    public final String scaledUnits;
    private final float[] scaledResults;
    
    @Override
    public boolean isTestRecord() { return(true); }
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This parameter is used for tracking the Test ID.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public MultipleResultParametricRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MPR, dvd.getCpuType(), data);
        int j = getU2(0);
        int k = getU2(0); 
        rtnState = getNibbles(j);
        results =  new float[k];
        rsltMap = new TObjectFloatHashMap<>(10, 0.7f, MISSING_FLOAT);
        scaledRsltMap = new TObjectFloatHashMap<>(10, 0.7f, MISSING_FLOAT);
        for (int i=0; i<results.length; i++) results[i] = getR4(-Float.MAX_VALUE);
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
        if (optFlags.contains(OptFlag_t.NO_LO_LIMIT))
        {
        	loLimit = MISSING_FLOAT;
        	getR4(MISSING_FLOAT);
        }
        else loLimit = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loLimDefaults);
        if (optFlags.contains(OptFlag_t.NO_HI_LIMIT))
        {
        	hiLimit = MISSING_FLOAT;
        	getR4(MISSING_FLOAT);
        }
        else hiLimit = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiLimDefaults);
        startIn = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.startInDefaults);     
        incrIn  = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.incrInDefaults);     
        if (j != 0)
        {
            rtnIndex = new int[j];
        	Arrays.setAll(rtnIndex, p -> getU2(MISSING_INT));
        	if (dvd.rtnIndexDefaults.get(id) == null) dvd.rtnIndexDefaults.put(id, rtnIndex);
        }
        else
        {
        	rtnIndex = dvd.rtnIndexDefaults.get(id);
        }
        int n = 0;
        for (int i : rtnIndex)
        {
      	    String pin = (dvd.isFusionCx()) ? dvd.getPhysicalPinName(siteNumber, i) : dvd.getChannelName(siteNumber, i);
      	    rsltMap.put(pin, results[n]);
      	    n++;
        }
        units = setString(MISSING_STRING, getCn(), id , dvd.unitDefaults);
        // scale limits and units here:
        if (dvd.scaledLoLimits.get(id) == MISSING_FLOAT)
        {
            scaledLoLimit = scaleValue(loLimit, findScale(dvd));	
            dvd.scaledLoLimits.put(id, scaledLoLimit);
        }
        else scaledLoLimit = dvd.scaledLoLimits.get(id);
        if (dvd.scaledHiLimits.get(id) == MISSING_FLOAT)
        { 
        	scaledHiLimit = scaleValue(hiLimit, findScale(dvd));
        	dvd.scaledHiLimits.put(id, scaledHiLimit);
        }
        else scaledHiLimit = dvd.scaledHiLimits.get(id);
        if (dvd.scaledUnits.get(id) == null)
        {
        	scaledUnits = scaleUnits(units, findScale(dvd));
        	dvd.scaledUnits.put(id, scaledUnits);
        }
        else scaledUnits = dvd.scaledUnits.get(id);
        scaledResults = new float[results.length];
        for (int i=0; i<results.length; i++)
        {
            scaledResults[i] = scaleValue(results[i], findScale(dvd));
        }
        n = 0;
        for (int i : rtnIndex)
        {
      	    String pin = (dvd.isFusionCx()) ? dvd.getPhysicalPinName(siteNumber, i) : dvd.getChannelName(siteNumber, i);
      	    scaledRsltMap.put(pin, scaledResults[n]);
      	    n++;
        }
        unitsIn = setString(MISSING_STRING, getCn(), id , dvd.unitsInDefaults);
        resFmt = setString(MISSING_STRING, getCn(), id , dvd.resFmtDefaults);
        llmFmt = setString(MISSING_STRING, getCn(), id , dvd.llmFmtDefaults);
        hlmFmt = setString(MISSING_STRING, getCn(), id , dvd.hlmFmtDefaults);
        loSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loSpecDefaults);
        hiSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiSpecDefaults);
    }
    
    /**
     * This constructor is used to make a MultipleResultParametricRecord with field values. 
     * @param tdb The TestIdDatabase is needed to get the TestID.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     * @param paramFlags The PARM_FLG field.
     * @param rtnState   The RTN_STAT field.
     * @param results    The RTN_RSLT field.
     * @param testName   The TEST_TXT field.
     * @param alarmName  The ALARM_ID field.
     * @param optFlags   The OPT_FLAG field.
     * @param resScal    The RES_SCAL field.
     * @param llmScal    The LLM_SCAL field.
     * @param hlmScal    The HLM_SCAL field.
     * @param loLimit    The LO_LIMIT field.
     * @param hiLimit    The HI_LIMIT field.
     * @param startIn    The START_IN field.
     * @param incrIn     The INCR_IN field.
     * @param rtnIndex   The RTN_INDX field.
     * @param units      The UNITS field.
     * @param unitsIn    The UNITS_IN field.
     * @param resFmt     The C_RESFMT field.
     * @param llmFmt     The C_LLMFMT field.
     * @param hlmFmt     The C_HLMFMT field.
     * @param loSpec     The LO_SPEC field.
     * @param hiSpec     The HI_SPEC field.
     */
    public MultipleResultParametricRecord(
            final TestIdDatabase tdb,
            final DefaultValueDatabase dvd,
            final long testNumber,
            final short headNumber,
            final short siteNumber,
            final byte testFlags,
            final byte paramFlags,
            final byte[] rtnState,
            final float[] results,
    	    final String testName,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
    	    final byte resScal, 
    	    final byte llmScal,
    	    final byte hlmScal, 
    	    final float loLimit, 
    	    final float hiLimit,
    	    final float startIn,
    	    final float incrIn,
    	    final int[] rtnIndex,
    	    final String units,
    	    final String unitsIn,
    	    final String resFmt,
    	    final String llmFmt,
    	    final String hlmFmt,
    	    final float loSpec,
    	    final float hiSpec)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), testNumber, headNumber, siteNumber, testFlags, 
    			paramFlags, rtnState, results, testName, alarmName, optFlags, 
    			resScal, llmScal, hlmScal, loLimit, hiLimit, startIn, incrIn, rtnIndex, 
    			units, unitsIn, resFmt, llmFmt, hlmFmt, loSpec, hiSpec));
    }
    
    @Override
    protected void toBytes()
    {
    	bytes = toBytes(
    			cpuType,
                testNumber,
                headNumber,
                siteNumber,
                (byte) testFlags.stream().mapToInt(b -> b.getBit()).sum(),
                (byte) paramFlags.stream().mapToInt(b -> b.getBit()).sum(),
                rtnState,
                results,
                id.testName,
        	    alarmName,
        	    optFlags, 
        	    resScal, 
        	    llmScal,
        	    hlmScal, 
        	    loLimit, 
        	    hiLimit,
        	    startIn,
        	    incrIn,
        	    rtnIndex,
        	    units,
        	    unitsIn,
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
            final byte[] rState,
            final float[] rslts,
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final byte rScal,
    	    final byte lScal,
    	    final byte hScal, 
    	    final float lLimit, 
    	    final float hLimit,
    	    final float sIn,
    	    final float iIn,
    	    final int[] rIndex,
    	    final String uts,
    	    final String utsIn,
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
        list.addAll(cType.getU2Bytes(rState.length));
        list.addAll(cType.getU2Bytes(rslts.length));
        list.addAll(getNibbleBytes(rState));
        for (int i=0; i<rslts.length; i++) list.addAll(cType.getR4Bytes(rslts[i]));
        list.addAll(getCnBytes(tName));
        list.addAll(getCnBytes(aName));
        if (oFlags != null)
        {
        	list.add((byte) oFlags.stream().mapToInt(b -> b.getBit()).sum());
            list.addAll(getI1Bytes(rScal));
            list.addAll(getI1Bytes(lScal));
            list.addAll(getI1Bytes(hScal));
            list.addAll(cType.getR4Bytes(lLimit));
            list.addAll(cType.getR4Bytes(hLimit));
            list.addAll(cType.getR4Bytes(sIn));
            list.addAll(cType.getR4Bytes(iIn));
            Arrays.stream(rIndex).forEach(p -> list.addAll(cType.getU2Bytes(p)));
            list.addAll(getCnBytes(uts));
            list.addAll(getCnBytes(utsIn));
            list.addAll(getCnBytes(rFmt));
            list.addAll(getCnBytes(lFmt));
            list.addAll(getCnBytes(hFmt));
            list.addAll(cType.getR4Bytes(lSpec));
            list.addAll(cType.getR4Bytes(hSpec));
        }
        return(list.toArray());
    }
    
    /**
     * Get a copy of the RTN_STAT array.
     * @return A deep copy of the RTN_STAT array.
     */
    public final byte[] getRtnState() { return(Arrays.copyOf(rtnState, rtnState.length)); }
    
    /**
     * Get a copy of the RTN_RSLT array.
     * @return A deep copy of the RTN_RSLT array.
     */
    public final float[] getResults() { return(Arrays.copyOf(results, results.length)); }
    
    /**
     * Get a copy of the array of PMR indexes.
     * @return A deep copy of the RTN_INDX array.
     */
    public final int[] getRtnIndex() { return(Arrays.copyOf(rtnIndex, rtnIndex.length)); }
    
    /**
     * Get the pin names indicated by the RTN_INDX array.
     * @return A stream containing the names of the pins tested.
     */
    public Stream<String> getPinNames() { return(rsltMap.keySet().stream()); }
    
    /**
     * Get the result for a specific pin.
     * @param pinName The name of a pin.
     * @return The result for the specified pin, or StdfRecord.MISSING_FLOAT if
     * the pin name is invalid.
     */
    public float getResult(String pinName) { return(rsltMap.get(pinName)); }
    
    /**
     * Get the scaled result for a specific pin.  The scaled result is a normalized
     * result that should be used in conjunction with the scaled units.  For example,
     * if the result was 0.000001A, then the scaled result and scaled units would be 1.0uA.
     * @param pinName The name of a pin.
     * @return The scaled result for the specified pin, or StdfRecord.MISSING_FLOAT if
     * the pin name is invalid.
     */
    public float getScaledResult(String pinName) { return(scaledRsltMap.get(pinName)); }

	@Override
	public TestID getTestId()
	{
		return(id);
	}

	@Override
	public String getAlarmName()
	{
		return alarmName;
	}

	@Override
	public Set<OptFlag_t> getOptFlags()
	{
		return optFlags;
	}

	@Override
	public byte getResScal()
	{
		return resScal;
	}

	@Override
	public byte getLlmScal()
	{
		if (optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) return(MISSING_BYTE);
		return llmScal;
	}

	@Override
	public byte getHlmScal()
	{
		if (optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) return(MISSING_BYTE);
		return hlmScal;
	}

	@Override
	public float getLoLimit()
	{
		if (optFlags.contains(NO_LO_LIMIT) || optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) return(MISSING_FLOAT);
		return loLimit;
	}

	@Override
	public float getHiLimit()
	{
		if (optFlags.contains(NO_HI_LIMIT) || optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) return(MISSING_FLOAT);
		return hiLimit;
	}

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
		builder.append("MultipleResultParametricRecord [rsltMap=").append(rsltMap);
		builder.append(", scaledRsltMap=").append(scaledRsltMap);
		builder.append(", id=").append(id);
		builder.append(", alarmName=").append(alarmName);
		builder.append(", optFlags=").append(optFlags);
		builder.append(", resScal=").append(resScal);
		builder.append(", llmScal=").append(llmScal);
		builder.append(", hlmScal=").append(hlmScal);
		builder.append(", loLimit=").append(loLimit);
		builder.append(", hiLimit=").append(hiLimit);
		builder.append(", startIn=").append(startIn);
		builder.append(", incrIn=").append(incrIn);
		builder.append(", rtnState=").append(Arrays.toString(rtnState));
		builder.append(", rtnIndex=").append(Arrays.toString(rtnIndex));
		builder.append(", results=").append(Arrays.toString(results));
		builder.append(", units=").append(units);
		builder.append(", unitsIn=").append(unitsIn);
		builder.append(", resFmt=").append(resFmt);
		builder.append(", llmFmt=").append(llmFmt);
		builder.append(", hlmFmt=").append(hlmFmt);
		builder.append(", loSpec=").append(loSpec);
		builder.append(", hiSpec=").append(hiSpec);
		builder.append(", scaledLoLimit=").append(scaledLoLimit);
		builder.append(", scaledHiLimit=").append(scaledHiLimit);
		builder.append(", scaledUnits=").append(scaledUnits);
		builder.append(", scaledResults=").append(Arrays.toString(scaledResults));
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
		result = prime * result + Float.floatToIntBits(incrIn);
		result = prime * result + llmFmt.hashCode();
		result = prime * result + llmScal;
		result = prime * result + Float.floatToIntBits(loLimit);
		result = prime * result + Float.floatToIntBits(loSpec);
		result = prime * result + optFlags.hashCode();
		result = prime * result + resFmt.hashCode();
		result = prime * result + resScal;
		result = prime * result + Arrays.hashCode(results);
		result = prime * result + rsltMap.hashCode();
		result = prime * result + Arrays.hashCode(rtnIndex);
		result = prime * result + Arrays.hashCode(rtnState);
		result = prime * result + Float.floatToIntBits(scaledHiLimit);
		result = prime * result + Float.floatToIntBits(scaledLoLimit);
		result = prime * result + Arrays.hashCode(scaledResults);
		result = prime * result + scaledRsltMap.hashCode();
		result = prime * result + scaledUnits.hashCode();
		result = prime * result + Float.floatToIntBits(startIn);
		result = prime * result + units.hashCode();
		result = prime * result + unitsIn.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof MultipleResultParametricRecord)) return false;
		MultipleResultParametricRecord other = (MultipleResultParametricRecord) obj;
		if (!alarmName.equals(other.alarmName)) return false;
		if (Float.floatToIntBits(hiLimit) != Float.floatToIntBits(other.hiLimit)) return false;
		if (Float.floatToIntBits(hiSpec) != Float.floatToIntBits(other.hiSpec)) return false;
		if (!hlmFmt.equals(other.hlmFmt)) return false;
		if (hlmScal != other.hlmScal) return false;
		if (id != other.id) return false;
		if (Float.floatToIntBits(incrIn) != Float.floatToIntBits(other.incrIn)) return false;
		if (!llmFmt.equals(other.llmFmt)) return false;
		if (llmScal != other.llmScal) return false;
		if (Float.floatToIntBits(loLimit) != Float.floatToIntBits(other.loLimit)) return false;
		if (Float.floatToIntBits(loSpec) != Float.floatToIntBits(other.loSpec)) return false;
		if (!optFlags.equals(other.optFlags)) return false;
		if (!resFmt.equals(other.resFmt)) return false;
		if (resScal != other.resScal) return false;
		if (!Arrays.equals(results, other.results)) return false;
		if (!rsltMap.equals(other.rsltMap)) return false;
		if (!Arrays.equals(rtnIndex, other.rtnIndex)) return false;
		if (!Arrays.equals(rtnState, other.rtnState)) return false;
		if (Float.floatToIntBits(scaledHiLimit) != Float.floatToIntBits(other.scaledHiLimit)) return false;
		if (Float.floatToIntBits(scaledLoLimit) != Float.floatToIntBits(other.scaledLoLimit)) return false;
		if (!Arrays.equals(scaledResults, other.scaledResults)) return false;
		if (!scaledRsltMap.equals(other.scaledRsltMap)) return false;
		if (!scaledUnits.equals(other.scaledUnits)) return false;
		if (Float.floatToIntBits(startIn) != Float.floatToIntBits(other.startIn)) return false;
		if (!units.equals(other.units)) return false;
		if (!unitsIn.equals(other.unitsIn)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

}
