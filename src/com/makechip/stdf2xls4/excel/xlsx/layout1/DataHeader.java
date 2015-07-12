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
package com.makechip.stdf2xls4.excel.xlsx.layout1;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.makechip.stdf2xls4.excel.xlsx.Cell_t;
import com.makechip.stdf2xls4.excel.xlsx.SpreadSheetWriter1;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader 
{
	private final int col;
	private final int row;
	private final boolean wrapTestNames;
	private final boolean hiPrecision;
	private final SpreadSheetWriter1 sw;
	private List<TestHeader> hdrs;
	
	public DataHeader(SpreadSheetWriter1 sw, List<TestHeader> hdrs, CornerBlock cb, HeaderBlock hb, boolean wrapTestNames, boolean hiPrecision)
	{
		col = cb.getWidth();
		row = TitleBlock.HEIGHT + hb.getHeight();
		this.sw = sw;
		this.hdrs = hdrs;
		this.wrapTestNames = wrapTestNames;
		this.hiPrecision = hiPrecision;
	}
	
	public void addBlock(Sheet ws, boolean xssf)
	{
		int c = col;
		for (TestHeader cid : hdrs)
		{
			int width = 0;
			if (!wrapTestNames) width = cid.testName.length();
			else
			{
				if (hiPrecision) width = 13;
				else width = 11;
			}
			ws.setColumnWidth(c, width * 256);
//			CellStyle cs1 = sw.HEADER1_FMT;
//			CellStyle cs5 = sw.HEADER5_FMT;
		    Row r0 = ws.getRow(row);	
			if (r0 == null) r0 = ws.createRow(row);
			Cell c0 = r0.getCell(c);
			if (c0 == null)
			{
				c0 = r0.createCell(c, Cell_t.STRING.getType());
				c0.setCellValue(cid.testName);
				if (xssf)
				{
					XSSFCellStyle cs0 = (XSSFCellStyle) sw.TEST_NAME_FMT;
					cs0.setAlignment(HorizontalAlignment.JUSTIFY);
					c0.setCellStyle(cs0);
				}
				else c0.setCellStyle(sw.TEST_NAME_FMT);
			}
			Row r1 = ws.getRow(row+1);
			if (r1 == null) r1 = ws.createRow(row+1);
			Cell c1 = r1.getCell(c);
			if (c1 == null)
			{
				c1 = r1.createCell(c, Cell_t.STRING.getType());
				c1.setCellValue(cid.testNumber);
				CellStyle cs2 = sw.HEADER1_FMT;
				cs2.setAlignment(CellStyle.ALIGN_CENTER);
				c1.setCellStyle(cs2);
			}
			Row r2 = ws.getRow(row+2);
			if (r2 == null) r2 = ws.createRow(row+2);
			Cell c2 = r2.getCell(c);
			if (c2 == null)
			{
				c2 = r2.createCell(c, Cell_t.STRING.getType());
//				if (cid.getLoLimit() == Record.MISSING_FLOAT)
//				{
//					c2.setCellValue("");
//					c2.setCellStyle(cs1);
//				}
//				else
//				{
//					c2.setCellValue(cid.getLoLimit());
//					c2.setCellStyle(cs5);
//				}
			}
			Row r3 = ws.getRow(row+3);
			if (r3 == null) r3 = ws.createRow(row+3);
			Cell c3 = r3.getCell(c);
			if (c3 == null)
			{
//				c3 = r3.createCell(c, Cell_t.STRING.getType());
//				if (cid.getHiLimit() == Record.MISSING_FLOAT)
//				{
//					c3.setCellValue("");
//					c3.setCellStyle(cs1);
//				}
//				else
//				{
//					c3.setCellValue(cid.getHiLimit());
//					c3.setCellStyle(cs5);
//				}
			}
			Row r4 = ws.getRow(row+4);
			if (r4 == null) r4 = ws.createRow(row+4);
			Cell c4 = r4.getCell(c);
			if (c4 == null)
			{
				c4 = r4.createCell(c, Cell_t.STRING.getType());
				CellStyle cs3 = sw.HEADER1_FMT;
				cs3.setAlignment(CellStyle.ALIGN_CENTER);
				c4.setCellStyle(cs3);
//				c4.setCellValue(cid.getUnits() == null ? "" : cid.getUnits());
			}
			ws.addMergedRegion(new CellRangeAddress(row+4, row+5, c, c));
			c++;
		}
	}
}
