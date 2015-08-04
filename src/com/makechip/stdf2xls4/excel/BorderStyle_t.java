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

    private final BorderLineStyle style;

    private BorderStyle_t(BorderLineStyle style)
    {
        this.style = style;
    }
    
    public BorderLineStyle getBorderStyle() { return(style); }

}
