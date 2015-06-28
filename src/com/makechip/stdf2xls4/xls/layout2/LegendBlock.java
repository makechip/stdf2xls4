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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.makechip.stdf2xls4.xls.Cell_t;
import com.makechip.stdf2xls4.xls.SpreadSheetWriter1;

public class LegendBlock
{
    private static int COL;
    private static final int ROW = TitleBlock.HEIGHT;
    
    public static void addBlock(SpreadSheetWriter1 sw, Sheet ws, int cbWidth, boolean wrapTestNames)
    {
    	COL = cbWidth;
    	if (wrapTestNames) ws.addMergedRegion(new CellRangeAddress(ROW, ROW, COL, COL+2));
    	addCell(ws, COL, ROW, "Legend:", sw.HEADER1_FMT);
    	addCell(ws, COL, ROW+1, "PASS", sw.PASS_VALUE_FMT);
    	addCell(ws, COL, ROW+2, "FAIL", sw.FAIL_VALUE_FMT);
    	addCell(ws, COL, ROW+3, "UNRELIABLE VALUE", sw.UNRELIABLE_VALUE_FMT);
    	addCell(ws, COL, ROW+4, "TIMEOUT", sw.TIMEOUT_VALUE_FMT);
    	addCell(ws, COL, ROW+5, "ALARM", sw.ALARM_VALUE_FMT);
    	addCell(ws, COL, ROW+6, "ABORT", sw.ABORT_VALUE_FMT);
    	addCell(ws, COL+2, ROW, "", sw.HEADER1_FMT);
    	addCell(ws, COL+2, ROW+1, "", sw.PASS_VALUE_FMT);
    	addCell(ws, COL+2, ROW+2, "", sw.FAIL_VALUE_FMT);
    	addCell(ws, COL+2, ROW+3, "", sw.UNRELIABLE_VALUE_FMT);
    	addCell(ws, COL+2, ROW+4, "", sw.TIMEOUT_VALUE_FMT);
    	addCell(ws, COL+2, ROW+5, "", sw.ALARM_VALUE_FMT);
    	addCell(ws, COL+2, ROW+6, "", sw.ABORT_VALUE_FMT);
    	if (wrapTestNames)
    	{
    		ws.addMergedRegion(new CellRangeAddress(ROW+1, ROW+1, COL, COL+2));
    		ws.addMergedRegion(new CellRangeAddress(ROW+2, ROW+2, COL, COL+2));
    		ws.addMergedRegion(new CellRangeAddress(ROW+3, ROW+3, COL, COL+2));
    		ws.addMergedRegion(new CellRangeAddress(ROW+4, ROW+4, COL, COL+2));
    		ws.addMergedRegion(new CellRangeAddress(ROW+5, ROW+5, COL, COL+2));
    		ws.addMergedRegion(new CellRangeAddress(ROW+6, ROW+6, COL, COL+2));
    	}
    }
    
    private static void addCell(Sheet ws, int col, int row, String text, CellStyle fmt)
    {
    	Row r = ws.getRow(row);
    	if (r == null) r = ws.createRow(row);
    	Cell c = r.getCell(col);
    	if (c == null)
    	{
    		c = r.createCell(col, Cell_t.STRING.getType());
    		c.setCellValue(text);
    		c.setCellStyle(fmt);
    	}
    }
    
    public static int getWidth() { return(3); }
    
    public static int getHeight() { return(LogoBlock.getHeight()); }
}
