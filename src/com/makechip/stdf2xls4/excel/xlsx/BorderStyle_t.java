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

package com.makechip.stdf2xls4.excel.xlsx;

import org.apache.poi.ss.usermodel.CellStyle;

public enum BorderStyle_t
{
    NONE(CellStyle.BORDER_NONE),
    DASH_DOT(CellStyle.BORDER_DASH_DOT),
    DASH_DOT_DOT(CellStyle.BORDER_DASH_DOT_DOT),
    DASHED(CellStyle.BORDER_DASHED),
    DOTTED(CellStyle.BORDER_DOTTED),
    DOUBLE(CellStyle.BORDER_DOUBLE),
    HAIR(CellStyle.BORDER_HAIR),
    MEDIUM(CellStyle.BORDER_MEDIUM),
    MEDIUM_DASH_DOT(CellStyle.BORDER_MEDIUM_DASH_DOT),
    MEDIUM_DASH_DOT_DOT(CellStyle.BORDER_MEDIUM_DASH_DOT_DOT),
    MEDIUM_DASHED(CellStyle.BORDER_MEDIUM_DASHED),
    SLANTED_DASH_DOT(CellStyle.BORDER_SLANTED_DASH_DOT),
    THICK(CellStyle.BORDER_THICK),
    THIN(CellStyle.BORDER_THIN),
    DEFAULT(CellStyle.BORDER_THIN);

    private final short style;

    private BorderStyle_t(final short style)
    {
        this.style = style;
    }

    public final short getStyle() { return(style); }

    public static BorderStyle_t getStyle(short style)
    {
    	for (BorderStyle_t b : BorderStyle_t.class.getEnumConstants())
    	{
    		if (style == b.getStyle()) return(b);
    	}
        return(DEFAULT);
    }

}
