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
//import java.util.stream.IntStream;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
*** @author eric
*** @version $Id: GenericDataRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class GenericDataRecord extends StdfRecord
{
	/**
	 * This is the GEN_DATA field of a Generic Data Record. The
	 * FLD_CNT field is equivalent to the size of this list.
	 */
    public final List<Data> list;
	
    /**
     * This is a wrapper class for generic data objects.
     * @author eric
     */
	public static class Data
	{
		/**
		 * This is the type of the data contained in this wrapper class.
		 */
		public final Data_t type;
		/**
		 * This is the data values.  Since most data types are primitive
		 * types, auto-boxing is used a lot with generic data records.
		 */
		public final Object value;
		/**
		 * The number of pad bytes used by this data field.
		 */
		public final int padCnt;
		
		public Data(PadData p, Object value)
		{
			this.type = p.type;
			padCnt = p.padCnt;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Data [type=").append(type);
			builder.append(", value=").append(value);
			builder.append(", padCnt=").append(padCnt);
			builder.append("]");
			return builder.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + padCnt;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof Data)) return false;
			Data other = (Data) obj;
			if (padCnt != other.padCnt) return false;
			if (type != other.type) return false;
			if (value == null)
			{
				if (other.value != null) return false;
			} 
			else if (!value.equals(other.value)) return false;
			return true;
		}
		
	}
	
	/**
	 * This class is a specialization of the Data class and is used
	 * for data types that are bit fields.
	 * @author eric
	 */
	public static final class BitData extends Data
	{
		/**
		 * The number of bits contained in this data value.
		 */
		public final int numBits;
		
		/**
		 * CTOR for bit data.
		 * @param p The padding information
		 * @param numBits The number of bits in this bit field.
		 * @param value The data payload, normally an array of some type.
		 */
		public BitData(PadData p, int numBits, Object value)
		{
			super(p, value);
			this.numBits = numBits;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("BitData [numBits=").append(numBits);
			builder.append(", type=").append(type);
			builder.append(", value=").append(value);
			builder.append(", padCnt=").append(padCnt);
			builder.append("]");
			return builder.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + numBits;
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (!super.equals(obj)) return false;
			if (!(obj instanceof BitData)) return false;
			BitData other = (BitData) obj;
			if (numBits != other.numBits) return false;
			return true;
		}
		
	}
	
	/**
	 * Wrapper class for holding data type and pad count of a field.
	 * @author eric
	 */
	public static final class PadData
	{
		/**
		 * The data type of a generic field.
		 */
		public final Data_t type;
		/**
		 * The pad count used by a generic field.
		 */
		public final byte padCnt;
		
		/**
		 * CTOR for pad data.
		 * @param type  The data type of the field.
		 * @param padCnt The pad count for the field.
		 */
		public PadData(Data_t type, int padCnt)
		{
			this.type = type;
			this.padCnt = (byte) padCnt;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("PadData [type=").append(type);
			builder.append(", padCnt=").append(padCnt);
			builder.append("]");
			return builder.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + padCnt;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof PadData)) return false;
			PadData other = (PadData) obj;
			if (padCnt != other.padCnt) return false;
			if (type != other.type) return false;
			return true;
		}
	}
	
	private static PadData getType(Cpu_t cpu, ByteInputStream is)
	{
		int type = 0;
		int cnt = 0;
		while (type == (short) 0) 
		{
			type = cpu.getU1(is);
			if (type != (byte) 0) break;
			cnt++;
		}
		return(new PadData(Data_t.getDataType(type), cnt));
	}
	
	private void getField(List<Data> l, PadData v, Cpu_t cpu, ByteInputStream is)
	{
        switch (v.type)
        {
        case U1: l.add(new Data(v, cpu.getU1(is))); break;
        case U2: l.add(new Data(v, cpu.getU2(is))); break;
        case U4: l.add(new Data(v, cpu.getU4(is))); break;
        case I1: l.add(new Data(v, cpu.getI1(is))); break;
        case I2: l.add(new Data(v, cpu.getI2(is))); break;
        case I4: l.add(new Data(v, cpu.getI4(is))); break;
        case R4: l.add(new Data(v, cpu.getR4(is))); break;
        case R8: l.add(new Data(v, cpu.getR8(is))); break;
        case CN: l.add(new Data(v, cpu.getCN(is))); break;
        case BN: l.add(new Data(v, cpu.getBN(is))); break;
        case N1: l.add(new Data(v, cpu.getN1(is))); break;
        case DN: int numBits = cpu.getU2(is);
                 l.add(new BitData(v, numBits, cpu.getDN(numBits, is))); 
                 break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
        }
	}
	
	/**
	private static void setField(Cpu_t cpu, TByteArrayList l, Data field)
	{
		IntStream.generate(() -> 0).limit(field.padCnt).forEach(v -> l.add((byte) 0));
		Log.msg("GDR TYPE = " + field.type.type);
		l.add((byte) field.type.type);
		switch (field.type)
		{
        case U1: l.add(cpu.getU1Bytes((short) field.value)); break;
        case U2: l.addAll(cpu.getU2Bytes((int) field.value)); break;
        case U4: l.addAll(cpu.getU4Bytes((long) field.value)); break;
        case I1: l.add(cpu.getI1Bytes((byte) field.value)); break;
        case I2: l.addAll(cpu.getI2Bytes((short) field.value)); break;
        case I4: l.addAll(cpu.getI4Bytes((int) field.value)); break;
        case R4: l.addAll(cpu.getR4Bytes((float) field.value)); break;
        case R8: l.addAll(cpu.getR8Bytes((double) field.value)); break;
        case CN: l.addAll(cpu.getCNBytes((String) field.value)); break;
        case BN: l.addAll(cpu.getBNBytes((byte[]) field.value)); break;
        case N1: byte[] b = (byte[]) field.value;
        	     l.add(cpu.getN1Byte(b[0], b[1])); break;
        case DN: BitData bd = (BitData) field; 
        	      l.addAll(cpu.getDNBytes(bd.numBits, (byte[]) field.value)); 
        	      break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
		}
	}
	**/
	
	/**
     * Constructor for initializing this record with binary stream data.
     * @param tdb The TestIdDatabase  is not used by this record, but is
     * required so STDF records have consistent constructor signatures.
	 * @param data
	 */
    public GenericDataRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
        super(Record_t.GDR);
        List<Data> l = new ArrayList<Data>();
        int fields = cpu.getU2(is);
        for (int i=0; i<fields; i++)
        {
            PadData v = getType(cpu, is);
            getField(l, v, cpu, is);
        }
        list = Collections.unmodifiableList(l);
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param list A list of Data fields.
     */
    public GenericDataRecord(List<Data> list)
    {
    	super(Record_t.GDR);
    	this.list = Collections.unmodifiableList(list);
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, list);
		TByteArrayList l2 = getHeaderBytes(cpu, Record_t.GDR, b.length);
		l2.addAll(b);
        return(l2.toArray());
	}
	
	private static byte[] toBytes(Cpu_t cpu, List<Data> list)
	{
	    TByteArrayList l = new TByteArrayList();	
	    l.addAll(cpu.getU2Bytes(list.size()));
	    for (Data d : list)
	    {
	    	for (int i=0; i<d.padCnt; i++) l.addAll(cpu.getU1Bytes((short) 0));
	    	l.addAll(cpu.getU1Bytes(d.type.type));
	    	switch (d.type)
	    	{
	    	case U1: l.addAll(cpu.getU1Bytes((short) d.value)); break;
	    	case U2: l.addAll(cpu.getU2Bytes((int) d.value)); break;
	    	case U4: l.addAll(cpu.getU4Bytes((long) d.value)); break;
	    	case I1: l.addAll(cpu.getI1Bytes((byte) d.value)); break;
	    	case I2: l.addAll(cpu.getI2Bytes((short) d.value)); break;
	    	case I4: l.addAll(cpu.getI4Bytes((int) d.value)); break;
	    	case R4: l.addAll(cpu.getR4Bytes((float) d.value)); break;
	    	case R8: l.addAll(cpu.getR8Bytes((double) d.value)); break;
	    	case CN: l.addAll(cpu.getCNBytes(d.value.toString())); break;
	    	case BN: l.addAll(cpu.getBNBytes((byte[]) d.value)); break;
	    	case DN: l.addAll(cpu.getDNBytes(((BitData) d).numBits, (byte[]) d.value)); break;
	    	case N1: l.add(cpu.getN1Byte(((byte[])d.value)[0], ((byte[])d.value)[1])); break;
	    	default: throw new RuntimeException("GDR ERROR: unsupported data type: " + d.type);
	    	}
	    }
	    return(l.toArray());
	}
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof GenericDataRecord)) return false;
		GenericDataRecord other = (GenericDataRecord) obj;
		if (list == null)
		{
			if (other.list != null) return false;
		} 
		else if (!list.equals(other.list)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

    
}
