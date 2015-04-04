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
    private final String finishDate;
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
        long d = getU4(-1);
        finishDate = new Date(d * 1000L).toString();
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
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber); sb.append(Log.eol);
        sb.append("    finish date: "); sb.append(finishDate); sb.append(Log.eol);
        sb.append("    number of parts tested: " + partCount); sb.append(Log.eol);
        sb.append("    number of parts re-tested: " + retestCount); sb.append(Log.eol);
        sb.append("    number of aborts: " + abortCount); sb.append(Log.eol);
        sb.append("    number of good parts: " + passCount); sb.append(Log.eol);
        sb.append("    number of functional parts: " + functionalCount); sb.append(Log.eol);
        sb.append("    wafer ID: "); sb.append(waferID); sb.append(Log.eol);
        sb.append("    fab wafer ID: "); sb.append(fabWaferID); sb.append(Log.eol);
        sb.append("    wafer frame ID: "); sb.append(waferFrameID); sb.append(Log.eol);
        sb.append("    wafer mask ID: "); sb.append(waferMaskID); sb.append(Log.eol);
        sb.append("    user wafer description: "); sb.append(userWaferDesc); sb.append(Log.eol);
        sb.append("    exec wafer description: "); sb.append(execWaferDesc); sb.append(Log.eol);
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
        return finishDate;
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
