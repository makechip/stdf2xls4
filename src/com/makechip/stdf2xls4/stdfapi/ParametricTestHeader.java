package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.StdfRecord;
import com.makechip.stdf2xls4.stdf.TestID;

public class ParametricTestHeader extends TestHeader
{
	public final String units;
	public final float loLimit;
	public final float hiLimit;
	public final boolean noLoLimit;
	public final boolean noHiLimit;

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
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ParametricTestHeader)) return false;
		ParametricTestHeader other = (ParametricTestHeader) obj;
		if (Float.floatToIntBits(hiLimit) != Float.floatToIntBits(other.hiLimit)) return false;
		if (Float.floatToIntBits(loLimit) != Float.floatToIntBits(other.loLimit)) return false;
		if (units == null)
		{
			if (other.units != null) return false;
		} 
		else if (!units.equals(other.units)) return false;
		return true;
	}

	public ParametricTestHeader(TestID id, String units, float loLimit, float hiLimit)
	{
		super(id);
		this.units = units;
		this.loLimit = loLimit;
		this.hiLimit = hiLimit;
		this.noLoLimit = loLimit == StdfRecord.MISSING_FLOAT;
		this.noHiLimit = hiLimit == StdfRecord.MISSING_FLOAT;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ParametricTestHeader [");
		builder.append("id=").append(id.toString()).append(", ");
		if (units != null)
		{
			builder.append("units=");
			builder.append(units);
			builder.append(", ");
		}
		builder.append("loLimit=");
		builder.append(loLimit);
		builder.append(", hiLimit=");
		builder.append(hiLimit);
		builder.append("]");
		return builder.toString();
	}
	
}
