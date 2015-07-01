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
 * This enum represents the bits in the TEST_FLG byte of the FunctionalTestRecord,
 * ParametricTestRecord, and MultipleResultParametricRecord.
 * @author eric
 *
 */
public enum TestFlag_t
{
	/**
	 *  Bit 0 - If set indicates an alarm occurred during this test.
	 */
    ALARM(1),
    /**
     *  Bit 1 - If set indicates that the RESULT field is invalid.
     */
    RESULT_INVALID(2),
    /**
     *  Bit 2 - If set indicates that the RESULT is unreliable.
     */
    UNRELIABLE(4),
    /**
     *  Bit 3 - If set indicates that a timeout occurred during this test.
     */
    TIMEOUT(8),
    /**
     *  Bit 4 - If set indicates that the test was not executed.
     */
    NOT_EXECUTED(16),
    /**
     *  Bit 5 - If set indicates that the test was aborted.
     */
    ABORT(32),
    /**
     *  Bit 6 - If set indicates that no pass/fail indication was given.
     */
    NO_PASS_FAIL(64),
    /**
     *  Bit 7 - If set indicates that the test failed.
     */
    FAIL(128);
    
    /**
     *  The binary value of the bit position of this flag.
     *  For example, if the bit-position is 3, this will have a value of 8.
     */
   public final byte bit;
    
    private TestFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
    public static EnumSet<TestFlag_t> getBits(byte b)
    {
        EnumSet<TestFlag_t> set = EnumSet.noneOf(TestFlag_t.class);
        EnumSet<TestFlag_t> all = EnumSet.allOf(TestFlag_t.class);
        all.stream().filter(p -> (byte) (p.bit & b) == p.bit).forEach(x -> set.add(x));
        return(set);
    }
}
