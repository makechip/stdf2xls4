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
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
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
    
    public final IntList siteNumbers; // short
    
    public SiteDescriptionRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super(Record_t.SDR);
        headNumber = cpu.getU1(is);
        siteGroupNumber = cpu.getU1(is);
        final int k = cpu.getU1(is);
        int l = 3;
       	siteNumbers = new IntList(Data_t.U1, cpu, k, is);
       	l += k;
        if (l < recLen)
        {
            handlerType = cpu.getCN(is);
            l += 1 + handlerType.length();
        }
        else handlerType = null;
        if (l < recLen)
        {
            handlerID = cpu.getCN(is);
            l += 1 + handlerID.length();
        }
        else handlerID = null;
        if (l < recLen)
        {
            probeCardType = cpu.getCN(is);
            l += 1 + probeCardType.length();
        }
        else probeCardType = null;
        if (l < recLen)
        {
            probeCardID = cpu.getCN(is);
            l += 1 + probeCardID.length();
        }
        else probeCardID = null;
        if (l < recLen)
        {
            loadBoardType = cpu.getCN(is);
            l += 1 + loadBoardType.length();
        }
        else loadBoardType = null;
        if (l < recLen)
        {
            loadBoardID = cpu.getCN(is);
            l += 1 + loadBoardID.length();
        }
        else loadBoardID = null;
        if (l < recLen)
        {
            dibBoardType = cpu.getCN(is);
            l += 1 + dibBoardType.length();
        }
        else dibBoardType = null;
        if (l < recLen)
        {
            dibBoardID = cpu.getCN(is);
            l += 1 + dibBoardID.length();
        }
        else dibBoardID = null;
        if (l < recLen)
        {
            ifaceCableType = cpu.getCN(is);
            l += 1 + ifaceCableType.length();
        }
        else ifaceCableType = null;
        if (l < recLen)
        {
            ifaceCableID = cpu.getCN(is);
            l += 1 + ifaceCableID.length();
        }
        else ifaceCableID = null;
        if (l < recLen)
        {
            contactorType = cpu.getCN(is);
            l += 1 + contactorType.length();
        }
        else contactorType = null;
        if (l < recLen)
        {
            contactorID = cpu.getCN(is);
            l += 1 + contactorID.length();
        }
        else contactorID = null;
        if (l < recLen)
        {
            laserType = cpu.getCN(is);
            l += 1 + laserType.length();
        }
        else laserType = null;
        if (l < recLen)
        {
            laserID = cpu.getCN(is);
            l += 1 + laserID.length();
        }
        else laserID = null;
        if (l < recLen)
        {
            equipType = cpu.getCN(is);
            l += 1 + equipType.length();
        }
        else equipType = null;
        if (l < recLen)
        {
            equipID = cpu.getCN(is);
            l += 1 + equipID.length();
        }
        else equipID = null;
        if (l != recLen) throw new RuntimeException("Record length error in SiteDescriptionRecord.");
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteGroupNumber, siteNumbers.getArray(), handlerType, 
				           handlerID, probeCardType, probeCardID, loadBoardType, 
					       loadBoardID, dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, 
					       contactorType, contactorID, laserType, laserID, equipType, equipID);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.SDR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static byte[] toBytes(
		Cpu_t cpu,
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
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteGroupNumber));
		l.addAll(cpu.getU1Bytes((short) siteNumbers.length));
		for (int i=0; i<siteNumbers.length; i++) l.addAll(cpu.getU1Bytes((short) siteNumbers[i]));
		if (handlerType != null)
		{
		  l.addAll(cpu.getCNBytes(handlerType));
		  if (handlerID != null)
		  {
            l.addAll(cpu.getCNBytes(handlerID));
            if (probeCardType != null)
            {
              l.addAll(cpu.getCNBytes(probeCardType));
              if (probeCardID != null)
              {
                l.addAll(cpu.getCNBytes(probeCardID));
                if (loadBoardType != null)
                {
                  l.addAll(cpu.getCNBytes(loadBoardType));
                  if (loadBoardID != null)
                  {
                    l.addAll(cpu.getCNBytes(loadBoardID));
                    if (dibBoardType != null)
                    {
                      l.addAll(cpu.getCNBytes(dibBoardType));
                      if (dibBoardID != null)
                      {
                        l.addAll(cpu.getCNBytes(dibBoardID));
                        if (ifaceCableType != null)
                        {
                          l.addAll(cpu.getCNBytes(ifaceCableType));
                          if (ifaceCableID != null)
                          {
                            l.addAll(cpu.getCNBytes(ifaceCableID));
                            if (contactorType != null)
                            {
                              l.addAll(cpu.getCNBytes(contactorType));
                              if (contactorID != null)
                              {
                                l.addAll(cpu.getCNBytes(contactorID));
                                if (laserType != null)
                                {
                                  l.addAll(cpu.getCNBytes(laserType));
                                  if (laserID != null)
                                  {
                                    l.addAll(cpu.getCNBytes(laserID));
                                    if (equipType != null)
                                    {
                                      l.addAll(cpu.getCNBytes(equipType));
                                      if (equipID != null)
                                      {
                                        l.addAll(cpu.getCNBytes(equipID));
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
		  }
		}
        return(l.toArray());
	}
	
	/**
     * This constructor is used to make a SiteDescriptionRecord with field values. 
     * @param cpu  The CPU type.
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
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public SiteDescriptionRecord(
		Cpu_t cpu,
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
		this(cpu, null,
			 getRecLen(siteNumbers, handlerType,
		             handlerID, probeCardType, probeCardID, loadBoardType, loadBoardID,
		             dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, contactorType,
		             contactorID, laserType, laserID, equipType, equipID),
			 new ByteInputStream(toBytes(cpu, headNumber, siteGroupNumber, 
					 siteNumbers, handlerType, handlerID, probeCardType, probeCardID, loadBoardType, 
					 loadBoardID, dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, 
					 contactorType, contactorID, laserType, laserID, equipType, equipID)));
	}

	private static int getRecLen(int[] siteNumbers, 
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
		int l = 3 + siteNumbers.length;
        if (handlerType != null) l += 1 + handlerType.length(); else return(l);
        if (handlerID != null) l += 1 + handlerID.length(); else return(l);
        if (probeCardType != null) l += 1 + probeCardType.length(); else return(l);
        if (probeCardID != null) l += 1 + probeCardID.length(); else return(l);
        if (loadBoardType != null) l += 1 + loadBoardType.length(); else return(l);
        if (loadBoardID != null) l += 1 + loadBoardID.length(); else return(l);
        if (dibBoardType != null) l += 1 + dibBoardType.length(); else return(l);
        if (dibBoardID != null) l += 1 + dibBoardID.length(); else return(l);
        if (ifaceCableType != null) l += 1 + ifaceCableType.length(); else return(l);
        if (ifaceCableID != null) l += 1 + ifaceCableID.length(); else return(l);
        if (contactorType != null) l += 1 + contactorType.length(); else return(l);
        if (contactorID != null) l += 1 + contactorID.length(); else return(l);
        if (laserType != null) l += 1 + laserType.length(); else return(l);
        if (laserID != null) l += 1 + laserID.length(); else return(l);
        if (equipType != null) l += 1 + equipType.length(); else return(l);
        if (equipID != null) l += 1 + equipID.length();
		return(l);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactorID == null) ? 0 : contactorID.hashCode());
		result = prime * result + ((contactorType == null) ? 0 : contactorType.hashCode());
		result = prime * result + ((dibBoardID == null) ? 0 : dibBoardID.hashCode());
		result = prime * result + ((dibBoardType == null) ? 0 : dibBoardType.hashCode());
		result = prime * result + ((equipID == null) ? 0 : equipID.hashCode());
		result = prime * result + ((equipType == null) ? 0 : equipType.hashCode());
		result = prime * result + ((handlerID == null) ? 0 : handlerID.hashCode());
		result = prime * result + ((handlerType == null) ? 0 : handlerType.hashCode());
		result = prime * result + headNumber;
		result = prime * result + ((ifaceCableID == null) ? 0 : ifaceCableID.hashCode());
		result = prime * result + ((ifaceCableType == null) ? 0 : ifaceCableType.hashCode());
		result = prime * result + ((laserID == null) ? 0 : laserID.hashCode());
		result = prime * result + ((laserType == null) ? 0 : laserType.hashCode());
		result = prime * result + ((loadBoardID == null) ? 0 : loadBoardID.hashCode());
		result = prime * result + ((loadBoardType == null) ? 0 : loadBoardType.hashCode());
		result = prime * result + ((probeCardID == null) ? 0 : probeCardID.hashCode());
		result = prime * result + ((probeCardType == null) ? 0 : probeCardType.hashCode());
		result = prime * result + siteGroupNumber;
		result = prime * result + siteNumbers.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SiteDescriptionRecord)) return false;
		SiteDescriptionRecord other = (SiteDescriptionRecord) obj;
		if (contactorID == null)
		{
			if (other.contactorID != null) return false;
		} 
		else if (!contactorID.equals(other.contactorID)) return false;
		if (contactorType == null)
		{
			if (other.contactorType != null) return false;
		} 
		else if (!contactorType.equals(other.contactorType)) return false;
		if (dibBoardID == null)
		{
			if (other.dibBoardID != null) return false;
		} 
		else if (!dibBoardID.equals(other.dibBoardID)) return false;
		if (dibBoardType == null)
		{
			if (other.dibBoardType != null) return false;
		} 
		else if (!dibBoardType.equals(other.dibBoardType)) return false;
		if (equipID == null)
		{
			if (other.equipID != null) return false;
		} 
		else if (!equipID.equals(other.equipID)) return false;
		if (equipType == null)
		{
			if (other.equipType != null) return false;
		} 
		else if (!equipType.equals(other.equipType)) return false;
		if (handlerID == null)
		{
			if (other.handlerID != null) return false;
		} 
		else if (!handlerID.equals(other.handlerID)) return false;
		if (handlerType == null)
		{
			if (other.handlerType != null) return false;
		} 
		else if (!handlerType.equals(other.handlerType)) return false;
		if (headNumber != other.headNumber) return false;
		if (ifaceCableID == null)
		{
			if (other.ifaceCableID != null) return false;
		} 
		else if (!ifaceCableID.equals(other.ifaceCableID)) return false;
		if (ifaceCableType == null)
		{
			if (other.ifaceCableType != null) return false;
		} 
		else if (!ifaceCableType.equals(other.ifaceCableType)) return false;
		if (laserID == null)
		{
			if (other.laserID != null) return false;
		} 
		else if (!laserID.equals(other.laserID)) return false;
		if (laserType == null)
		{
			if (other.laserType != null) return false;
		} 
		else if (!laserType.equals(other.laserType)) return false;
		if (loadBoardID == null)
		{
			if (other.loadBoardID != null) return false;
		} 
		else if (!loadBoardID.equals(other.loadBoardID)) return false;
		if (loadBoardType == null)
		{
			if (other.loadBoardType != null) return false;
		} 
		else if (!loadBoardType.equals(other.loadBoardType)) return false;
		if (probeCardID == null)
		{
			if (other.probeCardID != null) return false;
		} 
		else if (!probeCardID.equals(other.probeCardID)) return false;
		if (probeCardType == null)
		{
			if (other.probeCardType != null) return false;
		} 
		else if (!probeCardType.equals(other.probeCardType)) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		return(siteNumbers.equals(other.siteNumbers));
	}


}
