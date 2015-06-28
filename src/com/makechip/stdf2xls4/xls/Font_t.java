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

import jxl.write.*;

public enum Font_t
{
    ARIAL(WritableFont.ARIAL),
    COURIER(WritableFont.COURIER),
    TAHOMA(WritableFont.TAHOMA),
    TIMES(WritableFont.TIMES);

    private WritableFont.FontName font;

    private Font_t(WritableFont.FontName font)
    {
        this.font = font;
    }

    public WritableFont.FontName getFont() { return(font); }

    public static Font_t getFont(int ordinal)
    {
        switch (ordinal)
        {
            case 0:  return(ARIAL);
            case 1:  return(COURIER);
            case 2:  return(TAHOMA);
            case 3:  return(TIMES);
            default:
        }
        return(null);
    }

}
