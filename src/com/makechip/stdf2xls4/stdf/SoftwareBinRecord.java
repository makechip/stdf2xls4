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
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields for a Software Bin Record.
 *  @author eric
 */
public class SoftwareBinRecord extends StdfRecord
{
    /**
     *  This is the HEAD_NUM field.
     */
    public final short headNumber;
    /**
     *  This is the SITE_NUM field.
     */
    public final short siteNumber;
    /**
     *  This is the SBIN_NUM field.
     */
    public final int swBinNumber;
    /**
     *  This is the SBIN_CNT field.
     */
    public final long count;
    /**
     *  This is the SBIN_PF field.
     */
    public final Character pf;
    /**
     *  This is the SBIN_NAM field.
     */
    public final String binName;
    
    public SoftwareBinRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.SBR);
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
        swBinNumber = cpu.getU2(is);
        count = cpu.getU4(is);
        int l = 8;
        if (l < recLen)
        {
            pf = (char) cpu.getI1(is);
            l++;
        }
        else pf = null;
        if (l < recLen)
        {
            binName = cpu.getCN(is);
            l += 1 + binName.length();
        }
        else binName = null;
        if (l != recLen) throw new RuntimeException("Record length error in SoftwareBinRecord l = " + l + " recLen = " + recLen + " binName = " + binName);
    }

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteNumber, swBinNumber, count, pf, binName);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.SBR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static byte[] toBytes(
		Cpu_t cpu, 
		short headNumber, 
		short siteNumber, 
		int swBinNumber, 
		long count, 
		Character pf, 
		String binName)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteNumber));
		l.addAll(cpu.getU2Bytes(swBinNumber));
		l.addAll(cpu.getU4Bytes(count));
		if (pf != null)
		{
			l.addAll(cpu.getI1Bytes((byte) pf.charValue()));
		    if (binName != null)
		    {
		    	l.addAll(cpu.getCNBytes(binName));
		    }
		}
		return(l.toArray());
	}

	private static int getRecLen(Character pf, String binName)
	{
		int l = 8;
		if (pf != null) l++; else return(l);
		if (binName != null) l += 1 + binName.length();
		return(l);
	}
	
	/**
	 * 
     * This constructor is used to make a SoftwareBinRecord with field values. 
     * @param cpu  The CPU type.
	 * @param headNumber The HEAD_NUM field.
	 * @param siteNumber The SITE_NUM field.
	 * @param swBinNumber The SBIN_NUM field.
	 * @param count    The SBIN_CNT field.
	 * @param pf       The SBIN_PF field.
	 * @param binName  The SBIN_NAM field.
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public SoftwareBinRecord(
			Cpu_t cpu,
			short headNumber, 
			short siteNumber, 
			int swBinNumber, 
			long count, 
			char pf, 
			String binName,
			CliOptions options)
	{
		this(cpu, null, getRecLen(pf, binName),
			 new ByteInputStream(toBytes(cpu, headNumber, siteNumber, swBinNumber, count, pf, binName)), options);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((binName == null) ? 0 : binName.hashCode());
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + headNumber;
		result = prime * result + ((pf == null) ? 0 : pf.hashCode());
		result = prime * result + siteNumber;
		result = prime * result + swBinNumber;
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
		if (!(obj instanceof SoftwareBinRecord)) return false;
		SoftwareBinRecord other = (SoftwareBinRecord) obj;
		if (binName == null)
		{
			if (other.binName != null) return false;
		} 
		else if (!binName.equals(other.binName)) return false;
		if (count != other.count) return false;
		if (headNumber != other.headNumber) return false;
		if (pf == null)
		{
			if (other.pf != null) return false;
		} 
		else if (!pf.equals(other.pf)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (swBinNumber != other.swBinNumber) return false;
		return true;
	}

    
}
