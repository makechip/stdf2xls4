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
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.makechip.util.Identity;
import com.makechip.util.Log;


public final class IdentityFactoryLSSON<K, V extends Identity>
{
    private TLongObjectHashMap<TShortObjectHashMap<TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>>>> map;
    private Class<V> valueClass;
    private Class<K> keyClass;

    public IdentityFactoryLSSON(Class<K> keyClass, Class<V> valueClass)
    {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        map = new TLongObjectHashMap<TShortObjectHashMap<TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>>>>();
    }

    public int getInstanceCount()
    {
        int sum = 0;
        for (long l : map.keys()) 
        {
        	TShortObjectHashMap<TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>>> m1 = map.get(l);
        	for (short s1 : m1.keys())
        	{
        	    TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>> m2 = m1.get(s1);
        	    for (short s2 : m2.keys())
        	    {
        	        HashMap<K, TIntObjectHashMap<V>> m3 = m2.get(s2);
        	        for (K s : m3.keySet())
        	        {
        	            TIntObjectHashMap<V> m4 = m3.get(s);
        	            sum += m4.size();
        	        }
        	    }
        	}
        }
        return(sum);
    }

    public V getExistingValue(long p1, short p2, short p3, K p4, int p5)
    {
        TShortObjectHashMap<TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>>> m1 = map.get(p1);
        if (m1 != null) 
        {
        	TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>> m2 = m1.get(p2);
        	if (m2 != null) 
        	{
        	    HashMap<K, TIntObjectHashMap<V>> m3 = m2.get(p3);
        	    if (m3 != null)
        	    {
        	        TIntObjectHashMap<V> m4 = m3.get(p4);
        	        return(m4.get(p5));
        	    }
        	}
        }
        return(null);
    }
        
    public V getValue(long p1, short p2, short p3, K p4, int p5)
    {
        TShortObjectHashMap<TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>>> m1 = map.get(p1);
        if (m1 == null)
        {
            m1 = new TShortObjectHashMap<>();
            map.put(p1, m1);
        }
        TShortObjectHashMap<HashMap<K, TIntObjectHashMap<V>>> m2 = m1.get(p2);
        if (m2 == null)
        {
        	m2 = new TShortObjectHashMap<>();
        	m1.put(p2,  m2);
        }
        HashMap<K, TIntObjectHashMap<V>> m3 = m2.get(p3);
        if (m3 == null)
        {
            m3 = new HashMap<>();
            m2.put(p3, m3);
        }
        TIntObjectHashMap<V> m4 = m3.get(p4);
        if (m4 == null)
        {
            m4 = new TIntObjectHashMap<>();
            m3.put(p4, m4);
        }
        V v = m4.get(p5);
        if (v == null)
        {
            try
            {
                Constructor<V> ctor = valueClass.getDeclaredConstructor(long.class, short.class, short.class, keyClass, int.class);
                ctor.setAccessible(true);
                v = ctor.newInstance(p1, p2, p3, p4, p5);
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
            m4.put(p5, v);
        }
        return(v);
    }
}
