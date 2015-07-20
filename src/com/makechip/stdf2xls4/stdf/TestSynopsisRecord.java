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
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;

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
    public final Long numExecs;
    /**
     *  The FAIL_CNT field.
     */
    public final Long numFailures;
    /**
     *  The ALRM_CNT field.
     */
    public final Long numAlarms;
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
    public Float testTime;
    /**
     *  The TEST_MIN field.
     */
    public Float testMin;
    /**
     *  The TEST_MAX field.
     */
    public Float testMax;
    /**
     *  The TST_SUMS field.
     */
    public Float testSum;
    /**
     *  The TEST_SQRS field.
     */
    public Float testSumSquares;
    
   public TestSynopsisRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super();
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
        testType = (char) cpu.getI1(is);
        testNumber = cpu.getU4(is);
        int l = 7;
        if (l < recLen)
        {
            numExecs = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else numExecs = null;
        if (l < recLen)
        {
            numFailures = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else numFailures = null;
        if (l < recLen)
        {
            numAlarms = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else numAlarms = null;
        if (l < recLen)
        {
            testName = cpu.getCN(is);
            l += 1 + testName.length();
        }
        else testName = null;
        if (l < recLen)
        {
            sequencerName = cpu.getCN(is);
            l += 1 + sequencerName.length();
        }
        else sequencerName = null;
        if (l < recLen)
        {
            testLabel = cpu.getCN(is);
            l += 1 + testLabel.length();
        }
        else testLabel = null;
        if (l < recLen)
        {
            optFlags = Collections.unmodifiableSet(TestOptFlag_t.getBits((byte) cpu.getU1(is)));
            l++;
        }
        else optFlags = null;
        if (l < recLen)
        {
            testTime = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        }
        else testTime = null;
        if (l < recLen)
        {
            testMin = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        }
        else testMin = null;
        if (l < recLen)
        {
            testMax = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        }
        else testMax = null;
        if (l < recLen)
        {
            testSum = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        }
        else testSum = null;
        if (l < recLen)
        {
            testSumSquares = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        }
        else testSumSquares = null;
        if (l != recLen) throw new RuntimeException("Record length error in TestSynopsisRecord.");
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteNumber, testType, testNumber, numExecs, 
				           numFailures, numAlarms, testName, sequencerName, testLabel, 
				           optFlags, testTime, testMin, testMax, testSum, testSumSquares);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.TSR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static int getRecLen(
	    Long numExecs,
	    Long numFailures,
	    Long numAlarms,
	    String testName,
	    String sequencerName,
	    String testLabel,
	    Set<TestOptFlag_t> optFlags,
	    Float testTime,
	    Float testMin,
	    Float testMax,
	    Float testSum,
	    Float testSumSquares)
	{
        int l = 7;
        if (numExecs != null) l += Data_t.U4.numBytes; else return(l);
        if (numFailures != null) l += Data_t.U4.numBytes; else return(l);
        if (numAlarms != null) l += Data_t.U4.numBytes; else return(l);
        if (testName != null) l += 1 + testName.length(); else return(l);
        if (sequencerName != null) l += 1 + sequencerName.length(); else return(l);
        if (testLabel != null) l += 1 + testLabel.length(); else return(l);
        if (optFlags != null) l++; else return(l);
        if (testTime != null) l += Data_t.R4.numBytes; else return(l);
        if (testMin != null) l += Data_t.R4.numBytes; else return(l);
        if (testMax != null) l += Data_t.R4.numBytes; else return(l);
        if (testSum != null) l += Data_t.R4.numBytes; else return(l);
        if (testSumSquares != null) l += Data_t.R4.numBytes;
        return(l);
	}
	
	private static byte[] toBytes(
	    Cpu_t cpu,
	    short headNumber,
	    short siteNumber,
	    char testType,
	    long testNumber,
	    Long numExecs,
	    Long numFailures,
	    Long numAlarms,
	    String testName,
	    String sequencerName,
	    String testLabel,
	    Set<TestOptFlag_t> optFlags,
	    Float testTime,
	    Float testMin,
	    Float testMax,
	    Float testSum,
	    Float testSumSquares)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteNumber));
		l.addAll(cpu.getI1Bytes((byte) testType));
		l.addAll(cpu.getU4Bytes(testNumber));
		if (numExecs != null) l.addAll(cpu.getU4Bytes(numExecs)); else return(l.toArray());
		if (numFailures != null) l.addAll(cpu.getU4Bytes(numFailures)); else return(l.toArray());
		if (numAlarms != null) l.addAll(cpu.getU4Bytes(numAlarms)); else return(l.toArray());
		if (testName != null) l.addAll(cpu.getCNBytes(testName)); else return(l.toArray());
		if (sequencerName != null) l.addAll(cpu.getCNBytes(sequencerName)); else return(l.toArray());
		if (testLabel != null) l.addAll(cpu.getCNBytes(testLabel)); else return(l.toArray());
		if (optFlags != null) l.add((byte) optFlags.stream().mapToInt(b -> b.bit).sum()); else return(l.toArray());
		if (testTime != null) l.addAll(cpu.getR4Bytes(testTime)); else return(l.toArray());
		if (testMin != null) l.addAll(cpu.getR4Bytes(testMin)); else return(l.toArray());
		if (testMax != null) l.addAll(cpu.getR4Bytes(testMax)); else return(l.toArray());
		if (testSum != null) l.addAll(cpu.getR4Bytes(testSum)); else return(l.toArray());
		if (testSumSquares != null) l.addAll(cpu.getR4Bytes(testSumSquares));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param Cpu_t  The CPU type.
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
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public TestSynopsisRecord(
		Cpu_t cpu,
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
		this(cpu, null, getRecLen(numExecs, numFailures, numAlarms, testName, sequencerName, testLabel, 
	    	                optFlags, testTime, testMin, testMax, testSum, testSumSquares),
			 new ByteInputStream(toBytes(cpu, headNumber, siteNumber, testType, 
					        testNumber, numExecs, numFailures, numAlarms, testName, sequencerName, 
					        testLabel, optFlags, testTime, testMin, testMax, testSum, testSumSquares)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + headNumber;
		result = prime * result + ((numAlarms == null) ? 0 : numAlarms.hashCode());
		result = prime * result + ((numExecs == null) ? 0 : numExecs.hashCode());
		result = prime * result + ((numFailures == null) ? 0 : numFailures.hashCode());
		result = prime * result + ((optFlags == null) ? 0 : optFlags.hashCode());
		result = prime * result + ((sequencerName == null) ? 0 : sequencerName.hashCode());
		result = prime * result + siteNumber;
		result = prime * result + ((testLabel == null) ? 0 : testLabel.hashCode());
		result = prime * result + ((testMax == null) ? 0 : testMax.hashCode());
		result = prime * result + ((testMin == null) ? 0 : testMin.hashCode());
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + (int) (testNumber ^ (testNumber >>> 32));
		result = prime * result + ((testSum == null) ? 0 : testSum.hashCode());
		result = prime * result + ((testSumSquares == null) ? 0 : testSumSquares.hashCode());
		result = prime * result + ((testTime == null) ? 0 : testTime.hashCode());
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
		if (obj == null) return false;
		if (!(obj instanceof TestSynopsisRecord)) return false;
		TestSynopsisRecord other = (TestSynopsisRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (numAlarms == null)
		{
			if (other.numAlarms != null) return false;
		} 
		else if (!numAlarms.equals(other.numAlarms)) return false;
		if (numExecs == null)
		{
			if (other.numExecs != null) return false;
		} 
		else if (!numExecs.equals(other.numExecs)) return false;
		if (numFailures == null)
		{
			if (other.numFailures != null) return false;
		} 
		else if (!numFailures.equals(other.numFailures)) return false;
		if (optFlags == null)
		{
			if (other.optFlags != null) return false;
		} 
		else if (!optFlags.equals(other.optFlags)) return false;
		if (sequencerName == null)
		{
			if (other.sequencerName != null) return false;
		} 
		else if (!sequencerName.equals(other.sequencerName)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (testLabel == null)
		{
			if (other.testLabel != null) return false;
		} 
		else if (!testLabel.equals(other.testLabel)) return false;
		if (testMax == null)
		{
			if (other.testMax != null) return false;
		} 
		else if (!testMax.equals(other.testMax)) return false;
		if (testMin == null)
		{
			if (other.testMin != null) return false;
		} 
		else if (!testMin.equals(other.testMin)) return false;
		if (testName == null)
		{
			if (other.testName != null) return false;
		} 
		else if (!testName.equals(other.testName)) return false;
		if (testNumber != other.testNumber) return false;
		if (testSum == null)
		{
			if (other.testSum != null) return false;
		} 
		else if (!testSum.equals(other.testSum)) return false;
		if (testSumSquares == null)
		{
			if (other.testSumSquares != null) return false;
		} 
		else if (!testSumSquares.equals(other.testSumSquares)) return false;
		if (testTime == null)
		{
			if (other.testTime != null) return false;
		} 
		else if (!testTime.equals(other.testTime)) return false;
		if (testType != other.testType) return false;
		return true;
	}



}
