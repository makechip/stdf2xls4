package com.makechip.stdf2xls4.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

public interface CellFormat
{

	public CellStyle getFormat(XSSFWorkbook wb);
	public CellStyle getFormat(XSSFWorkbook wb, int precision);
    public WritableCellFormat getFormat() throws WriteException;
    public WritableCellFormat getFormat(int precision) throws WriteException;
}
