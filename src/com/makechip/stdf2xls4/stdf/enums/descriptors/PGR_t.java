package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;

import com.makechip.stdf2xls4.stdf.enums.types.CN_t;
import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U2Array_t;
import com.makechip.stdf2xls4.stdf.enums.types.U2_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PGR_t implements FieldDescriptor
{
    GRP_INDX(U2, null, true, U2_t.GRP_INDX, "Unique index associated with pin group"),
    GRP_NAM(CN,  null, true, CN_t.GRP_NAM, "Name of pin group"),
    INDX_CNT(U2, null, true, U2_t.INDX_CNT, "Count(k) of PMR indexes"),
    PMR_INDX(U2, INDX_CNT, false, U2Array_t.PMR_INDX, "Array of indexes for  pins in the group");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PGR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

