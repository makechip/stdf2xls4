package com.makechip.stdf2xls4.excel;

import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdfapi.HeaderUtil;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.util.Identity;
import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryONNZ;

public final class SheetName implements Identity, Immutable
{
	public static final String WAFER_PREFIX = "  WAFER ";
	public static final String STEP_PREFIX = "  STEP ";
	public final boolean wafersort;
	public final int version;
	public final String name;
	public final int page;
	private static final String nullKey = "NONE";
	private static IdentityFactoryONNZ<String, SheetName> map = new IdentityFactoryONNZ<>(String.class, SheetName.class);

	private SheetName(String name, int page, int version, boolean wafersort)
	{
		this.name = name;
		this.page = page;
		this.version = version;
		this.wafersort = wafersort;
	}
	
	public static SheetName getSheet(boolean wafersort, PageHeader hdr, int page, int version)
	{
	    String waferOrStep = getWaferOrStepName(wafersort, hdr);
	    if (waferOrStep == null) waferOrStep = nullKey;
        return(map.getValue(waferOrStep, page, version, wafersort));
	}
	
	public static SheetName getSheet(String sheetName)
	{
	    StringTokenizer st = new StringTokenizer(sheetName);
	    if (st.countTokens() != 5) throw new RuntimeException("Invalid sheet name " + sheetName);
	    String prefix = st.nextToken();
	    boolean wafersort = prefix.contains("WAFER");
	    String waferOrStep = st.nextToken();
	    if (waferOrStep.equals("null")) waferOrStep = null;
	    st.nextToken(); // burn "Page";
	    String pnum = st.nextToken();
	    String vnum = st.nextToken();
	    int p = 0;
	    try { p = Integer.parseInt(pnum); } 
	    catch (Exception e) { throw new RuntimeException(e.getMessage()); }
	    int v = 0;
	    try { v = Integer.parseInt(vnum.substring(1)); }
	    catch (Exception e) { throw new RuntimeException(e.getMessage()); }
	    if (waferOrStep == null) waferOrStep = nullKey; // null keys don't work
	    return(map.getValue(waferOrStep, p, v, wafersort));
	}
	
	public static SheetName getExistingSheetName(boolean wafersort, PageHeader hdr, int page, int version)
	{
	    String waferOrStep = getWaferOrStepName(wafersort, hdr);
	    if (waferOrStep == null) waferOrStep = nullKey;
        return(map.getExistingValue(waferOrStep, page, version, wafersort));
	}

    private static String getWaferOrStepName(boolean wsort, PageHeader hdr)
    {
        if (hdr == null) return(null);
    	return(wsort ? hdr.get(HeaderUtil.WAFER_ID) : hdr.get(HeaderUtil.STEP));
    }
    
	@Override
	public int getInstanceCount()
	{
		return(map.getInstanceCount());
	}
	
	@Override
	public String toString()
	{
		String s = wafersort ? WAFER_PREFIX + name : STEP_PREFIX + name;
		return(s + " Page " + page + " V" + version);
	}

}
