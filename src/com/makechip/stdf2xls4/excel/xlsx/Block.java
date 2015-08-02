package com.makechip.stdf2xls4.excel.xlsx;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Cell_t;
import com.makechip.stdf2xls4.excel.Coord;

public interface Block
{
	public int getWidth();
	public int getHeight();
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws);
	public void addFormat(XSSFWorkbook wb, XSSFSheet ws);

	public static void setCell(Row r, int col, CellStyle cs, String val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	public static void setCell(XSSFSheet ws, int col, int row, CellStyle cs, String val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	public static void setCell(Row r, int col, CellStyle cs, long val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	public static void setCell(XSSFSheet ws, int col, int row, CellStyle cs, long val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	public static void setCell(Row r, int col, CellStyle cs, int val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	public static void setCell(XSSFSheet ws, int col, int row, CellStyle cs, int val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	public static void setCell(Row r, int col, CellStyle cs, float val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	public static void setCell(XSSFSheet ws, int col, int row, CellStyle cs, float val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	public static void setCell(Row r, int col, CellStyle cs, double val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	public static void setCell(XSSFSheet ws, int col, int row, double val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, val);
	}

	public static void setCell(Row r, int col, String val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
		}
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, String val)
	{
		Row r = ws.getRow(cell.r);
		if (r == null) r = ws.createRow(cell.r);
		setCell(r, cell.c, val);
	}

	public static void setCell(Row r, int col, long val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
		}
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, long val)
	{
		Row r = ws.getRow(cell.r);
		if (r == null) r = ws.createRow(cell.r);
		setCell(r, cell.c, val);
	}

	public static void setCell(Row r, int col, int val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
		}
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, int val)
	{
		Row r = ws.getRow(cell.r);
		if (r == null) r = ws.createRow(cell.r);
		setCell(r, cell.c, val);
	}

	public static void setCell(Row r, int col, float val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
		}
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, float val)
	{
		Row r = ws.getRow(cell.r);
		if (r == null) r = ws.createRow(cell.r);
		setCell(r, cell.c, val);
	}

	public static void setCell(Row r, int col, double val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.NUMERIC.type);
			c.setCellValue(val);
		}
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, double val)
	{
		Row r = ws.getRow(cell.r);
		if (r == null) r = ws.createRow(cell.r);
		setCell(r, cell.c, val);
	}
	
	public static void setCell(XSSFSheet ws, Coord cell, CellStyle cs)
	{
		Row r = ws.getRow(cell.r);
		Cell c = r.getCell(cell.c);
		c.setCellStyle(cs);
	}
	
	public static void setCell(Row r, int col, CellStyle cs)
	{
		Cell c = r.getCell(col);
		c.setCellStyle(cs);
	}
}
