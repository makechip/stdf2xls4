package com.makechip.stdf2xls4.stdf.enums.types;

public enum U4_t implements FieldType
{
    MOD_TIM(null),
    SETUP_T(null),
    START_T(null),
    FINISH_T(null),
    PART_CNT(null),
    RTST_CNT(4294967295L),
    ABRT_CNT(4294967295L),
    GOOD_CNT(4294967295L),
    FUNC_CNT(4294967295L),
    HBIN_CNT(null),
    SBIN_CNT(null),
    TEST_T(0L),
    TEST_NUM(null),
    EXEC_CNT(4294967295L),
    FAIL_CNT(4294967295L),
    ALRM_CNT(4294967295L),
    CYCL_CNT(null),
    REL_VADR(null),
    REPT_CNT(null),
    NUM_FAIL(null),
    
    END(null);
    
    public final Long defaultValue;
    
    private U4_t(Long defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
