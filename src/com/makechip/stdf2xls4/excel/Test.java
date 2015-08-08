package com.makechip.stdf2xls4.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test
{
	private XSSFWorkbook wb;
	private XSSFSheet ws;
	private File file;
	
	public static final short[] halign =
		{ CellStyle.ALIGN_LEFT, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_RIGHT };
	
	public static final short[] valign =
		{ CellStyle.VERTICAL_TOP, CellStyle.VERTICAL_CENTER, CellStyle.VERTICAL_BOTTOM };

	public Test() throws InvalidFormatException, IOException
	{
		file = new File("/home/eric/stdf/test.xlsx");
		if (file.exists())
		{
			wb = new XSSFWorkbook(file);
			ws = wb.getSheetAt(0);
			readAlignments();
		}
		else
		{
		    wb = new XSSFWorkbook();
		    ws = wb.createSheet("TEST");
		}
	}
	
	private void readAlignments()
	{
		IntStream.rangeClosed(0, 2).forEach(row ->
		{
			XSSFRow r = ws.getRow(row);
			IntStream.rangeClosed(0, 2).forEach(col ->
			{
			    XSSFCell c = r.getCell(col);
			    XSSFCellStyle cs = c.getCellStyle();
			    HorizontalAlignment ha = cs.getAlignmentEnum();
			    VerticalAlignment va = cs.getVerticalAlignmentEnum();
			    System.out.println("alignment = [" + ha + ", " + va + "]");
			});
		});
	}
	
	public void run()
	{
		XSSFRow[] rows = new XSSFRow[3];
		IntStream.range(0, 3).forEach(i ->
		{
			rows[i] = ws.getRow(i);
			if (rows[i] == null) rows[i] = ws.createRow(i);
			rows[i].setHeight((short) 512);
		});
		
		Arrays.stream(rows).forEach(row ->
		{
			IntStream.range(0, 3).forEach(col->
			{
			    XSSFCell c = row.getCell(col);
			    if (c == null) c = row.createCell(col);
	        	XSSFCellStyle cs = wb.createCellStyle();
	            cs.setFont(getFont(Font_t.ARIAL_NORMAL, 10, Color_t.BLACK));
	        	cs.setAlignment(halign[col]);
	        	cs.setVerticalAlignment(valign[row.getRowNum()]);
	        	c.setCellStyle(cs);
	        	c.setCellValue("A");
			});
		});
	    close(file);	
	}
	
	public static void main(String[] args)
	{
		try
		{
		    Test t = new Test();
		    t.run();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	static XSSFFont f = null;
	
    private Font getFont(Font_t font, int size, Color_t color)
    {
    	if (f == null)
    	{
    		f = wb.createFont();
    		f.setBoldweight((short) font.getStyle().value);
            f.setColor(new XSSFColor(color.awtColor));
            f.setFontHeight((short) (20 * size));
            f.setFontName(font.getFontName().toString());
            f.setItalic(font.isItalic());
            f.setUnderline((byte) font.getUtype().getValue());
    	}
        return(f);
    	
    }
    
	public void close(File file)
	{
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
        }
        catch (Exception e) { throw new RuntimeException(e); }
	}

}
