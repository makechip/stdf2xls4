package com.makechip.stdf2xls4.xls.layout1;

import java.util.ArrayList;
import java.util.List;

import com.makechip.stdf2xls4.stdfapi.PageHeader;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import static com.makechip.stdf2xls4.xls.Format_t.*;

@SuppressWarnings("unused")
public class HeaderBlock
{
	public static final int WIDTH = 8;
    public static final int ROW = TitleBlock.HEIGHT;
    public static final int COL = TitleBlock.COL;
    //private final int height;
    
    public HeaderBlock(PageHeader hdr)
    {
    }
    
    public void addBlock(WritableSheet ws) throws WriteException
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
}
