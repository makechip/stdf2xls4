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

import com.makechip.util.Identity;
import com.makechip.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;


public final class IdentityFactoryIO<K1 extends Identity, K2, V extends Identity>
{
    private Map<K1, Map<K2, V>> map;
    private Class<V> valueClass;
    private Class<K1> keyClass1;
    private Class<K2> keyClass2;

    public IdentityFactoryIO(Class<K1> keyClass1, Class<K2> keyClass2, Class<V> valueClass)
    {
        this.keyClass1 = keyClass1;
        this.keyClass2 = keyClass2;
        this.valueClass = valueClass;
        map = new IdentityHashMap<K1, Map<K2, V>>();
    }

    public int getInstanceCount()
    {
        int sum = 0;
        for (Map<K2, V> m : map.values()) sum += m.size();
        return(sum);
    }

    public V getExistingValue(K1 p1, K2 p2)
    {
        Map<K2, V> m = map.get(p1);
        if (m != null) return(m.get(p2));
        return(null);
    }
        
    public V getValue(K1 p1, K2 p2)
    {
        Map<K2, V> m = map.get(p1);
        if (m == null)
        {
            m = new HashMap<K2, V>();
            map.put(p1, m);
        }
        V v = m.get(p2);
        if (v == null)
        {
            try
            {
                Constructor<V> ctor = valueClass.getDeclaredConstructor(keyClass1, keyClass2);
                ctor.setAccessible(true);
                v = ctor.newInstance(p1, p2);
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
            m.put(p2, v);
        }
        return(v);
    }
}
