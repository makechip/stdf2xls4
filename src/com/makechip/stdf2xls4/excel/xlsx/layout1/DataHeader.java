package com.makechip.stdf2xls4.excel.xlsx.layout1;

import static com.makechip.stdf2xls4.excel.xlsx.layout1.Format_t.*;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;
import com.makechip.util.Log;

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
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		CellStyle cst = TEST_NAME_FMT.getFormat(wb);
		CellStyle cstw = TEST_NAME_FMT_WRAP.getFormat(wb);
		cstw.setWrapText(true);
		CellStyle cs1 = HEADER1_FMT.getFormat(wb);
		CellStyle cs5 = HEADER5_FMT.getFormat(wb, precision);
		int c = col;
		for (TestHeader hdr : hdrs)
		{
			if (noWrapTestNames) 
			{
				ws.setColumnWidth(c, getCellWidth(hdr.testName));
				setCell(ws, c, row, cst, hdr.testName);
				Log.msg("NOT wrapping text");
			}
			else 
			{
				ws.setColumnWidth(c, 256 * (8 + precision));
				setCell(ws, c, row, cstw, hdr.testName);
				cstw.setWrapText(true);
				Log.msg("wrapping text");
			}
			setCell(ws, c, row+1, cs1, hdr.testNumber);
			setCell(ws, c, row+2, cs1, hdr.dupNum);
			
			int mc = c;
			if (hdr instanceof ParametricTestHeader)
			{
			    ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
			    if (phdr.loLimit == null) setCell(ws, c, row+3, cs1, "");
			    else setCell(ws, c, row+3, cs5, phdr.loLimit);
			    if (phdr.hiLimit == null) setCell(ws, c, row+4, cs1, "");
			    else setCell(ws, c, row+4, cs5, phdr.hiLimit);
			    setCell(ws, c, row+5, cs1, "");
			    setCell(ws, c, row+6, cs1, phdr.units);
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
			    MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
			    if (mhdr.loLimit == null) setCell(ws, c, row+3, cs1, "");
			    else setCell(ws, c, row+3, cs5, mhdr.loLimit);
			    if (mhdr.hiLimit == null) setCell(ws, c, row+4, cs1, "");
			    else setCell(ws, c, row+4, cs5, mhdr.hiLimit);
			    setCell(ws, c, row+5, cs1, mhdr.pin);
			    setCell(ws, c, row+6, cs1, mhdr.units);
			}
			else
			{
				setCell(ws, c, row+3, cs1, "");
				setCell(ws, c, row+4, cs1, "");
				setCell(ws, c, row+5, cs1, "");
				setCell(ws, c, row+6, cs1, "");
			}
			ws.addMergedRegion(new CellRangeAddress(row+6, row+7, mc, mc));
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
		w *= 256.0;
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
