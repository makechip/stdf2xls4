package com.makechip.stdf2xls4.excel.xlsx;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Cell_t;
import com.makechip.stdf2xls4.excel.Color_t;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Font_t;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.Format_t;

import gnu.trove.map.hash.TIntObjectHashMap;

public class PoiSpreadsheet implements Spreadsheet
{
	private XSSFWorkbook wb;
	private XSSFSheet[] ws;
    private DataFormat df;
	private Map<SheetName, XSSFSheet> sheetMap;
	private static Map<Format_t, TIntObjectHashMap<XSSFCellStyle>> map = new EnumMap<>(Format_t.class);
    private static Map<Font_t, TIntObjectHashMap<Map<Color_t, XSSFFont>>> cache = new EnumMap<>(Font_t.class);
    private static Map<Color_t, XSSFColor> colorMap = new EnumMap<>(Color_t.class);

	public PoiSpreadsheet()
	{
	}

	@Override
	public void openWorkbook(File file) throws InvalidFormatException, IOException
	{
		sheetMap = new IdentityHashMap<>();
		if (file.exists()) wb = new XSSFWorkbook(file);
		else wb = new XSSFWorkbook();
		Iterator<XSSFSheet> it = wb.iterator();
		while (it.hasNext())
		{
			XSSFSheet sh = it.next();
			String s = sh.getSheetName();
			SheetName sn = SheetName.getSheet(s);
			sheetMap.put(sn, sh);
		}
		df = wb.createDataFormat();
	}
	
	private Cell getCell(int page, Coord xy, Cell_t type)
	{
		Row r = ws[page].getRow(xy.r);
		if (r == null) ws[page].createRow(xy.r);
		Cell c = r.getCell(xy.c);
		if (c == null) c = r.createCell(xy.c, type.poiType);
		return(c);
	}

	@Override
	public String getCellContents(int page, Coord xy)
	{
		Row r = ws[page].getRow(xy.r);
		if (r == null) return(null);
		Cell cell = r.getCell(xy.c);
		if (cell == null) return(null);
		return(cell.getStringCellValue());
	}

	@Override
	public Cell_t getCellType(int page, Coord xy)
	{
		Row r = ws[page].getRow(xy.r);
		if (r == null) return(Cell_t.BLANK);
		Cell cell = r.getCell(xy.c);
		if (cell == null) return(Cell_t.BLANK);
		int cellType = cell.getCellType();
		return(Cell_t.getCellType(cellType));
	}

	@Override
	public void setCell(int page, Coord xy, String value)
	{
		Cell c = getCell(page, xy, Cell_t.STRING);
		c.setCellValue(value);
	}

	@Override
	public void setCell(int page, Coord xy, double value)
	{
		Cell c = getCell(page, xy, Cell_t.NUMERIC);
		c.setCellValue(value);
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, String value)
	{
		Cell c = getCell(page, xy, Cell_t.STRING);
		c.setCellValue(value);
		c.setCellStyle(getFormat(format));
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, int precision, double value)
	{
		Cell c = getCell(page, xy, Cell_t.NUMERIC);
		c.setCellValue(value);
		c.setCellStyle(getFormat(format, precision));
	}

	@Override
	public void setCell(int page, Coord xy, Format_t format, long value)
	{
		Cell c = getCell(page, xy, Cell_t.STRING);
		c.setCellValue(value);
		c.setCellStyle(getFormat(format));
	}

	private Cell getCell(int page, Coord xy)
	{
		Row r = ws[page].getRow(xy.r);
		if (r == null) ws[page].createRow(xy.r);
		Cell c = r.getCell(xy.c);
		return(c);
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format)
	{
		Cell c = getCell(page, xy);
		c.setCellStyle(getFormat(format));
	}

	@Override
	public void setFormat(int page, Coord xy, Format_t format, int precision)
	{
		Cell c = getCell(page, xy);
		c.setCellStyle(getFormat(format, precision));
	}

	@Override
	public void createNewPages(int pages)
	{
		ws = new XSSFSheet[pages];
	}

	@Override
	public void createSheet(int page, SheetName name)
	{
		ws[page] = wb.createSheet(name.toString());
		sheetMap.put(name, ws[page]);
	}

	@Override
	public void setColumnWidth(int page, int col, int widthInChars)
	{
	    ws[page].setColumnWidth(col, 256 * widthInChars);
	}

