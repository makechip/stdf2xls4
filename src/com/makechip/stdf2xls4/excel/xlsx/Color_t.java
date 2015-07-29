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

package com.makechip.stdf2xls4.excel.xlsx;

import org.apache.poi.xssf.usermodel.XSSFColor;

public enum Color_t
{
    BLACK(new XSSFColor(java.awt.Color.BLACK), 8),
    WHITE(new XSSFColor(java.awt.Color.WHITE), 9),
    YELLOW(new XSSFColor(new java.awt.Color(0xff, 0xff,0)), 13),
	RED               (new XSSFColor(new java.awt.Color(0xff,0,0)), 10),
	BRIGHT_GREEN      (new XSSFColor(new java.awt.Color(0,0xff,0)), 11),
	BLUE              (new XSSFColor(new java.awt.Color(0xff,0xff,0)), 12),
	PINK              (new XSSFColor(new java.awt.Color(0xff,0,0xff)), 14),
	TURQUOISE         (new XSSFColor(new java.awt.Color(0,0xff,0xff)), 15),
	DARK_RED          (new XSSFColor(new java.awt.Color(0x80,0,0)), 16),
	GREEN             (new XSSFColor(new java.awt.Color(0,0x80,0)), 17),
	DARK_BLUE         (new XSSFColor(new java.awt.Color(0,0,0x80)), 18),
	DARK_YELLOW       (new XSSFColor(new java.awt.Color(0x80,0x80,0)), 19),
	VIOLET            (new XSSFColor(new java.awt.Color(0x80,0x80,0)), 20),
	TEAL              (new XSSFColor(new java.awt.Color(0,0x80,0x80)), 21),
	GREY_25_PERCENT   (new XSSFColor(new java.awt.Color(0xc0,0xc0,0xc0)), 22),
	GREY_50_PERCENT   (new XSSFColor(new java.awt.Color(0x80,0x80,0x80)), 23),
	PERIWINKLE        (new XSSFColor(new java.awt.Color(0x99, 0x99, 0xff)), 24),
	PLUM2             (new XSSFColor(new java.awt.Color(0x99, 0x33, 0x66)), 25),
	IVORY             (new XSSFColor(new java.awt.Color(0xff, 0xff, 0xcc)), 26),
	LIGHT_TURQUOISE2  (new XSSFColor(new java.awt.Color(0xcc, 0xff, 0xff)), 27),
	DARK_PURPLE       (new XSSFColor(new java.awt.Color(0x66, 0x0, 0x66)), 28),
	CORAL             (new XSSFColor(new java.awt.Color(0xff, 0x80, 0x80)), 29),
	OCEAN_BLUE        (new XSSFColor(new java.awt.Color(0x0, 0x66, 0xcc)), 30),
	ICE_BLUE          (new XSSFColor(new java.awt.Color(0xcc, 0xcc, 0xff)), 31),
	DARK_BLUE2        (new XSSFColor(new java.awt.Color(0,0,0x80)), 32),
	PINK2             (new XSSFColor(new java.awt.Color(0xff,0,0xff)), 33),
	YELLOW2           (new XSSFColor(new java.awt.Color(0xff,0xff,0x0)), 34),
	TURQOISE2         (new XSSFColor(new java.awt.Color(0x0,0xff,0xff)), 35),
	VIOLET2           (new XSSFColor(new java.awt.Color(0x80,0x0,0x80)), 36),
	DARK_RED2         (new XSSFColor(new java.awt.Color(0x80,0x0,0x0)), 37),
	TEAL2             (new XSSFColor(new java.awt.Color(0x0,0x80,0x80)), 38),
	BLUE2             (new XSSFColor(new java.awt.Color(0x0,0x0,0xff)), 39),
	SKY_BLUE          (new XSSFColor(new java.awt.Color(0,0xcc,0xff)), 40),
	LIGHT_TURQUOISE   (new XSSFColor(new java.awt.Color(0xcc,0xff,0xff)), 41),
	LIGHT_GREEN       (new XSSFColor(new java.awt.Color(0xcc,0xff,0xcc)), 42),
	VERY_LIGHT_YELLOW (new XSSFColor(new java.awt.Color(0xff,0xff,0x99)), 43),
	PALE_BLUE         (new XSSFColor(new java.awt.Color(0x99,0xcc,0xff)), 44),
	ROSE              (new XSSFColor(new java.awt.Color(0xff,0x99,0xcc)), 45),
	LAVENDER          (new XSSFColor(new java.awt.Color(0xcc,0x99,0xff)), 46),
	TAN               (new XSSFColor(new java.awt.Color(0xff,0xcc,0x99)), 47),
	LIGHT_BLUE        (new XSSFColor(new java.awt.Color( 0x33, 0x66, 0xff)), 48),
	AQUA              (new XSSFColor(new java.awt.Color(0x33,0xcc,0xcc)), 49),
	LIME              (new XSSFColor(new java.awt.Color(0x99,0xcc,0)), 50),
	GOLD              (new XSSFColor(new java.awt.Color(0xff,0xcc,0)), 51),
	LIGHT_ORANGE      (new XSSFColor(new java.awt.Color(0xff,0x99,0)), 52),
	ORANGE            (new XSSFColor(new java.awt.Color(0xff,0x66,0)), 53),
	BLUE_GREY         (new XSSFColor(new java.awt.Color(0x66,0x66,0xcc)), 54),
	GREY_40_PERCENT   (new XSSFColor(new java.awt.Color(0x96,0x96,0x96)), 55),
	DARK_TEAL         (new XSSFColor(new java.awt.Color(0,0x33,0x66)), 56),
	SEA_GREEN         (new XSSFColor(new java.awt.Color(0x33,0x99,0x66)), 57),
	DARK_GREEN        (new XSSFColor(new java.awt.Color(0,0x33,0)), 58),
	OLIVE_GREEN       (new XSSFColor(new java.awt.Color(0x33,0x33,0)), 59),
	BROWN             (new XSSFColor(new java.awt.Color(0x99,0x33,0)), 60),
	PLUM              (new XSSFColor(new java.awt.Color(0x99,0x33,0x66)), 61),
	INDIGO            (new XSSFColor(new java.awt.Color(0x33,0x33,0x99)), 62),
	GREY_80_PERCENT   (new XSSFColor(new java.awt.Color(0x33,0x33,0x33)), 63),
    MC_BLUE(new XSSFColor(new java.awt.Color(65, 112, 233)), 65),
    DEFAULT(new XSSFColor(java.awt.Color.BLACK), 8);

    public final XSSFColor color;
    public final short index;

    private Color_t(XSSFColor color, int index)
    {
        this.color = color;
        this.index = (short) index;
    }

}
