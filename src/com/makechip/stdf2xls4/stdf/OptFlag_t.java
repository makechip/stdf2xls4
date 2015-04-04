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

public enum OptFlag_t
{
    RES_SCAL_INVALID,
    START_INCR_IN_INVALID,
    NO_LO_SPEC_LIMIT,
    NO_HI_SPEC_LIMIT,
    LO_LIMIT_LLM_SCAL_INVALID,
    HI_LIMIT_HLM_SCAL_INVALID,
    NO_LO_LIMIT,
    NO_HI_LIMIT;
    
    
    public static EnumSet<OptFlag_t> getBits(byte b)
    {
        EnumSet<OptFlag_t> optFlags = EnumSet.noneOf(OptFlag_t.class); 
        if ((b & (byte) 1)   == (byte) 1)   optFlags.add(OptFlag_t.RES_SCAL_INVALID);
        if ((b & (byte) 2)   == (byte) 2)   optFlags.add(OptFlag_t.START_INCR_IN_INVALID);
        if ((b & (byte) 4)   == (byte) 4)   optFlags.add(OptFlag_t.NO_LO_SPEC_LIMIT);
        if ((b & (byte) 8)   == (byte) 8)   optFlags.add(OptFlag_t.NO_HI_SPEC_LIMIT);
        if ((b & (byte) 16)  == (byte) 16)  optFlags.add(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID);
        if ((b & (byte) 32)  == (byte) 32)  optFlags.add(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID);
        if ((b & (byte) 64)  == (byte) 64)  optFlags.add(OptFlag_t.NO_LO_LIMIT);
        if ((b & (byte) 128) == (byte) 128) optFlags.add(OptFlag_t.NO_HI_LIMIT);
        return(optFlags);
    }

}
