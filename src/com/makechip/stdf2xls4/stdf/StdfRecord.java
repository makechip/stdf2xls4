package com.makechip.stdf2xls4.stdf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;


/**
 * This is the superclass for all StdfRecord Types.  In general,
 * the field values for all STDF record CTORs may not be null.
 * @author eric
 */
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
    /**
     * Enum value that specifies the CPU type.
     */
    protected final Cpu_t cpuType;
    /**
     * Enum value that indicates the record type.
     */
	public final Record_t type;
	protected int ptr;
	/**
	 * This field holds the STDF byte stream data for this record.
	 * It does not include the REC_LEN, REC_TYP, and REC_SUB bytes.
	 */
	protected byte[] bytes;
	
	/**
	 * This is a helper class to allow an integer to be passed
	 * to a method as a reference. 
	 * @author eric
	 */
	public static class MutableInt
	{
		public int n;
	}

	/**
	 * The constructor for an STDF record.
	 * @param type  The record type.
	 * @param cpuType The cpuType.
	 * @param bytes The byte data for the record being built. This array
	 * must *not* include the REC_LEN, REC_TYP, and REC_SUB byte fields.
	 */
	protected StdfRecord(Record_t type, Cpu_t cpuType, byte[] bytes)
	{
		this.type = type;
		this.bytes = bytes;
		this.cpuType = cpuType;
		ptr = 0;
	}
	
	/**
	 * Method to write out this record to a binary byte stream.
	 * @param ds A DataOutputStream.
	 * @throws IOException
	 */
	public void writeStdf(DataOutputStream ds) throws IOException
	{
		/*
		byte[] b = cpuType.getU2Bytes(bytes.length);
		ds.write(b, 0, b.length);
		b[0] = (type == Record_t.DTRX) ? (byte) Record_t.DTR.recordType : (byte) type.recordType;
		b[1] = (type == Record_t.DTRX) ? (byte) Record_t.DTR.recordSubType : (byte) type.recordSubType;
		ds.write(b[0]);
		ds.write(b[1]);
		*/
		toBytes();
		byte[] b = getBytes();
		ds.write(b, 0, b.length);
	}
	
	/**
	 * This method converts the field values to the byte array used by the constructor.
	 * It is used to take a record initialized by field values, and load it
	 * into the byte array.
	 */
	protected abstract void toBytes();
	
	/**
	 * Get the byte stream for this record.  This byte stream does include the
	 * REC_LEN, REC_TYP, and REC_SUB byte fields.
	 * @return
	 */
	public byte[] getBytes() 
	{ 
		byte[] b = new byte[bytes.length + 4];
		byte[] l = cpuType.getU2Bytes(bytes.length);
		b[0] = l[0];
		b[1] = l[1];
		b[2] = (type == Record_t.DTRX) ? (byte) Record_t.DTR.recordType : (byte) type.recordType;
	    b[3] = (type == Record_t.DTRX) ? (byte) Record_t.DTR.recordSubType : (byte) type.recordSubType;	
	    for (int i=4; i<b.length; i++) b[i] = bytes[i-4];
		return(Arrays.copyOf(b, b.length));
	}
	
	/**
	 * Gets the length of the byte array field.
	 * @return The length of the bytes field.
	 */
	protected int getSize() { return(bytes.length); }
   
	/**
	 * Extract a String from the bytes array.
	 * @return
	 */
    protected String getCn()
    {
        if (bytes.length <= ptr) return(MISSING_STRING);
        int s = 0xFF & bytes[ptr++];
        String out = new String(bytes, ptr, s); 
        ptr += s;
        return(out);
    }
    
    /**
     * Converts a String into a C*n byte array.
     * @param s The String to be converted.  If s is
     * null, then it is treated as a zero-length String.
     * s can have a maximum length of 255 characters.
     * @return The C*n byte array which includes
     * the length byte in element 0 of the array.
     */
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
   
    /**
     * Extract an unsigned byte from the bytes array.
     * @param defaultValue The value to return if the
     * end of the bytes array has already been reached.
     * @return The value contained in one byte; this
     * is returned as a short because java does not have unsigned types.
     */
    protected short getU1(short defaultValue)
    {
        if (bytes.length < ptr+1) return(defaultValue);
        byte b = bytes[ptr++];
        short s = 0;
        s |= (b & 0xFF);
        return(s);
    }
    
    /**
     * Converts an unsigned byte to a byte array.
     * This will be a single-element array.
     * @param value A value between 0 and 255 inclusive.
     * @return
     */
    protected static byte[] getU1Bytes(short value)
    {
    	byte[] b = new byte[1];
    	b[0] = (byte) (value & 0xFF);
    	return(b);
    }
    
    /**
     * Extract an unsigned short from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached. 
     * The value must be between 0 and 2 * Short.MAX_VALUE + 1 inclusive.
     * @return A two-byte positive value.  An int is used because
     * java does not have unsigned types.
     */
    protected int getU2(int defaultValue)
    {
        if (bytes.length < ptr+2) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        return(cpuType.getU2(b0,  b1));
    }
    
    /**
     * Extract an unsigned int from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached. 
     * The value must be between 0 and 2 * Integer.MAX_VALUE + 1 inclusive.
     * @return A four-byte positive value.  A long is used because
     * java does not have unsigned types.
     */
    protected long getU4(long defaultValue)
    {
    	if (bytes.length < ptr+4) return(defaultValue);
    	byte b0 = bytes[ptr++];
    	byte b1 = bytes[ptr++];
    	byte b2 = bytes[ptr++];
    	byte b3 = bytes[ptr++];
    	return(cpuType.getU4(b0,  b1, b2, b3));
    }
    
    /**
     * Extract a one-byte integer from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return A one byte value.
     */
    protected byte getI1(byte defaultValue)
    {
        if (bytes.length <= ptr) return(defaultValue);
        return(bytes[ptr++]);
    }
    
    /**
     * Converts a signed byte to a byte array.
     * In reality this method is exactly the same as getU1Bytes().
     * This will be a single-element array.
     * @param value A value between 0 and 255 inclusive.
     * @return A single-element byte array.
     */
    protected static byte[] getI1Bytes(byte value)
    {
    	byte[] b = new byte[1];
    	b[0] = value;
    	return(b);
    }
    
    /**
     * Extract a short value from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return A two-byte signed short value.
     */
    protected short getI2(short defaultValue)
    {
        if (bytes.length < ptr+2) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        return(cpuType.getI2(b0, b1));
    }
    
    /**
     * Extract an int value from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return A four-byte signed int value.
     */
    protected int getI4(int defaultValue)
    {
        if (bytes.length < ptr+4) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        return(cpuType.getI4(b0,  b1,  b2,  b3));
    }
    
    /**
     * Extract a four-byte float value from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return A four-byte float value.
     */
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
    
    /**
     * Extract an eight-byte double value from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return An eight-byte double value.
     */
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
    
    /**
     * Extract a single-byte value from the bytes array.
     * @return A byte. If the end of the bytes array has already
     * been reached, the StdfRecord.MISSING_BYTE will be returned.
     */
    protected byte getByte() 
    { 
    	if (ptr >= getSize()) return(MISSING_BYTE);
    	return(bytes[ptr++]); 
    }
    
    /**
     * Extract a variable-length (B*n) bit-encoded field from the bytes array.
     * @return The byte array containing the encoded bits.  If the end of
     * the bytes array has already been reached, then StdfRecord.MISSING_BYTE_ARRAY
     * will be returned. The maximum length of this array is 255 bytes.
     */
    protected byte[] getBn()
    {
        if (bytes.length == ptr) return(MISSING_BYTE_ARRAY);
        int l = 0xFF & bytes[ptr++];
        if (bytes.length < ptr+l) return(MISSING_BYTE_ARRAY);
        byte[] b = new byte[l];
        for (int i=0; i<l; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    /**
     * Convert a variable-length (B*n) bit-encoded field to an array of bytes.
     * @param bs A byte array containing a bit-encoded field.
     * The array must not be longer than 255 elements.
     * @return The B*n byte array. including the length field.
     */
    protected static byte[] getBnBytes(byte[] bs)
    {
    	byte[] b = new byte[1 + bs.length];
        b[0] = (byte) bs.length;
        for (int i=0; i<bs.length; i++) b[i+1] = bs[i];
    	return(b);
    }
    
    /**
     * Extract a C*len fixed-length byte array from the bytes array.
     * @param len The length of the field in bytes.
     * @return The byte array.
     */
    String getFixedLengthString(int len)
    {
        if (bytes.length < ptr+len) return(MISSING_STRING);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(new String(b));
    }
    
    /**
     * Convert a String to a fixed-length byte array.
     * @param s A String.
     * @return The equivalent array of bytes.
     */
    protected static byte[] getFixedLengthStringBytes(String s)
    {
    	return(s.getBytes());
    }
    
    /** 
     * Unpack nibbles packed in bytes.
     * @param cnt Number of nibbles
     * @return An array of bytes containing one nibble per byte.
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
        
    /**
     * Convert a byte-array of one nibble per byte into a
     * packed two-nibble per byte array.
     * @param nibs A byte array containing one nibble per byte.
     * @return A packed array containing two nibbles per byte.
     */
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

    /**
     * Extract a variable-length bit-encoded field from the bytes array.
     * This field can have a maximum length of 65535 bytes.
     * @param numBits The number of bits in this field is returned
     * in this reference variable.
     * @return The array of bytes containing the bit-encoded field.
     * The length bytes are not included in this array.
     */
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
