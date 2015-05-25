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

import java.util.EnumSet;

import com.makechip.stdf2xls4.stdf.enums.Error_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import com.makechip.stdf2xls4.stdfapi.TestID;
import com.makechip.util.Log;

public abstract class TestResult
{
    public final boolean pass;
    public final boolean invalid;
    public final boolean unreliable;
    public final boolean alarm;
    public final boolean timeout;
    public final boolean abort;
    public final boolean fail;
    public final Error_t error;
    public final TestID id;

    public TestResult(TestID id, EnumSet<TestFlag_t> flags)
    {
    	this.id = id;
        if (flags == null)
        {
        	alarm = false;
        	unreliable = false;
        	timeout = false;
        	abort = false;
        	invalid = false;
        	pass = true;
        	fail = false;
        }
        else
        {
            alarm = flags.contains(TestFlag_t.ALARM); 
            unreliable = flags.contains(TestFlag_t.UNRELIABLE);
            timeout = flags.contains(TestFlag_t.TIMEOUT);
            abort = flags.contains(TestFlag_t.ABORT);
            invalid = flags.contains(TestFlag_t.NO_PASS_FAIL);
            fail = flags.contains(TestFlag_t.FAIL);
            pass = false;
        }
        if (alarm) error = Error_t.FAIL;
        else if (abort) error = Error_t.FAIL;
        else if (timeout) error = Error_t.FAIL;
        else if (invalid) error = Error_t.FAIL;
        else if (unreliable) error = Error_t.FAIL;
        else if (fail) error = Error_t.FAIL;
        else error = Error_t.PASS;
    }
    
    @Override
    public abstract String toString();
    
    protected String getTestHeader()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TEST: ").append(id.toString()).append(Log.eol);
        sb.append("RESULT: ").append((pass ? "PASS" : "FAIL")).append(Log.eol);
        sb.append("Error: ").append(error.toString()).append(Log.eol);
        return(sb.toString());
    }

}
