package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.TestID.PinTestID;

public class MultiParametricTestHeader extends ParametricTestHeader
{
	public static final String LL_HDR = "LoLimit";
	public static final String HL_HDR = "HiLimit";
	public final String pin;

	public MultiParametricTestHeader(PinTestID id, String units, Float loLimit, Float hiLimit)
	{
		this(id.testName, id.testNumber, id.dupNum, ((PinTestID) id).pin, units, loLimit, hiLimit);
	}
	
	public MultiParametricTestHeader(String tname, long tnum, int dupNum, String pin, String units, Float loLimit, Float hiLimit)
	{
	    super(tname, tnum, dupNum, units, loLimit, hiLimit);	
	    this.pin = pin;
	}
	
	public MultiParametricTestHeader(String tname, long tnum, int dupNum, String units, Limit_t limit)
	{
	    this(tname, tnum, dupNum, limit.name, units, null, null);	
	}
	
	public MultiParametricTestHeader(TestID id, String units, Limit_t limit)
	{
		this(id.testName, id.testNumber, id.dupNum, units, limit);
	}
	
	@Override
	public boolean isLoLimitHeader()
	{
		return(pin.equals(LL_HDR));
	}
	
	@Override
	public boolean isHiLimitHeader()
	{
		return(pin.equals(HL_HDR));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MultiParametricTestHeader [").append("pin=").append(pin);
		builder.append(", ").append("units=").append(units);
		builder.append(", ").append("loLimit=").append(loLimit);
		builder.append(", hiLimit=").append(hiLimit);
		builder.append(", noLoLimit=").append(noLoLimit);
		builder.append(", noHiLimit=").append(noHiLimit);
		builder.append(", ");
		if (testName != null)
		{
			builder.append("testName=").append(testName);
			builder.append(", ");
		}
		builder.append("testNumber=").append(testNumber);
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
		result = prime * result + pin.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof MultiParametricTestHeader)) return false;
		MultiParametricTestHeader other = (MultiParametricTestHeader) obj;
		if (!pin.equals(other.pin)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}
	

}
