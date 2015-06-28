package com.makechip.stdf2xls4.xls.layout1;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import static com.makechip.stdf2xls4.xls.Format_t.*;

public class LegendBlock
{
    private static final int COL = TitleBlock.COL + LogoBlock.getWidth();
    private static final int ROW = TitleBlock.ROW;
    
    public static void addBlock(WritableSheet ws) throws WriteException
    {
        ws.mergeCells(COL, ROW, COL+2, ROW+1);
        ws.addCell(new Label(COL, ROW, "Legend:", HEADER1_FMT.getFormat()));
        ws.mergeCells(COL, ROW+2, COL+2, ROW+2);
        ws.mergeCells(COL, ROW+3, COL+2, ROW+3);
        ws.mergeCells(COL, ROW+4, COL+2, ROW+4);
        ws.mergeCells(COL, ROW+5, COL+2, ROW+5);
        ws.mergeCells(COL, ROW+6, COL+2, ROW+6);
        //ws.mergeCells(COL, ROW+7, COL+2, ROW+7);
        ws.addCell(new Label(COL, ROW+2, "FAIL", FAIL_VALUE_FMT.getFormat()));
        //ws.addCell(new Label(COL, ROW+3, "INVALID VALUE", INVALID_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, ROW+3, "UNRELIABLE VALUE", UNRELIABLE_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, ROW+4, "TIMEOUT", TIMEOUT_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, ROW+5, "ALARM", ALARM_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, ROW+6, "ABORT", ABORT_VALUE_FMT.getFormat()));
    }
    
    public static int getWidth() { return(3); }
    
    public static int getHeight() { return(LogoBlock.getHeight()); }
}
