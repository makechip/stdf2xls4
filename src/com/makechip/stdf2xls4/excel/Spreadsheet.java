package com.makechip.stdf2xls4.excel;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jxl.read.biff.BiffException;

public interface Spreadsheet
{
    public static final String[] PRECISION = { "0.000", "0.0", "0.00", "0.000", "0.0000", 
    		                                   "0.00000", "0.000000", "0.0000000",
                                               "0.00000000", "0.000000000", "0.0000000000",
                                               "0.00000000000", "0.00000000000" };
	
	public void openWorkbook(File file) throws InvalidFormatException, IOException, BiffException;
	
	public void clearStyles();
	
	public String getCellContents(int page, Coord xy);
	
	public double getCellValue(int page, Coord xy);
	
	public Cell_t getCellType(int page, Coord xy);
	
    public void setCell(int page, Coord xy, String value);
    
    public void setCell(int page, Coord xy, double value);
    
    public void setCell(int page, Coord xy, Format_t format, String value);
    
    public void setCell(int page, Coord xy, Format_t format, int precision, double value);
    
    public void setCell(int page, Coord xy, Format_t format, long value);

    public void setFormat(int page, Coord xy, Format_t format);
    
    public void setFormat(int page, Coord xy, Format_t format, int precision);
    
    public void createNewPages(int pages);
    
    public Object initSheet(int page, SheetName sname);
    
    public void createSheet(int page, SheetName name);
    
    public void setColumnWidth(int page, int col, int widthInChars);
    
    public void mergeCells(int page, int upperRow, int lowerRow, int leftCol, int rightCol);
    
    public void addImage(int page, File imageFile, Coord ul, Coord lr);
    
    public void close(File file);
    
    public int getRowHeight(int page, int row);
    
    public void setRowHeight(int page, int row, int height);
    
    public int getNumberOfSheets();
    
    public String getSheetName(int page);
}
