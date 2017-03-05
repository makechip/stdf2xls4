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

import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryON;

public class SN extends SnOrXy implements Immutable, Comparable<SN>
{
    private static IdentityFactoryON<String, SN> map = new IdentityFactoryON<String, SN>(String.class, SN.class);
    public final String serialNumber;
    
    protected SN(String serialNumber, int dupNum)
    {
        super(dupNum);
        this.serialNumber = serialNumber;
    }
    
    public static SN getSN(String serialNumber, int dupNum)
    {
        return(map.getValue(serialNumber.trim(), dupNum));
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
    public int compareTo(SN sn)
    {
    	//int a = Integer.parseInt(serialNumber);
    	//int b = Integer.parseInt(sn.getSerialNumber());
    	//return(a - b);
        return(serialNumber.compareTo(sn.getSerialNumber()));
    }

}
