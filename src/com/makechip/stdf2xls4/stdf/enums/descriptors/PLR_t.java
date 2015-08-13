package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PLR_t implements FieldDescriptor
{
    GRP_CNT(U2, null, true, U2_t.GRP_CNT, "Count (k) og pins or pin groups"),
    GRP_INDX(U2, GRP_CNT, true, U2Array_t.GRP_INDX, "Array of pin or pin group indexes"),
    GRP_MODE(U2, GRP_CNT, false, U2Array_t.GRP_MODE, "Operating mode of pin group"),
    GRP_RADX(U1, GRP_CNT, false, U1Array_t.GRP_RADX, "Display radix of pin group"),
    PGM_CHAR(CN, GRP_CNT, false, CNArray_t.PGM_CHAR, "Program state encoding characters"),
    RTN_CHAR(CN, GRP_CNT, false, CNArray_t.RTN_CHAR, "Return state encoding characters"),
    PGM_CHAL(CN, GRP_CNT, false, CNArray_t.PGM_CHAL, "Program state encoding characters"),
    RTN_CHAL(CN, GRP_CNT, false, CNArray_t.RTN_CHAL, "Return state encoding characters");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PLR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

