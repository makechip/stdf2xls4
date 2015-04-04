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
*** @version $Id: PartCountRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PartCountRecord extends StdfRecord
{
    private final short headNumber;
    private final short siteNumber;
    private final long partsTested;
    private final long partsReTested;
    private final long aborts;
    private final long good;
    private final long functional;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public PartCountRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PCR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        partsTested = getU4(-1);
        partsReTested = getU4(-1);
        aborts = getU4(-1);
        good = getU4(-1);
        functional = getU4(-1);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        sb.append("    parts tested: " + partsTested); sb.append(Log.eol);
        sb.append("    number of aborts: " + aborts); sb.append(Log.eol);
        sb.append("    number good: " + good); sb.append(Log.eol);
        sb.append("    number functional: " + functional); sb.append(Log.eol);
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
     * @return the partsTested
     */
    public long getPartsTested()
    {
        return partsTested;
    }

    /**
     * @return the partsReTested
     */
    public long getPartsReTested()
    {
        return partsReTested;
    }

    /**
     * @return the aborts
     */
    public long getAborts()
    {
        return aborts;
    }

    /**
     * @return the good
     */
    public long getGood()
    {
        return good;
    }

    /**
     * @return the functional
     */
    public long getFunctional()
    {
        return functional;
    }

}
