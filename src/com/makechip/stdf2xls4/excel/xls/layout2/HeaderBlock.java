package com.makechip.stdf2xls4.excel.xls.layout2;

import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.stdfapi.PageHeader;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@SuppressWarnings("unused")
public class HeaderBlock implements Block
{
	public static final int WIDTH = 6;
	public static final String OPTIONS_LABEL = "OPTIONS:";
	public static final int VALUE_COL = 2;
	private final PageHeader hdr;
    private final int height;
    private final String optionsString;
    
    public HeaderBlock(CliOptions options, PageHeader hdr)
    {
        this.hdr = hdr;
        height = hdr.getNumFields() + 1;
        optionsString = options.toString();
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
    {
    	int row = PageTitleBlock.HEIGHT;
        for (String k : hdr.getNames())
        {
            ws.mergeCells(0, row, 1, row);
            ws.mergeCells(2, row, WIDTH-1, row);
            ws.addCell(new Label(0, row, k, HEADER2_FMT.getFormat()));
            ws.addCell(new Label(VALUE_COL, row, hdr.get(k), HEADER3_FMT.getFormat()));
            row++;
        }
        ws.mergeCells(0,  row, 2, row);
        ws.mergeCells(3, row, WIDTH-1, row);
        ws.addCell(new Label(0, row, OPTIONS_LABEL, HEADER2_FMT.getFormat()));
        ws.addCell(new Label(VALUE_COL, row, optionsString, HEADER3_FMT.getFormat()));
    }
    
    public int getHeight() 
    { 
        return(height); 
    }

	@Override
	public int getWidth()
	{
		return(WIDTH);
	}
}
