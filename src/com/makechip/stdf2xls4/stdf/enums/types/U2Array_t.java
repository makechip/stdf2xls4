package com.makechip.stdf2xls4.stdf.enums.types;

public enum U2Array_t implements FieldType
{
    PMR_INDX(new int[0]),
    GRP_INDX(null),
    GRP_MODE(new int[0]),
    RTST_BIN(null),
    RTN_INDX(new int[0]),
    PGM_INDX(new int[0]),
    
    END(null);
    
    public final int[] defaultValue;
    
    private U2Array_t(int[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
