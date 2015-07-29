package com.makechip.stdf2xls4.excel.xls;

import java.io.IOException;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface Block
{
	public int getWidth();
	public int getHeight();
	public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException;
}
