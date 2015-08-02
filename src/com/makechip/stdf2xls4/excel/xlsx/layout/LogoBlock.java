package com.makechip.stdf2xls4.excel.xlsx.layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout.Format_t;

class LogoBlock implements Block
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
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
   		ws.addMergedRegion(new CellRangeAddress(ROW, ROW + HEIGHT - 1, COL, COL));
   		ws.setColumnWidth(COL, 28 * 256);
    	if (logoFile == null)
    	{
    		Block.setCell(ws, new Coord(COL, ROW), "makechip.com");
    	}
    	else
    	{
    		try
    		{
    			//FileInputStream obtains input bytes from the image file
    			InputStream inputStream = new FileInputStream(logoFile);
    			//Get the contents of an InputStream as a byte[].
    			byte[] bytes = IOUtils.toByteArray(inputStream);
    			//Adds a picture to the workbook
    			int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
    			//close the input stream
    			inputStream.close();
    			//Returns an object that handles instantiating concrete classes
    			CreationHelper helper = wb.getCreationHelper();
    			//Creates the top-level drawing patriarch.
    			Drawing drawing = ws.createDrawingPatriarch();
    			//Create an anchor that is attached to the worksheet
    			ClientAnchor anchor = helper.createClientAnchor();
    			//set top-left corner for the image
    			anchor.setAnchorType(0);
    			anchor.setCol1(COL);
    			anchor.setRow1(ROW);
    			anchor.setCol2(COL + WIDTH -1);
    			anchor.setRow2(ROW + HEIGHT -1);
    			//Creates a picture
    			Picture pict = drawing.createPicture(anchor, pictureIdx);
    			pict.resize();
    		}
    		catch (IOException e) { throw new RuntimeException(e.getMessage()); }
    	}
    	addFormat(wb, ws);
    }
    
    @Override
    public void addFormat(XSSFWorkbook wb, XSSFSheet ws)
    {
    	if (logoFile == null)
    	{
    		Block.setCell(ws, new Coord(COL, ROW),  Format_t.LOGO_FMT.getFormat(wb));
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
