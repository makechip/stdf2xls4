package com.makechip.stdf2xls4.stdf;


public abstract class StdfRecord
{
    public static final float MISSING_FLOAT = -9.9E30F;
    public static final byte MISSING_BYTE = (byte) -120;
    public static final int MISSING_INT = -9999999;
	private Record_t type;
	private int ptr;
	private byte[] bytes;
	private final int sequenceNumber;
	private final int devNum;

	protected StdfRecord(Record_t type, int sequenceNumber, int devNum, byte[] bytes)
	{
		this.type = type;
		this.sequenceNumber = sequenceNumber;
		this.devNum = devNum;
		this.bytes = bytes;
		ptr = 0;
	}
	
	protected int getPtr() { return(ptr); }
	protected int getSize() { return(bytes.length); }
   
	public int getSequenceNumber() { return(sequenceNumber); }
	public int getDeviceNumber()   { return(devNum); }
	public Record_t getRecordType() { return(type); }
	
    protected String getCn()
    {
        if (bytes.length <= ptr) return(null);
        int s = 0xFF & bytes[ptr++];
        String out = new String(bytes, ptr, s); 
        ptr += s;
        return(out);
    }
   
    protected int getU4(int defaultValue)
    {
    	if (bytes.length < ptr+4) return(defaultValue);
    	byte b0 = bytes[ptr++];
    	byte b1 = bytes[ptr++];
    	byte b2 = bytes[ptr++];
    	byte b3 = bytes[ptr++];
    	int l = 0;
    	switch ((StdfReader.cpuType))
    	{
    	    case SUN: l |= (b3 & 0xFFL); l |= ((b2 & 0xFFL) << 8); l |= ((b1 & 0xFFL) << 16); l |= ((b0 & 0xFFL) << 24); break;
    	    case VAX:
    	    default: l |= (b0 & 0xFFL); l |= ((b1 & 0xFFL) << 8); l |= ((b2 & 0xFFL) << 16); l |= ((b3 & 0xFFL) << 24);
    	}
    	return(l);
    }
   
    protected int getScale(float result, float hiLimit, byte resScal, byte llmScal, byte hlmScal)
    {
        int scale = 0;
        if (result != 0.0f) scale = resScal;
        else if (hiLimit != 0.0f) scale = hlmScal;
        else scale = hlmScal;
        return(scale);
    }
  
   protected float scaleValue(float value, int scale)
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
    
    protected byte getByte() { return(bytes[ptr++]); }

    protected byte[] getBn()
    {
        if (bytes.length == ptr) return(null);
        int l = 0xFF & bytes[ptr++];
        if (bytes.length < ptr+l) return(null);
        byte[] b = new byte[l];
        for (int i=0; i<l; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    protected byte[] getFixedLengthBitEncodedData(int len)
    {
        if (bytes.length < ptr+len) return(null);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(b);
    }
    
    String getFixedLengthString(int len)
    {
        if (bytes.length < ptr+len) return(null);
        byte[] b = new byte[len];
        for (int i=0; i<len; i++) b[i] = bytes[ptr++];
        return(new String(b));
    }
    
    protected byte[] getDn()
    {
        if (bytes.length < ptr+2) return(null);
        byte b0 = bytes[ptr++];
        byte b1 = bytes[ptr++];
        int l = 0;
        switch (StdfReader.cpuType)
        {
            case SUN: l |= (b1 & 0xFF); l |= ((b0 & 0xFF) << 8); break;
            case VAX:
            default: l |= (b0 & 0xFF); l |= ((b1 & 0xFF) << 8);
        }
        byte[] b = new byte[l];
        int length = (l % 8 == 0) ? l/8 : l/8 + 1;
        if (bytes.length < ptr+length) return(null);
        for (int i=0; i<length; i++) 
        {
            if (bytes.length > ptr+1) b[i] = bytes[ptr++];
        }
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
        

}
