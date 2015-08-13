package com.makechip.stdf2xls4.stdf.enums.types;

public enum I1_t implements FieldType
{
    RES_SCAL(null),
    LLM_SCAL(null),
    HLM_SCAL(null),
    
    END(null);
    
    public final Byte defaultValue;
    
    private I1_t(Byte defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
