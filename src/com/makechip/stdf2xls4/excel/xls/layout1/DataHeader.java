package com.makechip.stdf2xls4.excel.xls.layout1;

import static com.makechip.stdf2xls4.excel.xls.layout1.Format_t.*;

import java.io.IOException;
import java.util.List;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.makechip.stdf2xls4.excel.xls.Block;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader implements Block
{
	private final int col;
	private final int row;
	private final boolean noWrapTestNames;
	private final int precision;
	private final List<TestHeader> hdrs;
	
	public DataHeader(HeaderBlock hb, boolean noWrapTestNames, int precision, List<TestHeader> hdrs)
	{
		col = hb.getWidth();
		row = hb.getHeight();
		this.noWrapTestNames = noWrapTestNames;
		this.precision = precision;
		this.hdrs = hdrs;
	}
	
	@Override
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int c = col;
		for (TestHeader hdr : hdrs)
		{
			if (noWrapTestNames) 
			{
				ws.setColumnView(c, getCellWidth(hdr.testName));
			    ws.addCell(new Label(c, row, hdr.testName, TEST_NAME_FMT.getFormat()));
			}
			else 
			{
				ws.setColumnView(c, 8 + precision);
			    ws.addCell(new Label(c, row, hdr.testName, TEST_NAME_FMT_WRAP.getFormat()));
			}
			ws.addCell(new Number(c, row+1, hdr.testNumber, HEADER1_FMT.getFormat()));
			ws.addCell(new Number(c, row+2, hdr.dupNum, HEADER1_FMT.getFormat()));
			ws.mergeCells(c, row+6, c, row+7);
			if (hdr instanceof ParametricTestHeader)
			{
			    ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
			    if (phdr.loLimit == null) ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+3, phdr.loLimit, HEADER5_FMT.getFormat(precision)));
			    if (phdr.hiLimit == null) ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+4, phdr.hiLimit, HEADER5_FMT.getFormat(precision)));
				ws.addCell(new Label(c, row+5, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+6, phdr.units, HEADER1_FMT.getFormat()));
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
			    MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
			    if (mhdr.loLimit == null) ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+3, mhdr.loLimit, HEADER5_FMT.getFormat(precision)));
			    if (mhdr.hiLimit == null) ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c, row+4, mhdr.hiLimit, HEADER5_FMT.getFormat(precision)));
				ws.addCell(new Label(c, row+5, mhdr.pin, HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+6, mhdr.units, HEADER1_FMT.getFormat()));
			}
			else
			{
				ws.addCell(new Label(c, row+3, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+4, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+5, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c, row+6, "", HEADER1_FMT.getFormat()));
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
