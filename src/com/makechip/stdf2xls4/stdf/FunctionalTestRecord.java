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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.*;
/**
 *  This class holds the fields for an STDF FunctionalTestRecord.
 *  @author eric
 */
public class FunctionalTestRecord extends TestRecord
{
	/**
	 *  This is the TEST_NUM field.
	 */
    public final long testNumber;
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
    public final Long cycleCount;    // cycle count is invalid if CYCLE_CNT_INVALID is set
	/**
	 *  This is the REL_VADDR field.
	 */
    public final Long relVaddr;      // relVaddr is invalid if REL_VADDR_INVALID is set
	/**
	 *  This is the REPT_CNT field.
	 */
    public final Long rptCnt;        // rptCnt is invalid if REPEAT_CNT_INVALID is set
	/**
	 *  This is the NUM_FAIL field.
	 */
    public final Long numFail;       // numFail is invalid if NUM_FAIL_INVALID is set
	/**
	 *  This is the XFAIL_AD field.
	 */
    public final Integer xFailAddr;     // xFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
	/**
	 *  This is the YFAIL_AD field.
	 */
    public final Integer yFailAddr;     // yFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
	/**
	 *  This is the VECT_OFF field.
	 */
    public final Short vecOffset;   // vecOffset is invalid if VEC_OFFSET_INVALID is set
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
     *  This is the TEST_TXT field.
     */
    public final String testName;
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
    public final Short patGenNum;

    private final int[] rtnIndex;
    private final int numFailPinBits;
    private final byte[] rtnState;
    private final int[] pgmIndex;
    private final byte[] pgmState;
    private final byte[] failPin;
    private final int numEnCompBits;
    private final byte[] enComps;
    
