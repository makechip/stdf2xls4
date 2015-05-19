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

import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: FunctionalTestRecord.java 258 2008-10-22 01:22:44Z eric $
**/
public class FunctionalTestRecord extends StdfRecord
{
	public final long testNumber;
	public final short headNumber;
	public final short siteNumber;
	public final Set<TestFlag_t> testFlags;
    public final Set<FTROptFlag_t> optFlags;
    public final long cycleCount;    // cycle count is invalid if CYCLE_CNT_INVALID is set
    public final long relVaddr;      // relVaddr is invalid if REL_VADDR_INVALID is set
    public final long rptCnt;        // rptCnt is invalid if REPEAT_CNT_INVALID is set
    public final long numFail;       // numFail is invalid if NUM_FAIL_INVALID is set
    public final int xFailAddr;     // xFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
    public final int yFailAddr;     // yFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
    public final short vecOffset;   // vecOffset is invalid if VEC_OFFSET_INVALID is set
    public final String vecName;
    public final String timeSetName;
    public final String vecOpCode;
    public final String testName;
    public final String alarmName;
    public final String progTxt;
    public final String rsltTxt;
    public final short patGenNum;
    
    private final int[] rtnIndex;
    private final byte[] rtnState;
    private final int[] pgmIndex;
    private final byte[] pgmState;
    private final byte[] failPin;
    private final byte[] enComps;
    
