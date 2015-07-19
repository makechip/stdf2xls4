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
import com.makechip.stdf2xls4.stdf.enums.Data_t;
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
    
    public PinListRecord(Cpu_t cpu, int recLen, ByteInputStream is)
    {
        super();
        int k = cpu.getU2(is);
        int l = 2;
        pinIndex = new int[k];
        for (int i=0; i<k; i++) pinIndex[i] = cpu.getU2(is);
        l += Data_t.U2.numBytes * pinIndex.length;
        if (l < recLen && k > 0)
        {
            mode = new int[k];
            for (int i=0; i<k; i++) mode[i] = cpu.getU2(is);
            l += Data_t.U2.numBytes * mode.length;
        }
        else mode = null;
        if (l < recLen && k > 0)
        {
            radix = new int[k];
            for (int i=0; i<k; i++) radix[i] = cpu.getU1(is);
            l += Data_t.U1.numBytes * radix.length;
        }
        else radix = null;
        if (l < recLen && k > 0)
        {
            pgmChar = new String[k];
            for (int i=0; i<k; i++)
            {
            	pgmChar[i] = cpu.getCN(is);
            	l += 1 + pgmChar[i].length();
            }
        }
        else pgmChar = null;
        if (l < recLen && k > 0)
        {
            rtnChar = new String[k];
            for (int i=0; i<k; i++)
            {
            	rtnChar[i] = cpu.getCN(is);
            	l += 1 + rtnChar[i].length();
            }
        }
        else rtnChar = null;
        if (l < recLen && k > 0)
        {
            pgmChal = new String[k];
            for (int i=0; i<k; i++)
            {
            	pgmChal[i] = cpu.getCN(is);
            	l += 1 + pgmChal[i].length();
            }
        }
        else pgmChal = null;
        if (l < recLen && k > 0)
        {
            rtnChal = new String[k];
            for (int i=0; i<k; i++)
            {
            	rtnChal[i] = cpu.getCN(is);
            	l += 1 + rtnChal[i].length();
            }
        }
        else rtnChal = null;
        if (l != recLen) throw new RuntimeException("Record length error in PinListRecord."); 
    }
    
	private static byte[] toBytes(
		Cpu_t cpu,
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU2Bytes(pinIndex.length));
		Arrays.stream(pinIndex).forEach(p -> l.addAll(cpu.getU2Bytes(p)));
		Arrays.stream(mode).forEach(p -> l.addAll(cpu.getU2Bytes(p)));
		Arrays.stream(radix).forEach(p -> l.addAll(cpu.getU1Bytes((short) p)));
		Arrays.stream(pgmChar).forEach(p -> l.addAll(cpu.getCNBytes(p)));
		Arrays.stream(rtnChar).forEach(p -> l.addAll(cpu.getCNBytes(p)));
		Arrays.stream(pgmChal).forEach(p -> l.addAll(cpu.getCNBytes(p)));
		Arrays.stream(rtnChal).forEach(p -> l.addAll(cpu.getCNBytes(p)));
		return(l.toArray());
	}
	
	private static int getRecLen(
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
	    int l = Data_t.U2.numBytes;
	    l += Data_t.U2.numBytes * pinIndex.length;
	    if (mode != null)
	    {
	    	l += Data_t.U2.numBytes * mode.length;
	    	if (radix != null)
	    	{
	    		l += Data_t.U1.numBytes * radix.length;
	    		if (pgmChar != null)
	    		{
	    			l += pgmChar.length;
	    			for (int i=0; i<pgmChar.length; i++) l += pgmChar[i].length();
	    			if (rtnChar != null)
	    			{
	    			    l += rtnChar.length;
	    			    for (int i=0; i<rtnChar.length; i++) l += rtnChar[i].length();
	    			    if (pgmChal != null)
	    			    {
	    			        l += pgmChal.length;
	    			        for (int i=0; i<pgmChal.length; i++) l += pgmChal[i].length();
	    			        if (rtnChal != null)
	    			        {
	    			            l += rtnChal.length;
	    			            for (int i=0; i<rtnChal.length; i++) l += rtnChal[i].length();
	    			        }	
	    			    }
	    			}
	    		}
	    	}
	    }
	    return(l);
	}
	
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.PLR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	/**
     * This constructor is used to make a PinGroupRecord with field values. 
     * @param cpu      The cpu type.
	 * @param pinIndex The GRP_INDX field.
	 * @param mode     The GRP_MODE field.
	 * @param radix    The GRP_RADX field.
	 * @param pgmChar  The PGM_CHAR field.
	 * @param rtnChar  The RTN_CHAR field.
	 * @param pgmChal  The PGM_CHAL field.
	 * @param rtnChal  The RTN_CHAL field.
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public PinListRecord(
		Cpu_t cpu,
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
		this(cpu, getRecLen(pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal),
			new ByteInputStream(toBytes(cpu, pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal)));
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		if (obj == null) return false;
		if (!(obj instanceof PinListRecord)) return false;
		PinListRecord other = (PinListRecord) obj;
		if (!Arrays.equals(mode, other.mode)) return false;
		if (!Arrays.equals(pgmChal, other.pgmChal)) return false;
		if (!Arrays.equals(pgmChar, other.pgmChar)) return false;
		if (!Arrays.equals(pinIndex, other.pinIndex)) return false;
		if (!Arrays.equals(radix, other.radix)) return false;
		if (!Arrays.equals(rtnChal, other.rtnChal)) return false;
		if (!Arrays.equals(rtnChar, other.rtnChar)) return false;
		return true;
	}


}
