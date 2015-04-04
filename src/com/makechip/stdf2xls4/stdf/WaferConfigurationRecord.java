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

import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: WaferConfigurationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class WaferConfigurationRecord extends StdfRecord
{
    private final float waferSize;
    private final float dieHeight;
    private final float dieWidth;
    private final short units;
    private final char flatOrient;
    private final short centerX;
    private final short centerY;
    private final char posX;
    private final char posY;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public WaferConfigurationRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.WCR, sequenceNumber, devNum, data);
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
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    wafer size: " + waferSize); sb.append(Log.eol);
        sb.append("    die height: " + dieHeight); sb.append(Log.eol);
        sb.append("    die width: " + dieWidth); sb.append(Log.eol);
        sb.append("    dimension units: " + units); sb.append(Log.eol);
        sb.append("    flat orientation: " + flatOrient); sb.append(Log.eol);
        sb.append("    center die X-coordinate: " + centerX); sb.append(Log.eol);
        sb.append("    center die Y-coordinate: " + centerY); sb.append(Log.eol);
        sb.append("    positive X-direction: " + posX); sb.append(Log.eol);
        sb.append("    positive Y-direction: " + posY); sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the waferSize
     */
    public float getWaferSize()
    {
        return waferSize;
    }

    /**
     * @return the dieHeight
     */
    public float getDieHeight()
    {
        return dieHeight;
    }

    /**
     * @return the dieWidth
     */
    public float getDieWidth()
    {
        return dieWidth;
    }

    /**
     * @return the units
     */
    public short getUnits()
    {
        return units;
    }

    /**
     * @return the flatOrient
     */
    public char getFlatOrient()
    {
        return flatOrient;
    }

    /**
     * @return the centerX
     */
    public short getCenterX()
    {
        return centerX;
    }

    /**
     * @return the centerY
     */
    public short getCenterY()
    {
        return centerY;
    }

    /**
     * @return the posX
     */
    public char getPosX()
    {
        return posX;
    }

    /**
     * @return the posU
     */
    public char getPosY()
    {
        return posY;
    }

}
