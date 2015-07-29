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

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.*;

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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.SpreadSheetWriter;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.xlsx.layout2.CornerBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t;
import com.makechip.stdf2xls4.excel.xlsx.layout2.TitleBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.LegendBlock;
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
public class SpreadSheetWriter2 implements SpreadSheetWriter
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
    private int currentCol;
    private int testColumns;
    private Map<PageHeader, Integer> versionMap;
    private Map<SheetName, XSSFSheet> sheetMap;
    private TitleBlock titleBlock;

    public SpreadSheetWriter2(CliOptions options, StdfAPI api) throws IOException, InvalidFormatException
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
    public void generate() throws IOException, StdfException
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
    	final int pages = (devs.size() % colsPerPage == 0) ? devs.size() / colsPerPage : 1 + devs.size() / colsPerPage;
    	IntStream.range(0, pages).forEach(page -> writeResultsOnPage(hdr, page));
    }	
    	
    private void writeResultsOnPage(PageHeader hdr, int page)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
       	final int startIndex = page * colsPerPage;
       	final int endIndex = startIndex + colsPerPage > devs.size() ? devs.size() : startIndex + colsPerPage;
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
   	    int row = titleBlock.getFirstDataRow() + index;
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
				    setText(ws[page], currentCol, row, d.result);
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
    
    private void openSheet(PageHeader hdr) throws IOException, StdfException
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
        int pages = devs.size() / colsPerPage;
        if (devs.size() % colsPerPage != 0) pages++;
        ws = new XSSFSheet[pages];
        for (int page=0; page<pages; page++)
        {
        	int numDevices = (page == pages - 1) ? (devs.size() % colsPerPage == 0 ? colsPerPage : devs.size() % colsPerPage) : colsPerPage;
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
        	    Log.msg("headers are unequal: version = " + version);
        	    Log.msg("HEADER1 = " + hdr);
        	    Log.msg("HEADER2 = " + ph);
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
        		if (!checkRegistration(ws[page])) 
        		{
        			close();
        			throw new StdfException("Incompatible spreadsheet");
        		}
        	}
        }
    }
    
    private PageHeader getPageHeader(XSSFSheet s)
    {
    	String key = "";
    	int row = LegendBlock.HEIGHT;
    	Map<String, String> header  = new LinkedHashMap<>();
    	while (true)
    	{
    		Row r = s.getRow(row);
    		if (r.getCell(0).getCellType() == Cell_t.BLANK.type) break;
    	    key = r.getCell(0).getStringCellValue();
    	    if (key.trim().equals(HeaderBlock.OPTIONS_LABEL)) break;
    	    String value = r.getCell(HeaderBlock.VALUE_COL).getStringCellValue();
    	    header.put(key, value);
    	    row++;
    	    if (row > 100) throw new RuntimeException("Error header in spreadsheet is not compatible with this verison");
    	}
    	return(new PageHeader(header));
    }
    
    private void newSheet(int page, SheetName name, PageHeader hdr, int numDevices)
    {
    	ws[page] = wb.createSheet(name.toString());
    	sheetMap.put(name, ws[page]);
    	List<TestHeader> list = api.getTestHeaders(hdr);
    	titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, list);
    	titleBlock.addBlock(wb, ws[page]);
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
        int row = titleBlock.getFirstDataRow();
        Row r = existingSheet.getRow(row);
        while (r.getCell(titleBlock.getTestNameCol()).getCellType() != Cell_t.BLANK.type)
        {
        	r = existingSheet.getRow(row);
        	Cell tnameCell = r.getCell(titleBlock.getTestNameCol());
        	Cell tnumCell  = r.getCell(titleBlock.getTestNumberCol());
        	Cell dupCell   = r.getCell(titleBlock.getDupNumCol());
        	Cell loLimCell = r.getCell(titleBlock.getLoLimitCol());
        	Cell hiLimCell = r.getCell(titleBlock.getHiLimitCol());
        	Cell pnameCell = r.getCell(titleBlock.getPinNameCol());
        	Cell unitsCell = r.getCell(titleBlock.getUnitsCol());
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
        	row++;
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
    
    private boolean checkRegistration(XSSFSheet wsi) throws StdfException
    {
    	// Check for option compatibility:
    	int optRow = -1;
    	for (int row=LegendBlock.HEIGHT; row<100; row++)
    	{
    		Row r = wsi.getRow(row);
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
    	Row r = wsi.getRow(optRow);
    	String oldOpts = r.getCell(2).getStringCellValue();
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
    	int rrow = titleBlock.getFirstDataRow() - 1;
    	int rcol = titleBlock.getTestNameCol();
    	r = wsi.getRow(rrow);
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
    				setCell(wsi, col, titleBlock.getTimeStampRow(), cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    			setCell(wsi, col, titleBlock.getWaferOrStepRow(), cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeXY)
    			{
    				setCell(wsi, col, titleBlock.getTimeStampRow(), cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    		}
    		setCell(wsi, col, titleBlock.getXRow(), cs, dh.snxy.getX());
    		setCell(wsi, col, titleBlock.getYRow(), cs, dh.snxy.getY());
    	}
    	else // FT
    	{
    		if (options.onePage)
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				setCell(wsi, col, titleBlock.getTimeStampRow(), cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    			setCell(wsi, col, titleBlock.getWaferOrStepRow(), cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				setCell(wsi, col, titleBlock.getTimeStampRow(), cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    		}
    		setCell(wsi, col, titleBlock.getSnOrYRow(), cs, dh.snxy.getSerialNumber());
    	}
    	setCell(wsi, col, titleBlock.getHwBinRow(), cs, dh.hwBin);
    	setCell(wsi, col, titleBlock.getSwBinRow(), cs, dh.swBin);
    	setCell(wsi, col, titleBlock.getTempRow(), cs, dh.temperature);
    	if (dh.fail) setCell(wsi, col, titleBlock.getResultRow(), STATUS_FAIL_FMT.getFormat(wb), "FAIL");
    	else setCell(wsi, col, titleBlock.getResultRow(), cs, "PASS");
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
		Row r = ws.getRow(col);
		if (r == null) r = ws.createRow(col);
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
		Row r = ws.getRow(col);
		if (r == null) r = ws.createRow(col);
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
		Row r = ws.getRow(col);
		if (r == null) r = ws.createRow(col);
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
		Row r = ws.getRow(col);
		if (r == null) r = ws.createRow(col);
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
		Row r = ws.getRow(col);
		if (r == null) r = ws.createRow(col);
		setCell(r, col, cs, val);
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
        final int MAX_COLS = titleBlock.getFirstDataCol() + colsPerPage;
        if (wafersort)
        {
        	if (!options.noOverwrite)
        	{
        		for (int col=titleBlock.getFirstDataCol(); col<=titleBlock.getFirstDataCol() + MAX_COLS; col++)
        		{
        			Row r = ws[page].getRow(titleBlock.getXRow());
        			if (r == null)
        			{
        				currentCol = col;
        				break;
        			}
        			Cell cx = r.getCell(col);
        			if (cx.getCellType() == Cell_t.BLANK.type)
        			{
        				currentCol = col;
        				return;
        			}
        			r = ws[page].getRow(titleBlock.getYRow());
        			Cell cy = r.getCell(col);
        			short x = (short) cx.getNumericCellValue();
        			short y = (short) cy.getNumericCellValue();
        			if (options.onePage)
        			{
        				r = ws[page].getRow(titleBlock.getWaferOrStepRow());
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
            for (int col=titleBlock.getFirstDataCol(); col<=MAX_COLS; col++)
            {
            	Row r = ws[page].getRow(titleBlock.getXRow());
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
            for (int col=titleBlock.getFirstDataCol(); col<=titleBlock.getFirstDataRow() + MAX_COLS; col++)
            {
            	Row r = ws[page].getRow(titleBlock.getSnOrYRow());
                Cell c = r.getCell(col);
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
                	    r = ws[page].getRow(titleBlock.getWaferOrStepRow());
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
    
}
