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
import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryLO;

public final class TimeSN extends SnOrXy implements Identity, Immutable, Comparable<TimeSN>
{
    private static IdentityFactoryLO<String, TimeSN> map = new IdentityFactoryLO<String, TimeSN>(String.class, TimeSN.class);
    private final String serialNumber;
    private final long timeStamp;
    
    private TimeSN(long timeStamp, String serialNumber)
    {
        super();
        this.timeStamp = timeStamp;
        this.serialNumber = serialNumber;
    }
    
    public static TimeSN getTimeSN(long timeStamp, String serialNumber)
    {
        return(map.getValue(timeStamp, serialNumber));
    }
    
    @Override
	public String getSerialNumber() { return(serialNumber); }
    
    @Override
    public int getInstanceCount()
    {
        return(map.getInstanceCount());
    }

    @Override
    public short getX()
    {
        return(Short.MIN_VALUE);
    }

    @Override
    public short getY()
    {
        return(Short.MIN_VALUE);
    }

    @Override
    public int compareTo(TimeSN sn)
    {
    	if (!serialNumber.equals(sn.getSerialNumber())) return(serialNumber.compareTo(sn.getSerialNumber()));
    	if (timeStamp != sn.getTimeStamp())
    	{
    		if ((timeStamp - sn.getTimeStamp()) > 0) return(1);
    		return(-1);
    	}
        return(0);
    }

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp() 
	{
		return timeStamp;
	}

}
