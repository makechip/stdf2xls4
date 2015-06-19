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
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: WaferConfigurationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferConfigurationRecord extends StdfRecord
{
    public final float waferSize;
    public final float dieHeight;
    public final float dieWidth;
    public final short units;
    public final char flatOrient;
    public final short centerX;
    public final short centerY;
    public final char posX;
    public final char posY;
    
    /**
    *** @param p1
    *** @param p2
    **/
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":").append(Log.eol);
        sb.append("    wafer size: " + waferSize).append(Log.eol);
        sb.append("    die height: " + dieHeight).append(Log.eol);
        sb.append("    die width: " + dieWidth).append(Log.eol);
        sb.append("    dimension units: " + units).append(Log.eol);
        sb.append("    flat orientation: " + flatOrient).append(Log.eol);
        sb.append("    center die X-coordinate: " + centerX).append(Log.eol);
        sb.append("    center die Y-coordinate: " + centerY).append(Log.eol);
        sb.append("    positive X-direction: " + posX).append(Log.eol);
        sb.append("    positive Y-direction: " + posY).append(Log.eol);
        return(sb.toString());
    }

}
