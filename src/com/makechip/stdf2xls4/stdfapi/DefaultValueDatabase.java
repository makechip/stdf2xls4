package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.IdentityHashMap;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.PinMapRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;

public final class DefaultValueDatabase
{
	public final boolean fusionCx;
	public final long timeStamp;

    // for PTR and MPR
    public final IdentityHashMap<TestID, Set<OptFlag_t>> optDefaults;
    public final IdentityHashMap<TestID, Byte> resScalDefaults;
    public final IdentityHashMap<TestID, Byte> llmScalDefaults;
    public final IdentityHashMap<TestID, Byte> hlmScalDefaults;
    public final IdentityHashMap<TestID, Float> loLimDefaults;
    public final IdentityHashMap<TestID, Float> hiLimDefaults;
    public final IdentityHashMap<TestID, String> unitDefaults;
    // pin index map:
    //     site                head               index             pin name
    final TShortObjectHashMap<TShortObjectHashMap<TIntObjectHashMap<String>>> pinMaps;
    
    void clearDefaults()
    {
    	optDefaults.clear();
    	resScalDefaults.clear();
    	llmScalDefaults.clear();
    	hlmScalDefaults.clear();
    	loLimDefaults.clear();
    	hiLimDefaults.clear();
    	unitDefaults.clear();
    }
    
    public DefaultValueDatabase(boolean fusionCx, long timeStamp)
    {
    	this.timeStamp = timeStamp;
    	this.fusionCx = fusionCx;
    	optDefaults = new IdentityHashMap<>(1000);
    	resScalDefaults = new IdentityHashMap<>(100);
    	llmScalDefaults = new IdentityHashMap<>(100);
    	hlmScalDefaults = new IdentityHashMap<>(100);
    	loLimDefaults = new IdentityHashMap<>(100);
    	hiLimDefaults = new IdentityHashMap<>(100);
    	unitDefaults = new IdentityHashMap<>(100);
    	pinMaps = new TShortObjectHashMap<>(100);
    }
    
    void loadDefaults(ParametricRecord r)
    {
    	TestID id = r.getTestID();
    	if (optDefaults.get(id) == null && r.getOptFlags() != null) optDefaults.put(id, r.getOptFlags());
    	if (resScalDefaults.get(id) == null && r.getResScal() != null) resScalDefaults.put(id, r.getResScal());
    	if (llmScalDefaults.get(id) == null && r.getLlmScal() != null) llmScalDefaults.put(id, r.getLlmScal());
    	if (hlmScalDefaults.get(id) == null && r.getHlmScal() != null) hlmScalDefaults.put(id, r.getHlmScal());
    	if (loLimDefaults.get(id) == null && r.getLoLimit() != null)  loLimDefaults.put(id, r.getLoLimit());
    	if (hiLimDefaults.get(id) == null && r.getHiLimit() != null) hiLimDefaults.put(id, r.getHiLimit());
    	if (unitDefaults.get(id) == null && r.getUnits() != null) unitDefaults.put(id, r.getUnits());
    }
   
    public String getPinName(short site, short head, int index)
    {
    	TShortObjectHashMap<TIntObjectHashMap<String>> m1 = pinMaps.get(site);
        if (m1 == null) return(null);
        TIntObjectHashMap<String> m2 = m1.get(head);
        if (m2 == null) return(null);
        return(m2.get(index));
    }
   
    void setPinName(PinMapRecord r)
    {
    	TShortObjectHashMap<TIntObjectHashMap<String>> m1 = pinMaps.get(r.siteNumber);
    	if (m1 == null)
    	{
    		m1 = new TShortObjectHashMap<>();
    		pinMaps.put(r.siteNumber, m1);
    	}
    	TIntObjectHashMap<String> m2 = m1.get(r.headNumber);
    	if (m2 == null)
    	{
    		m2 = new TIntObjectHashMap<>();
    		m1.put(r.headNumber, m2);
    	}
    	m2.put(r.pmrIdx, fusionCx ? r.physicalPinName : r.channelName);
    }
    
    
    protected int findScale(TestID id)
    {
        Float loLimit = loLimDefaults.get(id);
        Float hiLimit = hiLimDefaults.get(id);
        float val = 0.0f;
        if (loLimit == null) val = Math.abs(hiLimit);
        else if (hiLimit == null) val = Math.abs(loLimit);
        else val = (Math.abs(hiLimit) > Math.abs(loLimit)) ? Math.abs(hiLimit) : Math.abs(loLimit);
        int scale = 0;
        if (val <= 1.0E-6f) scale = 9;
        else if (val <= 0.001f) scale = 6;
        else if (val <= 1.0f) scale = 3;
        else if (val <= 1000.0f) scale = 0;
        else if (val <= 1000000.0f) scale = -3;
        else if (val <= 1E9f) scale = -6;
        else scale = -9;
        return(scale);
    }

    protected int getScale(float result, Float hiLimit, byte resScal, byte llmScal, byte hlmScal)
    {
        int scale = 0;
        if (result != 0.0f) scale = (int) resScal;
        else if (hiLimit != null) scale = (int) hlmScal;
        else scale = (int) llmScal;
        return(scale);
    }
   
    protected Float scaleValue(Float value, int scale)
    {
        if (value == null) return(value);
        switch (scale)
        {
        case -9: value = value.floatValue() / 1E9f; break;
        case -6: value = value.floatValue() / 1E6f; break;
        case -3: value = value.floatValue() / 1E3f; break;
        case  3: value = value.floatValue() * 1E3f; break;
        case  6: value = value.floatValue() * 1E6f; break;
        case  9: value = value.floatValue() * 1E9f; break;
        case 12: value = value.floatValue() * 1E12f; break;
        default:
        }
        return(value);
    }
    
    protected String scaleUnits(String units, int scale)
    {
        String u = units;
        switch (scale)
        {
        case -9: u = "G" + units; break;
        case -6: u = "M" + units; break;
        case -3: u = "k" + units; break;
        case  3: u = "m" + units; break;
        case  6: u = "u" + units; break;
        case  9: u = "n" + units; break;
        case 12: u = "p" + units; break;
        default:
        }
        return(u);
    }

}
