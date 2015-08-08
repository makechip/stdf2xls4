package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.*;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.Stdf2xls4;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.stdfapi.PageHeader;

public class HeaderBlock
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
    
	public void addBlock(Spreadsheet ss, int page)
    {
    	int row = PageTitleBlock.HEIGHT;
        ss.setColumnWidth(page, VALUE_COL, maxLength);
        for (String k : hdr.getNames())
        {
        	ss.setCell(page, new Coord(KEY_COL, row), HEADER2_FMT, k);
        	ss.setCell(page, new Coord(VALUE_COL, row), HEADER3_FMT, hdr.get(k));
        	if (hdr.get(k).length() > maxLength) 
        	{
        		maxLength = getLength(hdr.get(k));
                ss.setColumnWidth(page, VALUE_COL, maxLength);
        	}
            row++;
        }
        ss.setCell(page, new Coord(KEY_COL, row), HEADER2_FMT, VERSION_LABEL);
        ss.setCell(page, new Coord(VALUE_COL, row), HEADER3_FMT, Stdf2xls4.VERSION);
        row++;
        ss.setCell(page, new Coord(KEY_COL,   row), HEADER2_FMT, OPTIONS_LABEL);
        ss.setCell(page, new Coord(VALUE_COL, row), HEADER3_FMT, optionsString);
    }
	
	private int getLength(String s)
	{
		double sum = 0.0;
		for (int i=0; i<s.length(); i++)
		{
			if (Character.isUpperCase(s.charAt(i))) sum += 1.5;
			else sum += 1.0;
		}
		return((int) sum);
	}
    
    public int getHeight() 
    { 
        return(height); 
    }

	public int getWidth()
	{
		return(WIDTH);
	}

}
