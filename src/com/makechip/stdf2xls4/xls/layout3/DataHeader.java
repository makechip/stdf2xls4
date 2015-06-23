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

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import com.makechip.stdf2xls4.stdfapi.DeviceHeader;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.xls.Cell_t;
import com.makechip.stdf2xls4.xls.SpreadSheetWriter3;

public class DataHeader 
{
	private static final int COL = 5;
	private int row;
	private final SpreadSheetWriter3 sw;
	private List<DeviceHeader> hdrs;
	
	public DataHeader(SpreadSheetWriter3 sw, List<DeviceHeader> hdrs, CornerBlock cb, HeaderBlock hb)
	{
		row = CornerBlock.ROW + cb.getHeight();
		this.sw = sw;
		this.hdrs = hdrs;
	}
	
	public void addBlock(Sheet ws, boolean xssf)
	{
		int maxWidth = 0;
		for (DeviceHeader cid : hdrs)
		{
			if (cid.getTestName().length() > maxWidth) maxWidth = cid.getTestName().length();
		}
		ws.setColumnWidth(COL, maxWidth * 256);
		CellStyle cs1 = sw.HEADER1_FMT;
		CellStyle cs5 = sw.HEADER5_FMT;
		for (DeviceHeader cid : hdrs)
		{
			int col = COL;
		    Row r = ws.getRow(row);	
			if (r == null) r = ws.createRow(row);
			Cell c0 = r.getCell(col);
			if (c0 == null)
			{
				c0 = r.createCell(col, Cell_t.STRING.getType());
				c0.setCellValue(cid.getTestName());
				c0.setCellStyle(sw.TEST_NAME_FMT);
			}
			col++;
			Cell c1 = r.getCell(col);
			if (c1 == null)
			{
				c1 = r.createCell(col, Cell_t.STRING.getType());
				c1.setCellValue(cid.getTestNumber());
				c1.setCellStyle(cs1);
			}
			col++;
			Cell c2 = r.getCell(col);
			if (c2 == null)
			{
				c2 = r.createCell(col, Cell_t.STRING.getType());
				if (cid.getLoLimit() == StdfRecord.MISSING_FLOAT)
				{
					c2.setCellValue("");
					c2.setCellStyle(cs1);
				}
				else
				{
					c2.setCellValue(cid.getLoLimit());
					c2.setCellStyle(cs5);
				}
			}
			col++;
			Cell c3 = r.getCell(col);
			if (c3 == null)
			{
				c3 = r.createCell(col, Cell_t.STRING.getType());
				if (cid.getHiLimit() == StdfRecord.MISSING_FLOAT)
				{
					c3.setCellValue("");
					c3.setCellStyle(cs1);
				}
				else
				{
					c3.setCellValue(cid.getHiLimit());
					c3.setCellStyle(cs5);
				}
			}
			col++;
			Cell c4 = r.getCell(col);
			if (c4 == null)
			{
				c4 = r.createCell(col, Cell_t.STRING.getType());
				c4.setCellStyle(cs1);
				c4.setCellValue(cid.getUnits() == null ? "" : cid.getUnits());
			}
			ws.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
			row++;
		}
	}
}
