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
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;
import com.makechip.stdf2xls4.stdfapi.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdfapi.TestIdDatabase;

/**
 *  This class holds the fields for a Test Synopsis Record.
 *  @author eric
 */
public class TestSynopsisRecord extends StdfRecord
{
    /**
     *  The HEAD_NUM field.
     */
    public final short headNumber;
    /**
     *  The SITE_NUM field.
     */
    public final short siteNumber;
    /**
     *  The TEST_TYP field.
     */
    public final char testType;
    /**
     *  The TEST_NUM field.
     */
    public final long testNumber;
    /**
     *  The EXEC_CNT field.
     */
    public final long numExecs;
    /**
     *  The FAIL_CNT field.
     */
    public final long numFailures;
    /**
     *  The ALRM_CNT field.
     */
    public final long numAlarms;
    /**
     *  The TEST_NAM field.
     */
    public final String testName;
    /**
     *  The SEQ_NAME field.
     */
    public final String sequencerName;
    /**
     *  The TEST_LBL field.
     */
    public final String testLabel;
    /**
     *  The OPT_FLAG field.
     */
    public Set<TestOptFlag_t> optFlags; 
    /**
     *  The TEST_TIM field.
     */
    public float testTime;
    /**
     *  The TEST_MIN field.
     */
    public float testMin;
    /**
     *  The TEST_MAX field.
     */
    public float testMax;
    /**
     *  The TST_SUMS field.
     */
    public float testSum;
    /**
     *  The TEST_SQRS field.
     */
    public float testSumSquares;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the TestSynopsisRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
   public TestSynopsisRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.TSR, dvd.getCpuType(), data);
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
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteNumber, testType, testNumber, numExecs,
	    		        numFailures, numAlarms, testName, sequencerName, testLabel, 
	    		        optFlags, testTime, testMin, testMax, testSum, testSumSquares);	
	}
	
	private static byte[] toBytes(
	    Cpu_t cpuType,
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
	    Set<TestOptFlag_t> optFlags,
	    float testTime,
	    float testMin,
	    float testMax,
	    float testSum,
	    float testSumSquares)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(getFixedLengthStringBytes("" + testType));
		l.addAll(cpuType.getU4Bytes(testNumber));
		l.addAll(cpuType.getU4Bytes(numExecs));
		l.addAll(cpuType.getU4Bytes(numFailures));
		l.addAll(cpuType.getU4Bytes(numAlarms));
		l.addAll(getCnBytes(testName));
		l.addAll(getCnBytes(sequencerName));
		l.addAll(getCnBytes(testLabel));
		l.add((byte) optFlags.stream().mapToInt(b -> b.bit).sum());
		l.addAll(cpuType.getR4Bytes(testTime));
		l.addAll(cpuType.getR4Bytes(testMin));
		l.addAll(cpuType.getR4Bytes(testMax));
		l.addAll(cpuType.getR4Bytes(testSum));
		l.addAll(cpuType.getR4Bytes(testSumSquares));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
     * @param dvd The DefaultValueDatabase is used to access the CPU type.
	 * @param headNumber The HEAD_NUM field.
	 * @param siteNumber The SITE_NUM field.
	 * @param testType   The TEST_TYP field.
	 * @param testNumber The TEST_NUM field.
	 * @param numExecs   The EXEC_CNT field.
	 * @param numFailures The FAIL_CNT field.
	 * @param numAlarms  The ALRM_CNT field.
	 * @param testName   The TEST_NAM field.
	 * @param sequencerName The SEQ_NAME field.
	 * @param testLabel  The TEST_LBL field.
	 * @param optFlags   The OPT_FLAG field.
	 * @param testTime   The TEST_TIM field.
	 * @param testMin    The TEST_MIN field.
	 * @param testMax    The TEST_MAX field.
	 * @param testSum    The TST_SUMS field.
	 * @param testSumSquares The TST_SQRS field.
	 */
	public TestSynopsisRecord(
	    TestIdDatabase tdb,
	    DefaultValueDatabase dvd,
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
		this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, testType, testNumber, numExecs,
	    		        numFailures, numAlarms, testName, sequencerName, testLabel, 
	    		        optFlags, testTime, testMin, testMax, testSum, testSumSquares));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TestSynopsisRecord [headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", testType=").append(testType);
		builder.append(", testNumber=").append(testNumber);
		builder.append(", numExecs=").append(numExecs);
		builder.append(", numFailures=").append(numFailures);
		builder.append(", numAlarms=").append(numAlarms);
		builder.append(", testName=").append(testName);
		builder.append(", sequencerName=").append(sequencerName);
		builder.append(", testLabel=").append(testLabel);
		builder.append(", optFlags=").append(optFlags);
		builder.append(", testTime=").append(testTime);
		builder.append(", testMin=").append(testMin);
		builder.append(", testMax=").append(testMax);
		builder.append(", testSum=").append(testSum);
		builder.append(", testSumSquares=").append(testSumSquares);
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
		result = prime * result + headNumber;
		result = prime * result + (int) (numAlarms ^ (numAlarms >>> 32));
		result = prime * result + (int) (numExecs ^ (numExecs >>> 32));
		result = prime * result + (int) (numFailures ^ (numFailures >>> 32));
		result = prime * result + optFlags.hashCode();
		result = prime * result + sequencerName.hashCode();
		result = prime * result + siteNumber;
		result = prime * result + testLabel.hashCode();
		result = prime * result + Float.floatToIntBits(testMax);
		result = prime * result + Float.floatToIntBits(testMin);
		result = prime * result + testName.hashCode();
		result = prime * result + (int) (testNumber ^ (testNumber >>> 32));
		result = prime * result + Float.floatToIntBits(testSum);
		result = prime * result + Float.floatToIntBits(testSumSquares);
		result = prime * result + Float.floatToIntBits(testTime);
		result = prime * result + testType;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof TestSynopsisRecord)) return false;
		TestSynopsisRecord other = (TestSynopsisRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (numAlarms != other.numAlarms) return false;
		if (numExecs != other.numExecs) return false;
		if (numFailures != other.numFailures) return false;
		if (!optFlags.equals(other.optFlags)) return false;
		if (!sequencerName.equals(other.sequencerName)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!testLabel.equals(other.testLabel)) return false;
		if (Float.floatToIntBits(testMax) != Float.floatToIntBits(other.testMax)) return false;
		if (Float.floatToIntBits(testMin) != Float.floatToIntBits(other.testMin)) return false;
		if (!testName.equals(other.testName)) return false;
		if (testNumber != other.testNumber) return false;
		if (Float.floatToIntBits(testSum) != Float.floatToIntBits(other.testSum)) return false;
		if (Float.floatToIntBits(testSumSquares) != Float.floatToIntBits(other.testSumSquares)) return false;
		if (Float.floatToIntBits(testTime) != Float.floatToIntBits(other.testTime)) return false;
		if (testType != other.testType) return false;
		if (!super.equals(obj)) return false;
		return true;
	}


}
