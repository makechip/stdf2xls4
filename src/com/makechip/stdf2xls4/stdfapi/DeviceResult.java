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

import java.util.LinkedHashMap;
import java.util.stream.Stream;

import com.makechip.stdf2xls4.stdf.PartResultsRecord;
import com.makechip.stdf2xls4.stdf.TestID;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.util.Log;

public class DeviceResult
{
    public final boolean pass;
    public final int hwbin;
    public final int swbin;
    public final boolean abnormalEOT;
    public final boolean noPassFailIndication;
    private final LinkedHashMap<TestID, TestResult> tests;
    
    public DeviceResult(PartResultsRecord prr, LinkedHashMap<TestID, TestResult> tests)
    {
    	this.pass = !prr.partInfoFlags.contains(PartInfoFlag_t.PART_FAILED);
    	this.hwbin = prr.hwBinNumber;
    	this.swbin = prr.swBinNumber;
    	this.abnormalEOT = prr.partInfoFlags.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST);
    	this.noPassFailIndication = prr.partInfoFlags.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION);
        this.tests = tests;
    }
    
    @Override
	public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("DEVICE_RESULT:"); sb.append(Log.eol);
    	sb.append("    pass = "); sb.append("" + pass); sb.append(Log.eol);
    	for (TestID id : tests.keySet())
    	{
    		TestResult tr = tests.get(id);
    		sb.append("    "); sb.append(tr.toString());
    		sb.append(Log.eol);
    	}
    	return(sb.toString());
    }
    
    public Stream<TestID> getTestIDs() { return(tests.keySet().stream()); }
    public TestResult getTestResult(TestID id) { return(tests.get(id)); }
    public int getNumTests() { return(tests.size()); };
    

}
