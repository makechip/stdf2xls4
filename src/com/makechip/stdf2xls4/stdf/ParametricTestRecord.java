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
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.stdf.enums.OptFlag_t.*;
/**
*** @author eric
*** @version $Id: ParametricTestRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class ParametricTestRecord extends ParametricRecord
{
    public final float result; 
    public final String alarmName;
    private final TestID id;
    public final Set<OptFlag_t> optFlags; 
    public final byte resScal;
    public final byte llmScal;
    public final byte hlmScal; 
    public final float loLimit; 
    public final float hiLimit;
    public final String units;
    public final String resFmt;
    public final String llmFmt;
    public final String hlmFmt;
    public final float loSpec;
    public final float hiSpec;
    public final float scaledLoLimit;
    public final float scaledHiLimit;
    public final String scaledUnits;
    public final float scaledResult;
    
    /**
    *** @param p1
    **/
    public ParametricTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
    	super(Record_t.PTR, dvd.getCpuType(), data);
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
            scaledLoLimit = scaleValue(loLimit, findScale(dvd));	
            dvd.scaledLoLimits.put(id, scaledLoLimit);
        }
        else scaledLoLimit = dvd.scaledLoLimits.get(id);
        if (dvd.scaledHiLimits.get(id) == MISSING_FLOAT)
        {
        	scaledHiLimit = scaleValue(hiLimit, findScale(dvd));
        	dvd.scaledLoLimits.put(id, scaledHiLimit);
        }
        else scaledHiLimit = dvd.scaledHiLimits.get(id);
        if (dvd.scaledUnits.get(id) == null)
        {
        	scaledUnits = scaleUnits(units, findScale(dvd));
        	dvd.scaledUnits.put(id, scaledUnits);
        }
        else scaledUnits = dvd.scaledUnits.get(id);
        scaledResult = scaleValue(result, findScale(dvd));
        
        resFmt = setString(MISSING_STRING, getCn(), id , dvd.resFmtDefaults);
        llmFmt = setString(MISSING_STRING, getCn(), id , dvd.llmFmtDefaults);
        hlmFmt = setString(MISSING_STRING, getCn(), id , dvd.hlmFmtDefaults);
        loSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loSpecDefaults);
        hiSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiSpecDefaults);
    }
    
    public ParametricTestRecord(
            final TestIdDatabase tdb,
            final DefaultValueDatabase dvd,
            final Cpu_t cpuType,
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
    	this(tdb, dvd, toBytes(cpuType, testNumber, headNumber, siteNumber, testFlags, paramFlags, result, testName,
    		 alarmName, optFlags, resScal, llmScal, hlmScal, loLimit, hiLimit, units, resFmt, llmFmt,
    		 hlmFmt, loSpec, hiSpec));
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
        	list.add((byte) oFlags.stream().mapToInt(b -> b.getBit()).sum());
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
    	    
    @Override
    public boolean isTestRecord() { return(true); }
    	    
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
        sb.append("    result: " + result).append(Log.eol);
        sb.append("    test name: ").append(id.testName).append(Log.eol);
        sb.append("    alarm name: ").append(alarmName).append(Log.eol);
        if (optFlags != null)
        {
            sb.append("    optional flags:");
        	optFlags.stream().forEach(o -> sb.append(" ").append(o.toString()));
        	sb.append(Log.eol);
        	sb.append("    result scaling exponent: " + resScal).append(Log.eol);
        	sb.append("    low limit scaling exponent: " + llmScal).append(Log.eol);
        	sb.append("    high limit scaling exponent: " + hlmScal).append(Log.eol);
        	sb.append("    low limit: " + loLimit).append(Log.eol);
        	sb.append("    high limit: " + hiLimit).append(Log.eol);
        	sb.append("    units: "); sb.append(units).append(Log.eol);
        	sb.append("    result format string: ").append(resFmt).append(Log.eol);
        	sb.append("    low limit format string: ").append(llmFmt).append(Log.eol);
        	sb.append("    high limit format string: ").append(hlmFmt).append(Log.eol);
        	sb.append("    low spec limit value: " + loSpec).append(Log.eol);
        	sb.append("    high spec limit value: " + hiSpec).append(Log.eol);
        }
        return(sb.toString());
    }
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

}