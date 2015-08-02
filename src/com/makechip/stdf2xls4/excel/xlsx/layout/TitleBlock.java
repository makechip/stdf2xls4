package com.makechip.stdf2xls4.excel.xlsx.layout;

import java.io.File;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.LegendBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.LogoBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.PageTitleBlock;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class TitleBlock implements Block
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
    
	@Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
		hb.addBlock(wb, ws);
		ptb.addBlock(wb, ws);
		lb.addBlock(wb, ws);
		logo.addBlock(wb, ws);
		cb.addBlock(wb, ws);
		dh.addBlock(wb, ws);
    }
	
	@Override
	public int getWidth()
	{
		return(hb.getWidth() + cb.getWidth() + testWidth);
	}

	@Override
	public int getHeight()
	{
		return(hb.getHeight() + lb.getHeight() + 1);
	}
	

}
