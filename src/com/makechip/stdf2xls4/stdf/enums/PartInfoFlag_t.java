package com.makechip.stdf2xls4.stdf.enums;

import java.util.EnumSet;

/**
 * This is an enum representing the bits in the PART_FLG field of a PartResultsRecord.
 * @author eric
 *
 */
public enum PartInfoFlag_t
{
	/**
	 *  Bit 0 - If set it means that this is a re-test, and the results
	 *  of this device supersede any previous sequence of records with the same part ID.
	 */
    RETEST_EXEC0(1),
    /**
     *  Bit 1 - If set it means that this is a re-test, and the results
     *  of this device supersede any previous sequence of records with the same XY-coordinate.
     */
    RETEST_EXEC1(2),
    /**
     *  Bit 2 - If set indicates an abnormal end of test.
     */
    ABNORMAL_END_OF_TEST(4),
    /**
     *  Bit 3 - If set indicates the device failed.
     */
    PART_FAILED(8),
    /**
     *  Bit 4 - If set indicates that pass/fail status is unknown, and Bit-3 is invalid.
     */
    NO_PASS_FAIL_INDICATION(16);

    /**
     *  The binary value of the bit position of this flag.
     *  For example, if the bit-position is 3, this will have a value of 8.
     */
    public final byte bit;
    
    private PartInfoFlag_t(int bit)
    {
    	this.bit = (byte) bit;
    }
    
	/**
	 * Given the STDF PART_FLG byte this method will return the
	 * enums that are set in the byte.
	 * @param b The PARM_FLAG byte.
	 * @return A Set of the enum values that are set in the PARM_FLAG byte.
	 */
    public static EnumSet<PartInfoFlag_t> getBits(final byte b)
    {
    	EnumSet<PartInfoFlag_t> set = EnumSet.noneOf(PartInfoFlag_t.class);
    	EnumSet<PartInfoFlag_t> all = EnumSet.allOf(PartInfoFlag_t.class);
        all.stream().filter(p -> (byte) (p.bit & b) == p.bit).forEach(x -> set.add(x));	
    	return(set);
    }

}
