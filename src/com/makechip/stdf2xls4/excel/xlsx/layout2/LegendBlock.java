package com.makechip.stdf2xls4.excel.xlsx.layout2;

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;

public class LegendBlock implements Block
{
	public static final int WIDTH = 2;
	public static final int HEIGHT = 7;
    private static final int COL = 0;
    private final int row;
    
    public LegendBlock()
    {
        row = 0;
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	setCell(ws, COL, row, HEADER1_FMT.getFormat(wb), "Legend:");
    	setCell(ws, COL, row+1, PASS_VALUE_FMT.getFormat(wb), "PASS");
    	setCell(ws, COL, row+2, FAIL_VALUE_FMT.getFormat(wb), "FAIL");
    	setCell(ws, COL, row+3, UNRELIABLE_VALUE_FMT.getFormat(wb), "UNRELIABLE VALUE");
    	setCell(ws, COL, row+4, TIMEOUT_VALUE_FMT.getFormat(wb), "TIMEOUT");
    	setCell(ws, COL, row+5, ALARM_VALUE_FMT.getFormat(wb), "ALARM");
    	setCell(ws, COL, row+6, ABORT_VALUE_FMT.getFormat(wb), "ABORT");
    	ws.addMergedRegion(new CellRangeAddress(row, row, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+1, row+1, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+2, row+2, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+3, row+3, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+4, row+4, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+5, row+5, COL, COL+1));
    	ws.addMergedRegion(new CellRangeAddress(row+6, row+6, COL, COL+1));
    }

	@Override
	public int getWidth()
	{
		return(WIDTH);
	}

	@Override
	public int getHeight()
	{
		return(HEIGHT);
	}
    
}
