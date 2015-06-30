package com.makechip.stdf2xls4.stdf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


public abstract class StdfRecord
{
    public static final String TEXT_DATA          = "TEXT_DATA";
    public static final String SERIAL_MARKER      = "S/N";
    public static final float  MISSING_FLOAT      = Float.MAX_VALUE;
    public static final byte   MISSING_BYTE       = (byte) -1;
    public static final int    MISSING_INT        = Integer.MAX_VALUE;
    public static final long   MISSING_LONG       = Long.MIN_VALUE;
    public static final short  MISSING_SHORT      = Short.MIN_VALUE;
    public static final String MISSING_STRING     = "";
    public static final byte[] MISSING_BYTE_ARRAY = new byte[0];
    protected final Cpu_t cpuType;
	public final Record_t type;
	protected int ptr;
	protected byte[] bytes;
	
	public static class MutableInt
	{
		public int n;
	}

	protected StdfRecord(Record_t type, Cpu_t cpuType, byte[] bytes)
	{
		this.type = type;
		this.bytes = bytes;
		this.cpuType = cpuType;
		ptr = 0;
	}
	
	public void writeStdf(DataOutputStream ds) throws IOException
	{
		byte[] b = cpuType.getU2Bytes(bytes.length);
		ds.write(b, 0, b.length);
		b[0] = (byte) type.getRecordType();
		b[1] = (byte) type.getRecordSubType();
		ds.write(b[0]);
		ds.write(b[1]);
		toBytes();
		ds.write(bytes, 0, bytes.length);
	}
	
	protected abstract void toBytes();
	
	public byte[] getBytes() 
	{ 
		byte[] b = new byte[bytes.length + 4];
		byte[] l = cpuType.getU2Bytes(bytes.length);
		b[0] = l[0];
		b[1] = l[1];
		b[2] = (byte) type.getRecordType();
	    b[3] = (byte) type.getRecordSubType();	
	    //Log.msg("b[2] = " + b[2] + " b[3] = " + b[3]);
	    for (int i=4; i<b.length; i++) b[i] = bytes[i-4];
		return(Arrays.copyOf(b, b.length));
	}
	
	//protected int getPtr() { return(ptr); }
	protected int getSize() { return(bytes.length); }
   
    protected String getCn()
    {
        if (bytes.length <= ptr) return(MISSING_STRING);
        int s = 0xFF & bytes[ptr++];
        String out = new String(bytes, ptr, s); 
        ptr += s;
        return(out);
    }
    
    protected static byte[] getCnBytes(String s)
    {
    	if (s == null) s = "";
    	byte[] b = new byte[s.length() + 1];
    	b[0] = (byte) s.length();
    	for (int i=0; i<s.length(); i++)
    	{
    		b[i+1] = (byte) s.charAt(i);
    	}
    	return(b);
    }
   
    protected short getU1(short defaultValue)
    {
        if (bytes.length < ptr+1) return(defaultValue);
        byte b = bytes[ptr++];
        short s = 0;
        s |= (b & 0xFF);
        return(s);
    }
    
    protected static byte[] getU1Bytes(short value)
    {
    	byte[] b = new byte[1];
    	b[0] = (byte) (value & 0xFF);
    	return(b);
    }
    
    protected int getU2(int defaultValue)
    {
        if (bytes.length < ptr+2) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        return(cpuType.getU2(b0,  b1));
    }
    
    protected long getU4(long defaultValue)
    {
    	if (bytes.length < ptr+4) return(defaultValue);
    	byte b0 = bytes[ptr++];
    	byte b1 = bytes[ptr++];
    	byte b2 = bytes[ptr++];
    	byte b3 = bytes[ptr++];
    	return(cpuType.getU4(b0,  b1, b2, b3));
    }
    
    protected byte getI1(byte defaultValue)
    {
        if (bytes.length <= ptr) return(defaultValue);
        return(bytes[ptr++]);
    }
    
    protected static byte[] getI1Bytes(byte value)
    {
    	byte[] b = new byte[1];
    	b[0] = value;
    	return(b);
    }
    
