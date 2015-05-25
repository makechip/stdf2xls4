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

import java.util.HashMap;

import com.makechip.util.Identity;
import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryIO;

public final class PinTestID extends TestID implements Identity, Immutable 
{
    public final String pin;
   
    private PinTestID(TestID id, String pin)
    {
    	super(id.testNum, id.testName, id.dupNum);
    	this.pin = pin;
    }
    
    public static PinTestID getTestID(HashMap<String, String> hdr, IdentityDatabase idb, TestID id, String pin)
    {
    	IdentityFactoryIO<TestID, String, PinTestID> io = idb.testIdPinMap.get(hdr);
    	if (io == null)
    	{
    		io = new IdentityFactoryIO<>(TestID.class, String.class, PinTestID.class);
    		idb.testIdPinMap.put(hdr,  io);
    	}
        return(io.getValue(id, pin));
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("" + testNum).append("_").append(testName);
        if (dupNum != 0) sb.append("_" + dupNum);
        sb.append(" [").append(pin).append("]");
        return(sb.toString());
    }
    
    @Override
    public int getInstanceCount()
    {
        return(-1);
    }
    
}
