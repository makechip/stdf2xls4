package com.makechip.stdf2xls4.excel.xlsx;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface CellFormat
{

	public CellStyle getFormat(XSSFWorkbook wb);
	public CellStyle getFormat(XSSFWorkbook wb, int precision);
}
