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
		public BitData(PadData p, MutableInt numBits, Object value)
		{
			super(p, value);
			this.numBits = numBits.n;
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
		if (v.type == null) Log.msg("v.type IS NULL");
        switch (v.type)
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
		IntStream.generate(() -> 0).limit(field.padCnt).forEach(v -> l.add((byte) 0));
		l.add((byte) field.type.getFieldType());
		switch (field.type)
		{
        case U_1: l.add(getU1Bytes((short) field.value)); break;
        case U_2: l.addAll(cpuType.getU2Bytes((int) field.value)); break;
        case U_4: l.addAll(cpuType.getU4Bytes((long) field.value)); break;
        case I_1: l.add(getI1Bytes((byte) field.value)); break;
        case I_2: l.addAll(cpuType.getI2Bytes((short) field.value)); break;
        case I_4: l.addAll(cpuType.getI4Bytes((int) field.value)); break;
        case R_4: l.addAll(cpuType.getR4Bytes((float) field.value)); break;
        case R_8: l.addAll(cpuType.getR8Bytes((double) field.value)); break;
        case C_N: l.addAll(getCnBytes((String) field.value)); break;
        case B_N: l.addAll(getBnBytes((byte[]) field.value)); break;
        case D_N: BitData bd = (BitData) field; 
        	      l.addAll(cpuType.getDnBytes(bd.numBits, (byte[]) field.value)); 
        	      break;
        case N_N: l.addAll(getNibbleBytes((byte[]) field.value)); break;
       	default: throw new RuntimeException("Unknown data type in GenericDataRecord");
		}
	}
	
	/**
     * Constructor for initializing this record with binary stream data.
     * @param tdb The TestIdDatabase  is not used by this record, but is
     * required so STDF records have consistent constructor signatures.
     * @param dvd This CTOR sets the CPU type in the DefaultValueDatabase.
	 * @param data
	 */
    public GenericDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.GDR, dvd.getCpuType(), data);
        List<Data> l = new ArrayList<Data>();
        int fields = getU2(0);
        Stream.generate(() -> getType()).limit(fields).forEach(v -> getField(l, v));
        list = Collections.unmodifiableList(l);
    }
    
    /**
     * Constructor for initializing this record with field values.
     * @param tdb The TestIdDatabase is needed because this CTOR calls the above CTOR.
     * @param dvd The DefaultValueDatabase is needed because this CTOR calls the above CTOR.
     * @param list A list of Data fields.
     */
    public GenericDataRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, List<Data> list)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), list));
    }

	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf.StdfRecord#toBytes()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("GenericDataRecord [list=");
		builder.append(list);
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
		if (!super.equals(obj)) return false;
		if (!(obj instanceof GenericDataRecord)) return false;
		GenericDataRecord other = (GenericDataRecord) obj;
		if (list == null)
		{
			if (other.list != null) return false;
		} 
		else if (!list.equals(other.list)) return false;
		return true;
	}
    
    
}
