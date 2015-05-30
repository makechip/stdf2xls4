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
package com.makechip.stdf2xls4.stdf;

import com.makechip.util.Identity;
import com.makechip.util.Immutable;
import com.makechip.util.Log;

public class TestID implements Identity, Immutable 
{
    public final long testNumber;
    public final String testName;
    public final int dupNum;
   
    protected TestID(long testNumber, String testName)
    {
    	this(testNumber, testName, 0);
    }
    
    protected TestID(long testNumber, String testName, int dupNum)
    {
        this.testNumber = testNumber;
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
    public static TestID createTestID(IdentityDatabase idb, long testNum, String testName)
    {
    	// 1. check testName and testNumber
    	TestID id = idb.idMap.getExistingValue(testNum, testName, 0);
    	Log.msg("id1 = " + id);
    	Log.msg("map size1 = " + idb.testIdDupMap.size());
    	if (id == null)
    	{
    		TestID d = idb.idMap.getValue(testNum, testName, 0);
    		Log.msg("id2 = " + d);
    		idb.testIdDupMap.put(d, 0);
    		Log.msg("dupNum2 = " + idb.testIdDupMap.get(d));
    		Log.msg("idb = " + idb);
    		return(d);
    	}
        // 2. Need duplicate ID
    	Log.msg("id3 = " + id);
    	Log.msg("map size3 = " + idb.testIdDupMap.size());
    	Integer dnum = idb.testIdDupMap.get(id);
   		Log.msg("dupNum3 = " + idb.testIdDupMap.get(id));
   		Log.msg("idb = " + idb);
    	dnum++;
    	TestID d = idb.idMap.getValue(testNum, testName, dnum);
    	idb.testIdDupMap.put(id,  dnum);
    	return(d);
    }
    
    /*
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("" + testNumber).append("_").append(testName);
        if (dupNum != 0) sb.append("_" + dupNum);
        return(sb.toString());
    }
    */
    @Override
    public int getInstanceCount()
    {
        return(-1);
    }
    
}
