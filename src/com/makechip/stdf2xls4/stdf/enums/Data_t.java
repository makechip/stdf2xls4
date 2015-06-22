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
 * This enum represents the STDF data types.
 *  @author ericw
 *  @version $Id: StubGen.java 24 2007-12-29 21:27:59Z eric $
 */
/**
 * @author eric
 *
 */
public enum Data_t
{
	/**
	 * Unsigned byte.
	 */
    U_1(1, 1),
    /**
     * Unsigned short.
     */
    U_2(2, 2),
    /**
     * Unsigned integer.
     */
    U_4(3, 4),
    /**
     * Signed byte.
     */
    I_1(4, 1),
    /**
     * Signed short.
     */
    I_2(5, 2),
    /**
     * Signed integer.
     */
    I_4(6, 4),
    /**
     * float.
     */
    R_4(7, 4),
    /**
     * double.
     */
    R_8(8, 8),
    /**
     * String.
     */
    C_N(10, 1),
    /**
     * Variable length bit field - up to 8 * 255 bits
     */
    B_N(11, 1),
    /**
     * Variable length bit field - up to 8 * 65535 bits
     */
    D_N(12, 1),
    /**
     * Fixed length Field of nibbles
     */
    N_N(13, 1);
 //   N_1(14, 1),
 //   B_1(15, 1),
 //   C_1(16, 1),
 //   V(17, -1);

    private final int type;
    private final int numBytes;

    private Data_t(int type, int numBytes)
    {
    	this.type = type;
        this.numBytes = numBytes;
    }

    /**
     * Get the number of bytes stored by this data type.
     * @return The number of bytes used by the data type, or 1 if a variable length field.
     */
    public int getNumBytes() { return(numBytes); }
    /**
     * Get the numeric type used for this type.  
     * (This is used in the GenericDataRecord)
     * @return The numeric representation of the data type.
     */
    public int getFieldType() { return(type); }
    
    /**
     * Convert the numeric representation of the data type back into the enum value.
     * @param fieldType The value returned by @see getFieldType().
     * @return The enum representation of the data type corresponding to the given numeric value.
     */
    public static Data_t getDataType(int fieldType)
    {
    	EnumSet<Data_t> set = EnumSet.allOf(Data_t.class);
    	return(set.stream().filter(d -> d.getFieldType() == fieldType).findFirst().orElse(null));
    }

}
