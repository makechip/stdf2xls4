package com.makechip.stdf2xls4.stdf.enums.types;

public enum C1_t implements FieldType
{
    MODE_COD(" "),
    RTST_COD(" "),
    PROT_COD(" "),
    CMOD_COD(" "),
    DISP_COD(" "),
    HBIN_PF(" "),
    SBIN_PF(" "),
    WF_FLAT(" "),
    POS_X(" "),
    POS_Y(" "),
    TEST_TYP(" "),
    
    END(null);
    
    public final String defaultValue;
    
    private C1_t(String defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
