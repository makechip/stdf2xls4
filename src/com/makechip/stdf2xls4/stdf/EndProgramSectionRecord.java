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

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
*** @author eric
*** @version $Id: EndProgramSelectionRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class EndProgramSectionRecord extends StdfRecord
{

    /**
     * Constructor for initializing this record with binary stream data.
     * @param tdb The TestIdDatabase  is not used by this record, but is
     * required so STDF records have consistent constructor signatures.
     * @param data The binary stream data for this record.  The array should
     * not contain the first four bytes of the record. For this specific
     * record, the array should have a length of zero because the EndProgramSectionRecord
     * has no data fields.
     */
    public EndProgramSectionRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.EPS);
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @throws StdfException 
     * @throws IOException 
     */
    public EndProgramSectionRecord(Cpu_t cpu, CliOptions options)
    {
    	this(cpu, null, 0, null, options);
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		return(getHeaderBytes(cpu, Record_t.EPS, 0).toArray());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		if (o == null) return false;
		if (o instanceof EndProgramSectionRecord) return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return(19733321);
	}
    
}
