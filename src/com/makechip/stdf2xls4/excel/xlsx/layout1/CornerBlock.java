package com.makechip.stdf2xls4.excel.xlsx.layout1;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout1.HeaderBlock;

public class CornerBlock implements Block
{
	public static final String LABEL_WAFER = "Wafer";
	public static final String LABEL_STEP = "Step";
	public static final String LABEL_X = "X";
	public static final String LABEL_Y = "X";
	public static final String LABEL_SN = "S/N";
	public static final String LABEL_DUP = "Duplicate";
	public static final String LABEL_HW_BIN = "HW Bin";
	public static final String LABEL_SW_BIN = "SW Bin";
	public static final String LABEL_RESULT = "Result";
	public static final String LABEL_TEMP = "Temp";
	public static final String LABEL_TEST_NAME = "Test Name";
	public static final String LABEL_TEST_NUM = "Test Num";
	public static final String LABEL_LO_LIMIT = "Lo Limit";
	public static final String LABEL_HI_LIMIT = "Hi Limit";
	public static final String LABEL_PIN = "Pin";
	public static final String LABEL_UNITS = "Units";
	private final boolean wafersort;
	private final boolean onePage;
	private final int startCol;
	private final int devRow;
	private final int testRow;
	private final int width;
	
	public CornerBlock(boolean wafersort, boolean onePage, HeaderBlock hb)
	{
		this.wafersort = wafersort;
		this.onePage = onePage;
		startCol = onePage ? (wafersort ? 1 : 2) : (wafersort ? 2 : 3);
		devRow = hb.getHeight() + 8;
		testRow = hb.getHeight();
		width = 5 + 3 - startCol;
	}
	
	
	@Override
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		int col = startCol;
		int row = devRow - 1;
		Row r = ws.getRow(devRow - 1);
		if (r == null) r = ws.createRow(devRow - 1);
		CellStyle cs = Format_t.HEADER1_FMT.getFormat(wb);
		if (onePage)
		{
			String s = (wafersort) ? LABEL_WAFER : LABEL_STEP;
			setCell(r, col, cs, s); col++;
		}
		if (wafersort)
		{
			setCell(r, col, cs, LABEL_X); col++;
			setCell(r, col, cs, LABEL_Y); col++;
		}
		else 
		{
			setCell(r, col, cs, LABEL_SN); col++;
		}
		setCell(r, col, cs, LABEL_HW_BIN); col++;
		setCell(r, col, cs, LABEL_SW_BIN); col++;
		setCell(r, col, cs, LABEL_RESULT); col++;
		setCell(r, col, cs, LABEL_TEMP); col++;
		
		col = 7;
		row = testRow;
		cs = Format_t.HEADER4_FMT.getFormat(wb);
	    setCell(ws, col, row, cs, LABEL_TEST_NAME);	row++;
	    setCell(ws, col, row, cs, LABEL_DUP);	row++;
	    setCell(ws, col, row, cs, LABEL_LO_LIMIT);	row++;
	    setCell(ws, col, row, cs, LABEL_HI_LIMIT);	row++;
	    setCell(ws, col, row, cs, LABEL_PIN);	row++;
	    setCell(ws, col, row, cs, LABEL_UNITS);	row++;
	}

	public int getWaferOrStepCol() { return(startCol);	}
	public int getXCol()          { return(onePage ? startCol + 1 : startCol);  }
	public int getYCol()          { return(getXCol() + 1); }
	public int getSnOrYCol()      { return(wafersort ? getXCol() + 1 : getXCol()); }
	public int getHwBinCol()      { return(4); }
	public int getSwBinCol()      { return(5); }
	public int getResultCol()     { return(6); }
	public int getTempCol()       { return(7); }
	public int getFirstDataRow()  { return(devRow); }
	public int getTestNameRow()   { return(devRow - 8); }
	public int getTestNumberRow() { return(devRow - 7); }
	public int getDupNumRow()     { return(devRow - 6); }
	public int getLoLimitRow()    { return(devRow - 5); }
	public int getHiLimitRow()    { return(devRow - 4); }
	public int getPinNameRow()    { return(devRow - 3); }
	public int getUnitsRow()      { return(devRow - 2); }
	
	@Override
	public int getHeight() {  return(8); }

	@Override
	public int getWidth() { return(width); }

}
