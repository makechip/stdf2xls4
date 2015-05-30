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

import com.makechip.util.Log;

/**
*** This is a special case of a DatalogTextRecord. It is used instead of
*** DatalogTextRecord when the text is of the form:
*** TEXT_DATA : testName : valueText [ : testNumber [ : siteNumber [ : headNumber ]]]
*** (The testNumber, siteNumber, and headNumber are optional; if they are missing
*** they will default to a value of 0)
*** @author eric
*** @version $Id: DatalogTextRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class DatalogTestRecord extends TestRecord
{
    private final String text;
    private String testName;
    private TestID id;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public DatalogTestRecord(int sequenceNumber, IdentityDatabase idb, byte[] data)
    {
        super(sequenceNumber, data);
        ptr = 0;
        text = getCn();
        id = TestID.createTestID(idb, testNumber, testName);
    }
    
    public DatalogTestRecord(int sequenceNumber, IdentityDatabase idb, String text)
    {
    	super(sequenceNumber, null);
    	this.text = text;
        id = TestID.createTestID(idb, testNumber, testName);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    TEXT_DATA: ").append(text).append(Log.eol);
        return(sb.toString());
    }

	@Override
	protected void toBytes()
	{
		bytes = getCnBytes(text);
	}

	@Override
	public TestID getTestId() 
	{
		return(id);
	}

	@Override
	protected void setTestName(String testName) 
	{
		this.testName = testName;
	}

}
