package com.makechip.stdf2xls4.excel.xls.layout2;

import java.io.File;
import java.io.IOException;

import com.makechip.stdf2xls4.excel.xls.Block;
import com.makechip.stdf2xls4.excel.xls.layout2.Format_t;

import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

class LogoBlock implements Block
{
	public static final int WIDTH = 4;
    public static final int HEIGHT = 7;	
    private static final int COL = LegendBlock.WIDTH;
	@SuppressWarnings("unused")
	private static final double CELL_DEFAULT_HEIGHT = 255;
	@SuppressWarnings("unused")
	private static final double CELL_DEFAULT_WIDTH = 64;
	private File logoFile;
	private final int row;
    
    /**
     * Constructor for the LogoBlock.
     * @param logoFile Image file containing JPG image.
     * This parameter may be null, in which case a default logo will be supplied.
     */
    public LogoBlock(File logoFile)
    {
    	this.logoFile = logoFile;
    	row = 0;
    }
    
    @Override
    public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
    {
   		ws.mergeCells(COL, row, COL + WIDTH - 1, row + HEIGHT - 1);
    	if (logoFile == null)
    	{
    		Label l = new Label(COL, row, "makechip.com", Format_t.LOGO_FMT.getFormat());
    		ws.addCell(l);
    	}
    	else
    	{
    		WritableImage image = new WritableImage(COL, row, 4.0, 6.9, logoFile);
    		ws.addImage(image);
    	}
    }

	@Override
	public int getWidth()
	{
		return(WIDTH);
	}

	@Override
	public int getHeight()
	{
		return(HEIGHT);
	}
    
}
