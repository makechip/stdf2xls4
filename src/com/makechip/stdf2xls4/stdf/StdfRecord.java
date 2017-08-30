package com.makechip.stdf2xls4.stdf;

import java.lang.reflect.Field;
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
    public final Record_t type;	
	/**
	 * The constructor for an STDF record.
	 */
	protected StdfRecord(Record_t type)
	{
		this.type = type;
	}
	
	public boolean modify(Modifier m)
	{
	    return(false);
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
	    		int mods = f.getModifiers();
	    		if (!java.lang.reflect.Modifier.isStatic(mods))
	    		{
	    		    Object o = f.get(this);
	    		    String name = f.getName();
	    		    String obj = (o == null) ? "null" : o.toString();
	    		    sb.append("    ").append(name).append(" = ").append(obj).append(Log.eol);
	    		}
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

   	protected static void addNibbles(Cpu_t cpu, int[] array, TByteArrayList list, int index, int len)
   	{
   		byte b0 = (byte) array[2 * index];
   		byte b1 = (byte) (((index < len) || ((index % 2) == 0)) ? array[2*index+1] : 0);
   		list.add(cpu.getN1Byte(b0, b1));
   	}
   	

}
