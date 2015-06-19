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

	public ParametricTestHeader(TestID id, String units, float loLimit)
	{
		this(id, units, loLimit, StdfRecord.MISSING_FLOAT);
	}
	
	public ParametricTestHeader(TestID id, float hiLimit, String units)
	{
		this(id, units, StdfRecord.MISSING_FLOAT, hiLimit);
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
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ParametricTestHeader)
		{
			ParametricTestHeader p = ParametricTestHeader.class.cast(o);
			if (!units.equals(p.units)) return(false);
			if (loLimit != p.loLimit) return(false);
			if (hiLimit != p.hiLimit) return(false);
			if (noLoLimit != p.noLoLimit) return(false);
			return(noHiLimit == p.noHiLimit);
		}
		return(false);
	}
	
	@Override
	public int hashCode()
	{
		int h = super.id.hashCode() ^ units.hashCode() ^ Float.floatToIntBits(loLimit);
		h ^= Float.floatToIntBits(hiLimit);
		h ^= noLoLimit ? 123456789 : 987654321;
		h ^= noHiLimit ? 123456789 : 987654321;
		return(h);
	}

}
