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
import java.util.EnumSet;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import com.makechip.util.Log;
/**
 *  This class holds the fields for an STDF FunctionalTestRecord.
 *  @author eric
 */
public class FunctionalTestRecord extends TestRecord
{
	/**
	 *  This is the HEAD_NUM field.
	 */
	public final short headNumber;
	/**
	 *  This is the SITE_NUM field.
	 */
	public final short siteNumber;
	/**
	 *  This is the TEST_FLG field. 
	 */
	public final Set<TestFlag_t> testFlags;
	/**
	 *  This is the OPT_FLAG field.
	 */
    public final Set<FTROptFlag_t> optFlags;
	/**
	 *  This is the CYCL_CNT field.
	 */
    public final long cycleCount;    // cycle count is invalid if CYCLE_CNT_INVALID is set
	/**
	 *  This is the REL_VADDR field.
	 */
    public final long relVaddr;      // relVaddr is invalid if REL_VADDR_INVALID is set
	/**
	 *  This is the REPT_CNT field.
	 */
    public final long rptCnt;        // rptCnt is invalid if REPEAT_CNT_INVALID is set
	/**
	 *  This is the NUM_FAIL field.
	 */
    public final long numFail;       // numFail is invalid if NUM_FAIL_INVALID is set
	/**
	 *  This is the XFAIL_AD field.
	 */
    public final int xFailAddr;     // xFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
	/**
	 *  This is the YFAIL_AD field.
	 */
    public final int yFailAddr;     // yFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
	/**
	 *  This is the VECT_OFF field.
	 */
    public final short vecOffset;   // vecOffset is invalid if VEC_OFFSET_INVALID is set
	/**
	 *  This is the VECT_NAM field.
	 */
    public final String vecName;
	/**
	 *  This is the TIME_SET field.
	 */
    public final String timeSetName;
	/**
	 *  This is the OP_CODE field.
	 */
    public final String vecOpCode;
	/**
	 *  This is the ALARM_ID field.
	 */
    public final String alarmName;
	/**
	 *  This is the PROG_TXT field.
	 */
    public final String progTxt;
	/**
	 *  This is the RSLT_TXT field.
	 */
    public final String rsltTxt;
	/**
	 *  This is the PATG_NUM field.
	 */
    public final short patGenNum;
    /**
     *  This field holds the TEST_NUM and TEST_TXT fields.
     */
    public final TestID id;
    private final int numFailPinBits;
    private final int numEnCompBits;
    
    private final int[] rtnIndex;
    private final byte[] rtnState;
    private final int[] pgmIndex;
    private final byte[] pgmState;
    private final byte[] failPin;
    private final byte[] enComps;
    private final boolean missing;
    
