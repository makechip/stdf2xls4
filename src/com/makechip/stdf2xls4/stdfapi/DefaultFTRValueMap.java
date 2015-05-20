package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TLongShortHashMap;

import java.util.EnumSet;

import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;

public class DefaultFTRValueMap
{
    // for FTR
    TLongShortHashMap pgDefaults;
    TLongObjectHashMap<byte[]> ecDefaults;
    TLongObjectHashMap<EnumSet<FTROptFlag_t>> foptDefaults;
    TLongObjectHashMap<String> tnameDefaults;
    
	public DefaultFTRValueMap()
	{
        pgDefaults        = new TLongShortHashMap();
        ecDefaults        = new TLongObjectHashMap<byte[]>();
        foptDefaults      = new TLongObjectHashMap<EnumSet<FTROptFlag_t>>();
        tnameDefaults     = new TLongObjectHashMap<String>();
    }
	    
}
