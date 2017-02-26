package com.makechip.stdf2xls4.stdf.enums.types;

public enum B1_t implements FieldType
{
	PART_FLG(null),
	OPT_FLAG(null),
	TEST_FLG(null),
	PARM_FLG(null),
    
    END(null);
    
    public final Byte defaultValue;
    
    private B1_t(Byte defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
