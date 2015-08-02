package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.HEADER4_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.HEADER4_FMTR;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.DeviceXY;
import com.makechip.stdf2xls4.excel.TestXY;
import com.makechip.stdf2xls4.excel.xlsx.Block;

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
	public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
	{
		if (timeStampedFiles)
		{
			Block.setCell(ws, devxy.tstampLabel, LABEL_TIMESTAMP);
		}
		if (onePage)
		{
			Block.setCell(ws, devxy.wafOrStepLabel, wafersort ? LABEL_WAFER : LABEL_STEP);
		}
		if (wafersort)
		{
			Block.setCell(ws, devxy.xLabel, LABEL_X);
			Block.setCell(ws, devxy.yOrSnLabel, LABEL_Y);
		}
		else 
		{
			Block.setCell(ws, devxy.yOrSnLabel, LABEL_SN);
		}
		Block.setCell(ws, devxy.hwBinLabel, LABEL_HW_BIN);
		Block.setCell(ws, devxy.swBinLabel, LABEL_SW_BIN);
		Block.setCell(ws, devxy.rsltLabel, LABEL_RESULT);
		Block.setCell(ws, devxy.tempLabel, LABEL_TEMP);

		Block.setCell(ws, tstxy.tnameLabel, LABEL_TEST_NAME);
		Block.setCell(ws, tstxy.tnumLabel, LABEL_TEST_NUM);
		Block.setCell(ws, tstxy.dupNumLabel, LABEL_DUP);
		Block.setCell(ws, tstxy.pinLabel, LABEL_PIN);
		Block.setCell(ws, tstxy.loLimLabel, LABEL_LO_LIMIT);
		Block.setCell(ws, tstxy.hiLimLabel, LABEL_HI_LIMIT);
		Block.setCell(ws, tstxy.unitsLabel, LABEL_UNITS);

		if (rotate)
		{
		    ws.setColumnWidth(tstxy.tname.c, 256 * 48);
		    ws.addMergedRegion(getTRange(tstxy.tnameLabel));
		    ws.addMergedRegion(getTRange(tstxy.tnumLabel));
		    ws.addMergedRegion(getTRange(tstxy.dupNumLabel));
		    ws.addMergedRegion(getTRange(tstxy.pinLabel));
	 	    ws.addMergedRegion(getTRange(tstxy.loLimLabel));
		    ws.addMergedRegion(getTRange(tstxy.hiLimLabel));
		    ws.addMergedRegion(getTRange(tstxy.unitsLabel));
		}
		else
		{
			if (timeStampedFiles)
			{
				ws.setColumnWidth(devxy.tstampLabel.c, 15 * 256);
			    ws.addMergedRegion(getDRange(devxy.tstampLabel));	
			}
			if (onePage)
			{
				ws.addMergedRegion(getDRange(devxy.wafOrStepLabel));
			}
			if (wafersort)
			{
				ws.addMergedRegion(getDRange(devxy.xLabel));
			}
			ws.addMergedRegion(getDRange(devxy.yOrSnLabel));
			ws.addMergedRegion(getDRange(devxy.hwBinLabel));
			ws.addMergedRegion(getDRange(devxy.swBinLabel));
		    ws.addMergedRegion(getDRange(devxy.rsltLabel));
		    if (!noWrap)
		    {
		    	ws.addMergedRegion(new CellRangeAddress(tstxy.tnameLabel.r, 
		    			                                tstxy.tnumLabel.r-1, 
		    			                                tstxy.tnameLabel.c, 
		    			                                tstxy.tnameLabel.c));
		    }
		}
		addFormat(wb, ws);
	}
	
	@Override
	public void addFormat(XSSFWorkbook wb, XSSFSheet ws)
	{
		CellStyle cs4r = HEADER4_FMTR.getFormat(wb);
		CellStyle cs4 = HEADER4_FMT.getFormat(wb);
		if (timeStampedFiles)
		{
			Block.setCell(ws, devxy.tstampLabel, rotate ? cs4 : cs4r);
		}
		if (onePage)
		{
			Block.setCell(ws, devxy.wafOrStepLabel, rotate ? cs4 : cs4r);
		}
		if (wafersort)
		{
			Block.setCell(ws, devxy.xLabel, rotate ? cs4 : cs4r);
			Block.setCell(ws, devxy.yOrSnLabel, rotate ? cs4 : cs4r);
		}
		else 
		{
			Block.setCell(ws, devxy.yOrSnLabel, rotate ? cs4 : cs4r);
		}
		Block.setCell(ws, devxy.hwBinLabel, rotate ? cs4 : cs4r);
		Block.setCell(ws, devxy.swBinLabel, rotate ? cs4 : cs4r);
		Block.setCell(ws, devxy.rsltLabel, rotate ? cs4 : cs4r);
		Block.setCell(ws, devxy.tempLabel, rotate ? cs4 : cs4r);

		Block.setCell(ws, tstxy.tnameLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.tnumLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.dupNumLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.pinLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.loLimLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.hiLimLabel, rotate ? cs4r : cs4);
		Block.setCell(ws, tstxy.unitsLabel, rotate ? cs4r : cs4);
	}
	
	private CellRangeAddress getTRange(Coord xy)
	{
		return(new CellRangeAddress(xy.r, tstxy.unitsLabel.r, xy.c, xy.c));
	}
	
	private CellRangeAddress getDRange(Coord xy)
	{
		return(new CellRangeAddress(xy.r, devxy.tempLabel.r, xy.c, xy.c));
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
