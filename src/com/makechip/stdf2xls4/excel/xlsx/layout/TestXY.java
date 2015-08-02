package com.makechip.stdf2xls4.excel.xlsx.layout;

public class TestXY
{
	public final Coord tnameLabel;
	public final Coord tnumLabel;
	public final Coord dupNumLabel;
	public final Coord loLimLabel;
	public final Coord hiLimLabel;
	public final Coord pinLabel;
	public final Coord unitsLabel;
	
	private final boolean rotate;
	
	public Coord tname;
	public Coord tnum;
	public Coord dupNum;
	public Coord loLim;
	public Coord hiLim;
	public Coord pin;
	public Coord units;

	public TestXY(boolean timeStampedFiles, boolean onePage, boolean wafersort, boolean noWrap, boolean rotate)
	{
		this.rotate = rotate;
		if (rotate)
		{
			int row = 5;
			if (timeStampedFiles) row++;
			if (onePage) row++;
			if (wafersort) row++;
			final int c = LogoBlock.WIDTH + LegendBlock.WIDTH;
			final int r = PageTitleBlock.HEIGHT;
		    tnameLabel = new Coord(c, r);
		    tnumLabel = new Coord(c+1, r);
		    dupNumLabel = new Coord(c+2, r);
		    loLimLabel = new Coord(c+3, r);
		    hiLimLabel = new Coord(c+4, r);
		    pinLabel = new Coord(c+5, r);
		    unitsLabel = new Coord(c+6, r + row);
		}
		else
		{
			int y = 0;
			final int r = PageTitleBlock.HEIGHT;
			final int c = LogoBlock.WIDTH + LegendBlock.WIDTH;
			int col = c + 4;
		    if (timeStampedFiles) col++;
		    if (onePage) col++;
		    if (wafersort) col++;
		    tnameLabel = new Coord(col, r);
		    if (noWrap) y = 1; else y = 4;
		    tnumLabel = new Coord(col, r+y);
		    dupNumLabel = new Coord(col, r+y+1);
		    loLimLabel = new Coord(col, r+y+2);
		    hiLimLabel = new Coord(col, r+y+3);
		    pinLabel = new Coord(col, r+y+4);
		    unitsLabel = new Coord(col, r+y+5);
		    	
		}
	}
	
	public void reset()
	{
		if (rotate)
		{
		    tname = new Coord(tnameLabel.c, unitsLabel.r+1);
		    tnum = new Coord(tnumLabel.c, unitsLabel.r+1);
		    dupNum = new Coord(dupNumLabel.c, unitsLabel.r+1);
		    loLim = new Coord(loLimLabel.c, unitsLabel.r+1);
		    hiLim = new Coord(hiLimLabel.c, unitsLabel.r+1);
		    pin = new Coord(pinLabel.c, unitsLabel.r+1);
		    units = new Coord(unitsLabel.c, unitsLabel.r+1);
		}
		else
		{
		    tname = new Coord(tnameLabel.c+1, tnameLabel.r);
		    tnum = new Coord(tnumLabel.c+1, tnumLabel.r);
		    dupNum = new Coord(dupNumLabel.c+1, dupNumLabel.r);
		    loLim = new Coord(loLimLabel.c+1, loLimLabel.r);
		    hiLim = new Coord(hiLimLabel.c+1, hiLimLabel.r);
		    pin = new Coord(pinLabel.c+1, pinLabel.r);
		    units = new Coord(unitsLabel.c+1, unitsLabel.r);
		}
	}
	
	public void inc()
	{
		if (rotate)
		{
		    tname = new Coord(tname.c, tname.r+1);
		    tnum = new Coord(tnum.c, tnum.r+1);
		    dupNum = new Coord(dupNum.c, dupNum.r+1);
		    loLim = new Coord(loLim.c, loLim.r+1);
		    hiLim = new Coord(hiLim.c, hiLim.r+1);
		    pin = new Coord(pin.c, pin.r+1);
		    units = new Coord(units.c, units.r+1);
		}
		else
		{
		    tname = new Coord(tname.c+1, tname.r);
		    tnum = new Coord(tnum.c+1, tnum.r);
		    dupNum = new Coord(dupNum.c+1, dupNum.r);
		    loLim = new Coord(loLim.c+1, loLim.r);
		    hiLim = new Coord(hiLim.c+1, hiLim.r);
		    pin = new Coord(pin.c+1, pin.r);
		    units = new Coord(units.c+1, units.r);
		}
	}

}
