package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class ParametricTestHeader extends TestHeader
{
	public final String units;
	public final Float loLimit;
	public final Float hiLimit;

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
		builder.append(", testName=").append(testName);
		builder.append(", testNumber=").append(testNumber);
		builder.append(", dupNum=").append(dupNum);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public String getPin() { return(null); }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
//		result = prime * result + ((hiLimit == null) ? 0 : Float.floatToIntBits(hiLimit));
//		result = prime * result + ((loLimit == null) ? 0 : Float.floatToIntBits(loLimit));
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
//		if (loLimit == null)
//		{
//			if (other.loLimit != null) return false;
//		}
//		else if (other.loLimit == null) return false;
//		else if (Float.floatToIntBits(loLimit) != Float.floatToIntBits(other.loLimit)) return false;
//		if (hiLimit == null)
//		{
//			if (other.hiLimit != null) return false;
//		}
//		else if (other.hiLimit == null) return false;
//		else if (Float.floatToIntBits(hiLimit) != Float.floatToIntBits(other.hiLimit)) return false;
		if (!units.equals(other.units)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	
}
