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
import java.util.Map;

import com.makechip.util.Identity;
import com.makechip.util.Log;


public final class IdentityFactoryO<K1, V extends Identity>
{
    private Map<K1, V> map;
    private Class<V> valueClass;
    private Class<K1> keyClass;

    public IdentityFactoryO(Class<K1> keyClass, Class<V> valueClass)
    {
        this.valueClass = valueClass;
        this.keyClass = keyClass;
        map = new HashMap<K1, V>();
    }

    public int getInstanceCount()
    {
        return(map.size());
    }
    
    public V getExistingValue(K1 param)
    {
        return(map.get(param));
    }

    public V getValue(K1 param)
    {
        V v = map.get(param);
        if (v == null)
        {
            try
            {
                Constructor<V> ctor = valueClass.getDeclaredConstructor(keyClass);
                ctor.setAccessible(true);
                v = ctor.newInstance(param);
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
            map.put(param, v);
        }
        return(v);
    }
}
