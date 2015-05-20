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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.makechip.util.factory.ArrayIdentityFactoryO;
import com.makechip.util.Identity;
import com.makechip.util.Immutable;

public final class PinList implements Identity, Immutable
{
    private static ArrayIdentityFactoryO<String, PinList> map = 
        new ArrayIdentityFactoryO<String, PinList>(String.class, PinList.class);
    private static final PinList emptyList = new PinList(new ArrayList<String>());
    private List<String> list;
    
    private PinList(List<String> list)
    {
        this.list = new ArrayList<String>(list.size());
        for (String s : list) this.list.add(s);
    }
    
    public static PinList getPinList(List<String> list)
    {
    	if (list.size() == 0) return(emptyList);
        return(map.getValue(list));
    }
    
    public List<String> getPins()
    {
        return(Collections.unmodifiableList(list));
    }
    
    public int getNumPins() { return(list.size()); }

    @Override
    public int getInstanceCount()
    {
        return(map.getInstanceCount());
    }

}
