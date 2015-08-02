package com.makechip.stdf2xls4.excel;

public class DeviceXY
{
	public final Coord tstampLabel;
	public final Coord wafOrStepLabel;
	public final Coord xLabel;
	public final Coord yOrSnLabel;
	public final Coord hwBinLabel;
	public final Coord swBinLabel;
	public final Coord rsltLabel;
	public final Coord tempLabel;
	
	private final boolean rotate;
	
	public Coord tstamp;
	public Coord wafOrStep;
	public Coord x;
	public Coord yOrSn;
	public Coord hwBin;
	public Coord swBin;
	public Coord rslt;
	public Coord temp;

	public DeviceXY(boolean timeStampedFiles, boolean onePage, boolean wafersort, boolean rotate)
	{
		this.rotate = rotate;
		if (rotate)
		{
			final int c = 8;
		    tstampLabel = new Coord(c, 7);
		    wafOrStepLabel = new Coord(c, timeStampedFiles ? tstampLabel.r + 1 : tstampLabel.r);
		    xLabel = new Coord(c, onePage ? wafOrStepLabel.r + 1 : wafOrStepLabel.r);
		    yOrSnLabel = new Coord(c, wafersort ? xLabel.r + 1 : xLabel.r);
		    hwBinLabel = new Coord(c, yOrSnLabel.r + 1);
		    swBinLabel = new Coord(c, hwBinLabel.r + 1);
		    rsltLabel = new Coord(c, swBinLabel.r + 1);
		    tempLabel = new Coord(c, rsltLabel.r + 1);
		}
		else
		{
			final int r = 7;
			final int c0 = 2;
		    tstampLabel = new Coord(c0, r);
		    wafOrStepLabel = new Coord(timeStampedFiles ? tstampLabel.c + 1 : tstampLabel.c, r);
		    xLabel = new Coord(onePage ? wafOrStepLabel.c + 1 : wafOrStepLabel.c, r);
		    yOrSnLabel = new Coord(wafersort ? xLabel.c + 1 : xLabel.c, r);
		    hwBinLabel = new Coord(yOrSnLabel.c + 1, r);
		    swBinLabel = new Coord(hwBinLabel.c + 1, r);
		    rsltLabel = new Coord(swBinLabel.c + 1, r);
		    tempLabel = new Coord(rsltLabel.c + 1, r + 10);
		    	
		}
	}
	
	public void reset()
	{
		if (rotate)
		{
		    tstamp = new Coord(tstampLabel.c+1, tstampLabel.r);
		    wafOrStep = new Coord(wafOrStepLabel.c+1, wafOrStepLabel.r);
		    x = new Coord(xLabel.c+1, xLabel.r);
		    yOrSn = new Coord(yOrSnLabel.c+1, yOrSnLabel.r);
		    hwBin = new Coord(hwBinLabel.c+1, hwBinLabel.r);
		    swBin = new Coord(swBinLabel.c+1, swBinLabel.r);
		    rslt = new Coord(rsltLabel.c+1, rsltLabel.r);
		    temp = new Coord(tempLabel.c+1, tempLabel.r);
		}
		else
		{
		    tstamp = new Coord(tstampLabel.c, xLabel.r + 11);
		    wafOrStep = new Coord(wafOrStepLabel.c, xLabel.r + 11);
		    x = new Coord(xLabel.c, xLabel.r + 11);
		    yOrSn = new Coord(yOrSnLabel.c, xLabel.r + 11);
		    hwBin = new Coord(hwBinLabel.c, xLabel.r + 11);
		    swBin = new Coord(swBinLabel.c, xLabel.r + 11);
		    rslt = new Coord(rsltLabel.c, xLabel.r + 11);
		    temp = new Coord(tempLabel.c, xLabel.r + 11);
		}
	}
	
	public void inc()
	{
		if (rotate)
		{
		    tstamp = new Coord(tstamp.c+1, tstamp.r);
		    wafOrStep = new Coord(wafOrStep.c+1, wafOrStep.r);
		    x = new Coord(x.c+1, x.r);
		    yOrSn = new Coord(yOrSn.c+1, yOrSn.r);
		    hwBin = new Coord(hwBin.c+1, hwBin.r);
		    swBin = new Coord(swBin.c+1, swBin.r);
		    rslt = new Coord(rslt.c+1, rslt.r);
		    temp = new Coord(temp.c+1, temp.r);
		}
		else
		{
		    tstamp = new Coord(tstamp.c, tstamp.r+1);
		    wafOrStep = new Coord(wafOrStep.c, wafOrStep.r+1);
		    x = new Coord(x.c, x.r+1);
		    yOrSn = new Coord(yOrSn.c, yOrSn.r+1);
		    hwBin = new Coord(hwBin.c, hwBin.r+1);
		    swBin = new Coord(swBin.c, swBin.r+1);
		    rslt = new Coord(rslt.c, rslt.r+1);
		    temp = new Coord(temp.c, temp.r+1);
		}
	}

}
