package com.makechip.stdf2xls4.stdf;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongByteHashMap;
import gnu.trove.map.hash.TLongFloatHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.EnumSet;
import java.util.IdentityHashMap;

public class DefaultMPRValueMap
{
    TLongObjectHashMap<EnumSet<OptFlag_t>> poptDefaults;
    TObjectByteHashMap<TestID> resScalDefaults;
    TLongByteHashMap nresScalDefaults;
    TObjectByteHashMap<TestID> llmScalDefaults;
    TLongByteHashMap nllmScalDefaults;
    TObjectByteHashMap<TestID> hlmScalDefaults;
    TLongByteHashMap nhlmScalDefaults;
    TObjectFloatHashMap<TestID> loLimDefaults;
    TLongFloatHashMap nloLimDefaults;
    TObjectFloatHashMap<TestID> hiLimDefaults;
    TLongFloatHashMap nhiLimDefaults;
    IdentityHashMap<TestID, String> unitDefaults;
    TLongObjectHashMap<String> nunitDefaults;
    IdentityHashMap<TestID, String> resFmtDefaults;
    TLongObjectHashMap<String> nresFmtDefaults;
    IdentityHashMap<TestID, String> llmFmtDefaults;
    TLongObjectHashMap<String> nllmFmtDefaults;
    IdentityHashMap<TestID, String> hlmFmtDefaults;
    TLongObjectHashMap<String> nhlmFmtDefaults;
    TObjectFloatHashMap<TestID> loSpecDefaults;
    TLongFloatHashMap nloSpecDefaults;
    TObjectFloatHashMap<TestID> hiSpecDefaults;
    TLongFloatHashMap nhiSpecDefaults;
    TObjectFloatHashMap<TestID> scaledLoLimits;
    TObjectFloatHashMap<TestID> scaledHiLimits;
    IdentityHashMap<TestID, String> scaledUnits;
    TObjectIntHashMap<TestID> scales;
    TObjectFloatHashMap<TestID> startInDefaults;
    TLongFloatHashMap nstartInDefaults;
    TObjectFloatHashMap<TestID> incrInDefaults;
    TLongFloatHashMap nincrInDefaults;
    IdentityHashMap<TestID, int[]> rtnIndexDefaults;
    TLongObjectHashMap<int[]> nrtnIndexDefaults;
    IdentityHashMap<TestID, String> unitsInDefaults;
    TLongObjectHashMap<String> nunitsInDefaults;
    TShortObjectHashMap<TIntObjectHashMap<String>> pinmap;
    IdentityHashMap<TestID, PinList> mprPins;
    TLongObjectHashMap<PinList> nmprPins;

	public DefaultMPRValueMap()
	{
        poptDefaults      = new TLongObjectHashMap<EnumSet<OptFlag_t>>();
        resScalDefaults   = new TObjectByteHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_BYTE);
        nresScalDefaults  = new TLongByteHashMap(100, 0.7F, -1L, StdfRecord.MISSING_BYTE);
        llmScalDefaults   = new TObjectByteHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_BYTE);
        nllmScalDefaults  = new TLongByteHashMap(100, 0.7F, -1L, StdfRecord.MISSING_BYTE);
        hlmScalDefaults   = new TObjectByteHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_BYTE);
        nhlmScalDefaults  = new TLongByteHashMap(100, 0.7F, -1L, StdfRecord.MISSING_BYTE);
        loLimDefaults     = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nloLimDefaults    = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        hiLimDefaults     = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nhiLimDefaults    = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        unitDefaults      = new IdentityHashMap<TestID, String>();
        nunitDefaults     = new TLongObjectHashMap<String>();
        resFmtDefaults    = new IdentityHashMap<TestID, String>();
        nresFmtDefaults   = new TLongObjectHashMap<String>();
        llmFmtDefaults    = new IdentityHashMap<TestID, String>();
        nllmFmtDefaults   = new TLongObjectHashMap<String>();
        hlmFmtDefaults    = new IdentityHashMap<TestID, String>();
        nhlmFmtDefaults   = new TLongObjectHashMap<String>();
        loSpecDefaults    = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nloSpecDefaults   = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        hiSpecDefaults    = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nhiSpecDefaults   = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        scaledLoLimits    = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        scaledHiLimits    = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        scaledUnits       = new IdentityHashMap<TestID, String>();
        scales            = new TObjectIntHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_INT);
        startInDefaults   = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nstartInDefaults  = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        incrInDefaults    = new TObjectFloatHashMap<TestID>(100, 0.7F, StdfRecord.MISSING_FLOAT);
        nincrInDefaults   = new TLongFloatHashMap(100, 0.7F, -1L, StdfRecord.MISSING_FLOAT);
        rtnIndexDefaults  = new IdentityHashMap<TestID, int[]>();
        nrtnIndexDefaults = new TLongObjectHashMap<int[]>();
        unitsInDefaults   = new IdentityHashMap<TestID, String>();
        nunitsInDefaults  = new TLongObjectHashMap<String>();
        pinmap            = new TShortObjectHashMap<TIntObjectHashMap<String>>();
        mprPins           = new IdentityHashMap<TestID, PinList>();
        nmprPins          = new TLongObjectHashMap<PinList>();
    }

}
