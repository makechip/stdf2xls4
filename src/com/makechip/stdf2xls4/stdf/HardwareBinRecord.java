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
*** @version $Id: HardwareBinRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class HardwareBinRecord extends StdfRecord
{
    public final short headNumber;
    public final short siteNumber;
    public final int hwBin;
    public final long binCnt;
    public final char pf;
    public final String binName;
    /**
    *** @param p1
    *** @param p2
    **/
    public HardwareBinRecord(int sequenceNumber, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.HBR, sequenceNumber, data);
        headNumber = getU1((short) 0);
        siteNumber = getU1((short) 0);
        hwBin = getU2(-1);
        binCnt = getU4(0);
        pf = getFixedLengthString(1).charAt(0);
        binName = getCn();
    }
    
    public HardwareBinRecord(int sequenceNumber, short headNumber,
    		short siteNumber, int hwBin, long binCnt, char pf, String binName)
    {
    	super(Record_t.HBR, sequenceNumber, null);
    	this.headNumber = headNumber;
    	this.siteNumber = siteNumber;
    	this.hwBin = hwBin;
    	this.binCnt = binCnt;
    	this.pf = pf;
    	this.binName = binName;
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(getU2Bytes(hwBin));
		l.addAll(getU4Bytes(binCnt));
		l.addAll(getFixedLengthStringBytes("" + pf));
		l.addAll(getCnBytes(binName));
		bytes = l.toArray();
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site number: " + siteNumber).append(Log.eol);
        sb.append("    HW bin number:" + hwBin).append(Log.eol);
        sb.append("    bin count: " + binCnt).append(Log.eol);
        sb.append("    P/F: "); sb.append(pf).append(Log.eol);
        sb.append("    bin name: ").append(binName).append(Log.eol);
        return(sb.toString());
    }
    
}
