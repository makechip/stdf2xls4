package com.makechip.stdf2xls4.excel;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public interface Spreadsheet
{
	
	public void openWorkbook(File file) throws InvalidFormatException, IOException;
	
	public String getCellContents(int page, Coord xy);
	
	public Cell_t getCellType(int page, Coord xy);
	
    public void setCell(int page, Coord xy, String value);	
    
    public void setCell(int page, Coord xy, double value);
    
    public void setCell(int page, Coord xy, CellFormat format, String value);
    
    public void setCell(int page, Coord xy, CellFormat format, int precision, double value);
    
    public void setCell(int page, Coord xy, CellFormat format, long value);

    public void setFormat(int page, Coord xy, CellFormat format);
    
    public void setFormat(int page, Coord xy, CellFormat format, int precision);
    
    public void createNewPages(int pages);
    
    public void createSheet(int page, SheetName name);
}
