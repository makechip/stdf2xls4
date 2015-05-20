package com.makechip.stdf2xls4.stdfapi;

import java.util.HashMap;
import java.util.IdentityHashMap;

import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;

final class IdentityDatabase
{
	public final IdentityHashMap<DuplicateTestID, Integer> testIdDupMap;
	public final IdentityFactoryLON<String, TestID> duplicateTestIdMap;
	public final HashMap<String, TestID> testIdNameMap;
	public final HashMap<Integer, TestID> testIdNumberMap;
    public final IdentityFactoryIO<TestID, String, TestID> testIdPinMap;

	public IdentityDatabase()
	{
		testIdDupMap = new IdentityHashMap<>();
		testIdNameMap = new HashMap<>();
		testIdNumberMap = new HashMap<>();
		duplicateTestIdMap = new IdentityFactoryLON<>(String.class, TestID.class);
	    testIdPinMap = new IdentityFactoryIO<>(TestID.class, String.class, TestID.class);	
	}

}
