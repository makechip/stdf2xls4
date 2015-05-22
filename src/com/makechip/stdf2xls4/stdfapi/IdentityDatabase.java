package com.makechip.stdf2xls4.stdfapi;

import java.util.IdentityHashMap;

import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

final class IdentityDatabase
{
	public final IdentityHashMap<TestID, Integer> testIdDupMap;
	public final IdentityFactoryLON<String, TestID> idMap;
    public final IdentityFactoryIO<TestID, String, PinTestID> testIdPinMap;

	public IdentityDatabase()
	{
		testIdDupMap = new IdentityHashMap<>();
		idMap = new IdentityFactoryLON<>(String.class, TestID.class);
	    testIdPinMap = new IdentityFactoryIO<>(TestID.class, String.class, PinTestID.class);	
	}

}
