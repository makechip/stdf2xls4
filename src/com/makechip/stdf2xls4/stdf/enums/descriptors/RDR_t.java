package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;

import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U2Array_t;
import com.makechip.stdf2xls4.stdf.enums.types.U2_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum RDR_t implements FieldDescriptor
{
    NUM_BINS(U2, null, true, U2_t.NUM_BINS, "Number (k) of bins being retested"),
    RTST_BIN(U2, NUM_BINS, false, U2Array_t.RTST_BIN, "Array of retest bin numbers");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private RDR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

