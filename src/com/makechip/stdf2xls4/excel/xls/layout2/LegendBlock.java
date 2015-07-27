package com.makechip.stdf2xls4.excel.xls.layout2;

import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.*;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.Block;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class LegendBlock implements Block
{
	public static final int WIDTH = 2;
	public static final int HEIGHT = 7;
    private static final int COL = 0;
    private final int row;
    
    public LegendBlock()
    {
        row = 0;
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
    {
        ws.mergeCells(COL, row,   COL+1, row);
        ws.mergeCells(COL, row+1, COL+1, row+1);
        ws.mergeCells(COL, row+2, COL+1, row+2);
        ws.mergeCells(COL, row+3, COL+1, row+3);
        ws.mergeCells(COL, row+4, COL+1, row+4);
        ws.mergeCells(COL, row+5, COL+1, row+5);
        ws.mergeCells(COL, row+6, COL+1, row+6);

        ws.addCell(new Label(COL, row,   "Legend:", HEADER1_FMT.getFormat()));
        ws.addCell(new Label(COL, row+1, "PASS", PASS_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+2, "FAIL", FAIL_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+3, "UNRELIABLE VALUE", UNRELIABLE_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+4, "TIMEOUT", TIMEOUT_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+5, "ALARM", ALARM_VALUE_FMT.getFormat()));
        ws.addCell(new Label(COL, row+6, "ABORT", ABORT_VALUE_FMT.getFormat()));
    }

	@Override
	public int getWidth()
	{
		return(WIDTH);
	}

	@Override
	public int getHeight()
	{
		return(HEIGHT);
	}
    
}
