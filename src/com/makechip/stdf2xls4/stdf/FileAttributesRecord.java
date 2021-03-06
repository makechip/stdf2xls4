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
 *  This class holds the fields for an STDF FileAttributes record.
 *  @author eric
 */
public class FileAttributesRecord extends StdfRecord
{
	/**
	 * This field holds the STDF_VER value from a FileAttributesRecord.
	 */
    public final short stdfVersion;
    /**
     * This field holds the CPU_TYPE value from a FileAttributesRecord.
     */
    public final Cpu_t cpuType;
    
    /**
     * CTOR for a FileAttributesRecord.
     * @param cpu  The CPU type.  For this record only, cpu may be null.
     * @param recLen
     * @param is
     * @throws IOException
     * @throws StdfException
     */
    public FileAttributesRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.FAR);
        short c = (short) (0xFF & is.readByte());
        cpuType = Cpu_t.getCpuType((byte) c);
        stdfVersion = (short) (0xFF & is.readByte());
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param stdfVersion This value should always be 4.
     * @param cpuType The CPU type.
     * @throws StdfException 
     * @throws IOException 
     */
    public FileAttributesRecord(Cpu_t cpu, int stdfVersion, CliOptions options)
    {
    	this(cpu, null, 0, new ByteInputStream(toBytes(cpu, stdfVersion)), options);
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, stdfVersion);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.FAR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	private static byte[] toBytes(Cpu_t cpu, int stdfVersion)
	{
		TByteArrayList l = new TByteArrayList();
        l.add(cpu.type);
        l.add((byte) stdfVersion);
        return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 97;
		result = prime * result + cpuType.hashCode();
		result = prime * result + stdfVersion;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof FileAttributesRecord)) return false;
		FileAttributesRecord other = (FileAttributesRecord) obj;
		if (cpuType != other.cpuType) return false;
		if (stdfVersion != other.stdfVersion) return false;
		return true;
	}

}
