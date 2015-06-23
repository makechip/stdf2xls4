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

package com.makechip.stdf2xls4.xls;

import static org.apache.poi.ss.usermodel.IndexedColors.SKY_BLUE;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public final class CellStyleType
{
    private static EnumMap<HAlignment_t, EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>>>> map = 
            new EnumMap<HAlignment_t, EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>>>>(HAlignment_t.class);
    private static IdentityHashMap<CellStyleType, CellStyle> styles = new IdentityHashMap<CellStyleType, CellStyle>();

    private final HAlignment_t horizontalAlignment;
    private final VAlignment_t verticalAlignment;
    private final IndexedColors background;
    private final IndexedColors fontColor;
    private final FontStyle_t fontStyle;
    private final FontName_t font;
    private final String numberFormat;
    private final BorderType border;
    private final short fontSize;


    private CellStyleType(HAlignment_t horizontalAlignment,
    		           VAlignment_t verticalAlignment,
                       IndexedColors background,
                       IndexedColors fontColor,
                       FontStyle_t fontStyle,
                       FontName_t font,
                       String numberFormat,
                       short fontSize,
                       BorderType border)
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
    
    private static Font getFont(Workbook wb, FontName_t name, short size, FontStyle_t style, IndexedColors color)
    {
    	Font f = FontType.getFont(wb, name, size, style, color);
    	return(f);
    }
    
    public static CellStyle getCellStyle(Workbook wb,
    									 HAlignment_t horizontalAlignment,
    									 VAlignment_t verticalAlignment,
    									 IndexedColors background,
    									 IndexedColors fontColor,
    									 FontName_t font,
    									 FontStyle_t fontStyle,
    									 String numberFormat,
    									 BorderType border,
    									 short fontSize)
    {
    	CellStyleType cst = getCellStyleType(horizontalAlignment, 
    			                             verticalAlignment, 
    			                             background, 
    			                             fontColor, 
    			                             fontStyle, 
    			                             font, 
    			                             numberFormat, 
    			                             border, 
    			                             fontSize);
    	CellStyle cs = styles.get(cst);
    	if (cs == null)
    	{
    		cs = wb.createCellStyle();
    		cs.setAlignment(horizontalAlignment.getAlignment());
    		cs.setVerticalAlignment(verticalAlignment.getAlignment());
    		if (background == SKY_BLUE) cs.setFillForegroundColor((short) 0x39);
    		else cs.setFillForegroundColor(background.getIndex());
    		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
    		if (background == SKY_BLUE) cs.setFillBackgroundColor((short) 0x39);
    		else cs.setFillBackgroundColor(background.getIndex());
    		cs.setBorderBottom(border.getBottomBorderStyle().getStyle());
    		cs.setBorderLeft(border.getLeftBorderStyle().getStyle());
    		cs.setBorderRight(border.getRightBorderStyle().getStyle());
    		cs.setBorderTop(border.getTopBorderStyle().getStyle());
    		cs.setBottomBorderColor(border.getBottomBorderColor().getIndex());
    		cs.setLeftBorderColor(border.getLeftBorderColor().getIndex());
    		cs.setRightBorderColor(border.getRightBorderColor().getIndex());
    		cs.setTopBorderColor(border.getTopBorderColor().getIndex());
    		cs.setFont(getFont(wb, font, fontSize, fontStyle, fontColor));
    		DataFormat df = wb.createDataFormat();
    		cs.setDataFormat(df.getFormat(numberFormat));
    		styles.put(cst, cs);
    	}
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

    public static CellStyleType getCellStyleType(HAlignment_t horizontalAlignment, 
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
        EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>>> m1 = map.get(horizontalAlignment);
        if (m1 == null)
        {
            m1 = new EnumMap<VAlignment_t, EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>>>(VAlignment_t.class);
            map.put(horizontalAlignment, m1);
        }
        EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>> m2 = m1.get(verticalAlignment);
        if (m2 == null)
        {
            m2 = new EnumMap<IndexedColors, EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>>(IndexedColors.class);
            m1.put(verticalAlignment, m2);
        }
        EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>> m3 = m2.get(background);
        if (m3 == null)
        {
            m3 = new EnumMap<IndexedColors, EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>>(IndexedColors.class);
            m2.put(background, m3);
        }
        EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>> m4 = m3.get(fontColor);
        if (m4 == null)
        {
            m4 = new EnumMap<FontStyle_t, EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>>(FontStyle_t.class);
            m3.put(fontColor, m4);
        }
        EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>> m5 = m4.get(fontStyle);
        if (m5 == null)
        {
            m5 = new EnumMap<FontName_t, HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>>(FontName_t.class);
            m4.put(fontStyle, m5);
        }
        HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>> m6 = m5.get(font);
        if (m6 == null)
        {
            m6 = new HashMap<String, IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>>();
            m5.put(font, m6);
        }
        IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>> m7 = m6.get(numberFormat);
        if (m7 == null)
        {
            m7 = new IdentityHashMap<BorderType, TShortObjectHashMap<CellStyleType>>();
            m6.put(numberFormat, m7);
        }
        TShortObjectHashMap<CellStyleType> m8 = m7.get(border);
        if (m8 == null)
        {
            m8 = new TShortObjectHashMap<CellStyleType>();
            m7.put(border, m8);
        }
        CellStyleType fs = m8.get(fontSize);
        if (fs == null)
        {
            fs = new CellStyleType(horizontalAlignment, verticalAlignment, background, fontColor, fontStyle, font, numberFormat, fontSize, border);
            m8.put(fontSize, fs);
        }
        return(fs);
    }


}
