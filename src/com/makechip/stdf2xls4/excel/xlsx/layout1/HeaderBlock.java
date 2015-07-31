package com.makechip.stdf2xls4.excel.xlsx.layout1;

import static com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t.*;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.Stdf2xls4;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.stdfapi.PageHeader;

public class HeaderBlock implements Block
{
	public static final int WIDTH = 8;
	public static final String OPTIONS_LABEL = "OPTIONS:";
	public static final String VERSION_LABEL = "VERSION:";
	public static final int VALUE_COL = 3;
	private final PageHeader hdr;
    private final int height;
    private final String optionsString;
    
    public HeaderBlock(CliOptions options, PageHeader hdr)
    {
        this.hdr = hdr;
        height = hdr.getNumFields() + 2;
        optionsString = options.toString();
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	int row = 0;
    	CellStyle cs2 = HEADER2_FMT.getFormat(wb);
    	CellStyle cs3 = HEADER3_FMT.getFormat(wb);
        for (String k : hdr.getNames())
        {
        	setCell(ws, 0, row, cs2, k);
        	setCell(ws, VALUE_COL, row, cs3, hdr.get(k));
      	    ws.addMergedRegion(new CellRangeAddress(row, row, 0, VALUE_COL-1));
       	    ws.addMergedRegion(new CellRangeAddress(row, row, VALUE_COL, WIDTH-1));
            row++;
        }
        setCell(ws, 0, row, cs2, VERSION_LABEL);
        setCell(ws, VALUE_COL, row, cs3, Stdf2xls4.VERSION);
      	ws.addMergedRegion(new CellRangeAddress(row, row, 0, VALUE_COL-1));
       	ws.addMergedRegion(new CellRangeAddress(row, row, VALUE_COL, WIDTH-1));
       	row++;
        setCell(ws, 0, row, cs2, OPTIONS_LABEL);
        setCell(ws, VALUE_COL, row, cs3, optionsString);
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
