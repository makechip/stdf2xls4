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

import jxl.format.BorderLineStyle;

public enum BorderStyle_t
{
    NONE(BorderLineStyle.NONE),
    DASH_DOT(BorderLineStyle.DASH_DOT),
    DASH_DOT_DOT(BorderLineStyle.DASH_DOT_DOT),
    DASHED(BorderLineStyle.DASHED),
    DOTTED(BorderLineStyle.DOTTED),
    DOUBLE(BorderLineStyle.DOUBLE),
    HAIR(BorderLineStyle.HAIR),
    MEDIUM(BorderLineStyle.MEDIUM),
    MEDIUM_DASH_DOT(BorderLineStyle.MEDIUM_DASH_DOT),
    MEDIUM_DASH_DOT_DOT(BorderLineStyle.MEDIUM_DASH_DOT_DOT),
    MEDIUM_DASHED(BorderLineStyle.MEDIUM_DASHED),
    SLANTED_DASH_DOT(BorderLineStyle.SLANTED_DASH_DOT),
    THICK(BorderLineStyle.THICK),
    THIN(BorderLineStyle.THIN),
    DEFAULT(BorderLineStyle.THIN);

    private BorderLineStyle style;

    private BorderStyle_t(BorderLineStyle style)
    {
        this.style = style;
    }

    public BorderLineStyle getStyle() { return(style); }

    public static BorderStyle_t getStyle(int ordinal)
    {
        switch (ordinal)
        {
            case  0: return(NONE);
            case  1: return(DASH_DOT);
            case  2: return(DASH_DOT_DOT);
            case  3: return(DASHED);
            case  4: return(DOTTED);
            case  5: return(DOUBLE);
            case  6: return(HAIR);
            case  7: return(MEDIUM);
            case  8: return(MEDIUM_DASH_DOT);
            case  9: return(MEDIUM_DASH_DOT_DOT);
            case 10: return(MEDIUM_DASHED);
            case 11: return(SLANTED_DASH_DOT);
            case 12: return(THICK);
            case 13: return(THIN);
            case 14: return(DEFAULT);
            default:
        }
        return(DEFAULT);
    }

}
