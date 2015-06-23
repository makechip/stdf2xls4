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
package com.makechip.stdf2xls4.xls.layout3;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.makechip.stdf2xls4.xls.Cell_t;
import com.makechip.stdf2xls4.xls.SpreadSheetWriter3;

public class CornerBlock 
{
	public static final int COL = 10;
	public static final int ROW = TitleBlock.HEIGHT;
	private int row;
	private final SpreadSheetWriter3 sw;
	private boolean wafersort;
	private boolean onePage;
	
	public CornerBlock(SpreadSheetWriter3 sw, boolean wafersort, int headerBlockHeight, boolean onePage)
	{
		this.sw = sw;
		this.wafersort = wafersort;
		this.onePage = onePage;
	}
	
	public void addBlock(Sheet ws)
	{
		row = ROW;
		Row r0 = ws.getRow(row);
		if (r0 == null) r0 = ws.createRow(row);
		CellStyle cs = sw.HEADER1_FMT;
		if (onePage)
		{
			String s = (wafersort) ? "Wafer" : "Step";
			Cell c = r0.getCell(COL);
			if (c == null)
			{
				c = r0.createCell(COL, Cell_t.STRING.getType());
				c.setCellValue(s);
				c.setCellStyle(cs);
			}
			row++;
		}
		Row r1 = ws.getRow(row);
		if (r1 == null) r1 = ws.createRow(row);
		Cell c1 = r1.getCell(COL);
		if (c1 == null)
		{
			c1 = r1.createCell(COL, Cell_t.STRING.getType());
			c1.setCellValue("HW Bin");
			c1.setCellStyle(cs);
		}
		row++;
		Row r2 = ws.getRow(row);
		if (r2 == null) r2 = ws.createRow(row);
		Cell c2 = r2.getCell(COL);
		if (c2 == null)
		{
			c2 = r2.createCell(COL, Cell_t.STRING.getType());
			c2.setCellValue("SW Bin");
			c2.setCellStyle(cs);
		}
		row++;
		Row r3 = ws.getRow(row);
		if (r3 == null) r3 = ws.createRow(row);
		Cell c3 = r3.getCell(COL);
		if (c3 == null)
		{
			c3 = r3.createCell(COL, Cell_t.STRING.getType());
			c3.setCellValue("Rslt");
			c3.setCellStyle(cs);
		}
		row++;
		Row r4 = ws.getRow(row);
		if (r4 == null) r4 = ws.createRow(row);
		Cell c4 = r4.getCell(COL);
		if (c4 == null)
		{
			c4 = r4.createCell(COL, Cell_t.STRING.getType());
			c4.setCellValue("Temp");
			c4.setCellStyle(cs);
		}
		row++;
		Row r5 = ws.getRow(row);
		if (r5 == null) r5 = ws.createRow(row);
		if (wafersort)
		{
			Cell c5 = r5.getCell(COL);
			if (c5 == null)
			{
				c5 = r5.createCell(COL, Cell_t.STRING.getType());
				c5.setCellValue("X");
				c5.setCellStyle(cs);
			}
			row++;
			Row r6 = ws.getRow(row);
			if (r6 == null) r6 = ws.createRow(row);
			Cell c6 = r6.getCell(COL);
			if (c6 == null)
			{
				c6 = r6.createCell(COL, Cell_t.STRING.getType());
				c6.setCellValue("Y");
				c6.setCellStyle(cs);
			}
			row++;
		}
		else
		{
			Cell c5 = r5.getCell(COL);
			if (c5 == null)
			{
				c5 = r5.createCell(COL, Cell_t.STRING.getType());
				c5.setCellValue("S/N");
				c5.setCellStyle(cs);
			}
			row++;
		}
		Cell s0 = r0.getCell(5);
		CellStyle cs4 = sw.HEADER4_FMT;
		if (s0 == null)
		{
			s0 = r0.createCell(5, Cell_t.STRING.getType());
			s0.setCellValue("Test Name");
			s0.setCellStyle(cs4);
		}
		Cell s1 = r0.getCell(6);
		if (s1 == null)
		{
			s1 = r0.createCell(6, Cell_t.STRING.getType());
			s1.setCellValue("Test Num");
			s1.setCellStyle(cs4);
		}
		Cell s2 = r0.getCell(7);
		if (s2 == null)
		{
			s2 = r0.createCell(7, Cell_t.STRING.getType());
			s2.setCellValue("Lo Limit");
			s2.setCellStyle(cs4);
		}
		Cell s3 = r0.getCell(8);
		if (s3 == null)
		{
			s3 = r0.createCell(8, Cell_t.STRING.getType());
			s3.setCellValue("Hi Limit");
			s3.setCellStyle(cs4);
		}
		Cell s4 = r0.getCell(9);
		if (s4 == null)
		{
			s4 = r0.createCell(9, Cell_t.STRING.getType());
			s4.setCellValue("Units");
			s4.setCellStyle(cs4);
		}
		int rows = onePage ? (wafersort ? 6 : 5) : (wafersort ? 5 : 4);
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW+rows, 5, 5));
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW+rows, 6, 6));
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW+rows, 7, 7));
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW+rows, 8, 8));
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW+rows, 9, 9));
		
	}

	public int getHeight()
	{
		return(row - ROW);
	}
	
	public int getWidth()
	{
		return(8);
	}
}