    public FunctionalTestRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.FTR, sequenceNumber, devNum, data);
        testNumber = getU4(MISSING_INT);
        headNumber = getU1(MISSING_SHORT);
        siteNumber = getU1(MISSING_SHORT);
        EnumSet<TestFlag_t> s = TestFlag_t.getBits(getByte());
        testFlags = Collections.unmodifiableSet(s);
        if (getSize() <= getPtr()) optFlags = null;
        else 
        {
        	EnumSet<FTROptFlag_t> opt = FTROptFlag_t.getBits(getByte());
        	optFlags = Collections.unmodifiableSet(opt);
        }
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
        failPin = getDn();
        vecName = getCn();
        timeSetName = getCn();
        vecOpCode = getCn();
        testName = getCn();
        alarmName = getCn();
        progTxt = getCn();
        rsltTxt = getCn();
        patGenNum = getU1(MISSING_SHORT);
        enComps = getDn();
    }
    
    public FunctionalTestRecord(
        final int sequenceNumber,
        final int deviceNumber,
        final int testNumber,
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
        final byte[] failPin,
        final String vecName,
        final String timeSetName,
        final String vecOpCode,
        String label,
        final String alarmName,
        final String progTxt,
        final String rsltTxt,
        final short patGenNum,
        final byte[] enComps)
    {
        super(Record_t.FTR, sequenceNumber, deviceNumber, null);	
        this.testNumber = testNumber;
        this.headNumber = headNumber;
        this.siteNumber = siteNumber;
        EnumSet<TestFlag_t> s1 = TestFlag_t.getBits(testFlags);
        this.testFlags = Collections.unmodifiableSet(s1);
        EnumSet<FTROptFlag_t> s2 = FTROptFlag_t.getBits(optFlags);
        this.optFlags = Collections.unmodifiableSet(s2);
        this.cycleCount = cycleCount;
        this.relVaddr = relVaddr;
        this.rptCnt = rptCnt;
        this.numFail = numFail;
        this.xFailAddr = xFailAddr;
        this.yFailAddr = yFailAddr;
        this.vecOffset = vecOffset;
        this.rtnIndex = rtnIndex;
        this.rtnState = rtnState;
        this.pgmIndex = pgmIndex;
        this.pgmState = pgmState;
        this.failPin = failPin;
        this.vecName = vecName;
        this.timeSetName = timeSetName;
        this.vecOpCode = vecOpCode;
        this.testName = label;
        this.alarmName = alarmName;
        this.progTxt = progTxt;
        this.rsltTxt = rsltTxt;
        this.patGenNum = patGenNum;
        this.enComps = enComps;
    }
    
    @Override
    protected void toBytes()
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(getU4Bytes(testNumber));
        list.addAll(getU1Bytes(headNumber));
        list.addAll(getU1Bytes(siteNumber));
        byte b = 0;
        for (TestFlag_t t : testFlags) b |= t.getBit();
        list.add(b);
        if (optFlags != null)
        {
            b = 0;
            for (FTROptFlag_t t : optFlags) b |= t.getBit();
            list.add(b);
            list.addAll(getU4Bytes(cycleCount));
            list.addAll(getU4Bytes(relVaddr));
            list.addAll(getU4Bytes(rptCnt));
            list.addAll(getU4Bytes(numFail));
            list.addAll(getI4Bytes(xFailAddr));
            list.addAll(getI4Bytes(yFailAddr));
            list.addAll(getI2Bytes(vecOffset));
            list.addAll(getU2Bytes(rtnIndex.length));
            list.addAll(getU2Bytes(pgmIndex.length));
            for (int i=0; i<rtnIndex.length; i++) list.addAll(getU2Bytes(rtnIndex[i]));
            list.addAll(getNibbleBytes(rtnState));
            for (int i=0; i<pgmIndex.length; i++) list.addAll(getU2Bytes(pgmIndex[i]));
            list.addAll(getNibbleBytes(pgmState));
            list.addAll(getDnBytes(failPin));
            list.addAll(getCnBytes(vecName));
            list.addAll(getCnBytes(timeSetName));
            list.addAll(getCnBytes(vecOpCode));
            list.addAll(getCnBytes(testName));
            list.addAll(getCnBytes(alarmName));
            list.addAll(getCnBytes(progTxt));
            list.addAll(getCnBytes(rsltTxt));
            list.addAll(getU1Bytes(patGenNum));
            list.addAll(getDnBytes(enComps));
        }
        bytes = list.toArray();
    }
  
    public boolean alarm() { return(testFlags.contains(TestFlag_t.ALARM)); }
    public boolean unreliable() { return(testFlags.contains(TestFlag_t.UNRELIABLE)); }
    public boolean timeout() { return(testFlags.contains(TestFlag_t.TIMEOUT)); }
    public boolean notExecuted() { return(testFlags.contains(TestFlag_t.NOT_EXECUTED)); }
    public boolean abort() { return(testFlags.contains(TestFlag_t.ABORT)); }
    public boolean noPassFailIndication() { return(testFlags.contains(TestFlag_t.NO_PASS_FAIL)); }
    public boolean fail() { return(testFlags.contains(TestFlag_t.FAIL)); }
    
    public int[] getRtnIndex() { return(Arrays.copyOf(rtnIndex, rtnIndex.length)); }
    public byte[] getRtnState() { return(Arrays.copyOf(rtnState, rtnState.length)); }
    public int[] getPgmIndex() { return(Arrays.copyOf(pgmIndex, pgmIndex.length)); }
    public byte[] getPgmState() { return(Arrays.copyOf(pgmState, pgmState.length)); }
    public byte[] getFailPin() { return(Arrays.copyOf(failPin, failPin.length)); }
    public byte[] getEnComps() { return(Arrays.copyOf(enComps, enComps.length)); }

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
        sb.append("    optFlags:");
        optFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
        sb.append(Log.eol);
        sb.append("    cycleCount: ").append("" + cycleCount).append(Log.eol);
        sb.append("    relVaddr: ").append("" + relVaddr).append(Log.eol);
        sb.append("    rptCnt: ").append("" + rptCnt).append(Log.eol);
        sb.append("    numFail: ").append("" + numFail).append(Log.eol);
        sb.append("    xFailAddr: ").append("" + xFailAddr).append(Log.eol);
        sb.append("    yFailAddr: ").append("" + yFailAddr).append(Log.eol);
        sb.append("    vecOffset: ").append("" + vecOffset).append(Log.eol);
        sb.append("    PMR indicies:");
        Arrays.stream(rtnIndex).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    Returned States:");
        for (byte b : rtnState) sb.append(" " + b);
        sb.append(Log.eol);
        sb.append("    Programmed State Indicies:");
        Arrays.stream(pgmIndex).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    Programmed States:");
        for (byte b : pgmState) sb.append(" " + b);
        sb.append(Log.eol);
        sb.append("    Failing Pins:");
        for (byte b : failPin) sb.append(" " + b);
        sb.append(Log.eol);
        sb.append("    vecName: ").append(vecName).append(Log.eol);
        sb.append("    timeSetName: ").append(timeSetName).append(Log.eol);
        sb.append("    vecOpCode: ").append(vecOpCode).append(Log.eol);
        sb.append("    test name: ").append(testName).append(Log.eol);
        sb.append("    alarmName: ").append(alarmName).append(Log.eol);
        sb.append("    progTxt: ").append(progTxt).append(Log.eol);
        sb.append("    rsltTxt: ").append(rsltTxt).append(Log.eol);
        sb.append("    patGetNum: " + patGenNum).append(Log.eol);
        if (enComps != null)
        {
            sb.append("    enabled Comparators:");
            for (byte b : enComps) sb.append(" " + b);
        }
        sb.append(Log.eol);
        return(sb.toString());
    }
    
   
}













