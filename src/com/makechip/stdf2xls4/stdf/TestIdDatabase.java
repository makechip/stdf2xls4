package com.makechip.stdf2xls4.stdf;

import java.util.IdentityHashMap;

import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

public final class TestIdDatabase
{
	/**
	 * The hash map for tracking duplicate test IDs.
	 */
	public final IdentityHashMap<TestID, Integer> testIdDupMap;
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
    public void clearIdDups() { testIdDupMap.clear(); }
    
    /**
     * CTOR.
     */
    public TestIdDatabase()
    {
    	testIdDupMap = new IdentityHashMap<>();
    	idMap = new IdentityFactoryLON<>(String.class, TestID.class);
    	pinMap = new IdentityFactoryIO<>(TestID.class, String.class, TestID.PinTestID.class);
    }
   

}
