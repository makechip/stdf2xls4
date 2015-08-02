package com.makechip.stdf2xls4.excel.xlsx;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.CellFormat;
import com.makechip.stdf2xls4.excel.Cell_t;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.Spreadsheet;

public class PoiSpreadsheet implements Spreadsheet
{
	private XSSFWorkbook wb;
	private XSSFSheet[] ws;
	private Map<SheetName, XSSFSheet> sheetMap;

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
	public void setCell(int page, Coord xy, CellFormat format, String value)
	{
		Cell c = getCell(page, xy, Cell_t.STRING);
		c.setCellValue(value);
		c.setCellStyle(format.getFormat(wb));
	}

	@Override
	public void setCell(int page, Coord xy, CellFormat format, int precision, double value)
	{
		Cell c = getCell(page, xy, Cell_t.NUMERIC);
		c.setCellValue(value);
		c.setCellStyle(format.getFormat(wb, precision));
	}

	@Override
	public void setCell(int page, Coord xy, CellFormat format, long value)
	{
		Cell c = getCell(page, xy, Cell_t.STRING);
		c.setCellValue(value);
		c.setCellStyle(format.getFormat(wb));
	}

	private Cell getCell(int page, Coord xy)
	{
		Row r = ws[page].getRow(xy.r);
		if (r == null) ws[page].createRow(xy.r);
		Cell c = r.getCell(xy.c);
		return(c);
	}

	@Override
	public void setFormat(int page, Coord xy, CellFormat format)
	{
		Cell c = getCell(page, xy);
		c.setCellStyle(format.getFormat(wb));
	}

	@Override
	public void setFormat(int page, Coord xy, CellFormat format, int precision)
	{
		Cell c = getCell(page, xy);
		c.setCellStyle(format.getFormat(wb, precision));
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

}
