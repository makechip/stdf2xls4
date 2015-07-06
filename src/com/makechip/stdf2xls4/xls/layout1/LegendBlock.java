package com.makechip.stdf2xls4.xls.layout1;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import static com.makechip.stdf2xls4.xls.Format_t.*;

public class LegendBlock
{
	public static final int LEGEND_ROW_HEIGHT = 30;
	public static final int WIDTH = 3;
	public static final int HEIGHT = 7;
    private static final int COL = 0;
    
    public static void addBlock(WritableSheet ws, HeaderBlock hb) throws WriteException
    {
    	int row = hb.getHeight();
    	ws.setRowView(row, LEGEND_ROW_HEIGHT);
        ws.mergeCells(COL, row,   COL+2, row);
        ws.mergeCells(COL, row+1, COL+2, row+1);
        ws.mergeCells(COL, row+2, COL+2, row+2);
        ws.mergeCells(COL, row+3, COL+2, row+3);
        ws.mergeCells(COL, row+4, COL+2, row+4);
        ws.mergeCells(COL, row+5, COL+2, row+5);
        ws.mergeCells(COL, row+6, COL+2, row+6);

        ws.addCell(new Label(COL, row,   "Legend:", HEADER1_FMT.getFormat()));
        ws.addCell(new Label(COL, row+1, "PASS", PASS_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+2, "FAIL", FAIL_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+3, "UNRELIABLE VALUE", UNRELIABLE_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+4, "TIMEOUT", TIMEOUT_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+5, "ALARM", ALARM_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+6, "ABORT", ABORT_VALUE_FMT.getFormat()));
    }
    
}
