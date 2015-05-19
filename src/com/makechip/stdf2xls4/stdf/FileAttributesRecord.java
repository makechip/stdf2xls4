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

import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: FileAttributesRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class FileAttributesRecord extends StdfRecord
{
    public final int stdfVersion;
    public final Cpu_t cpuType;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public FileAttributesRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.FAR, sequenceNumber, devNum, data);
        cpuType = Cpu_t.getCpuType(data[0]);
        stdfVersion = (data[1] & 0xFF);
        assert stdfVersion == 4;
    }
    
    public FileAttributesRecord(int sequenceNumber, int devNum, int stdfVersion, Cpu_t cpuType)
    {
    	super(Record_t.FAR, sequenceNumber, devNum, null);
    	this.stdfVersion = stdfVersion;
    	this.cpuType = cpuType;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    CPU_TYPE: ").append(cpuType.toString()).append(Log.eol);
        sb.append("    STDF_VER: " + stdfVersion).append(Log.eol);
        return(sb.toString());
    }

	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.add(cpuType.getType());
		l.add((byte) stdfVersion);
		bytes = l.toArray();
	}

}
