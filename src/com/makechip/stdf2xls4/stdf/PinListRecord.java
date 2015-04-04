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
*** @version $Id: PinListRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinListRecord extends StdfRecord
{
    private final int k;
    private final int[] pinIndex;
    private final int[] mode;
    private final short[] radix;
    private final String[] pgmChar;
    private final String[] rtnChar;
    private final String[] pgmChal;
    private final String[] rtnChal;
    
    /**
    *** @param p1
    **/
    public PinListRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.PLR, sequenceNumber, devNum, data);
        k = getU2(0);
        pinIndex = new int[k];
        for (int i=0; i<k; i++) pinIndex[i] = getU2(-1);
        mode = new int[k];
        for (int i=0; i<k; i++) mode[i] = getU2(0);
        radix = new short[k];
        for (int i=0; i<k; i++) radix[i] = getU1((short) 0);
        pgmChar = new String[k];
        pgmChal = new String[k];
        rtnChar = new String[k];
        rtnChal = new String[k];
        for (int i=0; i<k; i++) pgmChar[i] = getCn();
        for (int i=0; i<k; i++) rtnChar[i] = getCn();
        for (int i=0; i<k; i++) pgmChal[i] = getCn();
        for (int i=0; i<k; i++) rtnChal[i] = getCn();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    pin group indicies:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + pinIndex[i]);
        }
        sb.append(Log.eol);
        sb.append("    pin mode:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + mode[i]);
        }
        sb.append(Log.eol);
        sb.append("    pin radix:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + radix[i]);
        }
        sb.append(Log.eol);
        sb.append("    program-state encoding characters-R:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + pgmChar[i]);
        }
        sb.append(Log.eol);
        sb.append("    return-state encoding characters-R:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + rtnChar[i]);
        }
        sb.append(Log.eol);
        sb.append("    program-state encoding characters-L:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + pgmChal[i]);
        }
        sb.append(Log.eol);
        sb.append("    return-state encoding characters-L:");
        for (int i=0; i<k; i++)
        {
            sb.append(" ");
            sb.append("" + rtnChal[i]);
        }
        sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the pinIndex
     */
    public int[] getPinIndex()
    {
        return pinIndex;
    }

    /**
     * @return the mode
     */
    public int[] getMode()
    {
        return mode;
    }

    /**
     * @return the radix
     */
    public short[] getRadix()
    {
        return radix;
    }

    /**
     * @return the pgmChar
     */
    public String[] getPgmChar()
    {
        return pgmChar;
    }

    /**
     * @return the rtnChar
     */
    public String[] getRtnChar()
    {
        return rtnChar;
    }

    /**
     * @return the pgmChal
     */
    public String[] getPgmChal()
    {
        return pgmChal;
    }

    /**
     * @return the rtnChal
     */
    public String[] getRtnChal()
    {
        return rtnChal;
    }

}
