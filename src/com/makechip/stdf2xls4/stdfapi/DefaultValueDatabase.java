package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.IdentityHashMap;
import java.util.Set;
import java.util.stream.IntStream;

import com.makechip.stdf2xls4.stdf.FloatList;
import com.makechip.stdf2xls4.stdf.IntList;
import com.makechip.stdf2xls4.stdf.MultipleResultParametricRecord;
import com.makechip.stdf2xls4.stdf.ParametricRecord;
import com.makechip.stdf2xls4.stdf.ParametricTestRecord;
import com.makechip.stdf2xls4.stdf.PinMapRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;

public final class DefaultValueDatabase
{
	public final boolean fusionCx;
	public final long timeStamp;

    // pin index map:
    //     site                head               index             pin name
    final TShortObjectHashMap<TShortObjectHashMap<TIntObjectHashMap<String>>> pinMaps;

    // for PTR and MPR
    public final IdentityHashMap<TestID, Set<OptFlag_t>> optDefaults;
    public final IdentityHashMap<TestID, Byte> resScalDefaults;
    public final IdentityHashMap<TestID, Byte> llmScalDefaults;
    public final IdentityHashMap<TestID, Byte> hlmScalDefaults;
    public final IdentityHashMap<TestID, Float> loLimDefaults;
    public final IdentityHashMap<TestID, Float> hiLimDefaults;
    public final IdentityHashMap<TestID, String> unitDefaults;
    public final IdentityHashMap<TestID, IntList> rtnIndexDefaults;

    public final TLongObjectHashMap<Set<OptFlag_t>> noptDefaults;
    public final TLongObjectHashMap<Byte> nresScalDefaults;
    public final TLongObjectHashMap<Byte> nllmScalDefaults;
    public final TLongObjectHashMap<Byte> nhlmScalDefaults;
    public final TLongObjectHashMap<Float> nloLimDefaults;
    public final TLongObjectHashMap<Float> nhiLimDefaults;
    public final TLongObjectHashMap<String> nunitDefaults;
    // MPR only
    public final TLongObjectHashMap<IntList> nrtnIndexDefaults;
    
    public Byte getDefaultLlmScal(TestID id)
    {
    	Byte b = llmScalDefaults.get(id);
    	if (b == null) b = nllmScalDefaults.get(id.testNumber);
    	return(b);
    }
    
    public Byte getDefaultHlmScal(TestID id)
    {
    	Byte b = hlmScalDefaults.get(id);
    	if (b == null) b = nhlmScalDefaults.get(id.testNumber);
    	return(b);
    }
    
    public Float getDefaultLoLimit(TestID id)
    {
        if (id.testName.equals("PLL_REG_1_V36"))
        {
            Float b = loLimDefaults.get(id);
    	    if (b == null) b = nloLimDefaults.get(id.testNumber);
    	    return(b);
        }
    	Float b = loLimDefaults.get(id);
    	if (b == null) b = nloLimDefaults.get(id.testNumber);
    	return(b);
    }
    
    public Float getDefaultHiLimit(TestID id)
    {
    	Float b = hiLimDefaults.get(id);
    	if (b == null) b = nhiLimDefaults.get(id.testNumber);
    	return(b);
    }
    
    public String getDefaultUnits(TestID id)
    {
    	String b = unitDefaults.get(id);
    	if (b == null) b = nunitDefaults.get(id.testNumber);
    	return(b);
    }
    
    public Byte getDefaultResScal(TestID id)
    {
    	Byte b = resScalDefaults.get(id);
    	if (b == null) b = nresScalDefaults.get(id.testNumber);
    	return(b);
    }
    
    public Set<OptFlag_t> getDefaultOptDefaults(TestID id)
    {
    	Set<OptFlag_t> s = optDefaults.get(id);
    	if (s == null) s = noptDefaults.get(id.testNumber);
    	return(s);
    }
    
