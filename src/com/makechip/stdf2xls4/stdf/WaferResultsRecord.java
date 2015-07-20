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
import com.makechip.stdf2xls4.stdf.enums.Data_t;
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
    public final Long retestCount;
    /**
     *  The ABRT_CNT field.
     */
    public final Long abortCount;
    /**
     * The GOOD_CNT field.
     */
    public final Long passCount;
    /**
     *  The FUNC_CNT field.
     */
    public final Long functionalCount;
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
    
    public WaferResultsRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is)
    {
        super(Record_t.WRR);
        headNumber = cpu.getU1(is);
        siteGroupNumber = cpu.getU1(is);
        finishDate = cpu.getU4(is);
        partCount = cpu.getU4(is);
        int l = 10;
        if (l < recLen)
        {
            retestCount = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else retestCount = null;
        if (l < recLen)
        {
            abortCount = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else abortCount = null;
        if (l < recLen)
        {
            passCount = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else passCount = null;
        if (l < recLen)
        {
            functionalCount = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else functionalCount = null;
        if (l < recLen)
        {
            waferID = cpu.getCN(is);
            l += 1 + waferID.length();
        }
        else waferID = null;
        if (l < recLen)
        {
            fabWaferID = cpu.getCN(is);
            l += 1 + fabWaferID.length();
        }
        else fabWaferID = null;
        if (l < recLen)
        {
            waferFrameID = cpu.getCN(is);
            l += 1 + waferFrameID.length();
        }
        else waferFrameID = null;
        if (l < recLen)
        {
            waferMaskID = cpu.getCN(is);
            l += 1 + waferMaskID.length();
        }
        else waferMaskID = null;
        if (l < recLen)
        {
            userWaferDesc = cpu.getCN(is);
            l += 1 + userWaferDesc.length();
        }
        else userWaferDesc = null;
        if (l < recLen)
        {
            execWaferDesc = cpu.getCN(is);
            l += 1 + execWaferDesc.length();
        }
        else execWaferDesc = null;
        if (l != recLen) throw new RuntimeException("Record length error in WaferResultsRecord.");
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, headNumber, siteGroupNumber, finishDate, partCount, retestCount, abortCount, passCount, 
				           functionalCount, waferID, fabWaferID, waferFrameID, waferMaskID, userWaferDesc, execWaferDesc);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.WRR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static int getRecLen(
        Long retestCount,
        Long abortCount,
        Long passCount,
        Long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
	{
		int l = 10;
	    if (retestCount != null) l += Data_t.U4.numBytes; else return(l);
	    if (abortCount != null) l += Data_t.U4.numBytes; else return(l);
	    if (passCount != null) l += Data_t.U4.numBytes; else return(l);
	    if (functionalCount != null) l += Data_t.U4.numBytes; else return(l);
	    if (waferID != null) l += 1 + waferID.length(); else return(l);
	    if (fabWaferID != null) l += 1 + fabWaferID.length(); else return(l);
	    if (waferFrameID != null) l += 1 + waferFrameID.length(); else return(l);
	    if (waferMaskID != null) l += 1 + waferMaskID.length(); else return(l);
	    if (userWaferDesc != null) l += 1 + userWaferDesc.length(); else return(l);
	    if (execWaferDesc != null) l += 1 + execWaferDesc.length();
		return(l);
	}
	private static byte[] toBytes(
		Cpu_t cpu,
        short headNumber,
        short siteGroupNumber,
        long finishDate,
        long partCount,
        Long retestCount,
        Long abortCount,
        Long passCount,
        Long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteGroupNumber));
		l.addAll(cpu.getU4Bytes(finishDate));
		l.addAll(cpu.getU4Bytes(partCount));
		if (retestCount != null) l.addAll(cpu.getU4Bytes(retestCount)); else return(l.toArray());
		if (abortCount != null) l.addAll(cpu.getU4Bytes(abortCount));
		if (passCount != null) l.addAll(cpu.getU4Bytes(passCount));
		if (functionalCount != null) l.addAll(cpu.getU4Bytes(functionalCount));
		if (waferID != null) l.addAll(cpu.getCNBytes(waferID));
		if (fabWaferID != null) l.addAll(cpu.getCNBytes(fabWaferID));
		if (waferFrameID != null) l.addAll(cpu.getCNBytes(waferFrameID));
		if (waferMaskID != null) l.addAll(cpu.getCNBytes(waferMaskID));
		if (userWaferDesc != null) l.addAll(cpu.getCNBytes(userWaferDesc));
		if (execWaferDesc != null) l.addAll(cpu.getCNBytes(execWaferDesc));
		return(l.toArray());
	}
	
	/**
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param cpu              The CPU type.
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
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public WaferResultsRecord(
		Cpu_t cpu,
        short headNumber,
        short siteGroupNumber,
        long finishDate,
        long partCount,
        Long retestCount,
        Long abortCount,
        Long passCount,
        Long functionalCount,
        String waferID,
        String fabWaferID,
        String waferFrameID,
        String waferMaskID,
        String userWaferDesc,
        String execWaferDesc)
    {
		this(cpu, null,
			 getRecLen(retestCount, abortCount, passCount, functionalCount, waferID, 
					   fabWaferID, waferFrameID, waferMaskID, userWaferDesc, execWaferDesc),
			 new ByteInputStream(toBytes(cpu, headNumber, siteGroupNumber, 
					 finishDate, partCount, retestCount, abortCount, passCount, functionalCount, 
					 waferID, fabWaferID, waferFrameID, waferMaskID, userWaferDesc, execWaferDesc)));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abortCount == null) ? 0 : abortCount.hashCode());
		result = prime * result + ((execWaferDesc == null) ? 0 : execWaferDesc.hashCode());
		result = prime * result + ((fabWaferID == null) ? 0 : fabWaferID.hashCode());
		result = prime * result + (int) (finishDate ^ (finishDate >>> 32));
		result = prime * result + ((functionalCount == null) ? 0 : functionalCount.hashCode());
		result = prime * result + headNumber;
		result = prime * result + (int) (partCount ^ (partCount >>> 32));
		result = prime * result + ((passCount == null) ? 0 : passCount.hashCode());
		result = prime * result + ((retestCount == null) ? 0 : retestCount.hashCode());
		result = prime * result + siteGroupNumber;
		result = prime * result + ((userWaferDesc == null) ? 0 : userWaferDesc.hashCode());
		result = prime * result + ((waferFrameID == null) ? 0 : waferFrameID.hashCode());
		result = prime * result + ((waferID == null) ? 0 : waferID.hashCode());
		result = prime * result + ((waferMaskID == null) ? 0 : waferMaskID.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof WaferResultsRecord)) return false;
		WaferResultsRecord other = (WaferResultsRecord) obj;
		if (abortCount == null)
		{
			if (other.abortCount != null) return false;
		} 
		else if (!abortCount.equals(other.abortCount)) return false;
		if (execWaferDesc == null)
		{
			if (other.execWaferDesc != null) return false;
		} 
		else if (!execWaferDesc.equals(other.execWaferDesc)) return false;
		if (fabWaferID == null)
		{
			if (other.fabWaferID != null) return false;
		} 
		else if (!fabWaferID.equals(other.fabWaferID)) return false;
		if (finishDate != other.finishDate) return false;
		if (functionalCount == null)
		{
			if (other.functionalCount != null) return false;
		} 
		else if (!functionalCount.equals(other.functionalCount)) return false;
		if (headNumber != other.headNumber) return false;
		if (partCount != other.partCount) return false;
		if (passCount == null)
		{
			if (other.passCount != null) return false;
		} 
		else if (!passCount.equals(other.passCount)) return false;
		if (retestCount == null)
		{
			if (other.retestCount != null) return false;
		} 
		else if (!retestCount.equals(other.retestCount)) return false;
		if (siteGroupNumber != other.siteGroupNumber) return false;
		if (userWaferDesc == null)
		{
			if (other.userWaferDesc != null) return false;
		} 
		else if (!userWaferDesc.equals(other.userWaferDesc)) return false;
		if (waferFrameID == null)
		{
			if (other.waferFrameID != null) return false;
		} 
		else if (!waferFrameID.equals(other.waferFrameID)) return false;
		if (waferID == null)
		{
			if (other.waferID != null) return false;
		} 
		else if (!waferID.equals(other.waferID)) return false;
		if (waferMaskID == null)
		{
			if (other.waferMaskID != null) return false;
		} 
		else if (!waferMaskID.equals(other.waferMaskID)) return false;
		return true;
	}

}
