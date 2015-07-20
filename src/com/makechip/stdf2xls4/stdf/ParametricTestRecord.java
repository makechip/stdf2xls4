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
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.*;
import static com.makechip.stdf2xls4.stdf.enums.OptFlag_t.*;
/**
 *  This class holds the fields of a Parametric Test Record.
 *  @author eric
 */
public class ParametricTestRecord extends ParametricRecord
{
	/**
	 *  This is the RESULT field.
	 */
    public final Float result; 
    /**
     * This is the TEST_TXT field.
     */
    public final String testName;
    /**
     *  This is the ALARM_ID field.
     */
    public final String alarmName;
    /**
     *  This is the OPT_FLAG field.
     */
    public final Set<OptFlag_t> optFlags; 
    /**
     *  This is the RES_SCAL field.
     */
    public final Byte resScal;
    /**
     *  This is the LLM_SCAL field.
     */
    private final Byte llmScal;
    /**
     *  This is the HLM_SCAL field.
     */
    private final Byte hlmScal; 
    /**
     *  This is the LO_LIMIT field.
     */
    private final Float loLimit; 
    /**
     *  This is the HI_LIMIT field.
     */
    private final Float hiLimit;
    /**
     *  This is the UNITS field.
     */
    public final String units;
    /**
     *  This is the C_RESFMT field.
     */
    public final String resFmt;
    /**
     *  This is the C_LLMFMT field.
     */
    public final String llmFmt;
    /**
     *  This is the C_HLMFMT field.
     */
    public final String hlmFmt;
    /**
     *  This is the LO_SPEC field.
     */
    public final Float loSpec;
    /**
     *  This is the HI_SPEC field.
     */
    public final Float hiSpec;
    /**
     * This is not a standard STDF field.  It is used to uniquely
     * identify this test.
     */
    public final TestID id;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This parameter is used for tracking the Test ID.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public ParametricTestRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
    	super(cpu, recLen, is);
    	int l = 8;
    	if (l < recLen)
    	{
            result = cpu.getR4(is);
            l += R4.numBytes;
    	}
    	else result = null;
    	if (l < recLen)
    	{
            testName = cpu.getCN(is); 
            l += 1 + testName.length();
    	}
    	else testName = null;
    	id = TestID.createTestID(tdb, testNumber, testName);
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
        	float f = cpu.getR4(is);
		    loLimit = (optFlags.contains(NO_LO_LIMIT) || optFlags.contains(LO_LIMIT_LLM_SCAL_INVALID)) ? null : f;
        	l += R4.numBytes;
        }
        else loLimit = null;
        if (l < recLen)
        {
        	float f = cpu.getR4(is);
		    hiLimit = (optFlags.contains(NO_HI_LIMIT) || optFlags.contains(HI_LIMIT_HLM_SCAL_INVALID)) ? null : f;
        	l += R4.numBytes;
        }
        else hiLimit = null;
        if (l < recLen)
        {
        	units = cpu.getCN(is);
        	l += 1 + units.length();
        }
        else units = null;
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
        if (l != recLen) throw new RuntimeException("Record length error in PTR record: testNumber = " + testNumber);
    }
    
    /**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param tdb The TestIdDatabase is needed to get the TestID.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
     * @param testNumber The TEST_NUM field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param testFlags  The TEST_FLG field.
     * @param paramFlags The PARM_FLG field.
     * @param result     The RESULT field.
     * @param testName   The TEST_TXT field.
     * @param alarmName  The ALARM_ID field.
     * @param optFlags   The OPT_FLAG field.
     * @param resScal    The RES_SCAL field.
     * @param llmScal    The LLM_SCAL field.
     * @param hlmScal    The HLM_SCAL field.
     * @param loLimit    The LO_LIMIT field.
     * @param hiLimit    The HI_LIMIT field.
     * @param units      The UNITS field.
     * @param resFmt     The C_RESFMT field.
     * @param llmFmt     The C_LLMFMT field.
     * @param hlmFmt     The C_HLMFMT field.
     * @param loSpec     The LO_SPEC field.
     * @param hiSpec     The HI_SPEC field.
     * @throws StdfException 
     * @throws IOException 
     */
    public ParametricTestRecord(
    		final Cpu_t cpu,
    		final TestIdDatabase tdb,
            final long testNumber,
            final short headNumber,
            final short siteNumber,
            final byte testFlags,
            final byte paramFlags,
    	    final Float result, 
    	    final String testName,
    	    final String alarmName,
    	    final EnumSet<OptFlag_t> optFlags, 
    	    final Byte resScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final Byte llmScal,
    	    final Byte hlmScal, 
    	    final Float loLimit, 
    	    final Float hiLimit,
    	    final String units,
    	    final String resFmt,
    	    final String llmFmt,
    	    final String hlmFmt,
    	    final Float loSpec,
    	    final Float hiSpec)
    {
    	this(cpu, tdb,
    		 getRecLen(result, testName, alarmName, optFlags, resScal, llmScal, hlmScal, 
    		 loLimit, hiLimit, units, resFmt, llmFmt, hlmFmt, loSpec, hiSpec),
    		 new ByteInputStream(toBytes(cpu, testNumber, headNumber, 
    		 siteNumber, testFlags, paramFlags, result, testName, alarmName, optFlags, resScal, 
    		 llmScal, hlmScal, loLimit, hiLimit, units, resFmt, llmFmt, hlmFmt, loSpec, hiSpec)));
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
        byte tf = (byte) testFlags.stream().mapToInt(b -> b.bit).sum();
        byte pf = (byte) paramFlags.stream().mapToInt(b -> b.bit).sum();
    	byte[] b = toBytes(cpu, testNumber, headNumber, siteNumber, tf, pf,
    		               result, testName, alarmName, optFlags, resScal, llmScal, hlmScal, 
    		               loLimit, hiLimit, units, resFmt, llmFmt, hlmFmt, loSpec, hiSpec);
    	TByteArrayList l = getHeaderBytes(cpu, Record_t.PTR, b.length);
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
    	    final Float rslt, 
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final Byte rScal, 
    	    final Byte lScal,
    	    final Byte hScal, 
    	    final Float lLimit, 
    	    final Float hLimit,
    	    final String uts,
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
        if (rslt != null) 
        {
          list.addAll(cpu.getR4Bytes(rslt));
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
                          if (uts != null)
                          {
                        	list.addAll(cpu.getCNBytes(uts));
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
        return(list.toArray());
    }
    	    
    private static int getRecLen(
    	    final Float rslt, 
    	    final String tName,
    	    final String aName,
    	    final Set<OptFlag_t> oFlags, 
    	    final Byte rScal, // if RES_SCAL_INVALID set, then use default res_scal
    	    final Byte lScal,
    	    final Byte hScal, 
    	    final Float lLimit, 
    	    final Float hLimit,
    	    final String uts,
    	    final String rFmt,
    	    final String lFmt,
    	    final String hFmt,
    	    final Float lSpec,
    	    final Float hSpec)
    {
    	int l = 8;
        if (rslt != null) l += U4.numBytes; else return(l);	
        if (tName != null) l += 1 + tName.length(); else return(l);
        if (aName != null) l += 1 + aName.length(); else return(l);
        if (oFlags != null) l++; else return(l);
        if (rScal != null) l++; else return(l);
        if (lScal != null) l++; else return(l);
        if (hScal != null) l++; else return(l);
        if (lLimit != null) l += R4.numBytes; else return(l);
        if (hLimit != null) l += R4.numBytes; else return(l);
        if (uts != null) l += 1 + uts.length(); else return(l);
        if (rFmt != null) l += 1 + rFmt.length(); else return(l);
        if (lFmt != null) l += 1 + lFmt.length(); else return(l);
        if (hFmt != null) l += 1 + hFmt.length(); else return(l);
        if (lSpec != null) l += R4.numBytes; else return(l);
        if (hSpec != null) l += R4.numBytes;
    	return(l);
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
		result = prime * result + ((llmFmt == null) ? 0 : llmFmt.hashCode());
		result = prime * result + ((llmScal == null) ? 0 : llmScal.hashCode());
		result = prime * result + ((loLimit == null) ? 0 : loLimit.hashCode());
		result = prime * result + ((loSpec == null) ? 0 : loSpec.hashCode());
		result = prime * result + ((optFlags == null) ? 0 : optFlags.hashCode());
		result = prime * result + ((resFmt == null) ? 0 : resFmt.hashCode());
		result = prime * result + ((resScal == null) ? 0 : resScal.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
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
		if (!(obj instanceof ParametricTestRecord)) return false;
		ParametricTestRecord other = (ParametricTestRecord) obj;
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
		if (result == null)
		{
			if (other.result != null) return false;
		} 
		else if (!result.equals(other.result)) return false;
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