package com.makechip.stdf2xls4.excel.xlsx.layout2;

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.TITLE_FMT;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;

class PageTitleBlock implements Block
{
	public static final int HEIGHT = 7;
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
   
    public PageTitleBlock(String title, int numDevices)
    {
    	this.title = title;
    	this.width = numDevices + CornerBlock.WIDTH;
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	setCell(ws, COL, 0, TITLE_FMT.getFormat(wb), title);
    	ws.addMergedRegion(new CellRangeAddress(0, HEIGHT - 1, COL, COL + width - 1));
    }

	@Override
	public int getWidth()
	{
		return(width);
	}

	@Override
	public int getHeight()
	{
		return(HEIGHT);
	}
}
