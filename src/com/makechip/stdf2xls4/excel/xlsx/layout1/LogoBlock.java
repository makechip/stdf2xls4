package com.makechip.stdf2xls4.excel.xlsx.layout1;

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

import com.makechip.stdf2xls4.excel.xlsx.Block;

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
    public LogoBlock(File logoFile, HeaderBlock hb)
    {
    	this.logoFile = logoFile;
    	row = hb.getHeight();
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
   		ws.addMergedRegion(new CellRangeAddress(row, row + HEIGHT - 1, COL, COL + WIDTH - 1));
    	if (logoFile == null)
    	{
    		setCell(ws, COL, row, Format_t.LOGO_FMT.getFormat(wb), "makechip.com");
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
            	anchor.setRow1(row);
            	anchor.setCol2(COL + WIDTH -1);
            	anchor.setRow2(row + HEIGHT -1);
            	//Creates a picture
            	Picture pict = drawing.createPicture(anchor, pictureIdx);
            	pict.resize();
            	/*
                InputStream i = new FileInputStream(logoFile);
                BufferedImage img = ImageIO.read(i);
                float w = img.getWidth();
                float h = img.getHeight();
                AffineTransform at = new AffineTransform();
                at.scale(313.0f/w, 130.0f/h);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                BufferedImage simg = null;
                simg = scaleOp.filter(img, simg);
                File f = File.createTempFile("XXX", "XXX");
                f.deleteOnExit();
                FileOutputStream fo = new FileOutputStream(f);
                ImageIO.write(simg, "PNG", fo);
                fo.close();
                FileInputStream is = new FileInputStream(f);
                image = IOUtils.toByteArray(is);
                is.close();
                */
            }   
            catch (IOException e) { throw new RuntimeException(e.getMessage()); }
    		//ws.addImage(image);
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
