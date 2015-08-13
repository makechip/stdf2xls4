package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.C1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.I2;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.R4;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum WCR_t implements FieldDescriptor
{
    WAFR_SIZ(R4, null, false, R4_t.WAFR_SIZ, "Diameter of wafer"),
    DIE_HT(R4,   null, false, R4_t.DIE_HT, "Height of die"),
    DIE_WID(R4,  null, false, R4_t.DIE_WID, "Width of die"),
    WF_UNITS(U1, null, false, U1_t.WF_UNITS, "Units for wafer and die dimensions"),
    WF_FLAT(C1,  null, false, C1_t.WF_FLAT, "Orientation of wafer flat"),
    CENTER_X(I2, null, false, I2_t.CENTER_X, "X coordinate of center die on wafer"),
    CENTER_Y(I2, null, false, I2_t.CENTER_Y, "Y coordinate of center die on wafer"),
    POS_X(C1,    null, false, C1_t.POS_X, "Positive X direction on wafer"),
    POS_Y(C1,    null, false, C1_t.POS_Y, "Positive Y direction on wafer");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private FieldType kind;

    private WCR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

