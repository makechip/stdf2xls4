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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.makechip.util.Identity;
import com.makechip.util.Log;


public final class IdentityFactoryONNZ<K, V extends Identity>
{
    private Map<K, TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>>> map;
    private Class<V> valueClass;
    private Class<K> keyClass;

    public IdentityFactoryONNZ(Class<K> keyClass, Class<V> valueClass)
    {
    	this.keyClass = keyClass;
        this.valueClass = valueClass;
        map = new HashMap<K, TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>>>();
    }
    
    public List<V> getValues()
    {
    	List<V> list = new ArrayList<>();
    	for (K k : map.keySet())
    	{
    		TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>> m1 = map.get(k);
    		for (int i : m1.keys())
    		{
    			TIntObjectHashMap<Map<Boolean, V>> m2 = m1.get(i);
    			for (int j : m2.keys())
    			{
    				Map<Boolean, V> m3 = m2.get(j);
    				for (V v : m3.values()) list.add(v);
    			}
    		}
    	}
    	return(list);
    }

    public int getInstanceCount()
    {
        int sum = 0;
        for (K k : map.keySet())
        {
        	TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>> m1 = map.get(k);
        	for (TIntObjectHashMap<Map<Boolean, V>> m : m1.values()) 
        	{
            	for (Map<Boolean, V> m2 : m.values()) sum += m2.size();
        	}
        }
        return(sum);
    }

    public V getExistingValue(K p0, int p1, int p2, boolean p3)
    {
       	TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>> m0 = map.get(p0);
       	if (m0 != null)
       	{
       		TIntObjectHashMap<Map<Boolean, V>> m1 = m0.get(p1);
        	if (m1 != null)
        	{
            	Map<Boolean, V> m2 = m1.get(p2);
            	if (m2 != null) 
            	{
            		V v = m2.get(p3);
            		return(v);
            	}
        	}
       	}
        return(null);
    }
        
    public V getValue(K p0, int p1, int p2, boolean p3)
    {
       	TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>> m0 = map.get(p0);
       	if (m0 == null)
       	{
       		m0 = new TIntObjectHashMap<TIntObjectHashMap<Map<Boolean, V>>>();
       		map.put(p0,  m0);
       	}
        TIntObjectHashMap<Map<Boolean, V>> m1 = m0.get(p1);
        if (m1 == null)
        {
            m1 = new TIntObjectHashMap<Map<Boolean, V>>();
            m0.put(p1, m1);
        }
        Map<Boolean, V> m2 = m1.get(p2);
        if (m2 == null)
        {
            m2 = new HashMap<>();
            m1.put(p2, m2);
        }
        V v = m2.get(p3);
        if (v == null)
        {
            try
            {
                Constructor<V> ctor = valueClass.getDeclaredConstructor(keyClass, int.class, int.class, boolean.class);
                ctor.setAccessible(true);
                v = ctor.newInstance(p0, p1, p2, p3);
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
