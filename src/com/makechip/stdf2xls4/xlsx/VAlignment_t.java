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

package com.makechip.stdf2xls4.xlsx;

import org.apache.poi.ss.usermodel.CellStyle;


public enum VAlignment_t  
{
    BOTTOM(CellStyle.VERTICAL_BOTTOM),
    CENTER(CellStyle.VERTICAL_CENTER),
    JUSTIFY(CellStyle.VERTICAL_JUSTIFY),
    TOP(CellStyle.VERTICAL_TOP),
    DEFAULT(CellStyle.VERTICAL_CENTER);

    private Short alignment;

    private VAlignment_t(Short alignment)
    {
        this.alignment = alignment;
    }

    public short getAlignment() { return(alignment); }

    public static VAlignment_t getAlignment(short align)
    {
    	for (VAlignment_t v : VAlignment_t.class.getEnumConstants())
    	{
    		if (align == v.getAlignment()) return(v);
    	}
        return(DEFAULT);
    }


}
