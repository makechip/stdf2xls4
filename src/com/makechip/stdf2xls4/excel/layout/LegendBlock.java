package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.*;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;

public class LegendBlock
{
	public static final int WIDTH = 1;
	public static final int HEIGHT = 7;
    public static final int COL = LogoBlock.WIDTH;
    public static final int ROW = 0;
    public static final int CELL_WIDTH = 24;
    
    public LegendBlock()
    {
    }
    
    public void addBlock(Spreadsheet ss, int page)
    {
    	ss.setCell(page, new Coord(COL, ROW), PASS_VALUE_FMTC, "PASS");
    	ss.setCell(page, new Coord(COL, ROW+1), FAIL_VALUE_FMTC, "FAIL");
    	ss.setCell(page, new Coord(COL, ROW+2), UNRELIABLE_VALUE_FMTC, "UNRELIABLE VALUE");
    	ss.setCell(page, new Coord(COL, ROW+3), TIMEOUT_VALUE_FMTC, "TIMEOUT");
    	ss.setCell(page, new Coord(COL, ROW+4), ALARM_VALUE_FMTC, "ALARM");
    	ss.setCell(page, new Coord(COL, ROW+5), ABORT_VALUE_FMTC, "ABORT");
    	ss.setCell(page, new Coord(COL, ROW+6), INVALID_VALUE_FMTC, "INVALID");
    	ss.setColumnWidth(page, COL, CELL_WIDTH);
    }

	public int getWidth()
	{
		return(WIDTH);
	}

	public int getHeight()
	{
		return(HEIGHT);
	}
    
}
