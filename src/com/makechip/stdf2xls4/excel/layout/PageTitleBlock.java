package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.TITLE_FMT;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;

public class PageTitleBlock
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
    
    public void addBlock(Spreadsheet ss, int page, boolean merge)
    {
    	ss.setCell(page, new Coord(COL, 0), TITLE_FMT, title);
    	/* if (merge) */ ss.mergeCells(page, 0, HEIGHT - 1, COL, COL + width - 1);
    }

	public int getWidth()
	{
		return(width);
	}

	public int getHeight()
	{
		return(HEIGHT);
	}
}
