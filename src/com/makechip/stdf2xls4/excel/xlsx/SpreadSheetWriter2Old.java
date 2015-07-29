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

package com.makechip.stdf2xls4.excel.xlsx;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.SpreadSheetWriter;
import com.makechip.stdf2xls4.excel.xlsx.layout2.TitleBlock;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdfapi.DeviceResult;
import com.makechip.stdf2xls4.stdfapi.SnOrXy;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;

import static com.makechip.stdf2xls4.excel.xlsx.FontName_t.ARIAL;
import static com.makechip.stdf2xls4.excel.xlsx.FontName_t.COURIER;
import static com.makechip.stdf2xls4.excel.xlsx.FontStyle_t.BOLD;
import static com.makechip.stdf2xls4.excel.xlsx.FontStyle_t.NORMAL;
import static com.makechip.stdf2xls4.excel.xlsx.HAlignment_t.CENTER;
import static com.makechip.stdf2xls4.excel.xlsx.HAlignment_t.LEFT;
import static com.makechip.stdf2xls4.excel.xlsx.HAlignment_t.RIGHT;
import static org.apache.poi.ss.usermodel.IndexedColors.BLACK;
import static org.apache.poi.ss.usermodel.IndexedColors.BLUE;
import static org.apache.poi.ss.usermodel.IndexedColors.BRIGHT_GREEN;
import static org.apache.poi.ss.usermodel.IndexedColors.LIGHT_BLUE;
import static org.apache.poi.ss.usermodel.IndexedColors.PINK;
import static org.apache.poi.ss.usermodel.IndexedColors.RED;
import static org.apache.poi.ss.usermodel.IndexedColors.SKY_BLUE;
import static org.apache.poi.ss.usermodel.IndexedColors.TURQUOISE;
import static org.apache.poi.ss.usermodel.IndexedColors.WHITE;
import static org.apache.poi.ss.usermodel.IndexedColors.YELLOW;

@SuppressWarnings("all")
public class SpreadSheetWriter2Old implements SpreadSheetWriter
{
    private final int firstDataCol;
    private final int firstDataRow;
    public final int MAX_COLS;
    
    private Workbook wb = null;
    private Sheet  ws;
    public static final Color myBlue = new XSSFColor(new byte[] { 0, 85, (byte) 165 });
    private String fileName;
	private boolean xssf;
    private int currentCol;
    private boolean waferMode;
    private boolean wrapTestNames;
    private boolean hiPrecision;
    private boolean noOverWrite;
    private boolean onePage;
    private final int RSLT_ROW;
    private final int X_ROW;
    private final int Y_ROW;
    private final int TEMP_ROW;
    private boolean sortByFilename;
    private boolean showDuplicates;
    public CellStyle TEST_NUMBER_FMT;
    public CellStyle TEST_NAME_FMT;
    public final CellStyle TITLE_FMT;
    public final CellStyle LO_LIMIT_FMT;
    public final CellStyle HI_LIMIT_FMT;
    public final CellStyle UNIT_FMT;
    public final CellStyle DATA_FMT;
    public final CellStyle HEADER1_FMT;
    public final CellStyle HEADER2_FMT;
    public final CellStyle HEADER3_FMT;
    public final CellStyle HEADER4_FMT;
    public final CellStyle HEADER5_FMT;
    public final CellStyle PASS_VALUE_HP_FMT;
    public final CellStyle FAIL_VALUE_HP_FMT;
    public final CellStyle INVALID_VALUE_HP_FMT;
    public final CellStyle UNRELIABLE_VALUE_HP_FMT;
    public final CellStyle ALARM_VALUE_HP_FMT;
    public final CellStyle TIMEOUT_VALUE_HP_FMT;
    public final CellStyle ABORT_VALUE_HP_FMT;
    public final CellStyle PASS_VALUE_FMT;
    public final CellStyle FAIL_VALUE_FMT;
    public final CellStyle INVALID_VALUE_FMT;
    public final CellStyle UNRELIABLE_VALUE_FMT;
    public final CellStyle ALARM_VALUE_FMT;
    public final CellStyle TIMEOUT_VALUE_FMT;
    public final CellStyle ABORT_VALUE_FMT;
    public final CellStyle STATUS_PASS_FMT;
    public final CellStyle STATUS_FAIL_FMT;
    public final CellStyle STATUS_INVALID_FMT;
    public final CellStyle STATUS_UNRELIABLE_FMT;
    public final CellStyle STATUS_ALARM_FMT;
    public final CellStyle STATUS_TIMEOUT_FMT;
    public final CellStyle STATUS_ABORT_FMT;

