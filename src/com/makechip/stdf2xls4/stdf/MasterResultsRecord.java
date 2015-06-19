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

import java.util.Date;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: MasterResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterResultsRecord extends StdfRecord
{
    public final long finishDate;
    public final String dispCode;
    public final String lotDesc;
    public final String execDesc;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public MasterResultsRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MRR, dvd.getCpuType(), data);
        finishDate = getU4(0);
        String s = getFixedLengthString(1);
        if (s.equals(MISSING_STRING)) dispCode = " "; else dispCode = s;
        lotDesc = getCn();
        execDesc = getCn();
    }
    
    public MasterResultsRecord(
    	TestIdDatabase tdb,
    	DefaultValueDatabase dvd,
    	long finishDate, 
    	char dispCode, 
    	String lotDesc, 
    	String execDesc)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), finishDate, "" + dispCode, lotDesc, execDesc));
    }
    
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, finishDate, dispCode, lotDesc, execDesc);	
	}
	
	private static byte[] toBytes(Cpu_t cpuType, long finishDate, String dispCode, String lotDesc, String execDesc)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU4Bytes(finishDate));
		l.addAll(getFixedLengthStringBytes(dispCode));
		l.addAll(getCnBytes(lotDesc));
		l.addAll(getCnBytes(execDesc));
		return(l.toArray());
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    finish date: ").append(getFinishDate()).append(Log.eol);
        sb.append("    lot disposition code: ").append(dispCode).append(Log.eol);
        sb.append("    user lot description: ").append(lotDesc).append(Log.eol);
        sb.append("    exec lot description: ").append(execDesc).append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the finishDate
     */
    private String getFinishDate()
    {
        return(new Date(1000L * finishDate).toString());
    }

}
