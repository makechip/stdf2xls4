package com.makechip.stdf2xls4.xls.layout1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.makechip.util.Log;

import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

class LogoBlock
{
    private static final String LOGO_FILE = "sts_logo.png";
    private static final double IMAGE_WIDTH = 4.99;
    private static final double IMAGE_HEIGHT = (double) TitleBlock.HEIGHT - 0.04;
    private static WritableImage image;
    
    static
    {
        File tmpFile = null;
        try (InputStream is = LogoBlock.class.getResourceAsStream(LOGO_FILE))
        {
            tmpFile = File.createTempFile("mc_", ".png");
            tmpFile.deleteOnExit();
            try (FileOutputStream fo = new FileOutputStream(tmpFile))
            {
                int c = 0;
                while ((c = is.read()) >= 0) fo.write(c);
            }
            catch (IOException e1) { Log.fatal(e1); }
            
        }
        catch (IOException e2) { Log.fatal(e2); }
        image = new WritableImage((double) TitleBlock.COL, (double) TitleBlock.ROW, IMAGE_WIDTH, IMAGE_HEIGHT, tmpFile);
    }
    
    
    public static void addBlock(WritableSheet ws) throws WriteException
    {
        int row = TitleBlock.ROW;
        int col = TitleBlock.COL;
        ws.mergeCells(col, row, col + getWidth() - 1, row + getHeight() - 1);
        ws.addImage(image);
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
