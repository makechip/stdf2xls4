package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.C1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.C1_t;
import com.makechip.stdf2xls4.stdf.enums.types.CN_t;
import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U4_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum MRR_t implements FieldDescriptor
{
    FINISH_T(U4, null, true, U4_t.FINISH_T, "Date and time last part tested"),
    DISP_COD(C1, null, false, C1_t.DISP_COD, "Lot disposition code"),
    USR_DESC(CN, null, false, CN_t.USR_DESC, "Lot description supplied by user"),
    EXC_DESC(CN, null, false, CN_t.EXC_DESC, "Lot description supplied by exec");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private MRR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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

