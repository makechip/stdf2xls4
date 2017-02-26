package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U1_t;
import com.makechip.stdf2xls4.stdf.enums.types.U4_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PCR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number"),
    PART_CNT(U4, null, true, U4_t.PART_CNT, "Number of parts tested"),
    RTST_CNT(U4, null, false, U4_t.RTST_CNT, "Number of parts retested"),
    ABRT_CNT(U4, null, false, U4_t.ABRT_CNT, "Number of aborts during testing"),
    GOOD_CNT(U4, null, false, U4_t.GOOD_CNT, "Number of good (passed) parts tested"),
    FUNC_CNT(U4, null, false, U4_t.FUNC_CNT, "Number of functional parts tested");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PCR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

