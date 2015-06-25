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
 *  This class holds data from a Datalog Text Record.  Datalog Text
 *  Records are generated any time the tester prints to the datalogger.
 *  Stdf2xls4 recognizes certain text record formats and interprets
 *  them to have special meanings that are then used in the spreadsheet
 *  generation.  These formats are:<br>
 *  1. Supplying an alphanumeric serial number (Some testers only support numeric serial numbers)<br>
 *     TEXT_DATA : S/N : serial_number<br>
 *  2. Generic Data per device (A "phony" test result)<br>
 *     TEXT_DATA : test_name : value [ : test_number [ : site_number [ : head_number ]]]<br>
 *     (Note: the square brackets indicate optional fields, and they do not actually appear in the text)<br>
 *  3. Fields that will appear in the spreadsheet headers<br>
 *     >>> header_name : header_value<br>
 *  4. Legacy fields:   <br>
 *     CUSTOMER: value<br>
 *     DEVICE NUMBER: value<br>
 *     SOW: value<br>
 *     CUSTOMER PO#: value<br>
 *     TESTER: value<br>
 *     TEST PROGRAM: value<br>
 *     LOADBOARD: value<br>
 *     CONTROL SERIAL #s: value<br>
 *     JOB #: value<br>
 *     LOT #: value<br>
 *     STEP #: value<br>
 *     TEMPERATURE: value<br>
 *    <br>
 *  Note: Upper case identifiers must appear in the text verbatim; lower case identifiers
 *  represent values that are supplied by the user.
 *
 *  @author eric
 */
public class DatalogTextRecord extends StdfRecord
{
    public final String text;
    
    /**
     * Constructor for initializing this record with binary stream data.
     * @param tdb The TestIdDatabase  is not used by this record, but is
     * required so STDF records have consistent constructor signatures.
     * @param dvd The DefaultValueDatabase is used to get the CPU type.
     * @param data The binary stream data for this record.  The array should
     * not contain the first four bytes of the record.
     */
    public DatalogTextRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.DTR, dvd.getCpuType(), data);
        text = getCn();
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param text This field holds the TEXT_DAT field. It must not be null. The
     * maximum length of this String is 255 characters.
     */
    public DatalogTextRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, String text)
    {
    	this(tdb, dvd, getCnBytes(text));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#isTestRecord()
	 */
    @Override
    public boolean isTestRecord() 
    { 
    	String s = text.trim();
    	return(s.startsWith("TEXT_DATA") && text.contains(":") && !text.contains(SERIAL_MARKER)); 
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
		bytes = getCnBytes(text);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("DatalogTextRecord [text=");
		builder.append(text);
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
		result = prime * result + text.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		DatalogTextRecord other = (DatalogTextRecord) obj;
		if (!text.equals(other.text)) return false;
		if (!(obj instanceof DatalogTextRecord)) return false;
		return true;
	}

}
