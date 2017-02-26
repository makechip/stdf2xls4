package com.makechip.stdf2xls4.stdf.enums.types;

public enum BN_t implements FieldType
{
	PART_FIX(new byte[0]),
    
    END(null);
    
    public final byte[] defaultValue;
    
    private BN_t(byte[] defaultValue)
    {
    	this.defaultValue = defaultValue;
    }
}
