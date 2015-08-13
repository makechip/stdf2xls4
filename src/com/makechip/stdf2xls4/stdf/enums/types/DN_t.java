package com.makechip.stdf2xls4.stdf.enums.types;

public enum DN_t implements FieldType
{
	FAIL_PIN(null),
	SPIN_MAP(null),
    
    END(null);
    
    public final byte[] defaultValue;
    
    private DN_t(byte[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
