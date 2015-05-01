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
import java.util.EnumSet;
import java.util.List;
import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: MultipleResultParametricRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public final class MultipleResultParametricRecord extends ParametricTestRecord
{
    private byte[] rtnState;
    private double[] results; 
    private float startIn;
    private float incrIn;
    private int[] rtnIndex;
    private String unitsIn;
    private PinList pins;
    
    /**
    *** @param p1
    **/
    public MultipleResultParametricRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MPR, sequenceNumber, devNum, data);
        reqFields = new RequiredParametricFields(this);
        int j = getU2(0);
        int k = getU2(0); 
        rtnState = getNibbles(j);
        results =  new double[k];
        Arrays.setAll(results, p -> getR4(-Float.MAX_VALUE));
        getParametricFields(); 
        startIn = getR4(MISSING_FLOAT);
        incrIn = getR4(MISSING_FLOAT);
        rtnIndex = new int[j];
        Arrays.setAll(rtnIndex, p -> getU2(-1));
        units = getCn();
        unitsIn = getCn();
        getLastFields(); 
    }
    
    public MultipleResultParametricRecord(
            final int sequenceNumber,
            final int deviceNumber,
            final int testNumber,
            final int headNumber,
            final int siteNumber,
            final EnumSet<TestFlag_t> testFlags,
            final EnumSet<ParamFlag_t> paramFlags,
            final int j,
            final int k,
            final byte[] rtnState,
            final double[] results,
    	    final String text,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
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
        super(Record_t.PTR, sequenceNumber, deviceNumber, null);
        reqFields = new RequiredParametricFields(testNumber, headNumber, siteNumber, testFlags, paramFlags);
        this.rtnState = Arrays.copyOf(rtnState, rtnState.length);
        this.results = Arrays.copyOf(results, results.length);
        this.text = text;
        this.alarmName = alarmName;
        if (optFlags != null)
        {
            this.optFlags = EnumSet.noneOf(OptFlag_t.class);
            optFlags.stream().forEach(p -> this.optFlags.add(p));
        }
        else this.optFlags = null;
        this.resScal = resScal;
        this.llmScal = llmScal;
        this.hlmScal = hlmScal;
        this.loLimit = loLimit;
        this.hiLimit = hiLimit;
        this.startIn = startIn;
        this.incrIn = incrIn;
        this.units = units;
        this.unitsIn = unitsIn;
        this.resFmt = resFmt;
        this.llmFmt = llmFmt;
        this.hlmFmt = hlmFmt;
        this.loSpec = loSpec;
        this.hiSpec = hiSpec;
    }
    
    public MultipleResultParametricRecord(MultipleResultParametricRecord mpr, DefaultMPRValueMap dmap)
    {
    	super(mpr.getRecordType(), mpr.getSequenceNumber(), mpr.getDeviceNumber(), null);
    	reqFields = new RequiredParametricFields(mpr.getTestNumber(), mpr.getHeadNumber(), mpr.getSiteNumber(),
    			mpr.getTestFlags(), mpr.getParamFlags());
    	rtnState = mpr.getRtnState();
    	results = mpr.getResults();
    	text = mpr.getTestName();
    	alarmName = mpr.getAlarmName();
    	if (mpr.getOptFlags() != null) optFlags = mpr.getOptFlags();
    	else optFlags = dmap.poptDefaults.get(mpr.getTestNumber());
    	if (mpr.getResScal() == MISSING_BYTE) 
    	{
    		resScal = dmap.resScalDefaults.get(testId);
    		if (resScal == MISSING_BYTE) resScal = dmap.nresScalDefaults.get(mpr.getTestNumber());
    	}
    	else resScal = mpr.getResScal();
    	if (mpr.getLlmScal() == MISSING_BYTE)
    	{
    		llmScal = dmap.llmScalDefaults.get(testId);
    		if (llmScal == MISSING_BYTE) llmScal = dmap.nllmScalDefaults.get(mpr.getTestNumber());
    	}
    	else llmScal = mpr.getLlmScal();
    	if (mpr.getHlmScal() == MISSING_BYTE)
    	{
    		hlmScal = dmap.hlmScalDefaults.get(testId);
    		if (hlmScal == MISSING_BYTE) hlmScal = dmap.nhlmScalDefaults.get(mpr.getTestNumber());
    	}
    	else hlmScal = mpr.getHlmScal();
    	if (mpr.getLoLimit() == MISSING_FLOAT)
    	{
    		loLimit = dmap.loLimDefaults.get(testId);
    		if (loLimit == MISSING_FLOAT) loLimit = dmap.nloLimDefaults.get(mpr.getTestNumber());
    	}
    	else loLimit = mpr.getLoLimit();
    	if (mpr.getHiLimit() == MISSING_FLOAT)
    	{
    		hiLimit = dmap.hiLimDefaults.get(testId);
    		if (hiLimit == MISSING_FLOAT) hiLimit = dmap.nhiLimDefaults.get(mpr.getTestNumber());
    	}
    	else hiLimit = mpr.getHiLimit();
        if (startIn == MISSING_FLOAT)
        {
        	startIn = dmap.startInDefaults.get(testId);
        	if (startIn == MISSING_FLOAT) startIn = dmap.nstartInDefaults.get(mpr.getTestNumber());
        }
        else startIn = mpr.getStartIn();
        if (incrIn == MISSING_FLOAT)
        {
        	incrIn = dmap.incrInDefaults.get(testId);
        	if (incrIn == MISSING_FLOAT) incrIn = dmap.nincrInDefaults.get(mpr.getTestNumber());
        }
        else incrIn = mpr.getIncrIn();
    	if (mpr.getUnits().equals(MISSING_STRING))
    	{
    		units = dmap.unitDefaults.get(testId);
    		if (units.equals(MISSING_STRING)) units = dmap.nunitDefaults.get(mpr.getTestNumber());
    	}
    	else units = mpr.getUnits();
    	if (mpr.getUnitsIn().equals(MISSING_STRING))
    	{
    		unitsIn = dmap.unitsInDefaults.get(testId);
    		if (unitsIn.equals(MISSING_STRING)) unitsIn = dmap.nunitsInDefaults.get(mpr.getTestNumber());
    	}
    	else unitsIn = mpr.getUnits();
    	if (mpr.getResFmt().equals(MISSING_STRING))
    	{
    		resFmt = dmap.resFmtDefaults.get(testId);
    		if (resFmt.equals(MISSING_STRING)) resFmt = dmap.nresFmtDefaults.get(mpr.getTestNumber());
    	}
    	else resFmt = mpr.getResFmt();
    	if (mpr.getHlmFmt().equals(MISSING_STRING))
    	{
    		hlmFmt = dmap.hlmFmtDefaults.get(testId);
    		if (hlmFmt.equals(MISSING_STRING)) hlmFmt = dmap.nhlmFmtDefaults.get(mpr.getTestNumber());
    	}
    	else hlmFmt = mpr.getHlmFmt();
    	if (mpr.getLlmFmt().equals(MISSING_STRING))
    	{
    		llmFmt = dmap.llmFmtDefaults.get(testId);
    		if (llmFmt.equals(MISSING_STRING)) llmFmt = dmap.nllmFmtDefaults.get(mpr.getTestNumber());
    	}
    	else llmFmt = mpr.getLlmFmt();
        if (mpr.getLoSpec() == MISSING_FLOAT)
        {
        	loSpec = dmap.loSpecDefaults.get(testId);
        	if (loSpec == MISSING_FLOAT) loSpec = dmap.nloSpecDefaults.get(mpr.getTestNumber());
        }
        else loSpec = mpr.getLoSpec();
        if (mpr.getHiSpec() == MISSING_FLOAT)
        {
        	hiSpec = dmap.hiSpecDefaults.get(testId);
        	if (hiSpec == MISSING_FLOAT) hiSpec = dmap.nhiSpecDefaults.get(mpr.getTestNumber());
        }
        else hiSpec = mpr.getHiSpec();
    }
    
    @Override
    protected void toBytes()
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(getU4Bytes(reqFields.getTestNumber()));
        list.addAll(getU1Bytes(reqFields.getHeadNumber()));
        list.addAll(getU1Bytes(reqFields.getSiteNumber()));
        list.add((byte) reqFields.getTestFlags().stream().mapToInt(b -> b.getBit()).sum());
        list.add((byte) reqFields.getParamFlags().stream().mapToInt(b -> b.getBit()).sum());
        list.addAll(getU2Bytes(rtnState.length));
        list.addAll(getU2Bytes(results.length));
        list.addAll(getNibbles(rtnState.length));
        Arrays.stream(results).forEach(p -> list.addAll(getR4Bytes((float) p)));
        list.addAll(getCnBytes(text));
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
        sb.append(":");
        sb.append(Log.eol);
        sb.append(reqFields.toString());
        sb.append("    returned states:");
        for (byte b : rtnState) sb.append(" " + b);
        sb.append(Log.eol);
        sb.append("    results:");
        Arrays.stream(results).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    test name: "); sb.append(text); sb.append(Log.eol);
        sb.append("    alarm name: "); sb.append(alarmName); sb.append(Log.eol);
        sb.append("    optional flags:");
        if (optFlags != null) optFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
        sb.append(Log.eol);
        sb.append("    result scaling exponent: " + resScal); sb.append(Log.eol);
        sb.append("    low limit scaling exponent: " + llmScal); sb.append(Log.eol);
        sb.append("    high limit scaling exponent: " + hlmScal); sb.append(Log.eol);
        sb.append("    low limit: " + getLoLimit()); sb.append(Log.eol);
        sb.append("    high limit: " + getHiLimit()); sb.append(Log.eol);
        sb.append("    starting input value: " + getStartIn()); sb.append(Log.eol);
        sb.append("    increment of input condition: " + getIncrIn()); sb.append(Log.eol);
        if (rtnIndex != null)
        {
            sb.append("    array of PMR indicies:");
            Arrays.stream(rtnIndex).forEach(p -> sb.append(" " + p));
        }
        sb.append(Log.eol);
        sb.append("    units: "); sb.append(units); sb.append(Log.eol);
        sb.append("    input condition units: "); sb.append(unitsIn); sb.append(Log.eol);
        sb.append("    result format string: "); sb.append(resFmt); sb.append(Log.eol);
        sb.append("    low limit format string: "); sb.append(llmFmt); sb.append(Log.eol);
        sb.append("    high limit format string: "); sb.append(hlmFmt); sb.append(Log.eol);
        sb.append("    low spec limit value: " + getLoSpec()); sb.append(Log.eol);
        sb.append("    high spec limit value: " + getHiSpec()); sb.append(Log.eol);
        return(sb.toString());
    }
    
    public double[] getScaledResults()
    {
        double[] r = new double[results.length];
        for (int i=0; i<results.length; i++)
        {
            r[i] = scaleValue(results[i], findScale());
        }
        return(r);
    }

    public List<String> getPins() { return(pins.getPins()); }

    public int[] getRtnIndex()
    {
        return(rtnIndex);
    }

    public int getRtnIcnt()
    {
        return(rtnIndex.length);
    }

    public int getRsltCnt()
    {
        return(results.length);
    }

    public byte[] getRtnState()
    {
        return(rtnState);
    }

    public double[] getResults()
    {
        return(results);
    }

    public float getStartIn()
    {
        return(startIn);
    }

    public float getIncrIn()
    {
        return(incrIn);
    }

    public String getUnitsIn()
    {
        return(unitsIn);
    }

}
