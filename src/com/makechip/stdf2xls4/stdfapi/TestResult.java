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

    public TestResult(final Set<TestFlag_t> flags)
    {
    	this.flags = EnumSet.noneOf(TestFlag_t.class);
    	flags.stream().forEach(s -> this.flags.add(s));
    }
    
    boolean fail()
    {
    	return(flags.contains(ALARM) || flags.contains(UNRELIABLE) || flags.contains(TIMEOUT) ||
    		   flags.contains(ABORT) || flags.contains(NO_PASS_FAIL) || flags.contains(FAIL));
    }
    
    boolean pass()
    {
    	return(!fail());
    }
    
    boolean alarm() { return(flags.contains(ALARM)); }
    
    boolean unreliable() { return(flags.contains(UNRELIABLE)); }
    
    boolean timeout() { return(flags.contains(TIMEOUT)); }
    
    boolean abort() { return(flags.contains(ABORT)); }
    
    boolean noPassFail() { return(flags.contains(NO_PASS_FAIL)); }
    
    @Override
    public String toString()
    {
        return(pass() ? "PASS" : "FAIL");
    }

}
