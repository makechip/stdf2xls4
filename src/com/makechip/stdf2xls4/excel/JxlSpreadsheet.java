package com.makechip.stdf2xls4.excel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import gnu.trove.map.hash.TIntObjectHashMap;
import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
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
	
	public void clearStyles() { map.clear(); }

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
	public double getCellValue(int page, Coord xy)
	{
		Number c = (Number) ws[page].getCell(xy.c, xy.r);
		return(c.getValue());
	}
	
	@Override
	public Cell_t getCellType(int page, Coord xy)
	{
		Cell c = ws[page].getCell(xy.c, xy.r);
		return(Cell_t.getCellType(c.getType()));
	}

	@Override
	public void setCell(int page, Coord xy, String value)
	{
	    try { ws[page].addCell(new Label(xy.c, xy.r, value)); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setCell(int page, Coord xy, double value)
	{
	    try { ws[page].addCell(new Number(xy.c, xy.r, value)); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, String value)
	{
	    try { ws[page].addCell(new Label(xy.c, xy.r, value, getFormat(format))); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, int precision, float value)
	{
	    try { ws[page].addCell(new Number(xy.c, xy.r, value, getFormat(format, precision))); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, long value)
	{
	    try { ws[page].addCell(new Number(xy.c, xy.r, value, getFormat(format))); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format)
	{
		WritableCell c = ws[page].getWritableCell(xy.c, xy.r);
		try { c.setCellFormat(getFormat(format)); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format, int precision)
	{
		WritableCell c = ws[page].getWritableCell(xy.c, xy.r);
		try { c.setCellFormat(getFormat(format, precision)); }
	    catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public void createNewPages(int pages)
	{
	    ws = new WritableSheet[pages];	
	}
	
	@Override
	public Object initSheet(int page, SheetName sname)
	{
		ws[page] = sheetMap.get(sname);
		return(ws[page]);
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
	    ws[page].setColumnView(col, widthInChars);	
	}
	
	@Override
	public void addImage(int page, File imageFile, Coord ul, Coord lr)
	{
		try
		{
		    WritableImage image = new WritableImage(ul.c, ul.r, lr.c - ul.c, (double) (lr.r - ul.r) - 0.1, imageFile);
		    ws[page].addImage(image);
		}
	    catch (Exception e) { throw new RuntimeException(e); }
	}
	
	@Override
	public void mergeCells(int page, int upperRow, int lowerRow, int leftCol, int rightCol)
	{
		try { ws[page].mergeCells(leftCol, upperRow, rightCol, lowerRow); }
	    catch (Exception e) { throw new RuntimeException(e); }
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

	@Override
	public void close(File file)
	{
        if (ws == null) return;
        try 
        {
            wb.write();
            wb.close();
        }
        catch (Exception e) { throw new RuntimeException(e); }
	}

	@Override
	public int getRowHeight(int page, int row)
	{
		CellView cv = ws[page].getRowView(row);
		return(cv.getSize());
	}

	@Override
	public void setRowHeight(int page, int row, int height)
	{
		CellView cv = ws[page].getRowView(row);
        cv.setSize(height);		
	}
	
	public int getNumberOfSheets() { return(wb.getNumberOfSheets()); }
	
	public String getSheetName(int page) { return(ws[page].getName()); }

}
