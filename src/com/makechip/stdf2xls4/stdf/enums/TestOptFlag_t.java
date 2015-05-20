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

package com.makechip.stdf2xls4.stdf.enums;

import java.util.EnumSet;

public enum TestOptFlag_t
{
    TEST_MIN_INVALID(1),
    TEST_MAX_INVALID(2),
    TEST_TIME_INVALID(4),
    TEST_SUMS_INVALID(16),
    TEST_SQRS_INVALID(32);
   
    private final byte bit;
    
    private TestOptFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
    public byte getBit() { return(bit); }
    
    public static EnumSet<TestOptFlag_t> getBits(byte b)
    {
        EnumSet<TestOptFlag_t> optFlags = EnumSet.noneOf(TestOptFlag_t.class); 
        if ((b & (byte) 1)   == (byte) 1)   optFlags.add(TestOptFlag_t.TEST_MIN_INVALID);
        if ((b & (byte) 2)   == (byte) 2)   optFlags.add(TestOptFlag_t.TEST_MAX_INVALID);
        if ((b & (byte) 4)   == (byte) 4)   optFlags.add(TestOptFlag_t.TEST_TIME_INVALID);
        if ((b & (byte) 16)  == (byte) 16)  optFlags.add(TestOptFlag_t.TEST_SUMS_INVALID);
        if ((b & (byte) 32)  == (byte) 32)  optFlags.add(TestOptFlag_t.TEST_SQRS_INVALID);
        return(optFlags);
    }

}
