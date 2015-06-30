package com.makechip.stdf2xls4.stdf;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 * This is the superclass for any StdfRecord that represents a test result.
 * @author eric
 *
 */
public abstract class TestRecord extends StdfRecord 
{
	/**
	 * This CTOR gets the head number and site number from the binary stream data.
	 * @param type The record type.  Required by the StdfRecord constructor.
	 * @param cpuType The CPU type.  Required for byte to number conversions.
	 * @param data The binary stream data for this record; required by the StdfRecord constructor.
	 */
	protected TestRecord(Record_t type, Cpu_t cpuType, byte[] data)
	{
		super(type, cpuType, data);
	}

	public abstract TestID getTestId();
}
