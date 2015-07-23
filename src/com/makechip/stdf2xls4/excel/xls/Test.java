// Copyright 2011,2012 makechip.com
// This file is part of stdf2xls.
// 
// stdf2xls is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// stdf2xls is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with stdf2xls.  If not, see <http://www.gnu.org/licenses/>.

package com.makechip.stdf2xls4.excel.xls;

import java.io.File;
import java.io.IOException;
import com.makechip.util.Log;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Test
{
    private WritableWorkbook wb = null;
    private WritableSheet ws;

    public Test() throws IOException, BiffException, WriteException
    {
       	wb = Workbook.createWorkbook(new File("x.xls"));
    }

    public void generate() throws RowsExceededException, WriteException, IOException
    {
    	ws = wb.createSheet("Sheet 1", 0);
    	ws.addCell(new Number(1, 1, 3.1415));
    	Cell c = ws.getCell(1, 1);
    	Log.msg("Cell type = " + c.getType());
    }
    
    public void close() throws WriteException, IOException
    {
    	wb.close();
    }
        
    public static void main(String[] args)
    {
    	try
    	{
            Test t = new Test();
            t.generate();
    	}
    	catch (Exception e) { Log.fatal(e); } 
    }
    
}
