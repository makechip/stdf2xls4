package com.makechip.stdf2xls4.stdf;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;

public class FloatList
{
	private float[] array;

	public FloatList(Cpu_t cpu, final int num, ByteInputStream is)
	{
		array = new float[num];
		for (int i=0; i<num; i++) array[i] = cpu.getR4(is);
	}
	
	public FloatList(float[] a)
	{
	    array = Arrays.copyOf(a, a.length);
	}
	
	public int size() { return(array.length); }
	
	public float get(int index) { return(array[index]); }
	
	public DoubleStream stream() 
	{ 
		double[] a = new double[array.length];
		IntStream.range(0, array.length).forEach(p -> a[p] = array[p]);
		return(Arrays.stream(a)); 
	}
	
	float[] getArray() { return(Arrays.copyOf(array, array.length)); }
	
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
		if (!(obj instanceof FloatList)) return false;
		FloatList other = (FloatList) obj;
		if (!Arrays.equals(array, other.array)) return false;
		return true;
	}

}
