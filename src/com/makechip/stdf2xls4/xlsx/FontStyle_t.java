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

package com.makechip.stdf2xls4.xlsx;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

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

    public Font createFont(Workbook wb, short size)
    {
        Font font = wb.createFont();
       	font.setColor(IndexedColors.BLACK.getIndex());
   		font.setFontHeight((short) (20 * size));
        switch (this)
        {
            case BOLD:  
          		font.setItalic(false);
          		font.setUnderline(Font.U_NONE);
          		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          		break;
            case ITALIC:  
          		font.setItalic(true);
          		font.setUnderline(Font.U_NONE);
          		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
          		break;
            case UNDERLINE:  
          		font.setItalic(false);
          		font.setUnderline(Font.U_SINGLE);
          		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
          		break;
            case BOLD_ITALIC:  
          		font.setItalic(true);
          		font.setUnderline(Font.U_NONE);
          		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          		break;
            case BOLD_UNDERLINE:  
          		font.setItalic(false);
          		font.setUnderline(Font.U_SINGLE);
          		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          		break;
            case ITALIC_UNDERLINE:  
          		font.setItalic(true);
          		font.setUnderline(Font.U_SINGLE);
          		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
          		break;
            case BOLD_ITALIC_UNDERLINE:  
          		font.setItalic(true);
          		font.setUnderline(Font.U_SINGLE);
          		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          		break;
            default: 
          		font.setItalic(false);
          		font.setUnderline(Font.U_NONE);
          		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        }
        return(font);
    }

    public Font createFont(Workbook wb, short size, IndexedColors color)
    {
        Font font = createFont(wb, size);
        
       	font.setColor(color.getIndex());
        return(font);
    }
}
