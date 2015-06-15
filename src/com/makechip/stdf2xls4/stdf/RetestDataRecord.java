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

import java.util.Arrays;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: RetestDataRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class RetestDataRecord extends StdfRecord
{
    private final int[] retestBins;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public RetestDataRecord(int sequenceNumber, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.RDR, sequenceNumber, data);
        int k = getU2(0);
        retestBins = new int[k];
        Arrays.setAll(retestBins, p -> getU2(-1));
    }
    
    public RetestDataRecord(int sequenceNumber, int[] retestBins)
    {
    	super(Record_t.RDR, sequenceNumber, null);
    	this.retestBins = Arrays.copyOf(retestBins, retestBins.length);
    }

	@Override
	protected void toBytes()
	{
	    TByteArrayList l = new TByteArrayList();	
	    l.addAll(getU2Bytes(retestBins.length));
	    Arrays.stream(retestBins).forEach(p -> l.addAll(getU2Bytes(p)));
	    bytes = l.toArray();
	}
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    retest bins:");
        Arrays.stream(retestBins).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        return(sb.toString());
    }
    
    /**
     * @return the retestBins
     */
    public int[] getRetestBins()
    {
        return Arrays.copyOf(retestBins, retestBins.length);
    }

    

}
