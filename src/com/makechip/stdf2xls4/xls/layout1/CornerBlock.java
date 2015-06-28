package com.makechip.stdf2xls4.xls.layout1;

import static com.makechip.stdf2xls4.xls.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.xls.Format_t.HEADER4_FMT;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CornerBlock 
{
	private final int COL = TitleBlock.COL;
	private final int ROW;
	private boolean wafersort;
	private boolean onePage;
	
	public CornerBlock(boolean wafersort, int headerBlockHeight, boolean onePage)
	{
		this.wafersort = wafersort;
		this.onePage = onePage;
		ROW = headerBlockHeight + LogoBlock.getHeight();
	}
	
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException
	{
		int rsltCol = wafersort ? COL + 4 : COL + 5;
		if (onePage)
		{
			String s = (wafersort) ? "Wafer" : "Step";
			ws.addCell(new Label(rsltCol-3, ROW+5, s, HEADER1_FMT.getFormat()));
		}
		ws.addCell(new Label(rsltCol-2, ROW+5, "HW Bin", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(rsltCol-1, ROW+5, "SW Bin", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(rsltCol, ROW+5, "Rslt", HEADER1_FMT.getFormat()));
		ws.addCell(new Label(rsltCol+1, ROW+5, "Temp", HEADER1_FMT.getFormat()));
		int col = 0;
		if (wafersort)
		{
			ws.addCell(new Label(rsltCol+2, ROW+5, "X", HEADER1_FMT.getFormat()));
			ws.addCell(new Label(rsltCol+3, ROW+5, "Y", HEADER1_FMT.getFormat()));
			col = rsltCol + 3;
		}
		else
		{
			ws.addCell(new Label(rsltCol+2, ROW+5, "S/N", HEADER1_FMT.getFormat()));
			col = rsltCol + 2;
		}
		ws.mergeCells(0, ROW, col, ROW);
		ws.mergeCells(0, ROW+1, col, ROW+1);
		ws.mergeCells(0, ROW+2, col, ROW+2);
		ws.mergeCells(0, ROW+3, col, ROW+3);
		ws.mergeCells(0, ROW+4, col, ROW+4);
        ws.addCell(new Label(0, ROW, "Test Name", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(0, ROW+1, "Test Num", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(0, ROW+2, "Lo Limit", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(0, ROW+3, "Hi Limit", HEADER4_FMT.getFormat()));
        ws.addCell(new Label(0, ROW+4, "Units", HEADER4_FMT.getFormat()));
        ws.setRowView(ROW, 2000);
	}

	public int getHeight()
	{
		return(6);
	}
	
	public int getWidth()
	{
		return(8);
	}
}
