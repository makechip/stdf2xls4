package com.makechip.stdf2xls4.stdf;

import java.util.IdentityHashMap;

import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLSSON;

import gnu.trove.map.hash.TShortObjectHashMap;

public final class TestIdDatabase
{
	/**
	 * The hash map for tracking duplicate test IDs.
	 */
	public final IdentityHashMap<TestID, TShortObjectHashMap<TShortObjectHashMap<Integer>>> testIdDupMap;
	/**
	 * The factory for creating unique TestIDs.
	 */
	public final IdentityFactoryLSSON<String, TestID> idMap;
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
    	idMap = new IdentityFactoryLSSON<>(String.class, TestID.class);
    	pinMap = new IdentityFactoryIO<>(TestID.class, String.class, TestID.PinTestID.class);
    }
   
    public void addDupNum(TestID id, short siteNum, short headNum, int dupNum)
    {
        TShortObjectHashMap<TShortObjectHashMap<Integer>> m1 = testIdDupMap.get(id);
        if (m1 == null)
        {
            m1 = new TShortObjectHashMap<>();
            testIdDupMap.put(id, m1);
        }
        TShortObjectHashMap<Integer> m2 = m1.get(siteNum);
        if (m2 == null)
        {
            m2 = new TShortObjectHashMap<>();
            m1.put(siteNum, m2);
        }
        m2.put(headNum, dupNum);
    }
    
    public Integer getDupNum(TestID id, short siteNum, short headNum)
    {
        TShortObjectHashMap<TShortObjectHashMap<Integer>> m1 = testIdDupMap.get(id);
        if (m1 != null)
        {
            TShortObjectHashMap<Integer> m2 = m1.get(siteNum);
            return(m2.get(headNum));
        }
        return(null);
    }

}
