package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;

import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U1_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PIR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_NUM(U1, null, true, U1_t.SITE_NUM, "Test site number");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PIR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

