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
*** @version $Id: HardwareBinRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class HardwareBinRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteNumber;
    private final int hwBin;
    private final long binCnt;
    private final String pf;
    private final String binName;
    /**
    *** @param p1
    *** @param p2
    **/
    public HardwareBinRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.HBR, sequenceNumber, devNum, data);
        headNumber = getU1((short) 0);
        siteNumber = getU1((short) 0);
        hwBin = getU2(-1);
        binCnt = getU4(0);
        pf = getFixedLengthString(1);
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
        sb.append("    HW bin number:" + hwBin); sb.append(Log.eol);
        sb.append("    bin count: " + binCnt); sb.append(Log.eol);
        sb.append("    P/F: "); sb.append(pf); sb.append(Log.eol);
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
     * @return the hwBin
     */
    public int getHwBin()
    {
        return hwBin;
    }

    /**
     * @return the binCnt
     */
    public long getBinCnt()
    {
        return binCnt;
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
