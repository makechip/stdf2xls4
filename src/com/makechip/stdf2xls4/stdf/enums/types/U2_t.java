package com.makechip.stdf2xls4.stdf.enums.types;

public enum U2_t implements FieldType
{
    REC_LEN(null),
    BURN_TIM(65535), 
    HBIN_NUM(null),
    SBIN_NUM(null),
    PMR_INDX(null),
    CHAN_TYP(0),
    GRP_INDX(null),
    INDX_CNT(null),
    GRP_CNT(null),
    NUM_BINS(null),
    NUM_TEST(null),
    HARD_BIN(null),
    SOFT_BIN(65535),
    RTN_ICNT(0),
    RSLT_CNT(0),
    PGM_ICNT(0),    
    FLD_CNT(null),
     
    END(null);
    
    public final Integer defaultValue;
    
    private U2_t(Integer defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
