package com.makechip.stdf2xls4.excel;

public interface Block
{
	public int getWidth();
	public int getHeight();
	public void addBlock(Spreadsheet ss, int page);
	public void addFormat(Spreadsheet ss, int page);

}
