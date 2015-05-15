package com.makechip.stdf2xls4.stdf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.makechip.util.Log;


public abstract class StdfRecord
{
    public static final float MISSING_FLOAT = Float.MAX_VALUE;
    public static final byte MISSING_BYTE = (byte) -127;
    public static final int MISSING_INT = Integer.MIN_VALUE;
    public static final short MISSING_SHORT = Short.MIN_VALUE;
    public static final String MISSING_STRING = "";
    public static final byte[] MISSING_BYTE_ARRAY = new byte[0];
	private Record_t type;
	private int ptr;
	protected byte[] bytes;
	protected final int sequenceNumber;
	protected final int devNum;

	protected StdfRecord(Record_t type, int sequenceNumber, int devNum, byte[] bytes)
	{
		this.type = type;
		this.sequenceNumber = sequenceNumber;
		this.devNum = devNum;
		this.bytes = bytes;
		ptr = 0;
		//Log.msg("seqNum = " + sequenceNumber + " type = " + getClass().getSimpleName());
	}
	
	public void writeStdf(DataOutputStream ds) throws IOException
	{
		if (bytes == null) toBytes();
		byte[] b = getU2Bytes(bytes.length);
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
		if (bytes == null) toBytes();
		//Log.msg("record = " + getClass().getSimpleName());
		byte[] b = new byte[bytes.length + 4];
		byte[] l = getU2Bytes(bytes.length);
		b[0] = l[0];
		b[1] = l[1];
		b[2] = (byte) type.getRecordType();
	    b[3] = (byte) type.getRecordSubType();	
	    //Log.msg("b[2] = " + b[2] + " b[3] = " + b[3]);
	    for (int i=4; i<b.length; i++) b[i] = bytes[i-4];
		return(Arrays.copyOf(b, b.length));
	}
	
	protected int getPtr() { return(ptr); }
	protected int getSize() { return(bytes.length); }
   
	public int getSequenceNumber() { return(sequenceNumber); }
	public int getDeviceNumber()   { return(devNum); }
	public Record_t getRecordType() { return(type); }
	
    protected String getCn()
    {
        if (bytes.length <= ptr) return(MISSING_STRING);
        int s = 0xFF & bytes[ptr++];
        //Log.msg("s.len = " + s + " bytes.length = " + bytes.length + " ptr = " + ptr);
        String out = new String(bytes, ptr, s); 
        //Log.msg("out = " + out);
        ptr += s;
        return(out);
    }
    
    protected byte[] getCnBytes(String s)
    {
    	byte[] b = new byte[s.length() + 1];
    	b[0] = (byte) s.length();
    	for (int i=0; i<s.length(); i++)
    	{
    		b[i+1] = (byte) s.charAt(i);
    	}
    	return(b);
    }
   
    protected int getScale(float result, float hiLimit, byte resScal, byte llmScal, byte hlmScal)
    {
        int scale = 0;
        if (result != 0.0f) scale = resScal;
        else if (hiLimit != 0.0f) scale = hlmScal;
        else scale = llmScal;
        return(scale);
    }
  
    protected double scaleValue(double value, int scale)
    {
        if (value == MISSING_FLOAT) return(value);
        switch (scale)
        {
        case -9: value /= 1E9; break;
        case -6: value /= 1E6; break;
        case -3: value /= 1E3; break;
        case  3: value *= 1E3; break;
        case  6: value *= 1E6; break;
        case  9: value *= 1E9; break;
        case 12: value *= 1E12; break;
        default:
        }
        return(value);
    }
   
    protected String scaleUnits(String units, int scale)
    {
        String u = units;
        switch (scale)
        {
        case -9: u = "G" + units; break;
        case -6: u = "M" + units; break;
        case -3: u = "k" + units; break;
        case  3: u = "m" + units; break;
        case  6: u = "u" + units; break;
        case  9: u = "n" + units; break;
        case 12: u = "p" + units; break;
        default:
        }
        return(u);
    }

