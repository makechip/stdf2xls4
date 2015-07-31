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

import static com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t.*;

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
import com.makechip.stdf2xls4.excel.xlsx.layout1.CornerBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t;
import com.makechip.stdf2xls4.excel.xlsx.layout1.TitleBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout1.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.LegendBlock;
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
public class SpreadSheetWriter1 implements SpreadSheetWriter
{
	private final CliOptions options;
	private final StdfAPI api;
	private boolean noOverwrite;
    private HeaderBlock hb;	
    private CornerBlock cb;
	
    private static final float MISSING_FLOAT = Float.MAX_VALUE;
    public static final int MAX_ROWS = 1000000;
    
    private static final int colsPerPage = 200;
    private XSSFWorkbook wb = null;
    private XSSFSheet[] ws;
    private int sheetNum = 0;
    private int currentRow;
    private int testColumns;
    private List<TestHeader> testHeaders;
    private Map<PageHeader, Integer> versionMap;
    private Map<SheetName, XSSFSheet> sheetMap;
    private TitleBlock titleBlock;

    public SpreadSheetWriter1(CliOptions options, StdfAPI api) throws InvalidFormatException, IOException
    {
    	this.options = options;
    	this.api = api;
        versionMap = new HashMap<>();
        sheetMap = new IdentityHashMap<>();
        wb = null; 
        if (options.xlsName.exists()) wb = new XSSFWorkbook(options.xlsName);
        else wb = new XSSFWorkbook();
        Iterator<XSSFSheet> it = wb.iterator();
        while (it.hasNext())
        {
        	XSSFSheet sh = it.next();
        	String s = sh.getSheetName();
        	SheetName sn = SheetName.getSheet(s);
        	if (!s.equals(sn.toString())) 
        	{
        		throw new RuntimeException("Incorrectly formatted sheet name: " + s + " : " + sn);
        	}
        	sheetMap.put(sn, sh);
        }
        sheetNum = 0;
        noOverwrite = options.noOverwrite;
    }

    // 1. Make logo
    // 2. Make title block
    // 3. Make horizontal header
    // 4. Make vertical header
    // 5. Enter data
    public void generate() throws StdfException, IOException
    {
    	if (options.onePage)
    	{
    		openSheet(api.getPageHeaders().stream().findFirst().orElse(new PageHeader(new HashMap<String, String>())));
    		for (PageHeader hdr : api.getPageHeaders()) writeData(hdr);
    	}
    	else
    	{
    	    for (PageHeader hdr : api.getPageHeaders())
    		{
    	    	Log.msg("HDR: " + hdr.toString());
    			openSheet(hdr);
    		    writeData(hdr);
    		}
    	}
        close();
    }
        
