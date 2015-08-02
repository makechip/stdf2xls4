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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
 * with test headers running across the page, and devices running down the page.
 * The layout of the spreadsheet is as follows:<br>
 * <br>
 * <pre> 
      |   0  |   1  |   2  |  3   |   4  |   5  |   6  |   7  |   8  |   9  |
     -+------+------+------+------+------+------+------+------+------+------+
     0|                                                       |
     -+                                                       +
      |                                                       |
     -+                                                       +
      |                                                       |
     -+                                                       +
      |                                                       |
     -+                  HeaderBlock                          +   PageTitleBlock ---->
      |                                                       |
     -+                                                       +
      |                                                       |
     -+                                                       +
      |                                                       |
     -+                                                       +
     n|                                                       |
     -+------+------+------+------+------+------+------+------+------+------+
   1+n|                    |                           |      |
   1+n|                    |                           |      | 
   1+n|                    |                           |      | TestNames ->
   1+n|                    |                           |      |
     -+                    +                           +      + 
      |                    |                           |  C   | TestNumbers ->
     -+                    +                           +  o   +
      |                    |                           |  r   | Duplicate numbers ->
     -+                    +                           +  n   +
      |  LegendBlock       |       LogoBlock           |  e   | LoLimits ->
     -+                    +                           +  r   +                 DataBlock ---->
      |                    |                           |  B   | HiLimits ->
     -+                    +                           +  l   +
      |                    |                           |  o   | PinNames ->
     -+                    +                           +  c   +
   7+n|                    |                           |  k   |
     -+------+------+------+------+------+------+------+      + Units ->
   8+n|      |  wf  ?stp/x ? y/sn   hbin   sbin   rslt   temp |
     -+      +------+------+------+------+------+------+------+------+------+
   9+n|                                                       |
     -+                            Device Info                +
      |                                 |                     |
     -+                                 |                     +       Test Results  ---->
      |                                 V                     |             |
      +                                                       +             |
      |                                                       |             V
</pre> 
 * 
 * The actual spreadsheet looks like this:
 * <p> <IMG SRC="{@docRoot}/doc-files/xls1.png"> 
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
public class SpreadSheetWriter1 extends SpreadsheetWriter implements SpreadSheetWriter
{
    private HeaderBlock hb;	
    private CornerBlock cb;
	
    private int currentRow;
    private int testColumns;
    private List<TestHeader> testHeaders;
    private TitleBlock titleBlock;

    public SpreadSheetWriter1(CliOptions options, StdfAPI api) throws InvalidFormatException, IOException
    {
    	super(options, api);
    }

    protected void writeData(PageHeader hdr) 
    {
    	if (options.sort && options.showDuplicates) noOverwrite = true;
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	final int pages = (tests.size() % COLS_PER_PAGE == 0) ? tests.size() / COLS_PER_PAGE : 1 + tests.size() / COLS_PER_PAGE;
    	IntStream.range(0,  pages).forEach(page -> writeResultsOnPage(hdr, page));
    }
    
    private void writeResultsOnPage(PageHeader hdr, int page)
    {
        List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
        String waferOrStep = api.wafersort(hdr) ? hdr.get(HeaderUtil.WAFER_ID) : hdr.get(HeaderUtil.STEP);
        IntStream.range(0, devs.size()).forEach(devIndex -> writeDeviceResults(hdr, waferOrStep, page, devIndex));
    }
    
