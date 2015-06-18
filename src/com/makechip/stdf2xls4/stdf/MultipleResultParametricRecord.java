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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: MultipleResultParametricRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public final class MultipleResultParametricRecord extends ParametricRecord
{
	private final LinkedHashMap<String, Float> rsltMap;
	private TestID id;
	public final String alarmName;
	public final Set<OptFlag_t> optFlags;
	public final byte resScal;
	public final byte llmScal;
	public final byte hlmScal;
	public final float loLimit;
	public final float hiLimit;
    public final float startIn;
    public final float incrIn;
    private final byte[] rtnState;
    private final int[] rtnIndex;
    private final float[] results;
    public final String units;
    public final String unitsIn;
    public final String resFmt;
    public final String llmFmt;
    public final String hlmFmt;
    public final float loSpec;
    public final float hiSpec;
    
    @Override
    public boolean isTestRecord() { return(true); }
    
    /**
    *** @param p1
    **/
    public MultipleResultParametricRecord(TestIdDatabase tdb, final DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MPR, data);
        int j = getU2(0);
        int k = getU2(0); 
        rtnState = getNibbles(j);
        results =  new float[k];
        rsltMap = new LinkedHashMap<>();
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
        unitsIn = setString(MISSING_STRING, getCn(), id , dvd.unitsInDefaults);
        resFmt = setString(MISSING_STRING, getCn(), id , dvd.resFmtDefaults);
        llmFmt = setString(MISSING_STRING, getCn(), id , dvd.llmFmtDefaults);
        hlmFmt = setString(MISSING_STRING, getCn(), id , dvd.hlmFmtDefaults);
        loSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loSpecDefaults);
        hiSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiSpecDefaults);
    }
    
    public MultipleResultParametricRecord(
            final TestIdDatabase tdb,
            final DefaultValueDatabase dvd,
            final long testNumber,
            final short headNumber,
            final short siteNumber,
            final byte testFlags,
            final byte paramFlags,
            final int j,
            final int k,
            final byte[] rtnState,
            final float[] results,
    	    final String testName,
    	    final String alarmName,
    	    final byte optFlags, 
    	    final byte resScal, // if RES_SCAL_INVALID set, then use default res_scal
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
        super(Record_t.MPR, testNumber, headNumber, siteNumber, testFlags, paramFlags);
        this.rtnState = Arrays.copyOf(rtnState, rtnState.length);
        this.results = Arrays.copyOf(results, results.length);
        id = TestID.createTestID(tdb, testNumber, testName);
        this.alarmName = alarmName;
        byte oflags = optFlags;
        if (oflags != MISSING_BYTE)
        {
        	this.optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(oflags));
        	if (dvd.optDefaults.get(id) == null) dvd.optDefaults.put(id, this.optFlags);
        }
        else
        {
        	this.optFlags = dvd.optDefaults.get(id);
        }
        this.resScal = setByte(MISSING_BYTE, resScal, id, dvd.resScalDefaults);
        this.llmScal = setByte(MISSING_BYTE, llmScal, id, dvd.llmScalDefaults);
        this.hlmScal = setByte(MISSING_BYTE, hlmScal, id, dvd.hlmScalDefaults);
        this.loLimit = setFloat(MISSING_FLOAT, loLimit, id, dvd.loLimDefaults);
        this.hiLimit = setFloat(MISSING_FLOAT, hiLimit, id, dvd.hiLimDefaults);
        this.startIn = setFloat(MISSING_FLOAT, startIn, id, dvd.startInDefaults);     
        this.incrIn  = setFloat(MISSING_FLOAT, incrIn, id, dvd.startInDefaults);     
        if (j != 0)
        {
        	this.rtnIndex = Arrays.copyOf(rtnIndex, rtnIndex.length);
        	if (dvd.rtnIndexDefaults.get(id) == null) dvd.rtnIndexDefaults.put(id, rtnIndex);
        }
        else
        {
        	this.rtnIndex = dvd.rtnIndexDefaults.get(id);
        }
        rsltMap = new LinkedHashMap<>();
        int n = 0;
        for (int i : rtnIndex)
        {
      	    String pin = (dvd.isFusionCx()) ? dvd.getPhysicalPinName(siteNumber, i) : dvd.getChannelName(siteNumber, i);
      	    rsltMap.put(pin, results[n]);
      	    n++;
        }
        this.units = setString(MISSING_STRING, units, id , dvd.unitDefaults);
        this.unitsIn = setString(MISSING_STRING, unitsIn, id , dvd.unitsInDefaults);
        this.resFmt = setString(MISSING_STRING, resFmt, id , dvd.resFmtDefaults);
        this.llmFmt = setString(MISSING_STRING, llmFmt, id , dvd.llmFmtDefaults);
        this.hlmFmt = setString(MISSING_STRING, hlmFmt, id , dvd.hlmFmtDefaults);
        this.loSpec = setFloat(MISSING_FLOAT, loSpec, id, dvd.loSpecDefaults);
        this.hiSpec = setFloat(MISSING_FLOAT, hiSpec, id, dvd.hiSpecDefaults);
    }
    
    @Override
    protected void toBytes()
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(getU4Bytes(testNumber));
        list.addAll(getU1Bytes(headNumber));
        list.addAll(getU1Bytes(siteNumber));
        list.add((byte) testFlags.stream().mapToInt(b -> b.getBit()).sum());
        list.add((byte) paramFlags.stream().mapToInt(b -> b.getBit()).sum());
        list.addAll(getU2Bytes(rtnState.length));
        list.addAll(getU2Bytes(results.length));
        list.addAll(getNibbleBytes(rtnState));
        for (int i=0; i<results.length; i++) list.addAll(getR4Bytes(results[i]));
        list.addAll(getCnBytes(id.testName));
        list.addAll(getCnBytes(alarmName));
        if (optFlags != null)
        {
        	list.add((byte) optFlags.stream().mapToInt(b -> b.getBit()).sum());
            list.addAll(getI1Bytes(resScal));
            list.addAll(getI1Bytes(llmScal));
            list.addAll(getI1Bytes(hlmScal));
            list.addAll(getR4Bytes(loLimit));
            list.addAll(getR4Bytes(hiLimit));
            list.addAll(getR4Bytes(startIn));
            list.addAll(getR4Bytes(incrIn));
            Arrays.stream(rtnIndex).forEach(p -> list.addAll(getU2Bytes(p)));
            list.addAll(getCnBytes(units));
            list.addAll(getCnBytes(unitsIn));
            list.addAll(getCnBytes(resFmt));
            list.addAll(getCnBytes(llmFmt));
            list.addAll(getCnBytes(hlmFmt));
            list.addAll(getR4Bytes(loSpec));
            list.addAll(getR4Bytes(hiSpec));
        }
        bytes = list.toArray();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    testNumber: " + testNumber).append(Log.eol);
        sb.append("    headNumber: " + headNumber).append(Log.eol);
        sb.append("    siteNumber: " + siteNumber).append(Log.eol);
        sb.append("    testFlags:");
        testFlags.stream().forEach(p -> sb.append(" ").append(p));
        sb.append("    paramFlags:");
        paramFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
        sb.append(Log.eol);
        sb.append("    returned states:");
        for (byte b : rtnState) sb.append(" " + b);
        sb.append(Log.eol);
        sb.append("    results:");
        for (float p : results) sb.append(" " + p);
        sb.append(Log.eol);
        sb.append("    test name: ").append(id.testName).append(Log.eol);
        sb.append("    alarm name: ").append(alarmName).append(Log.eol);
        if (optFlags != null) 
        {
            sb.append("    optional flags:");
        	optFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
        	sb.append(Log.eol);
        	sb.append("    result scaling exponent: " + resScal).append(Log.eol);
        	sb.append("    low limit scaling exponent: " + llmScal).append(Log.eol);
        	sb.append("    high limit scaling exponent: " + hlmScal).append(Log.eol);
        	sb.append("    low limit: " + loLimit).append(Log.eol);
        	sb.append("    high limit: " + hiLimit).append(Log.eol);
        	sb.append("    starting input value: " + startIn).append(Log.eol);
        	sb.append("    increment of input condition: " + incrIn).append(Log.eol);
        	if (rtnIndex != null)
        	{
        		sb.append("    array of PMR indicies:");
        		Arrays.stream(rtnIndex).forEach(p -> sb.append(" " + p));
        	}
        	sb.append(Log.eol);
        	sb.append("    units: ").append(units).append(Log.eol);
        	sb.append("    input condition units: ").append(unitsIn).append(Log.eol);
        	sb.append("    result format string: ").append(resFmt).append(Log.eol);
        	sb.append("    low limit format string: ").append(llmFmt).append(Log.eol);
        	sb.append("    high limit format string: ").append(hlmFmt).append(Log.eol);
        	sb.append("    low spec limit value: " + loSpec).append(Log.eol);
        	sb.append("    high spec limit value: " + hiSpec).append(Log.eol);
        }
        return(sb.toString());
    }

    public final byte[] getRtnState() { return(Arrays.copyOf(rtnState, rtnState.length)); }
    public final float[] getResults() { return(Arrays.copyOf(results, results.length)); }
    public final int[] getRtnIndex() { return(Arrays.copyOf(rtnIndex, rtnIndex.length)); }
    
    public Stream<String> getPinNames() { return(rsltMap.keySet().stream()); }
    
    public float getResult(String pinName) { return(rsltMap.get(pinName)); }

	@Override
	public TestID getTestId()
	{
		return(id);
	}

	@Override
	protected void setTestName(String testName)
	{
		// not needed
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
		return llmScal;
	}

	@Override
	public byte getHlmScal()
	{
		return hlmScal;
	}

	@Override
	public float getLoLimit()
	{
		return loLimit;
	}

	@Override
	public float getHiLimit()
	{
		return hiLimit;
	}

	@Override
	public String getUnits()
	{
		return units;
	}

	@Override
	protected void setText(String text)
	{
		throw new RuntimeException("Error: setText() should not be called on a MultipleResultParametricRecord");
	}
   

}
