package com.makechip.stdf2xls4.stdf.enums.types;

public enum U1_t implements FieldType
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
    PATG_NUM((short) 255),
    
    END(null);
    
    public final Short defaultValue;
    
    private U1_t(Short defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
