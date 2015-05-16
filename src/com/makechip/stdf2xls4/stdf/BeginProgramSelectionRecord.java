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
*** @author eric
*** @version $Id: BeginProgramSelectionRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class BeginProgramSelectionRecord extends StdfRecord
{
    private String seqName;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public BeginProgramSelectionRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.BPS, sequenceNumber, devNum, data);
        seqName = getCn();
    }
    
    public BeginProgramSelectionRecord(int sequenceNumber, int devNum, String seqName)
    {
    	super(Record_t.BPS, sequenceNumber, devNum, null);
    	this.seqName = seqName;
    }
    
    public String getSeqName() { return(seqName); }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("BeginProgramSelectionRecord:").append(Log.eol);
        sb.append("    SEQ_NAME: ").append(seqName).append(Log.eol).append(Log.eol);
        return(sb.toString());
    }

	@Override
	protected void toBytes()
	{
	    bytes = getCnBytes(seqName);	
	}

}
