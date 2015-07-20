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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields for a Pin List Record.
 *  @author eric
 */
public class PinListRecord extends StdfRecord
{
    public final IntList pinIndex;
    public final IntList mode;
    public final IntList radix;
    public final List<String> pgmChar;
    public final List<String> rtnChar;
    public final List<String> pgmChal;
    public final List<String> rtnChal;
    
    public PinListRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super(Record_t.PLR);
        final int k = cpu.getU2(is);
        int l = 2;
        pinIndex = new IntList(Data_t.U2, cpu, k, is);
        l += Data_t.U2.numBytes * pinIndex.size();
        if (l < recLen && k > 0)
        {
            mode = new IntList(Data_t.U2, cpu, k, is);
            l += Data_t.U2.numBytes * mode.size();
        }
        else mode = null;
        if (l < recLen && k > 0)
        {
            radix = new IntList(Data_t.U1, cpu, k, is);
            l += Data_t.U1.numBytes * radix.size();
        }
        else radix = null;
        if (l < recLen && k > 0)
        {
            List<String> p = new ArrayList<>(k);
            IntStream.range(0, k).forEach(i -> p.add(cpu.getCN(is)));
            l += p.size() + p.stream().mapToInt(s -> s.length()).sum();
            pgmChar = Collections.unmodifiableList(p);
        }
        else pgmChar = null;
        if (l < recLen && k > 0)
        {
            List<String> p = new ArrayList<>(k);
            IntStream.range(0, k).forEach(i -> p.add(cpu.getCN(is)));
            l += p.size() + p.stream().mapToInt(s -> s.length()).sum();
            rtnChar = Collections.unmodifiableList(p);
        }
        else rtnChar = null;
        if (l < recLen && k > 0)
        {
            List<String> p = new ArrayList<>(k);
            IntStream.range(0, k).forEach(i -> p.add(cpu.getCN(is)));
            l += p.size() + p.stream().mapToInt(s -> s.length()).sum();
            pgmChal = Collections.unmodifiableList(p);
        }
        else pgmChal = null;
        if (l < recLen && k > 0)
        {
            List<String> p = new ArrayList<>(k);
            IntStream.range(0, k).forEach(i -> p.add(cpu.getCN(is)));
            l += p.size() + p.stream().mapToInt(s -> s.length()).sum();
            rtnChal = Collections.unmodifiableList(p);
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
		int[] p = (pinIndex == null) ? null : pinIndex.getArray();
		int[] m = (mode == null) ? null : mode.getArray();
		int[] r = (radix == null) ? null : radix.getArray();
		String[] pr = (pgmChar == null) ? null : pgmChar.toArray(new String[pgmChar.size()]);
		String[] rr = (rtnChar == null) ? null : rtnChar.toArray(new String[rtnChar.size()]);
		String[] pl = (pgmChal == null) ? null : pgmChal.toArray(new String[pgmChal.size()]);
		String[] rl = (rtnChal == null) ? null : rtnChal.toArray(new String[rtnChal.size()]);
		byte[] b = toBytes(cpu, p, m, r, pr, rr, pl, rl);
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
		this(cpu, null, getRecLen(pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal),
			new ByteInputStream(toBytes(cpu, pinIndex, mode, radix, pgmChar, rtnChar, pgmChal, rtnChal)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((pgmChal == null) ? 0 : pgmChal.hashCode());
		result = prime * result + ((pgmChar == null) ? 0 : pgmChar.hashCode());
		result = prime * result + ((pinIndex == null) ? 0 : pinIndex.hashCode());
		result = prime * result + ((radix == null) ? 0 : radix.hashCode());
		result = prime * result + ((rtnChal == null) ? 0 : rtnChal.hashCode());
		result = prime * result + ((rtnChar == null) ? 0 : rtnChar.hashCode());
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
		if (mode == null)
		{
			if (other.mode != null) return false;
		} 
		else if (!mode.equals(other.mode)) return false;
		if (pgmChal == null)
		{
			if (other.pgmChal != null) return false;
		} 
		else if (!pgmChal.equals(other.pgmChal)) return false;
		if (pgmChar == null)
		{
			if (other.pgmChar != null) return false;
		} 
		else if (!pgmChar.equals(other.pgmChar)) return false;
		if (!pinIndex.equals(other.pinIndex)) return false;
		if (radix == null)
		{
			if (other.radix != null) return false;
		} 
		else if (!radix.equals(other.radix)) return false;
		if (rtnChal == null)
		{
			if (other.rtnChal != null) return false;
		} 
		else if (!rtnChal.equals(other.rtnChal)) return false;
		if (rtnChar == null)
		{
			if (other.rtnChar != null) return false;
		} 
		else if (!rtnChar.equals(other.rtnChar)) return false;
		return true;
	}



}
