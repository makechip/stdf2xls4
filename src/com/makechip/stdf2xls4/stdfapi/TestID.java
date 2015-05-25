/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4.stdfapi;

import java.util.HashMap;
import java.util.IdentityHashMap;

import com.makechip.util.Identity;
import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryLON;

class TestID implements Identity, Immutable 
{
    public final long testNum;
    public final String testName;
    public final int dupNum;
   
    protected TestID(long testNum, String testName)
    {
    	this(testNum, testName, 0);
    }
    
    protected TestID(long testNum, String testName, int dupNum)
    {
        this.testNum = testNum;
        this.testName = testName;
        this.dupNum = dupNum;    	
    }
    
    /**
     * The test ID is computed with the following algorithm:
     * 1. If the test name and the test number have been encountered,
     *    then the test ID is based on the test name, the test number,
     *    and a duplicate count number.
     * @param idb
     * @param testNum
     * @param testName
     * @return
     */
    public static TestID createTestID(HashMap<String, String> hdr, IdentityDatabase idb, long testNum, String testName)
    {
    	// 1. check testName and testNumber
    	IdentityFactoryLON<String, TestID> idf = idb.idMap.get(hdr);
    	if (idf == null)
    	{
    		idf = new IdentityFactoryLON<>(String.class, TestID.class);
    		idb.idMap.put(hdr, idf);
    	}
    	TestID id = idf.getExistingValue(testNum, testName, 0);
    	if (id == null)
    	{
    		TestID d = idf.getValue(testNum, testName, 0);
    		IdentityHashMap<TestID, Integer> idupMap = idb.testIdDupMap.get(hdr);
    		if (idupMap == null)
    		{
    			idupMap = new IdentityHashMap<>();
    			idb.testIdDupMap.put(hdr, idupMap);
    		}
    		idupMap.put(d, 0);
    		return(d);
    	}
        // 4. Need duplicate ID
   		IdentityHashMap<TestID, Integer> idupMap = idb.testIdDupMap.get(hdr);
   		if (idupMap == null)
   		{
   			idupMap = new IdentityHashMap<>();
   			idb.testIdDupMap.put(hdr, idupMap);
   		}
    	Integer dnum = idupMap.get(id);
    	if (dnum == null)
    	{
    		idupMap.put(id, 0);
    		return(id);
    	}
    	dnum++;
    	TestID d = idf.getValue(testNum, testName, dnum);
    	idupMap.put(id,  dnum);
    	return(d);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("" + testNum).append("_").append(testName);
        if (dupNum != 0) sb.append("_" + dupNum);
        return(sb.toString());
    }
    
    @Override
    public int getInstanceCount()
    {
        return(-1);
    }
    
}
