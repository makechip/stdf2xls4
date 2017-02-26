package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.B1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.C1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.R4;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum TSR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    TEST_TYP(C1, null, true, C1_t.TEST_TYP, "Test type"),
    TEST_NUM(U4, null, true, U4_t.TEST_NUM, "Test number"),
    EXEC_CNT(U4, null, false, U4_t.EXEC_CNT, "Number of test executions"),
    FAIL_CNT(U4, null, false, U4_t.FAIL_CNT, "Number of test failures"),
    ALRM_CNT(U4, null, false, U4_t.ALRM_CNT, "Number of alarmed tests"),
    TEST_NAM(CN, null, false, CN_t.TEST_NAM, "Test name"),
    SEQ_NAME(CN, null, false, CN_t.SEQ_NAME, "Sequencer (program segment/flow) name"),
    TEST_LBL(CN, null, false, CN_t.TEST_LBL, "Test label or text"),
    OPT_FLAG(B1, null, false, B1_t.OPT_FLAG, "Optional data flag"),
    TEST_TIM(R4, null, false, R4_t.TEST_TIM, "Average test execution time in seconds"),
    TEST_MIN(R4, null, false, R4_t.TEST_MIN, "Lowest test result value"),
    TEST_MAX(R4, null, false, R4_t.TEST_MAX, "Highest test result value"),
    TST_SUMS(R4, null, false, R4_t.TST_SUMS, "Sum of test result values"),
    TST_SQRS(R4, null, false, R4_t.TST_SQRS, "Sum of squares of test result values");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private TSR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

