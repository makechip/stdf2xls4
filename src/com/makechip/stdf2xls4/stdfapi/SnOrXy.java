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

public abstract class SnOrXy implements Identity
{
    
    protected SnOrXy()
    {
    }
    
    /**
     * This method will return null if this object represents an x-y coordinate.
     * @return The serial number.  Null if this object represents an x-y coordinate.
     */
    public abstract String getSerialNumber();
    
    /**
     * Get the X-coordinate.
     * @return The X-coordinate; Short.MIN_VALUE if this object in not an X-Y coordinate.
     */
    public abstract short getX();
    
    /**
     * Get the Y-coordinate.
     * @return The Y-coordinate; Short.MIN_VALUE if this object in not an X-Y coordinate.
     */
    public abstract short getY();
    
    @Override
	public String toString()
    {
    	if (getSerialNumber() == null) return("X = " + getX() + " Y = " + getY());
    	return("SN = " + getSerialNumber());
    }

}
