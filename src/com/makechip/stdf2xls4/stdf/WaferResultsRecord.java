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
*** @version $Id: WaferResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferResultsRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteGroupNumber;
    private final long finishDate;
    private final long partCount;
    private final long retestCount;
    private final long abortCount;
    private final long passCount;
    private final long functionalCount;
    private final String waferID;
    private final String fabWaferID;
    private final String waferFrameID;
    private final String waferMaskID;
    private final String userWaferDesc;
    private final String execWaferDesc;
    
    /**
    *** @param p1
    **/
    public WaferResultsRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.WRR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) -1);
        finishDate = getU4(-1);
        partCount = getU4(-1);
        retestCount = getU4(-1);
        abortCount = getU4(-1);
        passCount = getU4(-1);
        functionalCount = getU4(-1);
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
	
	public WaferResultsRecord(int sequenceNumber, int devNum,
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
		super(Record_t.WRR, sequenceNumber, devNum, null);
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
        sb.append("    exec wafer description: ").append(execWaferDesc).append(Log.eol).append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return headNumber;
    }

    /**
     * @return the siteGroupNumber
     */
    public short getSiteGroupNumber()
    {
        return siteGroupNumber;
    }

    /**
     * @return the finishDate
     */
    public String getFinishDate()
    {
        return(new Date(finishDate * 1000L).toString());
    }

    /**
     * @return the partCount
     */
    public long getPartCount()
    {
        return partCount;
    }

    /**
     * @return the retestCount
     */
    public long getRetestCount()
    {
        return retestCount;
    }

    /**
     * @return the abortCount
     */
    public long getAbortCount()
    {
        return abortCount;
    }

    /**
     * @return the passCount
     */
    public long getPassCount()
    {
        return passCount;
    }

    /**
     * @return the functionalCount
     */
    public long getFunctionalCount()
    {
        return functionalCount;
    }

    /**
     * @return the waferID
     */
    public String getWaferID()
    {
        return waferID;
    }

    /**
     * @return the fabWaferID
     */
    public String getFabWaferID()
    {
        return fabWaferID;
    }

    /**
     * @return the waferFrameID
     */
    public String getWaferFrameID()
    {
        return waferFrameID;
    }

    /**
     * @return the waferMaskID
     */
    public String getWaferMaskID()
    {
        return waferMaskID;
    }

    /**
     * @return the userWaferDesc
     */
    public String getUserWaferDesc()
    {
        return userWaferDesc;
    }

    /**
     * @return the execWaferDesc
     */
    public String getExecWaferDesc()
    {
        return execWaferDesc;
    }

}