    private CellStyle getFormat(Format_t fmt)
    {
        TIntObjectHashMap<XSSFCellStyle> m = map.get(fmt);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(fmt, m);
        }
        XSSFCellStyle f = m.get(3);
        if (f == null)
        {
        	f = wb.createCellStyle();
            f.setFont(getFont(fmt.getFont(), fmt.getFontSize(), fmt.getFgColor()));
        	f.setAlignment((short) fmt.getHAlign().getValue());
        	f.setVerticalAlignment((short) fmt.getVAlign().getValue());
        	XSSFColor bg = new XSSFColor();
        	bg.setIndexed(fmt.getBgColor().index);
        	f.setFillForegroundColor(bg);
        	f.setFillPattern(CellStyle.SOLID_FOREGROUND);
        	f.setFillBackgroundColor(bg);
        	f.setBorderBottom((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderTop((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderLeft((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderRight((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	XSSFColor bc = new XSSFColor();
        	bc.setIndexed(fmt.getBorderColor().index);
            f.setBottomBorderColor(bc);
            f.setTopBorderColor(bc);
            f.setLeftBorderColor(bc);
            f.setRightBorderColor(bc);
            m.put(3, f);
        }
        return(f);
    }
    
    private CellStyle getFormat(Format_t fmt, int precision)
    {
        TIntObjectHashMap<XSSFCellStyle> m = map.get(fmt);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(fmt, m);
        }
        XSSFCellStyle f = m.get(3);
        if (f == null)
        {
        	f = wb.createCellStyle();
            f.setFont(getFont(fmt.getFont(), fmt.getFontSize(), fmt.getFgColor()));
        	f.setAlignment((short) fmt.getHAlign().getValue());
        	f.setVerticalAlignment((short) fmt.getVAlign().getValue());
        	XSSFColor bg = new XSSFColor();
        	bg.setIndexed(fmt.getBgColor().index);
        	f.setFillForegroundColor(bg);
        	f.setFillPattern(CellStyle.SOLID_FOREGROUND);
        	f.setFillBackgroundColor(bg);
        	f.setBorderBottom((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderTop((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderLeft((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	f.setBorderRight((short) fmt.getBorderStyle().getBorderStyle().getValue());
        	XSSFColor bc = new XSSFColor();
        	bc.setIndexed(fmt.getBorderColor().index);
            f.setBottomBorderColor(bc);
            f.setTopBorderColor(bc);
            f.setLeftBorderColor(bc);
            f.setRightBorderColor(bc);
            f.setDataFormat(df.getFormat(PRECISION[precision]));
            m.put(3, f);
        }
        return(f);
    }
    

    @SuppressWarnings("unused")
	private XSSFFont getFont(Font_t font, int size)
    {
    	TIntObjectHashMap<Map<Color_t, XSSFFont>> m1 = cache.get(font);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(font, m1);
    	}
    	Map<Color_t, XSSFFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	XSSFFont f = m2.get(Color_t.BLACK);
    	if (f == null)
    	{
    	    XSSFColor c = getColor(Color_t.BLACK);
    		f = wb.createFont();
    		f.setBoldweight((short) font.getStyle().value);
            f.setColor(c);
            f.setFontHeight(size);
            f.setFontName(font.getFontName().toString());
            f.setItalic(font.isItalic());
            f.setUnderline((byte) font.getUtype().getValue());
    		m2.put(Color_t.BLACK, f);
    	}
        return(f);
    }
    
    private XSSFFont getFont(Font_t font, int size, Color_t color)
    {
    	TIntObjectHashMap<Map<Color_t, XSSFFont>> m1 = cache.get(font);
    	if (m1 == null)
    	{
    		m1 = new TIntObjectHashMap<>();
    		cache.put(font, m1);
    	}
    	Map<Color_t, XSSFFont> m2 = m1.get(size);
    	if (m2 == null)
    	{
    		m2 = new EnumMap<>(Color_t.class);
    		m1.put(size, m2);
    	}
    	XSSFFont f = m2.get(color);
    	if (f == null)
    	{
    		f = wb.createFont();
    		f.setBoldweight((short) font.getStyle().value);
    		XSSFColor c = getColor(color);
            f.setColor(c);
            f.setFontHeight(size);
            f.setFontName(font.getFontName().toString());
            f.setItalic(font.isItalic());
            f.setUnderline((byte) font.getUtype().getValue());
            m2.put(color, f);
    	}
        return(f);
    	
    }
    
    private XSSFColor getColor(Color_t color)
    {
    	 XSSFColor c = colorMap.get(color);
    	 if (c == null)
    	 {
    		 c = new XSSFColor();
    		 c.setIndexed(color.index);
    		 colorMap.put(color, c);
    	 }
    	 return(c);
    }

}
