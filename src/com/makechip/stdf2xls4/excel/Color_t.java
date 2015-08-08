// Copyright 2011,2012 makechip.com
// This file is part of stdf2xls.
// 
// stdf2xls is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// stdf2xls is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with stdf2xls.  If not, see <http://www.gnu.org/licenses/>.

package com.makechip.stdf2xls4.excel;

import jxl.format.Colour;
import jxl.format.RGB;

public enum Color_t
{
    BLACK(8, getColor(Colour.BLACK)),
    WHITE(9, getColor(Colour.WHITE)),
    YELLOW(13, getColor(Colour.YELLOW)),
	RED(10, getColor(Colour.RED)),
	BRIGHT_GREEN(11, getColor(Colour.BRIGHT_GREEN)),
	BLUE(12, getColor(Colour.BLUE)),
	PINK(14, getColor(Colour.PINK)),
	TURQUOISE(15, getColor(Colour.TURQUOISE)),
	DARK_RED(16, getColor(Colour.DARK_RED)),
	GREEN(17, getColor(Colour.GREEN)),
	DARK_BLUE(18, getColor(Colour.DARK_BLUE)),
	DARK_YELLOW(19, getColor(Colour.DARK_YELLOW)),
	VIOLET(20, getColor(Colour.VIOLET)),
	TEAL(21, getColor(Colour.TEAL)),
	GREY_25_PERCENT(22, getColor(Colour.GREY_25_PERCENT)),
	GREY_50_PERCENT(23, getColor(Colour.GREY_50_PERCENT)),
	PERIWINKLE(24, getColor(Colour.PERIWINKLE)),
	PLUM2(25, getColor(Colour.PLUM2)),
	IVORY(26, getColor(Colour.IVORY)),
	LIGHT_TURQUOISE2(27, getColor(Colour.LIGHT_TURQUOISE2)),
	DARK_PURPLE(28, getColor(Colour.DARK_PURPLE)),
	CORAL(29, getColor(Colour.CORAL)),
	OCEAN_BLUE(30, getColor(Colour.OCEAN_BLUE)),
	ICE_BLUE(31, getColor(Colour.ICE_BLUE)),
	DARK_BLUE2(32, getColor(Colour.DARK_BLUE2)),
	PINK2(33, getColor(Colour.PINK2)),
	YELLOW2(34, getColor(Colour.YELLOW2)),
	TURQOISE2(35, getColor(Colour.TURQOISE2)),
	VIOLET2(36, getColor(Colour.VIOLET2)),
	DARK_RED2(37, getColor(Colour.DARK_RED2)),
	TEAL2(38, getColor(Colour.TEAL2)),
	BLUE2(39, getColor(Colour.BLUE2)),
	SKY_BLUE(40, new java.awt.Color(82, 123, 189)),
	LIGHT_TURQUOISE(41, getColor(Colour.LIGHT_TURQUOISE)),
	LIGHT_GREEN(42, getColor(Colour.LIGHT_GREEN)),
	VERY_LIGHT_YELLOW(43, getColor(Colour.VERY_LIGHT_YELLOW)),
	PALE_BLUE(44, getColor(Colour.PALE_BLUE)),
	ROSE(45, getColor(Colour.ROSE)),
	LAVENDER(46, getColor(Colour.LAVENDER)),
	TAN(47, getColor(Colour.TAN)),
	LIGHT_BLUE(48, getColor(Colour.LIGHT_BLUE)),
	AQUA(49, getColor(Colour.AQUA)),
	LIME(50, getColor(Colour.LIME)),
	GOLD(51, getColor(Colour.GOLD)),
	LIGHT_ORANGE(52, getColor(Colour.LIGHT_ORANGE)),
	ORANGE(53, getColor(Colour.ORANGE)),
	BLUE_GREY(54, getColor(Colour.BLUE_GREY)),
	GREY_40_PERCENT(55, getColor(Colour.GREY_40_PERCENT)),
	DARK_TEAL(56, getColor(Colour.DARK_TEAL)),
	SEA_GREEN(57, getColor(Colour.SEA_GREEN)),
	DARK_GREEN(58, getColor(Colour.DARK_GREEN)),
	OLIVE_GREEN(59, getColor(Colour.OLIVE_GREEN)),
	BROWN(60, getColor(Colour.BROWN)),
	PLUM(61, getColor(Colour.PLUM)),
	INDIGO(62, getColor(Colour.INDIGO)),
	GREY_80_PERCENT(63, getColor(Colour.GREY_80_PERCENT)),
    MC_BLUE(40, new java.awt.Color(82, 123, 189)),
    DEFAULT(8, getColor(Colour.BLACK));

    public final short index;
    public final java.awt.Color awtColor;

    private Color_t(int index, java.awt.Color awtColor)
    {
        this.index = (short) index;
        this.awtColor = awtColor;
    }
    
    private static java.awt.Color getColor(Colour c)
    {
        RGB rgb = c.getDefaultRGB();
        return(new java.awt.Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue()));
    }

}
