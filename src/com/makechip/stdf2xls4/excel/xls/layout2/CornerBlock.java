package com.makechip.stdf2xls4.excel.xls.layout2;

import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.HEADER4_FMT;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.xls.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.LegendBlock;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
	private final int startRow;
	private final int devCol;
	private final int testCol;
	private final int width;
	
	public CornerBlock(boolean wafersort, boolean onePage, HeaderBlock hb)
	{
		this.wafersort = wafersort;
		this.onePage = onePage;
		int h = LegendBlock.HEIGHT;
		startRow = onePage ? (wafersort ? h + 7 : h + 6) : (wafersort ? h + 6 : h + 5);
		devCol = 16;
		testCol = 6;
		width = 8;
	}
	
	@Override
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int col = devCol - 1;
		int row = 7;
		if (onePage)
		{
			String s = (wafersort) ? LABEL_WAFER : LABEL_STEP;
			ws.addCell(new Label(col, row++, s, HEADER1_FMT.getFormat()));
		}
		if (wafersort)
		{
	        ws.addCell(new Label(col, row++, LABEL_X, HEADER1_FMT.getFormat()));	
	        ws.addCell(new Label(col, row++, LABEL_Y, HEADER1_FMT.getFormat()));	
		}
		else 
		{
			ws.addCell(new Label(col, row++, LABEL_SN, HEADER1_FMT.getFormat()));
		}
		ws.addCell(new Label(col, row++, LABEL_HW_BIN, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col, row++, LABEL_SW_BIN, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col, row++, LABEL_RESULT, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col, row++, LABEL_TEMP, HEADER1_FMT.getFormat()));
		
		col = 7;
		row = testCol;
		ws.setColumnView(8, 32);
		ws.mergeCells(8,  7,  8, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(9,  7,  9, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(10, 7, 10, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(11, 7, 11, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(12, 7, 12, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(13, 7, 13, startRow - LegendBlock.HEIGHT);
		ws.mergeCells(14, 7, 14, startRow - LegendBlock.HEIGHT);
		
        ws.addCell(new Label(8,  7, LABEL_TEST_NAME, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(9,  7, LABEL_TEST_NUM, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(10, 7, LABEL_DUP, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(11, 7, LABEL_PIN, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(12, 7, LABEL_LO_LIMIT, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(13, 7, LABEL_HI_LIMIT, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(14, 7, LABEL_UNITS, HEADER4_FMT.getFormat()));
	}

	public int getWaferOrStepRow() { return(7);	}
	public int getXRow()          { return(onePage ? 8 : 7);  }
	public int getYRow()          { return(getXRow() + 1); }
	public int getSnOrYRow()      { return(wafersort ? getXRow() + 1 : getXRow()); }
	public int getHwBinRow()      { return(getSnOrYRow() + 1); }
	public int getSwBinRow()      { return(getSnOrYRow() + 2); }
	public int getResultRow()     { return(getSnOrYRow() + 3); }
	public int getTempRow()       { return(getSnOrYRow() + 4); }
	public int getFirstDataCol()  { return(devCol); }
	public int getTestNameCol()   { return(devCol - 8); }
	public int getTestNumberCol() { return(devCol - 7); }
	public int getDupNumCol()     { return(devCol - 6); }
	public int getLoLimitCol()    { return(devCol - 5); }
	public int getHiLimitCol()    { return(devCol - 4); }
	public int getPinNameCol()    { return(devCol - 3); }
	public int getUnitsCol()      { return(devCol - 2); }
	
	@Override
	public int getHeight() {  return(onePage ? (wafersort ? 7 : 6) : (wafersort ? 6 : 5)); }

	@Override
	public int getWidth() { return(width); }

}
