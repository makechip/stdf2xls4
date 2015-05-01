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

import java.util.Arrays;

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
    private final int[] siteNumbers;
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
        siteNumbers = new int[numSites];
        Arrays.setAll(siteNumbers, p -> getU1((short) -1));
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
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(getU1Bytes(numSites));
		Arrays.stream(siteNumbers).forEach(p -> l.addAll(getU1Bytes((short) p)));
		l.addAll(getCnBytes(handlerType));
        l.addAll(getCnBytes(handlerID));
        l.addAll(getCnBytes(probeCardType));
        l.addAll(getCnBytes(probeCardID));
        l.addAll(getCnBytes(loadBoardType));
        l.addAll(getCnBytes(loadBoardID));
        l.addAll(getCnBytes(dibBoardType));
        l.addAll(getCnBytes(dibBoardID));
        l.addAll(getCnBytes(ifaceCableType));
        l.addAll(getCnBytes(ifaceCableID));
        l.addAll(getCnBytes(contactorType));
        l.addAll(getCnBytes(contactorID));
        l.addAll(getCnBytes(laserType));
        l.addAll(getCnBytes(laserID));
        l.addAll(getCnBytes(equipType));
        l.addAll(getCnBytes(equipID));
        bytes = l.toArray();
	}
	
	public SiteDescriptionRecord(int sequenceNumber, int devNum,
		short headNumber,
		short siteGroupNumber,
		short numSites,
		int[] siteNumbers,
		String handlerType,
		String handlerID,
		String probeCardType,
		String probeCardID,
		String loadBoardType,
		String loadBoardID,
		String dibBoardType,
		String dibBoardID,
		String ifaceCableType,
		String ifaceCableID,
		String contactorType,
		String contactorID,
		String laserType,
		String laserID,
		String equipType,
		String equipID
		)
	{
		super(Record_t.SDR, sequenceNumber, devNum, null);
		this.headNumber = headNumber;
		this.siteGroupNumber = siteGroupNumber;
		this.numSites = numSites;
	    this.siteNumbers = Arrays.copyOf(siteNumbers, siteNumbers.length);
	    this.handlerType = handlerType;
	    this.handlerID = handlerID;
	    this.probeCardType = probeCardType;
	    this.probeCardID = probeCardID;
	    this.loadBoardType = loadBoardType;
	    this.loadBoardID = loadBoardID;
	    this.dibBoardType = dibBoardType;
	    this.dibBoardID = dibBoardID;
	    this.ifaceCableType = ifaceCableType;
	    this.ifaceCableID = ifaceCableID;
	    this.contactorType = contactorType;
	    this.contactorID = contactorID;
	    this.laserType = laserType;
	    this.laserID = laserID;
	    this.equipType = equipType;
	    this.equipID = equipID;
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site group number: " + siteGroupNumber).append(Log.eol);
        sb.append("    number of sites: " + numSites).append(Log.eol);
        sb.append("    site numbers:");
        Arrays.stream(siteNumbers).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    handler type: ").append(handlerType).append(Log.eol);
        sb.append("    handler ID: ").append(handlerID).append(Log.eol);
        sb.append("    probe card type: ").append(probeCardType).append(Log.eol);
        sb.append("    probe card ID: ").append(probeCardID).append(Log.eol);
        sb.append("    loadboard type: ").append(loadBoardType).append(Log.eol);
        sb.append("    loadboard ID: ").append(loadBoardID).append(Log.eol);
        sb.append("    DIB board type: ").append(dibBoardType).append(Log.eol);
        sb.append("    DIB board ID: ").append(dibBoardID).append(Log.eol);
        sb.append("    interface cable type: ").append(ifaceCableType).append(Log.eol);
        sb.append("    interface cable ID: ").append(ifaceCableID).append(Log.eol);
        sb.append("    contactor type: ").append(contactorType).append(Log.eol);
        sb.append("    contactor ID: ").append(contactorID).append(Log.eol);
        sb.append("    laser type: ").append(laserType).append(Log.eol);
        sb.append("    laser ID: ").append(laserID).append(Log.eol);
        sb.append("    extra equipment type: ").append(equipType).append(Log.eol);
        sb.append("    extra equipment ID: ").append(equipID).append(Log.eol);
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
    public int[] getSiteNumbers()
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
