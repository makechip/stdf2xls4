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

package com.makechip.stdf2xls4.xls;

import jxl.format.Colour;

public enum Color_t
{
    BLACK(Colour.BLACK),
    WHITE(Colour.WHITE),
    RED(Colour.RED),
    BRIGHT_GREEN(Colour.BRIGHT_GREEN),
    BLUE(Colour.BLUE),
    YELLOW(Colour.YELLOW),
    PINK(Colour.PINK),
    TURQUOISE(Colour.TURQUOISE),
    DARK_RED(Colour.DARK_RED),
    GREEN(Colour.GREEN),
    DARK_BLUE(Colour.DARK_BLUE),
    DARK_YELLOW(Colour.DARK_YELLOW),
    VIOLET(Colour.VIOLET),
    TEAL(Colour.TEAL),
    GREY_25_PERCENT(Colour.GREY_25_PERCENT),
    GREY_50_PERCENT(Colour.GREY_50_PERCENT),
    PERIWINKLE(Colour.PERIWINKLE),
    PLUM2(Colour.PLUM2),
    IVORY(Colour.IVORY),
    LIGHT_TURQUOISE2(Colour.LIGHT_TURQUOISE2),
    DARK_PURPLE(Colour.DARK_PURPLE),
    CORAL(Colour.CORAL),
    OCEAN_BLUE(Colour.OCEAN_BLUE),
    ICE_BLUE(Colour.ICE_BLUE),
    DARK_BLUE2(Colour.DARK_BLUE2),
    PINK2(Colour.PINK2),
    YELLOW2(Colour.YELLOW2),
    TURQOISE2(Colour.TURQOISE2),
    VIOLET2(Colour.VIOLET2),
    DARK_RED2(Colour.DARK_RED2),
    TEAL2(Colour.TEAL2),
    BLUE2(Colour.BLUE2),
    SKY_BLUE(Colour.SKY_BLUE), // Note: this is overwritten to a custom color.
    LIGHT_TURQUOISE(Colour.LIGHT_TURQUOISE),
    LIGHT_GREEN(Colour.LIGHT_GREEN),
    VERY_LIGHT_YELLOW(Colour.VERY_LIGHT_YELLOW),
    PALE_BLUE(Colour.PALE_BLUE),
    ROSE(Colour.ROSE),
    LAVENDER(Colour.LAVENDER),
    TAN(Colour.TAN),
    LIGHT_BLUE(Colour.LIGHT_BLUE),
    AQUA(Colour.AQUA),
    LIME(Colour.LIME),
    GOLD(Colour.GOLD),
    LIGHT_ORANGE(Colour.LIGHT_ORANGE),
    ORANGE(Colour.ORANGE),
    BLUE_GREY(Colour.BLUE_GREY),
    GREY_40_PERCENT(Colour.GREY_40_PERCENT),
    DARK_TEAL(Colour.DARK_TEAL),
    SEA_GREEN(Colour.SEA_GREEN),
    DARK_GREEN(Colour.DARK_GREEN),
    OLIVE_GREEN(Colour.OLIVE_GREEN),
    BROWN(Colour.BROWN),
    PLUM(Colour.PLUM),
    INDIGO(Colour.INDIGO),
    GREY_80_PERCENT(Colour.GREY_80_PERCENT),
    MC_BLUE(Colour.SKY_BLUE),
    DEFAULT(Colour.WHITE);

    public final Colour color;

    private Color_t(Colour color)
    {
        this.color = color;
    }

}
