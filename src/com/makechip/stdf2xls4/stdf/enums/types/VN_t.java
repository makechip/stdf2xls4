package com.makechip.stdf2xls4.stdf.enums.types;

public enum VN_t implements FieldType
{
    GEN_DATA(null), 
    END(null);
    
    public final Object[] defaultValue;
    
    private VN_t(Object[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
