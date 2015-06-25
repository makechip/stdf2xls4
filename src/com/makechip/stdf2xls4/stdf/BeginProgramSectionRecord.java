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
    public BeginProgramSectionRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.BPS, dvd.getCpuType(), data);
        seqName = getCn();
    }
    
    /**
     * This constructor is used to generate a BeginProgramSectionRecord.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param seqName The SEQ_NAME field. (Must not be null).
     */
    public BeginProgramSectionRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, String seqName)
    {
    	this(tdb, dvd, getCnBytes(seqName));
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
	 * @see com.makechip.strf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = getCnBytes(seqName);	
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
