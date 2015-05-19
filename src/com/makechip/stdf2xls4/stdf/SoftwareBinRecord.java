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

import com.makechip.util.Log;



/**
*** @author eric
*** @version $Id: SoftwareBinRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class SoftwareBinRecord extends StdfRecord
{
    public final short headNumber;
    public final short siteNumber;
    public final int swBinNumber;
    public final int count;
    public final String pf;
    public final String binName;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public SoftwareBinRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.SBR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        swBinNumber = getU2(-1);
        count = getU2(0);
        String s = getFixedLengthString(1);
        byte[] b = s.getBytes();
        if (b.length == 1)
        {
        	if (b[0] == (byte) 0) pf = "0";
        	else pf = s;
        }
        else pf = s;
        binName = getCn();
    }

	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.addAll(getU2Bytes(swBinNumber));
		l.addAll(getU2Bytes(count));
		l.addAll(getFixedLengthStringBytes(pf));
		l.addAll(getCnBytes(binName));
		bytes = l.toArray();
	}
	
	public SoftwareBinRecord(int sequenceNumber, int devNum,
		short headNumber,
		short siteNumber,
		int swBinNumber,
		int count,
		String pf,
		String binName)
	{
		super(Record_t.SBR, sequenceNumber, devNum, null);
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
		this.swBinNumber = swBinNumber;
		this.count = count;
		this.pf = pf;
		this.binName = binName;
	}
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site number: " + siteNumber).append(Log.eol);
        sb.append("    software bin number: " + swBinNumber).append(Log.eol);
        sb.append("    bin count: " + count).append(Log.eol);
        sb.append("    pass/fail indication: ").append(pf).append(Log.eol);
        sb.append("    bin name: ").append(binName).append(Log.eol);
        return(sb.toString());
    }
    
}
