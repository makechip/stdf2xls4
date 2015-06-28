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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;

public final class FormatStruct
{
    private static EnumMap<Alignment_t, EnumMap<Color_t, EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>>>> map = 
            new EnumMap<Alignment_t, EnumMap<Color_t, EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>>>>(Alignment_t.class);

    private Alignment_t alignment            = Alignment_t.DEFAULT; // 0 - 3
    private Color_t background               = Color_t.DEFAULT;     // 0 - 55
    private Color_t fontColor                = Color_t.BLACK;       // 0 - 55
    private FontStyle_t fontStyle            = FontStyle_t.NORMAL; // 0 - 7
    private Font_t font                      = Font_t.ARIAL;      // 0 - 3
    private BorderStruct border              = null;
    private String numberFormat              = "";
    private int fontSize                     = 10;


    private FormatStruct(Alignment_t alignment,
                         Color_t background,
                         Color_t fontColor,
                         FontStyle_t fontStyle,
                         Font_t font,
                         String numberFormat,
                         BorderStruct border,
                         int fontSize)
    {
        this.alignment = alignment;
        this.background = background;
        this.fontColor = fontColor;
        this.fontStyle = fontStyle;
        this.font = font;
        this.numberFormat = numberFormat;
        this.border = border;
        this.fontSize = fontSize;
    }

    public Alignment_t getAlignment() { return(alignment); }
    public Color_t getBackground() { return(background); }
    public Color_t getFontColor() { return(fontColor); }
    public FontStyle_t getFontStyle() { return(fontStyle); }
    public Font_t getFont() { return(font); }
    public BorderStruct getBorderStruct() { return(border); }
    public String getNumberFormat() { return(numberFormat); }
    public int getFontSize() { return(fontSize); }

    public static FormatStruct getFormatStruct(Alignment_t alignment, 
                                               Color_t background, 
                                               Color_t fontColor, 
                                               FontStyle_t fontStyle, 
                                               Font_t font, 
                                               String numberFormat, 
                                               BorderStruct border, 
                                               int fontSize)
    {
        if (numberFormat == null) numberFormat = "";
        Integer fsize = new Integer(fontSize);
        EnumMap<Color_t, EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>>> m1 = map.get(alignment);
        if (m1 == null)
        {
            m1 = new EnumMap<Color_t, EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>>>(Color_t.class);
            map.put(alignment, m1);
        }
        EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>> m2 = m1.get(background);
        if (m2 == null)
        {
            m2 = new EnumMap<Color_t, EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>>(Color_t.class);
            m1.put(background, m2);
        }
        EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>> m3 = m2.get(fontColor);
        if (m3 == null)
        {
            m3 = new EnumMap<FontStyle_t, EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>>(FontStyle_t.class);
            m2.put(fontColor, m3);
        }
        EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>> m4 = m3.get(fontStyle);
        if (m4 == null)
        {
            m4 = new EnumMap<Font_t, HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>>(Font_t.class);
            m3.put(fontStyle, m4);
        }
        HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>> m5 = m4.get(font);
        if (m5 == null)
        {
            m5 = new HashMap<String, IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>>();
            m4.put(font, m5);
        }
        IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>> m6 = m5.get(numberFormat);
        if (m6 == null)
        {
            m6 = new IdentityHashMap<BorderStruct, HashMap<Integer, FormatStruct>>();
            m5.put(numberFormat, m6);
        }
        HashMap<Integer, FormatStruct> m7 = m6.get(border);
        if (m7 == null)
        {
            m7 = new HashMap<Integer, FormatStruct>();
            m6.put(border, m7);
        }
        FormatStruct fs = m7.get(fontSize);
        if (fs == null)
        {
            fs = new FormatStruct(alignment, background, fontColor, fontStyle, font, numberFormat, border, fontSize);
            m7.put(fsize, fs);
        }
        return(fs);
    }


}
