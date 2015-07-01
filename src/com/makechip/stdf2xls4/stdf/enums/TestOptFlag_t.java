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
 * This enum represents the bits in the OPT_FLAG byte of the TestSynopsysRecord.
 * @author eric
 *
 */
public enum TestOptFlag_t
{
	/**
	 * Bit 0 - If set indicates that the TEST_MIN value is invalid.
	 */
    TEST_MIN_INVALID(1),
    /**
     *  Bit 1 - If set indicates that the TEST_MAX value is invalid.
     */
    TEST_MAX_INVALID(2),
    /**
     *  Bit 2 - If set indicates that the TEST_TIM value is invalid.
     */
    TEST_TIME_INVALID(4),
    /**
     *  Bit 4 - If set indicates that the TST_SUMS value is invalid.
     */
    TEST_SUMS_INVALID(16),
    /**
     *  Bit 5 - If set indicates that the TST_SQRS value is invalid.
     */
    TEST_SQRS_INVALID(32);
   
    public final byte bit;
    
    private TestOptFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
	/**
	 * Given the STDF PARM_FLAG byte this method will return the
	 * enums that are set in the byte.
	 * @param b The PARM_FLAG byte.
	 * @return A Set of the enum values that are set in the PARM_FLAG byte.
	 */
    public static EnumSet<TestOptFlag_t> getBits(byte b)
    {
        EnumSet<TestOptFlag_t> set = EnumSet.noneOf(TestOptFlag_t.class); 
        EnumSet<TestOptFlag_t> all = EnumSet.allOf(TestOptFlag_t.class); 
        all.stream().filter(p -> (byte) (p.bit & b) == p.bit).forEach(x -> set.add(x));
        return(set);
    }

}
