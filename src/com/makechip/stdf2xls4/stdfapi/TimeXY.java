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
package com.makechip.stdf2xls4.stdfapi;

import com.makechip.util.Identity;
import com.makechip.util.factory.IdentityFactoryLSS;
import com.makechip.util.Immutable;

public final class TimeXY extends SnOrXy implements Comparable<TimeXY>, Identity, Immutable
{
    private static IdentityFactoryLSS<TimeXY> map = new IdentityFactoryLSS<TimeXY>(TimeXY.class);
    private final long timeStamp;
    private final short x;
    private final short y;
    
    private TimeXY(long timeStamp, short x, short y)
    {
        super();
        this.timeStamp = timeStamp;
        this.x = x;
        this.y = y;
    }
    
    public static TimeXY getTimeXY(long timeStamp, short x, short y)
    {
        return(map.getValue(timeStamp, x, y));
    }
    
    public static TimeXY getExistingTimeXY(long timeStamp, short x, short y)
    {
    	return(map.getExistingValue(timeStamp, x, y));
    }
    
    @Override
	public short getX()
    {
        return(x);
    }
    
    @Override
	public short getY()
    {
        return(y);
    }

    @Override
    public int compareTo(TimeXY xy)
    {
        if (x != xy.getX()) return(x - xy.getX());
        if (y != xy.getY()) return(y - xy.getY());
        if (timeStamp != xy.getTimeStamp())
        {
        	if (timeStamp > xy.getTimeStamp()) return(1);
        	return(-1);
        }
        return(0);
    }
    
    @Override
    public String toString()
    {
        return("" + x + ", " + y);
    }

    @Override
    public int getInstanceCount()
    {
        return(map.getInstanceCount());
    }

    @Override
    public String getSerialNumber()
    {
        return(null);
    }

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp() 
	{
		return timeStamp;
	}
    
}
