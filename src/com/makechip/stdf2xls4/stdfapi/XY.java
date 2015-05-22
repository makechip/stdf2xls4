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

import gnu.trove.map.hash.TShortObjectHashMap;

public final class XY extends SnOrXy implements Comparable<XY>, Immutable
{
    private static TShortObjectHashMap<TShortObjectHashMap<XY>> map = new TShortObjectHashMap<TShortObjectHashMap<XY>>();
    private final short x;
    private final short y;
    
    private XY(short x, short y)
    {
        super();
        this.x = x;
        this.y = y;
    }
    
    public static XY getXY(short x, short y)
    {
        TShortObjectHashMap<XY> m1 = map.get(x);
        if (m1 == null)
        {
            m1 = new TShortObjectHashMap<XY>();
            map.put(x, m1);
        }
        XY xy = m1.get(y);
        if (xy == null)
        {
            xy = new XY(x, y);
            m1.put(y, xy);
        }
        return(xy);
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
    public int compareTo(XY xy)
    {
        if (x != xy.getX()) return(x - xy.getX());
        return(y - xy.getY());
    }
    
    @Override
    public String toString()
    {
        return("" + x + ", " + y);
    }

    @Override
    public int getInstanceCount()
    {
        int i = 0;
        for (TShortObjectHashMap<XY> m1 : map.values()) i+= m1.size();
        return(i);
    }

    @Override
    public String getSerialNumber()
    {
        return(null);
    }
    
}
