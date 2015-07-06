package com.makechip.stdf2xls4.excel.xls.layout1;

import static com.makechip.stdf2xls4.excel.xls.Format_t.TITLE_FMT;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.Block;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

class PageTitleBlock implements Block
{
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
    private final int height;
   
    public PageTitleBlock(String title, int width, HeaderBlock hb)
    {
    	this.title = title;
    	this.width = width;
    	height = hb.getHeight();
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws WriteException, RowsExceededException, IOException
    {
        ws.mergeCells(COL, TitleBlock.ROW, COL + width - 1, height - 1);
        ws.addCell(new Label(COL, 0, title, TITLE_FMT.getFormat()));
    }

	@Override
	public int getWidth()
	{
		return(width);
	}

	@Override
	public int getHeight()
	{
		return(height);
	}
}
