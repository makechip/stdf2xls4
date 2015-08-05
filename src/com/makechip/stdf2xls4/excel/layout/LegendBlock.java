package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.*;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;

public class LegendBlock implements Block
{
	public static final int WIDTH = 1;
	public static final int HEIGHT = 7;
    public static final int COL = LogoBlock.WIDTH;
    public static final int ROW = 0;
    public static final int CELL_WIDTH = 24;
    
    public LegendBlock()
    {
    }
    
    @Override
    public void addBlock(Spreadsheet ss, int page)
    {
    	ss.setCell(page, new Coord(COL, ROW),   "Legend:");
    	ss.setCell(page, new Coord(COL, ROW+1), "PASS");
    	ss.setCell(page, new Coord(COL, ROW+2), "FAIL");
    	ss.setCell(page, new Coord(COL, ROW+3), "UNRELIABLE VALUE");
    	ss.setCell(page, new Coord(COL, ROW+4), "TIMEOUT");
    	ss.setCell(page, new Coord(COL, ROW+5), "ALARM");
    	ss.setCell(page, new Coord(COL, ROW+6), "ABORT");
    	ss.setColumnWidth(page, COL, CELL_WIDTH);
    	addFormat(ss, page);
    }

    @Override
    public void addFormat(Spreadsheet ss, int page)
    {
    	ss.setFormat(page, new Coord(COL, ROW),   HEADER1_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+1), PASS_VALUE_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+2), FAIL_VALUE_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+3), UNRELIABLE_VALUE_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+4), TIMEOUT_VALUE_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+5), ALARM_VALUE_FMT);
    	ss.setFormat(page, new Coord(COL, ROW+6), ABORT_VALUE_FMT);
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
