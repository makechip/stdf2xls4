package com.makechip.stdf2xls4.excel;

public class Coord
{
	public final int c;
	public final int r;

	public Coord(int c, int r)
	{
		this.c = c;
		this.r = r;
	}
	
	@Override
	public String toString()
	{
	    return("c = " + c + " r = " + r);
	}

}
