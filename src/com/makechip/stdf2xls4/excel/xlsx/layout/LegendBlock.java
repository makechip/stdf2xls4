package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.xlsx.Block;

public class LegendBlock implements Block
{
	public static final int WIDTH = 1;
	public static final int HEIGHT = 7;
    public static final int COL = LogoBlock.WIDTH;
    public static final int ROW = 0;
    public static final int CELL_WIDTH = 24 * 256;
    
    public LegendBlock()
    {
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	Block.setCell(ws, new Coord(COL, ROW),   "Legend:");
    	Block.setCell(ws, new Coord(COL, ROW+1), "PASS");
    	Block.setCell(ws, new Coord(COL, ROW+2), "FAIL");
    	Block.setCell(ws, new Coord(COL, ROW+3), "UNRELIABLE VALUE");
    	Block.setCell(ws, new Coord(COL, ROW+4), "TIMEOUT");
    	Block.setCell(ws, new Coord(COL, ROW+5), "ALARM");
    	Block.setCell(ws, new Coord(COL, ROW+6), "ABORT");
    	ws.setColumnWidth(COL, CELL_WIDTH);
    	addFormat(wb, ws);
    }

    @Override
    public void addFormat(XSSFWorkbook wb, XSSFSheet ws)
    {
    	Block.setCell(ws, new Coord(COL, ROW),   HEADER1_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+1), PASS_VALUE_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+2), FAIL_VALUE_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+3), UNRELIABLE_VALUE_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+4), TIMEOUT_VALUE_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+5), ALARM_VALUE_FMT.getFormat(wb));
    	Block.setCell(ws, new Coord(COL, ROW+6), ABORT_VALUE_FMT.getFormat(wb));
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
