package com.makechip.stdf2xls4.stdf;

import java.util.EnumSet;

public enum PartInfoFlag_t
{
    RETEST_EXEC0(1),
    RETEST_EXEC1(2),
    ABNORMAL_END_OF_TEST(4),
    PART_FAILED(8),
    NO_PASS_FAIL_INDICATION(16);
    
    private final byte bit;
    
    private PartInfoFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
    public byte getBit() { return(bit); }
    
    public static EnumSet<PartInfoFlag_t> getBits(byte b)
    {
    	EnumSet<PartInfoFlag_t> set = EnumSet.noneOf(PartInfoFlag_t.class);
    	if ((b & (byte) 1)  == (byte) 1)  set.add(RETEST_EXEC0);
    	if ((b & (byte) 2)  == (byte) 2)  set.add(RETEST_EXEC1);
    	if ((b & (byte) 4)  == (byte) 4)  set.add(ABNORMAL_END_OF_TEST);
    	if ((b & (byte) 8)  == (byte) 8)  set.add(PART_FAILED);
    	if ((b & (byte) 16) == (byte) 16) set.add(NO_PASS_FAIL_INDICATION);
    	return(set);
    }

}
