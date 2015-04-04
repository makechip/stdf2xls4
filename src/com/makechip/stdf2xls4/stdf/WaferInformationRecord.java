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
*** @version $Id: WaferInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferInformationRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteGroupNumber;
    private final String startDate;
    private final String waferID;
    
    /**
    *** @param p1
    **/
    public WaferInformationRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.WIR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) -1);
        long d = getU4(-1);
        startDate = new Date(d * 1000L).toString();
        waferID = getCn();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber); sb.append(Log.eol);
        sb.append("    start date: "); sb.append(startDate); sb.append(Log.eol);
        sb.append("    wafer ID: "); sb.append(waferID); sb.append(Log.eol);
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
     * @return the startDate
     */
    public String getStartDate()
    {
        return startDate;
    }

    /**
     * @return the waferID
     */
    public String getWaferID()
    {
        return waferID;
    }

}
