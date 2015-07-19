package com.makechip.stdf2xls4.stdf;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public class IntList
{
	private int[] array;

	public IntList(Data_t type, Cpu_t cpu, final int num, ByteInputStream is)
	{
		array = new int[num];
		switch (type)
		{
		case U2:
			IntStream.range(0,  num).forEach(p -> array[p] = cpu.getU2(is));
			break;
		case U1:
			array = new int[num];
			IntStream.range(0,  num).forEach(p -> array[p] = cpu.getU1(is));
			break;
		case N1:
			IntStream.range(0,  (num+1)/2).forEach(p -> assignNibbles(cpu, p, is));
			break;
		default: throw new RuntimeException("Error: Unsupported type for IntList: " + type);
		}
	}
	
    private void assignNibbles(Cpu_t cpu, int index, ByteInputStream is)
    {
    	byte[] b = cpu.getN1(is);
    	array[2*index] = b[0];
    	if ((2*index < array.length -1) || (array.length % 2 == 0)) array[2*index+1] = b[1];
    }

	public IntList(short[] a)
	{
	    array = new int[a.length];
		IntStream.range(0, a.length).forEach(p -> array[p] = a[p]);
	}
	
	public IntList(byte[] a)
	{
	    array = new int[a.length];
		IntStream.range(0, a.length).forEach(p -> array[p] = a[p]);
	}
	
	public IntList(int[] a)
	{
		array = new int[a.length];
		IntStream.range(0, a.length).forEach(p -> array[p] = a[p]);
	}
	
	public int size() { return(array.length); }
	
	public int get(int index) { return(array[index]); }
	
	public IntStream stream() { return(Arrays.stream(array)); }
	
	int[] getArray() { return(Arrays.copyOf(array, array.length)); }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return(Arrays.toString(array));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(array);
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
		if (!(obj instanceof IntList)) return false;
		IntList other = (IntList) obj;
		if (!Arrays.equals(array, other.array)) return false;
		return true;
	}

}
