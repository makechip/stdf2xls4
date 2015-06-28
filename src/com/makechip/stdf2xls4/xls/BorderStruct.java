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

public class BorderStruct
{
    private static final Color_t DEFCLR = Color_t.GREY_80_PERCENT;
    private static final BorderStyle_t DEFAULT = BorderStyle_t.DEFAULT;
    private static EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>>>> structs = 
                new EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>>>>(Color_t.class);
    private Color_t       topBorderColor     = Color_t.BLACK;         // 0 - 55
    private BorderStyle_t topBorderStyle     = BorderStyle_t.DEFAULT; // 0 - 13
    private Color_t       rightBorderColor   = Color_t.BLACK;
    private BorderStyle_t rightBorderStyle   = BorderStyle_t.DEFAULT;
    private Color_t       leftBorderColor    = Color_t.BLACK;
    private BorderStyle_t leftBorderStyle    = BorderStyle_t.DEFAULT;
    private Color_t       bottomBorderColor  = Color_t.BLACK;
    private BorderStyle_t bottomBorderStyle  = BorderStyle_t.DEFAULT;

    public static final BorderStruct DEFAULT_BORDER = BorderFactory.getBorderStruct(BorderFactory.getBorderHandle(DEFCLR.ordinal(), 
                                                                                                                  DEFAULT.ordinal(), 
                                                                                                                  DEFCLR.ordinal(), 
                                                                                                                  DEFAULT.ordinal(), 
                                                                                                                  DEFCLR.ordinal(), 
                                                                                                                  DEFAULT.ordinal(), 
                                                                                                                  DEFCLR.ordinal(), 
                                                                                                                  DEFAULT.ordinal()));

    public Color_t getTopBorderColor()    { return(topBorderColor);    }
    public Color_t getRightBorderColor()  { return(rightBorderColor);  }
    public Color_t getLeftBorderColor()   { return(leftBorderColor);   }
    public Color_t getBottomBorderColor() { return(bottomBorderColor); }
    public BorderStyle_t getTopBorderStyle()    { return(topBorderStyle);    }
    public BorderStyle_t getRightBorderStyle()  { return(rightBorderStyle);  }
    public BorderStyle_t getLeftBorderStyle()   { return(leftBorderStyle);   }
    public BorderStyle_t getBottomBorderStyle() { return(bottomBorderStyle); }

    private BorderStruct(Color_t tbc, BorderStyle_t tbs, Color_t rbc, BorderStyle_t rbs, Color_t lbc, BorderStyle_t lbs, Color_t bbc, BorderStyle_t bbs)
    {
        topBorderColor = tbc;
        topBorderStyle = tbs;
        rightBorderColor = rbc;
        rightBorderStyle = rbs;
        leftBorderColor = lbc;
        leftBorderStyle = lbs;
        bottomBorderColor = bbc;
        bottomBorderStyle = bbs;
    }

    public static BorderStruct getTopBorderStruct(Color_t tbc, BorderStyle_t tbs)
    {
        return(getBorderStruct(tbc, tbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderStruct getRightBorderStruct(Color_t rbc, BorderStyle_t rbs)
    {
        return(getBorderStruct(DEFCLR, DEFAULT, rbc, rbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderStruct getLeftBorderStruct(Color_t lbc, BorderStyle_t lbs)
    {
        return(getBorderStruct(DEFCLR, DEFAULT, DEFCLR, DEFAULT, lbc, lbs, DEFCLR, DEFAULT));
    }

    public static BorderStruct getBottomBorderStruct(Color_t bbc, BorderStyle_t bbs)
    {
        return(getBorderStruct(DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT, bbc, bbs));
    }

    public static BorderStruct getLeftTopBorderStruct(Color_t lbc, BorderStyle_t lbs, Color_t tbc, BorderStyle_t tbs)
    {
        return(getBorderStruct(tbc, tbs, DEFCLR, DEFAULT, lbc, lbs, DEFCLR, DEFAULT));
    }

    public static BorderStruct getTopRightBorderStruct(Color_t tbc, BorderStyle_t tbs, Color_t rbc, BorderStyle_t rbs)
    {
        return(getBorderStruct(tbc, tbs, rbc, rbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderStruct getRightBottomBorderStruct(Color_t rbc, BorderStyle_t rbs, Color_t bbc, BorderStyle_t bbs)
    {
        return(getBorderStruct(DEFCLR, DEFAULT, rbc, rbs, DEFCLR, DEFAULT, bbc, bbs));
    }

    public static BorderStruct getBottomLeftBorderStruct(Color_t bbc, BorderStyle_t bbs, Color_t lbc, BorderStyle_t lbs)
    {
        return(getBorderStruct(DEFCLR, DEFAULT, DEFCLR, DEFAULT, lbc, lbs, bbc, bbs));
    }

    public static BorderStruct getBorderStruct(Color_t tbc, 
                                               BorderStyle_t tbs, 
                                               Color_t rbc, 
                                               BorderStyle_t rbs, 
                                               Color_t lbc, 
                                               BorderStyle_t lbs, 
                                               Color_t bbc, 
                                               BorderStyle_t bbs)
    {
        EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>>> m1 = structs.get(tbc);
        if (m1 == null)
        {
            m1 = new EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>>>(BorderStyle_t.class); 
            structs.put(tbc, m1);
        }
        EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>> m2 = m1.get(tbs);
        if (m2 == null)
        {
            m2 = new EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>>(Color_t.class);
            m1.put(tbs, m2);
        }
        EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>> m3 = m2.get(rbc);
        if (m3 == null)
        {
            m3 = new EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>>(BorderStyle_t.class);
            m2.put(rbc, m3);
        }
        EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>> m4 = m3.get(rbs);
        if (m4 == null)
        {
            m4 = new EnumMap<Color_t, EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>>(Color_t.class);
            m3.put(rbs, m4);
        }
        EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>> m5 = m4.get(lbc);
        if (m5 == null)
        {
            m5 = new EnumMap<BorderStyle_t, EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>>(BorderStyle_t.class);
            m4.put(lbc, m5);
        }
        EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>> m6 = m5.get(lbs);
        if (m6 == null)
        {
            m6 = new EnumMap<Color_t, EnumMap<BorderStyle_t, BorderStruct>>(Color_t.class);
            m5.put(lbs, m6);
        }
        EnumMap<BorderStyle_t, BorderStruct> m7 = m6.get(bbc);
        if (m7 == null)
        {
            m7 = new EnumMap<BorderStyle_t, BorderStruct>(BorderStyle_t.class);
            m6.put(bbc, m7);
        }
        BorderStruct bs = (BorderStruct) m7.get(bbs);
        if (bs == null)
        {
            bs = new BorderStruct(tbc, tbs, rbc, rbs, lbc, lbs, bbc, bbs);
            m7.put(bbs, bs);
        }
        return(bs);
    }

}
