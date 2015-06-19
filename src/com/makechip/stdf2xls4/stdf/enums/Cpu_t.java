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

/**
*** @author eric
*** @version $Id: Cpu_t.java 248 2008-10-19 16:48:59Z ericw $
**/
public enum Cpu_t
{
    VAX(0),
    SUN(1),
    PC(2);
    
    private final byte type_;

    private Cpu_t(int type)
    {
    	this.type_ = (byte) type;
    }
    
    public byte getType() { return(type_); }
    
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
    
    public int getU2(byte _0, byte _1) // same as "getUnsignedInt()"
    {
    	if (this == SUN)
    	{
    		return((_1 &0xFF) + ((_0 & 0xFF) << 8));
    	}
    	return((_0 & 0xFF) + ((_1 & 0xFF) << 8));
    }
   
    public long getU4(byte _0, byte _1, byte _2, byte _3)
    {
        if (this == SUN)
        {
        	return((_3 & 0xFFL) + ((_2 & 0xFFL) << 8) + ((_1 & 0xFFL) << 16) + ((_0 & 0xFFL) << 24));
        }
        return((_0 & 0xFFL) + ((_1 & 0xFFL) << 8) + ((_2 & 0xFFL) << 16) + ((_3 & 0xFFL) << 24));
    }
    
    public short getI2(byte _0, byte _1)
    {
        if (this == SUN) return((short) ((_1 & 0xFF) + ((_0 & 0xFF) << 8)));
        return((short) ((_0 & 0xFF) + ((_1 & 0xFF) << 8)));
    }
    
    public int getI4(byte _0, byte _1, byte _2, byte _3)
    {
        if (this == SUN)
        {
        	return((_3 & 0xFF) + ((_2 & 0xFF) << 8) + ((_1 & 0xFF) << 16) + ((_0 & 0xFF) << 24));
        }
        return((_0 & 0xFF) + ((_1 & 0xFF) << 8) + ((_2 & 0xFF) << 16) + ((_3 & 0xFF) << 24));
    }
    
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
    
    public byte[] getU2Bytes(int value)
    {
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
    
    public byte[] getU4Bytes(long v)
    {
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
    
    public byte[] getR4Bytes(float val)
    {
    	int value = Float.floatToIntBits(val);
    	byte[] b = new byte[4];
    	if (this == SUN)
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
    
    public byte[] getDnBytes(byte[] dn)
    {
    	int l = dn.length * 8;
    	byte[] b = new byte[dn.length + 2];
    	if (this == SUN)
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
    

    
    
    
    
    
    
    
    

}
