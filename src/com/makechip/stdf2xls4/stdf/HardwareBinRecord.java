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
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields for a Hardware Bin Record.
 *  @author eric
 */
public class HardwareBinRecord extends StdfRecord
{
	/**
	 *  This is the HEAD_NUM field of the HardwareBinRecord.
	 */
    public final short headNumber;
    /**
     *  This is the SITE_NUM field of the HardwareBinRecord.
     */
    public final short siteNumber;
    /**
     *  This is the HBIN_NUM field of the HardwareBinRecord.
     */
    public final int hwBin;
    /**
     *  This is the HBIN_CNT field of the HardwareBinRecord.
     */
    public final long binCnt;
    /**
     * This is the HBIN_PF field of the HardwareBinRecord.
     */
    public final Character pf;
    /**
     * This is the HBIN_NAM field of the HardwareBinRecord.
     */
    public final String binName;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the HardwareBinRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public HardwareBinRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super();
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
        hwBin = cpu.getU2(is);
        binCnt = cpu.getU4(is);
        int l = 8;
        if (recLen > 8) 
        {
        	pf = (char) cpu.getI1(is); 
        	l++;
        }
        else pf = null;
        if (recLen > 9) 
        {
        	binName = cpu.getCN(is);
        	l += 1 + binName.length();
        }
        else binName = null;
        if (l != recLen) throw new RuntimeException("Record length error in HardwareBinRecord: l = " + l + " recLen = " + recLen);
    }
    
    /**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param cpu         The CPU type.
     * @param headNumber  The HEAD_NUM field.
     * @param siteNumber  The SITE_NUM field.
     * @param hwBin       The HBIN_NUM field.
     * @param binCnt      The HBIN_CNT field.
     * @param pf          The HBIN_PF field.
     * @param binName     The HBIN_NAM field.
     * @throws StdfException 
     * @throws IOException 
     */
    public HardwareBinRecord(
    	Cpu_t cpu,
    	short headNumber, 
    	short siteNumber, 
    	int hwBin, 
    	long binCnt, 
    	Character pf, 
    	String binName)
    {
    	this(cpu, null, getRecLen(pf, binName),
    		 new ByteInputStream(toBytes(cpu, headNumber, siteNumber, hwBin, binCnt, pf, binName)));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteNumber, hwBin, binCnt, pf, binName);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.HBR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	private static int getRecLen(Character pf, String binName)
	{
		int l = 8;
		if (pf != null) l++;
		if (binName != null) l += 1 + binName.length();
		return(l);
	}
	
	private static byte[] toBytes(Cpu_t cpu, short headNumber, short siteNumber, int hwBin, long binCnt, Character pf, String binName)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteNumber));
		l.addAll(cpu.getU2Bytes(hwBin));
		l.addAll(cpu.getU4Bytes(binCnt));
		if (pf != null) l.add((byte) pf.charValue()); else return(l.toArray());
		if (binName != null) l.addAll(cpu.getCNBytes(binName));
		return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 157;
		result = prime * result + (int) (binCnt ^ (binCnt >>> 32));
		result = prime * result + binName.hashCode();
		result = prime * result + headNumber;
		result = prime * result + hwBin;
		result = prime * result + pf;
		result = prime * result + siteNumber;
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
		if (!(obj instanceof HardwareBinRecord)) return false;
		HardwareBinRecord other = (HardwareBinRecord) obj;
		if (binCnt != other.binCnt) return false;
		if (!binName.equals(other.binName)) return false;
		if (headNumber != other.headNumber) return false;
		if (hwBin != other.hwBin) return false;
		if (pf != other.pf) return false;
		if (siteNumber != other.siteNumber) return false;
		return true;
	}

    
}
