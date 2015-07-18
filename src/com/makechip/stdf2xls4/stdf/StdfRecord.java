package com.makechip.stdf2xls4.stdf;

import gnu.trove.list.array.TByteArrayList;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 * This is the superclass for all StdfRecord Types.  
 * Notes on optional / missing / invalid fields:
 * 1. Field that are invalid may either have some default value
 *    or else a flag that indicates that a specific field is invalid.
 *    This may imply that the tester is indicating that the data is
 *    just missing.  The STDF reader makes no assumptions about
 *    these fields, and just loads them.
 * 2. Optional data - some records may be truncated so save space.
 *    The truncation may only occur at the end of a record, and only
 *    if all remaining fields are optional.  Required fields in
 *    StdfRecords are stored in primitive types, while optional
 *    fields are stored in primitive wrapper types.  This way
 *    if the fields are missing from a record, they are loaded
 *    as null values.  This way if a field is null, it means the
 *    field was not in the STDF byte stream.
 * @author eric
 */
public abstract class StdfRecord
{
    public static final String TEXT_DATA          = "TEXT_DATA";
    public static final String SERIAL_MARKER      = "S/N";
	
	/**
	 * The constructor for an STDF record.
	 */
	protected StdfRecord()
	{
	}
	
	
	/**
	 * This method converts the field values to the byte array used by the constructor.
	 * It is used to take a record initialized by field values, and load it
	 * into the byte array.
	 */
	public abstract byte[] getBytes(Cpu_t cpu);
	
	protected TByteArrayList getHeaderBytes(Cpu_t cpu, Record_t rt, int recLen)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpu.getU4Bytes(recLen));
		l.add(cpu.getU1Bytes(rt.recordType));
		l.add(cpu.getU1Bytes(rt.recordSubType));
	    return(l);	
	}

}