    protected short getU1(short defaultValue)
    {
        if (bytes.length < ptr+1) return(defaultValue);
        byte b = bytes[ptr++];
        short s = 0;
        s |= (b & 0xFF);
        return(s);
    }
    
    protected byte[] getU1Bytes(short value)
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
        int l = 0;
        switch ((StdfReader.cpuType))
        {
            case SUN: l |= (b1 & 0xFF); l |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8);
        }
        return(l);
    }
    
    protected byte[] getU2Bytes(int value)
    {
    	byte[] b = new byte[2];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
            b[0] = (byte) ((value & 0xFF00) >> 8);
            b[1] = (byte) (value & 0xFF);
    	}
    	else
    	{
            b[0] = (byte) (value & 0xFF);
            b[1] = (byte) ((value & 0xFF00) >> 8);
    	}
    	return(b);
    }
    
    protected long getU4(long defaultValue)
    {
    	if (bytes.length < ptr+4) return(defaultValue);
    	byte b0 = bytes[ptr++];
    	byte b1 = bytes[ptr++];
    	byte b2 = bytes[ptr++];
    	byte b3 = bytes[ptr++];
    	int l = 0;
    	switch ((StdfReader.cpuType))
    	{
    	    case SUN: l |= (b3 & 0xFF); l |= ((b2 & 0xFF) << 8); l |= ((b1 & 0xFF) << 16); l |= ((b0 & 0xFF) << 24); break;
    	    case VAX:
    	    default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8); l |= ((b2 & 0xFF) << 16); l |= ((b3 & 0xFF) << 24);
    	}
    	return(l);
    }
    
    protected byte[] getU4Bytes(long v)
    {
    	byte[] b = new byte[4];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    		b[0] = (byte) (v >> 24);
    		b[1] = (byte) ((v & 0x00FF0000) >> 16);
    		b[2] = (byte) ((v & 0x0000FF00) >> 8);
    		b[3] = (byte) (v & 0x000000FF);
    	}
    	else
    	{
    	    b[0] = (byte) (v & 0x000000FF);
    	    b[1] = (byte) ((v & 0x0000FF00) >> 8);
    	    b[2] = (byte) ((v & 0x00FF0000) >> 16);
    	    b[3] = (byte) (v >> 24);
    	}
    	return(b);
    }
   
    protected static int getUnsignedInt(byte b0, byte b1)
    {
        int l = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b1 & 0xFF); l |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8);
        }
        return(l);
    }
    
    protected byte getI1(byte defaultValue)
    {
        if (bytes.length <= ptr) return(defaultValue);
        return(bytes[ptr++]);
    }
    
    protected byte[] getI1Bytes(byte value)
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
        short s = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: s |= (b1 & 0xFF); s |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: s |= (b0 & 0xFF); s |= ((b1 & 0xFF) << 8);
        }
        return(s);
    }
    
    protected byte[] getI2Bytes(short value)
    {
    	byte[] b = new byte[2];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    		b[0] = (byte) ((value & 0xFF00) >> 8);
    		b[1] = (byte) (value & 0xFF);
    	}
    	else
    	{
    		b[0] = (byte) (value & 0xFF);
    		b[1] = (byte) ((value & 0xFF00) >> 8);
    	}
    	return(b);
    }
    
    protected int getI4(int defaultValue)
    {
        if (bytes.length < ptr+4) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        int l = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b3 & 0xFF); l |= ((b2 & 0xFF) << 8); l |= ((b1 & 0xFF) << 16); l |= ((b0 & 0xFF) << 24); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8); l |= ((b2 & 0xFF) << 16); l |= ((b3 & 0xFF) << 24);
        }
        return(l);
    }
    
    protected byte[] getI4Bytes(int value)
    {
    	byte[] b = new byte[4];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    		b[0] = (byte) ((value & 0xFF000000) >> 24);
    		b[1] = (byte) ((value & 0x00FF0000) >> 16);
    		b[2] = (byte) ((value & 0x0000FF00) >> 8);
    		b[3] = (byte) ((value & 0x000000FF));
    	}
    	else
    	{
    		b[0] = (byte) ((value & 0x000000FF));
    		b[1] = (byte) ((value & 0x0000FF00) >> 8);
    		b[2] = (byte) ((value & 0x00FF0000) >> 16);
    		b[3] = (byte) ((value & 0xFF000000) >> 24);
    	}
    	return(b);
    }
    
    protected float getR4(float defaultValue)
    {
        if (bytes.length < ptr+4) return(defaultValue);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        byte b2 = bytes[ptr++];
        byte b3 = bytes[ptr++];
        int l = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b3 & 0xFF); l |= ((b2 & 0xFF) << 8); l |= ((b1 & 0xFF) << 16); l |= ((b0 & 0xFF) << 24); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8); l |= ((b2 & 0xFF) << 16); l |= ((b3 & 0xFF) << 24);
        }
        return(Float.intBitsToFloat(l));
    }
    
    protected byte[] getR4Bytes(float val)
    {
    	int value = Float.floatToIntBits(val);
    	byte[] b = new byte[4];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    		b[0] = (byte) ((((long) value) & 0xFF000000L) >> 24);
    		b[1] = (byte) ((((long) value) & 0x00FF0000L) >> 16);
    		b[2] = (byte) ((((long) value) & 0x0000FF00L) >> 8);
    		b[3] = (byte) ((((long) value) & 0x000000FFL));
    	}
    	else
    	{
    		b[0] = (byte) ((((long) value) & 0x000000FFL));
    		b[1] = (byte) ((((long) value) & 0x0000FF00L) >> 8);
    		b[2] = (byte) ((((long) value) & 0x00FF0000L) >> 16);
    		b[3] = (byte) ((((long) value) & 0xFF000000L) >> 24);
    	}
    	return(b);
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
        long l = 0L;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b7 & 0xFFL); l |= ((b6 & 0xFFL) << 8); l |= ((b5 & 0xFFL) << 16); l |= ((b4 & 0xFFL) << 24);
                      l |= ((b3 & 0xFFL) << 32); l |= ((b2 & 0xFFL) << 40); l |= ((b1 & 0xFFL) << 48);
                      l |= ((b0 & 0xFFL) << 56);
                      break;
            case VAX:
            default: l |= (b0 & 0xFFL); l |= ((b1 & 0xFFL) << 8); l |= ((b2 & 0xFFL) << 16); l |= ((b3 & 0xFFL) << 24);
                     l |= ((b4 & 0xFFL) << 32); l |= ((b5 & 0xFFL) << 40); l |= ((b6 & 0xFFL) << 48);
                     l |= ((b7 & 0xFFL) << 56);
        }
        return(Double.longBitsToDouble(l));
    }
    
    protected byte[] getR8Bytes(double val)
    {
    	long value = Double.doubleToLongBits(val);
    	byte[] b = new byte[8];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    		b[0] = (byte) ((((long) value) & 0xFF00000000000000L) >> 56);
    		b[1] = (byte) ((((long) value) & 0x00FF000000000000L) >> 48);
    		b[2] = (byte) ((((long) value) & 0x0000FF0000000000L) >> 40);
    		b[3] = (byte) ((((long) value) & 0x000000FF00000000L) >> 32);
    		b[4] = (byte) ((((long) value) & 0x00000000FF000000L) >> 24);
    		b[5] = (byte) ((((long) value) & 0x0000000000FF0000L) >> 16);
    		b[6] = (byte) ((((long) value) & 0x000000000000FF00L) >> 8);
    		b[7] = (byte) ((((long) value) & 0x00000000000000FFL));
    	}
    	else
    	{
    		b[0] = (byte) ((((long) value) & 0x00000000000000FFL));
    		b[1] = (byte) ((((long) value) & 0x000000000000FF00L) >> 8);
    		b[2] = (byte) ((((long) value) & 0x0000000000FF0000L) >> 16);
    		b[3] = (byte) ((((long) value) & 0x00000000FF000000L) >> 24);
    		b[4] = (byte) ((((long) value) & 0x000000FF00000000L) >> 32);
    		b[5] = (byte) ((((long) value) & 0x0000FF0000000000L) >> 40);
    		b[6] = (byte) ((((long) value) & 0x00FF000000000000L) >> 48);
    		b[7] = (byte) ((((long) value) & 0xFF00000000000000L) >> 56);
    	}
    	return(b);
    }
    
    protected byte getByte() 
    { 
    	if (ptr >= getSize()) return(MISSING_BYTE);
    	return(bytes[ptr++]); 
    }
    
    protected byte[] getByte(byte value)
    {
    	byte[] b = new byte[1];
    	b[0] = value;
    	return(b);
    }

    protected byte[] getBn()
    {
        if (bytes.length == ptr) return(MISSING_BYTE_ARRAY);
        int l = 0xFF & bytes[ptr++];
        if (bytes.length < ptr+l) return(MISSING_BYTE_ARRAY);
        byte[] b = new byte[l];
        for (int i=0; i<l; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    protected byte[] getBnBytes(byte[] bs)
    {
    	byte[] b = new byte[1 + bs.length];
        b[0] = (byte) bs.length;
        for (int i=0; i<bs.length; i++) b[i+1] = bs[i];
    	return(b);
    }
    
    protected byte[] getFixedLengthBitEncodedData(int len)
    {
        if (bytes.length < ptr+len) return(MISSING_BYTE_ARRAY);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    String getFixedLengthString(int len)
    {
        if (bytes.length < ptr+len) return(MISSING_STRING);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(new String(b));
    }
    
    protected byte[] getFixedLengthStringBytes(String s)
    {
    	return(s.getBytes());
    }
    
    protected byte[] getDn()
    {
        if (bytes.length < ptr+2) return(MISSING_BYTE_ARRAY);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        int l = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b1 & 0xFF); l |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8);
        }
        // l is number of bits
        byte[] b = new byte[l];
        int length = (l % 8 == 0) ? l/8 : l/8 + 1; 
        if (bytes.length < ptr+length) return(MISSING_BYTE_ARRAY);
        for (int i=0; i<length; i++) 
        {
        	byte x = bytes[ptr++];
            b[i] = x;
        }
        return(b);
    }
    
    protected byte[] getDnBytes(byte[] dn)
    {
    	int l = dn.length * 8;
    	byte[] b = new byte[dn.length + 2];
    	if (StdfReader.cpuType == Cpu_t.SUN)
    	{
    	    b[0] = (byte) ((l & 0xFF00)	>> 8);
    	    b[1] = (byte) (l &0x00FF);
    	}
    	else
    	{
    	    b[0] = (byte) (l &0x00FF);
    	    b[1] = (byte) ((l & 0xFF00)	>> 8);
    	}
    	for (int i=2; i<dn.length+2; i++) b[i] = dn[i-2];
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
        Log.msg("getNibbles(): n[0] = " + n[0]);
        for (byte x : n) Log.msg_(" " + x);
        Log.msg("");
        return(n);
    }
        
    protected byte[] getNibbleBytes(byte[] nibs)
    {
    	byte[] b = new byte[(nibs.length+1)/2];
    	int j = 0;
    	for (int i=0; i<b.length; i++)
    	{
    	    b[i] = (byte) (nibs[j] & 0x0F);
    	    j++;
    	    if (j == b.length) break;
    	    b[i] |= (byte) ((nibs[j] & 0x0F) << 4);
    	    j++;
    	}
    	Log.msg("getNibbleBytes(): b[0] = " + b[0]);
    	return(b);
    }

}
