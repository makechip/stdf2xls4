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
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.*;
import static com.makechip.stdf2xls4.stdf.enums.OptFlag_t.*;
/**
 *  This class holds the fields for a Multiple Result Parametric Record.
 *  @author eric
 */
public final class MultipleResultParametricRecord extends ParametricRecord
{
	/**
	 * This is the TEST_TXT field.
	 */
	public final String testName;
	/**
	 *  This is the ALARM_ID field of the MultipleResultParametricRecord.
	 */
	public final String alarmName;
	/**
	 *  This is the OPT_FLAG field of the MultipleResultParametricRecord.
	 */
	public final Set<OptFlag_t> optFlags;
	/**
	 *  This is the RES_SCAL field of the MultipleResultParametricRecord.
	 */
	public final Byte resScal;
	/**
	 *  This is the LLM_SCAL  field of the MultipleResultParametricRecord.
	 */
	public final Byte llmScal;
	/**
	 *  This is the HLM_SCAL field of the MultipleResultParametricRecord.
	 */
	public final Byte hlmScal;
	/**
	 *  This is the LO_LIMIT field of the MultipleResultParametricRecord.
	 */
	public final Float loLimit;
	/**
	 *  This is the HI_LIMIT field of the MultipleResultParametricRecord.
	 */
	public final Float hiLimit;
	/**
	 *  This is the START_IN field of the MultipleResultParametricRecord.
	 */
    public final Float startIn;
	/**
	 *  This is the INCR_IN field of the MultipleResultParametricRecord.
	 */
    public final Float incrIn;
	/**
	 *  This is the UNITS field of the MultipleResultParametricRecord.
	 */
    public final String units;
	/**
	 *  This is the UNITS_IN field of the MultipleResultParametricRecord.
	 */
    public final String unitsIn;
	/**
	 *  This is the C_RESFMT field of the MultipleResultParametricRecord.
	 */
    public final String resFmt;
	/**
	 *  This is the C_LLMFMT field of the MultipleResultParametricRecord.
	 */
    public final String llmFmt;
	/**
	 *  This is the C_HLMFMT field of the MultipleResultParametricRecord.
	 */
    public final String hlmFmt;
	/**
	 *  This is the LO_SPEC field of the MultipleResultParametricRecord.
	 */
    public final Float loSpec;
	/**
	 *  This is the HI_SPEC field of the MultipleResultParametricRecord.
	 */
    public final Float hiSpec;

