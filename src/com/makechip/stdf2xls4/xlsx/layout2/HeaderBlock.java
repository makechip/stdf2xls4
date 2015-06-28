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
package com.makechip.stdf2xls4.xlsx.layout2;

import org.apache.poi.ss.usermodel.Sheet;

import com.makechip.stdf2xls4.xlsx.SpreadSheetWriter2;

public class HeaderBlock
{
    public static final int ROW = TitleBlock.HEIGHT;
    public static final int COL = TitleBlock.COL;
    //private final SpreadSheetWriter3 sw;
    //private final int height;
    
    public HeaderBlock(SpreadSheetWriter2 sw)
    {
    	//this.sw = sw;
    	//this.items = new ArrayList<HeaderItem>();
    	//for (HeaderItem h : items) this.items.add(h);
    	//height = items.size();
    }
    
    public void addBlock(Sheet ws)
    {
        //int row = ROW;

        /*
        for (HeaderItem h : items)
        {
        	Row r = ws.getRow(row);
        	if (r == null) r = ws.createRow(row);
        	Cell c0 = r.getCell(COL);
        	if (c0 == null)
        	{
        		c0 = r.createCell(COL, Cell_t.STRING.getType());
        		c0.setCellValue(h.getLabel());
        		c0.setCellStyle(sw.HEADER2_FMT);
        	}
        	Cell c1 = r.getCell(COL+2);
        	if (c1 == null)
        	{
        		c1 = r.createCell(COL+2, Cell_t.STRING.getType());
        		c1.setCellValue(h.getValue());
        		c1.setCellStyle(sw.HEADER3_FMT);
        	}
        	ws.addMergedRegion(new CellRangeAddress(row, row, COL, COL+1));
        	ws.addMergedRegion(new CellRangeAddress(row, row, COL+2, COL+4));
            row++;
        }
        */
    }
    
    public int getHeight() 
    { 
        return(0); 
    }
}
