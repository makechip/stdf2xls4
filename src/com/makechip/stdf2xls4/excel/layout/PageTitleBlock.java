package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.TITLE_FMT;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;

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
    public void addBlock(Spreadsheet ss, int page)
    {
    	ss.setCell(page, new Coord(COL, 0), title);
    	ss.mergeCells(page, 0, HEIGHT - 1, COL, COL + width - 1);
    	addFormat(ss, page);
    }

    @Override
    public void addFormat(Spreadsheet ss, int page)
    {
    	ss.setFormat(page, new Coord(COL, 0), TITLE_FMT);
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
