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
package com.makechip.stdf2xls4.xlsx;

import com.makechip.stdf2xls4.stdfapi.SnOrXy;
import com.makechip.util.Identity;
import com.makechip.util.factory.IdentityFactoryONINN;
import com.makechip.util.Immutable;

public final class DeviceHeader implements Identity, Immutable
{
    private static IdentityFactoryONINN<Boolean, SnOrXy, DeviceHeader> map =
        new IdentityFactoryONINN<Boolean, SnOrXy, DeviceHeader>(Boolean.class, SnOrXy.class, DeviceHeader.class);
    private final boolean result;
    private final SnOrXy snOrXy;
    private final int temp;
    private final int hwbin;
    private final int swbin;
    
    private DeviceHeader(Boolean result, int temp, SnOrXy snOrXy, int hwbin, int swbin)
    {
        this.result = result;
        this.snOrXy = snOrXy;
        this.temp = temp;
        this.hwbin = hwbin;
        this.swbin = swbin;
    }
    
    public static DeviceHeader getDeviceHeader(boolean result, SnOrXy snOrXy, int temp, int hwbin, int swbin)
    {
        return(map.getValue(result, temp, snOrXy, hwbin, swbin));
    }

    public boolean isPass()
    {
        return(result);
    }


    public SnOrXy getSnOrXy()
    {
        return(snOrXy);
    }


    public int getTemp()
    {
        return(temp);
    }
    
    public int getHwBin()
    {
    	return(hwbin);
    }

    public int getSwBin()
    {
    	return(swbin);
    }

    @Override
    public int getInstanceCount()
    {
        return(map.getInstanceCount());
    }

}
