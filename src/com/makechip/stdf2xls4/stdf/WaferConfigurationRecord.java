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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import gnu.trove.list.array.TByteArrayList;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 *  This class holds the fields for a Wafer Configuration Record.
 *  @author eric
 */
public class WaferConfigurationRecord extends StdfRecord
{
    /**
     * The WAFR_SIZ field.
     */
    public final Float waferSize;
    /**
     * The DIE_HT field.
     */
    public final Float dieHeight;
    /**
     *  The DIE_WID field.
     */
    public final Float dieWidth;
    /**
     * The WF_UNITS field.
     */
    public final Short units;
    /**
     * The WF_FLAT field.
     */
    public final Character flatOrient;
    /**
     * The CENTER_X field.
     */
    public final Short centerX;
    /**
     *  The CENTER_Y field.
     */
    public final Short centerY;
    /**
     *  The POS_X field.
     */
    public final Character posX;
    /**
     * The POS_Y field.
     */
    public final Character posY;
    
    public WaferConfigurationRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        int l = 0;
        if (l < recLen)
        {
            waferSize = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        } 
        else  waferSize = null;
        if (l < recLen)
        {
            dieHeight = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        } 
        else dieHeight = null;
        if (l < recLen)
        {
            dieWidth = cpu.getR4(is);
            l += Data_t.R4.numBytes;
        } 
        else dieWidth = null;
        if (l < recLen)
        {
            units = cpu.getU1(is);
            l += Data_t.U1.numBytes;
        } 
        else units = null;
        if (l < recLen)
        {
            flatOrient = (char) cpu.getI1(is);
            l += Data_t.I1.numBytes;
        } 
        else flatOrient = null;
        if (l < recLen)
        {
            centerX = cpu.getI2(is);
            l += Data_t.I2.numBytes;
        } 
        else centerX = null;
        if (l < recLen)
        {
            centerY = cpu.getI2(is);
            l += Data_t.I2.numBytes;
        } 
        else centerY = null;
        if (l < recLen)
        {
            posX = (char) cpu.getI1(is);
            l += Data_t.I1.numBytes;
        } 
        else posX = null;
        if (l < recLen)
        {
            posY = (char) cpu.getI1(is);
            l += Data_t.I1.numBytes;
        } 
        else posY = null;
    }
    
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, waferSize, dieHeight, dieWidth, units, 
				           flatOrient, centerX, centerY, posX, posY);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.WCR, b.length);
		l.addAll(b);
		return(l.toArray());
	}
	
	private static int getRecLen(
        Float waferSize,
        Float dieHeight,
        Float dieWidth,
        Short units,
        Character flatOrient,
        Short centerX,
        Short centerY,
        Character posX,
        Character posY)
	{
		int l = 0;
		if (waferSize != null) l += Data_t.R4.numBytes; else return(l);
		if (dieHeight != null) l += Data_t.R4.numBytes; else return(l);
		if (dieWidth != null) l += Data_t.R4.numBytes; else return(l);
		if (units != null) l++; else return(l);
		if (flatOrient != null) l++; else return(l);
		if (centerX != null) l += Data_t.I2.numBytes; else return(l);
		if (centerY != null) l += Data_t.I2.numBytes; else return(l);
		if (posX != null) l++; else return(l);
		if (posY != null) l++;
		return(l);
	}

	private static byte[] toBytes(
		Cpu_t cpu,
        Float waferSize,
        Float dieHeight,
        Float dieWidth,
        Short units,
        Character flatOrient,
        Short centerX,
        Short centerY,
        Character posX,
        Character posY)
	{
	    TByteArrayList l = new TByteArrayList();
	    if (waferSize != null) l.addAll(cpu.getR4Bytes(waferSize)); else return(l.toArray());
	    if (dieHeight != null) l.addAll(cpu.getR4Bytes(dieHeight)); else return(l.toArray());
	    if (dieWidth != null) l.addAll(cpu.getR4Bytes(dieWidth)); else return(l.toArray());
	    if (units != null) l.addAll(cpu.getU1Bytes(units)); else return(l.toArray());
	    if (flatOrient != null) l.addAll(cpu.getI1Bytes((byte) flatOrient.charValue())); else return(l.toArray());
	    if (centerX != null) l.addAll(cpu.getI2Bytes(centerX)); else return(l.toArray());
	    if (centerY != null) l.addAll(cpu.getI2Bytes(centerY)); else return(l.toArray());
	    if (posX != null) l.addAll(cpu.getI1Bytes((byte) posX.charValue())); else return(l.toArray());
	    if (posY != null) l.addAll(cpu.getI1Bytes((byte) posY.charValue()));
	    return(l.toArray());
	}
	
	/**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param cpu  The CPU type.
	 * @param waferSize The WAFR_SIZ field.
	 * @param dieHeight The DIE_HT field.
	 * @param dieWidth  The DIE_WID field.
	 * @param units     The WF_UNITS field.
	 * @param flatOrient The WF_FLAT field.
	 * @param centerX   The CENTER_X field.
	 * @param centerY   The CENTER_Y field.
	 * @param posX      The POS_X field.
	 * @param posY      The POS_Y field.
	 * @throws StdfException 
	 * @throws IOException 
	 */
	public WaferConfigurationRecord(
	    Cpu_t cpu,
        Float waferSize,
        Float dieHeight,
        Float dieWidth,
        Short units,
        Character flatOrient,
        Short centerX,
        Short centerY,
        Character posX,
        Character posY) throws IOException, StdfException
    {
		this(cpu, getRecLen(waferSize, dieHeight, dieWidth, units, flatOrient, centerX, centerY, posX, posY),
			 new DataInputStream(new ByteArrayInputStream(toBytes(cpu, waferSize, dieHeight, dieWidth, units, 
				                                                  flatOrient, centerX, centerY, posX, posY))));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WaferConfigurationRecord [");
		boolean first = true;
		if (waferSize != null)
		{
			builder.append("waferSize=").append(waferSize);
			first = false;
		}
		if (dieHeight != null)
		{
			if (first) { builder.append("dieHeight=").append(dieHeight); first = false; }
			else builder.append(", dieHeight=").append(dieHeight);
		}
		if (dieWidth != null)
		{
			if (first) { builder.append("dieWidth=").append(dieWidth); first = false; }
			else builder.append("dieWidth=").append(dieWidth);
		}
		if (units != null)
		{
			if (first) { builder.append("units=").append(units); first = false; }
			else builder.append("units=").append(units);
		}
		if (flatOrient != null)
		{
			if (first) { builder.append("flatOrient=").append(flatOrient); first = false; }
			else builder.append("flatOrient=").append(flatOrient);
		}
		if (centerX != null)
		{
			if (first) { builder.append("centerX=").append(centerX); first = false; }
			else builder.append("centerX=").append(centerX);
		}
		if (centerY != null)
		{
			if (first) { builder.append("centerY=").append(centerY); first = false; }
			else builder.append("centerY=").append(centerY);
		}
		if (posX != null)
		{
			if (first) { builder.append("posX=").append(posX); first = false; }
			else builder.append("posX=").append(posX);
		}
		if (posY != null)
		{
			if (first) builder.append("posY=").append(posY);
			else builder.append(", posY=").append(posY);
		}
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
		int result = 1;
		result = prime * result + ((centerX == null) ? 0 : centerX.hashCode());
		result = prime * result + ((centerY == null) ? 0 : centerY.hashCode());
		result = prime * result + ((dieHeight == null) ? 0 : dieHeight.hashCode());
		result = prime * result + ((dieWidth == null) ? 0 : dieWidth.hashCode());
		result = prime * result + ((flatOrient == null) ? 0 : flatOrient.hashCode());
		result = prime * result + ((posX == null) ? 0 : posX.hashCode());
		result = prime * result + ((posY == null) ? 0 : posY.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((waferSize == null) ? 0 : waferSize.hashCode());
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
		if (!(obj instanceof WaferConfigurationRecord)) return false;
		WaferConfigurationRecord other = (WaferConfigurationRecord) obj;
		if (centerX == null)
		{
			if (other.centerX != null) return false;
		} 
		else if (!centerX.equals(other.centerX)) return false;
		if (centerY == null)
		{
			if (other.centerY != null) return false;
		} 
		else if (!centerY.equals(other.centerY)) return false;
		if (dieHeight == null)
		{
			if (other.dieHeight != null) return false;
		} 
		else if (!dieHeight.equals(other.dieHeight)) return false;
		if (dieWidth == null)
		{
			if (other.dieWidth != null) return false;
		} 
		else if (!dieWidth.equals(other.dieWidth)) return false;
		if (flatOrient == null)
		{
			if (other.flatOrient != null) return false;
		} 
		else if (!flatOrient.equals(other.flatOrient)) return false;
		if (posX == null)
		{
			if (other.posX != null) return false;
		} 
		else if (!posX.equals(other.posX)) return false;
		if (posY == null)
		{
			if (other.posY != null) return false;
		} 
		else if (!posY.equals(other.posY)) return false;
		if (units == null)
		{
			if (other.units != null) return false;
		} 
		else if (!units.equals(other.units)) return false;
		if (waferSize == null)
		{
			if (other.waferSize != null) return false;
		} 
		else if (!waferSize.equals(other.waferSize)) return false;
		return true;
	}


}
