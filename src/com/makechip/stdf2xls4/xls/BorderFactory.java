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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.*;

public class BorderFactory
{
    private static int brdInst = 0;
    private static TIntObjectHashMap<BorderStruct> borders = new TIntObjectHashMap<BorderStruct>();
    private static IdentityHashMap<BorderStruct, Integer> handles = new IdentityHashMap<BorderStruct, Integer>();


    public static BorderStruct getBorderStruct(int borderHandle)
    {
        BorderStruct bs = (BorderStruct) BorderFactory.borders.get(new Integer(borderHandle));
        if (bs == null) throw new RuntimeException("Invalid border handle");
        return(bs);
    }

    public static int getBorderHandle(BorderStruct bs)
    {
        Integer handle = (Integer) handles.get(bs);
        if (handle == null) throw new RuntimeException("Unknown border structure");
        return(handle.intValue());
    }
    
    public static int getTopBorderHandle(int topBorderColor, int topBorderStyle)
    {
        BorderStruct bs = BorderStruct.getTopBorderStruct(Color_t.getColor(topBorderColor), BorderStyle_t.getStyle(topBorderStyle));
        return(getHandle(bs));
    }

    public static int getRightBorderHandle(int rightBorderColor, int rightBorderStyle)
    {
        BorderStruct bs = BorderStruct.getRightBorderStruct(Color_t.getColor(rightBorderColor), BorderStyle_t.getStyle(rightBorderStyle));
        return(getHandle(bs));
    }

    public static int getLeftBorderHandle(int leftBorderColor, int leftBorderStyle)
    {
        BorderStruct bs = BorderStruct.getLeftBorderStruct(Color_t.getColor(leftBorderColor), BorderStyle_t.getStyle(leftBorderStyle));
        return(getHandle(bs));
    }

    public static int getBottomBorderHandle(int bottomBorderColor, int bottomBorderStyle)
    {
        BorderStruct bs = BorderStruct.getBottomBorderStruct(Color_t.getColor(bottomBorderColor), BorderStyle_t.getStyle(bottomBorderStyle));
        return(getHandle(bs));
    }

    public static int getLeftTopBorderHandle(int leftBorderColor, int leftBorderStyle, int topBorderColor, int topBorderStyle)
    {
        BorderStruct bs = BorderStruct.getLeftTopBorderStruct(Color_t.getColor(leftBorderColor), 
                                                              BorderStyle_t.getStyle(leftBorderStyle), 
                                                              Color_t.getColor(topBorderColor), 
                                                              BorderStyle_t.getStyle(topBorderStyle));
        return(getHandle(bs));
    }

    public static int getTopRightBorderHandle(int topBorderColor, int topBorderStyle, int rightBorderColor, int rightBorderStyle)
    {
        BorderStruct bs = BorderStruct.getTopRightBorderStruct(Color_t.getColor(topBorderColor), 
                                                               BorderStyle_t.getStyle(topBorderStyle), 
                                                               Color_t.getColor(rightBorderColor), 
                                                               BorderStyle_t.getStyle(rightBorderStyle));
        return(getHandle(bs));
    }

    public static int getRightBottomBorderHandle(int rightBorderColor, int rightBorderStyle, int bottomBorderColor, int bottomBorderStyle)
    {
        BorderStruct bs = BorderStruct.getRightBottomBorderStruct(Color_t.getColor(rightBorderColor), 
                                                                  BorderStyle_t.getStyle(rightBorderStyle), 
                                                                  Color_t.getColor(bottomBorderColor), 
                                                                  BorderStyle_t.getStyle(bottomBorderStyle));
        return(getHandle(bs));
    }

    public static int getBottomLeftBorderHandle(int bottomBorderColor, int bottomBorderStyle, int leftBorderColor, int leftBorderStyle)
    {
        BorderStruct bs = BorderStruct.getBottomLeftBorderStruct(Color_t.getColor(bottomBorderColor), 
                                                                 BorderStyle_t.getStyle(bottomBorderStyle), 
                                                                 Color_t.getColor(leftBorderColor), 
                                                                 BorderStyle_t.getStyle(leftBorderStyle));
        return(getHandle(bs));
    }

    private static int getHandle(BorderStruct bs)
    {
        Integer h = handles.get(bs);
        if (h == null)
        {
            h = new Integer(brdInst);
            handles.put(bs, h);
            borders.put(brdInst, bs);
            brdInst++;
        }
        return(h.intValue());
    }

    public static int getBorderHandle(int topBorderColor,
                                      int topBorderStyle,
                                      int rightBorderColor,
                                      int rightBorderStyle,
                                      int leftBorderColor,
                                      int leftBorderStyle,
                                      int bottomBorderColor,
                                      int bottomBorderStyle)
    {
        BorderStruct bs = BorderStruct.getBorderStruct(Color_t.getColor(topBorderColor), BorderStyle_t.getStyle(topBorderStyle),
                                                       Color_t.getColor(rightBorderColor), BorderStyle_t.getStyle(rightBorderStyle),
                                                       Color_t.getColor(leftBorderColor), BorderStyle_t.getStyle(leftBorderStyle),
                                                       Color_t.getColor(bottomBorderColor), BorderStyle_t.getStyle(bottomBorderStyle));
        return(getHandle(bs));
    }

}
