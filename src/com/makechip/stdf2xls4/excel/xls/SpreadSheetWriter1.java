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

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.LabelCell;
import jxl.NumberCell;
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
import com.makechip.stdf2xls4.excel.xls.layout1.CornerBlock;
import com.makechip.stdf2xls4.excel.xls.layout1.DataHeader;
import com.makechip.stdf2xls4.excel.xls.layout1.HeaderBlock;
import com.makechip.stdf2xls4.excel.xls.layout1.LegendBlock;
import com.makechip.stdf2xls4.excel.xls.layout1.TitleBlock;
import com.makechip.stdf2xls4.stdfapi.HeaderUtil;
import com.makechip.stdf2xls4.stdfapi.Limit_t;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.SnOrXy;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.stdfapi.TestHeader;
import com.makechip.stdf2xls4.stdf.StdfException;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.excel.xls.Format_t.*;

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
 * 
 * @author eric
 *
 */
@SuppressWarnings("unused")
public class SpreadSheetWriter1 implements SpreadSheetWriter
{
	private final CliOptions options;
	private final StdfAPI api;
    private final boolean newSpreadsheet;
    private HeaderBlock hb;	
    private CornerBlock cb;
	
    public static final int MAX_ROWS = 1000000;
    
    private static final int colsPerPage = 240;
    private WritableWorkbook wb = null;
    private WritableSheet[] ws;
    private int sheetNum = 0;
    private int currentRow;
    private CellView tnumView     = new CellView();
    private CellView loLimitView  = new CellView();
    private CellView hiLimitView  = new CellView();
    private CellView statusView   = new CellView();
    private CellView unitsView    = new CellView();
    private CellView hdrView      = new CellView();
    private int firstDataRow;
    private int firstDataCol;
    private int testColumns;
    private List<TestHeader> testHeaders;
    private TitleBlock titleBlock;

