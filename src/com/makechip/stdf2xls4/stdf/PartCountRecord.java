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

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
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
    public final long partsReTested;
	/**
	 *  This is the ABRT_CNT field.
	 */
    public final long aborts;
	/**
	 *  This is the GOOD_CNT field.
	 */
    public final long good;
	/**
	 *  This is the FUNC_CNT field.
	 */
    public final long functional;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This parameter is not used.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public PartCountRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PCR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        partsTested = getU4(-1);
        partsReTested = getU4(4294967295L);
        aborts = getU4(4294967295L);
        good = getU4(4294967295L);
        functional = getU4(4294967295L);
    }
    
    /**
     * 
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param tdb The TestIdDatabase is needed to get the TestID.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @param partsTested The PART_CNT field.
     * @param partsReTested The RTST_CNT field.
     * @param aborts The ABRT_CNT field.
     * @param good The GOOD_CNT field.
     * @param functional The FUNC_CNT field.
     */
    public PartCountRecord(
    		TestIdDatabase tdb, DefaultValueDatabase dvd,
    		short headNumber,
    		short siteNumber,
    		long partsTested,
    		long partsReTested,
    		long aborts,
    		long good,
    		long functional)
    {
    	this(tdb,  dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, partsTested, partsReTested, aborts, good, functional));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
 	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteNumber, partsTested, partsReTested, aborts, good, functional);	
	}
	
	private static byte[] toBytes(
    	Cpu_t cpuType,
    	short headNumber,
    	short siteNumber,
    	long partsTested,
    	long partsReTested,
    	long aborts,
    	long good,
    	long functional)
	{
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(getU1Bytes(headNumber));
	    l.addAll(getU1Bytes(siteNumber));
	    l.addAll(cpuType.getU4Bytes(partsTested));
	    l.addAll(cpuType.getU4Bytes(partsReTested));
	    l.addAll(cpuType.getU4Bytes(aborts));
	    l.addAll(cpuType.getU4Bytes(good));
	    l.addAll(cpuType.getU4Bytes(functional));
	    return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PartCountRecord [headNumber="); builder.append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", partsTested=").append(partsTested);
		builder.append(", partsReTested=").append(partsReTested);
		builder.append(", aborts=").append(aborts);
		builder.append(", good=").append(good);
		builder.append(", functional=").append(functional);
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
		int result = super.hashCode();
		result = prime * result + (int) (aborts ^ (aborts >>> 32));
		result = prime * result + (int) (functional ^ (functional >>> 32));
		result = prime * result + (int) (good ^ (good >>> 32));
		result = prime * result + headNumber;
		result = prime * result + (int) (partsReTested ^ (partsReTested >>> 32));
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
		if (!(obj instanceof PartCountRecord)) return false;
		PartCountRecord other = (PartCountRecord) obj;
		if (aborts != other.aborts) return false;
		if (functional != other.functional) return false;
		if (good != other.good) return false;
		if (headNumber != other.headNumber) return false;
		if (partsReTested != other.partsReTested) return false;
		if (partsTested != other.partsTested) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!super.equals(obj)) return false;
		return true;
	}


}
