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

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: EndProgramSelectionRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class EndProgramSelectionRecord extends StdfRecord
{
    /**
    *** @param p1
    *** @param p2
    **/
    public EndProgramSelectionRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.EPS, data);
    }
    
    public EndProgramSelectionRecord()
    {
    	super(Record_t.EPS, null);
    }
    
    @Override
    public String toString()
    {
        return("EndProgramSelectionRecord" + Log.eol);
    }

	@Override
	protected void toBytes()
	{
	    bytes = new byte[0];	
	}
    
}
