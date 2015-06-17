package com.makechip.stdf2xls4.stdf;

import java.util.IdentityHashMap;

import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

public final class TestIdDatabase
{
	public final IdentityHashMap<TestID, Integer> testIdDupMap;
	public final IdentityFactoryLON<String, TestID> idMap;
	public final IdentityFactoryIO<TestID, String, PinTestID> pinMap;

    public void clearIdDups() { testIdDupMap.clear(); }
    
    public TestIdDatabase()
    {
    	testIdDupMap = new IdentityHashMap<>();
    	idMap = new IdentityFactoryLON<>(String.class, TestID.class);
    	pinMap = new IdentityFactoryIO<>(TestID.class, String.class, PinTestID.class);
    }
   

}
