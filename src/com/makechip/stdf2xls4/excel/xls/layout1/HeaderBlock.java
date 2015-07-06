package com.makechip.stdf2xls4.excel.xls.layout1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.stdfapi.PageHeader;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import static com.makechip.stdf2xls4.excel.xls.Format_t.*;

@SuppressWarnings("unused")
public class HeaderBlock implements Block
{
	public static final int WIDTH = 8;
    //private final int height;
    
    public HeaderBlock(PageHeader hdr)
    {
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
    {
    	/*
        int col = COL;
        int row = ROW;
        for (HeaderItem h : items)
        {
            ws.mergeCells(col, row, col+2, row);
            ws.mergeCells(col+3, row, LogoBlock.getWidth() + LegendBlock.getWidth()-1, row);
            ws.addCell(new Label(col, row, h.getLabel(), HEADER2_FMT.getFormat()));
            ws.addCell(new Label(col+3, row, h.getValue(), HEADER3_FMT.getFormat()));
            row++;
        }
        */
    }
    
    public int getHeight() 
    { 
        return(0); 
    }

	@Override
	public int getWidth()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
