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

package com.makechip.stdf2xls4.stdf;

import java.util.EnumSet;

public enum TestFlag_t
{
    ALARM,
    UNRELIABLE,
    TIMEOUT,
    NOT_EXECUTED,
    ABORT,
    NO_PASS_FAIL,
    FAIL;
    
    public static EnumSet<TestFlag_t> getBits(byte b)
    {
        EnumSet<TestFlag_t> set = EnumSet.noneOf(TestFlag_t.class);
        if ((b & (byte) 1) == (byte) 1) set.add(ALARM);
        if ((b & (byte) 4) == (byte) 4) set.add(UNRELIABLE);
        if ((b & (byte) 8) == (byte) 8) set.add(TIMEOUT);
        if ((b & (byte) 16) == (byte) 16) set.add(NOT_EXECUTED);
        if ((b & (byte) 32) == (byte) 32) set.add(ABORT);
        if ((b & (byte) 64) == (byte) 64) set.add(NO_PASS_FAIL);
        if ((b & (byte) 128) == (byte) 128) set.add(FAIL);
        return(set);
    }
}
