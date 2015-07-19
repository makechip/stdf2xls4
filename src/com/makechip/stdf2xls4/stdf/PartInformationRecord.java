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
 *  This class holds the fields of a Part Information Record.
 *  @author eric
 */
public class PartInformationRecord extends StdfRecord
{
	/**
	 *  This is the HEAD_NUM field.
	 */
    public final short headNumber;
    /**
     *  This is the SITE_NUM field.
     */
    public final short siteNumber;
    
    public PartInformationRecord(Cpu_t cpu, int recLen, ByteInputStream is)
    {
        super();
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
    }
    
    public PartInformationRecord(Cpu_t cpu, short headNumber, short siteNumber)
    {
        this(cpu, 2, new ByteInputStream(toBytes(cpu, headNumber, siteNumber)));
    }
    
	private static byte[] toBytes(Cpu_t cpu, short headNumber, short siteNumber)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteNumber));
		return(l.toArray());
	}

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		TByteArrayList l = getHeaderBytes(cpu, Record_t.PIR, 2);
	    l.addAll(toBytes(cpu, headNumber, siteNumber));
	    return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 37;
		result = prime * result + headNumber;
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
		if (!(obj instanceof PartInformationRecord)) return false;
		PartInformationRecord other = (PartInformationRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (siteNumber != other.siteNumber) return false;
		return true;
	}


}
