package com.makechip.stdf2xls4.stdf.enums.types;

public enum R4Array_t implements FieldType
{
    RTN_RSLT(null),
    
    END(null);
    
    public final Float[] defaultValue;
    
    private R4Array_t(Float[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
