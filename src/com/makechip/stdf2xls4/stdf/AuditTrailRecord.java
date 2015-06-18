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


import gnu.trove.list.array.TByteArrayList;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: AuditTrailRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class AuditTrailRecord extends StdfRecord
{
    public final long date;
    public final String cmdLine;
    /**
    *** @param p1
    *** @param p2
    **/
    public AuditTrailRecord(TestIdDatabase tdb, DefaultValueDatabase dvb, byte[] data)
    {
        super(Record_t.ATR, data);
        date = getU4(0);
        cmdLine = getCn();
    }
    
    public AuditTrailRecord(final long date, final String cmdLine)
    {
    	super(Record_t.ATR, null);
    	this.date = date;
    	this.cmdLine = cmdLine;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(":").append(Log.eol);
        sb.append("    MOD_TIM: ").append(date).append(Log.eol);
        sb.append("    CMD_LINE: ").append(cmdLine).append(Log.eol);
        return(sb.toString());
    }

	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU4Bytes(date));
		l.addAll(getCnBytes(cmdLine));
		bytes = l.toArray();
	}

}
