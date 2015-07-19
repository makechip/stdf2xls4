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
import java.util.Collections;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields of a Part Results Record.
 *  @author eric
 */
public class PartResultsRecord extends StdfRecord
{
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
    public final Integer swBinNumber;
    /**
     *  This is the X_COORD field.
     */
    public final Short xCoord;
    /**
     *  This is the Y_COORD field.
     */
    public final Short yCoord;
    /**
     *  This is the TEST_T field.
     */
    public final Long testTime;
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
    public final IntList repair;
    
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
    
    public PartResultsRecord(Cpu_t cpu, int recLen, ByteInputStream is)
    {
        super();
        headNumber = cpu.getU1(is);
        siteNumber = cpu.getU1(is);
        partInfoFlags = Collections.unmodifiableSet(PartInfoFlag_t.getBits(cpu.getI1(is)));
        numExecs = cpu.getU2(is);
        hwBinNumber = cpu.getU2(is);
        int l = 7;
        if (l < recLen)
        {
            swBinNumber = cpu.getU2(is);
            l += Data_t.U2.numBytes;
        }
        else swBinNumber = null;
        if (l < recLen)
        {
            xCoord = cpu.getI2(is);
            l += Data_t.I2.numBytes;
        }
        else xCoord = null;
        if (l < recLen)
        {
            yCoord = cpu.getI2(is);
            l += Data_t.I2.numBytes;
        }
        else yCoord = null;
        if (l < recLen)
        {
            testTime = cpu.getU4(is);
            l += Data_t.U4.numBytes;
        }
        else testTime = null;
        if (l < recLen)
        {
            partID = cpu.getCN(is);
            l += 1 + partID.length();
        }
        else partID = null;
        if (l < recLen)
        {
            partDescription = cpu.getCN(is);
            l += 1 + partDescription.length();
        }
        else partDescription = null;
        if (l < recLen)
        {
            repair = new IntList(cpu.getBN(is));
            l += 1 + repair.size();
        }
        else repair = null;
        if (l != recLen) throw new RuntimeException("Error in record length for PartResultsRecord.");
    }
    
    /**
     * 
     * This constructor is used to make a ParametricTestRecord with field values.
     * @param cpu The cpu type.
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
     * @throws StdfException 
     * @throws IOException 
     */
    public PartResultsRecord(
        Cpu_t cpu,
    	short headNumber,
    	short siteNumber,
    	byte partInfoFlags,
    	int numExecs,
    	int hwBinNumber,
    	Integer swBinNumber,
    	Short xCoord,
    	Short yCoord,
    	Long testTime,
    	String partID,
    	String partDescription,
    	int[] repair)
    {
    	this(cpu, 
    		 getRecLen(swBinNumber, xCoord, yCoord, testTime, partID, partDescription, repair),
    		 new ByteInputStream(toBytes(cpu, headNumber, siteNumber, partInfoFlags, numExecs, 
    		     hwBinNumber, swBinNumber, xCoord, yCoord, testTime, partID, partDescription, repair)));
    }

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte pif = (byte) partInfoFlags.stream().mapToInt(p -> p.bit).sum();
		int[] r = (repair == null) ? null : repair.getArray();
		byte[] b = toBytes(cpu, headNumber, siteNumber, pif, numExecs, hwBinNumber, 
				           swBinNumber, xCoord, yCoord, testTime, partID, partDescription, r);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.PRR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
   
	private static int getRecLen(Integer swBinNumber, Short xCoord, Short yCoord, Long testTime, 
			                     String partID, String partDescription, int[] repair)
	{
		int l = 7;
		if (swBinNumber != null) l += Data_t.U2.numBytes; else return(l);
		if (xCoord != null) l += Data_t.I2.numBytes; else return(l);
		if (yCoord != null) l += Data_t.I2.numBytes; else return(l);
		if (testTime != null) l += Data_t.U4.numBytes; else return(l);
		if (partID != null) l += 1 + partID.length(); else return(l);
		if (partDescription != null) l += 1 + partDescription.length(); else return(l);
		if (repair != null) l += 1 + repair.length;
		return(l);
	}
	
	private static byte[] toBytes(
        Cpu_t cpu,
    	short headNumber,
    	short siteNumber,
    	byte partInfoFlags,
    	int numExecs,
    	int hwBinNumber,
    	Integer swBinNumber,
    	Short xCoord,
    	Short yCoord,
    	Long testTime,
    	String partID,
    	String partDescription,
    	int[] repair)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU1Bytes(headNumber));
		l.addAll(cpu.getU1Bytes(siteNumber));
		l.add(partInfoFlags);
		l.addAll(cpu.getU2Bytes(numExecs));
		l.addAll(cpu.getU2Bytes(hwBinNumber));
		if (swBinNumber != null)
		{
		  l.addAll(cpu.getU2Bytes(swBinNumber));
		  if (xCoord != null)
		  {
		    l.addAll(cpu.getI2Bytes(xCoord));
		    if (yCoord != null)
		    {
		      l.addAll(cpu.getI2Bytes(yCoord));
		      if (testTime != null)
		      {
		        l.addAll(cpu.getU4Bytes(testTime));
		        if (partID != null)
		        {
		          l.addAll(cpu.getCNBytes(partID));
		          if (partDescription != null)
		          {
		            l.addAll(cpu.getCNBytes(partDescription));
		            if (repair != null)
		            {
		              l.addAll(cpu.getBNBytes(repair));
		            }
		          }
		        }
		      }
		    }
		  }
		}	
		return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 31;
		result = prime * result + headNumber;
		result = prime * result + hwBinNumber;
		result = prime * result + numExecs;
		result = prime * result + partDescription.hashCode();
		result = prime * result + partID.hashCode();
		result = prime * result + partInfoFlags.hashCode();
		result = prime * result + ((repair == null) ? 0 : repair.hashCode());
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
		if (obj == null) return false;
		if (!(obj instanceof PartResultsRecord)) return false;
		PartResultsRecord other = (PartResultsRecord) obj;
		if (headNumber != other.headNumber) return false;
		if (hwBinNumber != other.hwBinNumber) return false;
		if (numExecs != other.numExecs) return false;
		if (!partDescription.equals(other.partDescription)) return false;
		if (!partID.equals(other.partID)) return false;
		if (!partInfoFlags.equals(other.partInfoFlags)) return false;
		if (repair == null)
		{
			if (other.repair != null) return(false);
		}
		else if (!repair.equals(other.repair)) return false;
		if (siteNumber != other.siteNumber) return false;
		if (swBinNumber != other.swBinNumber) return false;
		if (testTime != other.testTime) return false;
		if (xCoord != other.xCoord) return false;
		if (yCoord != other.yCoord) return false;
		return true;
	}
}
