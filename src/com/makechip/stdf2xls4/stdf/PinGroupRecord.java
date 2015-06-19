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
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: PinGroupRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinGroupRecord extends StdfRecord
{
    public final int groupIndex;
    public final String groupName;
    private final int[] pmrIdx;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public PinGroupRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PGR, dvd.getCpuType(), data);
        groupIndex = getU2(-1);
        groupName = getCn();
        int k = getU2(0);
        pmrIdx = new int[k];
        for (int i=0; i<k; i++) pmrIdx[i] = getU2(-1);
    }
    
    public PinGroupRecord(
    	TestIdDatabase tdb, 
    	DefaultValueDatabase dvd,
    	Cpu_t cpuType, 
    	int groupIndex, 
    	String groupName, 
    	int[] pmrIdx)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), groupIndex, groupName, pmrIdx));
    }

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
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    group index: " + groupIndex).append(Log.eol);
        sb.append("    group name: ").append(groupName).append(Log.eol);
        sb.append("    pmr indicies:");
        Arrays.stream(pmrIdx).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        return(sb.toString());
    }
    
    /**
     * @return the pmrIdx
     */
    public int[] getPmrIdx()
    {
        return Arrays.copyOf(pmrIdx, pmrIdx.length);
    }

    

}
