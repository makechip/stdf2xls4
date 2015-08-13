package com.makechip.stdf2xls4.stdf.enums.types;

public enum I2_t implements FieldType
{
    REC_TYP(null),
    REC_SUB(null),
    CPU_TYPE(null),
    STDF_VER(null),
    STAT_NUM(null),
    HEAD_NUM((short) 1),
    SITE_NUM((short) 1),
    SITE_GRP(null),
    SITE_CNT(null),
    WF_UNITS((short) 0),
    CENTER_X((short) -32768),
    CENTER_Y((short) -32768),
    X_COORD((short) -32768),
    Y_COORD((short) -32768),
    VECT_OFF(null),
    
    END(null);
    
    public final Short defaultValue;
    
    private I2_t(Short defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
