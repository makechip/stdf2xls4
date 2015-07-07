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
 *  This class holds the fields for a Software Bin Record.
 *  @author eric
 */
public class SoftwareBinRecord extends StdfRecord
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
     *  This is the SBIN_NUM field.
     */
    public final int swBinNumber;
    /**
     *  This is the SBIN_CNT field.
     */
    public final int count;
    /**
     *  This is the SBIN_PF field.
     */
    public final String pf;
    /**
     *  This is the SBIN_NAM field.
     */
    public final String binName;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public SoftwareBinRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.SBR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        swBinNumber = getU2(-1);
        count = getU2(0);
        String s = getFixedLengthString(1);
        pf = (s.equals(MISSING_STRING)) ? " " : s;
        binName = getCn();
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteNumber, swBinNumber, count, pf.charAt(0), binName);	
	}
	
	private static byte[] toBytes(
		Cpu_t cpuType, 
		short headNumber, 
		short siteNumber, 
		int swBinNumber, 
		int count, 
		char pf, 
		String binName)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(cpuType.getU2Bytes(swBinNumber));
		l.addAll(cpuType.getU2Bytes(count));
		l.addAll(getFixedLengthStringBytes("" + pf));
		l.addAll(getCnBytes(binName));
		return(l.toArray());
	}
	
	/**
	 * 
     * This constructor is used to make a SoftwareBinRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
	 * @param headNumber The HEAD_NUM field.
	 * @param siteNumber The SITE_NUM field.
	 * @param swBinNumber The SBIN_NUM field.
	 * @param count    The SBIN_CNT field.
	 * @param pf       The SBIN_PF field.
	 * @param binName  The SBIN_NAM field.
	 */
	public SoftwareBinRecord(
			TestIdDatabase tdb,
			DefaultValueDatabase dvd,
			short headNumber, 
			short siteNumber, 
			int swBinNumber, 
			int count, 
			char pf, 
			String binName)
	{
		this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, swBinNumber, count, pf, binName));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SoftwareBinRecord [headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", swBinNumber=").append(swBinNumber);
		builder.append(", count=").append(count);
		builder.append(", pf=").append(pf);
		builder.append(", binName=").append(binName);
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
		result = prime * result + binName.hashCode();
		result = prime * result + count;
		result = prime * result + headNumber;
		result = prime * result + pf.hashCode();
		result = prime * result + siteNumber;
		result = prime * result + swBinNumber;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof SoftwareBinRecord)) return false;
		SoftwareBinRecord other = (SoftwareBinRecord) obj;
		if (!binName.equals(other.binName)) return false;
		if (count != other.count) return false;
		if (headNumber != other.headNumber) return false;
		if (!pf.equals(other.pf)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (swBinNumber != other.swBinNumber) return false;
		if (!super.equals(obj)) return false;
		return true;
	}
    
    
}
