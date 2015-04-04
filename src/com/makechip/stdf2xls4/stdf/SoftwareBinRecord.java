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
*** @version $Id: SoftwareBinRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class SoftwareBinRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteNumber;
    private final int swBinNumber;
    private final int count;
    private final String pf;
    private final String binName;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public SoftwareBinRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.SBR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        swBinNumber = getU2(-1);
        count = getU2(0);
        String s = getFixedLengthString(1);
        byte[] b = s.getBytes();
        if (b.length == 1)
        {
        	if (b[0] == (byte) 0) pf = "0";
        	else pf = s;
        }
        else pf = s;
        binName = getCn();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        sb.append("    software bin number: " + swBinNumber); sb.append(Log.eol);
        sb.append("    bin count: " + count); sb.append(Log.eol);
        sb.append("    pass/fail indication: "); sb.append(pf); sb.append(Log.eol);
        sb.append("    bin name: "); sb.append(binName); sb.append(Log.eol);
        return(sb.toString());
    }
    
    /**
     * @return the headNumber
     */
    public short getHeadNumber()
    {
        return headNumber;
    }

    /**
     * @return the siteNumber
     */
    public short getSiteNumber()
    {
        return siteNumber;
    }

    /**
     * @return the swBinNumber
     */
    public int getSwBinNumber()
    {
        return swBinNumber;
    }

    /**
     * @return the count
     */
    public int getCount()
    {
        return count;
    }

    /**
     * @return the pf
     */
    public String getPf()
    {
        return pf;
    }

    /**
     * @return the binName
     */
    public String getBinName()
    {
        return binName;
    }
    


}
