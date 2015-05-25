package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.IdentityHashMap;

import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

final class IdentityDatabase
{
	public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, Integer>> testIdDupMap;
	public final HashMap<HashMap<String, String>, IdentityFactoryLON<String, TestID>> idMap;
    public final HashMap<HashMap<String, String>, IdentityFactoryIO<TestID, String, PinTestID>> testIdPinMap;

    // default value maps for MPR and PTR types
    public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, EnumSet<OptFlag_t>>> poptDefaults;
    public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, String>> unitDefaults;
    public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, String>> scaledUnits;

    public final HashMap<HashMap<String, String>, TObjectByteHashMap<TestID>> resScalDefaults;
    public final HashMap<HashMap<String, String>, TObjectByteHashMap<TestID>> llmScalDefaults;
    public final HashMap<HashMap<String, String>, TObjectByteHashMap<TestID>> hlmScalDefaults;

    public final HashMap<HashMap<String, String>, TObjectFloatHashMap<TestID>> loLimDefaults;
    public final HashMap<HashMap<String, String>, TObjectFloatHashMap<TestID>> hiLimDefaults;
    public final HashMap<HashMap<String, String>, TObjectFloatHashMap<TestID>> scaledLoLimits;
    public final HashMap<HashMap<String, String>, TObjectFloatHashMap<TestID>> scaledHiLimits;

    public final HashMap<HashMap<String, String>, TObjectIntHashMap<TestID>> scales;
    
    // default value maps for MPR 
    public final HashMap<HashMap<String, String>, TShortObjectHashMap<TIntObjectHashMap<String>>> pinmap;
    public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, PinList>> mprPins;
    public final HashMap<HashMap<String, String>, IdentityHashMap<TestID, int[]>> rtnIndexDefaults;
    
