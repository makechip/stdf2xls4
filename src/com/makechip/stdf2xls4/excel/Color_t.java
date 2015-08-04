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

public enum Color_t
{
    BLACK(8),
    WHITE(9),
    YELLOW(13),
	RED(10),
	BRIGHT_GREEN(11),
	BLUE(12),
	PINK(14),
	TURQUOISE(15),
	DARK_RED(16),
	GREEN(17),
	DARK_BLUE(18),
	DARK_YELLOW(19),
	VIOLET(20),
	TEAL(21),
	GREY_25_PERCENT(22),
	GREY_50_PERCENT(23),
	PERIWINKLE(24),
	PLUM2(25),
	IVORY(26),
	LIGHT_TURQUOISE2(27),
	DARK_PURPLE(28),
	CORAL(29),
	OCEAN_BLUE(30),
	ICE_BLUE(31),
	DARK_BLUE2(32),
	PINK2(33),
	YELLOW2(34),
	TURQOISE2(35),
	VIOLET2(36),
	DARK_RED2(37),
	TEAL2(38),
	BLUE2(39),
	SKY_BLUE(40),
	LIGHT_TURQUOISE(41),
	LIGHT_GREEN(42),
	VERY_LIGHT_YELLOW(43),
	PALE_BLUE(44),
	ROSE(45),
	LAVENDER(46),
	TAN(47),
	LIGHT_BLUE(48),
	AQUA(49),
	LIME(50),
	GOLD(51),
	LIGHT_ORANGE(52),
	ORANGE(53),
	BLUE_GREY(54),
	GREY_40_PERCENT(55),
	DARK_TEAL(56),
	SEA_GREEN(57),
	DARK_GREEN(58),
	OLIVE_GREEN(59),
	BROWN(60),
	PLUM(61),
	INDIGO(62),
	GREY_80_PERCENT(63),
    MC_BLUE(65),
    DEFAULT(8);

    public final short index;

    private Color_t(int index)
    {
        this.index = (short) index;
    }

}
