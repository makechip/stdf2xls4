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
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: PartCountRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PartCountRecord extends StdfRecord
{
    public final short headNumber;
    public final short siteNumber;
    public final long partsTested;
    public final long partsReTested;
    public final long aborts;
    public final long good;
    public final long functional;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public PartCountRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PCR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        partsTested = getU4(-1);
        partsReTested = getU4(4294967295L);
        aborts = getU4(4294967295L);
        good = getU4(4294967295L);
        functional = getU4(4294967295L);
    }
    
    public PartCountRecord(
    		TestIdDatabase tdb, DefaultValueDatabase dvd,
    		short headNumber,
    		short siteNumber,
    		long partsTested,
    		long partsReTested,
    		long aborts,
    		long good,
    		long functional)
    {
    	this(tdb,  dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, partsTested, partsReTested, aborts, good, functional));
    }
    
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteNumber, partsTested, partsReTested, aborts, good, functional);	
	}
	
	private static byte[] toBytes(
    	Cpu_t cpuType,
    	short headNumber,
    	short siteNumber,
    	long partsTested,
    	long partsReTested,
    	long aborts,
    	long good,
    	long functional)
	{
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(getU1Bytes(headNumber));
	    l.addAll(getU1Bytes(siteNumber));
	    l.addAll(cpuType.getU4Bytes(partsTested));
	    l.addAll(cpuType.getU4Bytes(partsReTested));
	    l.addAll(cpuType.getU4Bytes(aborts));
	    l.addAll(cpuType.getU4Bytes(good));
	    l.addAll(cpuType.getU4Bytes(functional));
	    return(l.toArray());
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site number: " + siteNumber).append(Log.eol);
        sb.append("    parts tested: " + partsTested).append(Log.eol);
        sb.append("    number of aborts: " + aborts).append(Log.eol);
        sb.append("    number good: " + good).append(Log.eol);
        sb.append("    number functional: " + functional).append(Log.eol);
        return(sb.toString());
    }

}
