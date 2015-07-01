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
 * This enum represents the OPT_FLAG values used by the ParametricTestRecord
 * and the MultipleResultParametricRecord.
 * @author eric
 *
 */
public enum OptFlag_t
{
	/**
	 * Indicates that the RES_SCAL value is invalid.
	 */
    RES_SCAL_INVALID(1),
    /**
     * Indicates that the START_IN and INCR_IN values are invalid.
     * This only applies to the MulipleResultParametricRecord.
     */
    START_INCR_IN_INVALID(2),
    /**
     * Indicates that there is no low specification limit.
     */
    NO_LO_SPEC_LIMIT(4),
    /**
     * Indicates that there is no high specification limit.
     */
    NO_HI_SPEC_LIMIT(8),
    /**
     * Indicates that the LO_LIMIT and LLM_SCAL values are invalid.
     */
    LO_LIMIT_LLM_SCAL_INVALID(16),
    /**
     * Indicates that the HI_LIMIT and HLM_SCAL values are invalid.
     */
    HI_LIMIT_HLM_SCAL_INVALID(32),
    /**
     * Indicates that there is no low limit.
     */
    NO_LO_LIMIT(64),
    /**
     * Indicates that there is no high limit.
     */
    NO_HI_LIMIT(128);
   
    public final byte bit;
    
    private OptFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
    /**
     * Given the OPT_FLAG byte this method will return a Set containing
     * all of the flags that are set within the byte.
     * @param b
     * @return
     */
    public static EnumSet<OptFlag_t> getBits(byte b)
    {
        EnumSet<OptFlag_t> set = EnumSet.noneOf(OptFlag_t.class); 
        EnumSet<OptFlag_t> all = EnumSet.allOf(OptFlag_t.class); 
        all.stream().filter(p -> (byte) (p.bit & b) == p.bit).forEach(x -> set.add(x));
        return(set);
    }

}
