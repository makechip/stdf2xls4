package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.HEADER4_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.HEADER4_FMTR;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.DeviceXY;
import com.makechip.stdf2xls4.excel.Format_t;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.TestXY;

public class CornerBlock
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
	
	public void addBlock(Spreadsheet ss, int page)
	{
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
				fixBorders(ss, page, devxy.tstampLabel, devxy.tempLabel.r, HEADER4_FMT);
			    ss.mergeCells(page, devxy.tstampLabel.r, devxy.tempLabel.r, devxy.tstampLabel.c, devxy.tstampLabel.c);
			}
			if (onePage)
			{
				fixBorders(ss, page, devxy.wafOrStepLabel, devxy.tempLabel.r, HEADER4_FMT);
				ss.mergeCells(page, devxy.wafOrStepLabel.r, devxy.tempLabel.r, devxy.wafOrStepLabel.c, devxy.wafOrStepLabel.c);
			}
			if (wafersort)
			{
				fixBorders(ss, page, devxy.xLabel, devxy.tempLabel.r, HEADER4_FMT);
				ss.mergeCells(page, devxy.xLabel.r, devxy.tempLabel.r, devxy.xLabel.c, devxy.xLabel.c);
			}
			fixBorders(ss, page, devxy.yOrSnLabel, devxy.tempLabel.r, HEADER4_FMT);
			ss.mergeCells(page, devxy.yOrSnLabel.r, devxy.tempLabel.r, devxy.yOrSnLabel.c, devxy.yOrSnLabel.c);
			fixBorders(ss, page, devxy.hwBinLabel, devxy.tempLabel.r, HEADER4_FMT);
			ss.mergeCells(page, devxy.hwBinLabel.r, devxy.tempLabel.r, devxy.hwBinLabel.c, devxy.hwBinLabel.c);
			fixBorders(ss, page, devxy.swBinLabel, devxy.tempLabel.r, HEADER4_FMT);
			ss.mergeCells(page, devxy.swBinLabel.r, devxy.tempLabel.r, devxy.swBinLabel.c, devxy.swBinLabel.c);
			fixBorders(ss, page, devxy.rsltLabel, devxy.tempLabel.r, HEADER4_FMT);
		    ss.mergeCells(page, devxy.rsltLabel.r, devxy.tempLabel.r, devxy.rsltLabel.c, devxy.rsltLabel.c);
		    if (!noWrap)
		    {
		    	ss.mergeCells(page, tstxy.tnameLabel.r, tstxy.tnumLabel.r-1, tstxy.tnameLabel.c, tstxy.tnameLabel.c);
		    }
		}
		Format_t cs4r = HEADER4_FMTR;
		Format_t cs4 = HEADER4_FMT;
		if (timeStampedFiles)
		{
			ss.setCell(page, devxy.tstampLabel, rotate ? cs4 : cs4r, LABEL_TIMESTAMP);
		}
		if (onePage)
		{
			ss.setCell(page, devxy.wafOrStepLabel, rotate ? cs4 : cs4r, wafersort ? LABEL_WAFER : LABEL_STEP);
		}
		if (wafersort)
		{
			ss.setCell(page, devxy.xLabel, rotate ? cs4 : cs4r, LABEL_X);
			ss.setCell(page, devxy.yOrSnLabel, rotate ? cs4 : cs4r, LABEL_Y);
		}
		else 
		{
			ss.setCell(page, devxy.yOrSnLabel, rotate ? cs4 : cs4r, LABEL_SN);
		}
		ss.setCell(page, devxy.hwBinLabel, rotate ? cs4 : cs4r, LABEL_HW_BIN);
		ss.setCell(page, devxy.swBinLabel, rotate ? cs4 : cs4r, LABEL_SW_BIN);
		ss.setCell(page, devxy.rsltLabel, rotate ? cs4 : cs4r, LABEL_RESULT);
		ss.setCell(page, devxy.tempLabel, rotate ? cs4 : cs4r, LABEL_TEMP);

		ss.setCell(page, tstxy.tnameLabel, rotate ? cs4r : cs4, LABEL_TEST_NAME);
		ss.setCell(page, tstxy.tnumLabel, rotate ? cs4r : cs4, LABEL_TEST_NUM);
		ss.setCell(page, tstxy.dupNumLabel, rotate ? cs4r : cs4, LABEL_DUP);
		ss.setCell(page, tstxy.pinLabel, rotate ? cs4r : cs4, LABEL_PIN);
		ss.setCell(page, tstxy.loLimLabel, rotate ? cs4r : cs4, LABEL_LO_LIMIT);
		ss.setCell(page, tstxy.hiLimLabel, rotate ? cs4r : cs4, LABEL_HI_LIMIT);
		ss.setCell(page, tstxy.unitsLabel, rotate ? cs4r : cs4, LABEL_UNITS);

	}
	
	private void fixBorders(Spreadsheet ss, int page, Coord xy, int row, Format_t fmt)
	{
		for (int i=xy.r; i<=row; i++)
		{
			ss.setCell(page, new Coord(xy.c, i), fmt, "");
		}
	}
	
	public int getHeight() 
	{  
		if (rotate)
		{
		    boolean t = timeStampedFiles;
		    boolean o = onePage;
		    boolean w = wafersort;
		    return(t ? (o ? (w ? 8 : 7) : (w ? 7 : 6)) : (o ? (w ? 7 : 6) : (w ? 6 : 5))); 
		}
		return(9);
	}

	public int getWidth() 
	{ 
		if (rotate) return(7); 
	    boolean t = timeStampedFiles;
	    boolean o = onePage;
	    boolean w = wafersort;
	    return(t ? (o ? (w ? 8 : 7) : (w ? 7 : 6)) : (o ? (w ? 7 : 6) : (w ? 6 : 5))); 
	}

}
