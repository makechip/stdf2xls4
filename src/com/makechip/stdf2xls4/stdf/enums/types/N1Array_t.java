package com.makechip.stdf2xls4.stdf.enums.types;

public enum N1Array_t implements FieldType
{
    RTN_STAT(null),
    PGM_STAT(null),
    
    END(null);
    
    public final byte[] defaultValue;
    
    private N1Array_t(byte[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
