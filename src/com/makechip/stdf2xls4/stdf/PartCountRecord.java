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

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields of a Part Count Record.
 *  @author eric
 */
public class PartCountRecord extends StdfRecord
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
	 *   This is the PART_CNT field.
	 */
    public final long partsTested;
	/**
	 *  This is the RTST_CNT field.
	 */
    public final Long partsReTested;
	/**
	 *  This is the ABRT_CNT field.
	 */
    public final Long aborts;
	/**
	 *  This is the GOOD_CNT field.
	 */
    public final Long good;
	/**
	 *  This is the FUNC_CNT field.
	 */
    public final Long functional;
    
    /**
     * 
     * @param cpu
     * @param recLen
     * @param is
     * @throws IOException
     * @throws StdfException
     */
    public PartCountRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.PCR);
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
        partsTested = cpu.getU4(is);
        int l = 6;
        if (l < recLen)
        {
        	partsReTested = cpu.getU4(is);
        	l += Data_t.U4.numBytes;
        }
        else partsReTested = null;
        if (l < recLen)
        {
            aborts = cpu.getU4(is);
        	l += Data_t.U4.numBytes;
        }
        else aborts = null;
        if (l < recLen)
        {
            good = cpu.getU4(is);
        	l += Data_t.U4.numBytes;
        }
        else good = null;
        if (l < recLen)
        {
            functional = cpu.getU4(is);
        	l += Data_t.U4.numBytes;
        }
        else functional = null;
        if (l != recLen) throw new RuntimeException("Record length error in PartCountRecord");
    }
    
    /**
     * 
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param cpu The cpu type.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param partsTested The PART_CNT field.
     * @param partsReTested The RTST_CNT field.
     * @param aborts The ABRT_CNT field.
     * @param good The GOOD_CNT field.
     * @param functional The FUNC_CNT field.
     * @throws StdfException 
     * @throws IOException 
     */
    public PartCountRecord(
    		Cpu_t cpu,
    		short headNumber,
    		short siteNumber,
    		long partsTested,
    		Long partsReTested,
    		Long aborts,
    		Long good,
    		Long functional,
    		CliOptions options)
    {
    	this(cpu, null,
    		getRecLen(partsReTested, aborts, good, functional),
    		new ByteInputStream(toBytes(cpu, headNumber, siteNumber, partsTested, partsReTested, aborts, good, functional)), options);
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
   		byte[] b = toBytes(cpu, headNumber, siteNumber, partsTested, 
    				       partsReTested, aborts, good, functional);
   		TByteArrayList l = getHeaderBytes(cpu,Record_t.PCR, b.length);
   		l.addAll(b);
		return(l.toArray());
	}

	private static byte[] toBytes(
    	Cpu_t cpu,
    	short headNumber,
    	short siteNumber,
    	long partsTested,
    	Long partsReTested,
    	Long aborts,
    	Long good,
    	Long functional)
	{
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(cpu.getU1Bytes(headNumber));
	    l.addAll(cpu.getU1Bytes(siteNumber));
	    l.addAll(cpu.getU4Bytes(partsTested));
	    if (partsReTested != null)
	    {
	        l.addAll(cpu.getU4Bytes(partsReTested));
	        if (aborts != null)
	        {
	            l.addAll(cpu.getU4Bytes(aborts));
	            if (good != null)
	            {
	                l.addAll(cpu.getU4Bytes(good));
	                if (functional != null)
	                {
	                    l.addAll(cpu.getU4Bytes(functional));
	                }
	            }
	        }
	    }
	    return(l.toArray());
	}

	private static int getRecLen(Long partsReTested, Long aborts, Long good, Long functional)
	{
		int l = 6;
		if (partsReTested != null)
		{
			l += Data_t.U4.numBytes;
			if (aborts != null)
			{
				l += Data_t.U4.numBytes;
				if (good != null)
				{
					l += Data_t.U4.numBytes;
					if (functional != null)
					{
						l += Data_t.U4.numBytes;
					}
				}
			}
		}
		return(l);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aborts == null) ? 0 : aborts.hashCode());
		result = prime * result + ((functional == null) ? 0 : functional.hashCode());
		result = prime * result + ((good == null) ? 0 : good.hashCode());
		result = prime * result + headNumber;
		result = prime * result + ((partsReTested == null) ? 0 : partsReTested.hashCode());
		result = prime * result + (int) (partsTested ^ (partsTested >>> 32));
		result = prime * result + siteNumber;
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
		if (!(obj instanceof PartCountRecord)) return false;
		PartCountRecord other = (PartCountRecord) obj;
		if (aborts == null)
		{
			if (other.aborts != null) return false;
		} 
		else if (!aborts.equals(other.aborts)) return false;
		if (functional == null)
		{
			if (other.functional != null) return false;
		} 
		else if (!functional.equals(other.functional)) return false;
		if (good == null)
		{
			if (other.good != null) return false;
		} 
		else if (!good.equals(other.good)) return false;
		if (headNumber != other.headNumber) return false;
		if (partsReTested == null)
		{
			if (other.partsReTested != null) return false;
		} 
		else if (!partsReTested.equals(other.partsReTested)) return false;
		if (partsTested != other.partsTested) return false;
		if (siteNumber != other.siteNumber) return false;
		return true;
	}


}
