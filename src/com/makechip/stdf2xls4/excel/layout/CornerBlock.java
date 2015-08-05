package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.HEADER4_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.HEADER4_FMTR;

import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.DeviceXY;
import com.makechip.stdf2xls4.excel.Format_t;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.TestXY;

public class CornerBlock implements Block
{
	public static final String LABEL_TIMESTAMP = "TimeStamp";
	public static final String LABEL_WAFER = "Wafer";
	public static final String LABEL_STEP = "Step";
	public static final String LABEL_X = "X";
	public static final String LABEL_Y = "X";
	public static final String LABEL_SN = "S/N";
	public static final String LABEL_DUP = "Duplicate";
	public static final String LABEL_HW_BIN = "HW Bin";
	public static final String LABEL_SW_BIN = "SW Bin";
	public static final String LABEL_RESULT = "Result";
	public static final String LABEL_TEMP = "Temp";
	public static final String LABEL_TEST_NAME = "Test Name";
	public static final String LABEL_TEST_NUM = "Test Num";
	public static final String LABEL_LO_LIMIT = "Lo Limit";
	public static final String LABEL_HI_LIMIT = "Hi Limit";
	public static final String LABEL_PIN = "Pin";
	public static final String LABEL_UNITS = "Units";
	public final DeviceXY devxy;
	public final TestXY tstxy;
	public final boolean timeStampedFiles;
	public final boolean onePage;
	public final boolean wafersort;
	public final boolean rotate;
	public final boolean noWrap;
	
	public CornerBlock(DeviceXY devxy, 
			           TestXY tstxy, 
			           boolean timeStampedFiles, 
			           boolean onePage, 
			           boolean wafersort,
			           boolean noWrap,
			           boolean rotate)
	{
		this.devxy = devxy;
		this.tstxy = tstxy;
		this.timeStampedFiles = timeStampedFiles;
		this.onePage = onePage;
		this.wafersort = wafersort;
		this.noWrap = noWrap;
		this.rotate = rotate;
	}
	
	@Override
	public void addBlock(Spreadsheet ss, int page)
	{
		if (timeStampedFiles)
		{
			ss.setCell(page, devxy.tstampLabel, LABEL_TIMESTAMP);
		}
		if (onePage)
		{
			ss.setCell(page, devxy.wafOrStepLabel, wafersort ? LABEL_WAFER : LABEL_STEP);
		}
		if (wafersort)
		{
			ss.setCell(page, devxy.xLabel, LABEL_X);
			ss.setCell(page, devxy.yOrSnLabel, LABEL_Y);
		}
		else 
		{
			ss.setCell(page, devxy.yOrSnLabel, LABEL_SN);
		}
		ss.setCell(page, devxy.hwBinLabel, LABEL_HW_BIN);
		ss.setCell(page, devxy.swBinLabel, LABEL_SW_BIN);
		ss.setCell(page, devxy.rsltLabel, LABEL_RESULT);
		ss.setCell(page, devxy.tempLabel, LABEL_TEMP);

		ss.setCell(page, tstxy.tnameLabel, LABEL_TEST_NAME);
		ss.setCell(page, tstxy.tnumLabel, LABEL_TEST_NUM);
		ss.setCell(page, tstxy.dupNumLabel, LABEL_DUP);
		ss.setCell(page, tstxy.pinLabel, LABEL_PIN);
		ss.setCell(page, tstxy.loLimLabel, LABEL_LO_LIMIT);
		ss.setCell(page, tstxy.hiLimLabel, LABEL_HI_LIMIT);
		ss.setCell(page, tstxy.unitsLabel, LABEL_UNITS);

		if (rotate)
		{
		    ss.setColumnWidth(page, tstxy.tname.c, 48);
		    ss.mergeCells(page, tstxy.tnameLabel.r, tstxy.unitsLabel.r, tstxy.tnameLabel.c, tstxy.tnameLabel.c);
		    ss.mergeCells(page, tstxy.tnumLabel.r, tstxy.unitsLabel.r, tstxy.tnumLabel.c, tstxy.tnumLabel.c);
		    ss.mergeCells(page, tstxy.dupNumLabel.r, tstxy.unitsLabel.r, tstxy.dupNumLabel.c, tstxy.dupNumLabel.c);
		    ss.mergeCells(page, tstxy.pinLabel.r, tstxy.unitsLabel.r, tstxy.pinLabel.c, tstxy.pinLabel.c);
	 	    ss.mergeCells(page, tstxy.loLimLabel.r, tstxy.unitsLabel.r, tstxy.loLimLabel.c, tstxy.loLimLabel.c);
		    ss.mergeCells(page, tstxy.hiLimLabel.r, tstxy.unitsLabel.r, tstxy.hiLimLabel.c, tstxy.hiLimLabel.c);
		    //ss.mergeCells(page, tstxy.unitsLabel.r, tstxy.unitsLabel.r, tstxy.));
		}
		else
		{
			if (timeStampedFiles)
			{
				ss.setColumnWidth(page, devxy.tstampLabel.c, 15);
			    ss.mergeCells(page, devxy.tstampLabel.r, devxy.tempLabel.r, devxy.tstampLabel.c, devxy.tstampLabel.c);
			}
			if (onePage)
			{
				ss.mergeCells(page, devxy.wafOrStepLabel.r, devxy.tempLabel.r, devxy.wafOrStepLabel.c, devxy.wafOrStepLabel.c);
			}
			if (wafersort)
			{
				ss.mergeCells(page, devxy.xLabel.r, devxy.tempLabel.r, devxy.xLabel.c, devxy.xLabel.c);
			}
			ss.mergeCells(page, devxy.yOrSnLabel.r, devxy.tempLabel.r, devxy.yOrSnLabel.c, devxy.yOrSnLabel.c);
			ss.mergeCells(page, devxy.hwBinLabel.r, devxy.tempLabel.r, devxy.hwBinLabel.c, devxy.hwBinLabel.c);
			ss.mergeCells(page, devxy.swBinLabel.r, devxy.tempLabel.r, devxy.swBinLabel.c, devxy.swBinLabel.c);
		    ss.mergeCells(page, devxy.rsltLabel.r, devxy.tempLabel.r, devxy.rsltLabel.c, devxy.rsltLabel.c);
		    if (!noWrap)
		    {
		    	ss.mergeCells(page, tstxy.tnameLabel.r, tstxy.tnumLabel.r-1, tstxy.tnameLabel.c, tstxy.tnameLabel.c);
		    }
		}
		addFormat(ss, page);
	}
	
