/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4.stdf;

import gnu.trove.list.array.TByteArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: GenericDataRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class GenericDataRecord extends StdfRecord
{
    public final List<Data> list;
	
	public static class Data
	{
		final Data_t type;
		final Object value;
		final int padCnt;
		
		public Data(PadData p, Object value)
		{
			this.type = p.getType();
			padCnt = p.getPadCnt();
			this.value = value;
		}
		
		public Data_t getType() { return(type); }
		
		public Object getValue() { return(value); }
		
		public int getPadCnt() { return(padCnt); }
		
		@Override
		public String toString()
		{
			return(value.toString());
		}
	}
	
	public static final class BitData extends Data
	{
		public final int numBits;
		
		public BitData(PadData p, MutableInt numBits, Object value)
		{
			super(p, value);
			this.numBits = numBits.n;
		}
	}
	
	public static final class PadData
	{
		final Data_t type;
		final byte padCnt;
		
		public PadData(Data_t type, int padCnt)
		{
			this.type = type;
			this.padCnt = (byte) padCnt;
		}
		
		Data_t getType() { return(type); }
		int getPadCnt() { return(padCnt); }
	}
	
	private PadData getType()
	{
		int type = 0;
		int cnt = 0;
		while (type == (short) 0) 
		{
			type = getU1((short) -1);
			if (type != (byte) 0) break;
			cnt++;
		}
		return(new PadData(Data_t.getDataType(type), cnt));
	}
	
	private void getField(List<Data> l, PadData v)
	{
		if (v == null) Log.msg("V IS NULL");
		if (v.getType() == null) Log.msg("v.type IS NULL");
        switch (v.getType())
        {
        case U_1: l.add(new Data(v, getU1((short) 0))); break;
        case U_2: l.add(new Data(v, getU2(0))); break;
        case U_4: l.add(new Data(v, getU4(0))); break;
        case I_1: l.add(new Data(v, getI1((byte) 0))); break;
        case I_2: l.add(new Data(v, getI2((short) 0))); break;
        case I_4: l.add(new Data(v, getI4(0))); break;
        case R_4: l.add(new Data(v, getR4(0.0f))); break;
        case R_8: l.add(new Data(v, getR8(0.0))); break;
        case C_N: l.add(new Data(v, getCn())); break;
        case B_N: l.add(new Data(v, getBn())); break;
        case D_N: MutableInt numBits = new MutableInt(); 
                  l.add(new BitData(v, numBits, getDn(numBits))); 
                  break;
        case N_N: l.add(new Data(v, getNibbles(1))); break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
        }
	}
	
	private static void setField(Cpu_t cpuType, TByteArrayList l, Data field)
	{
		IntStream.generate(() -> 0).limit(field.getPadCnt()).forEach(v -> l.add((byte) 0));
		l.add((byte) field.getType().getFieldType());
		switch (field.getType())
		{
        case U_1: l.add(getU1Bytes((short) field.getValue())); break;
        case U_2: l.addAll(cpuType.getU2Bytes((int) field.getValue())); break;
        case U_4: l.addAll(cpuType.getU4Bytes((long) field.getValue())); break;
        case I_1: l.add(getI1Bytes((byte) field.getValue())); break;
        case I_2: l.addAll(cpuType.getI2Bytes((short) field.getValue())); break;
        case I_4: l.addAll(cpuType.getI4Bytes((int) field.getValue())); break;
        case R_4: l.addAll(cpuType.getR4Bytes((float) field.getValue())); break;
        case R_8: l.addAll(cpuType.getR8Bytes((double) field.getValue())); break;
        case C_N: l.addAll(getCnBytes((String) field.getValue())); break;
        case B_N: l.addAll(getBnBytes((byte[]) field.getValue())); break;
        case D_N: BitData bd = (BitData) field; 
        	      l.addAll(cpuType.getDnBytes(bd.numBits, (byte[]) field.getValue())); 
        	      break;
        case N_N: l.addAll(getNibbleBytes((byte[]) field.getValue())); break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
		}
	}
	
    /**
    *** @param p1
    *** @param p2
    **/
    public GenericDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.GDR, dvd.getCpuType(), data);
        List<Data> l = new ArrayList<Data>();
        int fields = getU2(0);
        Stream.generate(() -> getType()).limit(fields).forEach(v -> getField(l, v));
        list = Collections.unmodifiableList(l);
    }
    
    public GenericDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, List<Data> list)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), list));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        list.stream().forEach(p -> sb.append("    ").append(p.toString()).append(Log.eol));
        return(sb.toString());
    }

	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, list);	
	}
	
	private static byte[] toBytes(Cpu_t cpuType, List<Data> list)
	{
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(cpuType.getU2Bytes(list.size())); 
	    list.stream().forEach(d -> setField(cpuType, l, d));
	    return(l.toArray());
	}
    
    
}
