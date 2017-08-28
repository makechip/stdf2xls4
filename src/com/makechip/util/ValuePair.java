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
// ******************************************************************************
// ** **
// ValuePair.java
// Author: eric
// Copyright 2004 Eric West
//
//****************************************************************************
package com.makechip.util;

/**
 * Utility class to pair together two objects
 * 
 * @version $Id: ValuePair.java 1 2007-09-15 21:50:34Z eric $
 * @param <T>
 * @param <U>
 */
public final class ValuePair<T, U>
{
    public T value1;
    public U value2;
    
    public ValuePair(T value1, U value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ValuePair)
        {
            ValuePair<?, ?> vt = ValuePair.class.cast(o);
            if ((value1 == null) ^ (vt.value1 == null)) return(false);
            if (value1 != null)
            {
                if (!value1.equals(vt.value1)) return(false);
            }
            if ((value2 == null) ^ (vt.value2 == null)) return(false);
            if (value2 != null)
            {
                if (!value2.equals(vt.value2)) return(false);
            }
            return(true);
        }
        return(false);
    }
    
    @Override
    public int hashCode()
    {
        int a = (value1 == null) ? 146 : value1.hashCode();
        int b = (value2 == null) ? 567 : value2.hashCode();
        return(a * b);
    }

    @Override
    public String toString()
    {
        return ("(" + value1 + ", " + value2 + ")");
    }

}