	@Override
	public void addFormat(Spreadsheet ss, int page)
	{
		Format_t cs4r = HEADER4_FMTR;
		Format_t cs4 = HEADER4_FMT;
		if (timeStampedFiles)
		{
			ss.setFormat(page, devxy.tstampLabel, rotate ? cs4 : cs4r);
		}
		if (onePage)
		{
			ss.setFormat(page, devxy.wafOrStepLabel, rotate ? cs4 : cs4r);
		}
		if (wafersort)
		{
			ss.setFormat(page, devxy.xLabel, rotate ? cs4 : cs4r);
			ss.setFormat(page, devxy.yOrSnLabel, rotate ? cs4 : cs4r);
		}
		else 
		{
			ss.setFormat(page, devxy.yOrSnLabel, rotate ? cs4 : cs4r);
		}
		ss.setFormat(page, devxy.hwBinLabel, rotate ? cs4 : cs4r);
		ss.setFormat(page, devxy.swBinLabel, rotate ? cs4 : cs4r);
		ss.setFormat(page, devxy.rsltLabel, rotate ? cs4 : cs4r);
		ss.setFormat(page, devxy.tempLabel, rotate ? cs4 : cs4r);

		ss.setFormat(page, tstxy.tnameLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.tnumLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.dupNumLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.pinLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.loLimLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.hiLimLabel, rotate ? cs4r : cs4);
		ss.setFormat(page, tstxy.unitsLabel, rotate ? cs4r : cs4);
	}
	
	@Override
	public int getHeight() 
	{  
		if (rotate)
		{
		    boolean t = timeStampedFiles;
		    boolean o = onePage;
		    boolean w = wafersort;
		    return(t ? (o ? (w ? 8 : 7) : (w ? 7 : 6)) : (o ? (w ? 7 : 6) : (w ? 6 : 5))); 
		}
		return(8);
	}

	@Override
	public int getWidth() 
	{ 
		if (rotate) return(7); 
	    boolean t = timeStampedFiles;
	    boolean o = onePage;
	    boolean w = wafersort;
	    return(t ? (o ? (w ? 8 : 7) : (w ? 7 : 6)) : (o ? (w ? 7 : 6) : (w ? 6 : 5))); 
	}

}
