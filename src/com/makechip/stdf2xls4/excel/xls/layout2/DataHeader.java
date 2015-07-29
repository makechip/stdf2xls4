package com.makechip.stdf2xls4.excel.xls.layout2;

import static com.makechip.stdf2xls4.excel.xls.layout2.Format_t.*;

import java.io.IOException;
import java.util.List;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.makechip.stdf2xls4.excel.xls.Block;
import com.makechip.stdf2xls4.excel.xls.layout2.PageTitleBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.CornerBlock;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader implements Block
{
	private final int col;
	private final int row;
	private final int precision;
	private final List<TestHeader> hdrs;
	private double maxWidth;
	
	public DataHeader(CornerBlock cb, int precision, List<TestHeader> hdrs)
	{
		col = HeaderBlock.WIDTH;
		row = PageTitleBlock.HEIGHT + cb.getHeight();
		maxWidth = 0;
		this.precision = precision;
		this.hdrs = hdrs;
	}
	
	@Override
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
	{
		int c = col;
		int r = row;
		for (TestHeader hdr : hdrs)
		{
			ws.setColumnView(c, getCellWidth(hdr.testName));
			ws.addCell(new Label(c, r, hdr.testName, TEST_NAME_FMT.getFormat()));
			ws.addCell(new Number(c + 1, r, hdr.testNumber, HEADER1_FMT.getFormat()));
			ws.addCell(new Number(c + 2, r, hdr.dupNum, HEADER1_FMT.getFormat()));
			ws.mergeCells(c + 6, r, c + 7, r);
			if (hdr instanceof ParametricTestHeader)
			{
			    ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
			    if (phdr.loLimit == null) ws.addCell(new Label(c + 3, r, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c + 3, r, phdr.loLimit, HEADER5_FMT.getFormat(precision)));
			    if (phdr.hiLimit == null) ws.addCell(new Label(c + 4, r, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c + 4, r, phdr.hiLimit, HEADER5_FMT.getFormat(precision)));
				ws.addCell(new Label(c + 5, r, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c + 6, r, phdr.units, HEADER1_FMT.getFormat()));
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
			    MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
			    if (mhdr.loLimit == null) ws.addCell(new Label(c + 3, r, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c + 3, r, mhdr.loLimit, HEADER5_FMT.getFormat(precision)));
			    if (mhdr.hiLimit == null) ws.addCell(new Label(c + 4, r, "", HEADER1_FMT.getFormat()));
			    else ws.addCell(new Number(c + 4, r, mhdr.hiLimit, HEADER5_FMT.getFormat(precision)));
				ws.addCell(new Label(c + 5, r, mhdr.pin, HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c + 6, r, mhdr.units, HEADER1_FMT.getFormat()));
			}
			else
			{
				ws.addCell(new Label(c + 3, r, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c + 4, r, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c + 5, r, "", HEADER1_FMT.getFormat()));
				ws.addCell(new Label(c + 6, r, "", HEADER1_FMT.getFormat()));
			}
			r++;
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
		if (w > maxWidth) maxWidth = w;
		return((int) maxWidth);
	}

	@Override
	public int getWidth()
	{
		return(8);
	}

	@Override
	public int getHeight()
	{
		return(hdrs.size());
	}
}
