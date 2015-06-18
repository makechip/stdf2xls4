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
import gnu.trove.map.hash.TIntObjectHashMap;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: PinMapRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinMapRecord extends StdfRecord
{
    public final int pmrIdx;
    public final int channelType;
    public final String channelName;
    public final String physicalPinName;
    public final String logicalPinName;
    public final short headNumber;
    public final short siteNumber;
    
    public PinMapRecord(TestIdDatabase tdb, DefaultValueDatabase idb, byte[] data)
    {
        super(Record_t.PMR, data);
        pmrIdx = getU2(-1);
        channelType = getU2(-1);
        channelName = getCn();
        physicalPinName = getCn();
        logicalPinName = getCn();
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) 1);
        TIntObjectHashMap<String> m1 = idb.chanMap.get(siteNumber);
        if (m1 == null)
        {
        	m1 = new TIntObjectHashMap<>();
        	idb.chanMap.put(siteNumber, m1);
        	m1.put(pmrIdx, channelName);
        }
        else
        {
        	String name = m1.get(pmrIdx);
        	if (name == null) m1.put(pmrIdx, channelName);
        	else
        	{
        		if (!name.equals(channelName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + channelName);
        	}
        }
        TIntObjectHashMap<String> m2 = idb.physMap.get(siteNumber);
        if (m2 == null)
        {
        	m2 = new TIntObjectHashMap<>();
        	idb.physMap.put(siteNumber, m2);
        	m2.put(siteNumber, physicalPinName);
        }
        else
        {
        	String name = m2.get(pmrIdx);
        	if (name == null) m2.put(pmrIdx, physicalPinName);
        	else
        	{
        		if (!name.equals(physicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + physicalPinName);
        	}
        }
        TIntObjectHashMap<String> m3 = idb.logMap.get(siteNumber);
        if (m3 == null)
        {
        	m3 = new TIntObjectHashMap<>();
        	idb.logMap.put(siteNumber, m3);
        	m3.put(pmrIdx, logicalPinName);
        }
        else
        {
        	String name = m3.get(pmrIdx);
        	if (name == null) m3.put(pmrIdx, logicalPinName);
        	else
        	{
        		if (!name.equals(logicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + logicalPinName);
        	}
        }
    }
    
    public PinMapRecord(DefaultValueDatabase idb,
    		            int pmrIdx,
    		            int channelType,
    		            String channelName,
    		            String physicalPinName,
    		            String logicalPinName,
    		            short headNumber,
    		            short siteNumber)
    		            {
    						super(Record_t.PMR, null);
    						this.pmrIdx = pmrIdx;
    						this.channelType = channelType;
    						this.channelName = channelName;
    						this.physicalPinName = physicalPinName;
    						this.logicalPinName = logicalPinName;
    						this.headNumber = headNumber;
    						this.siteNumber = siteNumber;
    				        TIntObjectHashMap<String> m1 = idb.chanMap.get(siteNumber);
    				        if (m1 == null)
    				        {
    				        	m1 = new TIntObjectHashMap<>();
    				        	idb.chanMap.put(siteNumber, m1);
    				        	m1.put(pmrIdx, channelName);
    				        }
    				        else
    				        {
    				        	String name = m1.get(pmrIdx);
    				        	if (name == null) m1.put(pmrIdx, channelName);
    				        	else
    				        	{
    				        		if (!name.equals(channelName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + channelName);
    				        	}
    				        }
    				        TIntObjectHashMap<String> m2 = idb.physMap.get(siteNumber);
    				        if (m2 == null)
    				        {
    				        	m2 = new TIntObjectHashMap<>();
    				        	idb.physMap.put(siteNumber, m2);
    				        	m2.put(siteNumber, physicalPinName);
    				        }
    				        else
    				        {
    				        	String name = m2.get(pmrIdx);
    				        	if (name == null) m2.put(pmrIdx, physicalPinName);
    				        	else
    				        	{
    				        		if (!name.equals(physicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + physicalPinName);
    				        	}
    				        }
    				        TIntObjectHashMap<String> m3 = idb.logMap.get(siteNumber);
    				        if (m3 == null)
    				        {
    				        	m3 = new TIntObjectHashMap<>();
    				        	idb.logMap.put(siteNumber, m3);
    				        	m3.put(pmrIdx, logicalPinName);
    				        }
    				        else
    				        {
    				        	String name = m3.get(pmrIdx);
    				        	if (name == null) m3.put(pmrIdx, logicalPinName);
    				        	else
    				        	{
    				        		if (!name.equals(logicalPinName)) Log.fatal("Inconsistent pin map detected: " + name + " vs " + logicalPinName);
    				        	}
    				        }
    		            }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU2Bytes(pmrIdx));
		l.addAll(getU2Bytes(channelType));
		l.addAll(getCnBytes(channelName));
		l.addAll(getCnBytes(physicalPinName));
		l.addAll(getCnBytes(logicalPinName));
		l.addAll(getU1Bytes(headNumber));
		l.addAll(getU1Bytes(siteNumber));
		bytes = l.toArray();
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    pin index: " + pmrIdx); sb.append(Log.eol);
        sb.append("    channel type: " + channelType); sb.append(Log.eol);
        sb.append("    channel name: "); sb.append(channelName); sb.append(Log.eol);
        sb.append("    physical pin name: "); sb.append(physicalPinName); sb.append(Log.eol);
        sb.append("    logical pin name: "); sb.append(logicalPinName); sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        return(sb.toString());
    }

}
