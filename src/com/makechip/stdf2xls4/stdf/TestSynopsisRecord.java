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

import java.util.EnumSet;

import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: TestSynopsisRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class TestSynopsisRecord extends StdfRecord
{
    public static enum TestOptFlag_t
    {
        TEST_MIN_INVALID,
        TEST_MAX_INVALID,
        TEST_TIME_INVALID,
        TEST_SUMS_INVALID,
        TEST_SQRS_INVALID;
    }
    
    private final short headNumber;
    private final short siteNumber;
    private final char testType;
    private final long testNumber;
    private final long numExecs;
    private final long numFailures;
    private final long numAlarms;
    private final String testName;
    private final String sequencerName;
    private final String testLabel;
    private EnumSet<TestOptFlag_t> optFlags; 
    private float testTime;
    private float testMin;
    private float testMax;
    private float testSum;
    private float testSumSquares;
    
    /**
    *** @param p1
    **/
    public TestSynopsisRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.TSR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        String c = getFixedLengthString(1);
        testType = c.charAt(0);
        testNumber = getU4(-1);
        numExecs = getU4(-1);
        numFailures = getU4(-1);
        numAlarms = getU4(-1);
        testName = getCn();
        sequencerName = getCn();
        testLabel = getCn();
        optFlags = EnumSet.noneOf(TestOptFlag_t.class);
        int f = getU1((short) 0);
        if ((f & 1) == 1) optFlags.add(TestOptFlag_t.TEST_MIN_INVALID);
        if ((f & 2) == 2) optFlags.add(TestOptFlag_t.TEST_MAX_INVALID);
        if ((f & 4) == 4) optFlags.add(TestOptFlag_t.TEST_TIME_INVALID);
        if ((f & 16) == 16) optFlags.add(TestOptFlag_t.TEST_SUMS_INVALID);
        if ((f & 32) == 32) optFlags.add(TestOptFlag_t.TEST_SQRS_INVALID);
        testTime = getR4(-1.0f);
        testMin = getR4(-Float.MAX_VALUE);
        testMax = getR4(-Float.MAX_VALUE);
        testSum = getR4(-Float.MAX_VALUE);
        testSumSquares = getR4(-Float.MAX_VALUE);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    headNumber: " + headNumber); sb.append(Log.eol);
        sb.append("    siteNumber: " + siteNumber); sb.append(Log.eol);
        sb.append("    test type: " + testType); sb.append(Log.eol);
        sb.append("    test number: " + testNumber); sb.append(Log.eol);
        sb.append("    number of test execs: " + numExecs); sb.append(Log.eol);
        sb.append("    number of failures: " + numFailures); sb.append(Log.eol);
        sb.append("    number of alarms: " + numAlarms); sb.append(Log.eol);
        sb.append("    test name: "); sb.append(testName); sb.append(Log.eol);
        sb.append("    sequencer name: "); sb.append(sequencerName); sb.append(Log.eol);
        sb.append("    test label: "); sb.append(testLabel); sb.append(Log.eol);
        sb.append("    optional data flags:");
        for (TestOptFlag_t t : optFlags)
        {
            sb.append(" ");
            sb.append(t.toString());
        }
        sb.append(Log.eol);
        sb.append("    test time: " + testTime); sb.append(Log.eol);
        sb.append("    minimum result value: " + testMin); sb.append(Log.eol);
        sb.append("    maximum result value: " + testMax); sb.append(Log.eol);
        sb.append("    sum of result values: " + testSum); sb.append(Log.eol);
        sb.append("    sum of squares of result values: " + testSumSquares); sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the optFlags
     */
    public EnumSet<TestOptFlag_t> getOptFlags()
    {
        return optFlags;
    }

    /**
     * @param optFlags the optFlags to set
     */
    public void setOptFlags(EnumSet<TestOptFlag_t> optFlags)
    {
        this.optFlags = optFlags;
    }

    /**
     * @return the testTime
     */
    public float getTestTime()
    {
        return testTime;
    }

    /**
     * @param testTime the testTime to set
     */
    public void setTestTime(float testTime)
    {
        this.testTime = testTime;
    }

    /**
     * @return the testMin
     */
    public float getTestMin()
    {
        return testMin;
    }

    /**
     * @param testMin the testMin to set
     */
    public void setTestMin(float testMin)
    {
        this.testMin = testMin;
    }

    /**
     * @return the testMax
     */
    public float getTestMax()
    {
        return testMax;
    }

    /**
     * @param testMax the testMax to set
     */
    public void setTestMax(float testMax)
    {
        this.testMax = testMax;
    }

    /**
     * @return the testSum
     */
    public float getTestSum()
    {
        return testSum;
    }

    /**
     * @param testSum the testSum to set
     */
    public void setTestSum(float testSum)
    {
        this.testSum = testSum;
    }

    /**
     * @return the testSumSquares
     */
    public float getTestSumSquares()
    {
        return testSumSquares;
    }

    /**
     * @param testSumSquares the testSumSquares to set
     */
    public void setTestSumSquares(float testSumSquares)
    {
        this.testSumSquares = testSumSquares;
    }

    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return headNumber;
    }

    /**
     * @return the siteNumber
     */
    public short getSiteNumber()
    {
        return siteNumber;
    }

    /**
     * @return the testType
     */
    public char getTestType()
    {
        return testType;
    }

    /**
     * @return the testNumber
     */
    public long getTestNumber()
    {
        return testNumber;
    }

    /**
     * @return the numExecs
     */
    public long getNumExecs()
    {
        return numExecs;
    }

    /**
     * @return the numFailures
     */
    public long getNumFailures()
    {
        return numFailures;
    }

    /**
     * @return the numAlarms
     */
    public long getNumAlarms()
    {
        return numAlarms;
    }

    /**
     * @return the testName
     */
    public String getTestName()
    {
        return testName;
    }

    /**
     * @return the sequencerName
     */
    public String getSequencerName()
    {
        return sequencerName;
    }

    /**
     * @return the testLabel
     */
    public String getTestLabel()
    {
        return testLabel;
    }

}
