package com.makechip.stdf2xls4.stdf.enums.types;

public enum I4_t implements FieldType
{
	XFAIL_AD(null),
	YFAIL_AD(null),
	
    
    END(null);
    
    public final Integer defaultValue;
    
    private I4_t(Integer defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
