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

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields for a Pin Group Record.
 *  @author eric
 */
public class PinGroupRecord extends StdfRecord
{
	/**
	 *  This is the GPR_INDX field.
	 */
    public final int groupIndex;
	/**
	 *  This is the GRP_NAM field.
	 */
    public final String groupName;
    private final int[] pmrIdx;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public PinGroupRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PGR, dvd.getCpuType(), data);
        groupIndex = getU2(-1);
        groupName = getCn();
        int k = getU2(0);
        pmrIdx = new int[k];
        for (int i=0; i<k; i++) pmrIdx[i] = getU2(-1);
    }
    
    /**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     * @param groupIndex This is the GRP_INDX field.
     * @param groupName This is the GRP_NAM field.
     * @param pmrIdx This is the PMR_INDX field.
     */
    public PinGroupRecord(
    	TestIdDatabase tdb, 
    	DefaultValueDatabase dvd,
    	int groupIndex, 
    	String groupName, 
    	int[] pmrIdx)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), groupIndex, groupName, pmrIdx));
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, groupIndex, groupName, pmrIdx);	
	}
	
	private static byte[] toBytes(Cpu_t cpuType, int groupIndex, String groupName, int[] pmrIdx)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU2Bytes(groupIndex));
		l.addAll(getCnBytes(groupName));
		l.addAll(cpuType.getU2Bytes(pmrIdx.length));
		Arrays.stream(pmrIdx).forEach(p -> l.addAll(cpuType.getU2Bytes(p)));
		return(l.toArray());
	}
    
    /**
     * This method provides access to the PMR_INDX field. The INDX_CNT field
     * is just the length of this array.
     * @return the pmrIdx A deep copy of the PMR_INDX array.
     */
    public int[] getPmrIdx()
    {
        return Arrays.copyOf(pmrIdx, pmrIdx.length);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PinGroupRecord [groupIndex="); builder.append(groupIndex);
		builder.append(", ").append("groupName=").append(groupName);
		builder.append(", ").append("pmrIdx=").append(Arrays.toString(pmrIdx));
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + groupIndex;
		result = prime * result + groupName.hashCode();
		result = prime * result + Arrays.hashCode(pmrIdx);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof PinGroupRecord)) return false;
		PinGroupRecord other = (PinGroupRecord) obj;
		if (groupIndex != other.groupIndex) return false;
		if (!groupName.equals(other.groupName)) return false;
		if (!Arrays.equals(pmrIdx, other.pmrIdx)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

    

}
