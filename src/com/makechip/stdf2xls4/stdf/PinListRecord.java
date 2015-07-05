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
 *  This class holds the fields for a Pin List Record.
 *  @author eric
 */
public class PinListRecord extends StdfRecord
{
    private final int[] pinIndex;
    private final int[] mode;
    private final int[] radix;
    private final String[] pgmChar;
    private final String[] rtnChar;
    private final String[] pgmChal;
    private final String[] rtnChal;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is not used for this record.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public PinListRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PLR, dvd.getCpuType(), data);
        int k = getU2(0);
        pinIndex = new int[k];
        Arrays.setAll(pinIndex, p -> getU2(-1));
        mode = new int[k];
        Arrays.setAll(mode, p -> getU2(0));
        radix = new int[k];
        Arrays.setAll(radix, p -> getU1((short) 0));
        pgmChar = new String[k];
        Arrays.setAll(pgmChar, p -> getCn());
        rtnChar = new String[k];
        Arrays.setAll(rtnChar, p -> getCn());
        pgmChal = new String[k];
        Arrays.setAll(pgmChal, p -> getCn());
        rtnChal = new String[k];
        Arrays.setAll(rtnChal, p -> getCn());
        
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
		bytes = toBytes(cpuType, pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal);
	}
	
	private static byte[] toBytes(
		Cpu_t cpuType,
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU2Bytes(pinIndex.length));
		Arrays.stream(pinIndex).forEach(p -> l.addAll(cpuType.getU2Bytes(p)));
		Arrays.stream(mode).forEach(p -> l.addAll(cpuType.getU2Bytes(p)));
		Arrays.stream(radix).forEach(p -> l.addAll(getU1Bytes((short) p)));
		Arrays.stream(pgmChar).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(rtnChar).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(pgmChal).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(rtnChal).forEach(p -> l.addAll(getCnBytes(p)));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param tdb The TestIdDatabase is not used for this record.
     * @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
	 * @param pinIndex The GRP_INDX field.
	 * @param mode     The GRP_MODE field.
	 * @param radix    The GRP_RADX field.
	 * @param pgmChar  The PGM_CHAR field.
	 * @param rtnChar  The RTN_CHAR field.
	 * @param pgmChal  The PGM_CHAL field.
	 * @param rtnChal  The RTN_CHAL field.
	 */
	public PinListRecord(
		TestIdDatabase tdb,
		DefaultValueDatabase dvd,
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
		this(tdb, dvd, toBytes(dvd.getCpuType(), pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal));
	}


    /**
     * Get the GRP_INDX field.
     * @return A deep copy of the GRP_INDX field.
     */
    public int[] getPinIndex()
    {
        return(Arrays.copyOf(pinIndex, pinIndex.length));
    }

    /**
     * Get the GRP_MODE field.
     * @return A deep copy of the GRP_MODE field.
     */
    public int[] getMode()
    {
        return(Arrays.copyOf(mode, mode.length));
    }

    /**
     * Get the GRP_RADX field.
     * @return A deep copy of the GRP_RADX field.
     */
    public int[] getRadix()
    {
        return(Arrays.copyOf(radix, radix.length));
    }

    /**
     * Get the PGM_CHAR field.
     * @return A deep copy of the PGM_CHAR field.
     */
    public String[] getPgmChar()
    {
        return(Arrays.copyOf(pgmChar, pgmChar.length));
    }

    /**
     * Get the RTN_CHAR field.
     * @return A deep copy of the RTN_CHAR field.
     */
    public String[] getRtnChar()
    {
        return(Arrays.copyOf(rtnChar, rtnChar.length));
    }

    /**
     * Get the PGM_CHAL field.
     * @return A deep copy of the PGM_CHAL field.
     */
    public String[] getPgmChal()
    {
        return(Arrays.copyOf(pgmChal, pgmChal.length));
    }

    /**
     * Get the RTN_CHAL field.
     * @return A deep copy of the RTN_CHAL field.
     */
    public String[] getRtnChal()
    {
        return(Arrays.copyOf(rtnChal, rtnChal.length));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PinListRecord [pinIndex="); builder.append(Arrays.toString(pinIndex));
		builder.append(", mode=").append(Arrays.toString(mode));
		builder.append(", radix=").append(Arrays.toString(radix));
		builder.append(", pgmChar=").append(Arrays.toString(pgmChar));
		builder.append(", rtnChar=").append(Arrays.toString(rtnChar));
		builder.append(", pgmChal=").append(Arrays.toString(pgmChal));
		builder.append(", rtnChal=").append(Arrays.toString(rtnChal));
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
		result = prime * result + Arrays.hashCode(mode);
		result = prime * result + Arrays.hashCode(pgmChal);
		result = prime * result + Arrays.hashCode(pgmChar);
		result = prime * result + Arrays.hashCode(pinIndex);
		result = prime * result + Arrays.hashCode(radix);
		result = prime * result + Arrays.hashCode(rtnChal);
		result = prime * result + Arrays.hashCode(rtnChar);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof PinListRecord)) return false;
		PinListRecord other = (PinListRecord) obj;
		if (!Arrays.equals(mode, other.mode)) return false;
		if (!Arrays.equals(pgmChal, other.pgmChal)) return false;
		if (!Arrays.equals(pgmChar, other.pgmChar)) return false;
		if (!Arrays.equals(pinIndex, other.pinIndex)) return false;
		if (!Arrays.equals(radix, other.radix)) return false;
		if (!Arrays.equals(rtnChal, other.rtnChal)) return false;
		if (!Arrays.equals(rtnChar, other.rtnChar)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

}
