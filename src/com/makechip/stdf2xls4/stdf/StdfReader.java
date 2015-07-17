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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdfapi.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdfapi.TestIdDatabase;
import com.makechip.util.Log;

/**
 *  This class reads an STDF file, and produces the list of STDF records
 *  that are containing in the file.
 *  @author eric
 */
public class StdfReader
{
    private final String filename;
    private List<StdfRecord> records;
    private final DefaultValueDatabase dvd;
    private final TestIdDatabase tdb;
    
    /**
     * This CTOR is used to read an STDF file when time-stamped files are not being used.
     * @param tdb  The TestID database. Used for tracking test IDs.
     * @param file An STDF file.
     */
    public StdfReader(TestIdDatabase tdb, File file)
    {
        this(tdb, file, false);
    }
    
    /**
     * This CTOR is used to read a time-stamped STDF file.  See the stdf2xls4 user manual
     * for information on time-stamped files.
     * @param tdb The TestID database. Used for tracking test IDs.
     * @param file An STDF file.
     * @param timeStampedFilePerDevice Indicates that file time-stamps are to be tracked.
     */
    public StdfReader(TestIdDatabase tdb, File file, boolean timeStampedFilePerDevice)
    {
    	filename = file.toString();
    	long timeStamp = timeStampedFilePerDevice ? getTimeStamp(filename) : 0L;
    	this.tdb = tdb;
    	dvd = new DefaultValueDatabase(timeStamp);
    	dvd.clearDefaults();
    	records = new ArrayList<>(100);
    }
    
    /**
     * Use this CTOR when reading an array of bytes.
     * @param tdb The TestID database. Used for tracking test IDs.
     */
    public StdfReader(TestIdDatabase tdb)
    {
    	this(tdb, null, false);
    }
    
    /**
     * Use this CTOR when reading an array of bytes, and emulating a time-stamped file.
     * @param tdb The TestID database. Used for tracking test IDs.
     * @param timeStamp The time-stamp to be used.  See the stdf2xls4 user manual for
     * valid time-stamp values.
     */
    public StdfReader(TestIdDatabase tdb, long timeStamp)
    {
    	this(tdb, new File("dummy_" + timeStamp + ".stdf"), true);
    }
    
    private StdfRecord FARcheck(int len, byte[] bytes) throws StdfException
    {
       	if (len != 6) throw new StdfException("Malformed FAR record");
       	if (bytes[2] != (byte)  0 || bytes[3] != (byte) 10) throw new StdfException("First STDF record is not a FAR");
        int stdfVersion = (bytes[5] & 0xFF);
        if (stdfVersion != 4) throw new StdfException("Unsupported STDF version: " + stdfVersion);
        byte[] far = new byte[2];
        far[0] = bytes[4];
        far[1] = bytes[5];
        return(Record_t.FAR.getInstance(tdb, dvd, far));
    }
    
    private static long getTimeStamp(String name) // throws NumberFormatException
    {
    	int dotIndex = (name.toLowerCase().endsWith(".std")) ? name.length() - 4 : name.length() - 5;
    	String stamp = name.substring(dotIndex - 14, dotIndex);
    	Log.msg("stamp = " + stamp);
    	long l = Long.parseLong(stamp);
        Log.msg("stamp = " + l);	
    	return(Long.parseLong(stamp));
    }
    
   /**
    * Use this method to read the records in an STDF file.
    * @return This STDF reader.
    * @throws IOException
    * @throws StdfException
    */
   public StdfReader read() throws IOException, StdfException
   {
        try (DataInputStream rdr = new DataInputStream(new BufferedInputStream(new FileInputStream(filename), 1000000)))
        {
        	byte[] far = new byte[6];
        	int len = rdr.read(far); 
            records.add(FARcheck(len, far));
        	while (rdr.available() >= 2)
        	{
                int recLen = dvd.getCpuType().getU2(rdr.readByte(), rdr.readByte());	
                Record_t type = Record_t.getRecordType(rdr.readByte(), rdr.readByte());
                byte[] record = new byte[recLen];
                len = rdr.read(record);
                String s = new String(record);
                if (type == Record_t.DTR)
                {
                	s = s.substring(1);
                    if (s.startsWith(StdfRecord.TEXT_DATA) && !s.contains(StdfRecord.SERIAL_MARKER)) type = Record_t.DTRX;
                }
                records.add(type.getInstance(tdb, dvd, record));
        	}                
        }
        catch (IOException e) { throw new IOException(e.getMessage()); }
        return(this);
    }
    
   /**
    * Use this method to convert an STDF byte stream into a list of STDF records.
    * @param bytes A valid STDF byte stream.
    * @throws StdfException
    */
    public void read(byte[] bytes) throws StdfException
    {
    	byte[] far = Arrays.copyOf(bytes, 6);
    	int ptr = 6;
    	records.add(FARcheck(6, far));
    	while (ptr <= (bytes.length - 2))
    	{
    		int recLen = dvd.getCpuType().getU2(bytes[ptr++], bytes[ptr++]);
    		Record_t type = Record_t.getRecordType(bytes[ptr++],  bytes[ptr++]);
    		byte[] record = Arrays.copyOfRange(bytes, ptr, ptr+recLen);
    		String s = new String(record);
    		if (type == Record_t.DTR)
    		{
    			s = s.substring(1);
    			if (s.startsWith(StdfRecord.TEXT_DATA) && !s.contains(StdfRecord.SERIAL_MARKER)) type = Record_t.DTRX;
    		}
    		records.add(type.getInstance(tdb, dvd, record));
    		ptr += recLen;
    	}
    }
    
    /**
     * Use this method to get the list of STDF records that is created
     * when one of the read() methods is called.
     * @return The list of STDF records loaded by one of the read() methods.
     */
    public List<StdfRecord> getRecords() { return(records); }

}
