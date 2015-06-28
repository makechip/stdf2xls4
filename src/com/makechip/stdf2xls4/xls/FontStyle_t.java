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
import jxl.format.UnderlineStyle;

public enum FontStyle_t
{
    NORMAL,               
    BOLD,                 
    ITALIC,               
    UNDERLINE,            
    BOLD_ITALIC,          
    BOLD_UNDERLINE,       
    BOLD_ITALIC_UNDERLINE,
    ITALIC_UNDERLINE;     

    public static FontStyle_t getFontStyle(int ordinal)
    {
        switch (ordinal)
        {
            case 0: return(NORMAL);
            case 1: return(BOLD);
            case 2: return(ITALIC);
            case 3: return(UNDERLINE);
            case 4: return(BOLD_ITALIC);
            case 5: return(BOLD_UNDERLINE);
            case 6: return(BOLD_ITALIC_UNDERLINE);
            case 7: return(ITALIC_UNDERLINE);
            default:
        }
        return(null);
    }

    public WritableFont createFont(Font_t f, int size)
    {
        WritableFont font = null;
        switch (ordinal())
        {
            case 0:  font = new WritableFont(f.getFont(), size, WritableFont.NO_BOLD); break;
            case 1:  font = new WritableFont(f.getFont(), size, WritableFont.BOLD); break;
            case 2:  font = new WritableFont(f.getFont(), size, WritableFont.NO_BOLD, true); break;
            case 3:  font = new WritableFont(f.getFont(), size, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE); break;
            case 4:  font = new WritableFont(f.getFont(), size, WritableFont.BOLD, true); break;
            case 5:  font = new WritableFont(f.getFont(), size, WritableFont.BOLD, false, UnderlineStyle.SINGLE); break;
            case 6:  font = new WritableFont(f.getFont(), size, WritableFont.BOLD, true, UnderlineStyle.SINGLE); break;
            default: font = new WritableFont(f.getFont(), size, WritableFont.NO_BOLD, true, UnderlineStyle.SINGLE); break;
        }
        return(font);
    }

    public WritableFont createFont(Font_t f, int size, Color_t color)
    {
        WritableFont font = createFont(f, size);
        try { font.setColour(color.getColor()); }
        catch (Exception e) { throw new RuntimeException("Invalid Colour"); }
        return(font);
    }
}
