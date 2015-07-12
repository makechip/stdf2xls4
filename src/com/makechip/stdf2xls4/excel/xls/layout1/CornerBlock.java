package com.makechip.stdf2xls4.excel.xls.layout1;

import static com.makechip.stdf2xls4.excel.xls.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.xls.Format_t.HEADER4_FMT;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.Block;

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
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int col = startCol;
		int row = devRow - 1;
		if (onePage)
		{
			String s = (wafersort) ? LABEL_WAFER : LABEL_STEP;
			ws.addCell(new Label(col++, row, s, HEADER1_FMT.getFormat()));
		}
		if (wafersort)
		{
	        ws.addCell(new Label(col++, row, LABEL_X, HEADER1_FMT.getFormat()));	
	        ws.addCell(new Label(col++, row, LABEL_Y, HEADER1_FMT.getFormat()));	
		}
		else 
		{
			ws.addCell(new Label(col++, row, LABEL_SN, HEADER1_FMT.getFormat()));
		}
		ws.addCell(new Label(col++, row, LABEL_HW_BIN, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, LABEL_SW_BIN, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, LABEL_RESULT, HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, LABEL_TEMP, HEADER1_FMT.getFormat()));
		
		col = 7;
		row = testRow;
		
        ws.addCell(new Label(col, row, LABEL_TEST_NAME, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+1, LABEL_TEST_NUM, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+2, LABEL_DUP, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+3, LABEL_LO_LIMIT, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+4, LABEL_HI_LIMIT, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+5, LABEL_PIN, HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+6, LABEL_UNITS, HEADER4_FMT.getFormat()));
	}

	public int getWaferOrStepCol() { return(startCol);	}
	public int getXCol()          { return(onePage ? startCol + 1 : startCol);  }
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
