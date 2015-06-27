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
*** @author eric
*** @version $Id: HardwareBinRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class HardwareBinRecord extends StdfRecord
{
	/**
	 *  This is the HEAD_NUM field of the HardwareBinRecord.
	 */
    public final short headNumber;
    /**
     *  This is the SITE_NUM field of the HardwareBinRecord.
     */
    public final short siteNumber;
    /**
     *  This is the HBIN_NUM field of the HardwareBinRecord.
     */
    public final int hwBin;
    /**
     *  This is the HBIN_CNT field of the HardwareBinRecord.
     */
    public final long binCnt;
    /**
     * This is the HBIN_PF field of the HardwareBinRecord.
     */
    public final char pf;
    /**
     * This is the HBIN_NAM field of the HardwareBinRecord.
     */
    public final String binName;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the HardwareBinRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public HardwareBinRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.HBR, dvd.getCpuType(), data);
        headNumber = getU1((short) 0);
        siteNumber = getU1((short) 0);
        hwBin = getU2(-1);
        binCnt = getU4(0);
        pf = getFixedLengthString(1).charAt(0);
        binName = getCn();
    }
    
    /**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
     * @param dvd The DefaultValueDatabase is used to access the CPU type.
     * @param headNumber  The HEAD_NUM field.
     * @param siteNumber  The SITE_NUM field.
     * @param hwBin       The HBIN_NUM field.
     * @param binCnt      The HBIN_CNT field.
     * @param pf          The HBIN_PF field.
     * @param binName     The HBIN_NAM field.
     */
    public HardwareBinRecord(
    	TestIdDatabase tdb,
    	DefaultValueDatabase dvd,
    	short headNumber, 
    	short siteNumber, 
    	int hwBin, 
    	long binCnt, 
    	char pf, 
    	String binName)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, hwBin, binCnt, pf, binName));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
		bytes = toBytes(cpuType, headNumber, siteNumber, hwBin, binCnt, pf, binName);
	}
	
	private static byte[] toBytes(Cpu_t cpuType, short headNumber, short siteNumber, int hwBin, long binCnt, char pf, String binName)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(cpuType.getU2Bytes(hwBin));
		l.addAll(cpuType.getU4Bytes(binCnt));
		l.addAll(getFixedLengthStringBytes("" + pf));
		l.addAll(getCnBytes(binName));
		return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("HardwareBinRecord [headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", hwBin=").append(hwBin);
		builder.append(", binCnt=").append(binCnt);
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
		result = prime * result + (int) (binCnt ^ (binCnt >>> 32));
		result = prime * result + binName.hashCode();
		result = prime * result + headNumber;
		result = prime * result + hwBin;
		result = prime * result + pf;
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
		if (!(obj instanceof HardwareBinRecord)) return false;
		HardwareBinRecord other = (HardwareBinRecord) obj;
		if (binCnt != other.binCnt) return false;
		if (!binName.equals(other.binName)) return false;
		if (headNumber != other.headNumber) return false;
		if (hwBin != other.hwBin) return false;
		if (pf != other.pf) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

    
}
