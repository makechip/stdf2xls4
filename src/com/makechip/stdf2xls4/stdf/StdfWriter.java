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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;

/**
 *  This class is used to generate STDF files.
 *  @author eric
 */
public class StdfWriter
{
    private FileAttributesRecord far;
    private List<AuditTrailRecord> atrs;
    private MasterInformationRecord mir;
    private RetestDataRecord rdr;
    private List<SiteDescriptionRecord> sdrs;
    private List<StdfRecord> records;
    private final Cpu_t cpu;
    
    /**
     * Constructor for an StdfWriter. 
     * @param filename Name of output file.
     * @param far FileAttributesRecord (required).
     * @param atrs List of one or more AuditTrailRecords (may be null).
     * @param mir MasterInformationRecord (required).
     * @param rdr RetestDataRecord (may be null).
     * @param sdrs List of SiteDescriptionRecords (may be null).
     */
    public StdfWriter(
    	Cpu_t cpu,
    	FileAttributesRecord far,
    	List<AuditTrailRecord> atrs,
    	MasterInformationRecord mir,
    	RetestDataRecord rdr,
    	List<SiteDescriptionRecord> sdrs)
    {
    	this.cpu = cpu;
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
    
    /**
     * Add a record to the list of records to be written to the file.
     * The records are written out in the order that they are added.
     * @param record An STDF record.
     */
    public void add(StdfRecord record)
    {
        records.add(record);	
    }
    
    /**
     * Get the bytes that this Writer will write out.
     * @return The set of bytes that this writer will write to a file.
     */
    public byte[] getBytes()
    {
    	TByteArrayList l = new TByteArrayList();
   		records.stream().map(p -> p.getBytes(cpu)).forEach(p -> l.addAll(p));
        return(l.toArray());	
    }
    
    /**
     * Write out the records contained in this Writer.
     * @param filename The name of the file where the records will be written.
     * @throws IOException
     */
    public void write(DataOutputStream os) throws IOException
    {
    	for (StdfRecord r : records) 
    	{ 
    		byte[] b = r.getBytes(cpu);
    		os.write(b);
    	}
    	os.close();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("StdfWriter [far="); builder.append(far);
		if (atrs != null) builder.append(", atrs=").append(atrs);
		builder.append(", mir=").append(mir);
		if (rdr != null) builder.append(", rdr=").append(rdr);
		if (sdrs != null) builder.append(", sdrs=").append(sdrs);
		builder.append(", records=").append(records);
		builder.append("]");
		return builder.toString();
	}
    
}
