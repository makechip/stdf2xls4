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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
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
    
    private final short[] siteNumbers;
    
    public SiteDescriptionRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        headNumber = cpu.getU1(is);
        siteGroupNumber = cpu.getU1(is);
        short k = cpu.getU1(is);
        int l = 3;
       	siteNumbers = new short[k];
        if (k != 0)
        {
        	for (int i=0; i<k; i++)
        	{
        		siteNumbers[i] = cpu.getU1(is);
        		l++;
        	}
        }
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
        if (l != recLen) throw new StdfException("Record length error in SiteDescriptionRecord.");
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteGroupNumber, siteNumbers, handlerType, 
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
		short[] siteNumbers,
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
		for (int i=0; i<siteNumbers.length; i++) l.addAll(cpu.getU1Bytes(siteNumbers[i]));
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
		short[] siteNumbers,
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
		) throws IOException, StdfException
	{
		this(cpu, 
			 getRecLen(siteNumbers, handlerType,
		             handlerID, probeCardType, probeCardID, loadBoardType, loadBoardID,
		             dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, contactorType,
		             contactorID, laserType, laserID, equipType, equipID),
			 new DataInputStream(new ByteArrayInputStream(toBytes(cpu, headNumber, siteGroupNumber, 
					 siteNumbers, handlerType, handlerID, probeCardType, probeCardID, loadBoardType, 
					 loadBoardID, dibBoardType, dibBoardID, ifaceCableType, ifaceCableID, 
					 contactorType, contactorID, laserType, laserID, equipType, equipID))));
	}

	private static int getRecLen(short[] siteNumbers, 
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
	/**
	 * This method gets the SITE_NUM field.
	 * @return A deep copy of the SITE_NUM array.
	 */
    public short[] getSiteNumbers()
    {
        return Arrays.copyOf(siteNumbers, siteNumbers.length);
    }


}
