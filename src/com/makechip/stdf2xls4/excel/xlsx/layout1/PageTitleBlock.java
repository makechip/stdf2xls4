package com.makechip.stdf2xls4.excel.xlsx.layout1;

import static com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t.TITLE_FMT;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;

class PageTitleBlock implements Block
{
    private static final int COL = HeaderBlock.WIDTH;
    private final String title;
    private final int width;
    private final int height;
   
    public PageTitleBlock(String title, int testWidth, HeaderBlock hb)
    {
    	this.title = title;
    	this.width = testWidth;
    	height = hb.getHeight();
    }
    
    @Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
    	setCell(ws, COL, 0, TITLE_FMT.getFormat(wb), title);
    	ws.addMergedRegion(new CellRangeAddress(0, height -1, COL, COL + width - 1));
    }

	@Override
	public int getWidth()
	{
		return(width);
	}

	@Override
	public int getHeight()
	{
		return(height);
	}
}
