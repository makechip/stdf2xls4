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
import java.io.FileInputStream;
//import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
//import com.makechip.util.Log;
import com.makechip.util.Log;

/**
 *  This class reads an STDF file, and produces the list of STDF records
 *  that are containing in the file.
 *  @author eric
 */
public class StdfReader
{
    private List<StdfRecord> records;
    
    public StdfReader()
    {
        records = new ArrayList<>();
    }
    
   /**
    * Use this method to read the records in an STDF file.
    * @return This STDF reader.
    * @throws IOException
    * @throws StdfException
    */
   public StdfReader read(TestIdDatabase tdb, List<Modifier> modifiers, ByteInputStream is, CliOptions options)
   {
	   Cpu_t cpu = Cpu_t.PC; // assume this for the first record. The FAR does not need this.
	   int cnt = 0;
	   while (is.available() >= 2)
	   {
		   int recLen = cpu.getU2(is);	
		   short rtype = cpu.getU1(is);
		   short stype = cpu.getU1(is);
		   Record_t type = Record_t.getRecordType(rtype, stype);
		   StdfRecord r = type.getInstance(cpu, tdb, recLen, is, options);
		   if (type == Record_t.FAR) cpu = ((FileAttributesRecord) r).cpuType;
		   else if (type == Record_t.PRR)
		   {
		       Log.msg("Clearing DUP map");
		       tdb.clearIdDups(); 
		   }
		   for (Modifier m : modifiers)
		   {
		       if (r.type == m.record) 
		       {
		           if (r.modify(m)) cnt++;
		       }
		   }
		   records.add(r);
	   }                
	   if (cnt > 0) Log.msg("Modified " + cnt + " records");
       return(this);
    }
    
    /**
     * Use this method to get the list of STDF records that is created
     * when one of the read() methods is called.
     * @return The list of STDF records loaded by one of the read() methods.
     */
    public List<StdfRecord> getRecords() { return(records); }
    
    public static void main(String[] args)
    {
    	TestIdDatabase tdb = new TestIdDatabase();
    	if (args.length != 1)
    	{
    		Log.msg("Usage: java com.makechip.stdf2xls4.stdf.StdfReader <stdfFile>");
    		System.exit(1);
    	}
    	try (DataInputStream is = new DataInputStream(new FileInputStream(args[0])))
    	{
            StdfReader rdr = new StdfReader();
            byte[] b = new byte[is.available()];
            is.readFully(b);
            CliOptions opts = new CliOptions(new String[0]);
            rdr.read(tdb, new ArrayList<Modifier>(), new ByteInputStream(b), opts);
            rdr.getRecords().stream().forEach(r -> Log.msg(r.toString()));
    	} 
    	catch (Exception e)
		{
    		Log.msg(e.getMessage());
			e.printStackTrace();
		} 
    }

}
