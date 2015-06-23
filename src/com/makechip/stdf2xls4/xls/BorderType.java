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

import java.util.EnumMap;

import org.apache.poi.ss.usermodel.IndexedColors;

import com.makechip.util.Log;

public class BorderType
{
    private static final IndexedColors DEFCLR = IndexedColors.GREY_80_PERCENT;
    private static final BorderStyle_t DEFAULT = BorderStyle_t.DEFAULT;
    private static EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>>>> Types = 
                new EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>>>>(IndexedColors.class);
    private IndexedColors topBorderColor     = IndexedColors.BLACK;         // 0 - 55
    private BorderStyle_t topBorderStyle     = BorderStyle_t.DEFAULT; // 0 - 13
    private IndexedColors rightBorderColor   = IndexedColors.BLACK;
    private BorderStyle_t rightBorderStyle   = BorderStyle_t.DEFAULT;
    private IndexedColors leftBorderColor    = IndexedColors.BLACK;
    private BorderStyle_t leftBorderStyle    = BorderStyle_t.DEFAULT;
    private IndexedColors bottomBorderColor  = IndexedColors.BLACK;
    private BorderStyle_t bottomBorderStyle  = BorderStyle_t.DEFAULT;

    public static final BorderType DEFAULT_BORDER = getBorderType(DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT);
    
    public static void main(String[] args)
    {
    	Log.msg("default = " + DEFAULT_BORDER);
    }

    public IndexedColors getTopBorderColor()    { return(topBorderColor);    }
    public IndexedColors getRightBorderColor()  { return(rightBorderColor);  }
    public IndexedColors getLeftBorderColor()   { return(leftBorderColor);   }
    public IndexedColors getBottomBorderColor() { return(bottomBorderColor); }
    public BorderStyle_t getTopBorderStyle()    { return(topBorderStyle);    }
    public BorderStyle_t getRightBorderStyle()  { return(rightBorderStyle);  }
    public BorderStyle_t getLeftBorderStyle()   { return(leftBorderStyle);   }
    public BorderStyle_t getBottomBorderStyle() { return(bottomBorderStyle); }

    private BorderType(IndexedColors tbc, BorderStyle_t tbs, IndexedColors rbc, BorderStyle_t rbs, IndexedColors lbc, BorderStyle_t lbs, IndexedColors bbc, BorderStyle_t bbs)
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

    public static BorderType getTopBorderType(IndexedColors tbc, BorderStyle_t tbs)
    {
        return(getBorderType(tbc, tbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderType getRightBorderType(IndexedColors rbc, BorderStyle_t rbs)
    {
        return(getBorderType(DEFCLR, DEFAULT, rbc, rbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderType getLeftBorderType(IndexedColors lbc, BorderStyle_t lbs)
    {
        return(getBorderType(DEFCLR, DEFAULT, DEFCLR, DEFAULT, lbc, lbs, DEFCLR, DEFAULT));
    }

    public static BorderType getBottomBorderType(IndexedColors bbc, BorderStyle_t bbs)
    {
        return(getBorderType(DEFCLR, DEFAULT, DEFCLR, DEFAULT, DEFCLR, DEFAULT, bbc, bbs));
    }

    public static BorderType getLeftTopBorderType(IndexedColors lbc, BorderStyle_t lbs, IndexedColors tbc, BorderStyle_t tbs)
    {
        return(getBorderType(tbc, tbs, DEFCLR, DEFAULT, lbc, lbs, DEFCLR, DEFAULT));
    }

    public static BorderType getTopRightBorderType(IndexedColors tbc, BorderStyle_t tbs, IndexedColors rbc, BorderStyle_t rbs)
    {
        return(getBorderType(tbc, tbs, rbc, rbs, DEFCLR, DEFAULT, DEFCLR, DEFAULT));
    }

    public static BorderType getRightBottomBorderType(IndexedColors rbc, BorderStyle_t rbs, IndexedColors bbc, BorderStyle_t bbs)
    {
        return(getBorderType(DEFCLR, DEFAULT, rbc, rbs, DEFCLR, DEFAULT, bbc, bbs));
    }

    public static BorderType getBottomLeftBorderType(IndexedColors bbc, BorderStyle_t bbs, IndexedColors lbc, BorderStyle_t lbs)
    {
        return(getBorderType(DEFCLR, DEFAULT, DEFCLR, DEFAULT, lbc, lbs, bbc, bbs));
    }

    public static BorderType getBorderType(IndexedColors tbc, 
                                           BorderStyle_t tbs, 
                                           IndexedColors rbc, 
                                           BorderStyle_t rbs, 
                                           IndexedColors lbc, 
                                           BorderStyle_t lbs, 
                                           IndexedColors bbc, 
                                           BorderStyle_t bbs)
    {
        EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>>> m1 = Types.get(tbc);
        if (m1 == null)
        {
            m1 = new EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>>>(BorderStyle_t.class); 
            Types.put(tbc, m1);
        }
        EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>> m2 = m1.get(tbs);
        if (m2 == null)
        {
            m2 = new EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>>(IndexedColors.class);
            m1.put(tbs, m2);
        }
        EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>> m3 = m2.get(rbc);
        if (m3 == null)
        {
            m3 = new EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>>(BorderStyle_t.class);
            m2.put(rbc, m3);
        }
        EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>> m4 = m3.get(rbs);
        if (m4 == null)
        {
            m4 = new EnumMap<IndexedColors, EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>>(IndexedColors.class);
            m3.put(rbs, m4);
        }
        EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>> m5 = m4.get(lbc);
        if (m5 == null)
        {
            m5 = new EnumMap<BorderStyle_t, EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>>(BorderStyle_t.class);
            m4.put(lbc, m5);
        }
        EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>> m6 = m5.get(lbs);
        if (m6 == null)
        {
            m6 = new EnumMap<IndexedColors, EnumMap<BorderStyle_t, BorderType>>(IndexedColors.class);
            m5.put(lbs, m6);
        }
        EnumMap<BorderStyle_t, BorderType> m7 = m6.get(bbc);
        if (m7 == null)
        {
            m7 = new EnumMap<BorderStyle_t, BorderType>(BorderStyle_t.class);
            m6.put(bbc, m7);
        }
        BorderType bs = m7.get(bbs);
        if (bs == null)
        {
            bs = new BorderType(tbc, tbs, rbc, rbs, lbc, lbs, bbc, bbs);
            m7.put(bbs, bs);
        }
        return(bs);
    }

}