    public SpreadSheetWriter2Old(CliOptions options, StdfAPI api)
    {
    	//this.fileName = opts.getXlsName();
    	//this.waferMode = waferMode;
    	//this.wrapTestNames = opts.getWrapTestNames();
    	//this.hiPrecision = opts.getHiP();
    	//this.noOverWrite = opts.getNoOverwrite();
    	//this.onePage = opts.getOnePage();
    	this.sortByFilename = sortByFilename;
    	//this.showDuplicates = opts.getShowDuplicates();
        firstDataCol = 11;
        firstDataRow = onePage ? (waferMode ? 17 : 16) : (waferMode ? 16 : 15);
    	RSLT_ROW = onePage ? TitleBlock.HEIGHT + 3 : TitleBlock.HEIGHT + 2;
    	TEMP_ROW = RSLT_ROW + 1;
    	X_ROW = RSLT_ROW + 2;
    	Y_ROW = RSLT_ROW + 3;
    	MAX_COLS = 1;
    	/*
    	sData = new HashMap<String, List<ResultList2>>();
    	data = new HashMap<String, TreeMap<SnOrXy, LinkedHashMap<TestID, Result>>>();
        dataHeader = new HashMap<String, LinkedHashSet<ColIdentifier>>();
        headerInfo = new HashMap<String, StepInfo>();
        devHdr = new HashMap<String, TreeMap<SnOrXy, DeviceHeader>>();
        System.out.println("Initializing workbook: " + fileName);
        wb = null; 
      	File ss = new File(fileName);
      	if (!ss.exists())
      	{
       	    Log.warning("Unable to open existing spreadsheet: " + fileName);
       	    if (fileName.endsWith(".xls")) wb = new HSSFWorkbook();
       	    else wb = new XSSFWorkbook();
       	}
      	else
       	{
      		try
      		{
      			if (fileName.endsWith(".xls")) wb = new HSSFWorkbook(new FileInputStream(ss));
      			else wb = new XSSFWorkbook(new FileInputStream(ss));
      		}
      		catch (Exception e) { Log.fatal(e); }
       	}
        if (fileName.endsWith(".xls"))
        {
            HSSFPalette p = ((HSSFWorkbook)wb).getCustomPalette();	
            p.setColorAtIndex((short) 0x39, (byte) 0, (byte) 85, (byte) 165);
            xssf = false;
        }
        else xssf = true;
        MAX_COLS = xssf ? (opts.getMsMode() ? 16000 : 1000) : 230;
        TEST_NUMBER_FMT         = CellStyleType.getCellStyle(wb, LEFT,   VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        if (wrapTestNames)
        {
        	Log.msg("wrapping test names!!!");
        	TEST_NAME_FMT           = CellStyleType.getCellStyle(wb, JUSTIFY,VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        }
        else
        {
        	TEST_NAME_FMT           = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        }
        */
        TITLE_FMT               = CellStyleType.getCellStyle(wb, LEFT,   VAlignment_t.CENTER, SKY_BLUE,     WHITE, ARIAL,   BOLD,   "",       BorderType.DEFAULT_BORDER, (short) 20);
        LO_LIMIT_FMT            = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        HI_LIMIT_FMT            = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        UNIT_FMT                = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        DATA_FMT                = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        HEADER1_FMT             = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   BOLD,   "",       BorderType.DEFAULT_BORDER, (short) 8);
        HEADER2_FMT             = CellStyleType.getCellStyle(wb, RIGHT,  VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   BOLD,   "",       BorderType.DEFAULT_BORDER, (short) 10);
        HEADER3_FMT             = CellStyleType.getCellStyle(wb, LEFT,   VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        HEADER4_FMT             = CellStyleType.getCellStyle(wb, RIGHT,  VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   BOLD,   "",       BorderType.DEFAULT_BORDER, (short) 8);
        HEADER5_FMT             = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, ARIAL,   BOLD,   "0.000",  BorderType.DEFAULT_BORDER, (short) 8);
        PASS_VALUE_HP_FMT       = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        FAIL_VALUE_HP_FMT       = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, RED,          BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        INVALID_VALUE_HP_FMT    = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, BRIGHT_GREEN, BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        UNRELIABLE_VALUE_HP_FMT = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, LIGHT_BLUE,   BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        ALARM_VALUE_HP_FMT      = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, YELLOW,       BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        TIMEOUT_VALUE_HP_FMT    = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, PINK,         BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        ABORT_VALUE_HP_FMT      = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, TURQUOISE,    BLACK, COURIER, NORMAL, "0.0000", BorderType.DEFAULT_BORDER, (short) 10);
        PASS_VALUE_FMT          = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        FAIL_VALUE_FMT          = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, RED,          BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        INVALID_VALUE_FMT       = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, BRIGHT_GREEN, BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        UNRELIABLE_VALUE_FMT    = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, LIGHT_BLUE,   BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        ALARM_VALUE_FMT         = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, YELLOW,       BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        TIMEOUT_VALUE_FMT       = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, PINK,         BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        ABORT_VALUE_FMT         = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, TURQUOISE,    BLACK, COURIER, NORMAL, "0.000",  BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_PASS_FMT         = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, WHITE,        BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_FAIL_FMT         = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, RED,          BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_INVALID_FMT      = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, BRIGHT_GREEN, BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_UNRELIABLE_FMT   = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, BLUE,         BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_ALARM_FMT        = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, YELLOW,       BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_TIMEOUT_FMT      = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, PINK,         BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
        STATUS_ABORT_FMT        = CellStyleType.getCellStyle(wb, CENTER, VAlignment_t.CENTER, TURQUOISE,    BLACK, COURIER, NORMAL, "",       BorderType.DEFAULT_BORDER, (short) 10);
    }

    // 1. Make logo
    // 2. Make title block
    // 3. Make horizontal header
    // 4. Make vertical header
    // 5. Enter data
    public void generate()
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
        
    private void addCell(Sheet ws, int col, int row, String text, CellStyle fmt)
    {
    	Row r = ws.getRow(row);
    	if (r == null) r = ws.createRow(row);
    	Cell c = r.getCell(col);
    	if (c == null)
    	{
    		c = r.createCell(col, Cell_t.STRING.getType());
    		c.setCellValue(text);
    		c.setCellStyle(fmt);
    	}
    }
    
    private void addCell(Sheet ws, int col, int row, int value, CellStyle fmt)
    {
    	Row r = ws.getRow(row);
    	if (r == null) r = ws.createRow(row);
    	Cell c = r.getCell(col);
    	if (c == null)
    	{
    		c = r.createCell(col, Cell_t.STRING.getType());
    		c.setCellValue(value);
    		c.setCellStyle(fmt);
    	}
    }
    
    private void addCell(Sheet ws, int col, int row, float value, CellStyle fmt)
    {
    	Row r = ws.getRow(row);
    	if (r == null) r = ws.createRow(row);
    	Cell c = r.getCell(col);
    	if (c == null)
    	{
    		c = r.createCell(col, Cell_t.STRING.getType());
    		c.setCellValue(value);
    		c.setCellStyle(fmt);
    	}
    }
    
    @SuppressWarnings("unused")
	private void addCell(Sheet ws, int col, int row, double value, CellStyle fmt)
    {
    	Row r = ws.getRow(row);
    	if (r == null) r = ws.createRow(row);
    	Cell c = r.getCell(col);
    	if (c == null)
    	{
    		c = r.createCell(col, Cell_t.STRING.getType());
    		c.setCellValue(value);
    		c.setCellStyle(fmt);
    	}
    }
    
    private void writeData(String waferOrStep)
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
           	DeviceHeader dh = m2.get(sn);
           	int k = 0;
            locateCol(waferOrStep, sn); // sets currentCol
            if (hiPrecision) ws.setColumnWidth(currentCol, 12 * 256);
            else ws.setColumnWidth(currentCol, 10 * 256);
            if (onePage) addCell(ws, currentCol, RSLT_ROW-3, waferOrStep, DATA_FMT);
            addCell(ws, currentCol, RSLT_ROW-2, dh.getHwBin(), DATA_FMT);	
            addCell(ws, currentCol, RSLT_ROW-1, dh.getSwBin(), DATA_FMT);
            if (dh.isPass()) setStatus(ws, currentCol, RSLT_ROW, Error_t.PASS);
            else setStatus(ws, currentCol, RSLT_ROW, Error_t.FAIL);
            addCell(ws, currentCol, TEMP_ROW, t, DATA_FMT);
            if (waferMode)
            {
            	addCell(ws, currentCol, X_ROW, sn.getX(), DATA_FMT);
            	addCell(ws, currentCol, Y_ROW, sn.getY(), DATA_FMT);
            }
            else
            {
            	addCell(ws, currentCol, X_ROW, "" + sn.getSerialNumber(), DATA_FMT);
            }
            Log.msg("LIST SIZE = " + list.size() + " hdrList.size() = " + hdrList.size());
            for (int i=0; i<list.size(); i++)
            {
                if (k == hdrList.size()) break;
                TestID id = hdrList.get(k);
                Result r = list.get(id);
                Log.msg("id = " + id + " rslt = " + r);
                if (r != null)
                {
                   	if (r.getType() == Test_t.FUNCTIONAL)
                   	{
                       	setStatus(ws, currentCol, firstDataRow+i, r.getError());
                   	}
                   	else if (r.getType() == Test_t.TEXT)
                   	{
                   		setText(ws, currentCol, firstDataRow+i, r.getText());
                   	}
                   	else
                   	{
                       	setValue(ws, currentCol, firstDataRow+i, r.getError(), r.getValue());
                   	}
               	}
               	k++;
           	}
       	}
       	*/
    } 
    
    private void openSheet(String waferOrStep)
    {
        String name = null;
        if (onePage) name = "    ALL STEPS";
        else name = (waferMode ? "    WAFER " : "    STEP ") + waferOrStep;
        ws = wb.getSheet(name);
        if (ws == null) newSheet(name, waferOrStep);
    }
    
    private void newSheet(String name, String waferOrStep)
    {
    	/*
        try
        {
            ws = wb.createSheet(name);
            StepInfo si = headerInfo.get(waferOrStep);
            HeaderBlock hb = new HeaderBlock(this, si.getHeaderItems());
            hb.addBlock(ws);
            CornerBlock cb = new CornerBlock(this, waferMode, hb.getHeight(), onePage);
            cb.addBlock(ws);
            List<ResultList2> devs = sData.get(waferOrStep);
            LinkedHashSet<ColIdentifier> m = dataHeader.get(waferOrStep);
            List<ColIdentifier> list = new ArrayList<ColIdentifier>();
            for (ColIdentifier ci : m) list.add(ci);
            Log.msg("sData.size = " + sData.size());
            TitleBlock.addBlock(wb, xssf, this, ws, name, devs.size() + 6);
            DataHeader dh = new DataHeader(this, list, cb, hb);
            dh.addBlock(ws, xssf);
            LegendBlock.addBlock(this, ws, hb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        */
    }
    
    private void setStatus(Sheet wsi, int col, int row)
    {
    	/*
        switch (err)
        {
        case PASS:       addCell(wsi, col, row, "PASS", STATUS_PASS_FMT);
        case FAIL:       addCell(wsi, col, row, "FAIL", STATUS_FAIL_FMT);
        case INVALID:    addCell(wsi, col, row, "FAIL", STATUS_INVALID_FMT);
        case UNRELIABLE: addCell(wsi, col, row, "FAIL", STATUS_UNRELIABLE_FMT);
        case ALARM:      addCell(wsi, col, row, "FAIL", STATUS_ALARM_FMT);
        case TIMEOUT:    addCell(wsi, col, row, "FAIL", STATUS_TIMEOUT_FMT);
        default:         addCell(wsi, col, row, "FAIL", STATUS_ABORT_FMT);
        }
        */
    }
    
    @SuppressWarnings("unused")
	private void setText(Sheet wsi, int col, int row, String text)
    {
    	String s = text.trim();
    	int size = wsi.getColumnWidth(col);
    	if (size < (s.length() * 256))
    	{
    		wsi.setColumnWidth(col, 256*(14 * s.length())/10);
    	}
    	addCell(wsi, col, row, text.trim(), STATUS_PASS_FMT);
    	
    }
    
    private void setValue(Sheet wsi, int col, int row, float value)
    {
    	/*
    	if (hiPrecision)
    	{
    		switch (err)
    		{
    		case PASS:       addCell(wsi, col, row, value, PASS_VALUE_HP_FMT);
    		case FAIL:       addCell(wsi, col, row, value, FAIL_VALUE_HP_FMT); break;
    		case INVALID:    addCell(wsi, col, row, value, INVALID_VALUE_HP_FMT); break;
    		case UNRELIABLE: addCell(wsi, col, row, value, UNRELIABLE_VALUE_HP_FMT); break;
    		case ALARM:      addCell(wsi, col, row, value, ALARM_VALUE_HP_FMT); break;
    		case TIMEOUT:    addCell(wsi, col, row, value, TIMEOUT_VALUE_HP_FMT); break;
    		default:         addCell(wsi, col, row, value, ABORT_VALUE_HP_FMT); break;
    		}
    	}
    	else
    	{
    		switch (err)
    		{
    		case PASS:       addCell(wsi, col, row, value, PASS_VALUE_FMT); break;
    		case FAIL:       addCell(wsi, col, row, value, FAIL_VALUE_FMT); break;
    		case INVALID:    addCell(wsi, col, row, value, INVALID_VALUE_FMT); break;
    		case UNRELIABLE: addCell(wsi, col, row, value, UNRELIABLE_VALUE_FMT); break;
    		case ALARM:      addCell(wsi, col, row, value, ALARM_VALUE_FMT); break;
    		case TIMEOUT:    addCell(wsi, col, row, value, TIMEOUT_VALUE_FMT); break;
    		default:         addCell(wsi, col, row, value, ABORT_VALUE_FMT); break;
    		}
    	}
    	*/
    }
    
    private void locateCol(String waferOrStep, SnOrXy snOrXy)
    {
        TIntArrayList xlist = new TIntArrayList();
        if (waferMode)
        {
        	if (!noOverWrite)
        	{
        		for (int col=firstDataCol; col<=MAX_COLS; col++)
        		{
        			Row r = ws.getRow(X_ROW);
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
        			short xval = (short) c.getNumericCellValue();
        			if (xval == snOrXy.getX()) xlist.add(col);
        		}
        		TIntIterator it = xlist.iterator();
        		while (it.hasNext())
        		{
        			int col = it.next();
        			Row r = ws.getRow(Y_ROW);
        			Cell c = r.getCell(col);
        			short yval = (short) c.getNumericCellValue();
        			if (yval == snOrXy.getY())
        			{	
        				currentCol = col;
        				return;
        			}
        		}
        	}
            try // existing x,y not found, so just look for the first empty column
            {
                for (int i=firstDataCol; i<=MAX_COLS; i++)
                {
                	Row r = ws.getRow(X_ROW);
                	if (r == null)
                	{
                		currentCol = i;
                		break;
                	}
                    Cell c = r.getCell(i);
                    if (c == null)
                    {
                        currentCol = i;
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
            for (int col=firstDataCol; col<=MAX_COLS; col++)
            {
            	Row r = ws.getRow(X_ROW);
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
                if (!noOverWrite)
                {
                	String sn = c.getStringCellValue();
                	if (sn.equals(snOrXy.getSerialNumber()))
                	{
                    	currentCol = col;
                    	break;
                	}
                }
            }
        }
    }
    
    public int getFirstDataCol() { return(11); }
    
    public void close()
    {
    	Row r = ws.getRow(firstDataRow);
    	Cell c = r.getCell(firstDataCol);
    	c.setAsActiveCell();
        if (ws == null) return;
        try
        {
        	FileOutputStream fos = new FileOutputStream(fileName);
            wb.write(fos);
            wb.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Workbook write failed");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
    }                                 
    
    private void addResult(String waferOrStep, SnOrXy snOrXy, TestID id, DeviceResult r, boolean first)
    {
    	/*
    	if (noOverWrite)
    	{
    		List<ResultList2> l = sData.get(waferOrStep);
    		if (l == null)
    		{
    			l = new ArrayList<ResultList2>();
    			sData.put(waferOrStep, l);
    			ResultList2 lr = new ResultList2(snOrXy);
    			l.add(lr);
    		}
    		else if (first)
    		{
    			ResultList2 lr = new ResultList2(snOrXy);
    			l.add(lr);
    		}
    		ResultList2 lr = l.get(l.size()-1);
    		lr.addResult(id, r);
    	}
    	else
    	{
    		TreeMap<SnOrXy, LinkedHashMap<TestID, Result>> m1 = data.get(waferOrStep);
    		if (m1 == null)
    		{
    			m1 = new TreeMap<SnOrXy, LinkedHashMap<TestID, Result>>();
    			data.put(waferOrStep, m1);
    		}
    		LinkedHashMap<TestID, Result> m2 = m1.get(snOrXy);
    		if (m2 == null)
    		{
    			m2 = new LinkedHashMap<TestID, Result>();
    			m1.put(snOrXy, m2);
    		}
    		if (first) m2.clear();
    		//Log.msg("xy = " + snOrXy + " id = " + id + " result = " + r);
    		m2.put(id, r);
    	}
    	*/
    }
    
    private void addColumnId(String waferOrStep)
    {
    	/*
        LinkedHashSet<ColIdentifier> m1 = dataHeader.get(waferOrStep);
        if (m1 == null)
        {
            m1 = new LinkedHashSet<ColIdentifier>();
            dataHeader.put(waferOrStep, m1);
        }
        m1.add(col);
        */
    }
   
    /**
     * This method builds up three data structures:
     * 1. data: map waferOrStep -> snOrxy -> testId -> result
     * 2. dataHeader: map waferOrStep -> TestID -> ColIdentifier  // for the header above the test data
     * 3. headerInfo: map waferOrStep -> StepInfo  // for the header information
     * Also, this method creates unique TestID's for each pin in a MultipleResultParametricStdfRecord,
     * and computes the the pass/fail status for each pin.
     * @param testInfo
     * @param results
     */
    public void addResults(HashMap<String, PageHeader> testInfo, List<DeviceResult> results)
    {
    	/*
        this.headerInfo = testInfo;
        for (DeviceResult dr : results)
        {
            StepInfo stepInfo = dr.getStepInfo();
            String waferOrStep = stepInfo.getStep();
            if (waferOrStep == null) waferOrStep = "NONE";
            headerInfo.put(waferOrStep, stepInfo);
            SnOrXy snOrXy = dr.getSnOrXy();
            DeviceHeader dh = DeviceHeader.getDeviceHeader(dr.isPass(), snOrXy, stepInfo.getTemperature(), dr.getHwBin(), dr.getSwBin());
            addDeviceHeader(waferOrStep, snOrXy, dh); 
            boolean first = true;
            for (TestID id : dr.getTestIDs())
            {
                TestResult tr = dr.getTestResult(id);
                Test_t type = null;
                if (tr instanceof FunctionalTestResult) type = Test_t.FUNCTIONAL;
                else if (tr instanceof ParametricTestResult) type = Test_t.PARAMETRIC;
                else if (tr instanceof TextResult) type = Test_t.TEXT;
                else type = Test_t.MULTI_PARAMETRIC;
                switch (type)
                {
                case PARAMETRIC:
                    ParametricTestResult ptr = ParametricTestResult.class.cast(tr);
                    Result r1 = new Result(type, ptr.getResult(), ptr.getError());
                    addColumnId(waferOrStep, ColIdentifier.getColIdentifier(id, ptr.getLoLimit(), ptr.getHiLimit(), ptr.getUnits()));
                    addResult(waferOrStep, snOrXy, id, r1, first);
                    first = false;
                    break;
                case MULTI_PARAMETRIC:
                    MultipleParametricTestResult mpr = MultipleParametricTestResult.class.cast(tr);
                    for (String pin : mpr.getPins())
                    {
                        TestID tid = TestID.getTestID(id, pin);
                        // ADD LIMIT CHECKING HERE
                        // before checking consider the following:
                        // 1. Is low limit valid?
                        // 2. is High limit valid?
                        // 3. if result == low limit is it a pass?
                        // 4. if result == high limit is it a pass?
                        // 5. if LO_LIMIT_LLM_SCAL_INVALID then use noLoLimit() in test ID
                        // 6. if HI_LIMIT_HLM_SCAL_INVALID then use noHiLimit() in test ID.
                        Result r3 = null;
                        if (mpr.getError() == Error_t.FAIL)
                        {
                            Float loLim = null;
                            Float hiLim = null;
                            if (mpr.useDefaultHiFlag())
                            {
                                if (mpr.getHiLimit() == StdfRecord.MISSING_FLOAT) hiLim = null; 
                                else hiLim = new Float(mpr.getHiLimit());
                            }
                            else
                            {
                                if (mpr.hasNoHiLimit()) hiLim = null;
                                else hiLim = new Float(mpr.getHiLimit());
                            }
                            if (mpr.useDefaultLoFlag())
                            {
                                if (mpr.hasNoLoLimit()) loLim = null; 
                                else loLim = new Float(mpr.getLoLimit());
                            }
                            else
                            {
                                if (mpr.hasNoHiLimit()) loLim = null;
                                else loLim = new Float(mpr.getLoLimit());
                            }
                            Error_t e = null;
                            if (loLim == null && hiLim == null)
                            {
                                e = Error_t.PASS;
                            }
                            else if (loLim == null)
                            {
                                if (mpr.doesHiLimitEqPass())
                                {
                                    if (mpr.getResult(pin) <= hiLim.floatValue()) e = Error_t.PASS;
                                    else e = Error_t.FAIL;
                                }
                                else
                                {
                                    if (mpr.getResult(pin) < hiLim.floatValue()) e = Error_t.PASS;
                                    else e = Error_t.FAIL;
                                }
                            }
                            else if (hiLim == null)
                            {
                                if (mpr.doesLoLimitEqPass())
                                {
                                    if (mpr.getResult(pin) >= loLim.floatValue()) e = Error_t.PASS;
                                    else e = Error_t.FAIL;
                                }
                                else
                                {
                                    if (mpr.getResult(pin) > loLim.floatValue()) e = Error_t.PASS;
                                    else e = Error_t.FAIL;
                                }
                            }
                            else
                            {
                                if (mpr.doesHiLimitEqPass() && mpr.doesLoLimitEqPass())
                                {
                                    if (mpr.getResult(pin) <= hiLim.floatValue() && mpr.getResult(pin) >= loLim.floatValue())
                                    {
                                        e = Error_t.PASS;
                                    }
                                    else e = Error_t.FAIL;
                                }
                                else if (mpr.doesHiLimitEqPass())
                                {
                                    if (mpr.getResult(pin) <= hiLim.floatValue() && mpr.getResult(pin) > loLim.floatValue()) 
                                    {
                                        e = Error_t.PASS;
                                    }
                                    else e = Error_t.FAIL;
                                }
                                else if (mpr.doesLoLimitEqPass())
                                {
                                    if (mpr.getResult(pin) < hiLim.floatValue() && mpr.getResult(pin) >= loLim.floatValue())
                                    {
                                        e = Error_t.PASS;
                                    }
                                    else e = Error_t.FAIL;
                                }
                                else
                                {
                                    if (mpr.getResult(pin) < hiLim.floatValue() && mpr.getResult(pin) > loLim.floatValue())
                                    {
                                        e = Error_t.PASS;
                                    }
                                    else e = Error_t.FAIL;
                                }
                            }
                            r3 = new Result(type, mpr.getResult(pin), e);
                        }
                        else 
                        {
                        	r3 = new Result(type, mpr.getResult(pin), mpr.getError());
                        }
                        addResult(waferOrStep, snOrXy, tid, r3, first);
                        first = false;
                        addColumnId(waferOrStep, ColIdentifier.getColIdentifier(tid, mpr.getLoLimit(), mpr.getHiLimit(), mpr.getUnits()));
                    }
                    break;
                case TEXT:
                    TextResult txt = TextResult.class.cast(tr);
                    Result r4 = new Result(type, txt.getResult());
                    addResult(waferOrStep, snOrXy, id, r4, first);
                    first = false;
                    addColumnId(waferOrStep, ColIdentifier.getColIdentifier(id, StdfRecord.MISSING_FLOAT, StdfRecord.MISSING_FLOAT, ""));
                    break;
                case FUNCTIONAL:
                    Result r2 = new Result(type, StdfRecord.MISSING_FLOAT, tr.getError());
                    addResult(waferOrStep, snOrXy, id, r2, first);
                    first = false;
                    addColumnId(waferOrStep, ColIdentifier.getColIdentifier(id, StdfRecord.MISSING_FLOAT, StdfRecord.MISSING_FLOAT, ""));
                default:
                }
            }
        }
        if (!noOverWrite)
        {
        	for (String stp : data.keySet())
        	{
        		Map<SnOrXy, LinkedHashMap<TestID, Result>> m = data.get(stp);
        		List<ResultList2> list = new ArrayList<ResultList2>();
        		for (SnOrXy sxy : m.keySet())
        		{
        			ResultList2 rls = new ResultList2(sxy, m.get(sxy));
        			list.add(rls);
        		}
        		sData.put(stp, list);
        	}
        }
        */
    }

}
