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

import java.util.ArrayList;
import java.util.List;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: GenericDataRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class GenericDataRecord extends StdfRecord
{
    private final List<Object> list;
    /**
    *** @param p1
    *** @param p2
    **/
    public GenericDataRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.GDR, sequenceNumber, devNum, data);
        list = new ArrayList<Object>();
        while (true)
        {
            int cnt = getU2(0);
            if (cnt == 0) break;
            int type = 0;
            while (type == 0) type = getU1((short) -1);
            switch (type)
            {
            case 1:  for (int j=0; j<cnt; j++) list.add(getU1((short) 0)); break;
            case 2:  for (int j=0; j<cnt; j++) list.add(getU2(0)); break;
            case 3:  for (int j=0; j<cnt; j++) list.add(getU4(0)); break;
            case 4:  for (int j=0; j<cnt; j++) list.add(getI1((byte) 0)); break;
            case 5:  for (int j=0; j<cnt; j++) list.add(getI2((short) 0)); break;
            case 6:  for (int j=0; j<cnt; j++) list.add(getI4(0)); break;
            case 7:  for (int j=0; j<cnt; j++) list.add(getR4(0.0f)); break;
            case 8:  for (int j=0; j<cnt; j++) list.add(getR8(0.0)); break;
            case 10: for (int j=0; j<cnt; j++) list.add(getCn()); break;
            case 11: for (int j=0; j<cnt; j++) list.add(getBn()); break;
            case 12: for (int j=0; j<cnt; j++) list.add(getDn()); break;
            case 13: for (int j=0; j<cnt; j++) list.add(getNibbles(1)); break;
            default: Log.fatal("Unknown data type in GenericDataRecord");
            }
        }
    }

    public List<Object> getData() { return(list); }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        for (Object o : list)
        {
        	if (o == null) continue;
            sb.append("    ");
            sb.append(o.toString());
            sb.append(Log.eol);
        }
        return(sb.toString());
    }
    
    
}
