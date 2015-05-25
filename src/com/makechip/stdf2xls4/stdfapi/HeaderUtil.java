package com.makechip.stdf2xls4.stdfapi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdf.DatalogTextRecord;
import com.makechip.stdf2xls4.stdf.MasterInformationRecord;
import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.WaferInformationRecord;

public class HeaderUtil
{
    public static final String HEADER_TAG = ">>> ";
    public static final String WAFER_ID = "WAFER_ID:";
    public static final String TESTER_TYPE = "TESTER_TYPE:";

    public static final String CUSTOMER = "CUSTOMER:";
    public static final String DEVICE_NUMBER = "DEVICE NUMBER:";
    public static final String SOW = "SOW:";
    public static final String DATE_CODE = "DATE_CODE:";
    public static final String CUSTOMER_PO = "CUSTOMER PO#:";
    public static final String TESTER = "TESTER:";
    public static final String TEST_PROGRAM = "TEST PROGRAM:";
    public static final String LOADBOARD = "LOADBOARD:";
    public static final String CONTROL_SERIALS = "CONTROL SERIAL #S:";
    public static final String JOB = "JOB #:";
    public static final String LOT = "LOT #:";
    public static final String STEP = "STEP #:";
    public static final String TEMPERATURE = "TEMPERATURE:";
    public final HashMap<String, String> header;
    private final List<String> legacyTags;

	public HeaderUtil()
	{
		header = new HashMap<>();
		legacyTags = new ArrayList<>();
		legacyTags.add(CUSTOMER);
		legacyTags.add(DEVICE_NUMBER);
		legacyTags.add(SOW);
		legacyTags.add(DATE_CODE);
		legacyTags.add(CUSTOMER_PO);
		legacyTags.add(TESTER);
		legacyTags.add(TEST_PROGRAM);
		legacyTags.add(LOADBOARD);
		legacyTags.add(CONTROL_SERIALS);
		legacyTags.add(JOB);
		legacyTags.add(LOT);
		legacyTags.add(STEP);
		legacyTags.add(TEMPERATURE);
	}
	
	public void setHeader(StdfRecord r)
	{
        if (r instanceof DatalogTextRecord)
        {
            DatalogTextRecord dtr = DatalogTextRecord.class.cast(r);
            String t = dtr.text;
            if (t.contains(HEADER_TAG))
            {
                String s = t.substring(HEADER_TAG.length()).trim();
                StringTokenizer st = new StringTokenizer(s, ":");
                if (!st.hasMoreTokens()) throw new RuntimeException("Invalid header item: " + t);
                String label = st.nextToken();
                if (!st.hasMoreTokens()) throw new RuntimeException("Invalid header item: " + t);
                String value = st.nextToken();
                header.put(label.trim(), value.trim());
            }
            else
            {
            	String tag = legacyTags.stream().filter(s -> t.toUpperCase().contains(s)).findFirst().orElse(null);
            	if (tag != null) header.put(tag, getTextField(tag, t));
            }
            return;
        }
        else if (r instanceof MasterInformationRecord)
        {
        	MasterInformationRecord mir = MasterInformationRecord.class.cast(r);
        	header.put(TESTER, mir.nodeName);
        	header.put(JOB, mir.jobRevisionNumber);
        	header.put(TESTER_TYPE, mir.testerType);
        	header.put(TEST_PROGRAM, mir.jobName);
        }
        else if (r instanceof WaferInformationRecord)
        {
        	WaferInformationRecord wir = WaferInformationRecord.class.cast(r);
        	header.put(WAFER_ID, wir.waferID);
        }
	}
	
	
	
	
	
	
	
	private String getTextField(String key, String text)
    {
        if (text.toUpperCase().contains(key.toUpperCase()))
        {
            int i = text.toUpperCase().indexOf(key.toUpperCase());
            int l = key.length();
            String a = text.substring(i+l+1);
            String f = null;
            if (a.indexOf('\n') > 0) f = a.substring(0, a.indexOf('\n')).trim();
            else f = a.trim();
            if (f.indexOf('@') > 0) f = f.substring(0, f.indexOf('@')).trim();
            return(f);
        }
        return(null);
    }
    


}
