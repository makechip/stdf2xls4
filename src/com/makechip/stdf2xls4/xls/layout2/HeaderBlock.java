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
//import com.makechip.stdf2xls4.xls.Cell_t;
import com.makechip.stdf2xls4.xls.SpreadSheetWriter1;

public class HeaderBlock
{
    public static final int ROW = TitleBlock.HEIGHT;
    public static final int COL = TitleBlock.COL;
//    private final List<HeaderItem> items;
    //private SpreadSheetWriter2 sw;
    private int height;
    
    public HeaderBlock(SpreadSheetWriter1 sw)
    {
    	//this.sw = sw;
 //   	this.items = new ArrayList<HeaderItem>();
 //   	for (HeaderItem h : items) this.items.add(h);
    	//height = items.size();
    }
    
    public void addBlock(Sheet ws)
    {
        //int col = COL;
        //int row = ROW;
        /*
        for (HeaderItem h : items)
        {
        	Row r0 = ws.getRow(row);
        	if (r0 == null) r0 = ws.createRow(row);
        	Cell c0 = r0.getCell(col);
        	if (c0 == null)
        	{
        		c0 = r0.createCell(col, Cell_t.STRING.getType());
        		c0.setCellValue(h.getLabel());
        		c0.setCellStyle(sw.HEADER2_FMT);
        	}
        	Cell c1 = r0.getCell(col+3);
        	Cell c2 = r0.getCell(col+7);
        	if (c2 == null)
        	{
        		c2 = r0.createCell(col+7, Cell_t.STRING.getType());
        		c2.setCellStyle(sw.HEADER3_FMT);
        	}
        	if (c1 == null)
        	{
        		c1 = r0.createCell(col+3, Cell_t.STRING.getType());
        		c1.setCellValue(h.getValue());
        		c1.setCellStyle(sw.HEADER3_FMT);
        	}
        	ws.addMergedRegion(new CellRangeAddress(row, row, col, col+2));
        	ws.addMergedRegion(new CellRangeAddress(row, row, col+3, LogoBlock.getWidth() + LegendBlock.getWidth()-1));
            row++;
        }
        */
    }
    
    public int getWidth() { return(LogoBlock.getWidth() + LegendBlock.getWidth()-1); }
    
    public int getHeight() 
    { 
        return(height); 
    }
}
