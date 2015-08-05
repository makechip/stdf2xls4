package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.*;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.Stdf2xls4;
import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;
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
    private int maxLength;
    
    public HeaderBlock(CliOptions options, PageHeader hdr)
    {
        this.hdr = hdr;
        height = hdr.getNumFields() + 2;
        optionsString = options.toString();
        maxLength = 20;
    }
    
    @Override
	public void addBlock(Spreadsheet ss, int page)
    {
    	int row = PageTitleBlock.HEIGHT;
        ss.setColumnWidth(page, VALUE_COL, maxLength);
        for (String k : hdr.getNames())
        {
        	ss.setCell(page, new Coord(KEY_COL, row), k);
        	ss.setCell(page, new Coord(VALUE_COL, row), hdr.get(k));
        	if (hdr.get(k).length() > maxLength) 
        	{
        		maxLength = hdr.get(k).length();
                ss.setColumnWidth(page, VALUE_COL, maxLength);
        	}
            row++;
        }
        ss.setCell(page, new Coord(KEY_COL, row), VERSION_LABEL);
        ss.setCell(page, new Coord(VALUE_COL, row), Stdf2xls4.VERSION);
        row++;
        ss.setCell(page, new Coord(KEY_COL,   row), OPTIONS_LABEL);
        ss.setCell(page, new Coord(VALUE_COL, row), optionsString);
        addFormat(ss, page);
    }
    
    @Override
	public void addFormat(Spreadsheet ss, int page)
    {
    	int row = PageTitleBlock.HEIGHT;
        for (@SuppressWarnings("unused") String k : hdr.getNames())
        {
        	ss.setFormat(page, new Coord(KEY_COL, row), HEADER2_FMT);
        	ss.setFormat(page, new Coord(VALUE_COL, row), HEADER3_FMT);
            row++;
        }
        ss.setFormat(page, new Coord(KEY_COL,   row), HEADER2_FMT);
        ss.setFormat(page, new Coord(VALUE_COL, row), HEADER3_FMT);
        row++;
        ss.setFormat(page, new Coord(KEY_COL,   row), HEADER2_FMT);
        ss.setFormat(page, new Coord(VALUE_COL, row), HEADER3_FMT);
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
