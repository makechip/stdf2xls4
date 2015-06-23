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
package com.makechip.stdf2xls4.xls.layout2;

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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import com.makechip.util.Log;

class LogoBlock
{
    private static final String LOGO_FILE = "mc_logo.png";
    private static final double IMAGE_WIDTH = 4.99;
    private static final double IMAGE_HEIGHT = TitleBlock.HEIGHT - 0.04;
    private static byte[] image;
    
    static
    {
        try 
        {
        	InputStream i = LogoBlock.class.getResourceAsStream(LOGO_FILE);
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
        }
        catch (IOException e) { Log.fatal(e); }
    }
    
    
    public static void addBlock(Workbook wb, Sheet ws)
    {
        int row = TitleBlock.ROW;
        int col = TitleBlock.COL;
        ws.addMergedRegion(new CellRangeAddress(row, row+getHeight()-1, col, col + getWidth()-1));
        //Adds a picture to the workbook
        int pictureIdx = wb.addPicture(image, Workbook.PICTURE_TYPE_PNG);
        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = wb.getCreationHelper();
        //Creates the top-level drawing patriarch.
        Drawing drawing = ws.createDrawingPatriarch();
        //Create an anchor that is attached to the worksheet
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner for the image
        anchor.setCol1(row);
        anchor.setRow1(col);
        //Creates a picture
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //Reset the image to the original size
        pict.resize();
    }
    
    public static int getWidth()
    {
        return((int) Math.ceil(IMAGE_WIDTH));
    }
    
    public static int getHeight()
    {
        return((int) Math.ceil(IMAGE_HEIGHT));
    }

}
