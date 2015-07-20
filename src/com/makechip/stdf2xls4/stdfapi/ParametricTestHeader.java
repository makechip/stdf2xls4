package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class ParametricTestHeader extends TestHeader
{
	public final String units;
	public final float loLimit;
	public final float hiLimit;
	public final boolean noLoLimit;
	public final boolean noHiLimit;

	public ParametricTestHeader(TestID id, String units, Float loLimit, Float hiLimit)
	{
        this(id.testName, id.testNumber, id.dupNum, units, loLimit, hiLimit);	
	}
	
	public ParametricTestHeader(String testName, long testNumber, int dupNum, String units, Float loLimit, Float hiLimit)
	{
		super(testName, testNumber, dupNum);
		this.units = units;
		this.loLimit = loLimit;
		this.hiLimit = hiLimit;
		this.noLoLimit = loLimit == null;
		this.noHiLimit = hiLimit == null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ParametricTestHeader [units=").append(units);
		builder.append(", loLimit=").append(loLimit);
		builder.append(", hiLimit=").append(hiLimit);
		builder.append(", noLoLimit=").append(noLoLimit);
		builder.append(", noHiLimit=").append(noHiLimit);
		builder.append(", testName=").append(testName);
		builder.append(", testNumber=").append(testNumber);
		builder.append(", dupNum=").append(dupNum);
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
		result = prime * result + Float.floatToIntBits(hiLimit);
		result = prime * result + Float.floatToIntBits(loLimit);
		result = prime * result + (noHiLimit ? 1231 : 1237);
		result = prime * result + (noLoLimit ? 1231 : 1237);
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof ParametricTestHeader)) return false;
		ParametricTestHeader other = (ParametricTestHeader) obj;
		if (Float.floatToIntBits(hiLimit) != Float.floatToIntBits(other.hiLimit)) return false;
		if (Float.floatToIntBits(loLimit) != Float.floatToIntBits(other.loLimit)) return false;
		if (noHiLimit != other.noHiLimit) return false;
		if (noLoLimit != other.noLoLimit) return false;
		if (!units.equals(other.units)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	
}
