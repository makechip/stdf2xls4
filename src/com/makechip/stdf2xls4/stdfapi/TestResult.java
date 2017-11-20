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
import java.util.Set;

import static com.makechip.stdf2xls4.stdf.enums.TestFlag_t.*;

import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;

public class TestResult
{
    private final Set<TestFlag_t> flags;
    public final Error_t error;

    public TestResult(final Set<TestFlag_t> flags)
    {
    	this.flags = EnumSet.noneOf(TestFlag_t.class);
    	if (flags != null)
    	{
    	    flags.stream().forEach(s -> this.flags.add(s));
    	    if (flags.contains(ABORT)) error = Error_t.ABORT;
    	    else if (flags.contains(NO_PASS_FAIL)) error = Error_t.INVALID;
    	    else if (flags.contains(UNRELIABLE)) error = Error_t.UNRELIABLE;
    	    else if (flags.contains(ALARM)) error = Error_t.ALARM;
    	    else if (flags.contains(TIMEOUT)) error = Error_t.TIMEOUT;
    	    else if (flags.contains(FAIL)) error = Error_t.FAIL;
    	    else error = Error_t.PASS;
    	}
    	else error = Error_t.PASS;
    }
    
    public boolean fail()
    {
    	return(flags.contains(ALARM) || flags.contains(UNRELIABLE) || flags.contains(TIMEOUT) ||
    		   flags.contains(ABORT) || flags.contains(NO_PASS_FAIL) || flags.contains(FAIL));
    }
    
    public boolean pass()
    {
    	return(!fail());
    }
    
    public boolean alarm() { return(flags.contains(ALARM)); }
    
    public boolean unreliable() { return(flags.contains(UNRELIABLE)); }
    
    public boolean timeout() { return(flags.contains(TIMEOUT)); }
    
    public boolean abort() { return(flags.contains(ABORT)); }
    
    public boolean noPassFail() { return(flags.contains(NO_PASS_FAIL)); }
    
    @Override
    public String toString()
    {
        return(pass() ? "PASS" : "FAIL");
    }

}