    private void writeDeviceResults(PageHeader hdr, String waferOrStep, int page, int devIndex)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	DeviceHeader dh = devs.get(devIndex);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	locateRow(api.wafersort(hdr), waferOrStep, dh.snxy, page);
    	setDevice(page, waferOrStep, devs.get(devIndex));
        final int startIndex = page * COLS_PER_PAGE;
        final int endIndex = startIndex + COLS_PER_PAGE > tests.size() ? tests.size() : startIndex + COLS_PER_PAGE;
        IntStream.range(startIndex,  endIndex).forEach(testIndex -> setResult(testIndex, page, tests.get(testIndex), hdr, dh));
    }    
        
    private void setResult(int testIndex, int page, TestHeader th, PageHeader hdr, DeviceHeader dh)
    {
    	int col = titleBlock.tstxy.unitsLabel.c+1 + (testIndex % COLS_PER_PAGE);
    	TestResult r = api.getRecord(hdr, dh, th);
        try 
        {
        	Log.msg("r = " + r);
        	if (r != null)
        	{
        		if (r instanceof ParametricTestResult)
        		{
        			ParametricTestResult p = (ParametricTestResult) r;
        			setValue(ws[page], col, currentRow, p);
        		}
        		else if (r instanceof DatalogTestResult)
        		{
        			DatalogTestResult d = (DatalogTestResult) r;
        			setText(ws[page], col, currentRow, STATUS_PASS_FMT, d.result);
        		}
        		else // a functional test
        		{
        			setStatus(ws[page], col, currentRow, r);
        		}
        	}
        }
        catch (Exception e)
        {
        	throw new RuntimeException(e.getMessage());
        }
    }
    	
    protected void openSheet(PageHeader hdr) throws StdfException, IOException
    {
        int numTests = api.getTestHeaders(hdr).size();
        int pages = numTests / COLS_PER_PAGE;
        if (numTests % COLS_PER_PAGE != 0) pages++;
        ws = new XSSFSheet[pages];
        for (int page=0; page<pages; page++)
        {
        	SheetName sname = null;
        	int version = 0;
        	while (true)
        	{
        		ws[page] = null;
        	    sname = SheetName.getExistingSheetName(api.wafersort(hdr), hdr, page+1, version);
        	    Log.msg("existing sheet name = " + sname);
        	    if (sname == null) break;
        	    ws[page] = sheetMap.get(sname);
        	    if (ws[page] == null) 
        	    {
        	    	Log.msg("page is null");
        	    	break;
        	    }
        	    PageHeader ph = getPageHeader(ws[page]);
        	    if (ph.equals(hdr)) break;
        	    version++;
        	}
        	if (ws[page] == null) 
        	{
        		Log.msg("getting new sheet");
        		sname = SheetName.getSheet(api.wafersort(hdr), hdr, page+1, version);
        		Log.msg("new sheet name = " + sname);
        		newSheet(page, sname, hdr);
        		testHeaders = getTestHeaders(hdr, page);
        	}
        	else // note: an existing sheet might have a titleblock that is incompatible with the current titleblock.
        	{
        		Log.msg("checking exising sheet...");
    	        //List<TestHeader> list = getTestHeaders(ws[page]);
    	        titleBlock = new TitleBlock(hdr, options.logoFile, sname.toString(), api.wafersort(hdr), api.timeStampedFiles, options, 0, null);
        		if (!checkRegistration(ws[page], LegendBlock.HEIGHT, titleBlock.tstxy.tnameLabel.c, titleBlock.tstxy.tnameLabel.r)) 
        		{
        			close();
        			throw new StdfException("Incompatible spreadsheet");
        		}
        		//testHeaders = getTestHeaders(ws[page]);
        	}
        }
    }
    
    private void newSheet(int page, SheetName name, PageHeader hdr)
    {
    	ws[page] = wb.createSheet(name.toString());
    	sheetMap.put(name, ws[page]);
    	List<TestHeader> list = getTestHeaders(hdr, page);
    	titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), api.timeStampedFiles, options, 0, list);
    	titleBlock.addBlock(wb, ws[page]);
    }
    
    private List<TestHeader> getTestHeaders(PageHeader hdr, int pageIndex)
    {
        List<TestHeader> plist = new ArrayList<>(COLS_PER_PAGE);
        List<TestHeader> list = api.getTestHeaders(hdr);
        int start = COLS_PER_PAGE * pageIndex;
        int end = start + COLS_PER_PAGE;
        int cols = 0;
        for (int i=start; i<end && i<list.size(); i++) 
        {
        	plist.add(list.get(i));
        	cols++;
        }
        testColumns = cols;
        return(plist);
    }
    
    private List<TestHeader> getTestHeaders(XSSFSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(COLS_PER_PAGE);
        int cnt = 0;
        for (int i=titleBlock.tstxy.unitsLabel.c+1; i<titleBlock.tstxy.unitsLabel.c+1+COLS_PER_PAGE; i++)
        {
        	Cell tnameCell = getExistingCell(existingSheet, i, titleBlock.tstxy.tname.r);
        	Cell tnumCell  = getExistingCell(existingSheet, i, titleBlock.tstxy.tnum.r);
        	Cell dupCell   = getExistingCell(existingSheet, i, titleBlock.tstxy.dupNum.r);
        	Cell loLimCell = getExistingCell(existingSheet, i, titleBlock.tstxy.loLim.r);
        	Cell hiLimCell = getExistingCell(existingSheet, i, titleBlock.tstxy.hiLim.r);
        	Cell pnameCell = getExistingCell(existingSheet, i, titleBlock.tstxy.pin.r);
        	Cell unitsCell = getExistingCell(existingSheet, i, titleBlock.tstxy.units.r);
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
        }
        return(plist);
    }
    
    private void setDevice(int page, String waferOrStep, DeviceHeader dh)
    {
    	XSSFSheet wsi = ws[page];
    	int row = currentRow;
    	CellStyle cs = STATUS_PASS_FMT.getFormat(wb);
        if (dh.snxy instanceof XY || dh.snxy instanceof TimeXY) // wafersort
        {
            if (options.onePage)
            {
            	if (dh.snxy instanceof TimeXY)
            	{
            	    wsi.setColumnWidth(0, 256 * 15);	
            	    setCell(wsi, 0, row, cs, ((TimeXY) dh.snxy).getTimeStamp());
            	}
            	setCell(wsi, titleBlock.devxy.wafOrStepLabel.c, row, cs, waferOrStep);
            }
            else
            {
            	if (dh.snxy instanceof TimeXY)
            	{
            	    setCell(wsi, 0, row, cs, ((TimeXY) dh.snxy).getTimeStamp());
            	    wsi.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
            	}
            }
            setCell(wsi, titleBlock.devxy.xLabel.c, row, cs, dh.snxy.getX());
            setCell(wsi, titleBlock.devxy.yOrSn.c, row, cs, dh.snxy.getY());
        }
        else // FT
        {
            if (options.onePage)
            {
                if (dh.snxy instanceof TimeSN)
                {
            	    setCell(wsi, 0, row, cs, ((TimeSN) dh.snxy).getTimeStamp());
            	    wsi.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
                }
            	setCell(wsi, titleBlock.devxy.wafOrStepLabel.c, row, cs, waferOrStep);
            }
            else
            {
                if (dh.snxy instanceof TimeSN)
                {
            	    setCell(wsi, 1, row, cs, ((TimeSN) dh.snxy).getTimeStamp());
            	    wsi.addMergedRegion(new CellRangeAddress(row, row, 1, 2));
                }
            }
            setCell(wsi, titleBlock.devxy.yOrSnLabel.c, row, cs, dh.snxy.getSerialNumber());
        }
        setCell(wsi, titleBlock.devxy.hwBinLabel.c, row, cs, dh.hwBin);
        setCell(wsi, titleBlock.devxy.swBinLabel.c, row, cs, dh.swBin);
        setCell(wsi, titleBlock.devxy.tempLabel.c, row, cs, dh.temperature);

        if (dh.fail) setCell(wsi, titleBlock.devxy.rsltLabel.c, row, STATUS_FAIL_FMT.getFormat(wb), "FAIL");
        else setCell(wsi, titleBlock.devxy.rsltLabel.c, row, cs, "PASS");
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
    
    private void locateRow(boolean wafersort, String waferOrStep, SnOrXy snOrXy, int page)
    {
        TIntArrayList xlist = new TIntArrayList();
        if (wafersort)
        {
        	if (!options.noOverwrite)
        	{
        		for (int row=titleBlock.devxy.tempLabel.r+1; row<=MAX_ROWS; row++)
        		{
        			Row r = ws[page].getRow(row);
        			if (r == null)
        			{
        				currentRow = row;
        				break;
        			}
        			Cell cx = r.getCell(titleBlock.devxy.xLabel.c);
        			if (cx == null)
        			{
        				currentRow = row;
        				return;
        			}
        			if (cx.getCellType() == Cell_t.BLANK.type)
        			{
        				currentRow = row;
        				return;
        			}
        			Cell cy = r.getCell(titleBlock.devxy.yOrSnLabel.c);
        			short x = (short) cx.getNumericCellValue();
        			short y = (short) cy.getNumericCellValue();
        			if (options.onePage)
        			{
        				String waf = r.getCell(titleBlock.devxy.wafOrStepLabel.c).getStringCellValue();
                        if (waf.equals(waferOrStep) && x == snOrXy.getX() && y == snOrXy.getY())
                        {
                        	currentRow = row;
                        	return;
                        }
        			}
        			else
        			{
        				if (x == snOrXy.getX() && y == snOrXy.getY())
        				{
        					currentRow = row;
        					return;
        				}
        			}
        		}
        	}
            for (int i=titleBlock.devxy.tempLabel.r+1; i<=MAX_ROWS; i++)
            {
            	Row r = ws[page].getRow(i);
            	if (r == null)
            	{
            		currentRow = i;
            		break;
            	}
                Cell c = r.getCell(titleBlock.devxy.xLabel.c);
                if (c.getCellType() == Cell_t.BLANK.type)
                {
                    currentRow = i;
                    break;
                }
            }
        }
        else // final test
        {
            for (int row=titleBlock.devxy.tempLabel.r+1; row<=MAX_ROWS; row++)
            {
            	Row r = ws[page].getRow(row);
            	if (r == null)
            	{
            		currentRow = row;
            		break;
            	}
                Cell c = r.getCell(titleBlock.devxy.yOrSnLabel.c);
                if (c == null)
                {
                	currentRow = row;
                	return;
                }
                if (c.getCellType() == Cell_t.BLANK.type)
                {
                    currentRow = row;
                    return;
                }
                if (!options.noOverwrite)
                {
                	String sn = c.getStringCellValue();
                	if (sn.equals(snOrXy.getSerialNumber()))
                	{
                    	currentRow = row;
                    	break;
                	}
                }
            }
        }
    }
    
}
