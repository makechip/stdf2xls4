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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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
    
   public WaferInformationRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        headNumber = cpu.getU1(is);
        siteGroupNumber = cpu.getU1(is);
        startDate = cpu.getU4(is);
        int l = 6;
        if (l < recLen)
        {
            waferID = cpu.getCN(is);
            l += 1 + waferID.length();
        }
        else waferID = null;
        if (l != recLen) throw new StdfException("Record length error in WaferInformationRecord.");
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteGroupNumber, startDate, waferID);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.WIR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	private static int getRecLen(String waferID)
	{
		int l = 6;
		if (waferID != null) l += 1 + waferID.length();
		return(l);
	}
	
	private static byte[] toBytes(
		Cpu_t cpu,
		short headNumber,
		short siteGroupNumber,
		long startDate,
		String waferID)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteGroupNumber));
		l.addAll(cpu.getU4Bytes(startDate));
		l.addAll(cpu.getCNBytes(waferID));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param cpu  The CPU type.
	 * @param headNumber      The HEAD_NUM field.
	 * @param siteGroupNumber The SITE_GRP field.
	 * @param startDate       The START_T field.
	 * @param waferID         The WAFER_ID field.
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public WaferInformationRecord(
		Cpu_t cpu,
		short headNumber,
		short siteGroupNumber,
		long startDate,
		String waferID) throws IOException, StdfException
	{
		this(cpu, getRecLen(waferID),
			 new DataInputStream(new ByteArrayInputStream(toBytes(cpu, headNumber, siteGroupNumber, startDate, waferID))));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + headNumber;
		result = prime * result + siteGroupNumber;
		result = prime * result + (int) (startDate ^ (startDate >>> 32));
		result = prime * result + ((waferID == null) ? 0 : waferID.hashCode());
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
		if (!(obj instanceof WaferInformationRecord)) return false;
		WaferInformationRecord other = (WaferInformationRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		if (startDate != other.startDate) return false;
		if (waferID == null)
		{
			if (other.waferID != null) return false;
		} 
		else if (!waferID.equals(other.waferID)) return false;
		return true;
	}

    
}
