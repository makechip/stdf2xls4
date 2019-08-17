package com.makechip.stdf2xls4.stdf;

import java.util.HashMap;
import java.util.IdentityHashMap;

import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

import gnu.trove.map.hash.TLongObjectHashMap;

public final class TestIdDatabase
{
	/**
	 * The hash map for tracking duplicate test IDs.
	 */
	public final IdentityHashMap<TestID, Integer> testIdDupMap;
	
	public TLongObjectHashMap<HashMap<String, TestID>> tempIdMap;
	/**
	 * The factory for creating unique TestIDs.
	 */
	public final IdentityFactoryLON<String, TestID> idMap;
	/**
	 * The factory for creating unique PinTestIDs.
	 */
	public final IdentityFactoryIO<TestID, String, TestID.PinTestID> pinMap;

	/**
	 * Method for reseting the duplicate TestID tracker.
	 * This method should be called whenever a PartResultsRecord is encountered.
	 */
    public void clearIdDups() 
    {
        tempIdMap.clear();
        testIdDupMap.clear(); 
    }
    
    boolean smartTest8;
    
    public boolean isSmartTest8() { return smartTest8; }
    
    /**
     * CTOR.
     */
    public TestIdDatabase()
    {
    	testIdDupMap = new IdentityHashMap<>();
    	tempIdMap = new TLongObjectHashMap<>();
    	idMap = new IdentityFactoryLON<>(String.class, TestID.class);
    	pinMap = new IdentityFactoryIO<>(TestID.class, String.class, TestID.PinTestID.class);
    }
   
}
