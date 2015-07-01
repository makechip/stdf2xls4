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

import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds data from a Datalog Test Record.  A DatalogTestRecord
 *  is not a standard StdfRecord. It's a DatalogTextRecord that contains
 *  special text markers that identify it as a record that should be passed
 *  on to the SpreadsheetWriter as a test result.
 *  The format of a DatalogTestRecord is:<br>
 *     TEXT_DATA : test_name : value [(units)] [ : test_number [ : site_number [ : head_number ]]]<br>
 *     (Note 1: the square brackets indicate optional fields, and they do not actually appear in the text.)<br>
 *     (Note 2: The value may be an int, a float, or a String; units are optional, and must
 *      be enclosed in parenthesis.)<br>
 *     (Note 3: Upper case identifiers must appear in the text verbatim; lower case identifiers
 *      represent values that are supplied by the user.)<br>
 *     (Note 4: value may be enclosed in quotes if it contains whitespace.)<br>
 *     (Note 5: units may not contain whitespace.)<br>
 *     (Note 6: Carriage-return not allowed in entire text record.)
 *  @author eric
 */
public class DatalogTestRecord extends TestRecord
{
	private final String text;
	public final TestID id;
	public final String units;
	public final Object value;
	public final Data_t valueType;
	public final short siteNumber;
	public final short headNumber;
	
    
    /**
     * Constructor for initializing this record with binary stream data.
     * @param tdb The TestIdDatabase  is not used by this record, but is
     * required so STDF records have consistent constructor signatures.
     * @param dvd The DefaultValueDatabase is used to get the CPU type.
     * @param data The binary stream data for this record.  The array should
     * not contain the first four bytes of the record.
     */
    public DatalogTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.DTR, dvd.getCpuType(), data);
        text = getCn();
        StringTokenizer st = new StringTokenizer(text, ":");
        st.nextToken(); // burn TEXT_DATA
        String tname = st.nextToken();
        String valueUnitsOpt = st.nextToken();
        String tnum = "0";
        short sn = (short) 0;
        short hn = (short) 0;
        if (st.hasMoreTokens())
        {
        	tnum = st.nextToken();
        	if (st.hasMoreTokens())
        	{
        		String site = st.nextToken();
        		Short s = new Short(site.trim());
        		sn = s.shortValue();
        		if (st.hasMoreElements())
        		{
        			String head = st.nextToken();
        			Short ss = new Short(head.trim());
        			hn = ss.shortValue();
        		}
        		else hn = (short) 0;
        	}
        	else sn =  (short) 0;
        }
        siteNumber = sn;
        headNumber = hn;
        Long tn = new Long(tnum.trim());
        id = TestID.createTestID(tdb, tn, tname.trim());
        StringTokenizer st2 = new StringTokenizer(valueUnitsOpt, "\" \t()"); 
        String v = st2.nextToken();
        if (st2.hasMoreTokens()) units = st2.nextToken(); else units = "";
        int alpha = v.chars().filter(p -> !Character.isDigit(p) && p != '.').findFirst().orElse('0');
        Data_t type = null;
        if (alpha == '0') // it's probably a number
        {
            long cnt = v.chars().filter(p -> p == '.').count();
            if (cnt == 1L) // it's a float
            {
                value = new Float(v);	
                type = Data_t.R_4;
            }
            else if (cnt == 0L) // it's an int
            {
                value = new Integer(v);	
                type = Data_t.I_4;
            }
            else // it's a String
            {
                value = v;	
                type = Data_t.C_N;
            }
        }
        else // it's a String
        {
            value = v;	
            type = Data_t.C_N;
        }
        valueType = type;
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param text This field holds the TEXT_DAT field. It must not be null. The
     * maximum length of this String is 255 characters.
     */
    public DatalogTestRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, String text)
    {
    	this(tdb, dvd, getCnBytes(text));
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
		builder.append("DatalogTestRecord [text=");
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
		if (!(obj instanceof DatalogTestRecord)) return false;
		DatalogTestRecord other = (DatalogTestRecord) obj;
		if (!text.equals(other.text)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.TestRecord#getTestId()
	 */
	@Override
	public TestID getTestId()
	{
		return(id);
	}

}
