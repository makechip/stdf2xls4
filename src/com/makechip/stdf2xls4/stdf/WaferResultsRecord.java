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
*** @version $Id: WaferResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferResultsRecord extends StdfRecord
{
    public final short headNumber;
    public final short siteGroupNumber;
    public final long finishDate;
    public final long partCount;
    public final long retestCount;
    public final long abortCount;
    public final long passCount;
    public final long functionalCount;
    public final String waferID;
    public final String fabWaferID;
    public final String waferFrameID;
    public final String waferMaskID;
    public final String userWaferDesc;
    public final String execWaferDesc;
    
    /**
    *** @param p1
    **/
    public WaferResultsRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.WRR, data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) 255);
        finishDate = getU4(-1L);
        partCount = getU4(-1L);
        retestCount = getU4(4294967295L);
        abortCount = getU4(4294967295L);
        passCount = getU4(4294967295L);
        functionalCount = getU4(4294967295L);
        waferID = getCn();
        fabWaferID = getCn();
        waferFrameID = getCn();
        waferMaskID = getCn();
        userWaferDesc = getCn();
        execWaferDesc = getCn();
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(getU4Bytes(finishDate));
		l.addAll(getU4Bytes(partCount));
		l.addAll(getU4Bytes(retestCount));
		l.addAll(getU4Bytes(abortCount));
		l.addAll(getU4Bytes(passCount));
		l.addAll(getU4Bytes(functionalCount));
		l.addAll(getCnBytes(waferID));
		l.addAll(getCnBytes(fabWaferID));
		l.addAll(getCnBytes(waferFrameID));
		l.addAll(getCnBytes(waferMaskID));
		l.addAll(getCnBytes(userWaferDesc));
		l.addAll(getCnBytes(execWaferDesc));
		bytes = l.toArray();
	}
	
	public WaferResultsRecord(
        short headNumber,
        short siteGroupNumber,
        long finishDate,
        long partCount,
        long retestCount,
        long abortCount,
        long passCount,
        long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
    {
		super(Record_t.WRR, null);
		this.headNumber = headNumber;
		this.siteGroupNumber = siteGroupNumber;
		this.finishDate = finishDate;
		this.partCount = partCount;
		this.retestCount = retestCount;
		this.abortCount = abortCount;
		this.passCount = passCount;
		this.functionalCount = functionalCount;
		this.waferID = waferID;
		this.fabWaferID = fabWaferID;
		this.waferFrameID = waferFrameID;
		this.waferMaskID = waferMaskID;
		this.userWaferDesc = userWaferDesc;
		this.execWaferDesc = execWaferDesc;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber).append(Log.eol);
        sb.append("    finish date: ").append(finishDate).append(Log.eol);
        sb.append("    number of parts tested: " + partCount).append(Log.eol);
        sb.append("    number of parts re-tested: " + retestCount).append(Log.eol);
        sb.append("    number of aborts: " + abortCount).append(Log.eol);
        sb.append("    number of good parts: " + passCount).append(Log.eol);
        sb.append("    number of functional parts: " + functionalCount).append(Log.eol);
        sb.append("    wafer ID: ").append(waferID).append(Log.eol);
        sb.append("    fab wafer ID: ").append(fabWaferID).append(Log.eol);
        sb.append("    wafer frame ID: ").append(waferFrameID).append(Log.eol);
        sb.append("    wafer mask ID: ").append(waferMaskID).append(Log.eol);
        sb.append("    user wafer description: ").append(userWaferDesc).append(Log.eol);
        sb.append("    exec wafer description: ").append(execWaferDesc).append(Log.eol);
        return(sb.toString());
    }

}
