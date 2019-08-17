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

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields for a Pin Map Record.
 *  @author eric
 */
public class PinMapRecord extends StdfRecord
{
    /**
     *  The PMR_INDX field.
     */
    public final int pmrIdx;
    /**
     *  The CHAN_TYP field.
     */
    public final Integer channelType;
    /**
     *  The CHAN_NAM field.
     */
    public final String channelName;
    /**
     *  The PHY_NAM field.
     */
    public final String physicalPinName;
    /**
     *  The LOG_NAM field.
     */
    public final String logicalPinName;
    /**
     *  The HEAD_NUM field.
     */
    public final Short headNumber;
    /**
     *  The SITE_NUM field.
     */
    public final Short siteNumber;
    
    /**
     * 
     * @param cpu
     * @param recLen
     * @param is
     * @throws IOException
     * @throws StdfException
     */
    public PinMapRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.PMR);
        String tmpPhys = null;
        String tmpLog = null;
        pmrIdx = cpu.getU2(is);
        int l = Data_t.U2.numBytes;
        if (l < recLen)
        {
            channelType = cpu.getU2(is);
            l += Data_t.U2.numBytes;
        }
        else channelType = null;
        if (l < recLen)
        {
            channelName = cpu.getCN(is);
            l += 1 + channelName.length();
        }
        else channelName = null;
        if (l < recLen)
        {
            if (tdb.isSmartTest8())
            {
                tmpLog = cpu.getCN(is);
                l += 1 + tmpLog.length();
            }
            else
            {
                tmpPhys = cpu.getCN(is);
                l += 1 + tmpPhys.length();
            }
        }
        else 
        {
            if (tdb.isSmartTest8()) tmpLog = null;
            else tmpPhys = null;
        }
        if (l < recLen)
        {
            if (tdb.isSmartTest8())
            {
                tmpPhys = cpu.getCN(is);
                l += 1 + tmpPhys.length();
            }
            else
            {
                tmpLog = cpu.getCN(is);
                l += 1 + tmpLog.length();
            }
        }
        else 
        {
            if (tdb.isSmartTest8()) tmpPhys = null;
            else tmpLog = null;
        }
        physicalPinName = tmpPhys;
        logicalPinName = tmpLog;
        if (l < recLen)
        {
            headNumber = cpu.getU1(is);
            l++;
        }
        else headNumber = null;
        if (l < recLen)
        {
            siteNumber = cpu.getU1(is);
            l++;
        }
        else siteNumber = 1;
        if (l != recLen) throw new RuntimeException("Record length error in PinMapRecord.");
    }
    
    /**
     * 
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param cpu    The cpu type.
     * @param pmrIdx The PMR_INDX field.
     * @param channelType The CHAN_TYP field.
     * @param channelName The CHAN_NAM field.
     * @param physicalPinName The PHY_PIN field.
     * @param logicalPinName  The LOG_PIN field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     * @throws StdfException 
     * @throws IOException 
     */
    public PinMapRecord(
    	Cpu_t cpu,
        int pmrIdx,
        Integer channelType,
        String channelName,
        String physicalPinName,
        String logicalPinName,
        Short headNumber,
        Short siteNumber,
        CliOptions options)
    {
    	this(cpu, null,
    		 getRecLen(channelType, channelName, physicalPinName, logicalPinName, headNumber, siteNumber),
    		 new ByteInputStream(toBytes(cpu, pmrIdx, channelType, channelName, 
    				             physicalPinName, logicalPinName, headNumber, siteNumber)), options);
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, pmrIdx, channelType, channelName, physicalPinName, logicalPinName, headNumber, siteNumber);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.PMR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	
	private static byte[] toBytes(
        Cpu_t cpu,
        int pmrIdx,
        Integer channelType,
        String channelName,
        String physicalPinName,
        String logicalPinName,
        Short headNumber,
        Short siteNumber)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU2Bytes(pmrIdx));
		if (channelType != null)
		{
			l.addAll(cpu.getU2Bytes(channelType));
			if (channelName != null)
			{
		        l.addAll(cpu.getCNBytes(channelName));
		        if (physicalPinName != null)
		        {
		            l.addAll(cpu.getCNBytes(physicalPinName));
		            if (logicalPinName != null)
		            {
		                l.addAll(cpu.getCNBytes(logicalPinName));
		                if (headNumber != null)
		                {
		                    l.addAll(cpu.getU1Bytes(headNumber));
		                    if (siteNumber != null)
		                    {
		                        l.addAll(cpu.getU1Bytes(siteNumber));
		                    }
		                }
		            }
		        }
			}
		}
		return(l.toArray());
	}

    private static int getRecLen(Integer channelType, 
    		                     String channelName, 
    		                     String physicalPinName, 
    		                     String logicalPinName, 
    		                     Short headNumber, 
    		                     Short siteNumber)
    {
    	int l = 2;
        if (channelType != null)
        {
        	l += Data_t.U2.numBytes;
        	if (channelName != null)
        	{
        		l += 1 + channelName.length();
        		if (physicalPinName != null)
        		{
        			l += 1 + physicalPinName.length();
        			if (logicalPinName != null)
        			{
        				l += 1 + logicalPinName.length();
        				if (headNumber != null)
        				{
        					l++;
        					if (siteNumber != null) l++;
        				}
        			}
        		}
        	}
        }
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
		result = prime * result + ((channelName == null) ? 0 : channelName.hashCode());
		result = prime * result + ((channelType == null) ? 0 : channelType.hashCode());
		result = prime * result + ((headNumber == null) ? 0 : headNumber.hashCode());
		result = prime * result + ((logicalPinName == null) ? 0 : logicalPinName.hashCode());
		result = prime * result + ((physicalPinName == null) ? 0 : physicalPinName.hashCode());
		result = prime * result + pmrIdx;
		result = prime * result + ((siteNumber == null) ? 0 : siteNumber.hashCode());
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
		if (!(obj instanceof PinMapRecord)) return false;
		PinMapRecord other = (PinMapRecord) obj;
		if (channelName == null)
		{
			if (other.channelName != null) return false;
		} 
		else if (!channelName.equals(other.channelName)) return false;
		if (channelType == null)
		{
			if (other.channelType != null) return false;
		} 
		else if (!channelType.equals(other.channelType)) return false;
		if (headNumber == null)
		{
			if (other.headNumber != null) return false;
		} 
		else if (!headNumber.equals(other.headNumber)) return false;
		if (logicalPinName == null)
		{
			if (other.logicalPinName != null) return false;
		} 
		else if (!logicalPinName.equals(other.logicalPinName)) return false;
		if (physicalPinName == null)
		{
			if (other.physicalPinName != null) return false;
		} 
		else if (!physicalPinName.equals(other.physicalPinName)) return false;
		if (pmrIdx != other.pmrIdx) return false;
		if (siteNumber == null)
		{
			if (other.siteNumber != null) return false;
		} 
		else if (!siteNumber.equals(other.siteNumber)) return false;
		return true;
	}


}
