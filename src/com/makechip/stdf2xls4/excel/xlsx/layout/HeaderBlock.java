package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.Stdf2xls4;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.stdfapi.PageHeader;

public class HeaderBlock implements Block
{
	public static final int WIDTH = 2;
	public static final String OPTIONS_LABEL = "OPTIONS:";
	public static final String VERSION_LABEL = "VERSION:";
	public static final int KEY_COL = 0;
	public static final int VALUE_COL = 1;
	private final PageHeader hdr;
    private final int height;
    private final String optionsString;
    private double maxWidth;
    
    public HeaderBlock(CliOptions options, PageHeader hdr)
    {
        this.hdr = hdr;
        height = hdr.getNumFields() + 2;
        optionsString = options.toString();
        maxWidth = 20.0 * 256.0;
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	int row = PageTitleBlock.HEIGHT;
        for (String k : hdr.getNames())
        {
        	Row r = ws.getRow(row);
        	if (r == null) r = ws.createRow(row);
        	Block.setCell(r, KEY_COL,   HEADER2_FMT.getFormat(wb), k);
        	Block.setCell(r, VALUE_COL, HEADER3_FMT.getFormat(wb), hdr.get(k));
            ws.setColumnWidth(VALUE_COL, getCellWidth(hdr.get(k)));
            row++;
        }
        Block.setCell(ws, KEY_COL,   row, HEADER2_FMT.getFormat(wb), VERSION_LABEL);
        Block.setCell(ws, VALUE_COL, row, HEADER3_FMT.getFormat(wb), Stdf2xls4.VERSION);
        row++;
        Block.setCell(ws, KEY_COL,   row, HEADER2_FMT.getFormat(wb), OPTIONS_LABEL);
        Block.setCell(ws, VALUE_COL, row, HEADER3_FMT.getFormat(wb), optionsString);
    }
    
	private int getCellWidth(String testName)
	{
		double w = 0.0;
		for (int i=0; i<testName.length(); i++)
		{
			if (Character.isUpperCase(testName.charAt(i))) w += 1.5;
			else w += 1.0;
		}
		w *= 256.0;
		if (w > maxWidth) maxWidth = w;
		return((int) maxWidth);
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
