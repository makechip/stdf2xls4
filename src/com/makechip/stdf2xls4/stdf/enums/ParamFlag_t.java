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
 * This enum represents the PARM_FLAG values used by the ParametricTestRecord
 * and the MultipleResultParametricRecord.
 * @author eric
 *
 */
public enum ParamFlag_t
{
	/**
	 * Indicates there there was a scale error.
	 */
    SCALE_ERROR(1),
    /**
     * Indicates that there was a drift error.
     */
    DRIFT_ERROR(2),
    /**
     * Indicates that oscillation was detected.
     */
    OSCILLATION(4),
    /**
     * Indicates that the measured value was higher than the high test limit.
     */
    VALUE_HIGH(8),
    /**
     * Indicates that the measured value was lower than the low test limit.
     */
    VALUE_LOW(16),
    /**
     * Indicates that the test passed alternate test limits.
     */
    ALTERNATE_PASS(32),
    /**
     * Indicates that if result == low limit, then it is considered a pass.
     */
    LO_LIMIT_EQ_PASS(64),
    /**
     * Indicates that if result == high limit, then it is considered a pass.
     */
    HI_LIMIT_EQ_PASS(128);
    
    private final byte bit;
    
    private ParamFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
	/**
	 * Returns the bit position in the STDF byte that holds these flags.
	 * @return The bit position within the PARM_FLAG byte.
	 */
    public byte getBit() { return(bit); }
    
	/**
	 * Given the STDF PARM_FLAG byte this method will return the
	 * enums that are set in the byte.
	 * @param b The PARM_FLAG byte.
	 * @return A Set of the enum values that are set in the PARM_FLAG byte.
	 */
    public static EnumSet<ParamFlag_t> getBits(byte b)
    {
        EnumSet<ParamFlag_t> set = EnumSet.noneOf(ParamFlag_t.class);
        if ((b & (byte) 1)   == (byte) 1)   set.add(ParamFlag_t.SCALE_ERROR);
        if ((b & (byte) 2)   == (byte) 2)   set.add(ParamFlag_t.DRIFT_ERROR);
        if ((b & (byte) 4)   == (byte) 4)   set.add(ParamFlag_t.OSCILLATION);
        if ((b & (byte) 8)   == (byte) 8)   set.add(ParamFlag_t.VALUE_HIGH);
        if ((b & (byte) 16)  == (byte) 16)  set.add(ParamFlag_t.VALUE_LOW);
        if ((b & (byte) 32)  == (byte) 32)  set.add(ParamFlag_t.ALTERNATE_PASS);
        if ((b & (byte) 64)  == (byte) 64)  set.add(ParamFlag_t.LO_LIMIT_EQ_PASS);
        if ((b & (byte) 128) == (byte) 128) set.add(ParamFlag_t.HI_LIMIT_EQ_PASS);
        return(set);
    }

}
