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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: StdfReader.java 261 2008-10-23 00:14:14Z ericw $
**/
public class StdfWriter
{
    protected FileAttributesRecord far;
    protected List<AuditTrailRecord> atrs;
    protected MasterInformationRecord mir;
    protected RetestDataRecord rdr;
    protected List<SiteDescriptionRecord> sdrs;
    protected List<StdfRecord> records;
    
    /**
     * 
     * @param filename Name of output file.
     * @param far FileAttributesRecord (required).
     * @param atrs List of one or more AuditTrailRecords (may be null).
     * @param mir MasterInformationRecord (required).
     * @param rdr RetestDataRecord (may be null).
     * @param sdrs List of SiteDescriptionRecords (may be null).
     */
    public StdfWriter(
    	FileAttributesRecord far,
    	List<AuditTrailRecord> atrs,
    	MasterInformationRecord mir,
    	RetestDataRecord rdr,
    	List<SiteDescriptionRecord> sdrs)
    {
        this.far = far;
        this.atrs = atrs;
        this.mir = mir;
        this.rdr = rdr;
        this.sdrs = sdrs;
        records = new ArrayList<StdfRecord>();
        records.add(far);
        if (atrs != null) atrs.stream().forEach(p -> records.add(p));
        records.add(mir);
        if (rdr != null) records.add(rdr);
        if (sdrs != null) sdrs.stream().forEach(p -> records.add(p));
    }
    
    public void add(StdfRecord record)
    {
        records.add(record);	
    }
    
    public byte[] getBytes()
    {
    	TByteArrayList l = new TByteArrayList();
   		records.stream().map(p -> p.getBytes()).forEach(p -> l.addAll(p));
        return(l.toArray());	
    }
    
    public void write(File f)
    {
    	write(f.getAbsolutePath());
    }
    
    public void write(String filename)
    {
    	Log.msg("records.size() = " + records.size());
    	int cnt = 0;
    	try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename)))
    	{
    		for (StdfRecord r : records) { r.writeStdf(dos); cnt++; }
    		dos.close();
    	}
    	catch (IOException e) { Log.fatal(e); }
    	Log.msg("Records written = " + cnt);
    }
    
}
