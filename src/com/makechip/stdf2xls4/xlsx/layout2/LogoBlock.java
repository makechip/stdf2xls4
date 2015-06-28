/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4.xlsx.layout2;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import com.makechip.util.Log;

class LogoBlock
{
    private static final String LOGO_FILE = "mc_logo.png";
    private static byte[] image;
    public static final int WIDTH = 5;
    
    static
    {
        try 
        {
        	InputStream i = LogoBlock.class.getResourceAsStream(LOGO_FILE);
        	BufferedImage img = ImageIO.read(i);
        	float w = img.getWidth();
        	float h = img.getHeight();
        	AffineTransform at = new AffineTransform();
        	at.scale(316.0f/w, 165.0f/h);
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
        }
        catch (IOException e) { Log.fatal(e); }
    }
    
    
    public static void addBlock(Workbook wb, Sheet ws)
    {
    	for (int i=0; i<TitleBlock.HEIGHT; i++)
    	{
    		Row r = ws.getRow(i);
    		if (r == null)
    		{
    			r = ws.createRow(i);
    			r.setHeight((short) -1);
    		}
    	}
        ws.addMergedRegion(new CellRangeAddress(0, TitleBlock.HEIGHT-1, 0, 4));
        //Adds a picture to the workbook
        int pictureIdx = wb.addPicture(image, Workbook.PICTURE_TYPE_PNG);
        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = wb.getCreationHelper();
        //Creates the top-level drawing patriarch.
        Drawing drawing = ws.createDrawingPatriarch();
        //Create an anchor that is attached to the worksheet
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner for the image
        anchor.setCol1(0);
        anchor.setRow1(0);
        //Creates a picture
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //Reset the image to the original size
        pict.resize();
    }
    
    public static int getWidth()
    {
        return(5);
    }
    
    //public static int getHeight()
   // {
   //     return((int) Math.ceil(IMAGE_HEIGHT));
    //}

}
