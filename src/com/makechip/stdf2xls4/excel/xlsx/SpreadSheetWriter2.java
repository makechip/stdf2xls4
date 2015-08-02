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

package com.makechip.stdf2xls4.excel.xlsx;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.SpreadSheetWriter;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.xlsx.layout.CornerBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.Format_t;
import com.makechip.stdf2xls4.excel.xlsx.layout.TitleBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.LegendBlock;
import com.makechip.stdf2xls4.stdfapi.*;
import com.makechip.stdf2xls4.stdf.StdfException;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.Log;

/**
 * This class is used to generate Spreadsheets in the old "xls" format, and
 * with test headers running down the page, and devices running across the page.
 * This layout is generated with the -r option, and it has the advantage that
 * all tests fit on one page.
 * The layout of the spreadsheet is as follows:<br>
 * <br>
 * <pre> 
      |   0  |   1  |   2  |  3   |   4  |   5  |   6          
     -+------+------+------+------+------+------+------------------------------------------------
     0|             |                           |
     -+             +                           +
      |             |                           |
     -+             +                           +
      |             |                           |
     -+  Legend     +                           +
      |   Block     |                           |
     -+             +      LogoBlock            +   PageTitleBlock ---->
      |             |                           |
     -+             +                           +
      |             |                           |
     -+             +                           +
     6|             |                           |
     -+------+------+------+------+------+------+---------------------------+-------+-------+-------+-------+-------+-------+--------+-------
     7|                                         |                           |       |       |       |       |       |       |tstamp? |
      +                                         +                           |       |       |       |       |       |       +        +-------
      |                                         |                           |       |       |       |       |       |       |waf/stp?|
     -+                                         +                           |       |       |       |       |       |       +--------+--------
      |                                         |                           |       |       |       |       |       |       |      X?|
     -+                                         +                           |       |       |       |       |       |       +--------+---------
      |                                         |                           |       |       |       |       |       |       |  Y/S_N:|
     -+                                         +                           |       |       |       |       |       |       +--------+--------
      |                                         |                           |       |       |       |       |       |       | HW Bin:|
     -+                                         +                           |       |       |       |       |       |       +--------+---------
      |                                         |                           |       |       |       |       |       |       | SW Bin:|
     -+                                         +                           |       |       |       |       |       |       +--------+--------
      |           HeaderBlock                   |                           |       |       |       |       |       |       |   Rslt:|
     -+                                                                     |       |       |       |       |       |       +--------+--------
      |                                         |       Test Name           |TestNum| DupNum| Pin   |LoLimit|HiLimit| Units |   Temp:|
     -+                                         +---------------------------+-------+-------+-------+-------+-------+-------+--------+--------
      |                                         |                           |       |       |       |       |       |                | result
     -+                                         +---------------------------+-------+-------+-------+-------+-------+----------------+-------
      |                                         |                           |       |       |       |       |       |                | result 
     -+                                         +---------------------------+-------+-------+-------+-------+-------+----------------+-------
   7+n|                                         |                           |       |       |       |       |       |                | result
     -+------+------+------+------+------+------+---------------------------+-------+-------+-------+-------+-------+----------------+------- 
   8+n|       
     -+       
   9+n|       
     -+       
      |       
</pre> 
 * 
 * The actual spreadsheet looks like this:
 * <p> <IMG SRC="{@docRoot}/doc-files/xls2.png"> 
 * @author eric
 * @TODO update picture
 * Algorithm for sheet management:
 * 1. Query workbook for sheet [name][page][version=0]
 * 2. if sheet does not exist, create new sheet;
 * 2a. Store the TitleBlock by its page name.
 * 2b. done.
 * 3. if the sheet does exist, get its page header
 * 4. Compare sheet header with current header.
 * 4a. if headers match use existing sheet.
 * 4b. if headers don't match, create new sheet with bumped version number.
 * 
 *
 *
 *
 */
@SuppressWarnings("unused")
public class SpreadSheetWriter2 extends SpreadsheetWriter implements SpreadSheetWriter
{
    private HeaderBlock hb;	
    private CornerBlock cb;
	
    private int currentCol;
    private int testColumns;
    private TitleBlock titleBlock;

    public SpreadSheetWriter2(CliOptions options, StdfAPI api) throws IOException, InvalidFormatException
    {
    	super(options, api);
    }

    protected void writeData(PageHeader hdr)
    {
    	if (options.sort && options.showDuplicates) noOverwrite = true;
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	final int pages = (devs.size() % COLS_PER_PAGE == 0) ? devs.size() / COLS_PER_PAGE : 1 + devs.size() / COLS_PER_PAGE;
    	IntStream.range(0, pages).forEach(page -> writeResultsOnPage(hdr, page));
    }	
    	
