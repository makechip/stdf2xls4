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

package com.makechip.stdf2xls4.excel.xls;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;
import java.util.Map;

import jxl.format.UnderlineStyle;
import jxl.write.*;

public enum Font_t
{
    ARIAL_NORMAL(WritableFont.ARIAL, false, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    COURIER_NORMAL(WritableFont.COURIER, false, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    TAHOMA_NORMAL(WritableFont.TAHOMA, false, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    TIMES_NORMAL(WritableFont.TIMES, false, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    ARIAL_BOLD(WritableFont.ARIAL, false, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    COURIER_BOLD(WritableFont.COURIER, false, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    TAHOMA_BOLD(WritableFont.TAHOMA, false, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    TIMES_BOLD(WritableFont.TIMES, false, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    ARIAL_ITALIC(WritableFont.ARIAL, true, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    COURIER_ITALIC(WritableFont.COURIER, true, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    TAHOMA_ITALIC(WritableFont.TAHOMA, true, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    TIMES_ITALIC(WritableFont.TIMES, true, WritableFont.NO_BOLD, UnderlineStyle.NO_UNDERLINE),
    ARIAL_UNDERLINE(WritableFont.ARIAL, false, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    COURIER_UNDERLINE(WritableFont.COURIER, false, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    TAHOMA_UNDERLINE(WritableFont.TAHOMA, false, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    TIMES_UNDERLINE(WritableFont.TIMES, false, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    ARIAL_BOLD_ITALIC(WritableFont.ARIAL, true, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    COURIER_BOLD_ITALIC(WritableFont.COURIER, true, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    TAHOMA_BOLD_ITALIC(WritableFont.TAHOMA, true, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    TIMES_BOLD_ITALIC(WritableFont.TIMES, true, WritableFont.BOLD, UnderlineStyle.NO_UNDERLINE),
    ARIAL_BOLD_UNDERLINE(WritableFont.ARIAL, false, WritableFont.BOLD, UnderlineStyle.SINGLE),
    COURIER_BOLD_UNDERLINE(WritableFont.COURIER, false, WritableFont.BOLD, UnderlineStyle.SINGLE),
    TAHOMA_BOLD_UNDERLINE(WritableFont.TAHOMA, false, WritableFont.BOLD, UnderlineStyle.SINGLE),
    TIMES_BOLD_UNDERLINE(WritableFont.TIMES, false, WritableFont.BOLD, UnderlineStyle.SINGLE),
    ARIAL_ITALIC_UNDERLINE(WritableFont.ARIAL, true, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    COURIER_ITALIC_UNDERLINE(WritableFont.COURIER, true, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    TAHOMA_ITALIC_UNDERLINE(WritableFont.TAHOMA, true, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    TIMES_ITALIC_UNDERLINE(WritableFont.TIMES, true, WritableFont.NO_BOLD, UnderlineStyle.SINGLE),
    ARIAL_BOLD_ITALIC_UNDERLINE(WritableFont.ARIAL, true, WritableFont.BOLD, UnderlineStyle.SINGLE),
    COURIER_BOLD_ITALIC_UNDERLINE(WritableFont.COURIER, true, WritableFont.BOLD, UnderlineStyle.SINGLE),
    TAHOMA_BOLD_ITALIC_UNDERLINE(WritableFont.TAHOMA, true, WritableFont.BOLD, UnderlineStyle.SINGLE),
    TIMES_BOLD_ITALIC_UNDERLINE(WritableFont.TIMES, true, WritableFont.BOLD, UnderlineStyle.SINGLE);

    public final WritableFont.FontName fontName;
    public final boolean italic;
    public final WritableFont.BoldStyle style;
    public final UnderlineStyle utype;
    private static Map<Font_t, TIntObjectHashMap<Map<Color_t, WritableFont>>> cache = new EnumMap<>(Font_t.class);

    private Font_t(WritableFont.FontName fontName, boolean italic, WritableFont.BoldStyle style, UnderlineStyle utype)
    {
        this.fontName = fontName;
        this.italic = italic;
        this.style = style;
        this.utype = utype;
    }
    
    public WritableFont getFont(int size) throws WriteException
    {
    	TIntObjectHashMap<Map<Color_t, WritableFont>> m1 = cache.get(this);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(this, m1);
    	}
    	Map<Color_t, WritableFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	WritableFont f = m2.get(Color_t.BLACK);
    	if (f == null)
    	{
    		f = new WritableFont(fontName, size, style, italic, utype);	
            f.setColour(jxl.format.Colour.BLACK);
    		m2.put(Color_t.BLACK, f);
    	}
        return(f);
    }
    
    public WritableFont getFont(int size, Color_t color) throws WriteException
    {
    	TIntObjectHashMap<Map<Color_t, WritableFont>> m1 = cache.get(this);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(this, m1);
    	}
    	Map<Color_t, WritableFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	WritableFont f = m2.get(color);
    	if (f == null)
    	{
            f = new WritableFont(fontName, size, style, italic, utype);	
            f.setColour(color.color);
            m2.put(color, f);
    	}
        return(f);
    	
    }
}
