package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.VN;

import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U2_t;
import com.makechip.stdf2xls4.stdf.enums.types.VN_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum GDR_t implements FieldDescriptor
{
    FLD_CNT(U2, null, true, U2_t.FLD_CNT, "Count of data fields in record"),
    GEN_DATA(VN, FLD_CNT, true, VN_t.GEN_DATA, "Data type code and data for one field");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private GDR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

