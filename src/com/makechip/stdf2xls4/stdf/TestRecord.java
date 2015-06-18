package com.makechip.stdf2xls4.stdf;

import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

public abstract class TestRecord extends StdfRecord 
{
	public final long testNumber;
	public final short headNumber;
	public final short siteNumber;
	
	protected TestRecord(Record_t type, byte[] data)
	{
		super(type, data);
		this.testNumber = getU4(MISSING_LONG);
		this.headNumber = getU1(MISSING_SHORT);
		this.siteNumber = getU1(MISSING_SHORT);
	}
	
	protected TestRecord(Record_t type, long testNumber, short headNumber, short siteNumber)
	{
		super(type, null);
		this.testNumber = testNumber;
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
	}
	
	protected TestRecord(String testName, String valueText, long testNumber, short headNumber, short siteNumber)
	{
		super(Record_t.DTR, null);
		this.testNumber = testNumber;
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
	}
	
	public abstract TestID getTestId();
	
	protected abstract void setTestName(String testName);
	
	protected abstract void setText(String text);
	
	protected TestRecord(byte[] data)
	{
		super(Record_t.DTR, data);
		String text = getCn();
		StringTokenizer st = new StringTokenizer(text, ": \t");
	    st.nextToken(); // burn TEXT_DATA
	    setTestName(st.nextToken());
	    String valueText = st.nextToken();
	    setText(valueText);
	    String tnum = (st.hasMoreTokens()) ? st.nextToken() : "0";
	    long t = 0L;
	    try { t = Long.parseLong(tnum); }
	    catch (Exception e) { Log.fatal("Invalid test number in DatalogTestRecord: " + tnum); }
	    testNumber = t;
	    String snum = (st.hasMoreTokens()) ? st.nextToken() : "0";
	    short s = (short) 0; 
	    try { s = Short.parseShort(snum); }
	    catch (Exception e) { Log.fatal("Invalid site number in DatalogTestRecord: " + snum); }
	    siteNumber = s;
	    String hnum = (st.hasMoreTokens()) ? st.nextToken() : "0";
	    short h = (short) 0;
	    try { h = Short.parseShort(hnum); }
	    catch (Exception e) { Log.fatal("Invalid head number in DatalogTestRecord: " + hnum); }
	    headNumber = h;
	}

}
