package com.makechip.stdf2xls4.excel.xlsx.layout2;

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.Stdf2xls4;
import com.makechip.stdf2xls4.excel.xlsx.Block;
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
	public static final String VERSION_LABEL = "VERSION:";
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
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	int row = PageTitleBlock.HEIGHT;
        for (String k : hdr.getNames())
        {
        	Row r = ws.getRow(row);
        	if (r == null) ws.createRow(row);
        	setCell(r, 0, HEADER2_FMT.getFormat(wb), k);
        	setCell(r, VALUE_COL, HEADER3_FMT.getFormat(wb), hdr.get(k));
        	ws.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
        	ws.addMergedRegion(new CellRangeAddress(row, row, VALUE_COL, WIDTH-1));
            row++;
        }
        setCell(ws, 0, row, HEADER2_FMT.getFormat(wb), VERSION_LABEL);
        setCell(ws, VALUE_COL, row, HEADER3_FMT.getFormat(wb), Stdf2xls4.VERSION);
        ws.addMergedRegion(new CellRangeAddress(row, row, 0, VALUE_COL-1));
        ws.addMergedRegion(new CellRangeAddress(row, row, VALUE_COL, WIDTH-1));
        row++;
        setCell(ws, 0, row, HEADER2_FMT.getFormat(wb), OPTIONS_LABEL);
        setCell(ws, VALUE_COL, row, HEADER3_FMT.getFormat(wb), optionsString);
        ws.addMergedRegion(new CellRangeAddress(row, row, 0, VALUE_COL-1));
        ws.addMergedRegion(new CellRangeAddress(row, row, VALUE_COL, WIDTH-1));
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
