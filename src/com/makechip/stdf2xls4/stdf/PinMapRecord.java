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
import gnu.trove.map.hash.TIntObjectHashMap;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
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
    public final int channelType;
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
    public final short headNumber;
    /**
     *  The SITE_NUM field.
     */
    public final short siteNumber;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public PinMapRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PMR, dvd.getCpuType(), data);
        pmrIdx = getU2(-1);
        channelType = getU2(-1);
        channelName = getCn();
        physicalPinName = getCn();
        logicalPinName = getCn();
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) 1);
        TIntObjectHashMap<String> m1 = dvd.chanMap.get(siteNumber);
        if (m1 == null)
        {
        	m1 = new TIntObjectHashMap<>();
        	dvd.chanMap.put(siteNumber, m1);
        	m1.put(pmrIdx, channelName);
        }
        else
        {
        	String name = m1.get(pmrIdx);
        	if (name == null) m1.put(pmrIdx, channelName);
        	//else
        	//{
        	//	if (!name.equals(channelName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + channelName);
        	//}
        }
        TIntObjectHashMap<String> m2 = dvd.physMap.get(siteNumber);
        if (m2 == null)
        {
        	m2 = new TIntObjectHashMap<>();
        	dvd.physMap.put(siteNumber, m2);
        	m2.put(siteNumber, physicalPinName);
        }
        else
        {
        	String name = m2.get(pmrIdx);
        	if (name == null) m2.put(pmrIdx, physicalPinName);
        	//else
        	//{
        	//	if (!name.equals(physicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + physicalPinName);
        	//}
        }
        TIntObjectHashMap<String> m3 = dvd.logMap.get(siteNumber);
        if (m3 == null)
        {
        	m3 = new TIntObjectHashMap<>();
        	dvd.logMap.put(siteNumber, m3);
        	m3.put(pmrIdx, logicalPinName);
        }
        else
        {
        	String name = m3.get(pmrIdx);
        	if (name == null) m3.put(pmrIdx, logicalPinName);
        	//else
        	//{
        	//	if (!name.equals(logicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + logicalPinName);
        	//}
        }
    }
    
    /**
     * 
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     * @param pmrIdx The PMR_INDX field.
     * @param channelType The CHAN_TYP field.
     * @param channelName The CHAN_NAM field.
     * @param physicalPinName The PHY_PIN field.
     * @param logicalPinName  The LOG_PIN field.
     * @param headNumber The HEAD_NUM field.
     * @param siteNumber The SITE_NUM field.
     */
    public PinMapRecord(
    	TestIdDatabase tdb,
        DefaultValueDatabase dvd,
        int pmrIdx,
        int channelType,
        String channelName,
        String physicalPinName,
        String logicalPinName,
        short headNumber,
        short siteNumber)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), pmrIdx, channelType, channelName, 
    			               physicalPinName, logicalPinName, headNumber, siteNumber));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, pmrIdx, channelType, channelName, physicalPinName, logicalPinName, headNumber, siteNumber);	
	}
	
	private static byte[] toBytes(
        Cpu_t cpuType,
        int pmrIdx,
        int channelType,
        String channelName,
        String physicalPinName,
        String logicalPinName,
        short headNumber,
        short siteNumber)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU2Bytes(pmrIdx));
		l.addAll(cpuType.getU2Bytes(channelType));
		l.addAll(getCnBytes(channelName));
		l.addAll(getCnBytes(physicalPinName));
		l.addAll(getCnBytes(logicalPinName));
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		return(l.toArray());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PinMapRecord [pmrIdx=").append(pmrIdx);
		builder.append(", channelType=").append(channelType);
		builder.append(", channelName=").append(channelName);
		builder.append(", physicalPinName=").append(physicalPinName);
		builder.append(", logicalPinName=").append(logicalPinName);
		builder.append(", headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + channelName.hashCode();
		result = prime * result + channelType;
		result = prime * result + headNumber;
		result = prime * result + logicalPinName.hashCode();
		result = prime * result + physicalPinName.hashCode();
		result = prime * result + pmrIdx;
		result = prime * result + siteNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return(false);
		if (getClass() != obj.getClass()) return false;
		PinMapRecord other = (PinMapRecord) obj;
		if (!channelName.equals(other.channelName)) return false;
		if (channelType != other.channelType) return false;
		if (headNumber != other.headNumber) return false;
		if (!logicalPinName.equals(other.logicalPinName)) return false;
		if (!physicalPinName.equals(other.physicalPinName)) return false;
		if (pmrIdx != other.pmrIdx) return false;
		if (siteNumber != other.siteNumber) return false;
		if (!super.equals(obj)) return false;
		return true;
	}


}
