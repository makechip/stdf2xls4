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
    public MasterResultsRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MRR, sequenceNumber, devNum, data);
        finishDate = getU4(0);
        dispCode = getFixedLengthString(1);
        lotDesc = getCn();
        execDesc = getCn();
    }
    
    public MasterResultsRecord(int sequenceNumber, int devNum,
    		long finishDate,
    		char dispCode,
    		String lotDesc,
    		String execDesc)
    {
    	super(Record_t.MRR, sequenceNumber, devNum, null);
    	this.finishDate = finishDate;
    	this.dispCode = "" + dispCode;
    	this.lotDesc = lotDesc;
    	this.execDesc = execDesc;
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU4Bytes(finishDate));
		l.addAll(getFixedLengthStringBytes(dispCode));
		l.addAll(getCnBytes(lotDesc));
		l.addAll(getCnBytes(execDesc));
		bytes = l.toArray();
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