    public SpreadSheetWriter1(CliOptions options, StdfAPI api) throws IOException, BiffException, WriteException
    {
    	this.options = options;
    	this.api = api;
        wb = null; 
       	if (options.xlsName.exists()) 
       	{
       		Workbook w = Workbook.getWorkbook(options.xlsName);
       		wb = Workbook.createWorkbook(options.xlsName, w);
       		newSpreadsheet = false;
       	}
       	else 
        {
       		wb = Workbook.createWorkbook(options.xlsName);
       		newSpreadsheet = true;
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
        /*	
    	if (sortByFilename && showDuplicates) noOverWrite = true;
        List<ResultList2> m1 = sData.get(waferOrStep);
        StepInfo si = headerInfo.get(waferOrStep);
        int temp = si.getTemperature();
        String t = "" + temp + "C";
        TreeMap<SnOrXy, DeviceHeader> m2 = devHdr.get(waferOrStep);
        LinkedHashSet<ColIdentifier> hdrs = dataHeader.get(waferOrStep);
        List<TestID> hdrList = new ArrayList<TestID>();
        for (ColIdentifier c : hdrs) hdrList.add(c.getTestID());
        for (ResultList2 rl : m1)
        {
        	SnOrXy sn = rl.getSnOrXy();
           	Map<TestID, Result> list = rl.getMap();
           	int pages = 0;
           	testColumns = colsPerPage;
           	if (hdrList.size() % colsPerPage == 0) pages = hdrList.size() / colsPerPage;
           	else pages = 1 + (hdrList.size() / colsPerPage);
           	DeviceHeader dh = m2.get(sn);
           	int k = 0;
           	for (int page=0; page<pages; page++)
           	{
               	locateRow(waferOrStep, sn, page); // sets currentRow
               	if (onePage) ws[page].addCell(new Label(rslt_col-3, currentRow, waferOrStep, DATA_FMT.getFormat()));
               	ws[page].addCell(new Number(rslt_col-2, currentRow, dh.getHwBin(), DATA_FMT.getFormat()));
               	ws[page].addCell(new Number(rslt_col-1, currentRow, dh.getSwBin(), DATA_FMT.getFormat()));
               	if (dh.isPass()) setStatus(ws[page], rslt_col, currentRow, Error_t.PASS);
               	else setStatus(ws[page], rslt_col, currentRow, Error_t.FAIL);
               	ws[page].addCell(new Label(temp_col, currentRow, t, DATA_FMT.getFormat()));
               	if (waferMode)
               	{
               	    ws[page].addCell(new Number(x_col, currentRow, sn.getX(), DATA_FMT.getFormat()));
               	    ws[page].addCell(new Number(y_sn_col, currentRow, sn.getY(), DATA_FMT.getFormat()));
               	}
               	else
               	{
               	    ws[page].addCell(new Label(x_col, currentRow, sn.getSerialNumber(), DATA_FMT.getFormat()));
               	}
               	for (int i=0; i<testColumns; i++)
               	{
                   	if (k == hdrList.size()) break;
                   	TestID id = hdrList.get(k);
                   	Result r = list.get(id);
                   	//Log.msg("xy = " + sn + " id = " + id + " result = " + r);
                   	if (r != null)
                   	{
                       	if (r.getType() == Test_t.FUNCTIONAL)
                       	{
                           	setStatus(ws[page], firstDataCol+i, currentRow, r.getError());
                       	}
                       	else if (r.getType() == Test_t.TEXT)
                       	{
                       		setText(ws[page], firstDataCol+i, currentRow, r.getText());
                       	}
                       	else
                       	{
                           	setValue(ws[page], firstDataCol+i, currentRow, r.getError(), r.getValue());
                       	}
                   	}
                   	k++;
               	}
           	}
        }
        */
    } 
    
    private void openSheet(PageHeader hdr) throws RowsExceededException, WriteException, IOException, StdfException
    {
        int numTests = api.getTestHeaders(hdr).size();
        int pages = numTests / colsPerPage;
        if (numTests % colsPerPage != 0) pages++;
        ws = new WritableSheet[pages];
        for (int i=0; i<pages; i++)
        {
        	String name = null;
        	name = getPageName(hdr, i);
        	ws[i] = wb.getSheet(name);
        	if (ws[i] == null) 
        	{
        		newSheet(i, name, hdr);
        		testHeaders = getTestHeaders(hdr, i);
        	}
        	else 
        	{
        		if (!checkRegistration(ws[i])) throw new StdfException("Incompatible spreadsheet");
        		testHeaders = getTestHeaders(ws[i]);
        	}
        }
    }
    
    private String getWaferOrStepName(PageHeader hdr)
    {
    	return(api.wafersort(hdr) ? hdr.get(HeaderUtil.WAFER_ID): hdr.get(HeaderUtil.STEP));
    }
    
    private String getPageName(PageHeader hdr, int pageIndex)
    {
        String prefix = api.wafersort(hdr) ? "    WAFER " : "    STEP ";
        return(prefix + getWaferOrStepName(hdr) + " Page " + (pageIndex + 1));
    }
    
    private int getFirstDataRow()
    {
        return(titleBlock.getHeight());
    }

    private void newSheet(int page, String name, PageHeader hdr) throws RowsExceededException, WriteException, IOException
    {
    	ws[page] = wb.createSheet(name, sheetNum);
    	List<TestHeader> list = getTestHeaders(hdr, page);
    	titleBlock = new TitleBlock(hdr, options.logoFile, getPageName(hdr, page), api.wafersort(hdr), options, list);
    	titleBlock.addBlock(ws[page]);
    	CellView c1 = ws[page].getRowView(6);
    	CellView c2 = ws[page].getRowView(7);
    	Log.msg("row6 height = " + c1.getSize() + " row7 height = " + c2.getSize());
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
    
    private List<TestHeader> getTestHeaders(WritableSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(colsPerPage);
        int cnt = 0;
        for (int i=titleBlock.getFirstDataCol(); i<titleBlock.getFirstDataCol()+colsPerPage; i++)
        {
        	Cell tnameCell = existingSheet.getCell(i, titleBlock.getTestNameRow());
        	Cell tnumCell  = existingSheet.getCell(i, titleBlock.getTestNumberRow());
        	Cell dupCell   = existingSheet.getCell(i, titleBlock.getDupNumRow());
        	Cell loLimCell = existingSheet.getCell(i, titleBlock.getLoLimitRow());
        	Cell hiLimCell = existingSheet.getCell(i, titleBlock.getHiLimitRow());
        	Cell pnameCell = existingSheet.getCell(i, titleBlock.getPinNameRow());
        	Cell unitsCell = existingSheet.getCell(i, titleBlock.getUnitsRow());
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
                   	float lolim = (loLimCell.getType() == CellType.EMPTY) ? StdfRecord.MISSING_FLOAT : (float) ((Number) loLimCell).getValue();
                   	float hilim = (hiLimCell.getType() == CellType.EMPTY) ? StdfRecord.MISSING_FLOAT : (float) ((Number) hiLimCell).getValue();
                    String pin = pnameCell.getContents();
                    plist.add(new MultiParametricTestHeader(tname, tnum, dnum, pin, units, lolim, hilim));
                }
        	}
        	else // no pin, so either a normal Parametric test or Functional or Datalog test
        	{
        	    if (loLimCell.getType() != CellType.EMPTY || hiLimCell.getType() != CellType.EMPTY)
        	    {
                   	float lolim = (loLimCell.getType() == CellType.EMPTY) ? StdfRecord.MISSING_FLOAT : (float) ((Number) loLimCell).getValue();
                   	float hilim = (hiLimCell.getType() == CellType.EMPTY) ? StdfRecord.MISSING_FLOAT : (float) ((Number) hiLimCell).getValue();
                   	String units = unitsCell.getContents();
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
    
    private boolean checkRegistration(WritableSheet ws) throws StdfException
    {
    	// Check for option compatibility:
    	int optRow = -1;
    	for (int row=0; row<100; row++)
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
    	String oldOpts = ws.getCell(3, optRow).getContents();
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
    	Cell c = ws.getCell(rcol, rrow);
    	Log.msg("CELL TYPE = " + c.getType());
    	Log.msg("CELL CONTENTS = " + c.getContents());
    	if (c.getType() == CellType.LABEL)
    	{
    		String text = c.getContents();
    		if (text.equals(CornerBlock.LABEL_TEST_NAME)) return(true);
    	}
    	return(false);
    }
    
    private void setStatus(WritableSheet wsi, int col, int row) throws RowsExceededException, WriteException
    {
    	/*
        switch (err)
        {
        case PASS:       wsi.addCell(new Label(col, row, "PASS", STATUS_PASS_FMT.getFormat())); break;
        case FAIL:       wsi.addCell(new Label(col, row, "FAIL", STATUS_FAIL_FMT.getFormat())); break;
        case INVALID:    wsi.addCell(new Label(col, row, "FAIL", STATUS_INVALID_FMT.getFormat())); break;
        case UNRELIABLE: wsi.addCell(new Label(col, row, "FAIL", STATUS_UNRELIABLE_FMT.getFormat())); break;
        case ALARM:      wsi.addCell(new Label(col, row, "FAIL", STATUS_ALARM_FMT.getFormat())); break;
        case TIMEOUT:    wsi.addCell(new Label(col, row, "FAIL", STATUS_TIMEOUT_FMT.getFormat())); break;
        default:         wsi.addCell(new Label(col, row, "FAIL", STATUS_ABORT_FMT.getFormat())); break;
        }
        */
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
    
    private void setValue(WritableSheet wsi, int col, int row, float value) throws RowsExceededException, WriteException
    {
    	/*
    	if (hiPrecision)
    	{
    		switch (err)
    		{
    		case PASS:       wsi.addCell(new Number(col, row, value, PASS_VALUE_HP_FMT.getFormat())); break;
    		case FAIL:       wsi.addCell(new Number(col, row, value, FAIL_VALUE_HP_FMT.getFormat())); break;
    		case INVALID:    wsi.addCell(new Number(col, row, value, INVALID_VALUE_HP_FMT.getFormat())); break;
    		case UNRELIABLE: wsi.addCell(new Number(col, row, value, UNRELIABLE_VALUE_HP_FMT.getFormat())); break;
    		case ALARM:      wsi.addCell(new Number(col, row, value, ALARM_VALUE_HP_FMT.getFormat())); break;
    		case TIMEOUT:    wsi.addCell(new Number(col, row, value, TIMEOUT_VALUE_HP_FMT.getFormat())); break;
    		default:         wsi.addCell(new Number(col, row, value, ABORT_VALUE_HP_FMT.getFormat())); break;
    		}
    	}
    	else
    	{
    		switch (err)
    		{
    		case PASS:       wsi.addCell(new Number(col, row, value, PASS_VALUE_FMT.getFormat())); break;
    		case FAIL:       wsi.addCell(new Number(col, row, value, FAIL_VALUE_FMT.getFormat())); break;
    		case INVALID:    wsi.addCell(new Number(col, row, value, INVALID_VALUE_FMT.getFormat())); break;
    		case UNRELIABLE: wsi.addCell(new Number(col, row, value, UNRELIABLE_VALUE_FMT.getFormat())); break;
    		case ALARM:      wsi.addCell(new Number(col, row, value, ALARM_VALUE_FMT.getFormat())); break;
    		case TIMEOUT:    wsi.addCell(new Number(col, row, value, TIMEOUT_VALUE_FMT.getFormat())); break;
    		default:         wsi.addCell(new Number(col, row, value, ABORT_VALUE_FMT.getFormat())); break;
    		}
    	}
    	*/
    }
    
    private void locateRow(PageHeader hdr, String waferOrStep, SnOrXy snOrXy, int page)
    {
        TIntArrayList xlist = new TIntArrayList();
        if (api.wafersort(hdr))
        {
        	if (!options.noOverwrite)
        	{
        		for (int row=firstDataRow; row<=MAX_ROWS; row++)
        		{
        			Cell c = ws[page].getCell(titleBlock.getXCol(), row);
        			if (c.getType() == CellType.EMPTY)
        			{
        				currentRow = row;
        				return;
        			}
        			NumberCell cd = (NumberCell) c;
        			short xval = (short) cd.getValue();
        			if (xval == snOrXy.getX()) xlist.add(row);
        		}
        		TIntIterator it = xlist.iterator();
        		while (it.hasNext())
        		{
        			int row = it.next();
        			NumberCell c = (NumberCell) ws[page].getCell(titleBlock.getSnOrYCol(), row);
        			short yval = (short) c.getValue();
        			if (yval == snOrXy.getY())
        			{	
        				currentRow = row;
        				return;
        			}
        		}
        	}
            try // existing x,y not found, so just look for the first empty row
            {
                for (int i=firstDataRow; i<=MAX_ROWS; i++)
                {
                    Cell c = ws[page].getWritableCell(titleBlock.getXCol(), i);
                    CellType t = c.getType();
                    if (t == CellType.EMPTY)
                    {
                        currentRow = i;
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Exception: " + e.getMessage());
                System.exit(-1);
            }
        }
        else
        {
            for (int row=firstDataRow; row<=MAX_ROWS; row++)
            {
                Cell c = ws[page].getWritableCell(titleBlock.getXCol(), row);
                if (c.getType() == CellType.EMPTY)
                {
                    currentRow = row;
                    return;
                }
                if (!options.noOverwrite)
                {
                	LabelCell cd = (LabelCell) c;
                	String sn = cd.getString();
                	if (sn.equals(snOrXy.getSerialNumber()))
                	{
                    	currentRow = row;
                    	break;
                	}
                }
            }
        }
    }
    
    public void setCell(WritableSheet wsi, int row, int col, String contents, Format_t format)
    {
        try { wsi.addCell(new Label(row, col, contents, format.getFormat())); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void setCell(WritableSheet wsi, int row, int col, double value, Format_t format)
    {
        try { wsi.addCell(new Number(row, col, value, format.getFormat(options.precision))); }
        catch (Exception e) { e.printStackTrace(); }
    }
    
    public void setCell(WritableSheet wsi, int row, int col, int value, Format_t format)
    {
        try { wsi.addCell(new Number(row, col, value, format.getFormat())); }
        catch (Exception e) { e.printStackTrace(); }
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
