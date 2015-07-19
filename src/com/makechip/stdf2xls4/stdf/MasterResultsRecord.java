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

import java.util.Date;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields for a Master Results Record.
 *  @author eric
 */
public class MasterResultsRecord extends StdfRecord
{
	/**
	 *  This is the FINISH_T field of the MasterResultsRecord.
	 */
    public final long finishDate;
    /**
     *  This is the DISP_COD field of the MasterResultsRecord.
     */
    public final Character dispCode;
    /**
     * This is the USR_DESC field of the MasterResultsRecord.
     */
    public final String lotDesc;
    /**
     *  This is the EXC_DESC field of the MasterResultsRecord.
     */
    public final String execDesc;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     */
   public MasterResultsRecord(Cpu_t cpu, int recLen, ByteInputStream is)
    {
        super();
        finishDate = cpu.getU4(is);
        int l = Data_t.U4.numBytes;
        if (l < recLen) dispCode = (char) cpu.getI1(is); else dispCode = null;
        l++;
        if (l < recLen)
        {
            lotDesc = cpu.getCN(is);
            l += 1 + lotDesc.length();
        }
        else lotDesc = null;
        if (l < recLen)
        {
            execDesc = cpu.getCN(is);
            l += 1 + execDesc.length();
        }
        else execDesc = null;
        if (l != recLen) throw new RuntimeException("Record length error in MasterResultsRecord.");
    }
    
   /**
    * This constructor is used to generate binary stream data from the field values.
    * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
    * @param dvd The DefaultValueDatabase is used to access the CPU type.
    * @param finishDate The FINISH_T field.
    * @param dispCode The DISP_COD field.
    * @param lotDesc The LOT_DESC field.
    * @param execDesc The EXC_DESC field.
 * @throws StdfException 
 * @throws IOException 
    */
    public MasterResultsRecord(
    	Cpu_t cpu,
    	long finishDate, 
    	Character dispCode, 
    	String lotDesc, 
    	String execDesc)
    {
    	this(cpu, getRecLen(dispCode, lotDesc, execDesc),
    	     new ByteInputStream(toBytes(cpu, finishDate, dispCode, lotDesc, execDesc)));
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, finishDate, dispCode, lotDesc, execDesc);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.MRR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	
	private static byte[] toBytes(Cpu_t cpu, long finishDate, Character dispCode, String lotDesc, String execDesc)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU4Bytes(finishDate));
		if (dispCode != null) l.addAll(cpu.getI1Bytes((byte) dispCode.charValue())); else return(l.toArray());
		if (lotDesc != null) l.addAll(cpu.getCNBytes(lotDesc)); else return(l.toArray());
		if (execDesc != null) l.addAll(cpu.getCNBytes(execDesc));
		return(l.toArray());
	}

	private static int getRecLen(Character dispCode, String lotDesc, String execDesc)
	{
	    int l = Data_t.U4.numBytes;	
	    if (dispCode != null) l++; else return(l);
	    if (lotDesc != null) l += 1 + lotDesc.length(); else return(l);
	    if (execDesc != null) l += 1 + execDesc.length();
	    return(l);
	}
	
    /**
     * This method returns the String form of the FINISH_T date.
     * @return the finishDate
     */
    public String getFinishDate()
    {
        return(new Date(1000L * finishDate).toString());
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 19;
		result = prime * result + dispCode.hashCode();
		result = prime * result + execDesc.hashCode();
		result = prime * result + (int) (finishDate ^ (finishDate >>> 32));
		result = prime * result + lotDesc.hashCode();
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
		if (!(obj instanceof MasterResultsRecord)) return false;
		MasterResultsRecord other = (MasterResultsRecord) obj;
		if (!dispCode.equals(other.dispCode)) return false;
		if (!execDesc.equals(other.execDesc)) return false;
		if (finishDate != other.finishDate) return false;
		if (!lotDesc.equals(other.lotDesc)) return false;
		return true;
	}

}
