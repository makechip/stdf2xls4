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

import com.makechip.util.Log;

import gnu.trove.map.hash.TIntObjectHashMap;


/**
*** @author eric
*** @version $Id: PinMapRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinMapRecord extends StdfRecord
{
    private final int pmrIdx;
    private final int channelType;
    private final String channelName;
    private final String physicalPinName;
    private final String logicalPinName;
    private final short headNumber;
    private final short siteNumber;
    private final String pinName;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public PinMapRecord(Tracker tracker, int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PMR, sequenceNumber, devNum, data);
        pmrIdx = getU2(-1);
        channelType = getU2(-1);
        channelName = getCn();
        physicalPinName = getCn();
        logicalPinName = getCn();
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) 1);
        pinName = (tracker.isLtx()) ? physicalPinName : channelName; 
        TIntObjectHashMap<String> m = tracker.pinmap.get(siteNumber);
        if (m == null)
        {
            m = new TIntObjectHashMap<String>();
            tracker.pinmap.put(siteNumber, m);
        }
        m.put(pmrIdx, pinName);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    pin index: " + pmrIdx); sb.append(Log.eol);
        sb.append("    channel type: " + channelType); sb.append(Log.eol);
        sb.append("    channel name: "); sb.append(channelName); sb.append(Log.eol);
        sb.append("    physical pin name: "); sb.append(physicalPinName); sb.append(Log.eol);
        sb.append("    logical pin name: "); sb.append(logicalPinName); sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the pmrIdx
     */
    public int getPmrIdx()
    {
        return pmrIdx;
    }

    /**
     * @return the channelType
     */
    public int getChannelType()
    {
        return channelType;
    }

    /**
     * @return the channelName
     */
    public String getChannelName()
    {
        return channelName;
    }

    /**
     * @return the physicalPinName
     */
    public String getPhysicalPinName()
    {
        return physicalPinName;
    }

    /**
     * @return the logicalPinName
     */
    public String getLogicalPinName()
    {
        return logicalPinName;
    }

    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return headNumber;
    }

    /**
     * @return the siteNumber
     */
    public short getSiteNumber()
    {
        return siteNumber;
    }

}
