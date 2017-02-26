package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.*;

import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.types.*;

public enum FTR_t implements FieldDescriptor
{
    TEST_NUM(U4, null, true, U4_t.TEST_NUM, "Test number"),
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    TEST_FLG(B1, null, true, B1_t.TEST_FLG, "Test flags"),
    OPT_FLAG(B1, null, false, B1_t.OPT_FLAG, "Optional data flag"),
    CYCL_CNT(U4, null, false, U4_t.CYCL_CNT, "Cycle count of vector"),
    REL_VADR(U4, null, false, U4_t.REL_VADR, "Relative vector address"),
    REPT_CNT(U4, null, false, U4_t.REPT_CNT, "Repeat count of vector"),
    NUM_FAIL(U4, null, false, U4_t.NUM_FAIL, "Number of pins with 1 or more failures"),
    XFAIL_AD(I4, null, false, I4_t.XFAIL_AD, "X logical device failure address"),
    YFAIL_AD(I4, null, false, I4_t.YFAIL_AD, "Y logical device failure address"),
    VECT_OFF(I2, null, false, I2_t.VECT_OFF, "Offset from vector of interest"),
    RTN_ICNT(U2, null, false, U2_t.RTN_ICNT, "Count (j) of return data PMR indexes"),
    PGM_ICNT(U2, null, false, U2_t.PGM_ICNT, "Count (k) of programmed state indexes"),
    RTN_INDX(U2, RTN_ICNT, false, U2Array_t.RTN_INDX, "Array of return data PMR indexes"),
    RTN_STAT(N1, RTN_ICNT, false, N1Array_t.RTN_STAT, "Array of returned states"),
    PGM_INDX(U2, PGM_ICNT, false, U2Array_t.PGM_INDX, "Array of programmed state indexes"),
    PGM_STAT(N1, PGM_ICNT, false, N1Array_t.PGM_STAT, "Array of programmed states"),
    FAIL_PIN(DN, null, false, DN_t.FAIL_PIN, "Failing pin bitfield"),
    VECT_NAM(CN, null, false, CN_t.VECT_NAM, "Vector module pattern name"),
    TIME_SET(CN, null, false, CN_t.TIME_SET, "Time set name"),
    OP_CODE(CN,  null, false, CN_t.OP_CODE, "Vector Op Code"),
    TEST_TXT(CN, null, false, CN_t.TEST_TXT, "Descriptive text or label"),
    ALARM_ID(CN, null, false, CN_t.ALARM_ID, "Name of alarm"),
    PROG_TXT(CN, null, false, CN_t.PROG_TXT, "Additional programmed information"),
    RSLT_TXT(CN, null, false, CN_t.RSLT_TXT, "Additional result information"),
    PATG_NUM(U1, null, false, U1_t.PATG_NUM, "Pattern generator number"),
    SPIN_MAP(DN, null, false, DN_t.SPIN_MAP, "Bitmap of enabled comparators");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private FTR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
    {
        this.type = type;
        this.countField = countField;
        this.required = required;
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

