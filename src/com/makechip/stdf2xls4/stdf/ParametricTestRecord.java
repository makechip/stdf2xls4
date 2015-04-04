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
*** @version $Id: ParametricTestRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class ParametricTestRecord extends StdfRecord
{
	protected TestID testId;
	protected Tracker tracker;
    protected int testNumber;
    protected short headNumber;
    protected short siteNumber;
    protected EnumSet<TestFlag_t> testFlags;
    protected EnumSet<ParamFlag_t> paramFlags;
    protected float result; 
    protected String text;
    protected String alarmName;
    protected EnumSet<OptFlag_t> optFlags; 
    protected byte resScal; // if RES_SCAL_INVALID set, then use default res_scal
    // if LO_LIMIT_LLM_SCAL_INVALID set, then use default llmScal;
    // if NO_LO_LIMIT set then llmScal is invalid
    protected byte llmScal;
    // if HI_LIMIT_HLM_SCAL_INVALID set, then use default hlmScal;
    // if NO_HI_LIMIT set then hlmScal is invalid
    protected byte hlmScal; 
    // if LO_LIMIT_LLM_SCAL_INVALID set, then use default loLimit;
    // if NO_LO_LIMIT set then LoLimit is invalid
    protected float loLimit; 
    // if HI_LIMIT_HLM_SCAL_INVALID set, then use default hiLimit;
    // if NO_HI_LIMIT set then hiLimit is invalid
    protected float hiLimit;
    protected String units;
    protected String resFmt;
    protected String llmFmt;
    protected String hlmFmt;
    protected float loSpec; // if NO_LO_SPEC_LIMIT set then loSpec is invalid
    protected float hiSpec; // if NO_HI_SPEC_LIMIT set the hiSpec is invalid
    
    public boolean alarm() { return(testFlags.contains(ALARM)); }
    public boolean unreliable() { return(testFlags.contains(UNRELIABLE)); }
    public boolean timeout() { return(testFlags.contains(TIMEOUT)); }
    public boolean notExecuted() { return(testFlags.contains(NOT_EXECUTED)); }
    public boolean abort() { return(testFlags.contains(ABORT)); }
    public boolean noPassFailIndication() { return(testFlags.contains(NO_PASS_FAIL)); }
    public boolean fail() { return(testFlags.contains(FAIL)); }
    
    protected ParametricTestRecord(Record_t type, Tracker tracker, int sequenceNumber, int devNum, byte[] data)
    {
        super(type, sequenceNumber, devNum, data);
        this.tracker = tracker;
    }
    
    /**
    *** @param p1
    **/
    public ParametricTestRecord(Tracker tracker, int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PTR, sequenceNumber, devNum, data);
        this.tracker = tracker;
        optFlags = EnumSet.noneOf(OptFlag_t.class);
        testNumber = getU4(-1);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        byte f = getI1((byte) 0);
        testFlags = TestFlag_t.getBits(f);
        f = getI1((byte) 0);
        paramFlags = ParamFlag_t.getBits(f);
        result = getR4(MISSING_FLOAT);
        text = getCn(); // okay
        testId = tracker.dup.getId(testNumber, text);
        alarmName = getCn();
        if (data.length <= getPtr())
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
            if (llmScal == MISSING_BYTE) llmScal = tracker.nllmScalDefaults.put(testNumber, llmScal);
            if (llmScal == MISSING_BYTE) tracker.llmScalDefaults.put(testId, (byte) 0);
        }
        else llmScal = ls;
        if (tracker.llmScalDefaults.get(testId) == MISSING_BYTE) 
        {
            tracker.llmScalDefaults.put(testId, llmScal);
            tracker.nllmScalDefaults.put(testNumber, llmScal);
        }
        
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
        
        // scale limits and units here:
        int s = 0;
        if (tracker.scales.get(testId) == MISSING_INT)
        {
            s = getScale(result, hiLimit, resScal, llmScal, hlmScal);
            tracker.scales.put(testId, s);
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
        sb.append("    result: " + result); sb.append(Log.eol);
        sb.append("    test name: "); sb.append(text); sb.append(Log.eol);
        sb.append("    alarm name: "); sb.append(alarmName); sb.append(Log.eol);
        sb.append("    optional flags:");
        for (OptFlag_t o : optFlags)
        {
            sb.append(" ");
            sb.append(o.toString());
        }
        sb.append(Log.eol);
        sb.append("    result scaling exponent: " + resScal); sb.append(Log.eol);
        sb.append("    low limit scaling exponent: " + llmScal); sb.append(Log.eol);
        sb.append("    high limit scaling exponent: " + hlmScal); sb.append(Log.eol);
        sb.append("    low limit: " + loLimit); sb.append(Log.eol);
        sb.append("    high limit: " + hiLimit); sb.append(Log.eol);
        sb.append("    units: "); sb.append(units); sb.append(Log.eol);
        sb.append("    result format string: "); sb.append(resFmt); sb.append(Log.eol);
        sb.append("    low limit format string: "); sb.append(llmFmt); sb.append(Log.eol);
        sb.append("    high limit format string: "); sb.append(hlmFmt); sb.append(Log.eol);
        sb.append("    low spec limit value: " + loSpec); sb.append(Log.eol);
        sb.append("    high spec limit value: " + hiSpec); sb.append(Log.eol);
        return(sb.toString());
    }

    public TestID getTestID() { return(testId); }
    
    /**
     * @return the optFlags
     */
    public EnumSet<OptFlag_t> getOptFlags()
    {
        return(optFlags);
    }

    /**
     * @return the resScal
     */
    public byte getResScal()
    {
        return(resScal);
    }

    /**
     * @return the llmScal
     */
    public byte getLlmScal()
    {
        return(llmScal);
    }

    /**
     * @return the hlmScal
     */
    public byte getHlmScal()
    {
        return(hlmScal);
    }

    /**
     * @return the loLimit
     */
    public float getLoLimit()
    {
        if (optFlags.contains(OptFlag_t.NO_LO_LIMIT)) return(MISSING_FLOAT); 
        return(loLimit);
    }

    /**
     * @return the hiLimit
     */
    public float getHiLimit()
    {
        if (optFlags.contains(OptFlag_t.NO_HI_LIMIT)) return(MISSING_FLOAT); 
        return(hiLimit);
    }

    /**
     * @return the units
     */
    public String getUnits()
    {
        return(units);
    }

    /**
     * @return the resFmt
     */
    public String getResFmt()
    {
        return(resFmt);
    }

    /**
     * @return the llmFmt
     */
    public String getLlmFmt()
    {
        return(llmFmt);
    }

    /**
     * @return the hlmFmt
     */
    public String getHlmFmt()
    {
        return(hlmFmt);
    }

    /**
     * @return the loSpec
     */
    public float getLoSpec()
    {
        if (optFlags.contains(OptFlag_t.NO_LO_SPEC_LIMIT)) return(MISSING_FLOAT);
        return(loSpec);
    }

    /**
     * @return the hiSpec
     */
    public float getHiSpec()
    {
        if (optFlags.contains(OptFlag_t.NO_HI_SPEC_LIMIT)) return(MISSING_FLOAT);
        return(hiSpec);
    }

    /**
     * @return the testNumber
     */
    public long getTestNumber()
    {
        return(testNumber);
    }


    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return(headNumber);
    }


    /**
     * @return the siteNumber
     */
    public short getSiteNumber()
    {
        return siteNumber;
    }


    /**
     * @return the testFlags
     */
    public EnumSet<TestFlag_t> getTestFlags()
    {
        return(testFlags);
    }


    /**
     * @return the paramFlags
     */
    public EnumSet<ParamFlag_t> getParamFlags()
    {
        return(paramFlags);
    }


    /**
     * @return the result
     */
    public float getResult()
    {
        return(result);
    }

    public float getScaledResult()
    {
    	return(scaleValue(result, findScale()));
    }
    
    public float getScaledLoLimit()
    {
        if (optFlags.contains(OptFlag_t.NO_LO_LIMIT)) return(MISSING_FLOAT); 
        return(tracker.scaledLoLimits.get(testId));
    }
    
    public float getScaledHiLimit()
    {
        if (optFlags.contains(OptFlag_t.NO_HI_LIMIT)) return(MISSING_FLOAT); 
        return(tracker.scaledHiLimits.get(testId));
    }
    
    public String getScaledUnits()
    {
        return(tracker.scaledUnits.get(testId));
    }
    
    protected int findScale()
    {
        float llim = Math.abs(tracker.loLimDefaults.get(testId));
        float hlim = Math.abs(tracker.hiLimDefaults.get(testId));
        float val = 0.0f;
        if (optFlags.contains(OptFlag_t.NO_LO_LIMIT)) val = hlim;
        else if (optFlags.contains(OptFlag_t.NO_HI_LIMIT)) val = llim;
        else val = (hlim > llim) ? hlim : llim;
        int scale = 0;
        if (val <= 1.0E-6f) scale = 9;
        else if (val <= 0.001f) scale = 6;
        else if (val <= 1.0f) scale = 3;
        else if (val <= 1000.0f) scale = 0;
        else if (val <= 1000000.0f) scale = -3;
        else if (val <= 1E9f) scale = -6;
        else scale = -9;
        return(scale);
    }

    /**
     * @return the text
     */
    public String getTestName()
    {
        return(text);
    }


    /**
     * @return the alarmName
     */
    public String getAlarmName()
    {
        return(alarmName);
    }

}
