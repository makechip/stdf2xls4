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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds data for a Begin Program Section Record.
 *  @author eric
 */
public class BeginProgramSectionRecord extends StdfRecord
{
	/**
	 * This field holds the value for the SEQ_NAME field.
	 */
    public final String seqName;
    
    /**
     * Constructor to create a BPS record from the binary data stream.
     * @param tdb This parameter is not used; it is provided in order to
     * keep a consistent constructor signature for all StdfRecords.
     * @param dvd The DefaultValueDatabase is used to get the CPU type.
     * @param data Binary stream data.  This array should not contain
     * the first four bytes of the record.
     */
    public BeginProgramSectionRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        if (recLen > 0) seqName = cpu.getCN(is);
        else seqName = null;
    }
    
    /**
     * This constructor is used to generate a BeginProgramSectionRecord.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param seqName The SEQ_NAME field. (Must not be null).
     * @throws StdfException 
     * @throws IOException 
     */
    public BeginProgramSectionRecord(Cpu_t cpu, String seqName) throws IOException, StdfException
    {
    	this(cpu, 0, new DataInputStream(new ByteArrayInputStream(cpu.getCNBytes(seqName))));
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = cpu.getCNBytes(seqName);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.BPS, b.length);
		l.addAll(b);
		return(l.toArray());
	}

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BeginProgramSectionRecord [seqName=");
		builder.append(seqName);
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
		result = prime * result + seqName.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof BeginProgramSectionRecord)) return false;
		BeginProgramSectionRecord other = (BeginProgramSectionRecord) obj;
		if (!seqName.equals(other.seqName)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

}
