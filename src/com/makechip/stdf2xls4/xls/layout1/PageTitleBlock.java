package com.makechip.stdf2xls4.xls.layout1;

import static com.makechip.stdf2xls4.xls.Format_t.TITLE_FMT;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

class PageTitleBlock
{
    //private static final int START_COL = LogoBlock.getWidth() + LegendBlock.getWidth();
    private static final int START_COL = LogoBlock.getWidth();
    
    
    public static void addBlock(String title, int width, WritableSheet ws) throws WriteException
    {
        ws.mergeCells(START_COL, TitleBlock.ROW, START_COL + width - 1, TitleBlock.HEIGHT - 1);
        ws.addCell(new Label(START_COL, TitleBlock.ROW, title, TITLE_FMT.getFormat()));
    }
}
