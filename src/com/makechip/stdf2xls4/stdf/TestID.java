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

import java.util.HashMap;

import com.makechip.util.Identity;
import com.makechip.util.factory.IdentityFactoryIO;
import com.makechip.util.factory.IdentityFactoryNON;
import com.makechip.util.Immutable;

public final class TestID implements Identity, Immutable 
{
	private static IdentityFactoryNON<String, TestID> map2 = 
		new IdentityFactoryNON<String, TestID>(String.class, TestID.class);
    private static IdentityFactoryIO<TestID, String, TestID> pmap =
        new IdentityFactoryIO<TestID, String, TestID>(TestID.class, String.class, TestID.class);
	private static HashMap<String, TestID> map1 = new HashMap<String, TestID>();
    
    private final int testNum;
    private final String testName;
    private String pin;

    private int dupNum;
   
    private TestID(int testNum, String testName)
    {
        this.testNum = testNum;
        this.testName = testName;
        dupNum = 0;
    }
    
    private TestID(int testNum, String testName, int dupNum)
    {
        this.testNum = testNum;
        this.testName = testName;
        this.dupNum = dupNum;
    }
    
    private TestID(TestID id, String pin)
    {
        this(id.getTestNumber(), id.getTestName());
        this.pin = pin;
        dupNum = 0;
    }
    
    public static TestID getTestID(int testNum, String testName, int dupNum)
    {
    	return(map2.getValue(testNum, testName, dupNum));
    }
    
    public static TestID getTestID(int testNum, String testName)
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
    
    public static TestID getTestID(TestID id, String pin)
    {
        return(pmap.getValue(id, pin));
    }
    
    public static TestID findTestID(int testNum, String testName)
    {
    	return(map2.getExistingValue(testNum, testName, 0));
    }
    
    public static TestID findTestID(int testNum, String testName, int dupNum)
    {
    	return(map2.getExistingValue(testNum, testName, dupNum));
    }
    
    public static TestID findTestID(TestID id, String pin)
    {
    	return(pmap.getExistingValue(id, pin));
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
    
    public int getTestNumber() { return(testNum); }
    
    public String getTestName() 
    { 
        return(pin == null ? testName : testName + " [" + pin + "]"); 
    }
    
    public int getDupNum() { return(dupNum); }
    
    public String getPin() { return(pin); }

    @Override
    public int getInstanceCount()
    {
        return(map2.getInstanceCount() + pmap.getInstanceCount() + map1.size());
    }
    
}
