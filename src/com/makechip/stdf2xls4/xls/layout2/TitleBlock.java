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
package com.makechip.stdf2xls4.xls.layout2;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.makechip.stdf2xls4.xls.SpreadSheetWriter2;


public class TitleBlock
{
    public static final int ROW = 0;
    public static final int COL = 0;
    public static final int HEIGHT = 8;
    
    public static void addBlock(Workbook wb, boolean xssf, SpreadSheetWriter2 sw, Sheet ws, String pageTitle, int titleWidth)
    {
        LogoBlock.addBlock(wb, ws);
        PageTitleBlock.addBlock(sw, ws, xssf, pageTitle, titleWidth);
    }

}
