package com.makechip.stdf2xls4.stdf;

import java.lang.reflect.Field;
import java.util.Arrays;

import gnu.trove.list.array.TByteArrayList;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;


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
		l.addAll(cpu.getU2Bytes(recLen));
		l.add(cpu.getU1Bytes(rt.recordType));
		l.add(cpu.getU1Bytes(rt.recordSubType));
	    return(l);	
	}
	
	@Override
	public final String toString()
	{
		StringBuilder sb = new StringBuilder();
	    Field[] fs = getClass().getDeclaredFields();	
	    sb.append(getClass().getSimpleName() + ":").append(Log.eol);
	    try
	    {
	    	if (this instanceof ParametricRecord)
	    	{
	    		Field[] gs = getClass().getSuperclass().getDeclaredFields();
	    		for (Field g : gs)
	    		{
	    			g.setAccessible(true);
	    			Object o = g.get(this);
	    			sb.append("    ").append(g.getName()).append(" = ").append((o == null) ? "null" : o.toString()).append(Log.eol);
	    		}
	    	}
	    	for (Field f : fs)
	    	{
	    		f.setAccessible(true);
	    		Object o = f.get(this);
	    		if (o != null && o.getClass().isArray())
	    		{
	    			String arrayContents = null;
	    			if (f.getType() == byte[].class) arrayContents = Arrays.toString((byte[]) o);
	    			else if (f.getType() == short[].class) arrayContents = Arrays.toString((short[]) o);
	    			else if (f.getType() == int[].class) arrayContents = Arrays.toString((int[]) o);
	    			else if (f.getType() == float[].class) arrayContents = Arrays.toString((float[]) o);
	    			else if (f.getType() == boolean[].class) arrayContents = Arrays.toString((boolean[]) o);
	    			else arrayContents = Arrays.toString((Object[]) o);
	    			sb.append("    ").append(f.getName()).append(" = ").append(arrayContents).append(Log.eol);
	    		}
	    		else sb.append("    ").append(f.getName()).append(" = ").append((o == null) ? "null" : o.toString()).append(Log.eol);
	    	}
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	throw new RuntimeException("Bogus error in toString() method: " + e.getMessage());
	    }
	    sb.append(Log.eol);
	    return(sb.toString());
	}

}
