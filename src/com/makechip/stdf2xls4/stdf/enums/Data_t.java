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
*** @author ericw
*** @version $Id: StubGen.java 24 2007-12-29 21:27:59Z eric $
**/
public enum Data_t
{
    U_1(1, 1),     // unsigned integer
    U_2(2, 2),      // unsigned integer
    U_4(3, 4),      // unsigned integer
    I_1(4, 1),      // signed integer
    I_2(5, 2),      // signed integer
    I_4(6, 4),      // signed integer
    R_4(7, 4),      // float
    R_8(8, 8),      // double
    X_0(9, 0),      // dummy
    C_N(10, 1),      // one-byte char
    B_N(11, 1),      // bit-encoded field
    D_N(12, 1),      // long bit-encoded field
    N_1(14, 1),      // single nibble nibbles
    N_N(13, 1),      // array of nibbles
    B_1(15, 1),      // one byte bit field?
    C_1(16, 1),      // single character
    V(17, -1);       // field specified data type

    private final int type;
    private final int numBytes;

    private Data_t(int type, int numBytes)
    {
    	this.type = type;
        this.numBytes = numBytes;
    }

    public int getNumBytes() { return(numBytes); }
    public int getFieldType() { return(type); }
    
    public static Data_t getDataType(int fieldType)
    {
    	EnumSet<Data_t> set = EnumSet.allOf(Data_t.class);
    	return(set.stream().filter(d -> d.getFieldType() == fieldType).findFirst().orElse(null));
    }

}