    public int[] getDefaultRtnIndex(HashMap<String, String> hdr, TestID id)
    {
    	IdentityHashMap<TestID, int[]> m = rtnIndexDefaults.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		rtnIndexDefaults.put(hdr, m);
    	}
    	return(m.get(id));
    }
    
    public void setDefaultRtnIndex(HashMap<String, String> hdr, TestID id, int[] idx)
    {
    	IdentityHashMap<TestID, int[]> m = rtnIndexDefaults.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		rtnIndexDefaults.put(hdr, m);
    	}
    	m.put(id, idx);
    }
    
    public PinList getPinList(HashMap<String, String> hdr, TestID id)
    {
    	IdentityHashMap<TestID, PinList> m = mprPins.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		mprPins.put(hdr,  m);
    	}
    	return(m.get(id));
    }
    
    public void setPinList(HashMap<String, String> hdr, TestID id, PinList p)
    {
    	IdentityHashMap<TestID, PinList> m = mprPins.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		mprPins.put(hdr,  m);
    	}
    	m.put(id, p);
    }
    
    public String getPin(HashMap<String, String> hdr, short site, int rtnIdx)
    {
    	TShortObjectHashMap<TIntObjectHashMap<String>> m1 = pinmap.get(hdr);
    	if (m1 == null)
    	{
    		m1 = new TShortObjectHashMap<>();
    		pinmap.put(hdr, m1);
    	}
    	TIntObjectHashMap<String> m2 = m1.get(site);
    	if (m2 == null)
    	{
    		m2 = new TIntObjectHashMap<String>();
    		m1.put(site, m2);
    	}
    	return(m2.get(rtnIdx));
    }
    
    public void setPin(HashMap<String, String> hdr, short site, int rtnIdx, String pin)
    {
    	TShortObjectHashMap<TIntObjectHashMap<String>> m1 = pinmap.get(hdr);
    	if (m1 == null)
    	{
    		m1 = new TShortObjectHashMap<>();
    		pinmap.put(hdr, m1);
    	}
    	TIntObjectHashMap<String> m2 = m1.get(site);
    	if (m2 == null)
    	{
    		m2 = new TIntObjectHashMap<String>();
    		m1.put(site, m2);
    	}
    	m2.put(rtnIdx, pin);
    }
    
    public int getScale(HashMap<String, String> hdr, TestID id)
    {
    	TObjectIntHashMap<TestID> m = scales.get(hdr);
    	if (m == null)
    	{
    		m = new TObjectIntHashMap<>();
    		scales.put(hdr, m);
    	}
    	return(m.get(id));
    }
    
    public void setScale(HashMap<String, String> hdr, TestID id, int scale)
    {
    	TObjectIntHashMap<TestID> m = scales.get(hdr);
    	if (m == null)
    	{
    		m = new TObjectIntHashMap<>();
    		scales.put(hdr, m);
    	}
    	m.put(id, scale);
    }
    
    public EnumSet<OptFlag_t> getDefaultOptFlags(HashMap<String, String> hdr, TestID id)
    {
    	IdentityHashMap<TestID, EnumSet<OptFlag_t>> m = poptDefaults.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		poptDefaults.put(hdr,  m);
    	}
    	return(m.get(id));
    }
    
    public void setDefaultOptFlags(HashMap<String, String> hdr, TestID id, EnumSet<OptFlag_t> optDef)
    {
    	IdentityHashMap<TestID, EnumSet<OptFlag_t>> m = poptDefaults.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		poptDefaults.put(hdr, m);
    	}
    	m.put(id, optDef);
    }
   
    private IdentityHashMap<TestID, String> getStringMap(HashMap<String, String> hdr, HashMap<HashMap<String, String>, IdentityHashMap<TestID, String>> map)
    {
    	IdentityHashMap<TestID, String> m = map.get(hdr);
    	if (m == null)
    	{
    		m = new IdentityHashMap<>();
    		map.put(hdr, m);
    	}
    	return(m);
    }
    
    public String getDefaultUnits(HashMap<String, String> hdr, TestID id)
    {
    	IdentityHashMap<TestID, String> m = getStringMap(hdr, unitDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultUnits(HashMap<String, String> hdr, TestID id, String units)
    {
    	IdentityHashMap<TestID, String> m = getStringMap(hdr, unitDefaults);
    	m.put(id, units);
    }

    public String getScaledUnits(HashMap<String, String> hdr, TestID id)
    {
    	IdentityHashMap<TestID, String> m = getStringMap(hdr, scaledUnits);
    	return(m.get(id));
    }
    
    public void setScaledUnits(HashMap<String, String> hdr, TestID id, String units)
    {
    	IdentityHashMap<TestID, String> m = getStringMap(hdr, scaledUnits);
    	m.put(id, units);
    }

    private TObjectByteHashMap<TestID> getMap(HashMap<String, String> hdr, HashMap<HashMap<String, String>, TObjectByteHashMap<TestID>> map)
    {
    	TObjectByteHashMap<TestID> m = map.get(hdr);
    	if (m == null)
    	{
    		m = new TObjectByteHashMap<>();
    		map.put(hdr,  m);
    	}
    	return(m);
    }
    
    public byte getDefaultResScal(HashMap<String, String> hdr, TestID id)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, resScalDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultResScale(HashMap<String, String> hdr, TestID id, byte resScal)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, resScalDefaults);
        m.put(id, resScal);    	
    }
    
    public byte getDefaultLlmScal(HashMap<String, String> hdr, TestID id)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, llmScalDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultLlmScale(HashMap<String, String> hdr, TestID id, byte resScal)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, llmScalDefaults);
        m.put(id, resScal);    	
    }
    
    public byte getDefaultHlmScal(HashMap<String, String> hdr, TestID id)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, hlmScalDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultHlmScale(HashMap<String, String> hdr, TestID id, byte resScal)
    {
    	TObjectByteHashMap<TestID> m = getMap(hdr, hlmScalDefaults);
        m.put(id, resScal);    	
    }
    
    private TObjectFloatHashMap<TestID> getFloatMap(HashMap<String, String> hdr, HashMap<HashMap<String, String>, TObjectFloatHashMap<TestID>> map)
    {
    	TObjectFloatHashMap<TestID> m = map.get(hdr);
    	if (m == null)
    	{
    		m = new TObjectFloatHashMap<>();
    		map.put(hdr, m);
    	}
    	return(m);
    }
    
    public float getDefaultLoLimit(HashMap<String, String> hdr, TestID id)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, loLimDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultLoLimit(HashMap<String, String> hdr, TestID id, float loLimit)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, loLimDefaults);
    	m.put(id, loLimit);
    }
    
    public float getDefaultHiLimit(HashMap<String, String> hdr, TestID id)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, hiLimDefaults);
    	return(m.get(id));
    }
    
    public void setDefaultHiLimit(HashMap<String, String> hdr, TestID id, float loLimit)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, hiLimDefaults);
    	m.put(id, loLimit);
    }
    
    public float getScaledLoLimit(HashMap<String, String> hdr, TestID id)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, scaledLoLimits);
    	return(m.get(id));
    }
    
    public void setScaledLoLimit(HashMap<String, String> hdr, TestID id, float loLimit)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, scaledLoLimits);
    	m.put(id, loLimit);
    }
    
    public float getScaledHiLimit(HashMap<String, String> hdr, TestID id)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, scaledHiLimits);
    	return(m.get(id));
    }
    
    public void setScaledHiLimit(HashMap<String, String> hdr, TestID id, float loLimit)
    {
    	TObjectFloatHashMap<TestID> m = getFloatMap(hdr, scaledHiLimits);
    	m.put(id, loLimit);
    }
    
    
	public IdentityDatabase()
	{
		testIdDupMap     = new HashMap<>();
		idMap            = new HashMap<>();
	    testIdPinMap     = new HashMap<>();
        poptDefaults     = new HashMap<>();
        resScalDefaults  = new HashMap<>();
        llmScalDefaults  = new HashMap<>();
        hlmScalDefaults  = new HashMap<>();
        loLimDefaults    = new HashMap<>();
        hiLimDefaults    = new HashMap<>();
        unitDefaults     = new HashMap<>();
        scaledLoLimits   = new HashMap<>();
        scaledHiLimits   = new HashMap<>();
        scaledUnits      = new HashMap<>();
        scales           = new HashMap<>();
        pinmap           = new HashMap<>();
        mprPins          = new HashMap<>();
        rtnIndexDefaults = new HashMap<>();
	}

}
