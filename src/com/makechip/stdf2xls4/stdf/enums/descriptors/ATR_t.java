package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.CN_t;
import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.types.U4_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum ATR_t implements FieldDescriptor
{
    MOD_TIM(U4,  null, true, U4_t.MOD_TIM, "Date and time of STDF file modification"),
    CMD_LINE(CN, null, true, CN_t.CMD_LINE, "Command line of program");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private ATR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
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
    public String getDescription()  { return(description); }
	@Override
    public FieldDescriptor getCountField() { return(countField); }
	@Override
    public boolean isRequired() { return(required); }
	@Override
	public FieldType getKind() { return(kind); }
}

