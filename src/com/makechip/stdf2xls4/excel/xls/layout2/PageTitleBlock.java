package com.makechip.stdf2xls4.excel.xls.layout2;

import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.TITLE_FMT;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.xls.Block;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

class PageTitleBlock implements Block
{
	public static final int HEIGHT = 7;
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
   
    public PageTitleBlock(String title, int numDevices)
    {
    	this.title = title;
    	this.width = numDevices + CornerBlock.WIDTH;
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws WriteException, RowsExceededException, IOException
    {
        ws.mergeCells(COL, 0, COL + width - 1, HEIGHT - 1);
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
		return(HEIGHT);
	}
}
