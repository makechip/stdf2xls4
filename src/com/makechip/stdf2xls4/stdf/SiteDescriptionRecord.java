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


/**
*** @author eric
*** @version $Id: SiteDescriptionRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class SiteDescriptionRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteGroupNumber;
    private final short numSites;
    private final short[] siteNumbers;
    private final String handlerType;
    private final String handlerID;
    private final String probeCardType;
    private final String probeCardID;
    private final String loadBoardType;
    private final String loadBoardID;
    private final String dibBoardType;
    private final String dibBoardID;
    private final String ifaceCableType;
    private final String ifaceCableID;
    private final String contactorType;
    private final String contactorID;
    private final String laserType;
    private final String laserID;
    private final String equipType;
    private final String equipID;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public SiteDescriptionRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.SDR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) -1);
        numSites = getU1((short) 0);
        siteNumbers = new short[numSites];
        for (int i=0; i<numSites; i++) siteNumbers[i] = getU1((short) -1);
        handlerType = getCn();
        handlerID = getCn();
        probeCardType = getCn();
        probeCardID = getCn();
        loadBoardType = getCn();
        loadBoardID = getCn();
        dibBoardType = getCn();
        dibBoardID = getCn();
        ifaceCableType = getCn();
        ifaceCableID = getCn();
        contactorType = getCn();
        contactorID = getCn();
        laserType = getCn();
        laserID = getCn();
        equipType = getCn();
        equipID = getCn();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber); sb.append(Log.eol);
        sb.append("    number of sites: " + numSites); sb.append(Log.eol);
        sb.append("    site numbers:");
        for (int i=0; i<numSites; i++)
        {
            sb.append(" ");
            sb.append("" + siteNumbers[i]);
        }
        sb.append(Log.eol);
        sb.append("    handler type: "); sb.append(handlerType); sb.append(Log.eol);
        sb.append("    handler ID: "); sb.append(handlerID); sb.append(Log.eol);
        sb.append("    probe card type: "); sb.append(probeCardType); sb.append(Log.eol);
        sb.append("    probe card ID: "); sb.append(probeCardID); sb.append(Log.eol);
        sb.append("    loadboard type: "); sb.append(loadBoardType); sb.append(Log.eol);
        sb.append("    loadboard ID: "); sb.append(loadBoardID); sb.append(Log.eol);
        sb.append("    DIB board type: "); sb.append(dibBoardType); sb.append(Log.eol);
        sb.append("    DIB board ID: "); sb.append(dibBoardID); sb.append(Log.eol);
        sb.append("    interface cable type: "); sb.append(ifaceCableType); sb.append(Log.eol);
        sb.append("    interface cable ID: "); sb.append(ifaceCableID); sb.append(Log.eol);
        sb.append("    contactor type: "); sb.append(contactorType); sb.append(Log.eol);
        sb.append("    contactor ID: "); sb.append(contactorID); sb.append(Log.eol);
        sb.append("    laser type: "); sb.append(laserType); sb.append(Log.eol);
        sb.append("    laser ID: "); sb.append(laserID); sb.append(Log.eol);
        sb.append("    extra equipment type: "); sb.append(equipType); sb.append(Log.eol);
        sb.append("    extra equipment ID: "); sb.append(equipID); sb.append(Log.eol);
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
     * @return the numSites
     */
    public short getNumSites()
    {
        return numSites;
    }

    /**
     * @return the siteNumbers
     */
    public short[] getSiteNumbers()
    {
        return siteNumbers;
    }

    /**
     * @return the handlerType
     */
    public String getHandlerType()
    {
        return handlerType;
    }

    /**
     * @return the probeCardType
     */
    public String getProbeCardType()
    {
        return probeCardType;
    }

    /**
     * @return the probeCardID
     */
    public String getProbeCardID()
    {
        return probeCardID;
    }

    /**
     * @return the loadBoardType
     */
    public String getLoadBoardType()
    {
        return loadBoardType;
    }

    /**
     * @return the loadBoardID
     */
    public String getLoadBoardID()
    {
        return loadBoardID;
    }

    /**
     * @return the dibBoardType
     */
    public String getDibBoardType()
    {
        return dibBoardType;
    }

    /**
     * @return the dibBoardID
     */
    public String getDibBoardID()
    {
        return dibBoardID;
    }

    /**
     * @return the ifaceCableType
     */
    public String getIfaceCableType()
    {
        return ifaceCableType;
    }

    /**
     * @return the ifaceCableID
     */
    public String getIfaceCableID()
    {
        return ifaceCableID;
    }

    /**
     * @return the contactorType
     */
    public String getContactorType()
    {
        return contactorType;
    }

    /**
     * @return the contactorID
     */
    public String getContactorID()
    {
        return contactorID;
    }

    /**
     * @return the laserType
     */
    public String getLaserType()
    {
        return laserType;
    }

    /**
     * @return the laserID
     */
    public String getLaserID()
    {
        return laserID;
    }

    /**
     * @return the equipType
     */
    public String getEquipType()
    {
        return equipType;
    }

    /**
     * @return the equipID
     */
    public String getEquipID()
    {
        return equipID;
    }

}
