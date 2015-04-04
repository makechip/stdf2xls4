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

import java.util.Date;

import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: MasterResultsRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterResultsRecord extends StdfRecord
{
    private final String finishDate;
    private final String dispCode;
    private final String lotDesc;
    private final String execDesc;
    
    /**
    *** @param p1
    *** @param p2
    **/
    public MasterResultsRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MRR, sequenceNumber, devNum, data);
        long d = getU4(0);
        finishDate = new Date(d * 1000L).toString();
        dispCode = getFixedLengthString(1);
        lotDesc = getCn();
        execDesc = getCn();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    finish date: "); sb.append(finishDate); sb.append(Log.eol);
        sb.append("    lot disposition code: "); sb.append(dispCode); sb.append(Log.eol);
        sb.append("    user lot description: "); sb.append(lotDesc); sb.append(Log.eol);
        sb.append("    exec lot description: "); sb.append(execDesc); sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the finishDate
     */
    public String getFinishDate()
    {
        return finishDate;
    }

    /**
     * @return the dispCode
     */
    public String getDispCode()
    {
        return dispCode;
    }

    /**
     * @return the lotDesc
     */
    public String getLotDesc()
    {
        return lotDesc;
    }

    /**
     * @return the execDesc
     */
    public String getExecDesc()
    {
        return execDesc;
    }

}