    private void writeResultsOnPage(PageHeader hdr, int page)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
       	final int startIndex = page * COLS_PER_PAGE;
       	final int endIndex = startIndex + COLS_PER_PAGE > devs.size() ? devs.size() : startIndex + COLS_PER_PAGE;
       	String waferOrStep = api.wafersort(hdr) ? hdr.get(HeaderUtil.WAFER_ID) : hdr.get(HeaderUtil.STEP);
        IntStream.range(startIndex, endIndex).forEach(devIndex -> writeDeviceResults(hdr, waferOrStep, page, devIndex));
    }
    
    private void writeDeviceResults(PageHeader hdr, String waferOrStep, int page, int devIndex)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	DeviceHeader dh = devs.get(devIndex);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
        locateCol(api.wafersort(hdr), waferOrStep, dh.snxy, page);
        setDevice(page, waferOrStep, devs.get(devIndex));
        IntStream.range(0, tests.size()).forEach(i -> setResult(i, page, tests.get(i), hdr, dh));
    } 
    
    private void setResult(int index, int page, TestHeader th, PageHeader hdr, DeviceHeader dh)
    {
   	    int row = titleBlock.tstxy.unitsLabel.r + 1 + index;
		TestResult r = api.getRecord(hdr, dh, th);
		try
		{
		    if (r != null)
		    {
			    if (r instanceof ParametricTestResult)
			    {
				    ParametricTestResult p = (ParametricTestResult) r;
				    setValue(ws[page], currentCol, row, p);
			    }
			    else if (r instanceof DatalogTestResult)
			    {
				    DatalogTestResult d = (DatalogTestResult) r;
				    setText(ws[page], currentCol, row, STATUS_PASS_FMT, d.result);
			    }
			    else // a functional test
			    {
				    setStatus(ws[page], currentCol, row, r);
			    }
		    }
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
    }
    
    protected void openSheet(PageHeader hdr) throws IOException, StdfException
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
        int pages = devs.size() / COLS_PER_PAGE;
        if (devs.size() % COLS_PER_PAGE != 0) pages++;
        ws = new XSSFSheet[pages];
        for (int page=0; page<pages; page++)
        {
        	int numDevices = (page == pages - 1) ? (devs.size() % COLS_PER_PAGE == 0 ? COLS_PER_PAGE : devs.size() % COLS_PER_PAGE) : COLS_PER_PAGE;
        	SheetName sname = null;
        	int version = 0;
        	while (true)
        	{
        		ws[page] = null;
        	    sname = SheetName.getExistingSheetName(api.wafersort(hdr), hdr, page+1, version);
        	    Log.msg("existing sheet name = " + sname);
        	    if (sname == null) break;
        	    ws[page] = sheetMap.get(sname);
        	    if (ws[page] == null) break;
        	    PageHeader ph = getPageHeader(ws[page]);
        	    if (ph.equals(hdr)) break;
        	    version++;
        	}
        	if (ws[page] == null) 
        	{
        		Log.msg("getting new sheet");
        		sname = SheetName.getSheet(api.wafersort(hdr), hdr, page+1, version);
        		Log.msg("new sheet name = " + sname);
        		newSheet(page, sname, hdr, numDevices);
        	}
        	else // note: an existing sheet might have a titleblock that is incompatible with the current titleblock.
        	{
        		Log.msg("checking exising sheet...");
    	        //List<TestHeader> list = getTestHeaders(ws[page]);
    	        titleBlock = new TitleBlock(hdr, options.logoFile, sname.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, null);
        		if (!checkRegistration(ws[page], LegendBlock.HEIGHT, titleBlock.tstxy.tnameLabel.c, titleBlock.tstxy.tnameLabel.r)) 
        		{
        			close();
        			throw new StdfException("Incompatible spreadsheet");
        		}
        	}
        }
    }
    
    private void newSheet(int page, SheetName name, PageHeader hdr, int numDevices)
    {
    	ws[page] = wb.createSheet(name.toString());
    	sheetMap.put(name, ws[page]);
    	List<TestHeader> list = api.getTestHeaders(hdr);
    	titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, list);
    	titleBlock.addBlock(wb, ws[page]);
    }
    
    private List<TestHeader> getTestHeaders(XSSFSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(COLS_PER_PAGE);
        int cnt = 0;
        int row = titleBlock.tstxy.unitsLabel.r + 1;
        Row r = existingSheet.getRow(row);
        while (r.getCell(titleBlock.tstxy.tname.c).getCellType() != Cell_t.BLANK.type)
        {
        	r = existingSheet.getRow(row);
        	Cell tnameCell = r.getCell(titleBlock.tstxy.tname.c);
        	Cell tnumCell  = r.getCell(titleBlock.tstxy.tnum.c);
        	Cell dupCell   = r.getCell(titleBlock.tstxy.dupNum.c);
        	Cell loLimCell = r.getCell(titleBlock.tstxy.loLim.c);
        	Cell hiLimCell = r.getCell(titleBlock.tstxy.hiLim.c);
        	Cell pnameCell = r.getCell(titleBlock.tstxy.pin.c);
        	Cell unitsCell = r.getCell(titleBlock.tstxy.units.c);
        	String tname = tnameCell.getStringCellValue();
           	long tnum = (long) tnumCell.getNumericCellValue();
           	int dnum = (int) dupCell.getNumericCellValue();
        	if (pnameCell.getCellType() == Cell_t.STRING.type) 
        	{
               	String units = unitsCell.getStringCellValue();
                if (loLimCell.getCellType() == Cell_t.BLANK.type && hiLimCell.getCellType() == Cell_t.BLANK.type)
                {
                    if (pnameCell.getStringCellValue().equals(MultiParametricTestHeader.LL_HDR)) // A LoLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.LO_LIMIT));
                    }
                    else if (pnameCell.getStringCellValue().equals(MultiParametricTestHeader.HL_HDR)) // A HiLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.HI_LIMIT));
                    }
                    else throw new StdfException("Malformed test header: testName = " + tname);
                }
                else // either a ParametricTest with pin, or a MultiParametricTest
                {
                   	Float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) loLimCell.getNumericCellValue();
                   	Float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) hiLimCell.getNumericCellValue();
                    String pin = pnameCell.getStringCellValue();
                    plist.add(new MultiParametricTestHeader(tname, tnum, dnum, pin, units, lolim, hilim));
                }
        	}
        	else // no pin, so either a normal Parametric test or Functional or Datalog test
        	{
        	    if (loLimCell.getCellType() != Cell_t.BLANK.type || hiLimCell.getCellType() != Cell_t.BLANK.type)
        	    {
                   	Float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) loLimCell.getNumericCellValue();
                   	Float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) hiLimCell.getNumericCellValue();
                   	String units = unitsCell.getStringCellValue();
                   	plist.add(new ParametricTestHeader(tname, tnum, dnum, units, lolim, hilim));
        	    }
        	    else
        	    {
        	    	plist.add(new TestHeader(tname, tnum, dnum));
        	    }
        	}
        	row++;
        }
        return(plist);
    }
    
    private void setDevice(int page, String waferOrStep, DeviceHeader dh)
    {
    	XSSFSheet wsi = ws[page];
    	int col = currentCol;
    	CellStyle cs = STATUS_PASS_FMT.getFormat(wb);
    	wsi.setColumnWidth(col, 256 * 12);
    	if (dh.snxy instanceof XY || dh.snxy instanceof TimeXY) // wafersort
    	{
    		if (options.onePage)
    		{
    			if (dh.snxy instanceof TimeXY)
    			{
    				wsi.setColumnWidth(0, 256 * 15);	
    				setCell(wsi, col, titleBlock.devxy.tstamp.r, cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    			setCell(wsi, col, titleBlock.devxy.wafOrStep.r, cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeXY)
    			{
    				setCell(wsi, col, titleBlock.devxy.tstamp.r, cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    		}
    		setCell(wsi, col, titleBlock.devxy.x.r, cs, dh.snxy.getX());
    		setCell(wsi, col, titleBlock.devxy.yOrSn.r, cs, dh.snxy.getY());
    	}
    	else // FT
    	{
    		if (options.onePage)
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				setCell(wsi, col, titleBlock.devxy.tstamp.r, cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    			setCell(wsi, col, titleBlock.devxy.wafOrStep.r, cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				setCell(wsi, col, titleBlock.devxy.tstamp.r, cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    		}
    		setCell(wsi, col, titleBlock.devxy.yOrSn.r, cs, dh.snxy.getSerialNumber());
    	}
    	setCell(wsi, col, titleBlock.devxy.hwBin.r, cs, dh.hwBin);
    	setCell(wsi, col, titleBlock.devxy.swBin.r, cs, dh.swBin);
    	setCell(wsi, col, titleBlock.devxy.temp.r, cs, dh.temperature);
    	wsi.addMergedRegion(new CellRangeAddress(titleBlock.devxy.temp.r, titleBlock.devxy.temp.r+1, col, col));
    	if (dh.fail) setCell(wsi, col, titleBlock.devxy.rslt.r, STATUS_FAIL_FMT.getFormat(wb), "FAIL");
    	else setCell(wsi, col, titleBlock.devxy.rslt.r, cs, "PASS");
    }
    
    private void setValue(XSSFSheet wsi, int col, int row, ParametricTestResult p)
    {
    	if (p.pass()) setCell(wsi, col, row, PASS_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else if (p.noPassFail()) setCell(wsi, col, row, INVALID_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else if (p.unreliable()) setCell(wsi, col, row, UNRELIABLE_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else if (p.alarm()) setCell(wsi, col, row, ALARM_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else if (p.timeout()) setCell(wsi, col, row, TIMEOUT_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else if (p.abort()) setCell(wsi, col, row, ABORT_VALUE_FMT.getFormat(wb, options.precision), p.result);
    	else setCell(wsi, col, row, FAIL_VALUE_FMT.getFormat(wb, options.precision), p.result);
    }
    
    private void locateCol(boolean wafersort, String waferOrStep, SnOrXy snOrXy, int page)
    {
        TIntArrayList xlist = new TIntArrayList();
        final int MAX_COLS = titleBlock.devxy.yOrSnLabel.c + 1 + COLS_PER_PAGE;
        if (wafersort)
        {
        	if (!options.noOverwrite)
        	{
        		for (int col=titleBlock.devxy.yOrSnLabel.c + 1; col<=titleBlock.devxy.yOrSnLabel.c + 1 + MAX_COLS; col++)
        		{
        			Row r = ws[page].getRow(titleBlock.devxy.xLabel.r);
        			if (r == null)
        			{
        				currentCol = col;
        				break;
        			}
        			Cell cx = r.getCell(col);
        			if (cx == null)
        			{
        				currentCol = col;
        				return;
        			}
        			if (cx.getCellType() == Cell_t.BLANK.type)
        			{
        				currentCol = col;
        				return;
        			}
        			r = ws[page].getRow(titleBlock.devxy.yOrSnLabel.r);
        			Cell cy = r.getCell(col);
        			short x = (short) cx.getNumericCellValue();
        			short y = (short) cy.getNumericCellValue();
        			if (options.onePage)
        			{
        				r = ws[page].getRow(titleBlock.devxy.wafOrStepLabel.r);
        				String waf = r.getCell(col).getStringCellValue();
                        if (waf.equals(waferOrStep) && x == snOrXy.getX() && y == snOrXy.getY())
                        {
                        	currentCol = col;
                        	return;
                        }
        			}
        			else
        			{
        				if (x == snOrXy.getX() && y == snOrXy.getY())
        				{
        					currentCol = col;
        					return;
        				}
        			}
        		}
        	}
            for (int col=titleBlock.devxy.yOrSnLabel.c + 1; col<=MAX_COLS; col++)
            {
            	Row r = ws[page].getRow(titleBlock.devxy.xLabel.r);
                Cell c = r.getCell(col);
                if (c.getCellType() == Cell_t.BLANK.type)
                {
                    currentCol = col;
                    break;
                }
            }
        }
        else // final test
        {
            for (int col=titleBlock.devxy.yOrSnLabel.c + 1; col<=titleBlock.devxy.yOrSnLabel.c + 1 + MAX_COLS; col++)
            {
            	Row r = ws[page].getRow(titleBlock.devxy.yOrSnLabel.r);
            	if (r == null)
            	{
            		currentCol = col;
            		return;
            	}
                Cell c = r.getCell(col);
                if (c == null)
                {
                	currentCol = col;
                	return;
                }
                if (c.getCellType() == Cell_t.BLANK.type)
                {
                    currentCol = col;
                    return;
                }
                if (!options.noOverwrite)
                {
                	String sn = c.getStringCellValue();
                	if (options.onePage)
                	{
                	    r = ws[page].getRow(titleBlock.devxy.wafOrStepLabel.r);
                	    c = r.getCell(col);
                	    String step = c.getStringCellValue();
                	    if (step.equals(waferOrStep) && sn.equals(snOrXy.getSerialNumber()))
                	    {
                	    	currentCol = col;
                	    	return;
                	    }
                	}
                	else
                	{
                	    if (sn.equals(snOrXy.getSerialNumber()))
                	    {
                    	    currentCol = col;
                    	    break;
                	    }
                	}
                }
            }
        }
    }
    
    /*
    public void close() throws IOException
    {
        if (ws == null) return;
        for (int i=0; i<ws.length; i++)
        {
            Row r1 = ws[i].getRow(20);
            Cell c = r1.getCell(8);
            if (c != null) c.setAsActiveCell();
        }
        FileOutputStream fos = new FileOutputStream(options.xlsName);
        wb.write(fos);
        wb.close();
        fos.close();
    }                                 
    */
    
}