    protected short getI2(short defaultValue)
    {
        if (bytes.length < ptr+2) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        return(cpuType.getI2(b0, b1));
    }
    
    protected int getI4(int defaultValue)
    {
        if (bytes.length < ptr+4) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        return(cpuType.getI4(b0,  b1,  b2,  b3));
    }
    
    protected float getR4(float defaultValue)
    {
        if (bytes.length < ptr+4) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        int l = cpuType.getI4(b0, b1, b2, b3);
        return(Float.intBitsToFloat(l));
    }
    
    protected double getR8(double defaultValue)
    {
        if (bytes.length < ptr+8) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        byte b4 = bytes[ptr++];
        byte b5 = bytes[ptr++];
        byte b6 = bytes[ptr++];
        byte b7 = bytes[ptr++];
        long l = cpuType.getLong(b0,  b1,  b2,  b3,  b4,  b5,  b6,  b7);
        return(Double.longBitsToDouble(l));
    }
    
   protected byte getByte() 
    { 
    	if (ptr >= getSize()) return(MISSING_BYTE);
    	return(bytes[ptr++]); 
    }
    
//    protected byte[] getByte(byte value)
//    {
//    	byte[] b = new byte[1];
//    	b[0] = value;
//    	return(b);
//    }

    protected byte[] getBn()
    {
        if (bytes.length == ptr) return(MISSING_BYTE_ARRAY);
        int l = 0xFF & bytes[ptr++];
        if (bytes.length < ptr+l) return(MISSING_BYTE_ARRAY);
        byte[] b = new byte[l];
        for (int i=0; i<l; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    protected static byte[] getBnBytes(byte[] bs)
    {
    	byte[] b = new byte[1 + bs.length];
        b[0] = (byte) bs.length;
        for (int i=0; i<bs.length; i++) b[i+1] = bs[i];
    	return(b);
    }
    
//    protected byte[] getFixedLengthBitEncodedData(int len)
//    {
//        if (bytes.length < ptr+len) return(MISSING_BYTE_ARRAY);
//        byte[] b = new byte[len];
//        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
//        return(b);
//    }
    
    String getFixedLengthString(int len)
    {
        if (bytes.length < ptr+len) return(MISSING_STRING);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(new String(b));
    }
    
    protected static byte[] getFixedLengthStringBytes(String s)
    {
    	return(s.getBytes());
    }
    
    protected byte[] getDn(MutableInt numBits)
    {
        if (bytes.length < ptr+2) return(MISSING_BYTE_ARRAY);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        int l = cpuType.getU2(b0,  b1);
        numBits.n = l;
        int length = (l % 8 == 0) ? l/8 : l/8 + 1; 
        byte[] b = new byte[length];
        if (bytes.length < ptr+length) return(MISSING_BYTE_ARRAY);
        for (int i=0; i<length; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    /** 
     * Unpack nibbles packed in bytes.
     * @param cnt Number of nibbles
     */
    public byte[] getNibbles(int cnt)
    {
        byte[] n = new byte[cnt];
        for (int i=0; i<cnt; i++)
        {
            byte b = bytes[ptr++];
            n[i] = (byte) (b & ((byte) 0x0F));
            i++;
            if (i == cnt) break;
            n[i] = (byte) ((b & 0xF0) >> 4);
        }
        return(n);
    }
        
    protected static byte[] getNibbleBytes(byte[] nibs)
    {
    	byte[] b = new byte[(nibs.length+1)/2];
    	int j = 0;
    	for (int i=0; i<b.length; i++)
    	{
    	    b[i] = (byte) (nibs[j] & 0x0F);
    	    j++;
    	    if (j == nibs.length) break; 
    	    b[i] |= (byte) ((nibs[j] & 0x0F) << 4);
    	    j++;
    	}
    	return(b);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		result = prime * result + cpuType.hashCode();
		result = prime * result + type.hashCode();
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
		if (!(obj instanceof StdfRecord)) return false;
		StdfRecord other = (StdfRecord) obj;
		if (!Arrays.equals(bytes, other.bytes)) return false;
		if (cpuType != other.cpuType) return false;
		if (type != other.type) return false;
		return true;
	}

}
