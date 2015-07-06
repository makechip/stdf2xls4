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
 *  This class holds the fields for a Retest Data Record.
 *  @author eric
 */
public class RetestDataRecord extends StdfRecord
{
    private final int[] retestBins;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
   public RetestDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.RDR, dvd.getCpuType(), data);
        int k = getU2(0);
        retestBins = new int[k];
        Arrays.setAll(retestBins, p -> getU2(-1));
    }
    
    /**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     * @param retestBins The RTST_BIN field.  The NUM_BINS field is just the length of this array.
     */
    public RetestDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, int[] retestBins)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), retestBins));
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, retestBins);	
	}
	
	private static byte[] toBytes(Cpu_t cpuType, int[] retestBins)	
	{
	    TByteArrayList l = new TByteArrayList();	
	    l.addAll(cpuType.getU2Bytes(retestBins.length));
	    Arrays.stream(retestBins).forEach(p -> l.addAll(cpuType.getU2Bytes(p)));
	    return(l.toArray());
	}
    
    /**
     *  This method returns the RTST_BIN field.
     * @return the retestBins A deep copy of the RTST_BIN array.
     */
    public int[] getRetestBins()
    {
        return Arrays.copyOf(retestBins, retestBins.length);
    }

	@Override
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RetestDataRecord [retestBins=").append(Arrays.toString(retestBins));
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(retestBins);
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		RetestDataRecord other = (RetestDataRecord) obj;
		if (!Arrays.equals(retestBins, other.retestBins)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

    

}
