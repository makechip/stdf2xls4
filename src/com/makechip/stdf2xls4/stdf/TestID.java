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
import com.makechip.util.Immutable;
import com.makechip.util.Log;

/**
 * This class provides TestIDs which are a combination of test names
 * and test numbers.  They are managed such that only one instance
 * can be created for each unique test name and test number combination.
 * This implies that they may be compared for equality using the '==' operator,
 * and they may be used as keys in IdentityHashMaps. 
 * in the case where multiple tests have the same name and number, then
 * the ID is given a duplicate number also. The duplicate number is incremented
 * whenever a duplicate is detected, and the duplicate number is reset
 * to 0 at the beginning of each device (Technically it is reset to zero
 * whenever a PartResultsRecord is encountered.)
 * @author eric
 */
public class TestID implements Identity, Immutable 
{
	/**
	 * The test number.
	 */
    public final long testNumber;
    /**
     * The test name.
     */
    public final String testName;
    /**
     * The duplicate number.
     */
    public final int dupNum;
    
    public 
    
    short siteNum;
    
    public short headNumber;
   
    protected TestID(long testNumber, String testName, int dupNum)
    {
        this.testNumber = testNumber;
        this.testName = testName;
        this.dupNum = dupNum;    	
    }
    
    /**
     * Factory method to create a TestID.
     * @param tdb The TestIdDatabase use to track duplicate IDs.
     * @param testNum The test number.
     * @param testName The test name. This value may not be null.
     * @return A TestID.
     */
    public static TestID createTestID(TestIdDatabase tdb, long testNum, short siteNum, short headNum, String testName)
    {
    	// 1. check testName and testNumber
    	TestID existId = tdb.idMap.getExistingValue(testNum, testName, 0);
    	HashMap<String, TestID> pid = tdb.tempIdMap.get(testNum);
    	if (pid == null)
    	{
    	    pid = new HashMap<>();
    	    tdb.tempIdMap.put(testNum, pid);
    	}
    	TestID id = pid.get(testName);
    	if (id == null)
    	{
    		if (existId == null) existId = tdb.idMap.getValue(testNum, testName, 0);
    		existId.siteNum = siteNum;
    		existId.headNumber = headNum;
    		tdb.testIdDupMap.put(existId, 0);
    		Log.msg("testNum = " + testNum + " siteNum = " + siteNum + " headNum = " + headNum + " testName = " + testName + " dupNum = 0");
    		return(existId);
    	}
        // 2. Need duplicate ID
    	if (id.siteNum == siteNum && id.headNumber == headNum)
    	{
    	    TestID oldid = id;
    	    Integer dnum = tdb.testIdDupMap.get(id);
    	    dnum++;
    	    id = tdb.idMap.getValue(testNum, testName, dnum);
    	    id.siteNum = siteNum;
    	    id.headNumber = headNum;
    		Log.msg("testNum = " + testNum + " siteNum = " + siteNum + " headNum = " + headNum + " testName = " + testName + " dupNum = " + dnum);
    	    tdb.testIdDupMap.put(oldid, dnum);
    	}
    	return(id);
    }
    
    /**
     * This method is required by the Identity interface, but is not used.
     * @return Just returns -1.
     */
    @Override
    public int getInstanceCount()
    {
        return(-1);
    }
    
    /**
     * This is a special case of the TestID class that is used for MultipleResultParametricRecords.
     * It combines the TestID with a pin name.  That way each pin measurement in
     * a MultipleResultParametricRecord may be assigned a unique TestID while still
     * having the same test name and test number.
     * @author eric
     *
     */
    public static class PinTestID extends TestID implements Identity, Immutable
    {
        @Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("PinTestID [");
			builder.append("pin=");
			builder.append(pin);
			builder.append(", ");
			builder.append("id=");
			builder.append(id);
			builder.append("]");
			return builder.toString();
		}


        /**
         * The pin name.
         */
		public final String pin;
		/**
		 * The original TestID from the MultipleResultParametricRecord.
		 */
        public final TestID id;
       
        private PinTestID(TestID id, String pin)
        {
        	super(id.testNumber, id.testName, id.dupNum);
        	this.pin = pin;
        	this.id = id;
        	this.siteNum = id.siteNum;
        	this.headNumber = id.headNumber;
        }
        
        /**
         * Factory method to create a PinTestID.
         * @param tdb The TestIdDatabase - used to track duplicate test IDs.
         * @param id The TestID from the MultipleResultParametricRecord. This value may not be null.
         * @param pin The pin name. This value may not be null.
         * @return A PinTestID.
         */
        public static TestID.PinTestID getTestID(TestIdDatabase tdb, TestID id, String pin)
        {
            return(tdb.pinMap.getValue(id, pin));
        }
        
        
        /**
         * This method is required by the Identity interface, but is not used.
         * @return Just returns -1.
         */
        @Override
        public int getInstanceCount()
        {
            return(-1);
        }
   	
    }

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TestID [testNumber=");
		builder.append(testNumber);
		builder.append(", ");
		builder.append("testName=");
		builder.append(testName);
		builder.append(", ");
		builder.append("siteNum=");
		builder.append(siteNum);
		builder.append(", ");
		builder.append("headNum=");
		builder.append(headNumber);
		builder.append(", ");
		builder.append("dupNum=");
		builder.append(dupNum);
		builder.append("]");
		return builder.toString();
	}
    
}