    public final IntList rtnState; // byte
    public final IntList rtnIndex;  // int
    public final FloatList results; // float
    /**
     * This is not a standard STDF field.  It is used to uniquely identify
     * this test.
     */
    public final TestID id;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     */
    public MultipleResultParametricRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super(cpu, Record_t.MPR, recLen, is);
        int l = 8;
        final int j = (l < recLen) ? cpu.getU2(is) : 0;
        l += U2.numBytes;
        final int k = (l < recLen) ? cpu.getU2(is) : 0;
        l += U2.numBytes;
        if (l < recLen)
        {
        	rtnState = new IntList(Data_t.N1, cpu, j, is);
        	l += (j + 1) / 2;
        }
        else rtnState = null;
        if (l < recLen)
        {
            results = new FloatList(cpu, k, is);
            l += R4.numBytes * results.size();
        }
        else results = null;
        if (l < recLen)
        {
            testName = cpu.getCN(is);
            l += 1 + testName.length();
        }
        else testName = null;
        id = TestID.createTestID(tdb, testNumber, siteNumber, headNumber, testName);
        if (l < recLen)
        {
            alarmName = cpu.getCN(is);
            l += 1 + alarmName.length();
        }
        else alarmName = null;
        if (l < recLen)
        {
        	optFlags = Collections.unmodifiableSet(OptFlag_t.getBits(cpu.getI1(is)));
        	l++;
        }
        else optFlags = null;
        if (l < recLen)
        {
            resScal = cpu.getI1(is);
            l++;
        }
        else resScal = null;
        if (l < recLen)
        {
            byte b = cpu.getI1(is);
        	llmScal = (optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) ? null : b;
            l++;
        }
        else llmScal = null;
        if (l < recLen)
        {
            byte b = cpu.getI1(is);
            hlmScal = (optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) ? null : b;
            l++;
        }
        else hlmScal = null;
        if (l < recLen)
        {
        	float b = cpu.getR4(is);
        	loLimit = (optFlags.contains(NO_LO_LIMIT) || optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) ? null : b;
        	l += R4.numBytes;
        }
        else loLimit = null;
        if (l < recLen)
        {
        	float b = cpu.getR4(is);
        	hiLimit = (optFlags.contains(NO_HI_LIMIT) || optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) ? null : b;
        	l += R4.numBytes;
        }
        else hiLimit = null;
        if (l < recLen)
        {
            startIn = cpu.getR4(is);
        	l += R4.numBytes;
        }
        else startIn = null;
        if (l < recLen)
        {
            incrIn  = cpu.getR4(is);
            l += R4.numBytes;
        }
        else incrIn = null;
        if (j > 0 && l < recLen)
        {
            rtnIndex = new IntList(Data_t.U2, cpu, j, is);
            l += U2.numBytes * rtnIndex.size();
        }
        else rtnIndex = null;
        if (l < recLen)
        {
            units = cpu.getCN(is);
            l += 1 + units.length();
        }
        else units = null;
        if (l < recLen)
        {
            unitsIn = cpu.getCN(is);
            l += 1 + unitsIn.length();
        }
        else unitsIn = null;
        if (l < recLen)
        {
            resFmt = cpu.getCN(is);
            l += 1 + resFmt.length();
        }
        else resFmt = null;
        if (l < recLen)
        {
            llmFmt = cpu.getCN(is);
            l += 1 + llmFmt.length();
        }
        else llmFmt = null;
        if (l < recLen)
        {
            hlmFmt = cpu.getCN(is);
            l += 1 + hlmFmt.length();
        }
        else hlmFmt = null;
        if (l < recLen)
        {
            loSpec = cpu.getR4(is);
            l += R4.numBytes;
        }
        else loSpec = null;
        if (l < recLen)
        {
            hiSpec = cpu.getR4(is);
            l += R4.numBytes;
        }
        else hiSpec = null;
        if (l != recLen) throw new RuntimeException("Record length error in MPR record: testNumber = " + testNumber + " l = " + l + " recLen = " + recLen);
    }
    
    /**
     * This constructor is used to make a MultipleResultParametricRecord with field values. 
     * @param cpu The cpu type.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     * @param paramFlags The PARM_FLG field.
     * @param rtnState   The RTN_STAT field.
     * @param results    The RTN_RSLT field.
     * @param testName   The TEST_TXT field.
     * @param alarmName  The ALARM_ID field.
     * @param optFlags   The OPT_FLAG field.
     * @param resScal    The RES_SCAL field.
     * @param llmScal    The LLM_SCAL field.
     * @param hlmScal    The HLM_SCAL field.
     * @param loLimit    The LO_LIMIT field.
     * @param hiLimit    The HI_LIMIT field.
     * @param startIn    The START_IN field.
     * @param incrIn     The INCR_IN field.
     * @param rtnIndex   The RTN_INDX field.
     * @param units      The UNITS field.
     * @param unitsIn    The UNITS_IN field.
     * @param resFmt     The C_RESFMT field.
     * @param llmFmt     The C_LLMFMT field.
     * @param hlmFmt     The C_HLMFMT field.
     * @param loSpec     The LO_SPEC field.
     * @param hiSpec     The HI_SPEC field.
     * @throws StdfException 
     * @throws IOException 
     */
    public MultipleResultParametricRecord(
    		final Cpu_t cpu,
    		final TestIdDatabase tdb,
            final long testNumber,
            final short headNumber,
            final short siteNumber,
            final byte testFlags,
            final byte paramFlags,
            final int[] rtnState,
            final float[] results,
    	    final String testName,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
    	    final Byte resScal, 
    	    final Byte llmScal,
    	    final Byte hlmScal, 
    	    final Float loLimit, 
    	    final Float hiLimit,
    	    final Float startIn,
    	    final Float incrIn,
    	    final int[] rtnIndex,
    	    final String units,
    	    final String unitsIn,
    	    final String resFmt,
    	    final String llmFmt,
    	    final String hlmFmt,
    	    final Float loSpec,
    	    final Float hiSpec)
    {
    	this(cpu, tdb,
    		 getRecLen(rtnState, results, testName, alarmName, optFlags, resScal, 
    				   llmScal, hlmScal, loLimit, hiLimit, startIn, incrIn, rtnIndex, 
    		           units, unitsIn, resFmt, llmFmt, hlmFmt, loSpec, hiSpec),
    		 new ByteInputStream(toBytes(cpu, testNumber, headNumber, siteNumber, testFlags, 
    		         paramFlags, rtnState, results, testName, alarmName, optFlags, 
    		         resScal, llmScal, hlmScal, loLimit, hiLimit, startIn, incrIn, rtnIndex, 
    		         units, unitsIn, resFmt, llmFmt, hlmFmt, loSpec, hiSpec)));
    }
    
    private static int getRecLen(
    		int[] rtnState,
    		float[] results,
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final Byte rScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final Byte lScal,
    	    final Byte hScal, 
    	    final Float lLimit, 
    	    final Float hLimit,
    	    final Float startIn,
    	    final Float incrIn,
    	    final int[] rtnIndex,
    	    final String uts,
    	    final String utsIn,
    	    final String rFmt,
    	    final String lFmt,
    	    final String hFmt,
    	    final Float lSpec,
    	    final Float hSpec)
    {
    	int l = 8;
    	if (rtnState != null) l += U2.numBytes + (rtnState.length+1) / 2; else return(l);
        if (results != null) l += U2.numBytes + U4.numBytes * results.length; else return(l);	
        if (tName != null) l += 1 + tName.length(); else return(l);
        if (aName != null) l += 1 + aName.length(); else return(l);
        if (oFlags != null) l++; else return(l);
        if (rScal != null) l++; else return(l);
        if (lScal != null) l++; else return(l);
        if (hScal != null) l++; else return(l);
        if (lLimit != null) l += R4.numBytes; else return(l);
        if (hLimit != null) l += R4.numBytes; else return(l);
        if (startIn != null) l += R4.numBytes; else return(l);
        if (incrIn != null) l += R4.numBytes; else return(l);
        if (rtnIndex != null) l += U2.numBytes * rtnIndex.length; else return(l);
        if (uts != null) l += 1 + uts.length(); else return(l);
        if (utsIn != null) l += 1 + utsIn.length(); else return(l);
        if (rFmt != null) l += 1 + rFmt.length(); else return(l);
        if (lFmt != null) l += 1 + lFmt.length(); else return(l);
        if (hFmt != null) l += 1 + hFmt.length(); else return(l);
        if (lSpec != null) l += R4.numBytes; else return(l);
        if (hSpec != null) l += R4.numBytes;
    	return(l);
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte tf = (byte) testFlags.stream().mapToInt(p -> p.bit).sum();
		byte pf = (byte) paramFlags.stream().mapToInt(p -> p.bit).sum();
    	byte[] b = toBytes(cpu, testNumber, headNumber, siteNumber, tf, 
    		               pf, rtnState.getArray(), results.getArray(), testName, alarmName, optFlags, 
    		               resScal, llmScal, hlmScal, loLimit, hiLimit, startIn, incrIn, rtnIndex.getArray(), 
    		               units, unitsIn, resFmt, llmFmt, hlmFmt, loSpec, hiSpec);
    	TByteArrayList l = getHeaderBytes(cpu, Record_t.MPR, b.length);
    	l.addAll(b);
		return(l.toArray());
	}

    private static byte[] toBytes(
    		final Cpu_t cpu,
            final long tNumber,
            final short hNumber,
            final short sNumber,
            final byte tFlags,
            final byte pFlags,
            final int[] rState,
            final float[] rslts,
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final Byte rScal,
    	    final Byte lScal,
    	    final Byte hScal, 
    	    final Float lLimit, 
    	    final Float hLimit,
    	    final Float sIn,
    	    final Float iIn,
    	    final int[] rIndex,
    	    final String uts,
    	    final String utsIn,
    	    final String rFmt,
    	    final String lFmt,
    	    final String hFmt,
    	    final Float lSpec,
    	    final Float hSpec)
    {	
        TByteArrayList list = new TByteArrayList();
        list.addAll(cpu.getU4Bytes(tNumber));
        list.addAll(cpu.getU1Bytes(hNumber));
        list.addAll(cpu.getU1Bytes(sNumber));
        list.add(tFlags);
        list.add(pFlags);
        if (rState != null) 
        {
          list.addAll(cpu.getU2Bytes(rState.length));
          if (rslts != null)
          {
        	list.addAll(cpu.getU2Bytes(rslts.length));
            if (rState != null)
            {
              final int len = (rState.length + 1) / 2;
              IntStream.range(0, len).forEach(p -> StdfRecord.addNibbles(cpu, rState, list, p, len));
              if (rslts != null)
              {
                for (int i=0; i<rslts.length; i++) list.addAll(cpu.getR4Bytes(rslts[i]));
                if (tName != null)
                {
                  list.addAll(cpu.getCNBytes(tName));
                  if (aName != null)
                  {
                	list.addAll(cpu.getCNBytes(aName));
        	        if (oFlags != null)
        	        {
        	          list.add((byte) oFlags.stream().mapToInt(b -> b.bit).sum());
                      if (rScal != null)
                      {
                    	list.addAll(cpu.getI1Bytes(rScal));
                        if (lScal != null)
                        {
                          list.addAll(cpu.getI1Bytes(lScal));
                          if (hScal != null)
                          {
                            list.addAll(cpu.getI1Bytes(hScal));
                            if (lLimit != null)
                            {
                              list.addAll(cpu.getR4Bytes(lLimit));
                              if (hLimit != null)
                              {
                                list.addAll(cpu.getR4Bytes(hLimit));
                                if (sIn != null)
                                {
                                  list.addAll(cpu.getR4Bytes(sIn));
                                  if (iIn != null)
                                  {
                                    list.addAll(cpu.getR4Bytes(iIn));
                                    if (rIndex != null)
                                    {
                                      Arrays.stream(rIndex).forEach(p -> list.addAll(cpu.getU2Bytes(p)));
                                      if (uts != null)
                                      {
                                        list.addAll(cpu.getCNBytes(uts));
                                        if (utsIn != null)
                                        {
                                          list.addAll(cpu.getCNBytes(utsIn));
                                          if (rFmt != null)
                                          {
                                            list.addAll(cpu.getCNBytes(rFmt));
                                            if (lFmt != null)
                                            {
                                              list.addAll(cpu.getCNBytes(lFmt));
                                              if (hFmt != null)
                                              {
                                                list.addAll(cpu.getCNBytes(hFmt));
                                                if (lSpec != null)
                                                {
                                                  list.addAll(cpu.getR4Bytes(lSpec));
                                                  if (hSpec != null)
                                                  {
                                                    list.addAll(cpu.getR4Bytes(hSpec));
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
        	        }
                  }
                }
              }
            }
          }
        }
        return(list.toArray());
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getAlarmName()
	 */
	@Override
	public String getAlarmName()
	{
		return alarmName;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getOptFlags()
	 */
	@Override
	public Set<OptFlag_t> getOptFlags()
	{
		return optFlags;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getResScal()
	 */
	@Override
	public Byte getResScal()
	{
		return resScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getLlmScal()
	 */
	@Override
	public Byte getLlmScal()
	{
		return llmScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getHlmScal()
	 */
	@Override
	public Byte getHlmScal()
	{
		return hlmScal;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getLoLimit()
	 */
	@Override
	public Float getLoLimit()
	{
		return loLimit;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getHiLimit()
	 */
	@Override
	public Float getHiLimit()
	{
		return hiLimit;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.ParametricRecord#getUnits()
	 */
	@Override
	public String getUnits()
	{
		return units;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((alarmName == null) ? 0 : alarmName.hashCode());
		result = prime * result + ((hiLimit == null) ? 0 : hiLimit.hashCode());
		result = prime * result + ((hiSpec == null) ? 0 : hiSpec.hashCode());
		result = prime * result + ((hlmFmt == null) ? 0 : hlmFmt.hashCode());
		result = prime * result + ((hlmScal == null) ? 0 : hlmScal.hashCode());
		result = prime * result + ((incrIn == null) ? 0 : incrIn.hashCode());
		result = prime * result + ((llmFmt == null) ? 0 : llmFmt.hashCode());
		result = prime * result + ((llmScal == null) ? 0 : llmScal.hashCode());
		result = prime * result + ((loLimit == null) ? 0 : loLimit.hashCode());
		result = prime * result + ((loSpec == null) ? 0 : loSpec.hashCode());
		result = prime * result + ((optFlags == null) ? 0 : optFlags.hashCode());
		result = prime * result + ((resFmt == null) ? 0 : resFmt.hashCode());
		result = prime * result + ((resScal == null) ? 0 : resScal.hashCode());
		result = prime * result + ((results == null) ? 0 : results.hashCode());
		result = prime * result + ((rtnIndex == null) ? 0 : rtnIndex.hashCode());
		result = prime * result + ((rtnState == null) ? 0 : rtnState.hashCode());
		result = prime * result + ((startIn == null) ? 0 : startIn.hashCode());
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((unitsIn == null) ? 0 : unitsIn.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof MultipleResultParametricRecord)) return false;
		MultipleResultParametricRecord other = (MultipleResultParametricRecord) obj;
		if (alarmName == null)
		{
			if (other.alarmName != null) return false;
		} 
		else if (!alarmName.equals(other.alarmName)) return false;
		if (hiLimit == null)
		{
			if (other.hiLimit != null) return false;
		} 
		else if (!hiLimit.equals(other.hiLimit)) return false;
		if (hiSpec == null)
		{
			if (other.hiSpec != null) return false;
		} 
		else if (!hiSpec.equals(other.hiSpec)) return false;
		if (hlmFmt == null)
		{
			if (other.hlmFmt != null) return false;
		} 
		else if (!hlmFmt.equals(other.hlmFmt)) return false;
		if (hlmScal == null)
		{
			if (other.hlmScal != null) return false;
		} 
		else if (!hlmScal.equals(other.hlmScal)) return false;
		if (incrIn == null)
		{
			if (other.incrIn != null) return false;
		} 
		else if (!incrIn.equals(other.incrIn)) return false;
		if (llmFmt == null)
		{
			if (other.llmFmt != null) return false;
		} 
		else if (!llmFmt.equals(other.llmFmt)) return false;
		if (llmScal == null)
		{
			if (other.llmScal != null) return false;
		} 
		else if (!llmScal.equals(other.llmScal)) return false;
		if (loLimit == null)
		{
			if (other.loLimit != null) return false;
		} 
		else if (!loLimit.equals(other.loLimit)) return false;
		if (loSpec == null)
		{
			if (other.loSpec != null) return false;
		} 
		else if (!loSpec.equals(other.loSpec)) return false;
		if (optFlags == null)
		{
			if (other.optFlags != null) return false;
		} 
		else if (!optFlags.equals(other.optFlags)) return false;
		if (resFmt == null)
		{
			if (other.resFmt != null) return false;
		} 
		else if (!resFmt.equals(other.resFmt)) return false;
		if (resScal == null)
		{
			if (other.resScal != null) return false;
		} 
		else if (!resScal.equals(other.resScal)) return false;
		if (!results.equals(other.results)) return false;
		if (!rtnIndex.equals(other.rtnIndex)) return false;
		if (!rtnState.equals(other.rtnState)) return false;
		if (startIn == null)
		{
			if (other.startIn != null) return false;
		} 
		else if (!startIn.equals(other.startIn)) return false;
		if (testName == null)
		{
			if (other.testName != null) return false;
		} 
		else if (!testName.equals(other.testName)) return false;
		if (units == null)
		{
			if (other.units != null) return false;
		} 
		else if (!units.equals(other.units)) return false;
		if (unitsIn == null)
		{
			if (other.unitsIn != null) return false;
		} 
		else if (!unitsIn.equals(other.unitsIn)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	@Override
	public long getTestNumber()
	{
		return(testNumber);
	}

	@Override
	public String getTestName()
	{
		return(testName);
	}

	@Override
	public Set<TestFlag_t> getTestFlags()
	{
		return(testFlags);
	}

	@Override
	public TestID getTestID()
	{
		return(id);
	}




}
