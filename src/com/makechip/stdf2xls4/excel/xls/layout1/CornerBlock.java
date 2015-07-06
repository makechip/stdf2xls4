package com.makechip.stdf2xls4.excel.xls.layout1;

import static com.makechip.stdf2xls4.excel.xls.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.xls.Format_t.HEADER4_FMT;

import java.io.IOException;

import com.makechip.stdf2xls4.excel.Block;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CornerBlock implements Block
{
	private final boolean wafersort;
	private final boolean onePage;
	private final int startCol;
	private final int devRow;
	private final int testRow;
	private final int width;
	
	public CornerBlock(boolean wafersort, boolean onePage, HeaderBlock hb)
	{
		this.wafersort = wafersort;
		this.onePage = onePage;
		startCol = onePage ? (wafersort ? 1 : 2) : (wafersort ? 2 : 3);
		devRow = hb.getHeight() + 8;
		testRow = hb.getHeight();
		width = 5 + 3 - startCol;
	}
	
	@Override
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int col = startCol;
		int row = devRow;
		if (onePage)
		{
			String s = (wafersort) ? "Wafer" : "Step";
			ws.addCell(new Label(col++, row, s, HEADER1_FMT.getFormat()));
		}
		if (wafersort)
		{
	        ws.addCell(new Label(col++, row, "X", HEADER1_FMT.getFormat()));	
	        ws.addCell(new Label(col++, row, "Y", HEADER1_FMT.getFormat()));	
		}
		else 
		{
			ws.addCell(new Label(col++, row, "S/N", HEADER1_FMT.getFormat()));
		}
		ws.addCell(new Label(col++, row, "HW Bin", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, "SW Bin", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, "Result", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(col++, row, "Temp", HEADER1_FMT.getFormat()));
		
		col = 7;
		row = testRow;
		
		ws.mergeCells(col, row, col, row+2);
        ws.addCell(new Label(col, row, "Test Name", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+3, "Test Num", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+4, "Lo Limit", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+5, "Hi Limit", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(col, row+6, "Units", HEADER4_FMT.getFormat()));
	}
	
	@Override
	public int getHeight() {  return(8); }

	@Override
	public int getWidth() { return(width); }

}
