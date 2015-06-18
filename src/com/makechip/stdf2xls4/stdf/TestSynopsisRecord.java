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

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: TestSynopsisRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class TestSynopsisRecord extends StdfRecord
{
    
    public final short headNumber;
    public final short siteNumber;
    public final char testType;
    public final long testNumber;
    public final long numExecs;
    public final long numFailures;
    public final long numAlarms;
    public final String testName;
    public final String sequencerName;
    public final String testLabel;
    public Set<TestOptFlag_t> optFlags; 
    public float testTime;
    public float testMin;
    public float testMax;
    public float testSum;
    public float testSumSquares;
    
    /**
    *** @param p1
    **/
    public TestSynopsisRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.TSR, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        String c = getFixedLengthString(1);
        testType = c.charAt(0);
        testNumber = getU4(-1L);
        numExecs = getU4(4294967295L);
        numFailures = getU4(4294967295L);
        numAlarms = getU4(4294967295L);
        testName = getCn();
        sequencerName = getCn();
        testLabel = getCn();
        optFlags = Collections.unmodifiableSet(TestOptFlag_t.getBits((byte) getU1((short) 0)));
        testTime = getR4(-1.0f);
        testMin = getR4(-Float.MAX_VALUE);
        testMax = getR4(-Float.MAX_VALUE);
        testSum = getR4(-Float.MAX_VALUE);
        testSumSquares = getR4(-Float.MAX_VALUE);
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(getFixedLengthStringBytes("" + testType));
		l.addAll(getU4Bytes(testNumber));
		l.addAll(getU4Bytes(numExecs));
		l.addAll(getU4Bytes(numFailures));
		l.addAll(getU4Bytes(numAlarms));
		l.addAll(getCnBytes(testName));
		l.addAll(getCnBytes(sequencerName));
		l.addAll(getCnBytes(testLabel));
		l.add((byte) optFlags.stream().mapToInt(b -> b.getBit()).sum());
		l.addAll(getR4Bytes(testTime));
		l.addAll(getR4Bytes(testMin));
		l.addAll(getR4Bytes(testMax));
		l.addAll(getR4Bytes(testSum));
		l.addAll(getR4Bytes(testSumSquares));
		bytes = l.toArray();
	}
	
	public TestSynopsisRecord(
	   short headNumber,
	   short siteNumber,
	   char testType,
	   long testNumber,
	   long numExecs,
	   long numFailures,
	   long numAlarms,
	   String testName,
	   String sequencerName,
	   String testLabel,
	   EnumSet<TestOptFlag_t> optFlags,
	   float testTime,
	   float testMin,
	   float testMax,
	   float testSum,
	   float testSumSquares)
	{
		super(Record_t.TSR, null);
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
		this.testType = testType;
		this.testNumber = testNumber;
		this.numExecs = numExecs;
		this.numFailures = numFailures;
		this.numAlarms = numAlarms;
		this.testName = testName;
		this.sequencerName = sequencerName;
		this.testLabel = testLabel;
		if (optFlags != null)
		{
		    this.optFlags = EnumSet.noneOf(TestOptFlag_t.class);
		    optFlags.stream().forEach(p -> this.optFlags.add(p));
		}
		this.testTime = testTime;
		this.testMin = testMin;
		this.testMax = testMax;
		this.testSum = testSum;
		this.testSumSquares = testSumSquares;
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    headNumber: " + headNumber).append(Log.eol);
        sb.append("    siteNumber: " + siteNumber).append(Log.eol);
        sb.append("    test type: " + testType).append(Log.eol);
        sb.append("    test number: " + testNumber).append(Log.eol);
        sb.append("    number of test execs: " + numExecs).append(Log.eol);
        sb.append("    number of failures: " + numFailures).append(Log.eol);
        System.err.print("numAlarms = " + numAlarms + "\n");
        sb.append("    number of alarms: " + (numAlarms == -1 ? 0 : numAlarms)).append(Log.eol);
        sb.append("    test name: "); sb.append(testName).append(Log.eol);
        sb.append("    sequencer name: "); sb.append(sequencerName).append(Log.eol);
        sb.append("    test label: "); sb.append(testLabel).append(Log.eol);
        sb.append("    optional data flags:");
        optFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
        sb.append(Log.eol);
        sb.append("    test time: " + testTime).append(Log.eol);
        sb.append("    minimum result value: " + testMin).append(Log.eol);
        sb.append("    maximum result value: " + testMax).append(Log.eol);
        sb.append("    sum of result values: " + testSum).append(Log.eol);
        sb.append("    sum of squares of result values: " + testSumSquares).append(Log.eol);
        return(sb.toString());
    }

}
