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


public enum FontName_t
{
    ARIAL("Arial"),
    COURIER("Courier"),
    TAHOMA("Tahoma"),
    TIMES("Times");

    private String fontName;

    private FontName_t(String fontName)
    {
        this.fontName = fontName;
    }

    public String getFont() { return(fontName); }

    public static FontName_t getFont(String fname)
    {
    	for (FontName_t f : FontName_t.class.getEnumConstants())
    	{
    		if (f.getFont().equals(fname)) return(f);
    	}
        return(null);
    }

}
