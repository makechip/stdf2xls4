package com.makechip.stdf2xls4.excel.xlsx.layout2;

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.HEADER4_FMT;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.LegendBlock;

public class CornerBlock implements Block
{
	public static final String LABEL_TIMESTAMP = "TimeStamp";
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
	private final boolean timeStampedFiles;
	private final int startRow;
	private final int devCol;
	public static final int WIDTH = 8;
	
	public CornerBlock(boolean wafersort, boolean onePage, boolean timeStampedFiles, HeaderBlock hb)
	{
		this.wafersort = wafersort;
		this.onePage = onePage;
		this.timeStampedFiles = timeStampedFiles;
		int h = LegendBlock.HEIGHT;
		startRow = onePage ? (wafersort ? h + 7 : h + 6) : (wafersort ? h + 6 : h + 5);
		devCol = 14;
	}
	
	@Override
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		int col = devCol - 1;
		int row = PageTitleBlock.HEIGHT;
		CellStyle cs = HEADER1_FMT.getFormat(wb);
		if (timeStampedFiles)
		{
			setCell(ws, col, row++, cs, LABEL_TIMESTAMP);
		}
		if (onePage)
		{
			String s = (wafersort) ? LABEL_WAFER : LABEL_STEP;
			setCell(ws, col, row++, cs, s);
		}
		if (wafersort)
		{
			setCell(ws, col, row++, cs, LABEL_X);
			setCell(ws, col, row++, cs, LABEL_Y);
		}
		else 
		{
			setCell(ws, col, row++, cs, LABEL_SN);
		}
		setCell(ws, col, row++, cs, LABEL_HW_BIN);
		setCell(ws, col, row++, cs, LABEL_SW_BIN);
		setCell(ws, col, row++, cs, LABEL_RESULT);
		setCell(ws, col, row++, cs, LABEL_TEMP);
		
		cs = HEADER4_FMT.getFormat(wb);
		Row r = ws.getRow(LegendBlock.HEIGHT);
		if (r == null) r = ws.createRow(LegendBlock.HEIGHT);
		setCell(r, getTestNameCol(), cs, LABEL_TEST_NAME);
		setCell(r, getTestNumberCol(), cs, LABEL_TEST_NUM);
		setCell(r, getDupNumCol(), cs, LABEL_DUP);
		setCell(r, getPinNameCol(), cs, LABEL_PIN);
		setCell(r, getLoLimitCol(), cs, LABEL_LO_LIMIT);
		setCell(r, getHiLimitCol(), cs, LABEL_HI_LIMIT);
		setCell(r, getUnitsCol(), cs, LABEL_UNITS);

		ws.setColumnWidth(getTestNameCol(), 256 * 48);
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getTestNameCol(), getTestNameCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getTestNumberCol(), getTestNumberCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getDupNumCol(), getDupNumCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getPinNameCol(), getPinNameCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getLoLimitCol(), getLoLimitCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getHiLimitCol(), getHiLimitCol()));
		ws.addMergedRegion(new CellRangeAddress(LegendBlock.HEIGHT, startRow - 1, getUnitsCol(), getUnitsCol()));
	}

	public int getTimeStampRow()   { return(7); }
	public int getWaferOrStepRow() { return(timeStampedFiles ? 8 : 7);	}
	public int getXRow()           { return(onePage ? getWaferOrStepRow() + 1 : getWaferOrStepRow());  }
	public int getYRow()           { return(getXRow() + 1); }
	public int getSnOrYRow()       { return(wafersort ? getXRow() + 1 : getXRow()); }
	public int getHwBinRow()       { return(getSnOrYRow() + 1); }
	public int getSwBinRow()       { return(getSnOrYRow() + 2); }
	public int getResultRow()      { return(getSnOrYRow() + 3); }
	public int getTempRow()        { return(getSnOrYRow() + 4); }
	public int getFirstDataCol()   { return(devCol); }
	public int getTestNameCol()    { return(devCol - 8); }
	public int getTestNumberCol()  { return(devCol - 7); }
	public int getDupNumCol()      { return(devCol - 6); }
	public int getLoLimitCol()     { return(devCol - 5); }
	public int getHiLimitCol()     { return(devCol - 4); }
	public int getPinNameCol()     { return(devCol - 3); }
	public int getUnitsCol()       { return(devCol - 2); }
	
	@Override
	public int getHeight() 
	{  
		return(timeStampedFiles ? (onePage ? (wafersort ? 8 : 7) : (wafersort ? 7 : 6)) : (onePage ? (wafersort ? 7 : 6) : (wafersort ? 6 : 5))); 
	}

	@Override
	public int getWidth() { return(WIDTH); }

}
