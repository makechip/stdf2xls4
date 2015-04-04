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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.makechip.util.Log;
/**
*** @author eric
*** @version $Id: MultipleResultParametricRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public final class MultipleResultParametricRecord extends ParametricTestRecord
{
    private final int j;
    private final int k; 
    private byte[] rtnState;
    private float[] results; 
    private float startIn;
    private float incrIn;
    private int[] rtnIndex;
    private String unitsIn;
    private PinList pins;
    
    /**
    *** @param p1
    **/
    public MultipleResultParametricRecord(Tracker tracker, int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MPR, tracker, sequenceNumber, devNum, data);
        optFlags = EnumSet.noneOf(OptFlag_t.class);
        testNumber = getU4(-1);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        byte f = getI1((byte) 0);
        testFlags = TestFlag_t.getBits(f);
        f = getI1((byte) 0);
        paramFlags = ParamFlag_t.getBits(f);
        j = getU2(0);
        k = getU2(0); 
        rtnState = getNibbles(j);
        results = new float[k]; 
        for (int i=0; i<k; i++) results[i] = getR4(-Float.MAX_VALUE);
        text = getCn();
        testId = tracker.dup.getId(testNumber, text);
        alarmName = getCn();
        if (data.length >= getPtr())
        {
            optFlags = tracker.poptDefaults.get(testNumber);
            if (optFlags == null) optFlags = EnumSet.noneOf(OptFlag_t.class);
        }
        else
        {
            f = getI1((byte) 0);
            optFlags = OptFlag_t.getBits(f);
            if (tracker.poptDefaults.get(testNumber) == null)
            {
                tracker.poptDefaults.put(testNumber, optFlags);
            }
        }
        
        byte rs = getI1(MISSING_BYTE);
        if (rs == MISSING_BYTE || optFlags.contains(OptFlag_t.RES_SCAL_INVALID))
        {
            resScal = tracker.resScalDefaults.get(testId);
            if (resScal == MISSING_BYTE) resScal = tracker.nresScalDefaults.get(testNumber);
            if (resScal == MISSING_BYTE) tracker.resScalDefaults.put(testId, (byte) 0);
        }
        else resScal = rs;
        if (tracker.resScalDefaults.get(testId) == MISSING_BYTE) 
        {
            tracker.resScalDefaults.put(testId, resScal);
            tracker.nresScalDefaults.put(testNumber, resScal);
        }
        
        byte ls = getI1(MISSING_BYTE);
        if (ls == MISSING_BYTE || optFlags.contains(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID))
        {
            llmScal = tracker.llmScalDefaults.get(testId);
            if (llmScal == MISSING_BYTE) tracker.llmScalDefaults.put(testId, (byte) 0);
        }
        else llmScal = ls;
        if (tracker.llmScalDefaults.get(testId) == MISSING_BYTE) tracker.llmScalDefaults.put(testId, llmScal);
        
        byte hs = getI1(MISSING_BYTE); 
        if (hs == MISSING_BYTE || optFlags.contains(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID))
        {
            hlmScal = tracker.hlmScalDefaults.get(testId);
            if (hlmScal == MISSING_BYTE) hlmScal = tracker.nhlmScalDefaults.get(testNumber);
            if (hlmScal == MISSING_BYTE) tracker.hlmScalDefaults.put(testId, (byte) 0);
        }
        else hlmScal = hs;
        if (tracker.hlmScalDefaults.get(testId) == MISSING_BYTE) 
        {
            tracker.hlmScalDefaults.put(testId, hlmScal);
            tracker.nhlmScalDefaults.put(testNumber, hlmScal);
        }
        
        float ll = getR4(MISSING_FLOAT); 
        if (ll == MISSING_FLOAT || optFlags.contains(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID))
        {
            loLimit = tracker.loLimDefaults.get(testId);
            if (loLimit == MISSING_FLOAT) loLimit = tracker.nloLimDefaults.get(testNumber);
        }
        else loLimit = ll;
        if (tracker.loLimDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.loLimDefaults.put(testId, loLimit);
            tracker.nloLimDefaults.put(testNumber, loLimit);
        }
        float hl = getR4(MISSING_FLOAT);
        if (hl == MISSING_FLOAT || optFlags.contains(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID))
        {
            hiLimit = tracker.hiLimDefaults.get(testId);
            if (hiLimit == MISSING_FLOAT) hiLimit = tracker.nhiLimDefaults.get(testNumber);
        }
        else hiLimit = hl;
        if (tracker.hiLimDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.hiLimDefaults.put(testId, hiLimit);
            tracker.nhiLimDefaults.put(testNumber, hiLimit);
        }
        
        float si = getR4(MISSING_FLOAT);
        if (si == MISSING_FLOAT)
        {
            startIn = tracker.startInDefaults.get(testId);
            if (startIn == MISSING_FLOAT) startIn = tracker.nstartInDefaults.get(testNumber);
        }
        else startIn = si;
        if (tracker.startInDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.startInDefaults.put(testId, si);
            tracker.nstartInDefaults.put(testNumber, startIn);
        }
        
        float ii = getR4(MISSING_FLOAT);
        if (ii == MISSING_FLOAT)
        {
            incrIn = tracker.incrInDefaults.get(testId);
            if (incrIn == MISSING_FLOAT) incrIn = tracker.nincrInDefaults.get(testNumber);
        }
        else incrIn = ii;
        if (tracker.incrInDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.incrInDefaults.put(testId, ii);
            tracker.nincrInDefaults.put(testNumber, incrIn);
        }
        
        int[] ri = new int[j];
        for (int i=0; i<j; i++) ri[i] = getU2(-1);
        if (data.length >= getPtr())
        {
            rtnIndex = tracker.rtnIndexDefaults.get(testId);
            // use default pinlist
            pins = tracker.mprPins.get(testId);
            if (pins == null) pins = tracker.nmprPins.get(testNumber);
            if (pins == null) Log.msg("ERROR pins is NULL: tnum = " + testNumber + " tname = " + text);
        }
        else 
        {
            rtnIndex = ri;
            // generate pin name list
            List<String> list = new ArrayList<String>(ri.length);
            for (int i=0; i<ri.length; i++)
            {
                String pname = tracker.pinmap.get(siteNumber).get(ri[i]);
                //Log.msg("pin = " + pname);
                list.add(pname);
            }
            pins = PinList.getPinList(list);
            tracker.mprPins.put(testId, pins);
            tracker.nmprPins.put(testNumber, pins);
        }
        if (tracker.rtnIndexDefaults.get(testId) == null) tracker.rtnIndexDefaults.put(testId, ri);
        if (tracker.mprPins.get(testId) == null) tracker.mprPins.put(testId, pins); 
        
        
        String u = getCn();
        if (u == null) 
        {
            units = tracker.unitDefaults.get(testId);
            if (units == null) units = tracker.nunitDefaults.get(testNumber);
        }
        else units = u;
        if (tracker.unitDefaults.get(testId) == null) 
        {
            tracker.unitDefaults.put(testId, units);
            tracker.nunitDefaults.put(testNumber, units);
        }
        
        String ui = getCn();
        if (ui == null) 
        {
            unitsIn = tracker.unitsInDefaults.get(testId);
            if (unitsIn == null) unitsIn = tracker.nunitsInDefaults.get(testNumber);
        }
        else unitsIn = ui;
        if (tracker.unitsInDefaults.get(testId) == null) 
        {
            tracker.unitsInDefaults.put(testId, ui);
            tracker.nunitsInDefaults.put(testNumber, unitsIn);
        }
        
        // scale limits and units here:
        int s = 0;
        if (tracker.scales.get(testId) == MISSING_INT)
        {
        	if (results.length != 0)
        	{
        		s = getScale(results[0], hiLimit, resScal, llmScal, hlmScal);
        		tracker.scales.put(testId, s);
        	}
        }
        else s = tracker.scales.get(testId);
        if (tracker.scaledLoLimits.get(testId) == MISSING_FLOAT)
        {
            float sLoLim = scaleValue(loLimit, findScale());
            tracker.scaledLoLimits.put(testId, sLoLim);
        }
        if (tracker.scaledHiLimits.get(testId) == MISSING_FLOAT)
        {
            float sHiLim = scaleValue(hiLimit, findScale());
            tracker.scaledHiLimits.put(testId, sHiLim);
        }
        if (tracker.scaledUnits.get(testId) == null)
        {
            String un = scaleUnits(units, findScale());
            tracker.scaledUnits.put(testId, un);
        }
        
        String rf = getCn();
        if (rf == null) 
        {
            resFmt = tracker.resFmtDefaults.get(testId);
            if (resFmt == null) resFmt = tracker.nresFmtDefaults.get(testNumber);
        }
        else resFmt = rf;
        if (tracker.resFmtDefaults.get(testId) == null) 
        {
            tracker.resFmtDefaults.put(testId, resFmt);
            tracker.nresFmtDefaults.put(testNumber, resFmt);
        }
        
        String lf = getCn();
        if (lf == null) 
        {
            llmFmt = tracker.llmFmtDefaults.get(testId);
            if (llmFmt == null) llmFmt = tracker.nllmFmtDefaults.get(testNumber);
        }
        else llmFmt = lf;
        if (tracker.llmFmtDefaults.get(testId) == null) 
        {
            tracker.llmFmtDefaults.put(testId, llmFmt);
            tracker.nllmFmtDefaults.put(testNumber, llmFmt);
        }
        
        String hf = getCn();
        if (hf == null) 
        {
            hlmFmt = tracker.hlmFmtDefaults.get(testId);
            if (hlmFmt == null) hlmFmt = tracker.nhlmFmtDefaults.get(testNumber);
        }
        else hlmFmt = hf;
        if (tracker.hlmFmtDefaults.get(testId) == null) 
        {
            tracker.hlmFmtDefaults.put(testId, hlmFmt);
            tracker.nhlmFmtDefaults.put(testNumber, hlmFmt);
        }
        
        float lsp = getR4(MISSING_FLOAT);
        if (lsp == MISSING_FLOAT) 
        {
            loSpec = tracker.loSpecDefaults.get(testId);
            if (loSpec == MISSING_FLOAT) loSpec = tracker.nloSpecDefaults.get(testNumber);
        }
        else loSpec = lsp;
        if (tracker.loSpecDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.loSpecDefaults.put(testId, loSpec);
            tracker.nloSpecDefaults.put(testNumber, loSpec);
        }
        
        float hsp = getR4(MISSING_FLOAT);
        if (hsp == MISSING_FLOAT) 
        {
            hiSpec = tracker.hiSpecDefaults.get(testId);
            if (hiSpec == MISSING_FLOAT) hiSpec = tracker.nhiSpecDefaults.get(testNumber);
        }
        else hiSpec = hsp;
        if (tracker.hiSpecDefaults.get(testId) == MISSING_FLOAT) 
        {
            tracker.hiSpecDefaults.put(testId, hiSpec);
            tracker.nhiSpecDefaults.put(testNumber, hiSpec);
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    test number: " + testNumber); sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        sb.append("    test flags:");
        for (TestFlag_t t : testFlags)
        {
            sb.append(" ");
            sb.append(t.toString());
        }
        sb.append(Log.eol);
        sb.append("    param flags:");
        for (ParamFlag_t p : paramFlags)
        {
            sb.append(" ");
            sb.append(p.toString());
        }
        sb.append(Log.eol);
        sb.append("    returned states:");
        for (int i=0; i<rtnState.length; i++)
        {
            sb.append(" ");
            sb.append("" + rtnState[i]);
        }
        sb.append(Log.eol);
        sb.append("    results:");
        for (int i=0; i<results.length; i++)
        {
            sb.append(" ");
            sb.append("" + results[i]);
        }
        sb.append(Log.eol);
        sb.append("    test name: "); sb.append(text); sb.append(Log.eol);
        sb.append("    alarm name: "); sb.append(alarmName); sb.append(Log.eol);
        sb.append("    optional flags:");
        if (optFlags != null)
        {
            for (OptFlag_t o : optFlags)
            {
                sb.append(" ");
                sb.append(o.toString());
            }
        }
        sb.append(Log.eol);
        sb.append("    result scaling exponent: " + resScal); sb.append(Log.eol);
        sb.append("    low limit scaling exponent: " + llmScal); sb.append(Log.eol);
        sb.append("    high limit scaling exponent: " + hlmScal); sb.append(Log.eol);
        sb.append("    low limit: " + getLoLimit()); sb.append(Log.eol);
        sb.append("    high limit: " + getHiLimit()); sb.append(Log.eol);
        sb.append("    starting input value: " + getStartIn()); sb.append(Log.eol);
        sb.append("    increment of input condition: " + getIncrIn()); sb.append(Log.eol);
        if (rtnIndex != null)
        {
            sb.append("    array of PMR indicies:");
            for (int i=0; i<rtnIndex.length; i++)
            {
                sb.append(" ");
                sb.append("" + rtnIndex[i]);
            }
        }
        sb.append(Log.eol);
        sb.append("    units: "); sb.append(units); sb.append(Log.eol);
        sb.append("    input condition units: "); sb.append(unitsIn); sb.append(Log.eol);
        sb.append("    result format string: "); sb.append(resFmt); sb.append(Log.eol);
        sb.append("    low limit format string: "); sb.append(llmFmt); sb.append(Log.eol);
        sb.append("    high limit format string: "); sb.append(hlmFmt); sb.append(Log.eol);
        sb.append("    low spec limit value: " + getLoSpec()); sb.append(Log.eol);
        sb.append("    high spec limit value: " + getHiSpec()); sb.append(Log.eol);
        return(sb.toString());
    }
    
    public float[] getScaledResults()
    {
        float[] r = new float[results.length];
        for (int i=0; i<results.length; i++)
        {
            r[i] = scaleValue(results[i], findScale());
        }
        return(r);
    }

    public List<String> getPins() { return(pins.getPins()); }

    public int[] getRtnIndex()
    {
        return(rtnIndex);
    }

    public int getRtnIcnt()
    {
        return(j);
    }

    public int getRsltCnt()
    {
        return(k);
    }

    public byte[] getRtnState()
    {
        return(rtnState);
    }

    public float[] getResults()
    {
        return(results);
    }

    public float getStartIn()
    {
        return(startIn);
    }

    public float getIncrIn()
    {
        return(incrIn);
    }

    public String getUnitsIn()
    {
        return(unitsIn);
    }

}
