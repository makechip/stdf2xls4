package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.HEADER5_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT_WRAP;

import java.util.List;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.layout.CornerBlock;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class DataHeader
{
	private final int precision;
	private final boolean rot;
	private final boolean noWrapTestNames;
	private final List<TestHeader> hdrs;
	private int maxLength;
	private int maxPinLength;
	private final CornerBlock cb;
	
	public DataHeader(CornerBlock cb, int precision, boolean rot, boolean noWrapTestNames, List<TestHeader> hdrs)
	{
		this.cb = cb;
		maxLength = 0;
		maxPinLength = 0;
		this.precision = precision;
		this.hdrs = hdrs;
		this.rot = rot;
		this.noWrapTestNames = noWrapTestNames;
	}
	
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
			else 
			{
				for (int r=cb.tstxy.tnameLabel.r; r<cb.tstxy.tnumLabel.r; r++)
				{
				    ss.setCell(page, new Coord(cb.tstxy.tname.c, r), TEST_NAME_FMT_WRAP, "");
				}
				ss.mergeCells(page, cb.tstxy.tnameLabel.r, cb.tstxy.tnumLabel.r-1, cb.tstxy.tname.c, cb.tstxy.tname.c);
			}
			ss.setCell(page, cb.tstxy.tname, noWrapTestNames ? TEST_NAME_FMT : TEST_NAME_FMT_WRAP, hdr.testName);
			ss.setCell(page, cb.tstxy.tnum, HEADER1_FMT, hdr.testNumber);	
			ss.setCell(page, cb.tstxy.dupNum, HEADER1_FMT, hdr.dupNum);
			if (hdr instanceof MultiParametricTestHeader)
			{
				MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
				if (mhdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.loLim, HEADER5_FMT, precision, mhdr.loLimit);
				if (mhdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.hiLim, HEADER5_FMT, precision, mhdr.hiLimit);
                if (mhdr.pin != null && mhdr.pin.length() > 1)
                {
                    int width = getPinColWidth(mhdr.pin);	
                    ss.setColumnWidth(page, cb.tstxy.pin.c, width);
                }
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, mhdr.pin);
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, mhdr.units);
			}
			else if (hdr instanceof ParametricTestHeader)
			{
				ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
				if (phdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.loLim, HEADER5_FMT, precision, phdr.loLimit);
				if (phdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.hiLim, HEADER5_FMT, precision, phdr.hiLimit);
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, phdr.units);
			}
			else
			{
				ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, "");
			}
			if (!rot)
			{
				ss.setCell(page, new Coord(cb.tstxy.units.c, cb.tstxy.units.r+1), HEADER1_FMT, "");
			    ss.mergeCells(page, cb.tstxy.units.r, cb.tstxy.units.r+1, cb.tstxy.units.c, cb.tstxy.units.c);
			}
			cb.tstxy.inc();
		});
	}
	
	private int getPinColWidth(String pin)
	{
	    double l = 0.0;
	    for (int i=0; i<pin.length(); i++) 
	    {
	    	if (Character.isUpperCase(pin.charAt(i))) l += 1.4; else l++;
	    }
	    int len = (int) l;
	    if (rot)
	    {
	    	if (len > maxPinLength) maxPinLength = len;
	    	return(maxPinLength);
	    }
	    return(len);
	}
	
	public int getWidth()
	{
		return(8);
	}

	public int getHeight()
	{
		return(hdrs.size());
	}

}
