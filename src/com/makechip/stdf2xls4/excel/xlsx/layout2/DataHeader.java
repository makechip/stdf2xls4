package com.makechip.stdf2xls4.excel.xlsx.layout2;

import static com.makechip.stdf2xls4.excel.xlsx.layout2.Format_t.*;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout2.PageTitleBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.CornerBlock;
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
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		int c = col;
		int r = row;
		for (TestHeader hdr : hdrs)
		{
		    Row rw = ws.getRow(r);
		    if (rw == null) rw = ws.createRow(r);
			ws.setColumnWidth(c, getCellWidth(hdr.testName));
			setCell(rw, c, TEST_NAME_FMT.getFormat(wb), hdr.testName);
		    setCell(rw, c+1, HEADER1_FMT.getFormat(wb), hdr.testNumber);	
		    setCell(rw, c+2,HEADER1_FMT.getFormat(wb), hdr.dupNum);
			if (hdr instanceof ParametricTestHeader)
			{
			    ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
			    if (phdr.loLimit == null) setCell(rw, c+3, HEADER1_FMT.getFormat(wb), "");
			    else setCell(rw, c+3, HEADER5_FMT.getFormat(wb, precision), phdr.loLimit);
			    if (phdr.hiLimit == null) setCell(rw, c+4, HEADER1_FMT.getFormat(wb), "");
			    else setCell(rw, c+4, HEADER5_FMT.getFormat(wb, precision), phdr.hiLimit);
			    setCell(rw, c+5, HEADER1_FMT.getFormat(wb), "");
			    setCell(rw, c+6, HEADER1_FMT.getFormat(wb), phdr.units);
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
			    MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
			    if (mhdr.loLimit == null) setCell(rw, c+3, HEADER1_FMT.getFormat(wb), "");
			    else setCell(rw, c+3, HEADER5_FMT.getFormat(wb, precision), mhdr.loLimit);
			    if (mhdr.hiLimit == null) setCell(rw, c+4, HEADER1_FMT.getFormat(wb), "");
			    else setCell(rw, c+4, HEADER5_FMT.getFormat(wb, precision), mhdr.hiLimit);
			    setCell(rw, c+5, HEADER1_FMT.getFormat(wb), "");
			    setCell(rw, c+6, HEADER1_FMT.getFormat(wb), mhdr.units);
			}
			else
			{
				setCell(rw, c + 3, HEADER1_FMT.getFormat(wb), "");
				setCell(rw, c + 4, HEADER1_FMT.getFormat(wb), "");
				setCell(rw, c + 5, HEADER1_FMT.getFormat(wb), "");
				setCell(rw, c + 6, HEADER1_FMT.getFormat(wb), "");
			}
		    ws.addMergedRegion(new CellRangeAddress(r, r, c + 6, c + 7));
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
		w *= 256.0;
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
