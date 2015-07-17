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
import com.makechip.stdf2xls4.stdfapi.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdfapi.TestIdDatabase;


/**
 *  This class holds the fields for a Wafer Configuration Record.
 *  @author eric
 */
public class WaferConfigurationRecord extends StdfRecord
{
    /**
     * The WAFR_SIZ field.
     */
    public final float waferSize;
    /**
     * The DIE_HT field.
     */
    public final float dieHeight;
    /**
     *  The DIE_WID field.
     */
    public final float dieWidth;
    /**
     * The WF_UNITS field.
     */
    public final short units;
    /**
     * The WF_FLAT field.
     */
    public final char flatOrient;
    /**
     * The CENTER_X field.
     */
    public final short centerX;
    /**
     *  The CENTER_Y field.
     */
    public final short centerY;
    /**
     *  The POS_X field.
     */
    public final char posX;
    /**
     * The POS_Y field.
     */
    public final char posY;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the TestSynopsisRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public WaferConfigurationRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.WCR, dvd.getCpuType(), data);
        waferSize = getR4(0.0f);
        dieHeight = getR4(0.0f);
        dieWidth = getR4(0.0f);
        units = getU1((short) 0);
        String c = getFixedLengthString(1);
        flatOrient = c.charAt(0);
        centerX = getI2((short) -32768);
        centerY = getI2((short) -32768);
        c = getFixedLengthString(1);
        posX = c.charAt(0);
        c = getFixedLengthString(1);
        posY = c.charAt(0);
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, waferSize, dieHeight, dieWidth, units, flatOrient, centerX, centerY, posX, posY);	
	}
	
	private static byte[] toBytes(
		Cpu_t cpuType,
        float waferSize,
        float dieHeight,
        float dieWidth,
        short units,
        char flatOrient,
        short centerX,
        short centerY,
        char posX,
        char posY)
	{
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(cpuType.getR4Bytes(waferSize));
	    l.addAll(cpuType.getR4Bytes(dieHeight));
	    l.addAll(cpuType.getR4Bytes(dieWidth));
	    l.addAll(getU1Bytes(units));
	    l.addAll(getFixedLengthStringBytes("" + flatOrient));
	    l.addAll(cpuType.getI2Bytes(centerX));
	    l.addAll(cpuType.getI2Bytes(centerY));
	    l.addAll(getFixedLengthStringBytes("" + posX));
	    l.addAll(getFixedLengthStringBytes("" + posY));
	    return(l.toArray());
	}
	
	/**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
     * @param dvd The DefaultValueDatabase is used to access the CPU type.
	 * @param waferSize The WAFR_SIZ field.
	 * @param dieHeight The DIE_HT field.
	 * @param dieWidth  The DIE_WID field.
	 * @param units     The WF_UNITS field.
	 * @param flatOrient The WF_FLAT field.
	 * @param centerX   The CENTER_X field.
	 * @param centerY   The CENTER_Y field.
	 * @param posX      The POS_X field.
	 * @param posY      The POS_Y field.
	 */
	public WaferConfigurationRecord(
		TestIdDatabase tdb,
		DefaultValueDatabase dvd,
        float waferSize,
        float dieHeight,
        float dieWidth,
        short units,
        char flatOrient,
        short centerX,
        short centerY,
        char posX,
        char posY)
    {
		this(tdb, dvd, toBytes(dvd.getCpuType(), waferSize, dieHeight, dieWidth, 
				               units, flatOrient, centerX, centerY, posX, posY));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WaferConfigurationRecord [waferSize=").append(waferSize);
		builder.append(", dieHeight=").append(dieHeight);
		builder.append(", dieWidth=").append(dieWidth);
		builder.append(", units=").append(units);
		builder.append(", flatOrient=").append(flatOrient);
		builder.append(", centerX=").append(centerX);
		builder.append(", centerY=").append(centerY);
		builder.append(", posX=").append(posX);
		builder.append(", posY=").append(posY);
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
		result = prime * result + centerX;
		result = prime * result + centerY;
		result = prime * result + Float.floatToIntBits(dieHeight);
		result = prime * result + Float.floatToIntBits(dieWidth);
		result = prime * result + flatOrient;
		result = prime * result + posX;
		result = prime * result + posY;
		result = prime * result + units;
		result = prime * result + Float.floatToIntBits(waferSize);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof WaferConfigurationRecord)) return false;
		WaferConfigurationRecord other = (WaferConfigurationRecord) obj;
		if (centerX != other.centerX) return false;
		if (centerY != other.centerY) return false;
		if (Float.floatToIntBits(dieHeight) != Float.floatToIntBits(other.dieHeight)) return false;
		if (Float.floatToIntBits(dieWidth) != Float.floatToIntBits(other.dieWidth)) return false;
		if (flatOrient != other.flatOrient) return false;
		if (posX != other.posX) return false;
		if (posY != other.posY) return false;
		if (units != other.units) return false;
		if (Float.floatToIntBits(waferSize) != Float.floatToIntBits(other.waferSize)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}


}
