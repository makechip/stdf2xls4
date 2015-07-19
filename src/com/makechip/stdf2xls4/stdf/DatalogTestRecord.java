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

import java.util.StringTokenizer;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
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
	public final String testName;
	public final Object value;
	public final String units;
	public final Long testNumber;
	public final Data_t valueType;
	public final Short siteNumber;
	public final Short headNumber;
	
    
    /**
     * This CTOR is a dummy CTOR that should not be used.  It is included so that
     * the initializer is available to the Record_t.DTRX enum.  It is never actually used.
     */
    public DatalogTestRecord(Cpu_t cpu, int recLen, ByteInputStream is)
    {
        super();
        testName = null;
        value = null;
        units = null;
        testNumber = null;
        valueType = null;
        siteNumber = null;
        headNumber = null;
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param text This field holds the TEXT_DAT field. It must not be null. The
     * maximum length of this String is 255 characters.
     */
    public DatalogTestRecord(String text)
    {
    	super();
        StringTokenizer st = new StringTokenizer(text, ":");
        st.nextToken(); // burn TEXT_DATA
        testName = st.nextToken();
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
        testNumber = new Long(tnum.trim());
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
                type = Data_t.R4;
            }
            else if (cnt == 0L) // it's an int
            {
                value = new Integer(v);	
                type = Data_t.I4;
            }
            else // it's a String
            {
                value = v;	
                type = Data_t.CN;
            }
        }
        else // it's a String
        {
            value = v;	
            type = Data_t.CN;
        }
        valueType = type;
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */

    // TEXT_DATA : test_name : value [(units)] [ : test_number [ : site_number [ : head_number ]]]<br>
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		StringBuilder sb = new StringBuilder("TEXT_DATA : ");
	    sb.append(testName);
	    sb.append(" : ");
	    sb.append(value.toString());
	    if (units != null) sb.append(" ").append(units);
	    if (testNumber != null) sb.append(" : ").append(testNumber.toString());
	    if (siteNumber != null) sb.append(" : ").append(siteNumber.toString());
	    if (headNumber != null) sb.append(" : ").append(headNumber.toString());
	    String text = sb.toString();
	    byte[] b = cpu.getCNBytes(text);
	    TByteArrayList l = getHeaderBytes(cpu, Record_t.DTR, b.length);
	    l.addAll(b);
		return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headNumber == null) ? 0 : headNumber.hashCode());
		result = prime * result + ((siteNumber == null) ? 0 : siteNumber.hashCode());
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + ((testNumber == null) ? 0 : testNumber.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((valueType == null) ? 0 : valueType.hashCode());
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
		if (!(obj instanceof DatalogTestRecord)) return false;
		DatalogTestRecord other = (DatalogTestRecord) obj;
		if (headNumber == null)
		{
			if (other.headNumber != null) return false;
		} 
		else if (!headNumber.equals(other.headNumber)) return false;
		if (siteNumber == null)
		{
			if (other.siteNumber != null) return false;
		} else if (!siteNumber.equals(other.siteNumber)) return false;
		if (testName == null)
		{
			if (other.testName != null) return false;
		} else if (!testName.equals(other.testName)) return false;
		if (testNumber == null)
		{
			if (other.testNumber != null) return false;
		} else if (!testNumber.equals(other.testNumber)) return false;
		if (units == null)
		{
			if (other.units != null) return false;
		} else if (!units.equals(other.units)) return false;
		if (value == null)
		{
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		if (valueType != other.valueType) return false;
		return true;
	}


}
