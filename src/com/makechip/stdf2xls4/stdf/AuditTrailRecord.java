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
 *  This class stores data found in an STDF AuditTrailRecord.
 *  @author eric
**/
public class AuditTrailRecord extends StdfRecord
{
	/**
	 * The date field holds the MOD_TIM value from the AuditTrailRecord.
	 */
    public final long date;
    /**
     * The cmdLine field holds the CMD_LINE value from the AuditTrailRecord.
     */
    public final String cmdLine;

    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the AuditTrailRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     * @throws StdfException 
     * @throws IOException 
     */
    public AuditTrailRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.ATR);
        date = cpu.getU4(is);
        cmdLine = cpu.getCN(is);
        if (cmdLine.indexOf("SmarTest s/w rev. 8") > 0) tdb.smartTest8 = true; else tdb.smartTest8 = false;
    }
    
    /**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
     * @param date The MOD_TIM value expressed as milliseconds from January 1, 1970, 00:00:00 GMT.
     * @param cmdLine The CMD_LINE value that holds the command line of the program that adds this record.
     *        Note: cmdLine may NOT be null.
     * @throws StdfException 
     * @throws IOException 
     */
    public AuditTrailRecord(Cpu_t cpu, final long date, final String cmdLine, CliOptions options)
    {
    	this(cpu, null, 0, new ByteInputStream(toBytes(cpu, date, cmdLine)), options);
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] a = toBytes(cpu, date, cmdLine);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.ATR, a.length);
		l.addAll(a);
		return(l.toArray());
	}
	
	private static byte[] toBytes(Cpu_t cpu, long date, String cmdLine)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU4Bytes(date));
		l.addAll(cpu.getCNBytes(cmdLine));
		return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 101;
		result = prime * result + cmdLine.hashCode();
		result = prime * result + (int) (date ^ (date >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof AuditTrailRecord)) return false;
		AuditTrailRecord other = (AuditTrailRecord) obj;
		if (!cmdLine.equals(other.cmdLine)) return false;
		if (date != other.date) return false;
		return true;
	}

}
