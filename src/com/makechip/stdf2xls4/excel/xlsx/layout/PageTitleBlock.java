package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.TITLE_FMT;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.xlsx.Block;

public class PageTitleBlock implements Block
{
	public static final int HEIGHT = 7;
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
   
    public PageTitleBlock(String title, int numCols, int cornerBlockWidth)
    {
    	this.title = title;
    	this.width = numCols + cornerBlockWidth;
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	Block.setCell(ws, new Coord(COL, 0), title);
    	ws.addMergedRegion(new CellRangeAddress(0, HEIGHT - 1, COL, COL + width - 1));
    	addFormat(wb, ws);
    }

    @Override
    public void addFormat(XSSFWorkbook wb, XSSFSheet ws)
    {
    	Block.setCell(ws, new Coord(COL, 0), TITLE_FMT.getFormat(wb));
    }

	@Override
	public int getWidth()
	{
		return(width);
	}

	@Override
	public int getHeight()
	{
		return(HEIGHT);
	}
}
