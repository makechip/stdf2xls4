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

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
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
    public final IntList pmrIdx;
    
    public PinGroupRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.PGR);
        groupIndex = cpu.getU2(is);
        groupName = cpu.getCN(is);
        int l = 2 + 1 + groupName.length();
        int k = cpu.getU2(is);
        l += Data_t.U2.numBytes;
        if (l < recLen && k > 0)
        {
            pmrIdx = new IntList(Data_t.U2, cpu, k, is);
            l += Data_t.U2.numBytes * pmrIdx.size();
        }
        else pmrIdx = null;
        if (l != recLen) throw new RuntimeException("Record length error in PinGroupRecord: l = " + l + " recLen = " + recLen + ".");
    }
    
    /**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param cpu The cpu type.
     * @param groupIndex This is the GRP_INDX field.
     * @param groupName This is the GRP_NAM field.
     * @param pmrIdx This is the PMR_INDX field.
     * @throws StdfException 
     * @throws IOException 
     */
    public PinGroupRecord(
        Cpu_t cpu,
    	int groupIndex, 
    	String groupName, 
    	int[] pmrIdx,
    	CliOptions options)
    {
    	this(cpu, null, getRecLen(groupName, pmrIdx),
    		 new ByteInputStream(toBytes(cpu, groupIndex, groupName, pmrIdx)), options);
    }

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		int[] p = (pmrIdx == null) ? null : pmrIdx.getArray();
		byte[] b = toBytes(cpu, groupIndex, groupName, p);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.PGR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static int getRecLen(String groupName, int[] pmrIdx)
	{
		int l = 2;
		l += 1 + groupName.length();
		l += Data_t.U2.numBytes;
		if (pmrIdx != null) l += Data_t.U2.numBytes * pmrIdx.length;
		return(l);
	}

	private static byte[] toBytes(Cpu_t cpu, int groupIndex, String groupName, int[] pmrIdx)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU2Bytes(groupIndex));
		l.addAll(cpu.getCNBytes(groupName));
		if (pmrIdx != null) l.addAll(cpu.getU2Bytes(pmrIdx.length));
		else l.addAll(cpu.getU2Bytes(0));
		if (pmrIdx != null)
		{
		    Arrays.stream(pmrIdx).forEach(p -> l.addAll(cpu.getU2Bytes(p)));
		}
		return(l.toArray());
	}
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + groupIndex;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((pmrIdx == null) ? 0 : pmrIdx.hashCode());
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
		if (!(obj instanceof PinGroupRecord)) return false;
		PinGroupRecord other = (PinGroupRecord) obj;
		if (groupIndex != other.groupIndex) return false;
		if (!groupName.equals(other.groupName)) return false;
		if (pmrIdx == null)
		{
			if (other.pmrIdx != null) return(false);
		}
		else if (!pmrIdx.equals(other.pmrIdx)) return(false);
		return true;
	}

    

}
