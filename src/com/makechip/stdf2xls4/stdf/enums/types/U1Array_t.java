package com.makechip.stdf2xls4.stdf.enums.types;

public enum U1Array_t implements FieldType
{
    PMR_RADX(new short[0]),
    GRP_RADX(null),
    SITE_NUM(null);
    
    public final short[] defaultValue;
    
    private U1Array_t(short[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
