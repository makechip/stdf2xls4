package com.makechip.stdf2xls4.excel.xlsx;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface Block
{
	public int getWidth();
	public int getHeight();
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws);

	default void setCell(Row r, int col, CellStyle cs, String val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	default void setCell(XSSFSheet ws, int col, int row, CellStyle cs, String val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	default void setCell(Row r, int col, CellStyle cs, long val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	default void setCell(XSSFSheet ws, int col, int row, CellStyle cs, long val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	default void setCell(Row r, int col, CellStyle cs, int val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	default void setCell(XSSFSheet ws, int col, int row, CellStyle cs, int val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	default void setCell(Row r, int col, CellStyle cs, float val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	default void setCell(XSSFSheet ws, int col, int row, CellStyle cs, float val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

	default void setCell(Row r, int col, CellStyle cs, double val)
	{
		Cell c = r.getCell(col);
		if (c == null)
		{
			c = r.createCell(col, Cell_t.STRING.type);
			c.setCellValue(val);
			c.setCellStyle(cs);
		}
	}
	
	default void setCell(XSSFSheet ws, int col, int row, CellStyle cs, double val)
	{
		Row r = ws.getRow(row);
		if (r == null) r = ws.createRow(row);
		setCell(r, col, cs, val);
	}

}
