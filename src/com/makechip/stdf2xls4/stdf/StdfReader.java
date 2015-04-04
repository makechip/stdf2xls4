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

import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.Cpu_t;
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.stdf.Record_t.*;

/**
*** @author eric
*** @version $Id: StdfReader.java 261 2008-10-23 00:14:14Z ericw $
**/
public class StdfReader
{
    private String filename;
    public static Cpu_t cpuType;
    private List<RecordBytes> records;
    private Tracker tracker;
    
    public StdfReader(String filename)
    {
        this.filename = filename;
        tracker = new Tracker();
    }
    
    public static void main(String[] args)
    {
    	if (args.length != 1) throw new RuntimeException("Missing filename argument");
    	StdfReader r = new StdfReader(args[0]);
    	r.read();
    	Log.msg("" + r.records.size() + " records read");
    }
    
    private RecordBytes farCheck(int len, byte[] bytes) throws StdfException
    {
       	if (len != 6) throw new StdfException("Malformed FAR record");
       	if (bytes[2] != (byte)  0) throw new StdfException("First STDF record is not a FAR");
       	if (bytes[3] != (byte) 10) throw new StdfException("First STDF record is not a FAR");
        cpuType = Cpu_t.getCpuType(bytes[4]);
        int stdfVersion = (bytes[5] & 0xFF);
        if (stdfVersion != 4) throw new StdfException("Unsupported STDF version: " + stdfVersion);
        byte[] far = new byte[2];
        far[0] = bytes[4];
        far[1] = bytes[5];
    	return(new RecordBytes(far, 0, tracker, FAR));
    }
    
    private boolean isDeviceRecord(Record_t type)
    {
    	return(type == FTR || type == MPR || type == PTR || type == PRR || type == WIR || type == DTR);
    }
    
    public void read()
    {
    	records = new ArrayList<RecordBytes>();
        try (DataInputStream rdr = new DataInputStream(new BufferedInputStream(new FileInputStream(filename), 1000000)))
        {
        	// first record should be a FAR:
        	byte[] far = new byte[6];
        	int len = rdr.read(far); 
            records.add(farCheck(len, far));
            int seqNum = 1;	
            int devNum = 1;
        	while (true)
        	{
        		byte l0;
        		byte l1;
            	try { l0 = rdr.readByte(); l1 = rdr.readByte(); } catch (EOFException e) { break; }
                int recLen = getUnsignedInt(l0, l1);	
                l0 = rdr.readByte(); // type
                l1 = rdr.readByte(); // sub-type
                Record_t type = Record_t.getRecordType(l0, l1);
                byte[] record = new byte[recLen];
                len = rdr.read(record);
                if (len != recLen) throw new RuntimeException("Error: record could not be read");
                if (isDeviceRecord(type)) records.add(new RecordBytes(record, seqNum, tracker, type, devNum));
                else records.add(new RecordBytes(record, seqNum, tracker, type));
                if (type == PRR) devNum++;
                seqNum++;
        	}                
        }
        catch (IOException e)
        {
        	Log.fatal(e);
        }
        catch (StdfException e2)
        {
        	Log.fatal(e2.getMessage());
        }
    }
    
    public Stream<RecordBytes> stream() { return(records.stream()); }

    public int getUnsignedInt(byte b0, byte b1)
    {
        int l = 0;
        switch (cpuType)
        {
            case SUN: l |= (b1 & 0xFF); l |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8);
        }
        return(l);
    }
    
}