    void clearDefaults()
    {
    	optDefaults.clear();
    	resScalDefaults.clear();
    	llmScalDefaults.clear();
    	hlmScalDefaults.clear();
    	loLimDefaults.clear();
    	hiLimDefaults.clear();
    	unitDefaults.clear();
    	rtnIndexDefaults.clear();
    	noptDefaults.clear();
    	nresScalDefaults.clear();
    	nllmScalDefaults.clear();
    	nhlmScalDefaults.clear();
    	nloLimDefaults.clear();
    	nhiLimDefaults.clear();
    	nunitDefaults.clear();
    	nrtnIndexDefaults.clear();
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
    	rtnIndexDefaults = new IdentityHashMap<>(100);
    	pinMaps = new TShortObjectHashMap<>(100);
    	noptDefaults = new TLongObjectHashMap<>(1000);
    	nresScalDefaults = new TLongObjectHashMap<>(100);
    	nllmScalDefaults = new TLongObjectHashMap<>(100);
    	nhlmScalDefaults = new TLongObjectHashMap<>(100);
    	nloLimDefaults = new TLongObjectHashMap<>(100);
    	nhiLimDefaults = new TLongObjectHashMap<>(100);
    	nunitDefaults = new TLongObjectHashMap<>(100);
    	nrtnIndexDefaults = new TLongObjectHashMap<>(100);
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
    	long tn = id.testNumber;
    	if (r instanceof MultipleResultParametricRecord)
    	{
    	    MultipleResultParametricRecord mpr = (MultipleResultParametricRecord) r;
    	    if (nrtnIndexDefaults.get(tn) == null && mpr.rtnIndex != null) nrtnIndexDefaults.put(tn, mpr.rtnIndex);
    	    if (rtnIndexDefaults.get(id) == null && mpr.rtnIndex != null) rtnIndexDefaults.put(id, mpr.rtnIndex);
    	}
    	if (noptDefaults.get(tn) == null && r.getOptFlags() != null) noptDefaults.put(tn, r.getOptFlags());
    	if (nresScalDefaults.get(tn) == null && r.getResScal() != null) nresScalDefaults.put(tn, r.getResScal());
    	if (nllmScalDefaults.get(tn) == null && r.getLlmScal() != null) nllmScalDefaults.put(tn, r.getLlmScal());
    	if (nhlmScalDefaults.get(tn) == null && r.getHlmScal() != null) nhlmScalDefaults.put(tn, r.getHlmScal());
    	if (nloLimDefaults.get(tn) == null && r.getLoLimit() != null)
    	{
    		nloLimDefaults.put(tn, r.getLoLimit());
    	}
    	if (nhiLimDefaults.get(tn) == null && r.getHiLimit() != null) 
    	{
    		nhiLimDefaults.put(tn, r.getHiLimit());
    	}
    	if (nunitDefaults.get(tn) == null && r.getUnits() != null) nunitDefaults.put(tn, r.getUnits());
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
        Float loLimit = getDefaultLoLimit(id);
        Float hiLimit = getDefaultHiLimit(id);
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
    
    // units = uA, scale = 3
    protected String scaleUnits(String units, int scale)
    {
        if (units.equals("")) return("");
        String u = units;
        switch (scale)
        {
        case -12: u = "T" + units; break;
        case -9:  u = "G" + units; break;
        case -6:  u = "M" + units; break;
        case -3:  u = "K" + units; break;
        case  0:  u = units;       break;
        case  3:  u = "m" + units; break;
        case  6:  u = "u" + units; break;
        case  9:  u = "n" + units; break;
        case 12:  u = "p" + units; break;
        case 15:  u = "f" + units;
        default:
        }
        if (u.length() >= 3)
        {
            // Potential bug here. Tester may use uppercase letter when it should
            // be using a lower case letter.  Consequently we may not be able
            // to differentiate between milli and Mega
            String p = u.substring(0, 2);
            boolean fix = false;
            if      (p.equals("mT")) { p = "G"; fix = true; }
            else if (p.equalsIgnoreCase("uT")) { p = "M"; fix = true; }
            else if (p.equalsIgnoreCase("nT")) { p = "K"; fix = true; }
            else if (p.equalsIgnoreCase("pT")) { p = "";  fix = true; }
            else if (p.equalsIgnoreCase("fT")) { p = "m"; fix = true; }
            else if (p.equalsIgnoreCase("KG")) { p = "T"; fix = true; }
            else if (p.equals("mG")) { p = "M"; fix = true; }
            else if (p.equalsIgnoreCase("uG")) { p = "K"; fix = true; }
            else if (p.equalsIgnoreCase("nG")) { p = "";  fix = true; }
            else if (p.equalsIgnoreCase("pG")) { p = "m"; fix = true; }
            else if (p.equalsIgnoreCase("fG")) { p = "u"; fix = true; }
            else if (p.equals("MM")) { p = "T"; fix = true; }
            else if (p.equals("KM") || p.equals("kM")) { p = "G"; fix = true; }
            else if (p.equals("mM")) { p = "K"; fix = true; }
            else if (p.equals("uM") || p.equals("UM")) { p = "";  fix = true; }
            else if (p.equals("nM") || p.equals("NM")) { p = "m"; fix = true; }
            else if (p.equals("pM") || p.equals("PM")) { p = "u"; fix = true; }
            else if (p.equals("fM") || p.equals("FM")) { p = "n"; fix = true; }
            else if (p.equalsIgnoreCase("GK")) { p = "T"; fix = true; }
            else if (p.equals("MK") || p.equals("Mk")) { p = "G"; fix = true; }
            else if (p.equalsIgnoreCase("KK")) { p = "M"; fix = true; }
            else if (p.equals("mK") || p.equals("mk")) { p = "";  fix = true; }
            else if (p.equalsIgnoreCase("uK")) { p = "m"; fix = true; }
            else if (p.equalsIgnoreCase("nK")) { p = "u"; fix = true; }
            else if (p.equalsIgnoreCase("pK")) { p = "n"; fix = true; }
            else if (p.equalsIgnoreCase("fK")) { p = "p"; fix = true; }
            else if (p.equals("Tm")) { p = "G"; fix = true; }
            else if (p.equals("Gm")) { p = "M"; fix = true; }
            else if (p.equals("Mm")) { p = "K"; fix = true; }
            else if (p.equals("Km") || p.equals("km")) { p = "";  fix = true; }
            else if (p.equals("mm")) { p = "u"; fix = true; }
            else if (p.equals("um") || p.equals("Um")) { p = "n"; fix = true; }
            else if (p.equals("nm") || p.equals("Nm")) { p = "p"; fix = true; }
            else if (p.equals("pm") || p.equals("Pm")) { p = "f"; fix = true; }
            else if (p.equalsIgnoreCase("Tu")) { p = "M"; fix = true; }
            else if (p.equalsIgnoreCase("Gu")) { p = "K"; fix = true; }
            else if (p.equals("Mu") || p.equals("MU")) { p = "";  fix = true; }
            else if (p.equalsIgnoreCase("Ku")) { p = "m"; fix = true; }
            else if (p.equals("mu") || p.equals("mU")) { p = "n"; fix = true; }
            else if (p.equalsIgnoreCase("uu")) { p = "p"; fix = true; }
            else if (p.equalsIgnoreCase("nu")) { p = "f"; fix = true; }
            else if (p.equalsIgnoreCase("Tn")) { p = "K"; fix = true; }
            else if (p.equalsIgnoreCase("Gn")) { p = "";  fix = true; }
            else if (p.equals("Mn") || p.equals("MN")) { p = "m"; fix = true; }
            else if (p.equalsIgnoreCase("Kn")) { p = "u"; fix = true; }
            else if (p.equals("mn") || p.equals("mN")) { p = "p"; fix = true; }
            else if (p.equalsIgnoreCase("un")) { p = "f"; fix = true; }
            else if (p.equalsIgnoreCase("Tp")) { p = "";  fix = true; }
            else if (p.equalsIgnoreCase("Gp")) { p = "m"; fix = true; }
            else if (p.equals("Mp") || p.equals("MP")) { p = "u"; fix = true; }
            else if (p.equalsIgnoreCase("Kp")) { p = "n"; fix = true; }
            else if (p.equals("mp") || p.equals("mP")) { p = "f"; fix = true; }
            else if (p.equalsIgnoreCase("Tf")) { p = "m"; fix = true; }
            else if (p.equalsIgnoreCase("Gf")) { p = "u"; fix = true; }
            else if (p.equals("Mf") || p.equals("MF")) { p = "n"; fix = true; }
            else if (p.equalsIgnoreCase("Kf")) { p = "p"; fix = true; }
            if (fix)
            {
                String newUnits = units.substring(1);
                u = p + newUnits;
            }
        }
        return(u);
    }
    
    public Float getScaledLoLimit(ParametricRecord r)
    {
        String units = r.getUnits();
    	if (units == null) units = getDefaultUnits(r.getTestID());
        if (units == null) 
        {
            if (r.getLoLimit() == null) return(getDefaultLoLimit(r.getTestID()));
            return(r.getLoLimit());
        }
        if (units.equals("")) 
        {
            if (r.getLoLimit() == null) return(getDefaultLoLimit(r.getTestID()));
            return(r.getLoLimit());
        }
    	Float l = null;
    	if (r.getLoLimit() != null) l = r.getLoLimit();
    	else l = getDefaultLoLimit(r.getTestID());
    	if (l == null) return(null);
    	return(scaleValue(l, findScale(r.getTestID())));
    }

    public Float getScaledHiLimit(ParametricRecord r)
    {
        String units = r.getUnits();
    	if (units == null) units = getDefaultUnits(r.getTestID());
        if (units == null) 
        {
            if (r.getHiLimit() == null) return(getDefaultHiLimit(r.getTestID()));
            return(r.getHiLimit());
        }
        if (units.equals("")) 
        {
            if (r.getHiLimit() == null) return(getDefaultHiLimit(r.getTestID()));
            return(r.getHiLimit());
        }
    	Float l = null;
    	if (r.getHiLimit() != null) l = r.getHiLimit();
    	else l = getDefaultHiLimit(r.getTestID());
    	if (l == null) return(null);
    	return(scaleValue(l, findScale(r.getTestID())));
    }

    public String getScaledUnits(ParametricRecord r)
    {
    	String units = r.getUnits();
    	if (units == null) units = getDefaultUnits(r.getTestID());
    	if (units == null) return("");
    	if (units.equals("")) return("");
    	return(scaleUnits(units, findScale(r.getTestID())));
    }

    public Float getScaledResult(ParametricTestRecord r)
    {
    	if (r.result == null) return(null);
    	String units = r.getUnits();
    	if (units == null) units = getDefaultUnits(r.getTestID());
    	if (units == null) return(r.result);
    	if (units.equals("")) return(r.result);
    	return(scaleValue(r.result, findScale(r.getTestID())));
    }
    
    public FloatList getScaledResults(MultipleResultParametricRecord r)
    {
    	if (r.results == null) return(null);
    	String units = r.getUnits();
    	if (units == null) units = getDefaultUnits(r.getTestID());
    	if (units == null) return(r.results);
    	if (units.equals(""))
    	{
    	    return(r.results);
    	}
    	float[] f = new float[r.results.size()];
        IntStream.range(0, f.length).forEach(i -> f[i] = scaleValue(r.results.get(i), findScale(r.getTestID())));	
    	return(new FloatList(f));
    }
    
    public IntList getDefaultRtnIndex(TestID id)
    {
    	IntList i = rtnIndexDefaults.get(id);
    	if (i == null) i = nrtnIndexDefaults.get(id.testNumber);
    	return(i);
    }

}