    private void writeData(PageHeader hdr) 
    {
    	if (options.sort && options.showDuplicates) noOverwrite = true;
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	final int pages = (tests.size() % colsPerPage == 0) ? tests.size() / colsPerPage : 1 + tests.size() / colsPerPage;
        devs.stream().forEach(device -> 
        {
        	String waferOrStep = api.wafersort(hdr) ? hdr.get(HeaderUtil.WAFER_ID) : hdr.get(HeaderUtil.STEP);
            IntStream.range(0, pages).forEach(page -> 
            {    	
    	        locateRow(hdr, waferOrStep, device.snxy, page);
            	final int startIndex = page * colsPerPage;
            	final int endIndex = startIndex + colsPerPage > tests.size() ? tests.size() : startIndex + colsPerPage;
           	    IntStream.range(startIndex, endIndex).forEach(index ->
           	    {
           	    	int col = titleBlock.getFirstDataCol() + index - startIndex;
           	    	TestHeader th = tests.get(index);
           			TestResult r = api.getRecord(hdr, device, th);
           			//Log.msg("xy = " + sn + " id = " + id + " result = " + r);
           			try
           			{
           			if (r != null)
           			{
           				setDevice(ws[page], waferOrStep, currentRow, device);
           				if (r instanceof ParametricTestResult)
           				{
           					ParametricTestResult p = (ParametricTestResult) r;
           					setValue(ws[page], col, currentRow, p);
           				}
           				else if (r instanceof DatalogTestResult)
           				{
           					DatalogTestResult d = (DatalogTestResult) r;
           					setText(ws[page], col, currentRow, d.result);
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
           		});
            });
        });
    } 
    
    private void openSheet(PageHeader hdr) throws StdfException, IOException
    {
        int numTests = api.getTestHeaders(hdr).size();
        int pages = numTests / colsPerPage;
        if (numTests % colsPerPage != 0) pages++;
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
        	    Log.msg("headers are unequal: version = " + version);
        	    Log.msg("HEADER1 = " + hdr);
        	    Log.msg("HEADER2 = " + ph);
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
    	        titleBlock = new TitleBlock(hdr, options.logoFile, sname.toString(), api.wafersort(hdr), options, null);
        		if (!checkRegistration(ws[page])) 
        		{
        			close();
        			throw new StdfException("Incompatible spreadsheet");
        		}
        		//testHeaders = getTestHeaders(ws[page]);
        	}
        }
    }
    
    private PageHeader getPageHeader(XSSFSheet s)
    {
    	String key = "";
    	int row = 0;
    	Map<String, String> header  = new LinkedHashMap<>();
    	while (true)
    	{
    		Row r = s.getRow(row);
    		if (r == null) break;
    		if (r.getCell(0).getCellType() == Cell_t.BLANK.type) break;
    	    key = r.getCell(0).getStringCellValue();
    	    if (key.trim().equals(HeaderBlock.OPTIONS_LABEL)) break;
    	    Cell c = r.getCell(HeaderBlock.VALUE_COL);
    	    String value = (c == null) ? "" : c.getStringCellValue();
    	    header.put(key, value);
    	    row++;
    	    if (row > 100) throw new RuntimeException("Error header in spreadsheet is not compatible with this verison");
    	}
    	return(new PageHeader(header));
    }
    
    private void newSheet(int page, SheetName name, PageHeader hdr)
    {
    	ws[page] = wb.createSheet(name.toString());
    	sheetMap.put(name, ws[page]);
    	List<TestHeader> list = getTestHeaders(hdr, page);
    	titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), options, list);
    	titleBlock.addBlock(wb, ws[page]);
    }
    
    private List<TestHeader> getTestHeaders(PageHeader hdr, int pageIndex)
    {
        List<TestHeader> plist = new ArrayList<>(colsPerPage);
        List<TestHeader> list = api.getTestHeaders(hdr);
        int start = colsPerPage * pageIndex;
        int end = start + colsPerPage;
        int cols = 0;
        for (int i=start; i<end && i<list.size(); i++) 
        {
        	plist.add(list.get(i));
        	cols++;
        }
        testColumns = cols;
        return(plist);
    }
    
    private Cell getExistingCell(XSSFSheet s, int col, int row)
    {
    	Row r = s.getRow(row);
    	return(r.getCell(col));
    }
    
    private List<TestHeader> getTestHeaders(XSSFSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(colsPerPage);
        int cnt = 0;
        for (int i=titleBlock.getFirstDataCol(); i<titleBlock.getFirstDataCol()+colsPerPage; i++)
        {
        	Cell tnameCell = getExistingCell(existingSheet, i, titleBlock.getTestNameRow());
        	Cell tnumCell  = getExistingCell(existingSheet, i, titleBlock.getTestNumberRow());
        	Cell dupCell   = getExistingCell(existingSheet, i, titleBlock.getDupNumRow());
        	Cell loLimCell = getExistingCell(existingSheet, i, titleBlock.getLoLimitRow());
        	Cell hiLimCell = getExistingCell(existingSheet, i, titleBlock.getHiLimitRow());
        	Cell pnameCell = getExistingCell(existingSheet, i, titleBlock.getPinNameRow());
        	Cell unitsCell = getExistingCell(existingSheet, i, titleBlock.getUnitsRow());
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
                   	float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? MISSING_FLOAT : (float) loLimCell.getNumericCellValue();
                   	float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? MISSING_FLOAT : (float) hiLimCell.getNumericCellValue();
                    String pin = pnameCell.getStringCellValue();
                    plist.add(new MultiParametricTestHeader(tname, tnum, dnum, pin, units, lolim, hilim));
                }
        	}
        	else // no pin, so either a normal Parametric test or Functional or Datalog test
        	{
        	    if (loLimCell.getCellType() != Cell_t.BLANK.type || hiLimCell.getCellType() != Cell_t.BLANK.type)
        	    {
                   	float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? MISSING_FLOAT : (float) loLimCell.getNumericCellValue();
                   	float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? MISSING_FLOAT : (float) hiLimCell.getNumericCellValue();
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
    
    private StdfException optionError(boolean oldHas, String opt)
    {
    	String e = null;
    	if (oldHas)
    	{
    	    e = "Existing spreadsheet created with " + opt + " option - try adding " + opt + " command-line switch.";	
    	}
    	else
    	{
    		e = "Existing spreadsheet not created with " + opt + " option - try removing " + opt + " command-line switch.";
    	}
    	return(new StdfException(e));
    }
    
    private boolean checkRegistration(XSSFSheet ws) throws StdfException
    {
    	// Check for option compatibility:
    	int optRow = -1;
    	for (int row=0; row<100; row++)
    	{
    		Row r = ws.getRow(row);
    		Cell c = r.getCell(0);
    		if (c.getCellType() == Cell_t.STRING.type)
    		{
    		    String s = c.getStringCellValue();
    		    if (s.equals("OPTIONS:"))
    		    {
    		    	optRow = row;
    		    	break;
    		    }
    		}
    	}
    	if (optRow < 0) throw new StdfException("Existing spreadsheet is incompatible");
    	Row r = ws.getRow(optRow);
    	String oldOpts = r.getCell(3).getStringCellValue();
    	Log.msg("oldOpts = " + oldOpts);
    	// -b onePage mismatch = error
    	if (oldOpts.contains("-b") && !options.onePage) throw optionError(true, "-b");
    	if (!oldOpts.contains("-b") && options.onePage) throw optionError(false, "-b");
    	// -v dontSkipSearchFails mismatch = error
    	if (oldOpts.contains("-v") && !options.dontSkipSearchFails) throw optionError(true, "-v");
        if (!oldOpts.contains("-v") && options.dontSkipSearchFails) throw optionError(false, "-v");	
    	// -r rotate mismatch = error
        if (oldOpts.contains("-r") && !options.rotate) throw optionError(true, "-r");
        if (!oldOpts.contains("-r") && options.rotate) throw optionError(false, "-r");
    	// -y dynamicLimits = error
        if (oldOpts.contains("-y") && !options.rotate) throw optionError(true, "-r");
        if (!oldOpts.contains("-y") && options.rotate) throw optionError(false, "-r");
    	// -n noWrapTestNames mismatch = warning
        if (oldOpts.contains("-n") && !options.noWrapTestNames)
        {
        	Log.warning("Existing spreadsheet does not wrap test names - will assume -n option is set.");
        }
        if (!oldOpts.contains("-n") && options.noWrapTestNames)
        {
        	Log.warning("Existing spreadsheet wraps test names - ignoring -n option.");
        }
    	// -p precision mismatch = warning
        String oldp = oldOpts.substring(oldOpts.lastIndexOf(' ')).trim();
        if (Character.isDigit(oldp.charAt(0)))
        {
        	Integer n = new Integer(oldp);
        	if (options.precision != n.intValue())
        	{
        		Log.warning("Existing spreadsheet uses different precision.");
        	}
        }
    	// -a pinSuffix mismatch = warning
        if (oldOpts.contains("-a") && !options.pinSuffix)
        {
        	Log.warning("Existing spreadsheet uses -a option, while current run does not.");
        }
        if (!oldOpts.contains("-a") && options.pinSuffix)
        {
            Log.warning("Existing spreadsheet does not use -a option, while current run does.");
        }
    	int rcol = titleBlock.getFirstDataCol() - 1;
    	int rrow = titleBlock.getTestNameRow();
    	r = ws.getRow(rrow);
    	Cell c = r.getCell(rcol);
    	Log.msg("col = " + rcol + " row = " + rrow);
    	Log.msg("CELL TYPE = " + c.getCellType());
    	Log.msg("CELL CONTENTS = " + c.getStringCellValue());
    	if (c.getCellType() == Cell_t.STRING.type)
    	{
    		String text = c.getStringCellValue();
    		if (text.equals(CornerBlock.LABEL_TEST_NAME)) return(true);
    	}
    	return(false);
    }
    
	private void setCell(Row r, int col, CellStyle cs, String val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	private void setCell(XSSFSheet ws, int col, int row, CellStyle cs, String val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	private void setCell(Row r, int col, CellStyle cs, long val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	private void setCell(XSSFSheet ws, int col, int row, CellStyle cs, long val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	private void setCell(Row r, int col, CellStyle cs, int val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	private void setCell(XSSFSheet ws, int col, int row, CellStyle cs, int val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	private void setCell(Row r, int col, CellStyle cs, float val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	private void setCell(XSSFSheet ws, int col, int row, CellStyle cs, float val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	private void setCell(Row r, int col, CellStyle cs, double val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	private void setCell(XSSFSheet ws, int col, int row, CellStyle cs, double val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

    private void setStatus(XSSFSheet wsi, int col, int row, TestResult r)
    {
    	Row rw = wsi.getRow(row);
    	if (rw == null) rw = wsi.createRow(row);
        switch (r.error)
        {
        case PASS:       setCell(rw, col, STATUS_PASS_FMT.getFormat(wb), "PASS"); break;
        case FAIL:       setCell(rw, col, STATUS_FAIL_FMT.getFormat(wb), "FAIL"); break;
        case INVALID:    setCell(rw, col, STATUS_INVALID_FMT.getFormat(wb), "FAIL"); break;
        case UNRELIABLE: setCell(rw, col, STATUS_UNRELIABLE_FMT.getFormat(wb), "FAIL"); break;
        case ALARM:      setCell(rw, col, STATUS_ALARM_FMT.getFormat(wb), "FAIL"); break;
        case TIMEOUT:    setCell(rw, col, STATUS_TIMEOUT_FMT.getFormat(wb), "FAIL"); break;
        default:         setCell(rw, col, STATUS_ABORT_FMT.getFormat(wb), "FAIL"); break;
        }
    }
    
    private void setText(XSSFSheet wsi, int col, int row, String text)
    {
        String s = text.trim();
        int size = wsi.getColumnWidth(col);
        if (size < (s.length() * 256))
        {   
            wsi.setColumnWidth(col, 256*(14 * s.length())/10);
        }   
        setCell(wsi, col, row,  STATUS_PASS_FMT.getFormat(wb), text.trim());
    }
    
    private void setDevice(XSSFSheet wsi, String waferOrStep, int row, DeviceHeader dh)
    {
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
            	setCell(wsi, titleBlock.getWaferOrStepCol(), row, cs, waferOrStep);
            }
            else
            {
            	if (dh.snxy instanceof TimeXY)
            	{
            	    setCell(wsi, 0, row, cs, ((TimeXY) dh.snxy).getTimeStamp());
            	    wsi.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
            	}
            }
            setCell(wsi, titleBlock.getXCol(), row, cs, dh.snxy.getX());
            setCell(wsi, titleBlock.getYCol(), row, cs, dh.snxy.getY());
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
            	setCell(wsi, titleBlock.getWaferOrStepCol(), row, cs, waferOrStep);
            }
            else
            {
                if (dh.snxy instanceof TimeSN)
                {
            	    setCell(wsi, 1, row, cs, ((TimeSN) dh.snxy).getTimeStamp());
            	    wsi.addMergedRegion(new CellRangeAddress(row, row, 1, 2));
                }
            }
            setCell(wsi, titleBlock.getSnOrYCol(), row, cs, dh.snxy.getSerialNumber());
        }
        setCell(wsi, titleBlock.getHwBinCol(), row, cs, dh.hwBin);
        setCell(wsi, titleBlock.getSwBinCol(), row, cs, dh.swBin);
        setCell(wsi, titleBlock.getTempCol(), row, cs, dh.temperature);

        if (dh.fail) setCell(wsi, titleBlock.getResultCol(), row, STATUS_FAIL_FMT.getFormat(wb), "FAIL");
        else setCell(wsi, titleBlock.getResultCol(), row, cs, "PASS");
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
    
    private void locateRow(PageHeader hdr, String waferOrStep, SnOrXy snOrXy, int page)
    {
        TIntArrayList xlist = new TIntArrayList();
        if (api.wafersort(hdr))
        {
        	if (!options.noOverwrite)
        	{
        		for (int row=titleBlock.getFirstDataRow(); row<=MAX_ROWS; row++)
        		{
        			Row r = ws[page].getRow(row);
        			if (r == null)
        			{
        				currentRow = row;
        				break;
        			}
        			Cell cx = r.getCell(titleBlock.getXCol());
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
        			Cell cy = r.getCell(titleBlock.getYCol());
        			short x = (short) cx.getNumericCellValue();
        			short y = (short) cy.getNumericCellValue();
        			if (options.onePage)
        			{
        				String waf = r.getCell(titleBlock.getWaferOrStepCol()).getStringCellValue();
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
            for (int i=titleBlock.getFirstDataRow(); i<=MAX_ROWS; i++)
            {
            	Row r = ws[page].getRow(i);
            	if (r == null)
            	{
            		currentRow = i;
            		break;
            	}
                Cell c = r.getCell(titleBlock.getXCol());
                if (c.getCellType() == Cell_t.BLANK.type)
                {
                    currentRow = i;
                    break;
                }
            }
        }
        else // final test
        {
            for (int row=titleBlock.getFirstDataRow(); row<=MAX_ROWS; row++)
            {
            	Row r = ws[page].getRow(row);
            	if (r == null)
            	{
            		currentRow = row;
            		break;
            	}
                Cell c = r.getCell(titleBlock.getSnOrYCol());
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
    
    public void close() throws IOException
    {
        if (ws == null) return;
        /*
        for (int i=0; i<ws.length; i++)
        {
            Row r1 = ws[i].getRow(20);
            Cell c = r1.getCell(8);
            if (c != null) c.setAsActiveCell();
        }
        */
        FileOutputStream fos = new FileOutputStream(options.xlsName);
        wb.write(fos);
        wb.close();
        fos.close();
    }                                 
    
}
