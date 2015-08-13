package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum PMR_t implements FieldDescriptor
{
    PMR_INDX(U2, null, true, U2_t.PMR_INDX, "Unique index associated with pin"),
    CHAN_TYP(U2, null, false, U2_t.CHAN_TYP, "Channel type"),
    CHAN_NAM(CN, null, false, CN_t.CHAN_NAM, "Channel name"),
    PHY_NAM(CN,  null, false, CN_t.PHY_NAM, "Physical name of pin"),
    LOG_NAM(CN,  null, false, CN_t.LOG_NAM, "Logical name of pin"),
    HEAD_NUM(U1, null, false, U1_t.HEAD_NUM, "Head number associated with channel"),
    SITE_NUM(U1, null, false, U1_t.SITE_NUM, "Site number associated with channel");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private PMR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

