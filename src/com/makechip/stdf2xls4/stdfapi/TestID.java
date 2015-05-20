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
import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryLON;
import com.makechip.util.Immutable;

public final class TestID implements Identity, Immutable 
{
    private final long testNum;
    private final String testName;
    private String pin;
    private int dupNum;
   
    protected TestID(long testNum, String testName)
    {
        this.testNum = testNum;
        this.testName = testName;
        dupNum = 0;
    }
    
    /**
     * The test ID is computed with the following algorithm:
     * 1. If the test name has not been encountered, Then the test ID
     *    is based on the name alone.
     * 2. If the test name has been encountered, then if the test
     *    number has not been encountered the test ID is based on the 
     *    test number alone.
     * 3. If both the test name and the test number have been encountered,
     *    then the test ID is based on both the test name and the test number
     * 4. If the test name and the test number have been encountered,
     *    then the test ID is based on the test name, the test number,
     *    and a duplicate count number.
     * @param idb
     * @param testNum
     * @param testName
     * @return
     */
    public static TestID getTestID(IdentityDatabase idb, long testNum, String testName)
    {
    	TestID id = map1.get(testName);
    	if (id != null)
    	{
    		if (id.getTestNumber() == testNum) return(id);
    		return(map2.getValue(testNum, testName, 0));
    	}
    	id = new TestID(testNum, testName);
    	map1.put(testName, id);
        return(id);
    }
    
    public static TestID findTestID(long testNum, String testName)
    {
    	return(map2.getExistingValue(testNum, testName, 0));
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("" + getTestNumber());
        sb.append(" "); sb.append(testName);
        if (dupNum != 0) sb.append(" " + dupNum);
        if (pin != null)
        {
            sb.append(" [");
            sb.append(pin);
            sb.append("]");
        }
        return(sb.toString());
    }
    
    public long getTestNumber() { return(testNum); }
    
    public String getTestName() 
    { 
        return(pin == null ? testName : testName + " [" + pin + "]"); 
    }
    
    @Override
    public int getInstanceCount()
    {
        return(-1);
    }
    
}
