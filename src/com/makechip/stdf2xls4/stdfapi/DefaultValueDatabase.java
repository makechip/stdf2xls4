package com.makechip.stdf2xls4.stdfapi;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TLongShortHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.IdentityHashMap;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.StdfRecord;

public final class DefaultValueDatabase
{
	private boolean fusionCx;
	public final long timeStamp;
	private Cpu_t cpuType;

    // for FTR
    public final TLongObjectHashMap<Set<FTROptFlag_t>> foptDefaults;
    public final TLongObjectHashMap<String> tnameDefaults;
    // for PTR and MPR
    public final IdentityHashMap<TestID, Set<OptFlag_t>> optDefaults;
    public final TObjectByteHashMap<TestID> resScalDefaults;
    public final TObjectByteHashMap<TestID> llmScalDefaults;
    public final TObjectByteHashMap<TestID> hlmScalDefaults;
    public final TObjectFloatHashMap<TestID> loLimDefaults;
    public final TObjectFloatHashMap<TestID> hiLimDefaults;
    public final IdentityHashMap<TestID, String> unitDefaults;
    // scale maps for limits and results of ParametricRecords
    public final TObjectFloatHashMap<TestID> scaledLoLimits;
    public final TObjectFloatHashMap<TestID> scaledHiLimits;
    public final IdentityHashMap<TestID, String> scaledUnits;
    public final TObjectIntHashMap<TestID> scales;
    // pin index maps:
    final TShortObjectHashMap<TIntObjectHashMap<String>> chanMap;
    final TShortObjectHashMap<TIntObjectHashMap<String>> physMap;
    final TShortObjectHashMap<TIntObjectHashMap<String>> logMap;
    
    void clearDefaults()
    {
    	foptDefaults.clear();
    	tnameDefaults.clear();
    	optDefaults.clear();
    	resScalDefaults.clear();
    	llmScalDefaults.clear();
    	hlmScalDefaults.clear();
    	loLimDefaults.clear();
    	hiLimDefaults.clear();
    	unitDefaults.clear();
    	scaledLoLimits.clear();
    	scaledHiLimits.clear();
    	scaledUnits.clear();
    	scales.clear();
    }
    
    public DefaultValueDatabase(long timeStamp)
    {
    	this.timeStamp = timeStamp;
    	foptDefaults = new TLongObjectHashMap<>(100, 0.7f);
    	tnameDefaults = new TLongObjectHashMap<>(100, 0.7f);
    	optDefaults = new IdentityHashMap<TestID, Set<OptFlag_t>>(1000);
    	resScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	llmScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	hlmScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	loLimDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	hiLimDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	unitDefaults = new IdentityHashMap<TestID, String>(100);
    	chanMap = new TShortObjectHashMap<>();
    	physMap = new TShortObjectHashMap<>();
    	logMap = new TShortObjectHashMap<>();
    	scaledLoLimits = new TObjectFloatHashMap<>(100, 0.7F, StdfRecord.MISSING_FLOAT);
    	scaledHiLimits = new TObjectFloatHashMap<>(100, 0.7F, StdfRecord.MISSING_FLOAT);
    	scaledUnits = new IdentityHashMap<>();
    	scales = new TObjectIntHashMap<>(100, 0.7F, StdfRecord.MISSING_INT);
    }
   
    String getChannelName(short site, int index)
    {
    	TIntObjectHashMap<String> m = chanMap.get(site);
        if (m == null) return(null);
        return(m.get(index));
    }
    
    String getPhysicalPinName(short site, int index)
    {
    	TIntObjectHashMap<String> m = physMap.get(site);
    	if (m == null) return(null);
    	return(m.get(index));
    }
    
    String getLogicalPinName(short site, int index)
    {
    	TIntObjectHashMap<String> m = logMap.get(site);
    	if (m == null) return(null);
    	return(m.get(index));
    }
    
    void setFusionCx() { fusionCx = true; }
    
    boolean isFusionCx() { return(fusionCx); }

	public Cpu_t getCpuType()
	{
		return cpuType;
	}

	void setCpuType(Cpu_t cpuType)
	{
		this.cpuType = cpuType;
	}

}
