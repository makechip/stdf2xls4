package com.makechip.stdf2xls4.excel.layout;

import java.io.File;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.Format_t;

class LogoBlock
{
	public static final int WIDTH = 1;
    public static final int HEIGHT = 7;	
    public static final int COL = 0;
	public static final int ROW = 0;
	private File logoFile;
    
    /**
     * Constructor for the LogoBlock.
     * @param logoFile Image file containing JPG image.
     * This parameter may be null, in which case a default logo will be supplied.
     */
    public LogoBlock(File logoFile)
    {
    	this.logoFile = logoFile;
    }
    
    public void addBlock(Spreadsheet ss, int page, boolean merge)
    {
   		if (merge) ss.mergeCells(page, ROW, ROW + HEIGHT - 1, COL, COL);
   		ss.setColumnWidth(page, COL, 32);
   		if (logoFile == null)  ss.setCell(page, new Coord(COL, ROW), Format_t.LOGO_FMT, "makechip.com");
    	else ss.addImage(page, logoFile, new Coord(COL, ROW), new Coord(COL + WIDTH, ROW + HEIGHT));
    }
    
	public int getWidth()
	{
		return(WIDTH);
	}

	public int getHeight()
	{
		return(HEIGHT);
	}
    
}
