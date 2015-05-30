package com.makechip.stdf2xls4.stdf;

import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TLongShortHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;

import java.util.IdentityHashMap;
import java.util.Set;

import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.Log;
import com.makechip.util.factory.IdentityFactoryLON;

public final class IdentityDatabase
{
	public final IdentityHashMap<TestID, Integer> testIdDupMap;
	public final IdentityFactoryLON<String, TestID> idMap;

    // for FTR
    public final TLongShortHashMap pgDefaults;
    public final TLongObjectHashMap<byte[]> ecDefaults;
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
    public final IdentityHashMap<TestID, String> resFmtDefaults;
    public final IdentityHashMap<TestID, String> llmFmtDefaults;
    public final IdentityHashMap<TestID, String> hlmFmtDefaults;
    public final TObjectFloatHashMap<TestID> loSpecDefaults;
    public final TObjectFloatHashMap<TestID> hiSpecDefaults;
    // for MPR
    public final TObjectFloatHashMap<TestID> startInDefaults;
    public final TObjectFloatHashMap<TestID> incrInDefaults;
    public final IdentityHashMap<TestID, int[]> rtnIndexDefaults;
    public final IdentityHashMap<TestID, String> unitsInDefaults;
    
    public void clearIdDups() { Log.msg("CLEARING DUPS"); testIdDupMap.clear(); }
    
    public void clearDefaults()
    {
    	pgDefaults.clear();
    	ecDefaults.clear();
    	foptDefaults.clear();
    	tnameDefaults.clear();
    	optDefaults.clear();
    	resScalDefaults.clear();
    	llmScalDefaults.clear();
    	hlmScalDefaults.clear();
    	loLimDefaults.clear();
    	hiLimDefaults.clear();
    	unitDefaults.clear();
    	resFmtDefaults.clear();
    	llmFmtDefaults.clear();
    	hlmFmtDefaults.clear();
    	loSpecDefaults.clear();
    	hiSpecDefaults.clear();
    	startInDefaults.clear();
    	incrInDefaults.clear();
    	rtnIndexDefaults.clear();
    	unitsInDefaults.clear();
    }
    
    public IdentityDatabase()
    {
    	Log.msg("IDB CTOR");
    	testIdDupMap = new IdentityHashMap<>();
    	idMap = new IdentityFactoryLON<>(String.class, TestID.class);
    	pgDefaults = new TLongShortHashMap(100, 0.7f, Long.MIN_VALUE, StdfRecord.MISSING_SHORT);
    	ecDefaults = new TLongObjectHashMap<>(100, 0.7f);
    	foptDefaults = new TLongObjectHashMap<>(100, 0.7f);
    	tnameDefaults = new TLongObjectHashMap<>(100, 0.7f);
    	optDefaults = new IdentityHashMap<TestID, Set<OptFlag_t>>(1000);
    	resScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	llmScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	hlmScalDefaults = new TObjectByteHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_BYTE);
    	loLimDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	hiLimDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	unitDefaults = new IdentityHashMap<TestID, String>(100);
    	resFmtDefaults = new IdentityHashMap<TestID, String>(100);
    	llmFmtDefaults = new IdentityHashMap<TestID, String>(100);
    	hlmFmtDefaults = new IdentityHashMap<TestID, String>(100);
    	loSpecDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	hiSpecDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	// for MPR
    	startInDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	incrInDefaults = new TObjectFloatHashMap<TestID>(100, 0.7f, StdfRecord.MISSING_FLOAT);
    	rtnIndexDefaults = new IdentityHashMap<TestID, int[]>(100);
    	unitsInDefaults = new IdentityHashMap<TestID, String>(100);
    }
   


}
