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
package com.makechip.stdf2xls4.xlsx.layout1;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.makechip.stdf2xls4.xlsx.Cell_t;
import com.makechip.stdf2xls4.xlsx.SpreadSheetWriter1;

public class CornerBlock 
{
	private final int COL = TitleBlock.COL;
	private final int ROW;
	private final SpreadSheetWriter1 sw;
	private boolean wafersort;
	private boolean onePage;
	
	public CornerBlock(SpreadSheetWriter1 sw, boolean wafersort, int headerBlockHeight, boolean onePage)
	{
		this.sw = sw;
		this.wafersort = wafersort;
		this.onePage = onePage;
		ROW = headerBlockHeight + LogoBlock.getHeight();
	}
	
	public void addBlock(Sheet ws)
	{
		int rsltCol = wafersort ? COL + 4 : COL + 5;
		Row r = ws.getRow(ROW+5);
		if (r == null) r = ws.createRow(ROW+5);
		CellStyle cs = sw.HEADER1_FMT;
		if (onePage)
		{
			String s = (wafersort) ? "Wafer" : "Step";
			Cell c = r.getCell(rsltCol);
			if (c == null)
			{
				c = r.createCell(rsltCol, Cell_t.STRING.getType());
				c.setCellValue(s);
				c.setCellStyle(cs);
			}
		}
		Cell c1 = r.getCell(rsltCol-2);
		if (c1 == null)
		{
			c1 = r.createCell(rsltCol-2, Cell_t.STRING.getType());
			c1.setCellValue("HW Bin");
			c1.setCellStyle(cs);
		}
		Cell c2 = r.getCell(rsltCol-1);
		if (c2 == null)
		{
			c2 = r.createCell(rsltCol-1, Cell_t.STRING.getType());
			c2.setCellValue("SW Bin");
			c2.setCellStyle(cs);
		}
		Cell c3 = r.getCell(rsltCol);
		if (c3 == null)
		{
			c3 = r.createCell(rsltCol, Cell_t.STRING.getType());
			c3.setCellValue("Rslt");
			c3.setCellStyle(cs);
		}
		Cell c4 = r.getCell(rsltCol+1);
		if (c4 == null)
		{
			c4 = r.createCell(rsltCol+1, Cell_t.STRING.getType());
			c4.setCellValue("Temp");
			c4.setCellStyle(cs);
		}
		int col = 0;
		if (wafersort)
		{
			Cell c5 = r.getCell(rsltCol+2);
			if (c5 == null)
			{
				c5 = r.createCell(rsltCol+2, Cell_t.STRING.getType());
				c5.setCellValue("X");
				c5.setCellStyle(cs);
			}
			Cell c6 = r.getCell(rsltCol+3);
			if (c6 == null)
			{
				c6 = r.createCell(rsltCol+3, Cell_t.STRING.getType());
				c6.setCellValue("Y");
				c6.setCellStyle(cs);
			}
			col = rsltCol + 3;
		}
		else
		{
			Cell c5 = r.getCell(rsltCol+2);
			if (c5 == null)
			{
				c5 = r.createCell(rsltCol+2, Cell_t.STRING.getType());
				c5.setCellValue("S/N");
				c5.setCellStyle(cs);
			}
			col = rsltCol + 2;
		}
		Row r0 = ws.getRow(ROW);
		if (r0 == null) r0 = ws.createRow(ROW);
		Cell s0 = r0.getCell(0);
		CellStyle cs4 = sw.HEADER4_FMT;
		cs4.setAlignment(CellStyle.ALIGN_RIGHT);
		Cell[] s = new Cell[7];
		for (int i=0; i< s.length; i++)
		{
			s[i] = r0.getCell(i+1);
			if (s[i] == null)
			{
				s[i] = r0.createCell(i+1, Cell_t.STRING.getType());
				s[i].setCellStyle(cs4);
			}
		}
		if (s0 == null)
		{
			s0 = r0.createCell(0, Cell_t.STRING.getType());
			s0.setCellValue("Test Name");
			s0.setCellStyle(cs4);
		}
		Row r1 = ws.getRow(ROW+1);
		if (r1 == null) r1 = ws.createRow(ROW+1);
		Cell s1 = r1.getCell(0);
		if (s1 == null)
		{
			s1 = r1.createCell(0, Cell_t.STRING.getType());
			s1.setCellValue("Test Num");
			s1.setCellStyle(cs4);
		}
		Row r2 = ws.getRow(ROW+2);
		if (r2 == null) r2 = ws.createRow(ROW+2);
		Cell s2 = r2.getCell(0);
		if (s2 == null)
		{
			s2 = r2.createCell(0, Cell_t.STRING.getType());
			s2.setCellValue("Lo Limit");
			s2.setCellStyle(cs4);
		}
		Row r3 = ws.getRow(ROW+3);
		if (r3 == null) r3 = ws.createRow(ROW+3);
		Cell s3 = r3.getCell(0);
		if (s3 == null)
		{
			s3 = r3.createCell(0, Cell_t.STRING.getType());
			s3.setCellValue("Hi Limit");
			s3.setCellStyle(cs4);
		}
		Row r4 = ws.getRow(ROW+4);
		if (r4 == null) r4 = ws.createRow(ROW+4);
		Cell s4 = r4.getCell(0);
		if (s4 == null)
		{
			s4 = r4.createCell(0, Cell_t.STRING.getType());
			s4.setCellValue("Units");
			s4.setCellStyle(cs4);
		}
        r0.setHeight((short) 2000);
		ws.addMergedRegion(new CellRangeAddress(ROW, ROW, 0, col));
		ws.addMergedRegion(new CellRangeAddress(ROW+1, ROW+1, 0, col));
		ws.addMergedRegion(new CellRangeAddress(ROW+2, ROW+2, 0, col));
		ws.addMergedRegion(new CellRangeAddress(ROW+3, ROW+3, 0, col));
		ws.addMergedRegion(new CellRangeAddress(ROW+4, ROW+4, 0, col));
		
	}

	public int getHeight()
	{
		return(6);
	}
	
	public int getWidth()
	{
		return(8);
	}
}
