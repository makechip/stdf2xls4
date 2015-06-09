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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.stdf.enums.Record_t.*;

/**
*** @author eric
*** @version $Id: StdfReader.java 261 2008-10-23 00:14:14Z ericw $
**/
public class StdfReader
{
    private final String filename;
    public static Cpu_t cpuType;
    private final long timeStamp;
    private List<RecordBytes> records;
    private DefaultValueDatabase idb;
    
    public StdfReader(DefaultValueDatabase idb, String filename)
    {
        this(idb, filename, false);
    }
    
    public StdfReader(DefaultValueDatabase idb, String filename, boolean timeStampedFilePerDevice)
    {
    	this.filename = filename;
    	this.timeStamp = timeStampedFilePerDevice ? getTimeStamp(filename) : 0L;
    	this.idb = idb;
    	idb.clearIdDups();
    	idb.clearDefaults();
    }
    
    /**
     * Use this CTOR when reading an array of bytes.
     */
    public StdfReader(DefaultValueDatabase idb)
    {
    	this(idb, null, false);
    }
    
    /**
     * Use this CTOR when reading an array of bytes.
     */
    public StdfReader(DefaultValueDatabase idb, long timeStamp)
    {
    	this.filename = null;
        this.timeStamp = timeStamp;
    	this.idb = idb;
    	this.idb.clearIdDups();
    	this.idb.clearDefaults();
    }
    
    public static void main(String[] args)
    {
    	if (args.length != 1) throw new RuntimeException("Missing filename argument");
    	DefaultValueDatabase idb = new DefaultValueDatabase();
    	StdfReader r = new StdfReader(idb, args[0]);
    	r.read();
    	Log.msg("" + r.records.size() + " records read");
    }
    
    private RecordBytes FARcheck(int len, byte[] bytes) throws StdfException
    {
       	if (len != 6) throw new StdfException("Malformed FAR record");
       	if (bytes[2] != (byte)  0 || bytes[3] != (byte) 10) throw new StdfException("First STDF record is not a FAR");
        cpuType = Cpu_t.getCpuType(bytes[4]);
        int stdfVersion = (bytes[5] & 0xFF);
        if (stdfVersion != 4) throw new StdfException("Unsupported STDF version: " + stdfVersion);
        byte[] far = new byte[2];
        far[0] = bytes[4];
        far[1] = bytes[5];
    	return(new RecordBytes(far, 0, FAR, idb, 0L));
    }
    
    private long getTimeStamp(String name)
    {
    	int dotIndex = 0;
    	if (name.toLowerCase().endsWith(".std")) dotIndex = name.length() - 4;
    	else dotIndex = name.length() - 5;
    	int bIndex = dotIndex - 14;
    	String stamp = name.substring(bIndex, dotIndex);
    	long timeStamp = 0L;
    	try { timeStamp = Long.parseLong(stamp); }
    	catch (Exception e) { Log.fatal("Incorrect timestamp format in filename: " + name); }
    	return(timeStamp);
    }
    
   public StdfReader read()
    {
    	records = new ArrayList<RecordBytes>();
        try (DataInputStream rdr = new DataInputStream(new BufferedInputStream(new FileInputStream(filename), 1000000)))
        {
        	byte[] far = new byte[6];
        	int len = rdr.read(far); 
            records.add(FARcheck(len, far));
            int seqNum = 1;	
        	while (true)
        	{
        		if (rdr.available() < 2) break;
            	byte l0 = rdr.readByte(); 
                int recLen = getUnsignedInt(l0, rdr.readByte());	
                l0 = rdr.readByte(); // type
                Record_t type = Record_t.getRecordType(l0, rdr.readByte());
                if (type == null) Log.msg("unkown type at record number = " + seqNum);
                byte[] record = new byte[recLen];
                len = rdr.read(record);
                if (len != recLen) throw new RuntimeException("Error: record could not be read");
                records.add(new RecordBytes(record, seqNum, type, idb, timeStamp));
    	    	if (type == PRR) idb.clearIdDups();
                seqNum++;
        	}                
        }
        catch (IOException e) { Log.fatal(e); }
        catch (StdfException e2) { Log.fatal(e2.getMessage()); }
        return(this);
    }
    
    public void read(byte[] bytes)
    {
    	records = new ArrayList<RecordBytes>();
    	try
    	{
    	    byte[] far = new byte[6];
    	    int ptr = 0;
    	    for (int i=0; i<6; i++) 
    	    {
    	    	far[i] = bytes[i];
    	    	ptr++;
    	    }
    	    records.add(FARcheck(6, far));
    	    int seqNum = 1;
    	    while (true)
    	    {
    	    	if (ptr > bytes.length - 2) break;
    	    	byte l0 = bytes[ptr++];
    	    	int recLen = getUnsignedInt(l0, bytes[ptr++]);
    	    	l0 = bytes[ptr++]; // type
    	    	Record_t type = Record_t.getRecordType(l0,  bytes[ptr++]);
                if (type == null) Log.msg("unknown type at record number = " + seqNum);
    	    	byte[] record = new byte[recLen];
    	    	if (ptr > bytes.length - recLen) throw new RuntimeException("Error: record could not be read");
    	    	for (int i=0; i<recLen; i++) record[i] = bytes[ptr++];
    	    	records.add(new RecordBytes(record, seqNum, type, idb, timeStamp));
    	    	if (type == PRR) idb.clearIdDups();
    	    	seqNum++;
    	    }
    	}
    	catch (StdfException e) { Log.fatal(e); }
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
