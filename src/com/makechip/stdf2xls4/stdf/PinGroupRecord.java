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
*** @version $Id: PinGroupRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinGroupRecord extends StdfRecord
{
    private final int groupIndex;
    private final String groupName;
    private final int k;
    private final int[] pmrIdx;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public PinGroupRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PGR, sequenceNumber, devNum, data);
        groupIndex = getU2(-1);
        groupName = getCn();
        k = getU2(0);
        pmrIdx = new int[k];
        for (int i=0; i<k; i++) pmrIdx[i] = getU2(-1);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    group index: " + groupIndex); sb.append(Log.eol);
        sb.append("    group name: "); sb.append(groupName); sb.append(Log.eol);
        sb.append("    pmr indicies:");
        for (int i=0; i<k; i++) 
        {
            sb.append(" ");
            sb.append("" + pmrIdx[i]);
        }
        sb.append(Log.eol);
        return(sb.toString());
    }
    
    /**
     * @return the groupIndex
     */
    public int getGroupIndex()
    {
        return groupIndex;
    }

    /**
     * @return the groupName
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * @return the pmrIdx
     */
    public int[] getPmrIdx()
    {
        return pmrIdx;
    }
    
    

}
