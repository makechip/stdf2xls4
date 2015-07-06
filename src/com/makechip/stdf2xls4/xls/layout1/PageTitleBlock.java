package com.makechip.stdf2xls4.xls.layout1;

import static com.makechip.stdf2xls4.xls.Format_t.TITLE_FMT;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

class PageTitleBlock
{
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
   
    public PageTitleBlock(String title, int width)
    {
    	this.title = title;
    	this.width = width;
    }
    
    public void addBlock(WritableSheet ws, HeaderBlock hb) throws WriteException
    {
        ws.mergeCells(COL, TitleBlock.ROW, COL + width - 1, hb.getHeight() - 1);
        ws.addCell(new Label(COL, 0, title, TITLE_FMT.getFormat()));
    }
}
