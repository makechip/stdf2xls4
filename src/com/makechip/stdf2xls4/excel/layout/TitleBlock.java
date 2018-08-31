package com.makechip.stdf2xls4.excel.layout;

import java.io.File;
import java.util.List;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.DeviceXY;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.TestXY;
import com.makechip.stdf2xls4.excel.layout.DataHeader;
import com.makechip.stdf2xls4.excel.layout.HeaderBlock;
import com.makechip.stdf2xls4.excel.layout.LegendBlock;
import com.makechip.stdf2xls4.excel.layout.LogoBlock;
import com.makechip.stdf2xls4.excel.layout.PageTitleBlock;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;
import com.makechip.util.Log;

public class TitleBlock
{
	private final HeaderBlock hb;
	private final PageTitleBlock ptb;
	private final LegendBlock lb;
	private final LogoBlock logo;
	private final CornerBlock cb;
	private final DataHeader dh;
	private final int testWidth;
	public final DeviceXY devxy;
	public final TestXY tstxy;
	
	public TitleBlock(PageHeader hdr, 
			          File logoFile, 
			          String pageTitle, 
			          boolean wafersort, 
			          boolean timeStampedFiles,
			          CliOptions options,
			          int numDevices,
			          List<TestHeader> hdrs)
	{
		devxy = new DeviceXY(timeStampedFiles, options.onePage, wafersort, options.rotate);
		tstxy = new TestXY(timeStampedFiles, options.onePage, wafersort, options.noWrapTestNames, options.rotate);
		devxy.reset();
		tstxy.reset();
	    hb = new HeaderBlock(options, hdr);
	    testWidth = hdrs == null ? 0 : hdrs.size();
	    cb = new CornerBlock(devxy, tstxy, timeStampedFiles, options.onePage, wafersort, options.noWrapTestNames, options.rotate);
	    ptb = new PageTitleBlock(pageTitle, options.rotate ? numDevices : hdrs.size(), cb.getWidth());
	    lb = new LegendBlock();
	    logo = new LogoBlock(logoFile);
	    dh = new DataHeader(cb, options.precision, options.rotate, options.noWrapTestNames, hdrs);
	}
	
	public int getRC(String testName, long tnum, String pin, int dupNum)
	{
	    return(dh.getRC(testName, tnum, pin, dupNum));
	}
    
	public void addBlock(Spreadsheet ss, int page, boolean new_sheet)
    {
	    int mr = ss.getNumMergedCells(page);
	    Log.msg("mr = " + mr + "  page = " + page);
		hb.addBlock(ss, page);
		ptb.addBlock(ss, page, (mr == 0) || new_sheet);
		lb.addBlock(ss, page);
		logo.addBlock(ss, page, (mr == 0) || new_sheet);
		cb.addBlock(ss, page, (mr == 0) || new_sheet);
		dh.addBlock(ss, page, (mr == 0) || new_sheet);
    }
	
	public int getWidth()
	{
		return(hb.getWidth() + cb.getWidth() + testWidth);
	}

	public int getHeight()
	{
		return(hb.getHeight() + lb.getHeight() + 1);
	}

}