    /**
     * Constructor to create an FTR record from the binary data stream.
     * @param tdb This parameter is used for tracking the Test ID.
     * @param dvd The DefaultValueDatabase is used to get the CPU type,
     * and to supply missing data with default values.
     * @param data Binary stream data.  This array should not contain
     * the first four bytes of the record.
     */
    public FunctionalTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.FTR, dvd.getCpuType(), data);
        long testNumber = getU4(MISSING_LONG);
        headNumber = getU1(MISSING_SHORT);
        siteNumber = getU1(MISSING_SHORT);
        EnumSet<TestFlag_t> s = TestFlag_t.getBits(getByte());
        testFlags = Collections.unmodifiableSet(s);
        boolean missingData = ptr >= getSize();
        optFlags = missingData ? dvd.foptDefaults.get(testNumber) : Collections.unmodifiableSet(FTROptFlag_t.getBits(getByte()));
        if (!missingData)
        {
        	if (dvd.foptDefaults.get(testNumber) == null) dvd.foptDefaults.put(testNumber, optFlags);
        }
        if (missingData)
        {
        	missing = true;
        	cycleCount = MISSING_INT;
        	relVaddr = MISSING_INT;
        	rptCnt = MISSING_INT;
        	numFail = MISSING_INT;
        	xFailAddr = MISSING_INT;
        	yFailAddr = MISSING_INT;
        	vecOffset = MISSING_SHORT;
        	rtnIndex = new int[0];
        	rtnState = new byte[0];
        	pgmIndex = new int[0];
        	pgmState = new byte[0];
        	numFailPinBits = 0;
        	failPin = new byte[0];
        	vecName = "";
        	timeSetName = "";
        	vecOpCode = "";
        	String testName = dvd.tnameDefaults.get(testNumber);
        	if (testName == null) testName = "";
        	id = TestID.createTestID(tdb, testNumber, testName);
        	alarmName = "";
        	progTxt = "";
        	rsltTxt = "";
        	patGenNum = dvd.pgDefaults.get(testNumber);
        	numEnCompBits = 0;
        	enComps = dvd.ecDefaults.get(testNumber);
        }
        else
        {
        	missing = false;
            cycleCount = getU4(MISSING_INT);
            relVaddr = getU4(MISSING_INT);
            rptCnt = getU4(MISSING_INT);
            numFail = getU4(MISSING_INT);
            xFailAddr = getI4(MISSING_INT);
            yFailAddr = getI4(MISSING_INT);
            vecOffset = getI2(MISSING_SHORT);
            int j = getU2(0);
            int k = getU2(0);
            rtnIndex = new int[j];
            for (int i=0; i<j; i++) rtnIndex[i] = getU2(MISSING_INT);
            rtnState = getNibbles(j); 
            pgmIndex = new int[k];
            for (int i=0; i<k; i++) pgmIndex[i] = getU2(MISSING_INT);
            pgmState = getNibbles(k);
            MutableInt numBits = new MutableInt();
            failPin = getDn(numBits);
            numFailPinBits = numBits.n;
            vecName = getCn();
            timeSetName = getCn();
            vecOpCode = getCn(); 
            String testName = getCn();
            if (!testName.equals(MISSING_STRING))
            {
        	    if (dvd.tnameDefaults.get(testNumber) == null) dvd.tnameDefaults.put(testNumber, testName);
            }
            else testName = dvd.tnameDefaults.get(testNumber);
            if (testName == null) testName = "";
            id = TestID.createTestID(tdb, testNumber, testName);
            alarmName = getCn();
            progTxt = getCn();
            rsltTxt = getCn();
            short p = getU1(MISSING_SHORT);
            if (p != MISSING_SHORT)
            {
            	if (dvd.pgDefaults.get(testNumber) == MISSING_SHORT) dvd.pgDefaults.put(testNumber, p);
            }
            else p = dvd.pgDefaults.get(testNumber);
            patGenNum = p;
            numBits.n = 0;
            byte[] ec = getDn(numBits); 
            numEnCompBits = numBits.n;
            if (ec.length != 0)
            {
            	if (dvd.ecDefaults.get(testNumber) == null) dvd.ecDefaults.put(testNumber, ec);
            }
            else ec = dvd.ecDefaults.get(testNumber);
            enComps = ec;
        }
    }
    
    /**
     * This constructor is used to generate a FunctionalTestRecord with field values.
     * @param tdb TestIdDatabase is for generating the TestID.
     * @param dvd The DefaultValueDatabase is for Supplying the CPU type and default values.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     * @param optFlags   The OPT_FLAG field.
     * @param cycleCount The CYCL_CNT field.
     * @param relVaddr   The REL_VADDR field.
     * @param rptCnt     The REPT_CNT field.
     * @param numFail    The NUM_FAIL field.
     * @param xFailAddr  The XFAIL_AD field.
     * @param yFailAddr  The YFAIL_AD field.
     * @param vecOffset  The VECT_OFF field.
     * @param rtnIndex   The RTN_INDX field.
     * @param rtnState   The RTN_STAT field.
     * @param pgmIndex   The PGM_INDX field.
     * @param pgmState   The PGM_STAT field.
     * @param numFailPinBits The number of bits in the FAIL_PIN field.
     * @param failPin    The FAIL_PIN field.
     * @param vecName    The VECT_NAM field.
     * @param timeSetName The TIME_SET field.
     * @param vecOpCode  The OP_CODE field.
     * @param label      The TEST_TXT field.
     * @param alarmName  The ALARM_ID field.
     * @param progTxt    The PROG_TXT field.
     * @param rsltTxt    The RSLT_TXT field.
     * @param patGenNum  The PATG_NUM field.
     * @param numEnCompBits The number of bits in the SPIN_MAP field.
     * @param enComps    The SPIN_MAP field.
     */
     public FunctionalTestRecord(
        TestIdDatabase tdb,
        DefaultValueDatabase dvd,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final byte testFlags,
        final byte optFlags,
        final long cycleCount,
        final long relVaddr,
        final long rptCnt,
        final long numFail,
        final int xFailAddr,
        final int yFailAddr,
        final short vecOffset,
        final int[] rtnIndex,
        final byte[] rtnState,
        final int[] pgmIndex,
        final byte[] pgmState,
        final int numFailPinBits,
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        String label,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final short patGenNum,
        final int numEnCompBits,
        final byte[] enComps)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), testNumber, headNumber, siteNumber, TestFlag_t.getBits(testFlags), 
    		 FTROptFlag_t.getBits(optFlags), cycleCount, relVaddr, rptCnt, numFail, xFailAddr, yFailAddr, 
    		 vecOffset, rtnIndex, rtnState, pgmIndex, pgmState, numFailPinBits, failPin, vecName, timeSetName, vecOpCode, 
    		 label, alarmName, progTxt, rsltTxt, patGenNum, numEnCompBits, enComps));
    }
    
    /**
     * This constructor is used to generate a FunctionalTestRecord with field values.
     * Use this for the case when omitting optional data.
     * @param tdb TestIdDatabase is for generating the TestID.
     * @param dvd The DefaultValueDatabase is for Supplying the CPU type and default values.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     */
     public FunctionalTestRecord(
        TestIdDatabase tdb,
        DefaultValueDatabase dvd,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final byte testFlags)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), testNumber, headNumber, siteNumber, TestFlag_t.getBits(testFlags)));
    }
    
    @Override
    protected void toBytes()
    {
    	if (missing)
    	{
    		bytes = toBytes(cpuType, id.testNumber, headNumber, siteNumber, testFlags);
    	}
    	else
    	{
    		bytes = toBytes(
    				cpuType,
    				id.testNumber,
    				headNumber,
    				siteNumber,
    				testFlags,
    				optFlags,
    				cycleCount,
    				relVaddr,
    				rptCnt,
    				numFail,
    				xFailAddr,
    				yFailAddr,
    				vecOffset,
    				rtnIndex,
    				rtnState,
    				pgmIndex,
    				pgmState,
    				numFailPinBits,
    				failPin,
    				vecName,
    				timeSetName,
    				vecOpCode,
    				id.testName,
    				alarmName,
    				progTxt,
    				rsltTxt,
    				patGenNum,
    				numEnCompBits,
    				enComps);
    	}
    }
    
    private static byte[] toBytes(
        Cpu_t cpuType,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final Set<TestFlag_t> testFlags,
        final Set<FTROptFlag_t> optFlags,
        final long cycleCount,
        final long relVaddr,
        final long rptCnt,
        final long numFail,
        final int xFailAddr,
        final int yFailAddr,
        final short vecOffset,
        final int[] rtnIndex,
        final byte[] rtnState,
        final int[] pgmIndex,
        final byte[] pgmState,
        final int numFailPinBits,
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        String label,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final short patGenNum,
        final int numEnCompBits,
        final byte[] enComps)
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(cpuType.getU4Bytes(testNumber));
        list.addAll(getU1Bytes(headNumber));
        list.addAll(getU1Bytes(siteNumber));
        byte b = 0;
        for (TestFlag_t t : testFlags) b |= t.bit;
        list.add(b);
        b = 0;
        for (FTROptFlag_t t : optFlags) b |= t.bit;
        list.add(b);
        list.addAll(cpuType.getU4Bytes(cycleCount));
        list.addAll(cpuType.getU4Bytes(relVaddr));
        list.addAll(cpuType.getU4Bytes(rptCnt));
        list.addAll(cpuType.getU4Bytes(numFail));
        list.addAll(cpuType.getI4Bytes(xFailAddr));
        list.addAll(cpuType.getI4Bytes(yFailAddr));
        list.addAll(cpuType.getI2Bytes(vecOffset));
        list.addAll(cpuType.getU2Bytes(rtnIndex.length));
        list.addAll(cpuType.getU2Bytes(pgmIndex.length));
        for (int i=0; i<rtnIndex.length; i++) list.addAll(cpuType.getU2Bytes(rtnIndex[i]));
        list.addAll(getNibbleBytes(rtnState));
        for (int i=0; i<pgmIndex.length; i++) list.addAll(cpuType.getU2Bytes(pgmIndex[i]));
        list.addAll(getNibbleBytes(pgmState));
        list.addAll(cpuType.getDnBytes(numFailPinBits, failPin));
        list.addAll(getCnBytes(vecName));
        list.addAll(getCnBytes(timeSetName));
        list.addAll(getCnBytes(vecOpCode));
        list.addAll(getCnBytes(label));
        list.addAll(getCnBytes(alarmName));
        list.addAll(getCnBytes(progTxt));
        list.addAll(getCnBytes(rsltTxt));
        list.addAll(getU1Bytes(patGenNum));
        list.addAll(cpuType.getDnBytes(numEnCompBits, enComps));
        return(list.toArray());
    }
  
    private static byte[] toBytes(
        Cpu_t cpuType,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final Set<TestFlag_t> testFlags)
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(cpuType.getU4Bytes(testNumber));
        list.addAll(getU1Bytes(headNumber));
        list.addAll(getU1Bytes(siteNumber));
        byte b = 0;
        for (TestFlag_t t : testFlags) b |= t.bit;
        list.add(b);
        return(list.toArray());
    }
  
    /**
     * Tests if the TEST_FLG field has the alarm bit (bit 0) set.
     * @return True if the alarm bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean alarm() { return(testFlags.contains(TestFlag_t.ALARM)); }
    /**
     * Tests if the TEST_FLG field has the unreliable bit (bit 2) set.
     * @return True if the unreliable bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean unreliable() { return(testFlags.contains(TestFlag_t.UNRELIABLE)); }
    /**
     * Tests if the TEST_FLG field has the timeout bit (bit 3) set.
     * @return True if the timeout bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean timeout() { return(testFlags.contains(TestFlag_t.TIMEOUT)); }
    /**
     * Tests if the TEST_FLG field has the not-executed bit (bit 4) set.
     * @return True if the not-executed bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean notExecuted() { return(testFlags.contains(TestFlag_t.NOT_EXECUTED)); }
    /**
     * Tests if the TEST_FLG field has the abort bit (bit 5) set.
     * @return True if the abort bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean abort() { return(testFlags.contains(TestFlag_t.ABORT)); }
    /**
     * Tests if the TEST_FLG field has the no-pass-fail-indication bit (bit 6) set. 
     * @return True if the no-pass-fail-indication bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean noPassFailIndication() { return(testFlags.contains(TestFlag_t.NO_PASS_FAIL)); }
    /**
     * Tests if the TEST_FLG field has the fail bit (bit 7) set.
     * @return True if the fail bit is set in the TEST_FLG field, false otherwise.
     */
    public boolean fail() { return(testFlags.contains(TestFlag_t.FAIL)); }
    
    /**
     * Get the array of PMR indexes. 
     * @return A copy of the PMR index (RTN_INDX) array.
     */
    public int[] getRtnIndex() { return(Arrays.copyOf(rtnIndex, rtnIndex.length)); }
    /**
     * Get the array of returned states.
     * @return A copy of the RTN_STAT array.
     */
    public byte[] getRtnState() { return(Arrays.copyOf(rtnState, rtnState.length)); }
    /**
     * Get the array of programmed state indexes.
     * @return A copy of the PGM_INDX array.
     */
    public int[] getPgmIndex() { return(Arrays.copyOf(pgmIndex, pgmIndex.length)); }
    /**
     * Get the array of programmed state indexes. 
     * @return A copy of the PGM_STAT array.
     */
    public byte[] getPgmState() { return(Arrays.copyOf(pgmState, pgmState.length)); }
    /**
     * Get the failing pin bit-field. Note: use the numFailPinBits field to
     * determine the exact number of bits in this field.
     * @return A copy of the failPin (FAIL_PIN) array.
     */
    public byte[] getFailPin() { return(Arrays.copyOf(failPin, failPin.length)); }
    /**
     * Get the bit map of enabled comparators. Note: use the numEnCompBits to
     * determine the exace number of bits in this field.
     * @return A copy of the enComps (SPIN_MAP) array.
     */
    public byte[] getEnComps() { return(Arrays.copyOf(enComps, enComps.length)); }



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("FunctionalTestRecord [testFlags=").append(testFlags);
		builder.append(", optFlags=").append(optFlags);
		builder.append(", cycleCount=").append(cycleCount);
		builder.append(", relVaddr=").append(relVaddr);
		builder.append(", rptCnt=").append(rptCnt);
		builder.append(", numFail=").append(numFail);
		builder.append(", xFailAddr=").append(xFailAddr);
		builder.append(", yFailAddr=").append(yFailAddr);
		builder.append(", vecOffset=").append(vecOffset);
		builder.append(", vecName=").append(vecName);
		builder.append(", timeSetName=").append(timeSetName);
		builder.append(", vecOpCode=").append(vecOpCode);
		builder.append(", alarmName=").append(alarmName);
		builder.append(", progTxt=").append(progTxt);
		builder.append(", rsltTxt=").append(rsltTxt);
		builder.append(", patGenNum=").append(patGenNum);
		builder.append(", numFailPinBits=").append(numFailPinBits);
		builder.append(", numEnCompBits=").append(numEnCompBits);
		builder.append(", rtnIndex=").append(Arrays.toString(rtnIndex));
		builder.append(", rtnState=").append(Arrays.toString(rtnState));
		builder.append(", pgmIndex=").append(Arrays.toString(pgmIndex));
		builder.append(", pgmState=").append(Arrays.toString(pgmState));
		builder.append(", failPin=").append(Arrays.toString(failPin));
		builder.append(", enComps=").append(Arrays.toString(enComps));
		builder.append(", id=").append(id);
		builder.append(", headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
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
		result = prime * result + alarmName.hashCode();
		result = prime * result + (int) (cycleCount ^ (cycleCount >>> 32));
		result = prime * result + Arrays.hashCode(enComps);
		result = prime * result + Arrays.hashCode(failPin);
		result = prime * result + headNumber;
		result = prime * result + id.hashCode();
		result = prime * result + numEnCompBits;
		result = prime * result + (int) (numFail ^ (numFail >>> 32));
		result = prime * result + numFailPinBits;
		result = prime * result + optFlags.hashCode();
		result = prime * result + patGenNum;
		result = prime * result + Arrays.hashCode(pgmIndex);
		result = prime * result + Arrays.hashCode(pgmState);
		result = prime * result + progTxt.hashCode();
		result = prime * result + (int) (relVaddr ^ (relVaddr >>> 32));
		result = prime * result + (int) (rptCnt ^ (rptCnt >>> 32));
		result = prime * result + rsltTxt.hashCode();
		result = prime * result + Arrays.hashCode(rtnIndex);
		result = prime * result + Arrays.hashCode(rtnState);
		result = prime * result + siteNumber;
		result = prime * result + testFlags.hashCode();
		result = prime * result + timeSetName.hashCode();
		result = prime * result + vecName.hashCode();
		result = prime * result + vecOffset;
		result = prime * result + vecOpCode.hashCode();
		result = prime * result + xFailAddr;
		result = prime * result + yFailAddr;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (!(obj instanceof FunctionalTestRecord)) return false;
		FunctionalTestRecord other = (FunctionalTestRecord) obj;
		if (!alarmName.equals(other.alarmName)) return false;
		if (cycleCount != other.cycleCount) return false;
		if (!Arrays.equals(enComps, other.enComps)) return false;
		if (!Arrays.equals(failPin, other.failPin)) return false;
		if (headNumber != other.headNumber) return false;
		if (id != other.id) return false;
		if (numEnCompBits != other.numEnCompBits) return false;
		if (numFail != other.numFail) return false;
		if (numFailPinBits != other.numFailPinBits) return false;
		if (!optFlags.equals(other.optFlags)) return false;
		if (patGenNum != other.patGenNum) return false;
		if (!Arrays.equals(pgmIndex, other.pgmIndex)) return false;
		if (!Arrays.equals(pgmState, other.pgmState)) return false;
		if (!progTxt.equals(other.progTxt)) return false;
		if (relVaddr != other.relVaddr) return false;
		if (rptCnt != other.rptCnt) return false;
		if (!rsltTxt.equals(other.rsltTxt)) return false;
		if (!Arrays.equals(rtnIndex, other.rtnIndex)) return false;
		if (!Arrays.equals(rtnState, other.rtnState)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!testFlags.equals(other.testFlags)) return false;
		if (!timeSetName.equals(other.timeSetName)) return false;
		if (!vecName.equals(other.vecName)) return false;
		if (vecOffset != other.vecOffset) return false;
		if (!vecOpCode.equals(other.vecOpCode)) return false;
		if (xFailAddr != other.xFailAddr) return false;
		if (yFailAddr != other.yFailAddr) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getTestId()
	 */
	@Override
	public TestID getTestId()
	{
		return(id);
	}

   
}













