package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.*;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum MPR_t implements FieldDescriptor
{
    TEST_NUM(U4, null, true, U4_t.TEST_NUM, "Test number"),
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    TEST_FLG(B1, null, true, B1_t.TEST_FLG, "Test flags"),
    PARM_FLG(B1, null, true, B1_t.PARM_FLG, "Parametric flags (drift, etc.)"),
    RTN_ICNT(U2, null, true, U2_t.RTN_ICNT, "Count (j) of PMR indexes"),
    RSLT_CNT(U2, null, true, U2_t.RSLT_CNT, "Count (k) of returned results"),
    RTN_STAT(N1, RTN_ICNT, false, N1Array_t.RTN_STAT, "Array of returned states"),
    RTN_RSLT(R4, RSLT_CNT, false, R4Array_t.RTN_RSLT, "Array of returned results"),
    TEST_TXT(CN, null, false, CN_t.TEST_TXT, "Test description text or label"),
    ALARM_ID(CN, null, false, CN_t.ALARM_ID, "Name of alarm"),
    OPT_FLAG(B1, null, false, B1_t.OPT_FLAG, "Optional data flag"),
    RES_SCAL(I1, null, false, I1_t.RES_SCAL, "Test results scaling exponent"),
    LLM_SCAL(I1, null, false, I1_t.LLM_SCAL, "Lfalseow limit scaling exponent"),
    HLM_SCAL(I1, null, false, I1_t.HLM_SCAL, "High limit scaling exponent"),
    LO_LIMIT(R4, null, false, R4_t.LO_LIMIT, "Low test limit value"),
    HI_LIMIT(R4, null, false, R4_t.HI_LIMIT, "High test limit value"),
    START_IN(R4, null, false, R4_t.START_IN, "Starting input value (condition)"),
    INCR_IN(R4,  null, false, R4_t.INCR_IN, "Increment of input condition"),
    RTN_INDX(U2, RTN_ICNT, false, U2Array_t.RTN_INDX, "Array of PMR indexes"),
    UNITS(CN,    null, false, CN_t.UNITS, "Test units"),
    UNITS_IN(CN, null, false, CN_t.UNITS_IN, "Input condition units"),
    C_RESFMT(CN, null, false, CN_t.C_RESFMT, "ANSI C result format string"),
    C_LLMFMT(CN, null, false, CN_t.C_LLMFMT, "ANSI C low limit format string"),
    C_HLMFMT(CN, null, false, CN_t.C_HLMFMT, "ANSI C high limit format string"),
    LO_SPEC(R4,  null, false, R4_t.LO_SPEC, "Low specification limit value"),
    HI_SPEC(R4,  null, false, R4_t.HI_SPEC, "High specification limit value");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private MPR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
    {
        this.type = type;
        this.required = required;
        this.countField = countField;
        this.description = description;
        this.kind = kind;
    }

	@Override
    public Data_t getType()         { return(type); }
	@Override
    public boolean isRequired()     { return(required); }
	@Override
    public String getDescription()  { return(description); }
	@Override
    public FieldDescriptor getCountField() { return(countField); }
	@Override
	public FieldType getKind() { return(kind); }
}

