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

import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields for a Site Description Record.
 *  @author eric
 */
public class SiteDescriptionRecord extends StdfRecord
{
    /**
     *  This is the HEAD_NUM field.
     */
    public final short headNumber;
    /**
     *  This is the SITE_GRP field.
     */
    public final short siteGroupNumber;
    /**
     *  This is the HAND_TYP field.
     */
    public final String handlerType;
    /**
     *  This is the HAND_ID field.
     */
    public final String handlerID;
    /**
     *  This is the CARD_TYP field.
     */
    public final String probeCardType;
    /**
     *  This is the CARD_ID field.
     */
    public final String probeCardID;
    /**
     *  This is the LOAD_TYP field.
     */
    public final String loadBoardType;
    /**
     *  This is the LOAD_ID field.
     */
    public final String loadBoardID;
    /**
     *  This is the DIB_TYP field.
     */
    public final String dibBoardType;
    /**
     *  This is the DIB_ID field.
     */
    public final String dibBoardID;
    /**
     *  This is the CABL_TYP field.
     */
    public final String ifaceCableType;
    /**
     *  This is the CABL_ID field.
     */
    public final String ifaceCableID;
    /**
     *  This is the CONT_TYP field.
     */
    public final String contactorType;
    /**
     *  This is the CONT_ID field.
     */
    public final String contactorID;
    /**
     *  This is the LASR_TYP field.
     */
    public final String laserType;
    /**
     *  This is the LASR_ID field.
     */
    public final String laserID;
    /**
     *  This is the EXTR_TYP field.
     */
    public final String equipType;
    /**
     *  This is the EXTR_ID field.
     */
    public final String equipID;
    
    private final int[] siteNumbers;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public SiteDescriptionRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.SDR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) -1);
        short numSites = getU1((short) 0);
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
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(headNumber, siteGroupNumber, siteNumbers, handlerType,
		                handlerID, probeCardType, probeCardID, loadBoardType, loadBoardID,
		                dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, contactorType,
		                contactorID, laserType, laserID, equipType, equipID);
	}
	
	private static byte[] toBytes(
		short headNumber,
		short siteGroupNumber,
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
		String equipID)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(getU1Bytes((short) siteNumbers.length));
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
        return(l.toArray());
	}
	
	/**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
	 * @param headNumber The HEAD_NUM field.
	 * @param siteGroupNumber The SITE_GRP field.
	 * @param siteNumbers The SITE_NUM field; the length of the array is the SITE_CNT Field.
	 * @param handlerType The HAND_TYP field.
	 * @param handlerID The HAND_ID field.
	 * @param probeCardType THE CARD_TYP field.
	 * @param probeCardID The CARD_ID field.
	 * @param loadBoardType The LOAD_TYP field.
	 * @param loadBoardID The LOAD_ID field.
	 * @param dibBoardType The DIB_TYP field.
	 * @param dibBoardID The DIB_ID field.
	 * @param ifaceCableType The CABL_TYP field.
	 * @param ifaceCableID The CABL_ID field.
	 * @param contactorType The CONT_TYP field.
	 * @param contactorID The CONT_ID field.
	 * @param laserType The LASR_TYP field.
	 * @param laserID The LASR_ID field.
	 * @param equipType The EXTR_TYP field.
	 * @param equipID The EXTR_ID field.
	 */
	public SiteDescriptionRecord(
	    TestIdDatabase tdb,
	    DefaultValueDatabase dvd,
		short headNumber,
		short siteGroupNumber,
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
		this(tdb, dvd, toBytes(headNumber, siteGroupNumber, siteNumbers, handlerType,
		                handlerID, probeCardType, probeCardID, loadBoardType, loadBoardID,
		                dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, contactorType,
		                contactorID, laserType, laserID, equipType, equipID));
	}

	/**
	 * This method gets the SITE_NUM field.
	 * @return A deep copy of the SITE_NUM array.
	 */
    public int[] getSiteNumbers()
    {
        return Arrays.copyOf(siteNumbers, siteNumbers.length);
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteDescriptionRecord [headNumber=").append(headNumber);
		builder.append(", siteGroupNumber=").append(siteGroupNumber);
		builder.append(", handlerType=").append(handlerType);
		builder.append(", handlerID=").append(handlerID);
		builder.append(", probeCardType=").append(probeCardType);
		builder.append(", probeCardID=").append(probeCardID);
		builder.append(", loadBoardType=").append(loadBoardType);
		builder.append(", loadBoardID=").append(loadBoardID);
		builder.append(", dibBoardType=").append(dibBoardType);
		builder.append(", dibBoardID=").append(dibBoardID);
		builder.append(", ifaceCableType=").append(ifaceCableType);
		builder.append(", ifaceCableID=").append(ifaceCableID);
		builder.append(", contactorType=").append(contactorType);
		builder.append(", contactorID=").append(contactorID);
		builder.append(", laserType=").append(laserType);
		builder.append(", laserID=").append(laserID);
		builder.append(", equipType=").append(equipType);
		builder.append(", equipID=").append(equipID);
		builder.append(", siteNumbers=").append(Arrays.toString(siteNumbers));
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + contactorID.hashCode();
		result = prime * result + contactorType.hashCode();
		result = prime * result + dibBoardID.hashCode();
		result = prime * result + dibBoardType.hashCode();
		result = prime * result + equipID.hashCode();
		result = prime * result + equipType.hashCode();
		result = prime * result + handlerID.hashCode();
		result = prime * result + handlerType.hashCode();
		result = prime * result + headNumber;
		result = prime * result + ifaceCableID.hashCode();
		result = prime * result + ifaceCableType.hashCode();
		result = prime * result + laserID.hashCode();
		result = prime * result + laserType.hashCode();
		result = prime * result + loadBoardID.hashCode();
		result = prime * result + loadBoardType.hashCode();
		result = prime * result + probeCardID.hashCode();
		result = prime * result + probeCardType.hashCode();
		result = prime * result + siteGroupNumber;
		result = prime * result + Arrays.hashCode(siteNumbers);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SiteDescriptionRecord)) return false;
		SiteDescriptionRecord other = (SiteDescriptionRecord) obj;
		if (!contactorID.equals(other.contactorID)) return false;
		if (!contactorType.equals(other.contactorType)) return false;
		if (!dibBoardID.equals(other.dibBoardID)) return false;
		if (!dibBoardType.equals(other.dibBoardType)) return false;
		if (!equipID.equals(other.equipID)) return false;
		if (!equipType.equals(other.equipType)) return false;
		if (!handlerID.equals(other.handlerID)) return false;
		if (!handlerType.equals(other.handlerType)) return false;
		if (headNumber != other.headNumber) return false;
		if (!ifaceCableID.equals(other.ifaceCableID)) return false;
		if (!ifaceCableType.equals(other.ifaceCableType)) return false;
		if (!laserID.equals(other.laserID)) return false;
		if (!laserType.equals(other.laserType)) return false;
		if (!loadBoardID.equals(other.loadBoardID)) return false;
		if (!loadBoardType.equals(other.loadBoardType)) return false;
		if (!probeCardID.equals(other.probeCardID)) return false;
		if (!probeCardType.equals(other.probeCardType)) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		if (!Arrays.equals(siteNumbers, other.siteNumbers)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

}