    /**
     * Constructor to create an FTR record from the binary data stream.
     * @param tdb This parameter is used for tracking the Test ID.
     * @param dvd The DefaultValueDatabase is used to get the CPU type,
     * and to supply missing data with default values.
     * @param data Binary stream data.  This array should not contain
     * the first four bytes of the record.
     */
    public FunctionalTestRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        int l = 0;
        testNumber = cpu.getU4(is); l += U4.numBytes;
        headNumber = cpu.getU1(is); l += 1;
        siteNumber = cpu.getU1(is); l += 1;
        EnumSet<TestFlag_t> s = TestFlag_t.getBits(cpu.getI1(is)); l++;
        testFlags = Collections.unmodifiableSet(s);
        if (l >= recLen) optFlags =  null; 
        else
        {
        	optFlags = Collections.unmodifiableSet(FTROptFlag_t.getBits(cpu.getI1(is))); 
        	l++;
        }
        if (l >= recLen) cycleCount = null; 
        else
        {
        	cycleCount = cpu.getU4(is); 
        	l += U4.numBytes;
        }
        if (l >= recLen) relVaddr = null; 
        else
        {
        	relVaddr = cpu.getU4(is); 
        	l += U4.numBytes;
        }
        if (l >= recLen) rptCnt = null; 
        else
        {
        	rptCnt = cpu.getU4(is); 
        	l += U4.numBytes;
        }
        if (l >= recLen)  numFail = null; 
        else
        { 
        	numFail = cpu.getU4(is); 
        	l += U4.numBytes;
        }
        if (l >= recLen) xFailAddr = null; 
        else
        {
        	xFailAddr = cpu.getI4(is); 
        	l += I4.numBytes;
        }
        if (l >= recLen) yFailAddr = null; 
        else
        {
        	yFailAddr = cpu.getI4(is); 
        	l += I4.numBytes;
        }
        if (l >= recLen) vecOffset = null; 
        else
        {
        	vecOffset = cpu.getI2(is); 
        	l += I2.numBytes;
        }
        int j = 0;
        if (l < recLen) 
        {
        	j = cpu.getU2(is); 
        	l += U2.numBytes;
        }
        int k = 0;
        if (l < recLen) 
        {
        	k = cpu.getU2(is); 
        	l += U2.numBytes;
        }
        if (j > 0)
        {
            rtnIndex = new int[j];
            for (int i=0; i<j; i++) 
            {
            	rtnIndex[i] = cpu.getU2(is);
            	l += U2.numBytes;
            }
        }
        else rtnIndex = null;
        if (j > 0)
        {
            TByteArrayList list = new TByteArrayList();
            for (int i=0; i<j; i++)
            {
            	byte[] b = cpu.getN1(is);
            	list.addAll(b);
            	l++;
            }
            rtnState = list.toArray();
        }
        else rtnState = null;
        if (k > 0)
        {
            pgmIndex = new int[k];
            for (int i=0; i<k; i++) 
            {
            	pgmIndex[i] = cpu.getU2(is);
            	l += U2.numBytes;
            }
        }
        else pgmIndex = null;
        if (k > 0)
        {
            TByteArrayList list = new TByteArrayList();
            for (int i=0; i<k; i++)
            {
            	byte[] b = cpu.getN1(is);
                list.addAll(b);
                l++;
            }
            pgmState = list.toArray();
        }
        else pgmState = null;
        if (l >= recLen)
        {
        	failPin = null;
        	numFailPinBits = 0;
        }
        else
        {
        	numFailPinBits = cpu.getU2(is);
        	l += U2.numBytes;
        	failPin = cpu.getDN(numFailPinBits, is);
        	l += failPin.length;
        }
        if (l < recLen)
        {
            vecName = cpu.getCN(is);
            l += vecName.length() + 1;
        }
        else vecName = null;
        if (l < recLen)
        {
        	timeSetName = cpu.getCN(is);
        	l += timeSetName.length() + 1;
        }
        else timeSetName = null;
        if (l < recLen)
        {
        	vecOpCode = cpu.getCN(is);
        	l += vecOpCode.length() + 1;
        }
        else vecOpCode = null; 
        if (l < recLen)
        {
        	testName = cpu.getCN(is);
        	l += testName.length() + 1;
        }
        else testName = null;
        if (l < recLen)
        {
        	alarmName = cpu.getCN(is);
        	l += alarmName.length() + 1;
        }
        else alarmName = null;
        if (l < recLen)
        {
        	progTxt = cpu.getCN(is);
        	l += progTxt.length() + 1;
        }
        else progTxt = null;
        if (l < recLen)
        {
        	rsltTxt = cpu.getCN(is);
        	l += rsltTxt.length() + 1;
        }
        else rsltTxt = null;
        if (l < recLen)
        {
        	patGenNum = cpu.getU1(is);
        	l++;
        }
        else patGenNum = null;
        if (l >= recLen)
        {
        	enComps = null;
        	numEnCompBits = 0;
        }
        else
        {
        	numEnCompBits = cpu.getU2(is);
        	l += U2.numBytes;
        	enComps = cpu.getDN(numEnCompBits, is);
        	l += failPin.length;
        }
        if (l != recLen) throw new StdfException("FTR record length incorrect - recLen = " + recLen + " actual = " + l); 
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
     * @throws StdfException 
     * @throws IOException 
     */
     public FunctionalTestRecord(
        final Cpu_t cpu,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final byte testFlags,
        final Byte optFlags,
        final Long cycleCount,
        final Long relVaddr,
        final Long rptCnt,
        final Long numFail,
        final Integer xFailAddr,
        final Integer yFailAddr,
        final Short vecOffset,
        final int[] rtnIndex,
        final byte[] rtnState,
        final int[] pgmIndex,
        final byte[] pgmState,
        final Integer numFailPinBits,
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        final String testName,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final Short patGenNum,
        final Integer numEnCompBits,
        final byte[] enComps) throws IOException, StdfException
    {
    	this(cpu, 
    		 getRecLen(testNumber, headNumber, siteNumber, testFlags, 
    		 optFlags, cycleCount, relVaddr, rptCnt, numFail, xFailAddr, yFailAddr, 
    		 vecOffset, rtnIndex, rtnState, pgmIndex, pgmState, numFailPinBits, failPin, vecName, timeSetName, vecOpCode, 
    		 testName, alarmName, progTxt, rsltTxt, patGenNum, numEnCompBits, enComps),
    		 new  DataInputStream(new ByteArrayInputStream(toBytes(cpu, testNumber, headNumber, siteNumber, testFlags, 
    		 optFlags, cycleCount, relVaddr, rptCnt, numFail, xFailAddr, yFailAddr, 
    		 vecOffset, rtnIndex, rtnState, pgmIndex, pgmState, numFailPinBits, failPin, vecName, timeSetName, vecOpCode, 
    		 testName, alarmName, progTxt, rsltTxt, patGenNum, numEnCompBits, enComps))));
    }
    
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte tflags = (byte) testFlags.stream().mapToInt(b -> b.bit).sum();
		Byte oflags = null;
		if (optFlags != null)
		{
			byte o = (byte) optFlags.stream().mapToInt(b -> b.bit).sum();
			oflags = new Byte(o);
		}
    	byte[] b = toBytes(cpu, testNumber, headNumber, siteNumber, tflags, oflags, cycleCount, 
    			           relVaddr, rptCnt, numFail, xFailAddr, yFailAddr, vecOffset, rtnIndex, 
    			           rtnState, pgmIndex, pgmState, numFailPinBits, failPin, vecName, timeSetName, vecOpCode, 
    		               testName, alarmName, progTxt, rsltTxt, patGenNum, numEnCompBits, enComps);
    	TByteArrayList l = getHeaderBytes(cpu, Record_t.FTR, b.length);
    	l.addAll(b);
		return(l.toArray());
	}
    
    private static byte[] toBytes(
        Cpu_t cpu,
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final byte testFlags,
        final Byte optFlags,
        final Long cycleCount,
        final Long relVaddr,
        final Long rptCnt,
        final Long numFail,
        final Integer xFailAddr,
        final Integer yFailAddr,
        final Short vecOffset,
        final int[] rtnIndex,
        final byte[] rtnState,
        final int[] pgmIndex,
        final byte[] pgmState,
        final int numFailPinBits,
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        final String testName,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final Short patGenNum,
        final int numEnCompBits,
        final byte[] enComps)
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(cpu.getU4Bytes(testNumber));
        list.addAll(cpu.getU1Bytes(headNumber));
        list.addAll(cpu.getU1Bytes(siteNumber));
        list.add(testFlags);
        if (optFlags != null) list.add(optFlags);
        else return(list.toArray());
        if (cycleCount != null) list.addAll(cpu.getU4Bytes(cycleCount));
        else return(list.toArray());
        if (relVaddr != null) list.addAll(cpu.getU4Bytes(relVaddr));
        else return(list.toArray());
        if (rptCnt != null) list.addAll(cpu.getU4Bytes(rptCnt));
        else return(list.toArray());
        if (numFail != null) list.addAll(cpu.getU4Bytes(numFail));
        else return(list.toArray());
        if (xFailAddr != null) list.addAll(cpu.getI4Bytes(xFailAddr));
        else return(list.toArray());
        if (yFailAddr != null) list.addAll(cpu.getI4Bytes(yFailAddr));
        else return(list.toArray());
        if (vecOffset != null) list.addAll(cpu.getI2Bytes(vecOffset));
        else return(list.toArray());
        if (rtnIndex != null) list.addAll(cpu.getU2Bytes(rtnIndex.length));
        else return(list.toArray());
        if (pgmIndex != null) list.addAll(cpu.getU2Bytes(pgmIndex.length));
        else return(list.toArray());
        for (int i=0; i<rtnIndex.length; i++) list.addAll(cpu.getU2Bytes(rtnIndex[i]));
        if (rtnState != null) 
        {
        	for (int i=0; i<rtnIndex.length; i+=2) list.add(cpu.getN1Bytes(rtnState[i], rtnState[i+1]));
        }
        else return(list.toArray());
        for (int i=0; i<pgmIndex.length; i++) list.addAll(cpu.getU2Bytes(pgmIndex[i]));
        if (pgmState != null) for (int i=0; i<pgmIndex.length; i+=2) list.addAll(cpu.getN1Bytes(pgmState[i], pgmState[i+1]));
        else return(list.toArray());
        if (failPin != null) list.addAll(cpu.getU2Bytes(numFailPinBits));
        if (failPin != null) list.addAll(cpu.getDNBytes(numFailPinBits, failPin));
        else return(list.toArray());
        if (vecName != null) list.addAll(cpu.getCNBytes(vecName));
        else return(list.toArray());
        if (timeSetName != null) list.addAll(cpu.getCNBytes(timeSetName));
        else return(list.toArray());
        if (vecOpCode != null) list.addAll(cpu.getCNBytes(vecOpCode));
        else return(list.toArray());
        if (testName != null) list.addAll(cpu.getCNBytes(testName));
        else return(list.toArray());
        if (alarmName != null) list.addAll(cpu.getCNBytes(alarmName));
        else return(list.toArray());
        if (progTxt != null) list.addAll(cpu.getCNBytes(progTxt));
        else return(list.toArray());
        if (rsltTxt != null) list.addAll(cpu.getCNBytes(rsltTxt));
        else return(list.toArray());
        if (patGenNum != null) list.addAll(cpu.getU1Bytes(patGenNum));
        else return(list.toArray());
        if (enComps != null) list.addAll(cpu.getU2Bytes(numEnCompBits));
        if (enComps != null) list.addAll(cpu.getDNBytes(numEnCompBits, enComps));
        return(list.toArray());
    }
  
    private static int getRecLen(
        final long testNumber,
        final short headNumber,
        final short siteNumber,
        final byte testFlags,
        final Byte optFlags,
        final Long cycleCount,
        final Long relVaddr,
        final Long rptCnt,
        final Long numFail,
        final Integer xFailAddr,
        final Integer yFailAddr,
        final Short vecOffset,
        final int[] rtnIndex,
        final byte[] rtnState,
        final int[] pgmIndex,
        final byte[] pgmState,
        final int numFailPinBits,
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        final String testName,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final Short patGenNum,
        final int numEnCompBits,
        final byte[] enComps)
    {
    	int l = U4.numBytes + U1.numBytes + U1.numBytes + 1;
        if (optFlags != null) l++; else return(l);
        if (cycleCount != null) l += U4.numBytes; else return(l);
        if (relVaddr != null) l += U4.numBytes; else return(l);
        if (rptCnt != null) l += U4.numBytes; else return(l);
        if (numFail != null) l+= U4.numBytes; else return(l);
        if (xFailAddr != null) l += I4.numBytes; else return(l);
        if (yFailAddr != null) l += I4.numBytes; else return(l);
        if (vecOffset != null) l += I2.numBytes; else return(l);
        if (rtnIndex != null) l += U2.numBytes * (1 + rtnIndex.length); else return(l);
        if (rtnState != null) l += U2.numBytes + (rtnState.length / 2); else return(l);
        if (pgmIndex != null) l += U2.numBytes * (1 + pgmIndex.length); else return(l);
        if (pgmState != null) l += U2.numBytes + (pgmState.length / 2); else return(l);
        if (failPin != null) l += U2.numBytes + failPin.length; else return(l);
        if (vecName != null) l += 1 + vecName.length(); else return(l);
        if (timeSetName != null) l += 1 + timeSetName.length(); else return(l);
        if (vecOpCode != null) l += 1 + vecOpCode.length(); else return(l);
        if (testName != null) l += 1 + testName.length(); else return(l);
        if (alarmName != null) l += 1 + alarmName.length(); else return(l);
        if (progTxt != null) l += 1 + progTxt.length(); else return(l);
        if (rsltTxt != null) l += 1 + rsltTxt.length(); else return(l);
        if (patGenNum != null) l += U1.numBytes; else return(l);
        if (enComps != null) l += U2.numBytes + enComps.length;
    	return(l);
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
     * @return A copy of the RTN_STAT array. Note that each element in the array contains
     * only one nibble.
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
     * Get the failing pin bit-field. 
     * @return A copy of the failPin (FAIL_PIN) array. Note that each array element
     * contains only one nibble.
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
		builder.append("FunctionalTestRecord [testNumber=").append(testNumber);
		builder.append(", headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", testFlags=").append(testFlags);
		if (optFlags != null) builder.append(", optFlags=").append(optFlags);
		if (cycleCount != null) builder.append(", cycleCount=").append(cycleCount);
		if (relVaddr != null) builder.append(", relVaddr=").append(relVaddr);
		if (rptCnt != null) builder.append(", rptCnt=").append(rptCnt);
		if (numFail != null) builder.append(", numFail=").append(numFail);
		if (xFailAddr != null) builder.append(", xFailAddr=").append(xFailAddr);
		if (yFailAddr != null) builder.append(", yFailAddr=").append(yFailAddr);
		if (vecOffset != null) builder.append(", vecOffset=").append(vecOffset);
		if (vecName != null) builder.append(", vecName=").append(vecName);
		if (timeSetName != null) builder.append(", timeSetName=").append(timeSetName);
		if (vecOpCode != null) builder.append(", vecOpCode=").append(vecOpCode);
		if (testName != null) builder.append(", testName=").append(testName);
		if (alarmName != null) builder.append(", alarmName=").append(alarmName);
		if (progTxt != null) builder.append(", progTxt=").append(progTxt);
		if (rsltTxt != null) builder.append(", rsltTxt=").append(rsltTxt);
		if (patGenNum != null) builder.append(", patGenNum=").append(patGenNum);
		if (rtnIndex != null) builder.append(", rtnIndex=").append(Arrays.toString(rtnIndex));
		if (rtnState != null) builder.append(", rtnState=").append(Arrays.toString(rtnState));
		if (pgmIndex != null) builder.append(", pgmIndex=").append(Arrays.toString(pgmIndex));
		if (pgmState != null) builder.append(", pgmState=").append(Arrays.toString(pgmState));
		if (failPin != null)
		{
		    builder.append(", numFailPinBits=").append(numFailPinBits);
			builder.append(", failPin=").append(Arrays.toString(failPin));
		}
		if (enComps != null)
		{
		    builder.append(", numEnCompBits=").append(numEnCompBits);
			builder.append(", enComps=").append(Arrays.toString(enComps));
		}
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
		int result = 1;
		result = prime * result + ((alarmName == null) ? 0 : alarmName.hashCode());
		result = prime * result + ((cycleCount == null) ? 0 : cycleCount.hashCode());
		if (enComps != null)
		{
		    result = prime * result + numEnCompBits;
		    result = prime * result + Arrays.hashCode(enComps);
		}
		if (failPin != null)
		{
		    result = prime * result + numFailPinBits;
		    result = prime * result + Arrays.hashCode(failPin);
		}
		result = prime * result + headNumber;
		result = prime * result + ((numFail == null) ? 0 : numFail.hashCode());
		result = prime * result + ((optFlags == null) ? 0 : optFlags.hashCode());
		result = prime * result + ((patGenNum == null) ? 0 : patGenNum.hashCode());
		if (pgmIndex != null) result = prime * result + Arrays.hashCode(pgmIndex);
		if (pgmState != null) result = prime * result + Arrays.hashCode(pgmState);
		result = prime * result + ((progTxt == null) ? 0 : progTxt.hashCode());
		result = prime * result + ((relVaddr == null) ? 0 : relVaddr.hashCode());
		result = prime * result + ((rptCnt == null) ? 0 : rptCnt.hashCode());
		result = prime * result + ((rsltTxt == null) ? 0 : rsltTxt.hashCode());
		if (rtnIndex != null) result = prime * result + Arrays.hashCode(rtnIndex);
		if (rtnState != null) result = prime * result + Arrays.hashCode(rtnState);
		result = prime * result + siteNumber;
		result = prime * result + ((testFlags == null) ? 0 : testFlags.hashCode());
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + (int) (testNumber ^ (testNumber >>> 32));
		result = prime * result + ((timeSetName == null) ? 0 : timeSetName.hashCode());
		result = prime * result + ((vecName == null) ? 0 : vecName.hashCode());
		result = prime * result + ((vecOffset == null) ? 0 : vecOffset.hashCode());
		result = prime * result + ((vecOpCode == null) ? 0 : vecOpCode.hashCode());
		result = prime * result + ((xFailAddr == null) ? 0 : xFailAddr.hashCode());
		result = prime * result + ((yFailAddr == null) ? 0 : yFailAddr.hashCode());
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
		if (!(obj instanceof FunctionalTestRecord)) return false;
		FunctionalTestRecord other = (FunctionalTestRecord) obj;
		if (alarmName == null)
		{
			if (other.alarmName != null) return false;
		} 
		else if (!alarmName.equals(other.alarmName)) return false;
		if (cycleCount == null)
		{
			if (other.cycleCount != null) return false;
		} 
		else if (!cycleCount.equals(other.cycleCount)) return false;
		if (!Arrays.equals(enComps, other.enComps)) return false;
		if (!Arrays.equals(failPin, other.failPin)) return false;
		if (headNumber != other.headNumber) return false;
		if (numEnCompBits != other.numEnCompBits) return false;
		if (numFail == null)
		{
			if (other.numFail != null) return false;
		} 
		else if (!numFail.equals(other.numFail)) return false;
		if (numFailPinBits != other.numFailPinBits) return false;
		if (optFlags == null)
		{
			if (other.optFlags != null) return false;
		} 
		else if (!optFlags.equals(other.optFlags)) return false;
		if (patGenNum == null)
		{
			if (other.patGenNum != null) return false;
		} 
		else if (!patGenNum.equals(other.patGenNum)) return false;
		if (!Arrays.equals(pgmIndex, other.pgmIndex)) return false;
		if (!Arrays.equals(pgmState, other.pgmState)) return false;
		if (progTxt == null)
		{
			if (other.progTxt != null) return false;
		} 
		else if (!progTxt.equals(other.progTxt)) return false;
		if (relVaddr == null)
		{
			if (other.relVaddr != null) return false;
		} 
		else if (!relVaddr.equals(other.relVaddr)) return false;
		if (rptCnt == null)
		{
			if (other.rptCnt != null) return false;
		} 
		else if (!rptCnt.equals(other.rptCnt)) return false;
		if (rsltTxt == null)
		{
			if (other.rsltTxt != null) return false;
		} 
		else if (!rsltTxt.equals(other.rsltTxt)) return false;
		if (!Arrays.equals(rtnIndex, other.rtnIndex)) return false;
		if (!Arrays.equals(rtnState, other.rtnState)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (testFlags == null)
		{
			if (other.testFlags != null) return false;
		} 
		else if (!testFlags.equals(other.testFlags)) return false;
		if (testName == null)
		{
			if (other.testName != null) return false;
		} 
		else if (!testName.equals(other.testName)) return false;
		if (testNumber != other.testNumber) return false;
		if (timeSetName == null)
		{
			if (other.timeSetName != null) return false;
		} 
		else if (!timeSetName.equals(other.timeSetName)) return false;
		if (vecName == null)
		{
			if (other.vecName != null) return false;
		} 
		else if (!vecName.equals(other.vecName)) return false;
		if (vecOffset == null)
		{
			if (other.vecOffset != null) return false;
		} 
		else if (!vecOffset.equals(other.vecOffset)) return false;
		if (vecOpCode == null)
		{
			if (other.vecOpCode != null) return false;
		} 
		else if (!vecOpCode.equals(other.vecOpCode)) return false;
		if (xFailAddr == null)
		{
			if (other.xFailAddr != null) return false;
		} 
		else if (!xFailAddr.equals(other.xFailAddr)) return false;
		if (yFailAddr == null)
		{
			if (other.yFailAddr != null) return false;
		} 
		else if (!yFailAddr.equals(other.yFailAddr)) return false;
		return true;
	}



   
}













