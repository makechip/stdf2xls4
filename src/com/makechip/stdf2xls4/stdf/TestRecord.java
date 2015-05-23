package com.makechip.stdf2xls4.stdf;

import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

public abstract class TestRecord extends StdfRecord 
{
	public final long testNumber;
	public final short headNumber;
	public final short siteNumber;
	public final String testName;
	protected final String valueText;
	
	protected TestRecord(Record_t type, int sequenceNumber, int devNum, long testNumber, 
			             short headNumber, short siteNumber, String testName, byte[] data)
	{
		super(type, sequenceNumber, devNum, data);
		this.testNumber = testNumber;
		this.headNumber = headNumber;
		this.siteNumber = siteNumber;
		this.testName = testName;
		valueText = null;
	}
	
	protected TestRecord(int sequenceNumber, int devNum, byte[] data)
	{
		super(Record_t.DTR, sequenceNumber, devNum, data);
		String text = getCn();
		StringTokenizer st = new StringTokenizer(text, ": \t");
	    st.nextToken(); // burn TEXT_DATA
	    testName = st.nextToken();
	    valueText = st.nextToken();
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
