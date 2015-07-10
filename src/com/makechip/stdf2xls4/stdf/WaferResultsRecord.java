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

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields for a Wafer Results Record.
 *  @author eric
 */
public class WaferResultsRecord extends StdfRecord
{
    /**
     *  The HEAD_NUM field.
     */
    public final short headNumber;
    /**
     *  The SITE_GRP field.
     */
    public final short siteGroupNumber;
    /**
     *  The FINISH_T field.
     */
    public final long finishDate;
    /**
     *  The PART_CNT field.
     */
    public final long partCount;
    /**
     *  The RTST_CNT field.
     */
    public final long retestCount;
    /**
     *  The ABRT_CNT field.
     */
    public final long abortCount;
    /**
     * The GOOD_CNT field.
     */
    public final long passCount;
    /**
     *  The FUNC_CNT field.
     */
    public final long functionalCount;
    /**
     *  The WAFER_ID field.
     */
    public final String waferID;
    /**
     *  The FABWF_ID field.
     */
    public final String fabWaferID;
    /**
     *  The FRAME_ID field.
     */
    public final String waferFrameID;
    /**
     *  The MASK_ID field.
     */
    public final String waferMaskID;
    /**
     *  The USR_DESC field.
     */
    public final String userWaferDesc;
    /**
     *  The EXC_DESC field.
     */
    public final String execWaferDesc;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This parameter is not used.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type, and convert bytes to numbers.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public WaferResultsRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.WRR, dvd.getCpuType(), data);
        headNumber = getU1((short) -1);
        siteGroupNumber = getU1((short) 255);
        finishDate = getU4(-1L);
        partCount = getU4(-1L);
        retestCount = getU4(4294967295L);
        abortCount = getU4(4294967295L);
        passCount = getU4(4294967295L);
        functionalCount = getU4(4294967295L);
        waferID = getCn();
        fabWaferID = getCn();
        waferFrameID = getCn();
        waferMaskID = getCn();
        userWaferDesc = getCn();
        execWaferDesc = getCn();
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, headNumber, siteGroupNumber, finishDate, partCount, retestCount,
	    		        abortCount, passCount, functionalCount, waferID, fabWaferID,
	    		        waferFrameID, waferMaskID, userWaferDesc, execWaferDesc);	
	}
	
	private static byte[] toBytes(
		Cpu_t cpuType,
        short headNumber,
        short siteGroupNumber,
        long finishDate,
        long partCount,
        long retestCount,
        long abortCount,
        long passCount,
        long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteGroupNumber));
		l.addAll(cpuType.getU4Bytes(finishDate));
		l.addAll(cpuType.getU4Bytes(partCount));
		l.addAll(cpuType.getU4Bytes(retestCount));
		l.addAll(cpuType.getU4Bytes(abortCount));
		l.addAll(cpuType.getU4Bytes(passCount));
		l.addAll(cpuType.getU4Bytes(functionalCount));
		l.addAll(getCnBytes(waferID));
		l.addAll(getCnBytes(fabWaferID));
		l.addAll(getCnBytes(waferFrameID));
		l.addAll(getCnBytes(waferMaskID));
		l.addAll(getCnBytes(userWaferDesc));
		l.addAll(getCnBytes(execWaferDesc));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param tdb The TestIdDatabase is not used in this class.
     * @param dvd The DefaultValueDatabase is used to convert numbers into bytes.
	 * @param headNumber       The HEAD_NUM field.
	 * @param siteGroupNumber  The SITE_GRP field.
	 * @param finishDate       The FINISH_T field.
	 * @param partCount        The PART_CNT field.
	 * @param retestCount      The RTST_CNT field.
	 * @param abortCount       The ABRT_CNT field.
	 * @param passCount        The GOOD_CNT field.
	 * @param functionalCount  The FUNC_CNT field.
	 * @param waferID          The WADER_ID field.
	 * @param fabWaferID       The FABWF_ID field.
	 * @param waferFrameID     The FRAME_ID field.
	 * @param waferMaskID      The MASK_ID field.
	 * @param userWaferDesc    The USR_DESC field.
	 * @param execWaferDesc    The EXC_DESC field.
	 */
	public WaferResultsRecord(
		TestIdDatabase tdb,
		DefaultValueDatabase dvd,
        short headNumber,
        short siteGroupNumber,
        long finishDate,
        long partCount,
        long retestCount,
        long abortCount,
        long passCount,
        long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
    {
		this(tdb, dvd, toBytes(dvd.getCpuType(), headNumber, siteGroupNumber, finishDate, partCount, retestCount,
	    		        abortCount, passCount, functionalCount, waferID, fabWaferID,
	    		        waferFrameID, waferMaskID, userWaferDesc, execWaferDesc));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WaferResultsRecord [headNumber=").append(headNumber);
		builder.append(", siteGroupNumber=").append(siteGroupNumber);
		builder.append(", finishDate=").append(finishDate);
		builder.append(", partCount=").append(partCount);
		builder.append(", retestCount=").append(retestCount);
		builder.append(", abortCount=").append(abortCount);
		builder.append(", passCount=").append(passCount);
		builder.append(", functionalCount=").append(functionalCount);
		builder.append(", waferID=").append(waferID);
		builder.append(", fabWaferID=").append(fabWaferID);
		builder.append(", waferFrameID=").append(waferFrameID);
		builder.append(", waferMaskID=").append(waferMaskID);
		builder.append(", userWaferDesc=").append(userWaferDesc);
		builder.append(", execWaferDesc=").append(execWaferDesc);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (abortCount ^ (abortCount >>> 32));
		result = prime * result + execWaferDesc.hashCode();
		result = prime * result + fabWaferID.hashCode();
		result = prime * result + (int) (finishDate ^ (finishDate >>> 32));
		result = prime * result + (int) (functionalCount ^ (functionalCount >>> 32));
		result = prime * result + headNumber;
		result = prime * result + (int) (partCount ^ (partCount >>> 32));
		result = prime * result + (int) (passCount ^ (passCount >>> 32));
		result = prime * result + (int) (retestCount ^ (retestCount >>> 32));
		result = prime * result + siteGroupNumber;
		result = prime * result + userWaferDesc.hashCode();
		result = prime * result + waferFrameID.hashCode();
		result = prime * result + waferID.hashCode();
		result = prime * result + waferMaskID.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof WaferResultsRecord)) return false;
		WaferResultsRecord other = (WaferResultsRecord) obj;
		if (abortCount != other.abortCount) return false;
		if (!execWaferDesc.equals(other.execWaferDesc)) return false;
		if (!fabWaferID.equals(other.fabWaferID)) return false;
		if (finishDate != other.finishDate) return false;
		if (functionalCount != other.functionalCount) return false;
		if (headNumber != other.headNumber) return false;
		if (partCount != other.partCount) return false;
		if (passCount != other.passCount) return false;
		if (retestCount != other.retestCount) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		if (!userWaferDesc.equals(other.userWaferDesc)) return false;
		if (!waferFrameID.equals(other.waferFrameID)) return false;
		if (!waferID.equals(other.waferID)) return false;
		if (!waferMaskID.equals(other.waferMaskID)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}


}
