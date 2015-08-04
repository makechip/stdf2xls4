package com.makechip.stdf2xls4.excel;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface Spreadsheet
{
    public static final String[] PRECISION = { "0.000", "0.0", "0.00", "0.000", "0.0000", 
    		                                   "0.00000", "0.000000", "0.0000000",
                                               "0.00000000", "0.000000000", "0.0000000000",
                                               "0.00000000000", "0.00000000000" };
	
	public void openWorkbook(File file) throws InvalidFormatException, IOException, BiffException;
	
	public String getCellContents(int page, Coord xy);
	
	public Cell_t getCellType(int page, Coord xy);
	
    public void setCell(int page, Coord xy, String value) throws RowsExceededException, WriteException;	
    
    public void setCell(int page, Coord xy, double value) throws RowsExceededException, WriteException;
    
    public void setCell(int page, Coord xy, Format_t format, String value) throws RowsExceededException, WriteException;
    
    public void setCell(int page, Coord xy, Format_t format, int precision, double value) throws RowsExceededException, WriteException;
    
    public void setCell(int page, Coord xy, Format_t format, long value) throws RowsExceededException, WriteException;

    public void setFormat(int page, Coord xy, Format_t format) throws RowsExceededException, WriteException;
    
    public void setFormat(int page, Coord xy, Format_t format, int precision) throws RowsExceededException, WriteException;
    
    public void createNewPages(int pages);
    
    public void createSheet(int page, SheetName name);
    
    public void setColumnWidth(int page, int col, int widthInChars);
}
