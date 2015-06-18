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
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;
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
    
    /**
    *** @param p1
    **/
    public ParametricTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
    	super(Record_t.PTR, data);
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
        resFmt = setString(MISSING_STRING, getCn(), id , dvd.resFmtDefaults);
        llmFmt = setString(MISSING_STRING, getCn(), id , dvd.llmFmtDefaults);
        hlmFmt = setString(MISSING_STRING, getCn(), id , dvd.hlmFmtDefaults);
        loSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.loSpecDefaults);
        hiSpec = setFloat(MISSING_FLOAT, getR4(MISSING_FLOAT), id, dvd.hiSpecDefaults);
    }
    
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
    	    final byte optFlags, 
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
        super(Record_t.PTR, testNumber, headNumber, siteNumber, testFlags, paramFlags);
        this.result = result;
        id = TestID.createTestID(tdb, testNumber, testName);
        this.alarmName = alarmName;
        byte oflags = optFlags;
        if (oflags != MISSING_BYTE)
        {
        	this.optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(oflags));
        	if (dvd.optDefaults.get(id) == null) dvd.optDefaults.put(id, Collections.unmodifiableSet(OptFlag_t.getBits(optFlags)));
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
        this.units = setString(MISSING_STRING, units, id , dvd.unitDefaults);
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
        list.addAll(getR4Bytes((float) result));
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
            list.addAll(getCnBytes(units));
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
		throw new RuntimeException("Error: setText() should not be called on a ParametricTestRecord");
	}

}