package com.makechip.stdf2xls4.excel.xls.layout1;

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
import static com.makechip.stdf2xls4.excel.xls.Format_t.*;

@SuppressWarnings("unused")
public class HeaderBlock implements Block
{
	public static final int WIDTH = 8;
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
    	int row = 0;
        for (String k : hdr.getNames())
        {
            ws.mergeCells(0, row, 2, row);
            ws.mergeCells(3, row, WIDTH-1, row);
            ws.addCell(new Label(0, row, k, HEADER2_FMT.getFormat()));
            ws.addCell(new Label(3, row, hdr.get(k), HEADER3_FMT.getFormat()));
            row++;
        }
        ws.mergeCells(0,  row, 2, row);
        ws.mergeCells(3, row, WIDTH-1, row);
        ws.addCell(new Label(0, row, "OPTIONS:", HEADER2_FMT.getFormat()));
        ws.addCell(new Label(3, row, optionsString, HEADER3_FMT.getFormat()));
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
