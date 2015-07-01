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

/**
 * This enum represents the OPT_FLAG values in the FunctionalTestRecord.
 * @author eric
 */
public enum FTROptFlag_t 
{
	/**
	 * Indicates that the CYCL_CNT data is invalid.
	 */
	CYCLE_CNT_INVALID(1),
	/**
	 * Indicates that the REL_VADR data is invalid.
	 */
	REL_VADDR_INVALID(2),
	/**
	 * Indicates that the REPT_CNT data is invalid.
	 */
	REPEAT_CNT_INVALID(4),
	/**
	 * Indicates that the NUM_FAIL data is invalid.
	 */
	NUM_FAIL_INVALID(8),
	/**
	 * Indicates that the XFAIL_AD and YFAIL_AD data are invalid.
	 */
	XY_FAIL_ADDR_INVALID(16),
	/**
	 * Indicates that the VECT_OFF data is invalid.
	 */
	VEC_OFFSET_INVALID(32);
	
    /**
     *  The binary value of the bit position of this flag.
     *  For example, if the bit-position is 3, this will have a value of 8.
     */
	public final byte bit;
	
	private FTROptFlag_t(int bit)
	{
		this.bit = (byte) bit;
	}
	
	/**
	 * Given the STDF OPT_FLAG byte this method will return the
	 * enums that are set in the byte.
	 * @param b The OPT_FLAG byte.
	 * @return A Set of the enum values that are set in the OPT_FLAG byte.
	 */
    public static EnumSet<FTROptFlag_t> getBits(final byte b)
    {
        EnumSet<FTROptFlag_t> set = EnumSet.noneOf(FTROptFlag_t.class);
        EnumSet<FTROptFlag_t> all = EnumSet.allOf(FTROptFlag_t.class);
        all.stream().filter(p -> (byte) (p.bit & b) == p.bit).forEach(x -> set.add(x));
        return(set);
    }

}
