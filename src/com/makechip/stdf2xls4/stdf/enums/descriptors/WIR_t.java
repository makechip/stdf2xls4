package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.CN_t;
import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U1_t;
import com.makechip.stdf2xls4.stdf.enums.types.U4_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum WIR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_GRP(U1, null, true, U1_t.SITE_GRP, "Site group number"),
    START_T(U4,  null, true, U4_t.START_T, "Date and time first part tested"),
    WAFER_ID(CN, null, false, CN_t.WAFER_ID, "Wafer ID");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private WIR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

