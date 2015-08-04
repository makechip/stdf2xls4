package com.makechip.stdf2xls4.excel.xls;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.makechip.stdf2xls4.excel.Cell_t;
import com.makechip.stdf2xls4.excel.Color_t;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Font_t;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.Format_t;

import gnu.trove.map.hash.TIntObjectHashMap;
import jxl.Cell;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;

public class JxlSpreadsheet implements Spreadsheet
{
    private static Map<Font_t, TIntObjectHashMap<Map<Color_t, WritableFont>>> cache = new EnumMap<>(Font_t.class);
    private static Map<Format_t, TIntObjectHashMap<WritableCellFormat>> map = new EnumMap<>(Format_t.class);
    private Map<SheetName, WritableSheet> sheetMap;
    private WritableWorkbook wb;
    private WritableSheet[] ws;

	public JxlSpreadsheet()
	{
	}

	@Override
	public void openWorkbook(File file) throws InvalidFormatException, IOException, BiffException
	{
		sheetMap = new IdentityHashMap<>();
	    if (file.exists())
	    {
	        Workbook w = Workbook.getWorkbook(file);    	
	        wb = Workbook.createWorkbook(file, w);
	        Arrays.stream(wb.getSheetNames()).forEach(s ->
	        {
	        	SheetName sn = SheetName.getSheet(s);
       			if (!s.equals(sn.toString())) 
       			{
       				throw new RuntimeException("Incorrectly formatted sheet name: " + s + " : " + sn);
       			}
       			sheetMap.put(sn, wb.getSheet(s));
	        });
	    }
	    else
	    {
	    	wb = Workbook.createWorkbook(file);
	    }
	    wb.setColourRGB(Colour.SKY_BLUE, 82, 123, 188);
	}

	@Override
	public String getCellContents(int page, Coord xy)
	{
		Cell c = ws[page].getCell(xy.c, xy.r);
		return(c.getContents());
	}

	@Override
	public Cell_t getCellType(int page, Coord xy)
	{
		Cell c = ws[page].getCell(xy.c, xy.r);
		return(Cell_t.getCellType(c.getType()));
	}

	@Override
	public void setCell(int page, Coord xy, String value) throws RowsExceededException, WriteException
	{
	    ws[page].addCell(new Label(xy.c, xy.r, value));	
	}

	@Override
	public void setCell(int page, Coord xy, double value) throws RowsExceededException, WriteException
	{
	    ws[page].addCell(new Number(xy.c, xy.r, value));	
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, String value) throws RowsExceededException, WriteException
	{
	    ws[page].addCell(new Label(xy.c, xy.r, value, getFormat(format)));	
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, int precision, double value) throws RowsExceededException, WriteException
	{
	    ws[page].addCell(new Number(xy.c, xy.r, value, getFormat(format, precision)));	
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, long value) throws RowsExceededException, WriteException
	{
	    ws[page].addCell(new Number(xy.c, xy.r, value, getFormat(format)));	
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format) throws WriteException
	{
		WritableCell c = ws[page].getWritableCell(xy.c, xy.r);
		c.setCellFormat(getFormat(format));
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format, int precision) throws WriteException
	{
		WritableCell c = ws[page].getWritableCell(xy.c, xy.r);
		c.setCellFormat(getFormat(format, precision));
	}

	@Override
	public void createNewPages(int pages)
	{
	    ws = new WritableSheet[pages];	
	}

	@Override
	public void createSheet(int page, SheetName name)
	{
	    ws[page] = wb.createSheet(name.toString(), page);	
	    sheetMap.put(name, ws[page]);
	}

	@Override
	public void setColumnWidth(int page, int col, int widthInChars)
	{
	    ws[page].setColumnView(col, (14*widthInChars)/10);	
	}

    private WritableCellFormat getFormat(Format_t fmt) throws WriteException 
    {
        TIntObjectHashMap<WritableCellFormat> m = map.get(fmt);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(fmt, m);
        }
        WritableCellFormat f = m.get(3);
        if (f == null)
        {
        	WritableFont wf = getFont(fmt.getFont(), fmt.getFontSize(), fmt.getFgColor());
        	f = new WritableCellFormat(wf);
        	f.setAlignment(fmt.getHAlign());
        	f.setBackground(Colour.getInternalColour(fmt.getBgColor().index));
        	f.setBorder(Border.ALL, fmt.getBorderStyle().getBorderStyle(), Colour.getInternalColour(fmt.getBorderColor().index));
        	f.setVerticalAlignment(fmt.getVAlign());
        	f.setWrap(fmt.isWrap());
        }
        return(f);
    }
    
    private WritableCellFormat getFormat(Format_t fmt, int precision) throws WriteException
    {
        TIntObjectHashMap<WritableCellFormat> m = map.get(fmt);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(fmt, m);
        }
        WritableCellFormat f = m.get(precision);
        if (f == null)
        {
        	WritableFont wf = getFont(fmt.getFont(), fmt.getFontSize(), fmt.getFgColor());
        	f = new WritableCellFormat(wf, new NumberFormat(PRECISION[precision]));
        	f.setAlignment(fmt.getHAlign());
        	f.setBackground(Colour.getInternalColour(fmt.getBgColor().index));
        	f.setBorder(Border.ALL, fmt.getBorderStyle().getBorderStyle(), Colour.getInternalColour(fmt.getBorderColor().index));
        	f.setVerticalAlignment(fmt.getVAlign());
        	f.setWrap(fmt.isWrap());
        	m.put(precision, f);
        }
        return(f);
    }

    @SuppressWarnings("unused")
	private WritableFont getFont(Font_t font, int size) throws WriteException
    {
    	TIntObjectHashMap<Map<Color_t, WritableFont>> m1 = cache.get(font);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(font, m1);
    	}
    	Map<Color_t, WritableFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	WritableFont f = m2.get(Color_t.BLACK);
    	if (f == null)
    	{
    		f = new WritableFont(font.getFontName(), size, font.getStyle(), font.isItalic(), font.getUtype());	
            f.setColour(jxl.format.Colour.BLACK);
    		m2.put(Color_t.BLACK, f);
    	}
        return(f);
    }
    
    private WritableFont getFont(Font_t font, int size, Color_t color) throws WriteException
    {
    	TIntObjectHashMap<Map<Color_t, WritableFont>> m1 = cache.get(font);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(font, m1);
    	}
    	Map<Color_t, WritableFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	WritableFont f = m2.get(color);
    	if (f == null)
    	{
            f = new WritableFont(font.getFontName(), size, font.getStyle(), font.isItalic(), font.getUtype());	
            f.setColour(Colour.getInternalColour(color.index));
            m2.put(color, f);
    	}
        return(f);
    	
    }
}
