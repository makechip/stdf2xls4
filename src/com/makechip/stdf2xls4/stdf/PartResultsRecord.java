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

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields of a Part Results Record.
 *  @author eric
 */
public class PartResultsRecord extends StdfRecord
{
	private static int sn = -1;
    
	/**
	 *  This is the HEAD_NUM field.
	 */
    public final short headNumber;
    /**
     *  This is the SITE_NUM field.
     */
    public final short siteNumber;
    /**
     *  This is the PART_FLG field with bits represented by enum values.
     */
    public final Set<PartInfoFlag_t> partInfoFlags;
    /**
     *  This is the NUM_TEST field.
     */
    public final int numExecs;
    /**
     *  This is the HARD_BIN field.
     */
    public final int hwBinNumber;
    /**
     *  This is the SOFT_BIN field.
     */
    public final int swBinNumber;
    /**
     *  This is the X_COORD field.
     */
    public final short xCoord;
    /**
     *  This is the Y_COORD field.
     */
    public final short yCoord;
    /**
     *  This is the TEST_T field.
     */
    public final long testTime;
    /**
     *  This is the PART_ID field.
     */
    public final String partID;
    /**
     *  This is the PART_TXT field.
     */
    public final String partDescription;
    /**
     *  This is the PART_FIX field.
     */
    private final byte[] repair;
    
    /**
     * Tests if bit 2 of the PART_FLG field is set.
     * @return True if the PART_FLG field indicates and abnormal end of test.
     */
    public boolean abnormalEOT() { return(partInfoFlags.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST)); }
    /**
     * Tests if bit 3 of the PART_FLG field is set.
     * @return True if the part failed, false if the part passed.
     */
    public boolean failed() { return(partInfoFlags.contains(PartInfoFlag_t.PART_FAILED)); }
    /**
     * Tests if bit 4 of the PART_FLG field is set.
     * @return True if bit 3 is invalid, false otherwise.
     */
    public boolean noPassFailIndication() { return(partInfoFlags.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION)); }
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase is cleared whenever this record is encountered.
     *  This is so that duplicate TestIDs can be tracked, and be uniquified for each device.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
   public PartResultsRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.PRR, dvd.getCpuType(), data);
        tdb.clearIdDups();
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
        String pid = getCn();
        partID = (pid.equals("")) ? "" + sn-- : pid;
        partDescription = getCn();
        repair = getBn();
    }
    
    /**
     * 
     * This constructor is used to make a ParametricTestRecord with field values.
     *  @param tdb The TestIdDatabase is cleared whenever this record is encountered.
     *  This is so that duplicate TestIDs can be tracked, and be uniquified for each device.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
     * @param headNumber This is the HEAD_NUM field.
     * @param siteNumber This is the SITE_NUM field.
     * @param partInfoFlags This is the PART_FLG field.
     * @param numExecs This is the NUM_TEST field.
     * @param hwBinNumber This is the HARD_BIN field.
     * @param swBinNumber This is the SOFT_BIN field.
     * @param xCoord This is the X_COORD field.
     * @param yCoord This is the Y_COORD field.
     * @param testTime This is the TEST_T field.
     * @param partID This is the PART_ID field.
     * @param partDescription This is the PART_TXT field.
     * @param repair This is the PART_FIX field.
     */
    public PartResultsRecord(
        TestIdDatabase tdb,
        DefaultValueDatabase dvd,
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
    	this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteNumber, partInfoFlags,
    		                   numExecs, hwBinNumber, swBinNumber, xCoord, yCoord, testTime,
    		                   partID, partDescription, repair));
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteNumber, 
	    	            (byte) partInfoFlags.stream().mapToInt(p -> p.bit).sum(),
	    	            numExecs, hwBinNumber, swBinNumber, xCoord, yCoord, testTime,
	    	            partID, partDescription, repair);
	}
	
	private static byte[] toBytes(
        Cpu_t cpuType,
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
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		l.add(partInfoFlags);
		l.addAll(cpuType.getU2Bytes(numExecs));
		l.addAll(cpuType.getU2Bytes(hwBinNumber));
		l.addAll(cpuType.getU2Bytes(swBinNumber));
		l.addAll(cpuType.getI2Bytes(xCoord));
		l.addAll(cpuType.getI2Bytes(yCoord));
		l.addAll(cpuType.getU4Bytes(testTime));
		l.addAll(getCnBytes(partID));
		l.addAll(getCnBytes(partDescription));
		l.addAll(getBnBytes(repair));
		return(l.toArray());
	}

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PartResultsRecord [headNumber=").append(headNumber);
		builder.append(", siteNumber=").append(siteNumber);
		builder.append(", partInfoFlags=").append(partInfoFlags);
		builder.append(", numExecs=").append(numExecs);
		builder.append(", hwBinNumber=").append(hwBinNumber);
		builder.append(", swBinNumber=").append(swBinNumber);
		builder.append(", xCoord=").append(xCoord);
		builder.append(", yCoord=").append(yCoord);
		builder.append(", testTime=").append(testTime);
		builder.append(", partID=").append(partID);
		builder.append(", partDescription=").append(partDescription);
		builder.append(", repair=").append(Arrays.toString(repair));
		builder.append("]");
		return builder.toString();
	}

    /**
     * Get the PART_FIX field.
     * @return A deep copy of the repair array.
     */
    public byte[] getRepair()
    {
        return Arrays.copyOf(repair, repair.length);
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + headNumber;
		result = prime * result + hwBinNumber;
		result = prime * result + numExecs;
		result = prime * result + partDescription.hashCode();
		result = prime * result + partID.hashCode();
		result = prime * result + partInfoFlags.hashCode();
		result = prime * result + Arrays.hashCode(repair);
		result = prime * result + siteNumber;
		result = prime * result + swBinNumber;
		result = prime * result + (int) (testTime ^ (testTime >>> 32));
		result = prime * result + xCoord;
		result = prime * result + yCoord;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof PartResultsRecord)) return false;
		PartResultsRecord other = (PartResultsRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (hwBinNumber != other.hwBinNumber) return false;
		if (numExecs != other.numExecs) return false;
		if (!partDescription.equals(other.partDescription)) return false;
		if (!partID.equals(other.partID)) return false;
		if (!partInfoFlags.equals(other.partInfoFlags)) return false;
		if (!Arrays.equals(repair, other.repair)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (swBinNumber != other.swBinNumber) return false;
		if (testTime != other.testTime) return false;
		if (xCoord != other.xCoord) return false;
		if (yCoord != other.yCoord) return false;
		if (!super.equals(obj)) return false;
		return true;
	}
    
}
