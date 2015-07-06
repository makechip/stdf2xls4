package com.makechip.stdf2xls4.excel.xls.layout1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.xls.Format_t;

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
	private static final double CELL_DEFAULT_HEIGHT = 17;
	@SuppressWarnings("unused")
	private static final double CELL_DEFAULT_WIDTH = 64;
	private File logoFile;
	private final int row;
    
    /**
     * Constructor for the LogoBlock.
     * @param logoFile Image file containing JPG image.
     * This parameter may be null, in which case a default logo will be supplied.
     */
    public LogoBlock(File logoFile, HeaderBlock hb)
    {
    	this.logoFile = logoFile;
    	row = hb.getHeight();
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
    		BufferedImage input = ImageIO.read(logoFile);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		ImageIO.write(input, "PNG", baos);
    		double rowHeight = 6.0 + LegendBlock.LEGEND_ROW_HEIGHT / CELL_DEFAULT_HEIGHT;
    		WritableImage image = new WritableImage(COL, row, 4.0, 6.0 + rowHeight, logoFile);
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
