package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.HEADER5_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.TEST_NAME_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.TEST_NAME_FMT_WRAP;

import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout.CornerBlock;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader implements Block
{
	private final int precision;
	private final boolean rot;
	private final boolean noWrapTestNames;
	private final List<TestHeader> hdrs;
	private double maxWidth;
	private final CornerBlock cb;
	
	public DataHeader(CornerBlock cb, int precision, boolean rot, boolean noWrapTestNames, List<TestHeader> hdrs)
	{
		this.cb = cb;
		maxWidth = 0;
		this.precision = precision;
		this.hdrs = hdrs;
		this.rot = rot;
		this.noWrapTestNames = noWrapTestNames;
	}
	
	@Override
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		CellStyle cst = TEST_NAME_FMT.getFormat(wb);
		CellStyle cstw = TEST_NAME_FMT_WRAP.getFormat(wb);
		cstw.setWrapText(true);
		CellStyle cs1 = HEADER1_FMT.getFormat(wb);
		CellStyle cs5 = HEADER5_FMT.getFormat(wb, precision);
		cb.tstxy.reset();
		hdrs.stream().forEach(hdr ->
		{
			if (rot || noWrapTestNames) ws.setColumnWidth(cb.tstxy.tname.c, getCellWidth(hdr.testName));
			else ws.addMergedRegion(new CellRangeAddress(cb.tstxy.tnameLabel.r, cb.tstxy.tnumLabel.r-1, cb.tstxy.tname.c, cb.tstxy.tname.c));
			Block.setCell(ws, cb.tstxy.tname, noWrapTestNames ? cst : cstw, hdr.testName);
			Block.setCell(ws, cb.tstxy.tnum, cs1, hdr.testNumber);	
			Block.setCell(ws, cb.tstxy.dupNum, cs1, hdr.dupNum);
			if (hdr instanceof ParametricTestHeader)
			{
				ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
				if (phdr.loLimit == null) Block.setCell(ws, cb.tstxy.loLim, cs1, "");
				else Block.setCell(ws, cb.tstxy.loLim, cs5, phdr.loLimit);
				if (phdr.hiLimit == null) Block.setCell(ws, cb.tstxy.hiLim, cs1, "");
				else Block.setCell(ws, cb.tstxy.hiLim, cs5, phdr.hiLimit);
				Block.setCell(ws, cb.tstxy.pin, cs1, "");
				Block.setCell(ws, cb.tstxy.units, cs1, phdr.units);
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
				MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
				if (mhdr.loLimit == null) Block.setCell(ws, cb.tstxy.loLim, cs1, "");
				else Block.setCell(ws, cb.tstxy.loLim, cs5, mhdr.loLimit);
				if (mhdr.hiLimit == null) Block.setCell(ws, cb.tstxy.hiLim, cs1, "");
				else Block.setCell(ws, cb.tstxy.hiLim, cs5, mhdr.hiLimit);
				Block.setCell(ws, cb.tstxy.pin, cs1, mhdr.pin);
				Block.setCell(ws, cb.tstxy.units, cs1, mhdr.units);
			}
			else
			{
				Block.setCell(ws, cb.tstxy.loLim, cs1, "");
				Block.setCell(ws, cb.tstxy.hiLim, cs1, "");
				Block.setCell(ws, cb.tstxy.pin, cs1, "");
				Block.setCell(ws, cb.tstxy.units, cs1, "");
			}
			if (!rot)
			{
			    ws.addMergedRegion(new CellRangeAddress(cb.tstxy.units.r, cb.tstxy.units.r+1, cb.tstxy.units.c, cb.tstxy.units.c));
			}
			cb.tstxy.inc();
		});
	}
	
	private int getCellWidth(String testName)
	{
		if (!rot && !noWrapTestNames) return(256 * (8 + precision));
		double w = 0.0;
		for (int i=0; i<testName.length(); i++)
		{
			if (Character.isUpperCase(testName.charAt(i))) w += 1.5;
			else w += 1.0;
		}
		w *= 256.0;
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
