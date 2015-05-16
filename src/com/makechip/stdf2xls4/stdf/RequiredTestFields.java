package com.makechip.stdf2xls4.stdf;

import static com.makechip.stdf2xls4.stdf.TestFlag_t.ABORT;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.ALARM;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.FAIL;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.NOT_EXECUTED;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.NO_PASS_FAIL;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.TIMEOUT;
import static com.makechip.stdf2xls4.stdf.TestFlag_t.UNRELIABLE;

import java.util.EnumSet;

import com.makechip.util.Log;

class RequiredTestFields
{
    private final long testNumber;
    private final short headNumber;
    private final short siteNumber;
    private EnumSet<TestFlag_t> testFlags;

	public RequiredTestFields(StdfRecord rec)
	{
		testNumber = rec.getU4(StdfRecord.MISSING_INT);
	    headNumber = rec.getU1((short) 0);	
	    siteNumber = rec.getU1((short) 0);	
	    testFlags = TestFlag_t.getBits(rec.getByte());
	}
	
	public RequiredTestFields(long testNumber, int headNumber, int siteNumber, EnumSet<TestFlag_t> testFlags)
	{
		this.testNumber = testNumber;
		this.headNumber = (short) headNumber;
		this.siteNumber = (short) siteNumber;
		this.testFlags = testFlags;
	}
	
	public RequiredTestFields(long testNumber, int headNumber, int siteNumber, byte testFlags)
	{
		this(testNumber, headNumber, siteNumber, TestFlag_t.getBits(testFlags));
	}
	public long getTestNumber() { return(testNumber); }
	
	public short getHeadNumber() { return(headNumber); }
	
	public short getSiteNumber() { return(siteNumber); }
	
    public boolean alarm() { return(testFlags.contains(ALARM)); }
    public boolean unreliable() { return(testFlags.contains(UNRELIABLE)); }
    public boolean timeout() { return(testFlags.contains(TIMEOUT)); }
    public boolean notExecuted() { return(testFlags.contains(NOT_EXECUTED)); }
    public boolean abort() { return(testFlags.contains(ABORT)); }
    public boolean noPassFailIndication() { return(testFlags.contains(NO_PASS_FAIL)); }
    public boolean fail() { return(testFlags.contains(FAIL)); }

    public EnumSet<TestFlag_t> getTestFlags() { return(testFlags); }
    
    @Override 
    public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("    testNumber: " + testNumber).append(Log.eol);
    	sb.append("    headNumber: " + headNumber).append(Log.eol);
    	sb.append("    siteNumber: " + siteNumber).append(Log.eol);
    	sb.append("    testFlags:");
    	testFlags.stream().forEach(p -> sb.append(" ").append(p.toString()));
    	sb.append(Log.eol).append(Log.eol);
    	return(sb.toString());
    }
}
