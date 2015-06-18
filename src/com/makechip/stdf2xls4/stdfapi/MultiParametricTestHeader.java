package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID.PinTestID;
import com.makechip.stdf2xls4.stdf.StdfRecord;

public class MultiParametricTestHeader extends ParametricTestHeader
{
	public final String pin;

	public MultiParametricTestHeader(PinTestID id, String units, float loLimit)
	{
		this(id, units, loLimit, StdfRecord.MISSING_FLOAT);
	}

	public MultiParametricTestHeader(PinTestID id, float hiLimit, String units)
	{
		this(id, units, StdfRecord.MISSING_FLOAT, hiLimit);
	}

	public MultiParametricTestHeader(PinTestID id, String units, float loLimit, float hiLimit)
	{
		super(id.id, units, loLimit, hiLimit);
		this.pin = id.pin;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof MultiParametricTestHeader)
		{
			MultiParametricTestHeader p = MultiParametricTestHeader.class.cast(o);
			if (!super.equals(p)) return(false);
			return(pin.equals(p.pin));
		}
		return(false);
	}
	
	@Override
	public int hashCode()
	{
		return(super.hashCode() ^ pin.hashCode());
	}

}
