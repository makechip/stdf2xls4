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

import java.util.Arrays;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: PartResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PartResultsRecord extends StdfRecord
{
	private static int sn = -1;
    
    public final short headNumber;
    public final short siteNumber;
    public final Set<PartInfoFlag_t> partInfoFlags;
    public final int numExecs;
    public final int hwBinNumber;
    public final int swBinNumber;
    public final short xCoord;
    public final short yCoord;
    public final long testTime;
    private String partID;
    public final String partDescription;
    private final byte[] repair;
    
    public boolean abnormalEOT() { return(partInfoFlags.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST)); }
    public boolean failed() { return(partInfoFlags.contains(PartInfoFlag_t.PART_FAILED)); }
    public boolean noPassFailIndication() { return(partInfoFlags.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION)); }
    
    /**
    *** @param p1
    **/
    public PartResultsRecord(int sequenceNumber, byte[] data)
    {
        super(Record_t.PRR, sequenceNumber, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        byte f = getI1((byte) 0);
        partInfoFlags = PartInfoFlag_t.getBits(f);
        numExecs = getU2(0);
        hwBinNumber = getU2(-1);
        swBinNumber = getU2(-1);
        xCoord = getI2((short) -32768);
        yCoord = getI2((short) -32768);
        testTime = getU4(-1);
        partID = getCn();
        if (partID == null)
        {
        	partID = "" + sn;
        	sn--;
        }
        partDescription = getCn();
        repair = getBn();
    }
    
    public PartResultsRecord(int sequenceNumber,
    	short headNumber,
    	short siteNumber,
    	byte partInfoFlags,
    	int numExecs,
    	int hwBinNumber,
    	int swBinNumber,
    	short xCoord,
    	short yCoord,
    	long testTime,
    	String partID,
    	String partDescription,
    	byte[] repair)
    {
    	super(Record_t.PRR, sequenceNumber, null);
    	this.headNumber = headNumber;
    	this.siteNumber = siteNumber;
    	this.partInfoFlags = PartInfoFlag_t.getBits(partInfoFlags);
    	this.numExecs = numExecs;
    	this.hwBinNumber = hwBinNumber;
    	this.swBinNumber = swBinNumber;
    	this.xCoord = xCoord;
    	this.yCoord = yCoord;
    	this.testTime = testTime;
    	this.partID = partID;
    	this.partDescription = partDescription;
    	this.repair = Arrays.copyOf(repair, repair.length);
    }

	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.add((byte) partInfoFlags.stream().mapToInt(b -> b.getBit()).sum());
		l.addAll(getU2Bytes(numExecs));
		l.addAll(getU2Bytes(hwBinNumber));
		l.addAll(getU2Bytes(swBinNumber));
		l.addAll(getI2Bytes(xCoord));
		l.addAll(getI2Bytes(yCoord));
		l.addAll(getU4Bytes(testTime));
		l.addAll(getCnBytes(partID));
		l.addAll(getCnBytes(partDescription));
		l.addAll(getBnBytes(repair));
		bytes = l.toArray();
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    head number: " + headNumber).append(Log.eol);
        sb.append("    site number: " + siteNumber).append(Log.eol);
        sb.append("    test flags:");
        partInfoFlags.stream().forEach(e -> sb.append(" ").append(e.toString()));
        sb.append(Log.eol);
        sb.append("    number of tests executed: " + numExecs).append(Log.eol);
        sb.append("    hardware bin number: " + hwBinNumber).append(Log.eol);
        sb.append("    software bin number: " + swBinNumber).append(Log.eol);
        sb.append("    wafer X-coordinate: " + xCoord).append(Log.eol);
        sb.append("    wafer Y-coordinate: " + yCoord).append(Log.eol);
        sb.append("    test time: " + testTime).append(Log.eol);
        sb.append("    part ID: "); sb.append(partID).append(Log.eol);
        sb.append("    part description: ").append(partDescription).append(Log.eol);
        if (repair != null)
        {
        	sb.append("    repair info:");
        	for (int i=0; i<repair.length; i++) sb.append(" " + repair[i]);
        }
        sb.append(Log.eol);
        return(sb.toString()); 
    }

    /**
     * @return the partID
     */
    public String getPartID()
    {
        return partID;
    }

    /**
     * @return the repair
     */
    public byte[] getRepair()
    {
        return Arrays.copyOf(repair, repair.length);
    }
}
