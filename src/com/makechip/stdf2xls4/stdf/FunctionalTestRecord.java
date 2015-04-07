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

import java.util.EnumSet;

import com.makechip.util.Log;

import static com.makechip.stdf2xls4.stdf.TestFlag_t.*;
/**
*** @author eric
*** @version $Id: FunctionalTestRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class FunctionalTestRecord extends StdfRecord
{
	private final TestID testId;
    private final int testNumber;
    private final short headNumber;
    private final short siteNumber;
    private final EnumSet<TestFlag_t> testFlags;
    private final EnumSet<FTROptFlag_t> optFlags;
    private final int cycleCount;    // cycle count is invalid if CYCLE_CNT_INVALID is set
    private final int relVaddr;      // relVaddr is invalid if REL_VADDR_INVALID is set
    private final int rptCnt;        // rptCnt is invalid if REPEAT_CNT_INVALID is set
    private final int numFail;       // numFail is invalid if NUM_FAIL_INVALID is set
    private final int xFailAddr;     // xFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
    private final int yFailAddr;     // yFailAddr is invalid if XY_FAIL_ADDR_INVALID is set
    private final short vecOffset;   // vecOffset is invalid if VEC_OFFSET_INVALID is set
    private final int[] rtnIndex;
    private final byte[] rtnState;
    private final int[] pgmIndex;
    private final byte[] pgmState;
    private final byte[] failPin;
    private final String vecName;
    private final String timeSetName;
    private final String vecOpCode;
    private String label;
    private final String alarmName;
    private final String progTxt;
    private final String rsltTxt;
    private final short patGenNum;
    private final byte[] enComps;
    
    public FunctionalTestRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.FTR, sequenceNumber, devNum, data);
        testNumber = getU4(MISSING_INT);
        headNumber = getU1((short) 0);
        siteNumber = getU1((short) 0);
        testFlags = TestFlag_t.getBits(getByte());
        if (getSize() <= getPtr()) optFlags = null;
        else optFlags = FTROptFlag_t.getBits(getByte());
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
        label = getCn();
        alarmName = getCn();
        progTxt = getCn();
        rsltTxt = getCn();
        testId = null;
        patGenNum = getU1(MISSING_SHORT);
        enComps = getDn();
    }
    
    public FunctionalTestRecord(
        final int sequenceNumber,
        final int deviceNumber,
        final int testNumber,
        final short headNumber,
        final short siteNumber,
        final EnumSet<TestFlag_t> testFlags,
        final EnumSet<FTROptFlag_t> optFlags,
        final int cycleCount,
        final int relVaddr,
        final int rptCnt,
        final int numFail,
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
        this.testFlags = testFlags;
        this.optFlags = optFlags;
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
        this.label = label;
        this.alarmName = alarmName;
        this.progTxt = progTxt;
        this.rsltTxt = rsltTxt;
        this.patGenNum = patGenNum;
        this.enComps = enComps;
        testId  = TestID.getTestID(testNumber, label);
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
            list.addAll(getCnBytes(label));
            list.addAll(getCnBytes(alarmName));
            list.addAll(getCnBytes(progTxt));
            list.addAll(getCnBytes(rsltTxt));
            list.addAll(getU1Bytes(patGenNum));
            list.addAll(getDnBytes(enComps));
        }
        bytes = list.toArray();
    }
  
    FunctionalTestRecord(FunctionalTestRecord ftr, DefaultFTRValueMap dmap)
    {
    	super(ftr.getRecordType(), ftr.getSequenceNumber(), ftr.getDeviceNumber(), null);
    	testNumber = ftr.getTestNumber();
        if (ftr.getOptFlags() == null) optFlags = dmap.foptDefaults.get(testNumber);
        else optFlags = ftr.getOptFlags();
        if (ftr.getPatGenNum() == (short) -1) patGenNum = dmap.pgDefaults.get(testNumber);
        else patGenNum = ftr.getPatGenNum();
        if (ftr.getEnComps().length == 0) enComps = dmap.ecDefaults.get(testNumber);
        else enComps = ftr.getEnComps();
        if (ftr.getTestName() == null) label = dmap.tnameDefaults.get(testNumber);
        else label = ftr.getTestName();
        yFailAddr = ftr.getyFailAddr();
        xFailAddr = ftr.getxFailAddr();
        vecOpCode = ftr.getVecOpCode();
        vecOffset = ftr.getVecOffset();
        vecName = ftr.getVecName();
        timeSetName = ftr.getTimeSetName();
        testId = TestID.getTestID(testNumber, label);
        testFlags = ftr.getTestFlags();
        siteNumber = ftr.getSiteNumber();
        headNumber = ftr.getHeadNumber();
        rtnState = ftr.getRtnState();
        rtnIndex = ftr.getRtnIndex();
        rsltTxt = ftr.getRsltTxt();
        rptCnt = ftr.getRptCnt();
        relVaddr = ftr.getRelVaddr();
        progTxt = ftr.getProgTxt();
        pgmState = ftr.getPgmState();
        pgmIndex = ftr.getPgmIndex();
        numFail = ftr.getNumFail();
        failPin = ftr.getFailPin();
        cycleCount = ftr.getCycleCount();
        alarmName = ftr.getAlarmName();
    }
    
    public boolean alarm() { return(testFlags.contains(ALARM)); }
    public boolean unreliable() { return(testFlags.contains(UNRELIABLE)); }
    public boolean timeout() { return(testFlags.contains(TIMEOUT)); }
    public boolean notExecuted() { return(testFlags.contains(NOT_EXECUTED)); }
    public boolean abort() { return(testFlags.contains(ABORT)); }
    public boolean noPassFailIndication() { return(testFlags.contains(NO_PASS_FAIL)); }
    public boolean fail() { return(testFlags.contains(FAIL)); }
    
    public TestID getTestID() { return(testId); }
    
    /**
     * Required Field.
     * @return the testNumber
     */
    public int getTestNumber() { return(testNumber); }

    /**
     * Required Field.
     * @return the headNumber
     */
    public short getHeadNumber() { return(headNumber); }

    /**
     * Required Field.
     * @return the siteNumber
     */
    public short getSiteNumber() { return(siteNumber); }

    /**
     * Required Field.
     * @return the testFlags
     */
    public EnumSet<TestFlag_t> getTestFlags() { return(testFlags); }

    /**
     * Optional field.  If missing, no other fields follow.
     * Default value is 0.
     * @return the optFlags
     */
    public EnumSet<FTROptFlag_t> getOptFlags() { return(optFlags); }

    /**
     * Can be missing; invalid indicated by optFlag bit 0.
     * @return the cycleCount
     */
    public int getCycleCount()
    {
        if (optFlags.contains(FTROptFlag_t.CYCLE_CNT_INVALID)) return(-1);
        return(cycleCount);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 1.
     * @return the relVaddr
     */
    public int getRelVaddr()
    {
        if (optFlags.contains(FTROptFlag_t.REL_VADDR_INVALID)) return(-1);
        return(relVaddr);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 2.
     * @return the rptCnt
     */
    public int getRptCnt()
    {
        if (optFlags.contains(FTROptFlag_t.REPEAT_CNT_INVALID)) return(-1);
        return(rptCnt);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 3.
     * @return the numFail
     */
    public int getNumFail()
    {
        if (optFlags.contains(FTROptFlag_t.NUM_FAIL_INVALID)) return(-1);
        return(numFail);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 4.
     * @return the xFailAddr
     */
    public int getxFailAddr()
    {
        if (optFlags.contains(FTROptFlag_t.XY_FAIL_ADDR_INVALID)) return(Integer.MIN_VALUE);
        return(xFailAddr);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 4.
     * @return the yFailAddr
     */
    public int getyFailAddr()
    {
        if (optFlags.contains(FTROptFlag_t.XY_FAIL_ADDR_INVALID)) return(Integer.MIN_VALUE);
        return(yFailAddr);
    }

    /**
     * Can be missing; invalid indicated by optFlag bit 5.
     * @return the vecOffset
     */
    public short getVecOffset()
    {
        if (optFlags.contains(FTROptFlag_t.VEC_OFFSET_INVALID)) return(Short.MIN_VALUE);
        return(vecOffset);
    }

    /**
     * Can be missing. Default value is 0-length array, indicated by RTN_ICNT = 0.
     * @return the rtnIndex
     */
    public int[] getRtnIndex() { return(rtnIndex); }

    /**
     * Can be missing. Default value is 0-length array, indicated by RTN_ICNT = 0.
     * @return the rtnState
     */
    public byte[] getRtnState() { return(rtnState); }

    /**
     * Can be missing. Default value is 0-length array, indicated by PGM_ICNT = 0.
     * @return the pgmIndex
     */
    public int[] getPgmIndex() { return(pgmIndex); }

    /**
     * Can be missing. Default value is 0-length array, indicated by PGM_ICNT = 0.
     * @return the pgmState
     */
    public byte[] getPgmState() { return(pgmState); }

    /**
     * Can be missing. Default value is 0-length array.
     * @return the failPin
     */
    public byte[] getFailPin() { return(failPin); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the vecName
     */
    public String getVecName() { return(vecName); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the timeSetName
     */
    public String getTimeSetName() { return(timeSetName); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the vecOpCode
     */
    public String getVecOpCode() { return(vecOpCode); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the label
     */
    public String getTestName() { return(label); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the alarmName
     */
    public String getAlarmName() { return(alarmName); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the progTxt
     */
    public String getProgTxt() { return(progTxt); }

    /**
     * Can be missing. Default value is 0-length string.
     * @return the rsltTxt
     */
    public String getRsltTxt() { return(rsltTxt); }

    /**
     * Can be missing. Default value is 255.
     * @return the patGenNum
     */
    public short getPatGenNum() { return(patGenNum); }

    /**
     * Can be missing. Default value is 0-length array.
     * @return the enComps
     */
    public byte[] getEnComps() { return(enComps); }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    testNumber: "); sb.append("" + testNumber); sb.append(Log.eol);
        sb.append("    headNumber: "); sb.append("" + headNumber); sb.append(Log.eol);
        sb.append("    siteNumber: "); sb.append("" + siteNumber); sb.append(Log.eol);
        sb.append("    testFlags:");
        for (TestFlag_t t : testFlags)
        {
            sb.append(" ");
            sb.append(t.toString());
        }
        sb.append(Log.eol);
        sb.append("    optFlags:");
        for (FTROptFlag_t d : optFlags)
        {
            sb.append(" ");
            sb.append(d.toString());
        }
        sb.append(Log.eol);
        sb.append("    cycleCount: "); sb.append("" + cycleCount); sb.append(Log.eol);
        sb.append("    relVaddr: "); sb.append("" + relVaddr); sb.append(Log.eol);
        sb.append("    rptCnt: "); sb.append("" + rptCnt); sb.append(Log.eol);
        sb.append("    numFail: "); sb.append("" + numFail); sb.append(Log.eol);
        sb.append("    xFailAddr: "); sb.append("" + xFailAddr); sb.append(Log.eol);
        sb.append("    yFailAddr: "); sb.append("" + yFailAddr); sb.append(Log.eol);
        sb.append("    vecOffset: "); sb.append("" + vecOffset); sb.append(Log.eol);
        sb.append("    PMR indicies:"); sb.append(Log.eol);
        for (int i=0; i<rtnIndex.length; i++)
        {
            sb.append("       ");
            sb.append("" + rtnIndex[i]);
            sb.append(Log.eol);
        }
        sb.append("    Returned States:");
        for (int i=0; i<rtnState.length; i++)
        {
            sb.append(" ");
            sb.append("" + rtnState[i]);
        }
        sb.append(Log.eol);
        sb.append("    Programmed State Indicies:");
        for (int i=0; i<pgmIndex.length; i++)
        {
            sb.append(" ");
            sb.append("" + pgmIndex[i]);
        }
        sb.append(Log.eol);
        sb.append("    Programmed States:");
        for (int i=0; i<pgmState.length; i++)
        {
            sb.append(" ");;
            sb.append("" + pgmState[i]);
        }
        sb.append(Log.eol);
        sb.append("    Failing Pins:");
        for (int i=0; i<failPin.length; i++)
        {
            sb.append(" ");
            sb.append("" + failPin[i]);
        }
        sb.append(Log.eol);
        sb.append("    vecName: "); sb.append(vecName); sb.append(Log.eol);
        sb.append("    timeSetName: "); sb.append(timeSetName); sb.append(Log.eol);
        sb.append("    vecOpCode: "); sb.append(vecOpCode); sb.append(Log.eol);
        sb.append("    test name: "); sb.append(label); sb.append(Log.eol);
        sb.append("    alarmName: "); sb.append(alarmName); sb.append(Log.eol);
        sb.append("    progTxt: "); sb.append(progTxt); sb.append(Log.eol);
        sb.append("    rsltTxt: "); sb.append(rsltTxt); sb.append(Log.eol);
        sb.append("    patGetNum: " + patGenNum); sb.append(Log.eol);
        sb.append("    enabled Comparators:"); sb.append(Log.eol);
        if (enComps != null)
        {
            for (int i=0; i<enComps.length; i++)
            {
                sb.append(" ");
                sb.append("" + enComps[i]);
            }
        }
        sb.append(Log.eol);
        return(sb.toString());
    }
    
   
}













