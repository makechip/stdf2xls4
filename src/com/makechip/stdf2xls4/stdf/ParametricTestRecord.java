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
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: ParametricTestRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class ParametricTestRecord extends TestRecord
{
	public final long testNumber;
	public final short headNumber;
	public final short siteNumber;
	public final Set<TestFlag_t> testFlags;
	public final Set<ParamFlag_t> paramFlags;
    public final double result; 
    private final String testName;
    public final String alarmName;
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
    
    public boolean alarm() { return(testFlags.contains(TestFlag_t.ALARM)); }
    public boolean unreliable() { return(testFlags.contains(TestFlag_t.UNRELIABLE)); }
    public boolean timeout() { return(testFlags.contains(TestFlag_t.TIMEOUT)); }
    public boolean notExecuted() { return(testFlags.contains(TestFlag_t.NOT_EXECUTED)); }
    public boolean abort() { return(testFlags.contains(TestFlag_t.ABORT)); }
    public boolean noPassFailIndication() { return(testFlags.contains(TestFlag_t.NO_PASS_FAIL)); }
    public boolean fail() { return(testFlags.contains(TestFlag_t.FAIL)); }
    
    @Override
    public boolean isTestRecord() { return(true); }
    
    /**
    *** @param p1
    **/
    public ParametricTestRecord(int sequenceNumber, int devNum, byte[] data)
    {
    	super(Record_t.PTR, sequenceNumber, devNum, data);
    	testNumber = getU4(MISSING_INT);
    	headNumber = getU1(MISSING_BYTE);
    	siteNumber = getU1(MISSING_BYTE);
    	testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(getByte()));
    	paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(getByte()));
        result = getR4(MISSING_FLOAT);
        testName = getCn(); 
        alarmName = getCn();
        if (getSize() <= getPtr()) optFlags = null;
        else optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(getByte()));
        resScal = getI1(MISSING_BYTE); 
        llmScal = getI1(MISSING_BYTE);
        hlmScal = getI1(MISSING_BYTE); 
        loLimit = getR4(MISSING_FLOAT); 
        hiLimit = getR4(MISSING_FLOAT);
        units = getCn();
        resFmt = getCn();
        llmFmt = getCn();
        hlmFmt = getCn();
        loSpec = getR4(MISSING_FLOAT);
        hiSpec = getR4(MISSING_FLOAT);
    }
    
    public ParametricTestRecord(
            final int sequenceNumber,
            final int deviceNumber,
            final int testNumber,
            final int headNumber,
            final int siteNumber,
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
        super(Record_t.PTR, sequenceNumber, deviceNumber, null);
        this.testNumber = testNumber;
        this.headNumber = (short) headNumber;
        this.siteNumber = (short) siteNumber;
        this.testFlags = Collections.unmodifiableSet(TestFlag_t.getBits(testFlags));
        this.paramFlags = Collections.unmodifiableSet(ParamFlag_t.getBits(paramFlags));
        this.result = result;
        this.testName = testName;
        this.alarmName = alarmName;
        if (optFlags != MISSING_BYTE) this.optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(optFlags));
        else this.optFlags = null;
        this.resScal = resScal;
        this.llmScal = llmScal;
        this.hlmScal = hlmScal;
        this.loLimit = loLimit;
        this.hiLimit = hiLimit;
        this.units = units;
        this.resFmt = resFmt;
        this.llmFmt = llmFmt;
        this.hlmFmt = hlmFmt;
        this.loSpec = loSpec;
        this.hiSpec = hiSpec;
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
        list.addAll(getCnBytes(testName));
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
        sb.append("    test name: ").append(testName).append(Log.eol);
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
	public String getTestName()
	{
		return testName;
	}
	@Override
	void setTestName(String testName)
	{
		// not needed
	}

}