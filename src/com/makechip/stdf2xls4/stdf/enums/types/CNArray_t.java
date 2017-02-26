package com.makechip.stdf2xls4.stdf.enums.types;

public enum CNArray_t implements FieldType
{
    PGM_CHAR(""),
    RTN_CHAR(""),
    PGM_CHAL(""),
    RTN_CHAL(""),
    
    END(null);
    
    public final String defaultValue;
    
    private CNArray_t(String defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
