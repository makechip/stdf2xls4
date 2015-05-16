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
/**
*** @author eric
*** @version $Id: ParametricTestRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class ParametricTestRecord extends StdfRecord
{
	protected RequiredParametricFields reqFields;
    protected double result; 
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
    
    public boolean alarm() { return(reqFields.alarm()); }
    public boolean unreliable() { return(reqFields.unreliable()); }
    public boolean timeout() { return(reqFields.timeout()); }
    public boolean notExecuted() { return(reqFields.notExecuted()); }
    public boolean abort() { return(reqFields.abort()); }
    public boolean noPassFailIndication() { return(reqFields.noPassFailIndication()); }
    public boolean fail() { return(reqFields.fail()); }
    
    
    /**
     * For Use by MultipleResultParametricTestRecord which inherits from this class.
     * @param type
     * @param tracker
     * @param sequenceNumber
     * @param devNum
     * @param data
     */
    protected ParametricTestRecord(Record_t type, int sequenceNumber, int devNum, byte[] data)
    {
        super(type, sequenceNumber, devNum, data);
    }
    
    /**
    *** @param p1
    **/
    public ParametricTestRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PTR, sequenceNumber, devNum, data);
        reqFields = new RequiredParametricFields(this);
        result = getR4(MISSING_FLOAT);
        getParametricFields();
        units = getCn();
        getLastFields();
    }
    
    protected void getLastFields()
    {
        resFmt = getCn();
        llmFmt = getCn();
        hlmFmt = getCn();
        loSpec = getR4(MISSING_FLOAT);
        hiSpec = getR4(MISSING_FLOAT);
    }
    
    protected void getParametricFields()
    {
        text = getCn(); 
        alarmName = getCn();
        if (getSize() <= getPtr()) optFlags = null;
        else optFlags = OptFlag_t.getBits(getByte());
        resScal = getI1(MISSING_BYTE); 
        llmScal = getI1(MISSING_BYTE);
        hlmScal = getI1(MISSING_BYTE); 
        loLimit = getR4(MISSING_FLOAT); 
        hiLimit = getR4(MISSING_FLOAT);
    }
    
    public ParametricTestRecord(
            final int sequenceNumber,
            final int deviceNumber,
            final int testNumber,
            final int headNumber,
            final int siteNumber,
            final EnumSet<TestFlag_t> testFlags,
            final EnumSet<ParamFlag_t> paramFlags,
    	    final float result, 
    	    final String text,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
    	    final byte resScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final byte llmScal,
    	    final byte hlmScal, 
    	    final float loLimit, 
    	    final float hiLimit,
    	    final String units,
    	    final String resFmt,
    	    final String llmFmt,
    	    final String hlmFmt,
    	    final float loSpec,
    	    final float hiSpec)
    {
        super(Record_t.PTR, sequenceNumber, deviceNumber, null);
        reqFields = new RequiredParametricFields(testNumber, headNumber, siteNumber, testFlags, paramFlags);
        this.result = result;
        this.text = text;
        this.alarmName = alarmName;
        if (optFlags != null)
        {
            this.optFlags = EnumSet.noneOf(OptFlag_t.class);
            optFlags.stream().forEach(p -> this.optFlags.add(p));
        }
        else this.optFlags = null;
        this.resScal = resScal;
        this.llmScal = llmScal;
        this.hlmScal = hlmScal;
        this.loLimit = loLimit;
        this.hiLimit = hiLimit;
        this.units = units;
        this.resFmt = resFmt;
        this.llmFmt = llmFmt;
        this.hlmFmt = hlmFmt;
        this.loSpec = loSpec;
        this.hiSpec = hiSpec;
    }
    
    @Override
    protected void toBytes()
    {
        TByteArrayList list = new TByteArrayList();
        list.addAll(getU4Bytes(reqFields.getTestNumber()));
        list.addAll(getU1Bytes(reqFields.getHeadNumber()));
        list.addAll(getU1Bytes(reqFields.getSiteNumber()));
        list.add((byte) reqFields.getTestFlags().stream().mapToInt(b -> b.getBit()).sum());
        list.add((byte) reqFields.getParamFlags().stream().mapToInt(b -> b.getBit()).sum());
        list.addAll(getR4Bytes((float) result));
        list.addAll(getCnBytes(text));
        list.addAll(getCnBytes(alarmName));
        if (optFlags != null)
        {
        	list.add((byte) optFlags.stream().mapToInt(b -> b.getBit()).sum());
            list.addAll(getI1Bytes(resScal));
            list.addAll(getI1Bytes(llmScal));
            list.addAll(getI1Bytes(hlmScal));
            list.addAll(getR4Bytes(loLimit));
            list.addAll(getR4Bytes(hiLimit));
            list.addAll(getCnBytes(units));
            list.addAll(getCnBytes(resFmt));
            list.addAll(getCnBytes(llmFmt));
            list.addAll(getCnBytes(hlmFmt));
            list.addAll(getR4Bytes(loSpec));
            list.addAll(getR4Bytes(hiSpec));
        }
        bytes = list.toArray();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append(reqFields.toString());
        sb.append("    result: " + result).append(Log.eol);
        sb.append("    test name: ").append(text).append(Log.eol);
        sb.append("    alarm name: ").append(alarmName).append(Log.eol);
        if (optFlags != null)
        {
            sb.append("    optional flags:");
        	optFlags.stream().forEach(o -> sb.append(" ").append(o.toString()));
        	sb.append(Log.eol);
        	sb.append("    result scaling exponent: " + resScal).append(Log.eol);
        	sb.append("    low limit scaling exponent: " + llmScal).append(Log.eol);
        	sb.append("    high limit scaling exponent: " + hlmScal).append(Log.eol);
        	sb.append("    low limit: " + loLimit).append(Log.eol);
        	sb.append("    high limit: " + hiLimit).append(Log.eol);
        	sb.append("    units: "); sb.append(units).append(Log.eol);
        	sb.append("    result format string: ").append(resFmt).append(Log.eol);
        	sb.append("    low limit format string: ").append(llmFmt).append(Log.eol);
        	sb.append("    high limit format string: ").append(hlmFmt).append(Log.eol);
        	sb.append("    low spec limit value: " + loSpec).append(Log.eol);
        	sb.append("    high spec limit value: " + hiSpec).append(Log.eol);
        }
        return(sb.toString());
    }

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
        return(reqFields.getTestNumber());
    }


    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return(reqFields.getHeadNumber());
    }


    /**
     * @return the siteNumber
     */
    public short getSiteNumber()
    {
        return(reqFields.getSiteNumber());
    }


    /**
     * @return the testFlags
     */
    public EnumSet<TestFlag_t> getTestFlags()
    {
        return(reqFields.getTestFlags());
    }


    /**
     * @return the paramFlags
     */
    public EnumSet<ParamFlag_t> getParamFlags()
    {
        return(reqFields.getParamFlags());
    }


    /**
     * @return the result
     */
    public double getResult()
    {
        return(result);
    }

    public float getScaledResult()
    {
    	//return(scaleValue(result, findScale()));
    	return(0.0f);
    }
    
    public float getScaledLoLimit()
    {
        //if (optFlags.contains(OptFlag_t.NO_LO_LIMIT)) return(MISSING_FLOAT); 
        //return(tracker.scaledLoLimits.get(testId));
    	return(0.0f);
    }
    
    public float getScaledHiLimit()
    {
        //if (optFlags.contains(OptFlag_t.NO_HI_LIMIT)) return(MISSING_FLOAT); 
        //return(tracker.scaledHiLimits.get(testId));
    	return(0.0f);
    }
    
    public String getScaledUnits()
    {
        //return(tracker.scaledUnits.get(testId));
    	return("");
    }
    
    protected int findScale()
    {
        float llim = 0.0f; // Math.abs(tracker.loLimDefaults.get(testId));
        float hlim = 0.0f; // Math.abs(tracker.hiLimDefaults.get(testId));
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
