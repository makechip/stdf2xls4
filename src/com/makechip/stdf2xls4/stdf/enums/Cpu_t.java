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

package com.makechip.stdf2xls4.stdf.enums;

import java.io.DataInputStream;
import java.io.IOException;

import com.makechip.stdf2xls4.stdf.ByteInputStream;


/**
 *  This enum represents the CPU_TYPE values used in the File Attributes Record.
 *  It determines the byte order used in converting bytes to various number types.
 *  @author eric
 */
public enum Cpu_t
{
    VAX(0),
    SUN(1),
    PC(2);
    
    /**
     * The byte value used in the STDF stream to identify the CPU type.
     * 0 for VAX computers, 1 for Sun 1, 2, 3, and 4 computers, or 2 for IBM-PC compatible computers.
     */
    public final byte type;

    private Cpu_t(int type)
    {
    	this.type = (byte) type;
    }
    
    /**
     * Given a byte value from the File Attributes Record CPU_TYPE field
     * this method returns the corresponding Cpu_t enum value.
     * @param val The byte value from the CPU_TYPE field.
     * @return The corresponding Cpu_t enum value.
     */
    public static Cpu_t getCpuType(byte val)
    {
        Cpu_t type = null;
        switch (val)
        {
            case 0: type = VAX; break;
            case 1: type = SUN; break;
            case 2: type = PC;  break;
            default: throw new RuntimeException("Invalid CPU type: " + val);
        }
        return(type);
    }
    
    /**
     * This converts a byte to an (unsigned) short.
     * @param _0 A single byte value.
     * @return A short value that is greater that or equal to 0, and less than 256.
     */
    public short getU1(byte _0)
    {
    	int s = 0xFF & _0;
    	return((short) s);
    }
    
    public short getU1(ByteInputStream is)
    {
    	int s = 0xFF & is.readByte();
    	return((short) s);
    }
    
    public short getU1(DataInputStream is) throws IOException
    {
    	int s = 0xFF & is.readByte();
    	return((short) s);
    }
    
    public byte[] getU1Bytes(short v)
    {
    	byte[] b = new byte[1];
    	b[0] = 0;
    	b[0] |= v;
    	return(b);
    }
    
    /**
     * This method converts two bytes to an unsigned short value
     * according to this CPU type.  Note that since java does not
     * support unsigned types, the type returned is actually a positive int.
     * @param _0 The byte occurring first in the stream.
     * @param _1 The byte occurring second in the stream.
     * @return The 16-bit unsigned value represented by
     * the two byte arguments and this Cpu_t.
     */
    public int getU2(byte _0, byte _1) // same as "getUnsignedInt()"
    {
    	if (this == SUN)
    	{
    		return((_1 & 0xFF) + ((_0 & 0xFF) << 8));
    	}
    	return((_0 & 0xFF) + ((_1 & 0xFF) << 8));
    }
   
	public int getU2(ByteInputStream is)
	{
		byte b0 = is.readByte();
		byte b1 = is.readByte();
		return(getU2(b0, b1));
	}

	public int getU2(DataInputStream is) throws IOException
	{
		byte b0 = is.readByte();
		byte b1 = is.readByte();
		return(getU2(b0, b1));
	}

    /**
     * This method converts an unsigned short value (represented 
     * with a java int type) to a two byte array.
     * @param value A 16-bit unsigned value. If the magnitude of the value
     * exceeds 16-bits or is less than 0 a RuntimeException will be thrown.
     * @return Two bytes in correct stream order.
     */
    public byte[] getU2Bytes(int value)
    {
    	if (value < 0 || value > 65535) throw new RuntimeException("Invalid value for 16-bit unsigned integer");
    	byte[] b = new byte[2];
    	if (this == SUN)
    	{
    	    b[0] = (byte) ((value & 0xFF00) >> 8);
    	    b[1] = (byte) (value & 0xFF);
    	}
    	else
    	{
    	    b[1] = (byte) ((value & 0xFF00) >> 8);
    	    b[0] = (byte) (value & 0xFF);
    	}
    	return(b);
    }
    
    /**
     * This method converts four bytes to an unsigned integer value
     * according to this CPU type.  Note that since java does not
     * support unsigned types, the type returned is actually a positive long.
     * @param _0 The byte occurring first in the stream.
     * @param _1 The byte occurring second in the stream.
     * @param _2 The byte occurring third in the stream.
     * @param _3 The byte occurring fourth in the stream.
     * @return The 32-bit unsigned value represented by the
     * four byte arguments and this Cpu_t.
     */
    public long getU4(byte _0, byte _1, byte _2, byte _3)
    {
        if (this == SUN)
        {
        	return((_3 & 0xFFL) + ((_2 & 0xFFL) << 8) + ((_1 & 0xFFL) << 16) + ((_0 & 0xFFL) << 24));
        }
        return((_0 & 0xFFL) + ((_1 & 0xFFL) << 8) + ((_2 & 0xFFL) << 16) + ((_3 & 0xFFL) << 24));
    }
    
