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

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum Font_t
{
    ARIAL_NORMAL("Arial", false, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    COURIER_NORMAL("Courier", false, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    TAHOMA_NORMAL("Tahoma", false, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    TIMES_NORMAL("Times", false, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    ARIAL_BOLD("Arial", false, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    COURIER_BOLD("Courier", false, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    TAHOMA_BOLD("Tahoma", false, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    TIMES_BOLD("Times", false, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    ARIAL_ITALIC("Arial", true, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    COURIER_ITALIC("Courier", true, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    TAHOMA_ITALIC("Tahoma", true, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    TIMES_ITALIC("Times", true, Font.BOLDWEIGHT_NORMAL, Font.U_NONE),
    ARIAL_UNDERLINE("Arial", false, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    COURIER_UNDERLINE("Courier", false, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    TAHOMA_UNDERLINE("Tahoma", false, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    TIMES_UNDERLINE("Times", false, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    ARIAL_BOLD_ITALIC("Arial", true, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    COURIER_BOLD_ITALIC("Courier", true, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    TAHOMA_BOLD_ITALIC("Tahoma", true, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    TIMES_BOLD_ITALIC("Times", true, Font.BOLDWEIGHT_BOLD, Font.U_NONE),
    ARIAL_BOLD_UNDERLINE("Arial", false, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    COURIER_BOLD_UNDERLINE("Courier", false, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    TAHOMA_BOLD_UNDERLINE("Tahoma", false, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    TIMES_BOLD_UNDERLINE("Times", false, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    ARIAL_ITALIC_UNDERLINE("Arial", true, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    COURIER_ITALIC_UNDERLINE("Courier", true, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    TAHOMA_ITALIC_UNDERLINE("Tahoma", true, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    TIMES_ITALIC_UNDERLINE("Times", true, Font.BOLDWEIGHT_NORMAL, Font.U_SINGLE),
    ARIAL_BOLD_ITALIC_UNDERLINE("Arial", true, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    COURIER_BOLD_ITALIC_UNDERLINE("Courier", true, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    TAHOMA_BOLD_ITALIC_UNDERLINE("Tahoma", true, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE),
    TIMES_BOLD_ITALIC_UNDERLINE("Times", true, Font.BOLDWEIGHT_BOLD, Font.U_SINGLE);
	public static final XSSFColor BLACK = new XSSFColor(java.awt.Color.BLACK);
    public final String fontName;
    public final boolean italic;
    public final short style;
    public final byte utype;
    private static Map<Font_t, TIntObjectHashMap<Map<XSSFColor, XSSFFont>>> cache = new EnumMap<>(Font_t.class);

    private Font_t(String fontName, boolean italic, short style, byte utype)
    {
        this.fontName = fontName;
        this.italic = italic;
        this.style = style;
        this.utype = utype;
    }
    
    public XSSFFont getFont(XSSFWorkbook wb, int size)
    {
    	TIntObjectHashMap<Map<XSSFColor, XSSFFont>> m1 = cache.get(this);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(this, m1);
    	}
    	Map<XSSFColor, XSSFFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new HashMap<>();
    		m1.put(size, m2);
    	}
    	XSSFFont f = m2.get(BLACK);
    	if (f == null)
    	{
    		f = wb.createFont();
    		f.setBoldweight(style);
            f.setColor(BLACK);
            f.setFontHeight(size);
            f.setFontName(fontName);
            f.setItalic(italic);
            f.setUnderline(utype);
    		m2.put(BLACK, f);
    	}
        return(f);
    }
    
    public XSSFFont getFont(XSSFWorkbook wb, int size, XSSFColor color)
    {
    	TIntObjectHashMap<Map<XSSFColor, XSSFFont>> m1 = cache.get(this);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(this, m1);
    	}
    	Map<XSSFColor, XSSFFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new HashMap<>();
    		m1.put(size, m2);
    	}
    	XSSFFont f = m2.get(color);
    	if (f == null)
    	{
    		f = wb.createFont();
    		f.setBoldweight(style);
            f.setColor(color);
            f.setFontHeight(size);
            f.setFontName(fontName);
            f.setItalic(italic);
            f.setUnderline(utype);
            m2.put(color, f);
    	}
        return(f);
    	
    }
}
