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
	
	public static final class Data
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
        case D_N: l.add(new Data(v, getDn())); break;
        case N_N: l.add(new Data(v, getNibbles(1))); break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
        }
	}
	
	private void setField(TByteArrayList l, Data field)
	{
		IntStream.generate(() -> 0).limit(field.getPadCnt()).forEach(v -> l.add((byte) 0));
		l.add((byte) field.getType().getFieldType());
		switch (field.getType())
		{
        case U_1: l.add(getU1Bytes((short) field.getValue())); break;
        case U_2: l.addAll(getU2Bytes((int) field.getValue())); break;
        case U_4: l.addAll(getU4Bytes((long) field.getValue())); break;
        case I_1: l.add(getI1Bytes((byte) field.getValue())); break;
        case I_2: l.addAll(getI2Bytes((short) field.getValue())); break;
        case I_4: l.addAll(getI4Bytes((int) field.getValue())); break;
        case R_4: l.addAll(getR4Bytes((float) field.getValue())); break;
        case R_8: l.addAll(getR8Bytes((double) field.getValue())); break;
        case C_N: l.addAll(getCnBytes((String) field.getValue())); break;
        case B_N: l.addAll(getBnBytes((byte[]) field.getValue())); break;
        case D_N: l.addAll(getDnBytes((byte[]) field.getValue())); break;
        case N_N: l.addAll(getNibbleBytes((byte[]) field.getValue())); break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
		}
	}
	
    /**
    *** @param p1
    *** @param p2
    **/
    public GenericDataRecord(int sequenceNumber, byte[] data)
    {
        super(Record_t.GDR, sequenceNumber, data);
        List<Data> l = new ArrayList<Data>();
        int fields = getU2(0);
        Stream.generate(() -> getType()).limit(fields).forEach(v -> getField(l, v));
        list = Collections.unmodifiableList(l);
    }
    
    public GenericDataRecord(int sequenceNumber, List<Data> list)
    {
    	super(Record_t.GDR, sequenceNumber, null);
    	this.list = new ArrayList<Data>(list);
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
	    TByteArrayList l = new TByteArrayList();
	    l.addAll(getU2Bytes(list.size())); 
	    list.stream().forEach(d -> setField(l, d));
	    bytes = l.toArray();
	}
    
    
}
