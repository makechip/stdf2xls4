package com.makechip.stdf2xls4.excel.xlsx.layout1;

import static com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;

public class LegendBlock implements Block
{
	public static final short LEGEND_ROW_HEIGHT = 1400;
	public static final int WIDTH = 3;
	public static final int HEIGHT = 7;
    private static final int COL = 0;
    private final int row;
    
    public LegendBlock(HeaderBlock hb)
    {
        row = hb.getHeight();	
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
        setCell(ws, COL, row, HEADER1_FMT.getFormat(wb), "Legend:");
        setCell(ws, COL, row+1, PASS_VALUE_FMT.getFormat(wb), "PASS");
        setCell(ws, COL, row+2, FAIL_VALUE_FMT.getFormat(wb), "FAIL");
        setCell(ws, COL, row+3, UNRELIABLE_VALUE_FMT.getFormat(wb), "UNRELIABLE_VALUE");
        setCell(ws, COL, row+4, TIMEOUT_VALUE_FMT.getFormat(wb), "TIMEOUT");
        setCell(ws, COL, row+5, ALARM_VALUE_FMT.getFormat(wb), "ALARM");
        setCell(ws, COL, row+6, ABORT_VALUE_FMT.getFormat(wb), "ABORT");
        Row r = ws.getRow(row);
        if (r == null) r = ws.createRow(row);
        r.setHeight(LEGEND_ROW_HEIGHT);
        ws.addMergedRegion(new CellRangeAddress(row, row, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+1, row+1, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+2, row+2, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+3, row+3, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+4, row+4, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+5, row+5, COL, COL+2));
        ws.addMergedRegion(new CellRangeAddress(row+6, row+6, COL, COL+2));
    }

	@Override
	public int getWidth()
	{
		return(3);
	}

	@Override
	public int getHeight()
	{
		return(7);
	}
    
}
