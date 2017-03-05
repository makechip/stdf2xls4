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
package com.makechip.util.factory;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.makechip.util.Identity;
import com.makechip.util.Log;


public final class IdentityFactorySSN<V extends Identity>
{
    private TShortObjectHashMap<TShortObjectHashMap<TIntObjectHashMap<V>>> map;
    private Class<V> valueClass;

    public IdentityFactorySSN(Class<V> valueClass)
    {
        this.valueClass = valueClass;
        map = new TShortObjectHashMap<TShortObjectHashMap<TIntObjectHashMap<V>>>();
    }

    public int getInstanceCount()
    {
        int sum = 0;
        for (short l : map.keys()) 
        {
        	TShortObjectHashMap<TIntObjectHashMap<V>> m = map.get(l);
        	for(TIntObjectHashMap<V> o : m.values()) sum += o.size();
        }
        return(sum);
    }

    public V getExistingValue(short p1, short p2, int p3)
    {
        TShortObjectHashMap<TIntObjectHashMap<V>> m = map.get(p1);
        if (m != null) 
        {
        	TIntObjectHashMap<V> m2 = m.get(p2);
        	if (m2 != null) return(m2.get(p3));
        }
        return(null);
    }
        
    public V getValue(short p1, short p2, int p3)
    {
        TShortObjectHashMap<TIntObjectHashMap<V>> m = map.get(p1);
        if (m == null)
        {
            m = new TShortObjectHashMap<TIntObjectHashMap<V>>();
            map.put(p1, m);
        }
        TIntObjectHashMap<V> m2 = m.get(p2);
        if (m2 == null)
        {
        	m2 = new TIntObjectHashMap<V>();
        	m.put(p2,  m2);
        }
        V v = m2.get(p3);
        if (v == null)
        {
            try
            {
                Constructor<V> ctor = valueClass.getDeclaredConstructor(long.class, short.class, short.class);
                ctor.setAccessible(true);
                v = ctor.newInstance(p1, p2, p3);
            }
            catch (NoSuchMethodException e1)
            {
                Log.msg("Unable to find constructor for " + valueClass.getSimpleName());
                Log.fatal(e1);
            }
            catch (InvocationTargetException e2)
            {
                Log.msg("Error - Exception thrown while creating " + valueClass.getSimpleName() + " object");
                Log.fatal(e2);
            }
            catch (IllegalAccessException e3)
            {
                Log.msg("Error - Apparently setAccessible(true) doesn't work...");
                Log.fatal(e3);
            }
            catch (InstantiationException e4)
            {
                Log.msg("Error instantiating " + valueClass.getSimpleName() + " object");
                Log.fatal(e4);
            }
            m2.put(p3, v);
        }
        return(v);
    }
}
