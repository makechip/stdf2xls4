package com.makechip.stdf2xls4.stdfapi;

public class DeviceHeader
{
	public final SnOrXy snxy;
	public final int hwBin;
	public final int swBin;
	public final boolean fail;
	public final boolean abnormalEOT;
	public final boolean noPassFailIndication;
	public final String temperature;

	public DeviceHeader(SnOrXy snxy, 
			            int hwBin, 
			            int swBin, 
			            boolean fail, 
			            boolean abnormalEOT, 
			            boolean noPassFailIndication, 
			            String temperature)
	{
		this.snxy = snxy;
		this.hwBin = hwBin;
		this.swBin = swBin;
		this.fail = fail;
		this.abnormalEOT = abnormalEOT;
		this.noPassFailIndication = noPassFailIndication;
		this.temperature = temperature;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof DeviceHeader)
		{
			DeviceHeader dh = DeviceHeader.class.cast(o);
			if (snxy != dh.snxy) return(false);
			if (hwBin != dh.hwBin) return(false);
			if (swBin != dh.swBin) return(false);
			if (fail != dh.fail) return(false);
			if (abnormalEOT != dh.abnormalEOT) return(false);
			if (noPassFailIndication != dh.noPassFailIndication) return(false);
			if (!temperature.equals(dh.temperature)) return(false);
			return(true);
		}
		return(false);
	}
	
	@Override
	public int hashCode()
	{
		int h = snxy.hashCode() ^ hwBin ^ swBin ^ temperature.hashCode();
		if (abnormalEOT) h = (h % 1584773) * 143; else h = (h  % 7394511) * 117;
		if (fail) h = (h % 493321777) * 57; else h = (h % 19776931) * 79;
		if (noPassFailIndication) h = h ^ 0xAAAAAAAA; else h = h ^ 0x55555555;
		return(h);
	}

}
