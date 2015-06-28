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
import java.util.Arrays;
import java.util.IdentityHashMap;
import jxl.format.Border;
import jxl.format.VerticalAlignment;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class FormatFactory
{
    private static int fmtInst = 0;
    private static TIntObjectHashMap<WritableCellFormat> formats = new TIntObjectHashMap<WritableCellFormat>();
    private static IdentityHashMap<FormatStruct, Integer> handles = new IdentityHashMap<FormatStruct, Integer>();

    public static void reInitialize()
    {
        TIntObjectHashMap<FormatStruct> h = new TIntObjectHashMap<FormatStruct>();
        for (FormatStruct fs : handles.keySet())
        {
            int inst = handles.get(fs);
            h.put(inst, fs);
        }
        handles.clear();
        formats.clear();
        fmtInst = 0;
        int[] harr = h.keys();
        Arrays.sort(harr);
        System.out.flush();
        for (int j=0; j<harr.length; j++)
        {
            FormatStruct fs = (FormatStruct) h.get(harr[j]);
            BorderStruct bs = fs.getBorderStruct();
            int borderHandle = BorderFactory.getBorderHandle(bs);
            getFormatHandle(fs.getAlignment(), 
                            fs.getBackground(), 
                            fs.getFontSize(), 
                            fs.getFont(), 
                            fs.getFontStyle(), 
                            fs.getFontColor(), 
                            fs.getNumberFormat(), 
                            borderHandle);
        }
    }

    public static int getFormatHandle(Alignment_t alignment, 
                                      Color_t bgcolor, 
                                      int fontSize, 
                                      Font_t font, 
                                      FontStyle_t 
                                      fontStyle, 
                                      Color_t fontColor)
    {
        FormatStruct fs = FormatStruct.getFormatStruct(alignment,
                                                       bgcolor,
                                                       fontColor,
                                                       fontStyle,
                                                       font,
                                                       null,
                                                       BorderStruct.DEFAULT_BORDER,
                                                       fontSize);
        Integer handle = (Integer) handles.get(fs);
        if (handle == null)
        {
            handle = new Integer(fmtInst);
            fmtInst++;
            handles.put(fs, handle);

            WritableFont wfont = fontStyle.createFont(fs.getFont(), fontSize, fs.getFontColor());
            BorderStruct bs = fs.getBorderStruct();
            WritableCellFormat f = new WritableCellFormat(wfont);
            try
            {
                f.setAlignment(fs.getAlignment().getAlignment());
                f.setBackground(fs.getBackground().getColor());
                f.setBorder(Border.TOP,    bs.getTopBorderStyle().getStyle(), bs.getTopBorderColor().getColor());
                f.setBorder(Border.RIGHT,  bs.getRightBorderStyle().getStyle(), bs.getRightBorderColor().getColor());
                f.setBorder(Border.LEFT,   bs.getLeftBorderStyle().getStyle(), bs.getLeftBorderColor().getColor());
                f.setBorder(Border.BOTTOM, bs.getBottomBorderStyle().getStyle(), bs.getBottomBorderColor().getColor());
            }
            catch (Exception e) { throw new RuntimeException("Invalid format initialization"); }
            formats.put(handle, f);
        }
        return(handle.intValue());
    }

    public static int getMiddleAlignedFormatHandle(Alignment_t alignment, 
                                      Color_t bgcolor, 
                                      int fontSize, 
                                      Font_t font, 
                                      FontStyle_t 
                                      fontStyle, 
                                      Color_t fontColor)
    {
        FormatStruct fs = FormatStruct.getFormatStruct(alignment,
                                                       bgcolor,
                                                       fontColor,
                                                       fontStyle,
                                                       font,
                                                       null,
                                                       BorderStruct.DEFAULT_BORDER,
                                                       fontSize);
        Integer handle = (Integer) handles.get(fs);
        if (handle == null)
        {
            handle = new Integer(fmtInst);
            fmtInst++;
            handles.put(fs, handle);

            WritableFont wfont = fontStyle.createFont(fs.getFont(), fontSize, fs.getFontColor());
            BorderStruct bs = fs.getBorderStruct();
            WritableCellFormat f = new WritableCellFormat(wfont);
            try
            {
            	f.setVerticalAlignment(VerticalAlignment.CENTRE);
                f.setAlignment(fs.getAlignment().getAlignment());
                f.setBackground(fs.getBackground().getColor());
                f.setBorder(Border.TOP,    bs.getTopBorderStyle().getStyle(), bs.getTopBorderColor().getColor());
                f.setBorder(Border.RIGHT,  bs.getRightBorderStyle().getStyle(), bs.getRightBorderColor().getColor());
                f.setBorder(Border.LEFT,   bs.getLeftBorderStyle().getStyle(), bs.getLeftBorderColor().getColor());
                f.setBorder(Border.BOTTOM, bs.getBottomBorderStyle().getStyle(), bs.getBottomBorderColor().getColor());
            }
            catch (Exception e) { throw new RuntimeException("Invalid format initialization"); }
            formats.put(handle, f);
        }
        return(handle.intValue());
    }

    public static int getFormatHandle(Alignment_t alignment, 
                                      Color_t bgcolor, 
                                      int fontSize, 
                                      Font_t font, 
                                      FontStyle_t fontStyle, 
                                      Color_t fontColor, 
                                      String numberFormat)
    {
        FormatStruct fs = FormatStruct.getFormatStruct(alignment,
                                                       bgcolor,
                                                       fontColor,
                                                       fontStyle,
                                                       font,
                                                       numberFormat,
                                                       BorderStruct.DEFAULT_BORDER,
                                                       fontSize);
        Integer handle = (Integer) handles.get(fs);
        if (handle == null)
        {
            handle = new Integer(fmtInst);
            fmtInst++;
            handles.put(fs, handle);

            WritableFont wfont = fontStyle.createFont(fs.getFont(), fontSize, fs.getFontColor());

            BorderStruct bs = fs.getBorderStruct();
            WritableCellFormat f = new WritableCellFormat(wfont, new NumberFormat(numberFormat));
            try
            {
                f.setAlignment(fs.getAlignment().getAlignment());
                f.setBackground(fs.getBackground().getColor());
                f.setBorder(Border.TOP,    bs.getTopBorderStyle().getStyle(), bs.getTopBorderColor().getColor());
                f.setBorder(Border.RIGHT,  bs.getRightBorderStyle().getStyle(), bs.getRightBorderColor().getColor());
                f.setBorder(Border.LEFT,   bs.getLeftBorderStyle().getStyle(), bs.getLeftBorderColor().getColor());
                f.setBorder(Border.BOTTOM, bs.getBottomBorderStyle().getStyle(), bs.getBottomBorderColor().getColor());
            }
            catch (Exception e) { throw new RuntimeException("Invalid format initialization"); }
            formats.put(handle, f);
        }
        return(handle.intValue());
    }

    public static int getFormatHandle(Alignment_t alignment, 
                                      Color_t bgcolor, 
                                      int fontSize, 
                                      Font_t font, 
                                      FontStyle_t 
                                      fontStyle, 
                                      Color_t fontColor, 
                                      int borderHandle)
    {
        BorderStruct bs = BorderFactory.getBorderStruct(borderHandle);
        FormatStruct fs = FormatStruct.getFormatStruct(alignment,
                                                       bgcolor,
                                                       fontColor,
                                                       fontStyle,
                                                       font,
                                                       null,
                                                       bs,
                                                       fontSize);
        Integer handle = (Integer) handles.get(fs);
        if (handle == null)
        {
            handle = new Integer(fmtInst);
            fmtInst++;
            handles.put(fs, handle);

            WritableFont wfont = fontStyle.createFont(fs.getFont(), fontSize, fs.getFontColor());

            WritableCellFormat f = new WritableCellFormat(wfont);
            try
            {
                f.setAlignment(fs.getAlignment().getAlignment());
                f.setBackground(fs.getBackground().getColor());
                f.setBorder(Border.TOP, bs.getTopBorderStyle().getStyle(), bs.getTopBorderColor().getColor()); 
                f.setBorder(Border.RIGHT, bs.getRightBorderStyle().getStyle(), bs.getRightBorderColor().getColor());
                f.setBorder(Border.LEFT, bs.getLeftBorderStyle().getStyle(), bs.getLeftBorderColor().getColor());
                f.setBorder(Border.BOTTOM, bs.getBottomBorderStyle().getStyle(), bs.getBottomBorderColor().getColor());
            }
            catch (Exception e) { throw new RuntimeException("Invalid format initialization"); }
            formats.put(handle, f);
        }
        return(handle.intValue());
    }

    public static int getFormatHandle(Alignment_t alignment, 
                                      Color_t bgcolor, 
                                      int fontSize, 
                                      Font_t font, 
                                      FontStyle_t fontStyle, 
                                      Color_t fontColor, 
                                      String numberFormat, 
                                      int borderHandle)
    {
        BorderStruct bs = BorderFactory.getBorderStruct(borderHandle);
        FormatStruct fs = FormatStruct.getFormatStruct(alignment,
                                                       bgcolor,
                                                       fontColor,
                                                       fontStyle,
                                                       font,
                                                       numberFormat,
                                                       bs,
                                                       fontSize);
        Integer handle = (Integer) handles.get(fs);
        if (handle == null)
        {
            handle = new Integer(fmtInst);
            fmtInst++;
            handles.put(fs, handle);

            WritableFont wfont = fontStyle.createFont(fs.getFont(), fontSize, fs.getFontColor());

            WritableCellFormat f = new WritableCellFormat(wfont, new NumberFormat(numberFormat));
            try
            {
                f.setAlignment(fs.getAlignment().getAlignment());
                f.setBackground(fs.getBackground().getColor());
                f.setBorder(Border.TOP, bs.getTopBorderStyle().getStyle(), bs.getTopBorderColor().getColor()); 
                f.setBorder(Border.RIGHT, bs.getRightBorderStyle().getStyle(), bs.getRightBorderColor().getColor());
                f.setBorder(Border.LEFT, bs.getLeftBorderStyle().getStyle(), bs.getLeftBorderColor().getColor());
                f.setBorder(Border.BOTTOM, bs.getBottomBorderStyle().getStyle(), bs.getBottomBorderColor().getColor());
            }
            catch (Exception e) { throw new RuntimeException("Invalid format initialization"); }
            formats.put(handle, f);
        }
        return(handle.intValue());
    }


    public static WritableCellFormat getFormat(int formatHandle)
    {
        return((WritableCellFormat) formats.get(new Integer(formatHandle)));
    }


}
