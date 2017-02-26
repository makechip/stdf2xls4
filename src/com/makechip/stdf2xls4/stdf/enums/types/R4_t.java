package com.makechip.stdf2xls4.stdf.enums.types;

public enum R4_t implements FieldType
{
    WAFR_SIZ(0.0f),
    DIE_HT(0.0f),
    DIE_WID(0.0f),
    TEST_TIM(null),
    TEST_MIN(null),
    TEST_MAX(null),
    TST_SUMS(null),
    TST_SQRS(null),
    RESULT(null),
    LO_LIMIT(null),
    HI_LIMIT(null),
    LO_SPEC(null),
    HI_SPEC(null),
    START_IN(null),
    INCR_IN(null),
    
    END(null);
    
    public final Float defaultValue;
    
    private R4_t(Float defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
