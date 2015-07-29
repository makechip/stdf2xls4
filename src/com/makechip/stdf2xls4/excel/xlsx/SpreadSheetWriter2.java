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

import static com.makechip.stdf2xls4.excel.xls.layout1.Format_t.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.SpreadSheetWriter;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.xls.layout2.CornerBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.Format_t;
import com.makechip.stdf2xls4.excel.xls.layout2.TitleBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.DataHeader;
import com.makechip.stdf2xls4.excel.xls.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.LegendBlock;
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
    private WritableWorkbook wb = null;
    private WritableSheet[] ws;
    private int sheetNum = 0;
    private int currentCol;
    private CellView tnumView     = new CellView();
    private CellView loLimitView  = new CellView();
    private CellView hiLimitView  = new CellView();
    private CellView statusView   = new CellView();
    private CellView unitsView    = new CellView();
    private CellView hdrView      = new CellView();
    private int testColumns;
    private Map<PageHeader, Integer> versionMap;
    private Map<SheetName, WritableSheet> sheetMap;
    private TitleBlock titleBlock;

    public SpreadSheetWriter2(CliOptions options, StdfAPI api) throws IOException, BiffException, WriteException
    {
    	this.options = options;
    	this.api = api;
        versionMap = new HashMap<>();
        sheetMap = new IdentityHashMap<>();
        wb = null; 
       	if (options.xlsName.exists()) 
       	{
       		Workbook w = Workbook.getWorkbook(options.xlsName);
       		wb = Workbook.createWorkbook(options.xlsName, w);
       		// create SheetName objects for existing pages:
       		Arrays.stream(wb.getSheetNames()).forEach(s -> 
       		{
       			SheetName sn = SheetName.getSheet(s);
       			if (!s.equals(sn.toString())) 
       			{
       				throw new RuntimeException("Incorrectly formatted sheet name: " + s + " : " + sn);
       			}
       			sheetMap.put(sn, wb.getSheet(s));
       		});
       	}
       	else 
        {
       		wb = Workbook.createWorkbook(options.xlsName);
        }
       	wb.setColourRGB(Colour.SKY_BLUE, 82, 123, 188);
        sheetNum = 0;
        HEADER1_FMT.getFormat().setWrap(true);
        tnumView.setSize(18*256);
        loLimitView.setSize(16*256);
        hiLimitView.setSize(16*256);
        statusView.setSize(16*256);
        unitsView.setSize(8*256);
        hdrView.setSize(14*256);
        noOverwrite = options.noOverwrite;
    }

    // 1. Make logo
    // 2. Make title block
    // 3. Make horizontal header
    // 4. Make vertical header
    // 5. Enter data
    public void generate() throws RowsExceededException, WriteException, IOException, StdfException
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
        
    private void writeData(PageHeader hdr) throws RowsExceededException, WriteException 
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
    
    private void openSheet(PageHeader hdr) throws RowsExceededException, WriteException, IOException, StdfException
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
        int pages = devs.size() / colsPerPage;
        if (devs.size() % colsPerPage != 0) pages++;
        ws = new WritableSheet[pages];
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
    
    private PageHeader getPageHeader(Sheet s)
    {
    	String key = "";
    	int row = 0;
    	Map<String, String> header  = new LinkedHashMap<>();
    	while (true)
    	{
    		if (s.getCell(0, row).getType() == CellType.EMPTY) break;
    	    key = s.getCell(0, row).getContents();
    	    if (key.trim().equals(HeaderBlock.OPTIONS_LABEL)) break;
    	    String value = s.getCell(HeaderBlock.VALUE_COL, row).getContents();
    	    header.put(key, value);
    	    row++;
    	    if (row > 100) throw new RuntimeException("Error header in spreadsheet is not compatible with this verison");
    	}
    	return(new PageHeader(header));
    }
    
    private void newSheet(int page, SheetName name, PageHeader hdr, int numDevices) throws RowsExceededException, WriteException, IOException
    {
    	ws[page] = wb.createSheet(name.toString(), page);
    	sheetMap.put(name, ws[page]);
    	List<TestHeader> list = api.getTestHeaders(hdr);
    	titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, list);
    	titleBlock.addBlock(ws[page]);
    	CellView c1 = ws[page].getRowView(6);
    	CellView c2 = ws[page].getRowView(7);
    	Log.msg("row6 height = " + c1.getSize() + " row7 height = " + c2.getSize());
    }
    
    private List<TestHeader> getTestHeaders(WritableSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(colsPerPage);
        int cnt = 0;
        int row = titleBlock.getFirstDataRow();
        while (existingSheet.getCell(titleBlock.getFirstDataRow(), titleBlock.getTestNameCol()).getType() != CellType.EMPTY)
        {
        	Cell tnameCell = existingSheet.getCell(titleBlock.getTestNameCol(), row);
        	Cell tnumCell  = existingSheet.getCell(titleBlock.getTestNumberCol(), row);
        	Cell dupCell   = existingSheet.getCell(titleBlock.getDupNumCol(), row);
        	Cell loLimCell = existingSheet.getCell(titleBlock.getLoLimitCol(), row);
        	Cell hiLimCell = existingSheet.getCell(titleBlock.getHiLimitCol(), row);
        	Cell pnameCell = existingSheet.getCell(titleBlock.getPinNameCol(), row);
        	Cell unitsCell = existingSheet.getCell(titleBlock.getUnitsCol(), row);
        	String tname = tnameCell.getContents();
           	long tnum = (long) ((Number) tnumCell).getValue();
           	int dnum = (int) ((Number) dupCell).getValue();
        	if (pnameCell.getType() == CellType.LABEL) 
        	{
               	String units = unitsCell.getContents();
                if (loLimCell.getType() == CellType.EMPTY && hiLimCell.getType() == CellType.EMPTY)
                {
                    if (pnameCell.getContents().equals(MultiParametricTestHeader.LL_HDR)) // A LoLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.LO_LIMIT));
                    }
                    else if (pnameCell.getContents().equals(MultiParametricTestHeader.HL_HDR)) // A HiLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.HI_LIMIT));
                    }
                    else throw new StdfException("Malformed test header: testName = " + tnameCell.getContents());
                }
                else // either a ParametricTest with pin, or a MultiParametricTest
                {
                   	float lolim = (loLimCell.getType() == CellType.EMPTY) ? MISSING_FLOAT : (float) ((Number) loLimCell).getValue();
                   	float hilim = (hiLimCell.getType() == CellType.EMPTY) ? MISSING_FLOAT : (float) ((Number) hiLimCell).getValue();
                    String pin = pnameCell.getContents();
                    plist.add(new MultiParametricTestHeader(tname, tnum, dnum, pin, units, lolim, hilim));
                }
        	}
        	else // no pin, so either a normal Parametric test or Functional or Datalog test
        	{
        	    if (loLimCell.getType() != CellType.EMPTY || hiLimCell.getType() != CellType.EMPTY)
        	    {
                   	float lolim = (loLimCell.getType() == CellType.EMPTY) ? MISSING_FLOAT : (float) ((Number) loLimCell).getValue();
                   	float hilim = (hiLimCell.getType() == CellType.EMPTY) ? MISSING_FLOAT : (float) ((Number) hiLimCell).getValue();
                   	String units = unitsCell.getContents();
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
    
    private boolean checkRegistration(WritableSheet ws) throws StdfException
    {
    	// Check for option compatibility:
    	int optRow = -1;
    	for (int row=LegendBlock.HEIGHT; row<100; row++)
    	{
    		Cell c = ws.getCell(0, row);
    		if (c.getType() == CellType.LABEL)
    		{
    		    String s = c.getContents();
    		    Log.msg("s = '" + s + "'");
    		    if (s.equals("OPTIONS:"))
    		    {
    		    	optRow = row;
    		    	Log.msg("optRow = " + optRow);
    		    	break;
    		    }
    		}
    	}
    	if (optRow < 0) throw new StdfException("Existing spreadsheet is incompatible");
    	String oldOpts = ws.getCell(2, optRow).getContents();
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
    	Cell c = ws.getCell(rcol, rrow);
    	Log.msg("col = " + rcol + " row = " + rrow);
    	Log.msg("CELL TYPE = " + c.getType());
    	Log.msg("CELL CONTENTS = " + c.getContents());
    	if (c.getType() == CellType.LABEL)
    	{
    		String text = c.getContents();
    		if (text.equals(CornerBlock.LABEL_TEST_NAME)) return(true);
    	}
    	return(false);
    }
    
    private void setStatus(WritableSheet wsi, int col, int row, TestResult r) throws RowsExceededException, WriteException
    {
        switch (r.error)
        {
        case PASS:       wsi.addCell(new Label(col, row, "PASS", STATUS_PASS_FMT.getFormat())); break;
        case FAIL:       wsi.addCell(new Label(col, row, "FAIL", STATUS_FAIL_FMT.getFormat())); break;
        case INVALID:    wsi.addCell(new Label(col, row, "FAIL", STATUS_INVALID_FMT.getFormat())); break;
        case UNRELIABLE: wsi.addCell(new Label(col, row, "FAIL", STATUS_UNRELIABLE_FMT.getFormat())); break;
        case ALARM:      wsi.addCell(new Label(col, row, "FAIL", STATUS_ALARM_FMT.getFormat())); break;
        case TIMEOUT:    wsi.addCell(new Label(col, row, "FAIL", STATUS_TIMEOUT_FMT.getFormat())); break;
        default:         wsi.addCell(new Label(col, row, "FAIL", STATUS_ABORT_FMT.getFormat())); break;
        }
    }
    
    private void setText(WritableSheet wsi, int col, int row, String text) throws RowsExceededException, WriteException
    {
    	String s = text.trim();
    	int size = wsi.getColumnView(col).getSize();
    	if (size < (s.length() * 256))
    	{
    		wsi.setColumnView(col, (14 * s.length())/10);
    	}
    	wsi.addCell(new Label(col, row, text.trim(), STATUS_PASS_FMT.getFormat()));
    	
    }
    
    private void setDevice(int page, String waferOrStep, DeviceHeader dh)
    {
    	WritableSheet wsi = ws[page];
    	int col = currentCol;
    	wsi.setColumnView(col, 12);
    	try
    	{
    		if (dh.snxy instanceof XY || dh.snxy instanceof TimeXY) // wafersort
    		{
    			if (options.onePage)
    			{
    				if (dh.snxy instanceof TimeXY)
    				{
    					wsi.setColumnView(0, 15);	
    					wsi.addCell(new Number(col, titleBlock.getTimeStampRow(), ((TimeXY) dh.snxy).getTimeStamp(), STATUS_PASS_FMT.getFormat()));
    				}
    				wsi.addCell(new Label(col, titleBlock.getWaferOrStepRow(), waferOrStep));
    			}
    			else
    			{
    				if (dh.snxy instanceof TimeXY)
    				{
    					wsi.addCell(new Number(col, titleBlock.getTimeStampRow(), ((TimeXY) dh.snxy).getTimeStamp(), STATUS_PASS_FMT.getFormat()));
    				}
    			}
    			wsi.addCell(new Number(col, titleBlock.getXRow(), dh.snxy.getX(), STATUS_PASS_FMT.getFormat()));
    			wsi.addCell(new Number(col, titleBlock.getYRow(), dh.snxy.getY(), STATUS_PASS_FMT.getFormat()));
    		}
    		else // FT
    		{
    			if (options.onePage)
    			{
    				if (dh.snxy instanceof TimeSN)
    				{
    					wsi.addCell(new Number(col, titleBlock.getTimeStampRow(), ((TimeSN) dh.snxy).getTimeStamp(), STATUS_PASS_FMT.getFormat()));
    				}
    				wsi.addCell(new Label(col, titleBlock.getWaferOrStepRow(), waferOrStep));
    			}
    			else
    			{
    				if (dh.snxy instanceof TimeSN)
    				{
    					wsi.addCell(new Number(col, titleBlock.getTimeStampRow(), ((TimeSN) dh.snxy).getTimeStamp(), STATUS_PASS_FMT.getFormat()));
    				}
    			}
    			wsi.addCell(new Label(col, titleBlock.getSnOrYRow(), dh.snxy.getSerialNumber(), STATUS_PASS_FMT.getFormat()));
    		}
    		wsi.addCell(new Number(col, titleBlock.getHwBinRow(), dh.hwBin, STATUS_PASS_FMT.getFormat()));
    		wsi.addCell(new Number(col, titleBlock.getSwBinRow(), dh.swBin, STATUS_PASS_FMT.getFormat()));
    		wsi.addCell(new Label(col, titleBlock.getTempRow(), dh.temperature, STATUS_PASS_FMT.getFormat()));
    		if (dh.fail) wsi.addCell(new Label(col, titleBlock.getResultRow(), "FAIL", STATUS_FAIL_FMT.getFormat()));
    		else wsi.addCell(new Label(col, titleBlock.getResultRow(), "PASS", STATUS_PASS_FMT.getFormat()));
    	}
    	catch (WriteException e) { throw new RuntimeException(e.getMessage()); }
    }
    
    private void setValue(WritableSheet wsi, int col, int row, ParametricTestResult p) throws RowsExceededException, WriteException
    {
    	if (p.pass()) wsi.addCell(new Number(col, row, p.result, PASS_VALUE_FMT.getFormat(options.precision)));
    	else if (p.noPassFail()) wsi.addCell(new Number(col, row, p.result, INVALID_VALUE_FMT.getFormat(options.precision)));
    	else if (p.unreliable()) wsi.addCell(new Number(col, row, p.result, UNRELIABLE_VALUE_FMT.getFormat(options.precision)));
    	else if (p.alarm()) wsi.addCell(new Number(col, row, p.result, ALARM_VALUE_FMT.getFormat(options.precision)));
    	else if (p.timeout()) wsi.addCell(new Number(col, row, p.result, TIMEOUT_VALUE_FMT.getFormat(options.precision)));
    	else if (p.abort()) wsi.addCell(new Number(col, row, p.result, ABORT_VALUE_FMT.getFormat(options.precision)));
    	else wsi.addCell(new Number(col, row, p.result, FAIL_VALUE_FMT.getFormat(options.precision)));
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
        			Cell cx = ws[page].getCell(col, titleBlock.getXRow());
        			if (cx.getType() == CellType.EMPTY)
        			{
        				currentCol = col;
        				return;
        			}
        			Cell cy = ws[page].getCell(col, titleBlock.getYRow());
        			short x = (short) ((NumberCell) cx).getValue();
        			short y = (short) ((NumberCell) cy).getValue();
        			if (options.onePage)
        			{
        				String waf = ws[page].getCell(col, titleBlock.getWaferOrStepRow()).getContents();
                        if (waf.equals(waferOrStep) && x == snOrXy.getX() && y == snOrXy.getY())
                        {
                        	currentCol = col;
                        	return;
                        }
        			}
        		}
        	}
            for (int col=titleBlock.getFirstDataCol(); col<=MAX_COLS; col++)
            {
                Cell c = ws[page].getCell(col, titleBlock.getXRow());
                CellType t = c.getType();
                if (t == CellType.EMPTY)
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
                Cell c = ws[page].getWritableCell(col, titleBlock.getSnOrYRow());
                if (c.getType() == CellType.EMPTY)
                {
                    currentCol = col;
                    return;
                }
                if (!options.noOverwrite)
                {
                	String sn = c.getContents();
                	if (sn.equals(snOrXy.getSerialNumber()))
                	{
                    	currentCol = col;
                    	break;
                	}
                }
            }
        }
    }
    
    public void close()
    {
        if (ws == null) return;
        try
        {
            wb.write();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Workbook write failed");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        try
        {
            wb.close();
        }
        catch (Exception e)
        {
            System.out.println("Workbook close failed");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
    }                                 
    
}
