package com.makechip.stdf2xls4.excel.xls.layout1;

import static com.makechip.stdf2xls4.excel.xls.Format_t.*;

import java.io.IOException;
import java.util.List;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader implements Block
{
	private final int col;
	private final int row;
	private final boolean wrapTestNames;
	private final int precision;
	private final List<TestHeader> hdrs;
	private final boolean pinSuffix;
	
	public DataHeader(HeaderBlock hb, boolean wrapTestNames, boolean pinSuffix, int precision, List<TestHeader> hdrs)
	{
		col = hb.getWidth();
		row = hb.getHeight();
		this.wrapTestNames = wrapTestNames;
		this.precision = precision;
		this.hdrs = hdrs;
		this.pinSuffix = pinSuffix;
	}
	
	@Override
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int c = col;
		for (TestHeader hdr : hdrs)
		{
			if (!wrapTestNames) ws.setColumnView(c, getCellWidth(hdr.getTestName()));
			else ws.setColumnView(c, 4 + precision);
			ws.mergeCells(c, row, c, row+1);
			if (hdr instanceof ParametricTestHeader)
			{
			    if (pinSuffix)
			    {
			        String tname = hdr.getTestName(); 	
			        int li = tname.lastIndexOf('_');
			        if (li > 0)
			        {
			            String testName = tname.substring(0, li);
			            String ppin = tname.substring(li);
			            ws.addCell(new Label(c, row, testName, HEADER1_FMT.getFormat()));             
			            ws.addCell(new Label(c, row+5, ppin, HEADER1_FMT.getFormat()));             
			        }
			        else ws.addCell(new Label(c, row, tname, HEADER1_FMT.getFormat()));
			    }
			    else ws.addCell(new Label(c, row, hdr.getTestName(), HEADER1_FMT.getFormat()));
			}
			else ws.addCell(new Label(c, row, hdr.getTestName(), HEADER1_FMT.getFormat()));
			ws.addCell(new Number(c, row+2, hdr.getTestNumber(), HEADER1_FMT.getFormat()));
			ws.mergeCells(col, row+6, col, row+7);
			if (hdr instanceof ParametricTestHeader)
			{
			    ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
			    if (phdr.noLoLimit) ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+3, phdr.loLimit, HEADER5_FMT.getFormat()));
			    if (phdr.noHiLimit) ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+4, phdr.hiLimit, HEADER5_FMT.getFormat()));
				ws.addCell(new Label(c, row+6, phdr.units, HEADER1_FMT.getFormat()));
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
			    MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
			    if (mhdr.noLoLimit) ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+3, mhdr.loLimit, HEADER5_FMT.getFormat()));
			    if (mhdr.noHiLimit) ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+4, mhdr.hiLimit, HEADER5_FMT.getFormat()));
				ws.addCell(new Label(c, row+5, mhdr.pin, HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+6, mhdr.units, HEADER1_FMT.getFormat()));
			}
			c++;
		}
	}
	
	private int getCellWidth(String testName)
	{
		double w = 0.0;
		for (int i=0; i<testName.length(); i++)
		{
			if (Character.isUpperCase(testName.charAt(i))) w += 1.5;
			else w += 1.0;
		}
		return((int) w);
	}

	@Override
	public int getWidth()
	{
		return(hdrs.size());
	}

	@Override
	public int getHeight()
	{
		return(8);
	}
}
