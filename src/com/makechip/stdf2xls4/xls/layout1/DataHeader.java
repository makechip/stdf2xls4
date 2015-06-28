package com.makechip.stdf2xls4.xls.layout1;

import static com.makechip.stdf2xls4.xls.Format_t.*;

import java.util.List;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import com.makechip.stdf2xls4.stdf.StdfRecord;

@SuppressWarnings("unused")
public class DataHeader 
{
	private int col;
	private int row;
	private boolean wrapTestNames;
	private boolean hiPrecision;
	
	public DataHeader(CornerBlock cb, HeaderBlock hb, boolean wrapTestNames, boolean hiPrecision)
	{
		col = cb.getWidth();
		row = TitleBlock.HEIGHT + hb.getHeight();
		this.wrapTestNames = wrapTestNames;
		this.hiPrecision = hiPrecision;
	}
	
	public void addBlock(WritableSheet ws) throws WriteException
	{
		int c = col;
		/*
		for (ColIdentifier cid : hdrs)
		{
			if (!wrapTestNames) ws.setColumnView(c, cid.getTestName().length());
			else
			{
				if (hiPrecision) ws.setColumnView(c, 13);
				else ws.setColumnView(c, 11);
			}
			ws.addCell(new Label(c, row, cid.getTestName(), HEADER1_FMT.getFormat()));
			ws.addCell(new Number(c, row+1, cid.getTestNumber(), HEADER1_FMT.getFormat()));
			if (cid.getLoLimit() == Record.MISSING_FLOAT)
			{
				ws.addCell(new Label(c, row+2, "", HEADER1_FMT.getFormat()));
			}
			else
			{
				ws.addCell(new Number(c, row+2, cid.getLoLimit(), HEADER5_FMT.getFormat()));
			}
			if (cid.getHiLimit() == Record.MISSING_FLOAT)
			{
				ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
			}
			else
			{
				ws.addCell(new Number(c, row+3, cid.getHiLimit(), HEADER5_FMT.getFormat()));
			}
			if (cid.getUnits() == null || cid.getUnits().equals(""))
			{
				ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
			}
			else
			{
				ws.addCell(new Label(c, row+4, cid.getUnits(), HEADER1_FMT.getFormat()));
			}
			ws.mergeCells(c, row+4, c, row+5);
			c++;
		}
		*/
	}
}
