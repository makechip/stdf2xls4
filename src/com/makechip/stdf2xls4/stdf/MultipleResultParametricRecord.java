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
import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: MultipleResultParametricRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public final class MultipleResultParametricRecord extends ParametricTestRecord
{
    public final byte[] rtnState;
    public final double[] results; 
    public final int[] rtnIndex;
    public final float startIn;
    public final float incrIn;
    public final String unitsIn;
    
    /**
    *** @param p1
    **/
    public MultipleResultParametricRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MPR, sequenceNumber, devNum, data);
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
            final byte testFlags,
            final byte paramFlags,
            final int j,
            final int k,
            final byte[] rtnState,
            final double[] results,
    	    final String text,
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
        super(sequenceNumber, deviceNumber, testNumber, headNumber, siteNumber, testFlags, paramFlags);
        this.rtnState = Arrays.copyOf(rtnState, rtnState.length);
        this.results = Arrays.copyOf(results, results.length);
        this.text = text;
        this.alarmName = alarmName;
        if (optFlags != (byte) -1) this.optFlags = OptFlag_t.getBits(optFlags);
        else this.optFlags = null;
        this.resScal = resScal;
        this.llmScal = llmScal;
        this.hlmScal = hlmScal;
        this.loLimit = loLimit;
        this.hiLimit = hiLimit;
        this.startIn = startIn;
        this.incrIn = incrIn;
        this.rtnIndex = Arrays.copyOf(rtnIndex, rtnIndex.length);
        this.units = units;
        this.unitsIn = unitsIn;
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
        list.addAll(getU2Bytes(rtnState.length));
        list.addAll(getU2Bytes(results.length));
        list.addAll(getNibbleBytes(rtnState));
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
        Arrays.stream(results).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    test name: ").append(text).append(Log.eol);
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
    

}
