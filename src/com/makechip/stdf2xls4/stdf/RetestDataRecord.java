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
 *  This class holds the fields for a Retest Data Record.
 *  @author eric
 */
public class RetestDataRecord extends StdfRecord
{
    public final IntList retestBins;
    
    /**
     * 
     * @param cpu
     * @param recLen
     * @param is
     * @throws IOException
     * @throws StdfException
     */
    public RetestDataRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.RDR);
        int k = cpu.getU2(is);
        if (k > 0) retestBins = new IntList(Data_t.U2, cpu, k, is);
        else retestBins = null;
    }
    
    /**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @ param cpu The cpu type.
     * @param retestBins The RTST_BIN field.  The NUM_BINS field is just the length of this array.
     * @throws StdfException 
     * @throws IOException 
     */
    public RetestDataRecord(Cpu_t cpu, int[] retestBins, CliOptions options)
    {
    	this(cpu, null, getRecLen(retestBins), new ByteInputStream(toBytes(cpu, retestBins)), options);
    }

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		int[] r = (retestBins == null) ? null : retestBins.getArray();
		byte[] b = toBytes(cpu, r);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.RDR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	private static int getRecLen(int[] retestBins)
	{
		if (retestBins == null) return(2);
		return(2 + Data_t.U2.numBytes * retestBins.length);
	}
	
	private static byte[] toBytes(Cpu_t cpu, int[] retestBins)	
	{
	    TByteArrayList l = new TByteArrayList();	
	    if (retestBins == null) l.addAll(cpu.getU2Bytes(0)); else l.addAll(cpu.getU2Bytes(retestBins.length));
	    Arrays.stream(retestBins).forEach(p -> l.addAll(cpu.getU2Bytes(p)));
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
		result = prime * result + ((retestBins == null) ? 0 : retestBins.hashCode());
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
		if (!(obj instanceof RetestDataRecord)) return false;
		RetestDataRecord other = (RetestDataRecord) obj;
		if (retestBins == null)
		{
			if (other.retestBins != null) return(false);
		}
		return(retestBins.equals(other.retestBins));
	}

    

}
