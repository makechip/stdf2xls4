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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Workbook;
import jxl.format.Colour;
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
import com.makechip.stdf2xls4.stdfapi.SnOrXy;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.Log;

import static com.makechip.stdf2xls4.excel.xls.Format_t.*;

@SuppressWarnings("unused")
public class SpreadSheetWriter2 implements SpreadSheetWriter
{
	private final CliOptions options;
	private final StdfAPI api;
    private final int firstDataCol;
    public static final int MAX_ROWS = 1000000;
    
    private int colsPerPage;
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
    private boolean waferMode;
    private boolean wrapTestNames;
    private boolean hiPrecision;
    private boolean noOverWrite;
    private boolean onePage;
    private int firstDataRow;
    private final int RSLT_COL;
    private final int X_COL;
    private final int Y_COL;
    private final int TEMP_COL;
    private int testColumns;
    private boolean sortByFilename;
    private boolean showDuplicates;

    public SpreadSheetWriter2(CliOptions options, StdfAPI api)
    {
    	this.options = options;
    	this.api = api;
    	RSLT_COL = waferMode ? 4 : 5;
    	TEMP_COL = RSLT_COL + 1;
    	X_COL = RSLT_COL + 2;
    	Y_COL = RSLT_COL + 3;
    	firstDataCol = 8;
        System.out.println("Initializing workbook: " + options.xlsName);
        wb = null; 
      	Workbook w = null;
       	try { w = Workbook.getWorkbook(options.xlsName); }
       	catch (Exception e) 
       	{ 
       	    Log.warning("Unable to open existing spreadsheet: " + options.xlsName);
       	    w = null; 
       	}
       	if (w != null)
       	{
       		try { wb = Workbook.createWorkbook(options.xlsName, w); }
       		catch (Exception e)
        	{
       			System.out.println("Exception: " + e.getMessage());
       			System.exit(-1);
        	}
       	}
       	else
       	{
       		try
        	{
        		wb = Workbook.createWorkbook(options.xlsName);
        	}
       		catch (Exception e)
        	{
            	System.out.println("Exception: " + e.getMessage());
            	System.exit(-1);
        	}
        }
       	wb.setColourRGB(Colour.SKY_BLUE, 0, 85, 165);
        sheetNum = 0;
        try
        {
            HEADER1_FMT.getFormat().setWrap(true);
            tnumView.setSize(18*256);
            loLimitView.setSize(16*256);
            hiLimitView.setSize(16*256);
            statusView.setSize(16*256);
            unitsView.setSize(8*256);
            hdrView.setSize(14*256);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    // 1. Make logo
    // 2. Make title block
    // 3. Make horizontal header
    // 4. Make vertical header
    // 5. Enter data
    public void generate() throws RowsExceededException, WriteException
    {
    	/*
    	boolean first = true;
    	for (String waferOrStep : sData.keySet())
    	{
    		if (onePage)
    		{
    			if (first) openSheet(waferOrStep);
    			first = false;
    		}
    		else openSheet(waferOrStep);
    		writeData(waferOrStep);
    	}
        close();
        */
    }
        
    private void writeData(String waferOrStep) throws RowsExceededException, WriteException
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
               	if (onePage) ws[page].addCell(new Label(RSLT_COL-3, currentRow, waferOrStep, DATA_FMT.getFormat()));
               	ws[page].addCell(new Number(RSLT_COL-2, currentRow, dh.getHwBin(), DATA_FMT.getFormat()));
               	ws[page].addCell(new Number(RSLT_COL-1, currentRow, dh.getSwBin(), DATA_FMT.getFormat()));
               	if (dh.isPass()) setStatus(ws[page], RSLT_COL, currentRow, Error_t.PASS);
               	else setStatus(ws[page], RSLT_COL, currentRow, Error_t.FAIL);
               	ws[page].addCell(new Label(TEMP_COL, currentRow, t, DATA_FMT.getFormat()));
               	if (waferMode)
               	{
               	    ws[page].addCell(new Number(X_COL, currentRow, sn.getX(), DATA_FMT.getFormat()));
               	    ws[page].addCell(new Number(Y_COL, currentRow, sn.getY(), DATA_FMT.getFormat()));
               	}
               	else
               	{
               	    ws[page].addCell(new Label(X_COL, currentRow, sn.getSerialNumber(), DATA_FMT.getFormat()));
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
    
    private void openSheet(String waferOrStep)
    {
    	/*
        int pages = 0;
        LinkedHashSet<ColIdentifier> list = dataHeader.get(waferOrStep);
        if (list.size() % colsPerPage == 0) pages = list.size() / colsPerPage;
        else pages = 1 + (list.size() / colsPerPage);
        ws = new WritableSheet[pages];
        try
        {
            for (int i=0; i<pages; i++)
            {
                String name = null;
                if (onePage) name = "    ALL STEPS";
                else name = (waferMode ? "    WAFER " : "    STEP ") + waferOrStep + " Page " + (i+1);
                ws[i] = wb.getSheet(name);
                if (ws[i] == null) 
                {
                	newSheet(i, name, waferOrStep);
                }
                else // count test columns 
                {
                	int tnumRow = getFirstDataRow(waferOrStep) - 5;
                	int j = firstDataCol;
                	int testCols = 0;
                	while (true)
                	{
                		Cell c = ws[i].getCell(j, tnumRow);
                		if (c.getType() == CellType.EMPTY) break;
                		testCols++;
                		j++;
                	}
                	if (testCols < colsPerPage) testColumns = testCols;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        firstDataRow = getFirstDataRow(waferOrStep);
        */
    }
    
    private int getFirstDataRow(String waferOrStep)
    {
    	/*
        StepInfo si = headerInfo.get(waferOrStep);
        HeaderBlock hb = new HeaderBlock(si.getHeaderItems());
        CornerBlock cb = new CornerBlock(waferMode, hb.getHeight(), onePage);
        return(TitleBlock.HEIGHT + hb.getHeight() + cb.getHeight());
        */
    	return(0);
    }

    private void newSheet(int page, String name, String waferOrStep)
    {
    	/*
        try
        {
            ws[page] = wb.createSheet(name, sheetNum);
            StepInfo si = headerInfo.get(waferOrStep);
            HeaderBlock hb = new HeaderBlock(si.getHeaderItems());
            hb.addBlock(ws[page]);
            CornerBlock cb = new CornerBlock(waferMode, hb.getHeight(), onePage);
            cb.addBlock(ws[page]);
            LinkedHashSet<ColIdentifier> m = dataHeader.get(waferOrStep);
            List<ColIdentifier> l1 = new ArrayList<ColIdentifier>();
            for (ColIdentifier ci : m) l1.add(ci);
            List<ColIdentifier> list = new ArrayList<ColIdentifier>();
            for (int i=0; i<colsPerPage; i++)
            {
                if ((i+colsPerPage*page) == m.size()) break;
                ColIdentifier cid = l1.get(i+colsPerPage*page);
                list.add(cid);
            }
            TitleBlock.addBlock(ws[page], name, LegendBlock.getWidth() + ((list.size() >= colsPerPage) ? colsPerPage : list.size()));
            DataHeader dh = new DataHeader(list, cb, hb, wrapTestNames, hiPrecision);
            dh.addBlock(ws[page]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        */
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
    
    private void locateRow(String waferOrStep, SnOrXy snOrXy, int page)
    {
        TIntArrayList xlist = new TIntArrayList();
        if (waferMode)
        {
        	if (!noOverWrite)
        	{
        		for (int row=firstDataRow; row<=MAX_ROWS; row++)
        		{
        			Cell c = ws[page].getCell(X_COL, row);
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
        			NumberCell c = (NumberCell) ws[page].getCell(Y_COL, row);
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
                    Cell c = ws[page].getWritableCell(X_COL, i);
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
                Cell c = ws[page].getWritableCell(X_COL, row);
                if (c.getType() == CellType.EMPTY)
                {
                    currentRow = row;
                    return;
                }
                if (!noOverWrite)
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
