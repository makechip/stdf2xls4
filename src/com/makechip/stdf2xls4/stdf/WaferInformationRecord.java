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

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: WaferInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferInformationRecord extends StdfRecord
{
    public final short headNumber;
    public final short siteGroupNumber;
    public final long startDate;
    public final String waferID;
    
    /**
    *** @param p1
    **/
    public WaferInformationRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.WIR, data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) 255);
        startDate = getU4(-1);
        waferID = getCn();
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(getU4Bytes(startDate));
		l.addAll(getCnBytes(waferID));
		bytes = l.toArray();
	}
	
	public WaferInformationRecord(
		short headNumber,
		short siteGroupNumber,
		long startDate,
		String waferID)
	{
		super(Record_t.WIR, null);
		this.headNumber = headNumber;
		this.siteGroupNumber = siteGroupNumber;
		this.startDate = startDate;
		this.waferID = waferID;
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber).append(Log.eol);
        sb.append("    start date: "); sb.append(startDate).append(Log.eol);
        sb.append("    wafer ID: "); sb.append(waferID).append(Log.eol);
        return(sb.toString());
    }
    
}
