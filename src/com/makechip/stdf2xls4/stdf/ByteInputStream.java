package com.makechip.stdf2xls4.stdf;

public class ByteInputStream
{
	private byte[] b;
	private int ptr;

	public ByteInputStream(byte[] b)
	{
		this.b = b;
		ptr = 0;
	}
	
	public int available()
	{
		return(b.length - ptr);
	}
	
	public byte readByte()
	{
		byte n = b[ptr];
		ptr++;
		return(n);
	}
	
	public byte[] readFully(byte[] bs)
	{
		if (bs.length > available())
		{
			byte[] n = new byte[available()];
			for (int i=0; i<n.length; i++) n[i] = b[ptr++];
			return(n);
		}
		for (int i=0; i<bs.length; i++) bs[i] = b[ptr++];
		return(bs);
	}

}
