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
    private final int j;
    private final int k;
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
        testNumber = getU4(-1);
        headNumber = getU1((short) 0);
        siteNumber = getU1((short) 0);
        byte x = getByte(); 
        testFlags = TestFlag_t.getBits(x);
        if (getSize() <= getPtr()) optFlags = null;
        else optFlags = FTROptFlag_t.getBits(getByte());
        cycleCount = getU4(-1);
        relVaddr = getU4(-1);
        rptCnt = getU4(-1);
        numFail = getU4(-1);
        xFailAddr = getI4(-1);
        yFailAddr = getI4(-1);
        vecOffset = getI2((short) -1);
        j = getU2(0);
        k = getU2(0);
        rtnIndex = new int[j];
        for (int i=0; i<j; i++) rtnIndex[i] = getU2(-1);
        rtnState = getNibbles(j); 
        pgmIndex = new int[k];
        for (int i=0; i<k; i++) pgmIndex[i] = getU2(-1);
        pgmState = getNibbles(k);
        failPin = getDn();
        vecName = getCn();
        timeSetName = getCn();
        vecOpCode = getCn();
        String name = getCn();
        if (name == null) label = null;
        else label = name;
        alarmName = getCn();
        progTxt = getCn();
        rsltTxt = getCn();
        testId = null;
        patGenNum = getU1((short) -1);
        enComps = getDn();
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
     * @return the testNumber
     */
    public int getTestNumber() { return(testNumber); }

    /**
     * @return the headNumber
     */
    public short getHeadNumber() { return(headNumber); }

    /**
     * @return the siteNumber
     */
    public short getSiteNumber() { return(siteNumber); }

    /**
     * @return the testFlags
     */
    public EnumSet<TestFlag_t> getTestFlags() { return(testFlags); }

    /**
     * @return the optFlags
     */
    public EnumSet<FTROptFlag_t> getOptFlags() { return(optFlags); }

    /**
     * @return the cycleCount
     */
    public int getCycleCount()
    {
        if (optFlags.contains(FTROptFlag_t.CYCLE_CNT_INVALID)) return(-1);
        return(cycleCount);
    }

    /**
     * @return the relVaddr
     */
    public int getRelVaddr()
    {
        if (optFlags.contains(FTROptFlag_t.REL_VADDR_INVALID)) return(-1);
        return(relVaddr);
    }

    /**
     * @return the rptCnt
     */
    public int getRptCnt()
    {
        if (optFlags.contains(FTROptFlag_t.REPEAT_CNT_INVALID)) return(-1);
        return(rptCnt);
    }

    /**
     * @return the numFail
     */
    public int getNumFail()
    {
        if (optFlags.contains(FTROptFlag_t.NUM_FAIL_INVALID)) return(-1);
        return(numFail);
    }

    /**
     * @return the xFailAddr
     */
    public int getxFailAddr()
    {
        if (optFlags.contains(FTROptFlag_t.XY_FAIL_ADDR_INVALID)) return(Integer.MIN_VALUE);
        return(xFailAddr);
    }

    /**
     * @return the yFailAddr
     */
    public int getyFailAddr()
    {
        if (optFlags.contains(FTROptFlag_t.XY_FAIL_ADDR_INVALID)) return(Integer.MIN_VALUE);
        return(yFailAddr);
    }

    /**
     * @return the vecOffset
     */
    public short getVecOffset()
    {
        if (optFlags.contains(FTROptFlag_t.VEC_OFFSET_INVALID)) return(Short.MIN_VALUE);
        return(vecOffset);
    }

    /**
     * @return the rtnIndex
     */
    public int[] getRtnIndex() { return(rtnIndex); }

    /**
     * @return the rtnState
     */
    public byte[] getRtnState() { return(rtnState); }

    /**
     * @return the pgmIndex
     */
    public int[] getPgmIndex() { return(pgmIndex); }

    /**
     * @return the pgmState
     */
    public byte[] getPgmState() { return(pgmState); }

    /**
     * @return the failPin
     */
    public byte[] getFailPin() { return(failPin); }

    /**
     * @return the vecName
     */
    public String getVecName() { return(vecName); }

    /**
     * @return the timeSetName
     */
    public String getTimeSetName() { return(timeSetName); }

    /**
     * @return the vecOpCode
     */
    public String getVecOpCode() { return(vecOpCode); }

    /**
     * @return the label
     */
    public String getTestName() { return(label); }

    /**
     * @return the alarmName
     */
    public String getAlarmName() { return(alarmName); }

    /**
     * @return the progTxt
     */
    public String getProgTxt() { return(progTxt); }

    /**
     * @return the rsltTxt
     */
    public String getRsltTxt() { return(rsltTxt); }

    /**
     * @return the patGenNum
     */
    public short getPatGenNum() { return(patGenNum); }

    /**
     * @return the enComps
     */
    public byte[] getEnComps() { return(enComps); }

    public void setTestName(String string)
    {
        label = string;
    }
    
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
        for (int i=0; i<j; i++)
        {
            sb.append("       ");
            sb.append("" + rtnIndex[i]);
            sb.append(Log.eol);
        }
        sb.append("    Returned States:");
        for (int i=0; i<j; i++)
        {
            sb.append(" ");
            sb.append("" + rtnState[i]);
        }
        sb.append(Log.eol);
        sb.append("    Programmed State Indicies:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + pgmIndex[i]);
        }
        sb.append(Log.eol);
        sb.append("    Programmed States:");
        for (int i=0; i<k; i++)
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













