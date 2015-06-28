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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.makechip.stdf2xls4.xls.Cell_t;
import com.makechip.stdf2xls4.xls.SpreadSheetWriter1;

class PageTitleBlock
{
    //private static final int START_COL = LogoBlock.getWidth() + LegendBlock.getWidth();
    private static final int START_COL = LogoBlock.getWidth();
    
    
    public static void addBlock(SpreadSheetWriter1 sw, Sheet ws, boolean xssf, String title, int width)
    {
    	Row r = ws.getRow(TitleBlock.ROW);
    	if (r == null) r = ws.createRow(TitleBlock.ROW);
    	Cell c = r.getCell(START_COL);
    	if (c == null)
    	{
    		c = r.createCell(START_COL, Cell_t.STRING.getType());
    		c.setCellValue(title);
    		if (xssf)
    		{
    			XSSFCellStyle xcs = (XSSFCellStyle) sw.TITLE_FMT;
    			xcs.setFillForegroundColor(new XSSFColor(new byte[] { 0, 85, (byte) 165 }));
    			xcs.setFillBackgroundColor(new XSSFColor(new byte[] { 0, 85, (byte) 165 }));
    			c.setCellStyle(xcs);
    		}
    		else c.setCellStyle(sw.TITLE_FMT);
    	}
    	ws.addMergedRegion(new CellRangeAddress(TitleBlock.ROW, TitleBlock.HEIGHT-1, START_COL, START_COL + width -1));
    }
}
