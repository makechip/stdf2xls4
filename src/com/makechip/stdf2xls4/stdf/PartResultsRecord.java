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

import java.util.EnumSet;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: PartResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PartResultsRecord extends StdfRecord
{
	private static int sn = -1;
    public static enum PartInfoFlag_t
    {
        RETEST_EXEC0,
        RETEST_EXEC1,
        ABNORMAL_END_OF_TEST,
        PART_FAILED,
        NO_PASS_FAIL_INDICATION;
    }
    
    private final short headNumber;
    private final short siteNumber;
    private final EnumSet<PartInfoFlag_t> partInfoFlags;
    private final int numExecs;
    private final int hwBinNumber;
    private final int swBinNumber;
    private final short xCoord;
    private final short yCoord;
    private final long testTime;
    private String partID;
    
    
    public void setPartID(String partID)
    {
        this.partID = partID;
    }


    private final String partDescription;
    private final byte[] repair;
    
    public boolean abnormalEOT() { return(partInfoFlags.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST)); }
    public boolean failed() { return(partInfoFlags.contains(PartInfoFlag_t.PART_FAILED)); }
    public boolean noPassFailIndication() { return(partInfoFlags.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION)); }
    
    /**
    *** @param p1
    **/
    public PartResultsRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PRR, sequenceNumber, devNum, data);
        headNumber = getU1((short) -1);
        siteNumber = getU1((short) -1);
        int f = getI1((byte) 0);
        partInfoFlags = EnumSet.noneOf(PartInfoFlag_t.class);
        if ((f & 1)  ==  1) partInfoFlags.add(PartInfoFlag_t.RETEST_EXEC0);
        if ((f & 2)  ==  2) partInfoFlags.add(PartInfoFlag_t.RETEST_EXEC1);
        if ((f & 4)  ==  4) partInfoFlags.add(PartInfoFlag_t.ABNORMAL_END_OF_TEST);
        if ((f & 8)  ==  8) partInfoFlags.add(PartInfoFlag_t.PART_FAILED);
        if ((f & 16) == 16) partInfoFlags.add(PartInfoFlag_t.NO_PASS_FAIL_INDICATION);
        numExecs = getU2(0);
        hwBinNumber = getU2(-1);
        swBinNumber = getU2(-1);
        xCoord = getI2((short) -32768);
        yCoord = getI2((short) -32768);
        testTime = getU4(-1);
        partID = getCn();
        if (partID == null)
        {
        	partID = "" + sn;
        	sn--;
        }
        partDescription = getCn();
        repair = getBn();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    head number: " + headNumber); sb.append(Log.eol);
        sb.append("    site number: " + siteNumber); sb.append(Log.eol);
        sb.append("    test flags:");
        for (PartInfoFlag_t t : partInfoFlags)
        {
            sb.append(" ");
            sb.append(t.toString());
        }
        sb.append(Log.eol);
        sb.append("    number of tests executed: " + numExecs); sb.append(Log.eol);
        sb.append("    hardware bin number: " + hwBinNumber); sb.append(Log.eol);
        sb.append("    software bin number: " + swBinNumber); sb.append(Log.eol);
        sb.append("    wafer X-coordinate: " + xCoord); sb.append(Log.eol);
        sb.append("    wafer Y-coordinate: " + yCoord); sb.append(Log.eol);
        sb.append("    test time: " + testTime); sb.append(Log.eol);
        sb.append("    part ID: "); sb.append(partID); sb.append(Log.eol);
        sb.append("    part description: "); sb.append(partDescription); sb.append(Log.eol);
        if (repair != null)
        {
        	sb.append("    repair info:");
        	for (int i=0; i<repair.length; i++)
        	{
        		sb.append(" ");
        		sb.append("" + repair[i]);
        	}
        }
        sb.append(Log.eol);
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
     * @return the partInfoFlags
     */
    public EnumSet<PartInfoFlag_t> getPartInfoFlags()
    {
        return partInfoFlags;
    }


    /**
     * @return the numExecs
     */
    public int getNumExecs()
    {
        return numExecs;
    }


    /**
     * @return the hwBinNumber
     */
    public int getHwBinNumber()
    {
        return hwBinNumber;
    }


    /**
     * @return the swBinNumber
     */
    public int getSwBinNumber()
    {
        return swBinNumber;
    }


    /**
     * @return the xCoord
     */
    public short getxCoord()
    {
        return xCoord;
    }


    /**
     * @return the yCoord
     */
    public short getyCoord()
    {
        return yCoord;
    }


    /**
     * @return the testTime
     */
    public long getTestTime()
    {
        return testTime;
    }


    /**
     * @return the partID
     */
    public String getPartID()
    {
        return partID;
    }


    /**
     * @return the partDescription
     */
    public String getPartDescription()
    {
        return partDescription;
    }


    /**
     * @return the repair
     */
    public byte[] getRepair()
    {
        return repair;
    }

}
