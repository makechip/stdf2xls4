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
 *  This class holds the fields for a Wafer Information Record.
 *  @author eric
 */
public class WaferInformationRecord extends StdfRecord
{
    /**
     *  The HEAD_NUM field.
     */
    public final short headNumber;
    /**
     *  The SITE_GRP field.
     */
    public final short siteGroupNumber;
    /**
     *  The START_T field.
     */
    public final long startDate;
    /**
     *  The WAFER_ID field.
     */
    public final String waferID;
    
   /**
    *  Constructor used by the STDF reader to load binary data into this class.
    *  @param tdb The TestIdDatabase.  This parameter is not used.
    *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
    *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
    *         REC_SUB values are not included in this array.
    */
   public WaferInformationRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.WIR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) 255);
        startDate = getU4(-1);
        waferID = getCn();
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteGroupNumber, startDate, waferID);	
	}
	
	private static byte[] toBytes(
		Cpu_t cpuType,
		short headNumber,
		short siteGroupNumber,
		long startDate,
		String waferID)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(cpuType.getU4Bytes(startDate));
		l.addAll(getCnBytes(waferID));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param tdb The TestIdDatabase is not used in this class.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
	 * @param headNumber      The HEAD_NUM field.
	 * @param siteGroupNumber The SITE_GRP field.
	 * @param startDate       The START_T field.
	 * @param waferID         The WAFER_ID field.
	 */
	public WaferInformationRecord(
		TestIdDatabase tdb,
		DefaultValueDatabase dvd,
		short headNumber,
		short siteGroupNumber,
		long startDate,
		String waferID)
	{
		this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteGroupNumber, startDate, waferID));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WaferInformationRecord [headNumber=").append(headNumber);
		builder.append(", siteGroupNumber=").append(siteGroupNumber);
		builder.append(", startDate=").append(startDate);
		builder.append(", waferID=").append(waferID);
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
		result = prime * result + headNumber;
		result = prime * result + siteGroupNumber;
		result = prime * result + (int) (startDate ^ (startDate >>> 32));
		result = prime * result + waferID.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof WaferInformationRecord)) return false;
		WaferInformationRecord other = (WaferInformationRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		if (startDate != other.startDate) return false;
		if (!waferID.equals(other.waferID)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

    
}
