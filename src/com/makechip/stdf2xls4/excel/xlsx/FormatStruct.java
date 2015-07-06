/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.makechip.stdf2xls4.excel.xlsx;

import static org.apache.poi.ss.usermodel.IndexedColors.SKY_BLUE;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public final class FormatStruct
{
    private static EnumMap<HAlignment_t, EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>>>> map = 
            new EnumMap<HAlignment_t, EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>>>>(HAlignment_t.class);

    private HAlignment_t horizontalAlignment = HAlignment_t.DEFAULT;
    private VAlignment_t verticalAlignment   = VAlignment_t.DEFAULT;
    private IndexedColors background         = IndexedColors.WHITE;
    private IndexedColors fontColor          = IndexedColors.BLACK;
    private FontStyle_t fontStyle            = FontStyle_t.NORMAL;
    private FontName_t font                      = FontName_t.ARIAL;
    private BorderType border              = null;
    private String numberFormat              = "";
    private short fontSize                   = 10;


    private FormatStruct(HAlignment_t horizontalAlignment,
    		             VAlignment_t verticalAlignment,
                         IndexedColors background,
                         IndexedColors fontColor,
                         FontStyle_t fontStyle,
                         FontName_t font,
                         String numberFormat,
                         BorderType border,
                         short fontSize)
    {
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.background = background;
        this.fontColor = fontColor;
        this.fontStyle = fontStyle;
        this.font = font;
        this.numberFormat = numberFormat;
        this.border = border;
        this.fontSize = fontSize;
    }
    
    public Font getFont(Workbook wb)
    {
    	Font f = getFontStyle().createFont(wb, getFontSize(), getFontColor());
    	f.setFontName(getFont().getFont());
    	f.setColor(getFontColor().getIndex());
    	return(f);
    }
    
    public CellStyle getCellStyle(Workbook wb)
    {
    	CellStyle cs = wb.createCellStyle();
    	cs.setAlignment(getHorizontalAlignment().getAlignment());
    	cs.setVerticalAlignment(getVerticalAlignment().getAlignment());
    	if (getBackground() == SKY_BLUE) cs.setFillForegroundColor((short) 0x39);
    	else cs.setFillForegroundColor(getBackground().getIndex());
    	cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	if (getBackground() == SKY_BLUE) cs.setFillBackgroundColor((short) 0x39);
    	else cs.setFillBackgroundColor(getBackground().getIndex());
    	BorderType bs = getBorderStruct();
    	cs.setBorderBottom(bs.getBottomBorderStyle().getStyle());
    	cs.setBorderLeft(bs.getLeftBorderStyle().getStyle());
    	cs.setBorderRight(bs.getRightBorderStyle().getStyle());
    	cs.setBorderTop(bs.getTopBorderStyle().getStyle());
    	cs.setBottomBorderColor(bs.getBottomBorderColor().getIndex());
    	cs.setLeftBorderColor(bs.getLeftBorderColor().getIndex());
    	cs.setRightBorderColor(bs.getRightBorderColor().getIndex());
    	cs.setTopBorderColor(bs.getTopBorderColor().getIndex());
    	cs.setFont(getFont(wb));
    	DataFormat df = wb.createDataFormat();
    	cs.setDataFormat(df.getFormat(getNumberFormat()));
    	return(cs);
    }

    public HAlignment_t getHorizontalAlignment() { return(horizontalAlignment); }
    public VAlignment_t getVerticalAlignment() { return(verticalAlignment); }
    public IndexedColors getBackground() { return(background); }
    public IndexedColors getFontColor() { return(fontColor); }
    public FontStyle_t getFontStyle() { return(fontStyle); }
    public FontName_t getFont() { return(font); }
    public BorderType getBorderStruct() { return(border); }
    public String getNumberFormat() { return(numberFormat); }
    public short getFontSize() { return(fontSize); }

    public static FormatStruct getFormatStruct(HAlignment_t horizontalAlignment, 
    		                                   VAlignment_t verticalAlignment,
                                               IndexedColors background, 
                                               IndexedColors fontColor, 
                                               FontStyle_t fontStyle, 
                                               FontName_t font, 
                                               String numberFormat, 
                                               BorderType border, 
                                               short fontSize)
    {
        if (numberFormat == null) numberFormat = "";
        Short fsize = new Short(fontSize);
        EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>>> m1 = map.get(horizontalAlignment);
        if (m1 == null)
        {
            m1 = new EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>>>(VAlignment_t.class);
            map.put(horizontalAlignment, m1);
        }
        EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>> m2 = m1.get(verticalAlignment);
        if (m2 == null)
        {
            m2 = new EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>>(IndexedColors.class);
            m1.put(verticalAlignment, m2);
        }
        EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>> m3 = m2.get(background);
        if (m3 == null)
        {
            m3 = new EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>>(IndexedColors.class);
            m2.put(background, m3);
        }
        EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>> m4 = m3.get(fontColor);
        if (m4 == null)
        {
            m4 = new EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>>(FontStyle_t.class);
            m3.put(fontColor, m4);
        }
        EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>> m5 = m4.get(fontStyle);
        if (m5 == null)
        {
            m5 = new EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>>(FontName_t.class);
            m4.put(fontStyle, m5);
        }
        HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>> m6 = m5.get(font);
        if (m6 == null)
        {
            m6 = new HashMap<String, IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>>();
            m5.put(font, m6);
        }
        IdentityHashMap<BorderType, HashMap<Short, FormatStruct>> m7 = m6.get(numberFormat);
        if (m7 == null)
        {
            m7 = new IdentityHashMap<BorderType, HashMap<Short, FormatStruct>>();
            m6.put(numberFormat, m7);
        }
        HashMap<Short, FormatStruct> m8 = m7.get(border);
        if (m8 == null)
        {
            m8 = new HashMap<Short, FormatStruct>();
            m7.put(border, m8);
        }
        FormatStruct fs = m8.get(fontSize);
        if (fs == null)
        {
            fs = new FormatStruct(horizontalAlignment, verticalAlignment, background, fontColor, fontStyle, font, numberFormat, border, fontSize);
            m8.put(fsize, fs);
        }
        return(fs);
    }


}
