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

public enum FTROptFlag_t 
{
	CYCLE_CNT_INVALID(1),
	REL_VADDR_INVALID(2),
	REPEAT_CNT_INVALID(4),
	NUM_FAIL_INVALID(8),
	XY_FAIL_ADDR_INVALID(16),
	VEC_OFFSET_INVALID(32);
	
	private final byte bit;
	
	private FTROptFlag_t(int bit)
	{
		this.bit = (byte) bit;
	}
	
	public byte getBit() { return(bit); }
	
    public static EnumSet<FTROptFlag_t> getBits(byte b)
    {
        EnumSet<FTROptFlag_t> set = EnumSet.noneOf(FTROptFlag_t.class);
        if ((b & (byte) 1) == (byte) 1) set.add(CYCLE_CNT_INVALID);
        if ((b & (byte) 2) == (byte) 2) set.add(REL_VADDR_INVALID);
        if ((b & (byte) 4) == (byte) 4) set.add(REPEAT_CNT_INVALID);
        if ((b & (byte) 8) == (byte) 8) set.add(NUM_FAIL_INVALID);
        if ((b & (byte) 16) == (byte) 16) set.add(XY_FAIL_ADDR_INVALID);
        if ((b & (byte) 32) == (byte) 32) set.add(VEC_OFFSET_INVALID);
        return(set);
    }

}
