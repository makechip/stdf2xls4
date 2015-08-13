package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.B1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.BN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.I2;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PRR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    PART_FLG(B1, null, true, B1_t.PART_FLG, "Part information flag"),
    NUM_TEST(U2, null, true, U2_t.NUM_TEST, "Number of tests executed"),
    HARD_BIN(U2, null, true, U2_t.HARD_BIN, "Hardware bin number"),
    SOFT_BIN(U2, null, false, U2_t.SOFT_BIN, "Software bin number"),
    X_COORD(I2,  null, false, I2_t.X_COORD, "(Wafer) X coordinate"),
    Y_COORD(I2,  null, false, I2_t.Y_COORD, "(Wafer) Y coordinate"),
    TEST_T(U4,   null, false, U4_t.TEST_T, "Elapsed test time in milliseconds"),
    PART_ID(CN,  null, false, CN_t.PART_ID, "Part identification"),
    PART_TXT(CN, null, false, CN_t.PART_TXT, "Part description text"),
    PART_FIX(BN, null, false, BN_t.PART_FIX, "Part repair information");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PRR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

