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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import com.makechip.util.Identity;
import com.makechip.util.Log;

                     // List of K mapped to Identity V
public final class ArrayIdentityFactoryO<K, V extends Identity>
{
    private HashMap<K, ArrayIdentityFactoryO<K, V>> map;
    private HashMap<K, V> values;
    private Class<K> keyClass;
    private Class<V> valueClass;
    
    public ArrayIdentityFactoryO(Class<K> keyClass, Class<V> valueClass)
    {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        map = new HashMap<K, ArrayIdentityFactoryO<K, V>>();
        values = new HashMap<K, V>();
    }
    
    public int getInstanceCount() 
    { 
        int ic = 0;
        ic += values.size();
        for (K k : map.keySet())
        {
            ArrayIdentityFactoryO<K, V> a = map.get(k);
            ic += a.getInstanceCount();
        }
        return(ic);
    }
    
    public V getValue(List<K> arrayValues)
    {
        int wNum = 0;
        if (arrayValues.size() == 1)
        {
            V v = values.get(arrayValues.get(wNum));
            if (v == null)
            {
                v = createValue(arrayValues);
                values.put(arrayValues.get(wNum), v);
            }
            return(v);
        }
        ArrayIdentityFactoryO<K, V> m1 = map.get(arrayValues.get(wNum));
        if (m1 == null)
        {
            m1 = new ArrayIdentityFactoryO<K, V>(keyClass, valueClass);
            map.put(arrayValues.get(wNum), m1);
        }
        int i = wNum + 1;
        return(m1.getValue(i, arrayValues));
    }
    
    private V getValue(int wNum, List<K> arrayValues)
    {
        if (wNum == (arrayValues.size() - 1))
        {    
            V v = values.get(arrayValues.get(wNum));
            if (v == null)
            {
                v = createValue(arrayValues);
                values.put(arrayValues.get(wNum), v);
            }
            return(v);
        }
        ArrayIdentityFactoryO<K, V> m1 = map.get(arrayValues.get(wNum));
        if (m1 == null)
        {
            m1 = new ArrayIdentityFactoryO<K, V>(keyClass, valueClass);
            map.put(arrayValues.get(wNum), m1);
        }
        int i = wNum + 1;
        return(m1.getValue(i, arrayValues));
    }
    
    private V createValue(List<K> arrayValues)
    {
        V v = null;
        try
        {
            Constructor<V> ctor = valueClass.getDeclaredConstructor(List.class);
            ctor.setAccessible(true);
            v = ctor.newInstance(arrayValues);
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
        return(v);
    }
    
}
