package com.makechip.stdf2xls4.xls.layout1;

import jxl.write.WritableSheet;
import jxl.write.WriteException;

public class TitleBlock
{
    public static final int ROW = 0;
    public static final int COL = 0;
    public static final int HEIGHT = 8;
    
    public static void addBlock(WritableSheet ws, String pageTitle, int titleWidth) throws WriteException
    {
        LogoBlock.addBlock(ws);
        LegendBlock.addBlock(ws);
        PageTitleBlock.addBlock(pageTitle, titleWidth, ws);
    }

}
