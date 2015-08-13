package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.B1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.I1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.R4;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PTR_t implements FieldDescriptor
{
    TEST_NUM(U4, null, true, U4_t.TEST_NUM, "Test number"),
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    TEST_FLG(B1, null, true, B1_t.TEST_FLG, "Test flags"),
    PARM_FLG(B1, null, true, B1_t.PARM_FLG, "Parametric flags (drift, etc.)"),
    RESULT(R4,   null, false, R4_t.RESULT, "Test result"),
    TEST_TXT(CN, null, false, CN_t.TEST_TXT, "Test description text or label"),
    ALARM_ID(CN, null, false, CN_t.ALARM_ID, "Name of alarm"),
    OPT_FLAG(B1, null, false, B1_t.OPT_FLAG, "Optional data flag"),
    RES_SCAL(I1, null, false, I1_t.RES_SCAL, "Test results scaling exponent"),
    LLM_SCAL(I1, null, false, I1_t.LLM_SCAL, "Low limit scaling exponent"),
    HLM_SCAL(I1, null, false, I1_t.HLM_SCAL, "High limit scaling exponent"),
    LO_LIMIT(R4, null, false, R4_t.LO_LIMIT, "Low test limit value"),
    HI_LIMIT(R4, null, false, R4_t.HI_LIMIT, "High test limit value"),
    UNITS(CN,    null, false, CN_t.UNITS, "Test units"),
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

    private PTR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

