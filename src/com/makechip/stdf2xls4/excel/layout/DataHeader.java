package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.HEADER5_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT_WRAP;

import java.util.List;
import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.layout.CornerBlock;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader implements Block
{
	private final int precision;
	private final boolean rot;
	private final boolean noWrapTestNames;
	private final List<TestHeader> hdrs;
	private int maxLength;
	private final CornerBlock cb;
	
	public DataHeader(CornerBlock cb, int precision, boolean rot, boolean noWrapTestNames, List<TestHeader> hdrs)
	{
		this.cb = cb;
		maxLength = 0;
		this.precision = precision;
		this.hdrs = hdrs;
		this.rot = rot;
		this.noWrapTestNames = noWrapTestNames;
	}
	
	@Override
	public void addBlock(Spreadsheet ss, int page)
	{
		cb.tstxy.reset();
		hdrs.stream().forEach(hdr ->
		{
			if (rot || noWrapTestNames) 
			{
				if (hdr.testName.length() > maxLength)
				{
					maxLength = hdr.testName.length();
				    ss.setColumnWidth(page, cb.tstxy.tname.c, maxLength);
				}
			}
			else ss.mergeCells(page, cb.tstxy.tnameLabel.r, cb.tstxy.tnumLabel.r-1, cb.tstxy.tname.c, cb.tstxy.tname.c);
			ss.setCell(page, cb.tstxy.tname, hdr.testName);
			ss.setCell(page, cb.tstxy.tnum, hdr.testNumber);	
			ss.setCell(page, cb.tstxy.dupNum, hdr.dupNum);
			if (hdr instanceof ParametricTestHeader)
			{
				ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
				if (phdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, "");
				else ss.setCell(page, cb.tstxy.loLim, phdr.loLimit);
				if (phdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, "");
				else ss.setCell(page, cb.tstxy.hiLim, phdr.hiLimit);
				ss.setCell(page, cb.tstxy.pin, "");
				ss.setCell(page, cb.tstxy.units, phdr.units);
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
				MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
				if (mhdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, "");
				else ss.setCell(page, cb.tstxy.loLim, mhdr.loLimit);
				if (mhdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, "");
				else ss.setCell(page, cb.tstxy.hiLim, mhdr.hiLimit);
				ss.setCell(page, cb.tstxy.pin, mhdr.pin);
				ss.setCell(page, cb.tstxy.units, mhdr.units);
			}
			else
			{
				ss.setCell(page, cb.tstxy.loLim, "");
				ss.setCell(page, cb.tstxy.hiLim, "");
				ss.setCell(page, cb.tstxy.pin, "");
				ss.setCell(page, cb.tstxy.units, "");
			}
			if (!rot)
			{
			    ss.mergeCells(page, cb.tstxy.units.r, cb.tstxy.units.r+1, cb.tstxy.units.c, cb.tstxy.units.c);
			}
			cb.tstxy.inc();
		});
		addFormat(ss, page);
	}
	
	@Override
	public void addFormat(Spreadsheet ss, int page)
	{
		cb.tstxy.reset();
		hdrs.stream().forEach(hdr ->
		{
			ss.setFormat(page, cb.tstxy.tname, noWrapTestNames ? TEST_NAME_FMT : TEST_NAME_FMT_WRAP);
			ss.setFormat(page, cb.tstxy.tnum, HEADER1_FMT);	
			ss.setFormat(page, cb.tstxy.dupNum, HEADER1_FMT);
			if (hdr instanceof ParametricTestHeader)
			{
				ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
				if (phdr.loLimit == null) ss.setFormat(page, cb.tstxy.loLim, HEADER1_FMT);
				else ss.setFormat(page, cb.tstxy.loLim, HEADER5_FMT, precision);
				if (phdr.hiLimit == null) ss.setFormat(page, cb.tstxy.hiLim, HEADER1_FMT);
				else ss.setFormat(page, cb.tstxy.hiLim, HEADER5_FMT, precision);
				ss.setFormat(page, cb.tstxy.pin, HEADER1_FMT);
				ss.setFormat(page, cb.tstxy.units, HEADER1_FMT);
			}
			else if (hdr instanceof MultiParametricTestHeader)
			{
				MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
				if (mhdr.loLimit == null) ss.setFormat(page, cb.tstxy.loLim, HEADER1_FMT);
				else ss.setFormat(page, cb.tstxy.loLim, HEADER5_FMT, precision);
				if (mhdr.hiLimit == null) ss.setFormat(page, cb.tstxy.hiLim, HEADER1_FMT);
				else ss.setFormat(page, cb.tstxy.hiLim, HEADER5_FMT, precision);
				ss.setFormat(page, cb.tstxy.pin, HEADER1_FMT);
				ss.setFormat(page, cb.tstxy.units, HEADER1_FMT);
			}
			else
			{
				ss.setFormat(page, cb.tstxy.loLim, HEADER1_FMT);
				ss.setFormat(page, cb.tstxy.hiLim, HEADER1_FMT);
				ss.setFormat(page, cb.tstxy.pin, HEADER1_FMT);
				ss.setFormat(page, cb.tstxy.units, HEADER1_FMT);
			}
			cb.tstxy.inc();
		});
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
