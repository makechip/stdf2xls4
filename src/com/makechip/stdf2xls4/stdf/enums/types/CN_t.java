package com.makechip.stdf2xls4.stdf.enums.types;


public enum CN_t implements FieldType
{
    CMD_LINE(null),
    LOT_ID(null),
    PART_TYP(null),
    NODE_NAM(null),
    TSTR_TYP(null),
    JOB_NAM(null),
    JOB_REV(""),
    SBLOT_ID(""),
    OPER_NAM(""),
    EXEC_TYP(""),
    EXEC_VER(""),
    TEST_COD(""),
    TST_TEMP(""),
    USER_TXT(""),
    AUX_FILE(""),
    PKG_TYP(""),
    FAMLY_ID(""),
    DATE_COD(""),
    FACIL_ID(""),
    FLOOR_ID(""),
    PROC_ID(""),
    OPER_FRQ(""),
    SPEC_NAM(""),
    SPEC_VER(""),
    FLOW_ID(""),
    SETUP_ID(""),
    DSGN_REV(""),
    ENG_ID(""),
    ROM_COD(""),
    SERL_NUM(""),
    SUPR_NAM(""),
    USR_DESC(""),
    EXC_DESC(""),
    HBIN_NAM(""),
    SBIN_NAM(""),
    CHAN_NAM(""),
    PHY_NAM(""),
    LOG_NAM(""),
    GRP_NAM(""),
    HAND_TYP(""),
    HAND_ID(""),
    CARD_TYP(""),
    CARD_ID(""),
    LOAD_TYP(""),
    LOAD_ID(""),
    DIB_TYP(""),
    DIB_ID(""),
    CABL_TYP(""),
    CABL_ID(""),
    CONT_TYP(""),
    CONT_ID(""),
    LASR_TYP(""),
    LASR_ID(""),
    EXTR_TYP(""),
    EXTR_ID(""),
    WAFER_ID(""),
    FABWF_ID(""),
    FRAME_ID(""),
    MASK_ID(""),
    PART_ID(""),
    PART_TXT(""),
    TEST_NAM(""),
    SEQ_NAME(""),
    TEST_LBL(""),
    TEST_TXT(""),
    ALARM_ID(""),
    UNITS(""),
    C_RESFMT(""),
    C_LLMFMT(""),
    C_HLMFMT(""),
    UNITS_IN(""),
    VECT_NAM(""),
    TIME_SET(""),
    OP_CODE(""),
    PROG_TXT(""),
    RSLT_TXT(""),
    TEXT_DAT(null),
    
    END(null);
    
    public final String defaultValue;
    
    private CN_t(String defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