    public long getU4(ByteInputStream is)
    {
    	byte b0 = is.readByte();
    	byte b1 = is.readByte();
    	byte b2 = is.readByte();
    	byte b3 = is.readByte();
    	return(getU4(b0, b1, b2, b3));
    }
    
    /**
     * This method converts an unsigned integer value (represented
     * with a java long type) to a four-byte array.
     * @param v A 32-bit unsigned value. If the value exceeds Integer.MAX_VALUE
     * or is less than 0 a RuntimeException will be thrown.
     * @return Four bytes in correct stream order.
     */
    public byte[] getU4Bytes(long v)
    {
    	if (v < 0L || v > ((long) Integer.MAX_VALUE)) throw new RuntimeException("Invalid value for 32-bit unsigned integer");
    	byte[] b = new byte[4];
    	if (this == SUN)
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
   
    /**
     * This is an identity method - it returns the argument.
     * @param _0 A single byte.
     * @return Returns the argument _0.
     */
    public byte getI1(byte _0)
    {
    	return(_0);
    }
    
    public byte getI1(ByteInputStream is)
    {
    	return(is.readByte());
    }
    
    public byte[] getI1Bytes(byte i1)
    {
    	byte[] b = new byte[1];
    	b[0] = i1;
    	return(b);
    }
    
    /**
     * This method converts two bytes to a signed short value
     * according to this CPU type. 
     * @param _0 The byte occurring first in the stream.
     * @param _1 The byte occurring second in the stream.
     * @return the 16-bit signed value represented by the two 
     * byte arguments and this Cpu_t.
     */
    public short getI2(byte _0, byte _1)
    {
        if (this == SUN) return((short) ((_1 & 0xFF) + ((_0 & 0xFF) << 8)));
        return((short) ((_0 & 0xFF) + ((_1 & 0xFF) << 8)));
    }
    
    public short getI2(ByteInputStream is)
    {
    	byte b0 = is.readByte();
    	byte b1 = is.readByte();
    	return(getI2(b0, b1));
    }
    
    /**
     * This method converts a signed short value to a two-byte array.
     * @param value A 16-bit signed integer value.
     * @return Two bytes in correct stream order.
     */
    public byte[] getI2Bytes(short value)
    {
    	byte[] b = new byte[2];
    	if (this == SUN)
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
   
    /**
     * This method converts four bytes to a signed integer value
     * according to this CPU type. 
     * @param _0 The byte occurring first in the stream.
     * @param _1 The byte occurring second in the stream.
     * @param _2 The byte occurring third in the stream.
     * @param _3 The byte occurring fourth in the stream.
     * @return The 32-bit signed value represented by the four
     * byte arguments and this Cpu_t.
     */
    public int getI4(byte _0, byte _1, byte _2, byte _3)
    {
        if (this == SUN)
        {
        	return((_3 & 0xFF) + ((_2 & 0xFF) << 8) + ((_1 & 0xFF) << 16) + ((_0 & 0xFF) << 24));
        }
        return((_0 & 0xFF) + ((_1 & 0xFF) << 8) + ((_2 & 0xFF) << 16) + ((_3 & 0xFF) << 24));
    }
    
    public int getI4(ByteInputStream is)
    {
    	byte b0 = is.readByte();
    	byte b1 = is.readByte();
    	byte b2 = is.readByte();
    	byte b3 = is.readByte();
    	return(getI4(b0, b1, b2, b3));
    }
    
    /**
     * This method converts a sighed integer value to a four-byte array.
     * @param value A 32-bit signed integer value.
     * @return Four bytes in correct stream order.
     */
    public byte[] getI4Bytes(int value)
    {
    	byte[] b = new byte[4];
    	if (this == SUN)
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
    
    /**
     * This method converts eight bytes to a signed long value
     * according to this CPU type.
     * @param _0 The byte occurring first in the stream.
     * @param _1 The byte occurring second in the stream.
     * @param _2 The byte occurring third in the stream.
     * @param _3 The byte occurring fourth in the stream.
     * @param _4 The byte occurring fifth in the stream.
     * @param _5 The byte occurring sixth in the stream.
     * @param _6 The byte occurring seventh in the stream.
     * @param _7 The byte occurring eighth in the stream.
     * @return The 64-bit signed value represented by the
     * eight byte arguments and this Cpu_t.
     */
    public long getLong(byte _0, byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7)
    {
        if (this == SUN) 
        {
        	return((_7 & 0xFFL) + ((_6 & 0xFFL) << 8) + ((_5 & 0xFFL) << 16) + ((_4 & 0xFFL) << 24) +
                   ((_3 & 0xFFL) << 32) + ((_2 & 0xFFL) << 40) + ((_1 & 0xFFL) << 48) + ((_0 & 0xFFL) << 56));
        }
        return((_0 & 0xFFL) + ((_1 & 0xFFL) << 8) + ((_2 & 0xFFL) << 16) + ((_3 & 0xFFL) << 24) +
               ((_4 & 0xFFL) << 32) + ((_5 & 0xFFL) << 40) + ((_6 & 0xFFL) << 48) + ((_7 & 0xFFL) << 56));
    }

    public float getR4(byte _0, byte _1, byte _2, byte _3)
    {
    	int l = getI4(_0, _1, _2, _3);
    	return(Float.intBitsToFloat(l));
    }
    
    public float getR4(ByteInputStream is)
    {
        byte b0 = is.readByte();
        byte b1 = is.readByte();
        byte b2 = is.readByte();
        byte b3 = is.readByte();
        return(getR4(b0, b1, b2, b3));
    }

    /**
     * This method converts a float value to a four-byte array.
     * @param val A float value.
     * @return Four bytes in correct stream order.  
     * Note that this probably does not work correctly for the VAX CPU type.
     */
    public byte[] getR4Bytes(float val)
    {
    	long value = (long) Float.floatToIntBits(val);
    	byte[] b = getU4Bytes(value);
    	return(b);
    }
    
    public double getR8(byte _0, byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7)
    {
    	long l = getLong(_0, _1, _2, _3, _4, _5, _6, _7);
    	return(Double.longBitsToDouble(l));
    }
    
    /**
     * Extract an eight-byte double value from the bytes array.
     * @param defaultValue The value to return if the end
     * of the bytes array has already been reached.
     * @return An eight-byte double value.
     */
    public double getR8(ByteInputStream is)
    {
        byte b0 = is.readByte();
        byte b1 = is.readByte();
        byte b2 = is.readByte();
        byte b3 = is.readByte();
        byte b4 = is.readByte();
        byte b5 = is.readByte();
        byte b6 = is.readByte();
        byte b7 = is.readByte();
        return(getR8(b0, b1, b2, b3, b4, b5, b6, b7));
    }
    
    /**
    * This method converts a double value to an eight-byte array.
     * @param val A double value.
     * @return Eight bytes in correct stream order.
     */
    public byte[] getR8Bytes(double val)
    {
    	long value = Double.doubleToLongBits(val);
    	byte[] b = new byte[8];
    	if (this == SUN)
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
    
    /**
     * This method is used to convert a bit field represented
     * as an array of bytes in stream order for the STDF Dn data type.
     * @param numBits  The number of bits in this bit field.
     * @param dn The array of bytes containing the bits of this bit field.
     * @return Basically the dn byte array pre-pended with two
     * bytes containing the bit count.
     */
    public byte[] getDNBytes(int numBits, byte[] dn)
    {
    	byte[] n = getU2Bytes(numBits);
    	byte[] b = new byte[dn.length + 2];
    	b[0] = n[0];
    	b[1] = n[1];
    	for (int i=2; i<dn.length+2; i++) b[i] = dn[i-2];
    	return(b);
    }
    
    public byte[] getDN(int numBits, ByteInputStream is)
    {
    	int length = (numBits % 8 == 0) ? numBits / 8 : 1 + numBits / 8;
    	byte[] b = new byte[length];
    	is.readFully(b);
    	return(b);
    }
    
    public String getCN(ByteInputStream is)
    {
        int l = 0xFF & is.readByte();
        if (l == 0) return("");
        byte[] b = new byte[l];
        is.readFully(b);
        return(new String(b));
    }
    
    public byte[] getCNBytes(String s)
    {
    	byte[] b = new byte[s.length()+1];
    	b[0] = 0;
    	b[0] |= s.length();
    	for (int i=0; i<s.length(); i++) b[i+1] = (byte) s.charAt(i);
    	return(b);
    }
    
    public byte[] getBN(ByteInputStream is)
    {
    	int l = 0xFF & is.readByte();
    	byte[] b = new byte[l];
    	is.readFully(b);
    	return(b);
    }
   
    public byte[] getBNBytes(byte[] bytes)
    {
    	byte[] b = new byte[1 + bytes.length];
    	b[0] = 0;
    	b[0] |= bytes.length;
    	for (int i=0; i<bytes.length; i++) b[i+1] = bytes[i];
    	return(b);
    }
    
    public byte[] getN1(ByteInputStream is)
    {
    	byte[] b = new byte[2];
    	byte n = is.readByte();
    	b[0] = (byte) (0x0F & n);
    	b[1] = (byte) ((0xF0 & n) >> 4);
    	return(b);
    }
    
    public byte getN1Byte(byte b0, byte b1)
    {
        byte b = 0;
        b = (byte) (b0 & 0x0F);
        b |= (byte) ((b1 & 0x0F) << 4);
        return(b);
    }
}
