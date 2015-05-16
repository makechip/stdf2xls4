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
import com.makechip.util.Log;


/**
*** @author eric
*** @version $Id: PinListRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class PinListRecord extends StdfRecord
{
    private final int[] pinIndex;
    private final int[] mode;
    private final int[] radix;
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
        int k = getU2(0);
        pinIndex = new int[k];
        Arrays.setAll(pinIndex, p -> getU2(-1));
        mode = new int[k];
        Arrays.setAll(mode, p -> getU2(0));
        radix = new int[k];
        Arrays.setAll(radix, p -> getU1((short) 0));
        pgmChar = new String[k];
        Arrays.setAll(pgmChar, p -> getCn());
        rtnChar = new String[k];
        Arrays.setAll(rtnChar, p -> getCn());
        pgmChal = new String[k];
        Arrays.setAll(pgmChal, p -> getCn());
        rtnChal = new String[k];
        Arrays.setAll(rtnChal, p -> getCn());
        
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU2Bytes(pinIndex.length));
		Arrays.stream(pinIndex).forEach(p -> l.addAll(getU2Bytes(p)));
		Arrays.stream(mode).forEach(p -> l.addAll(getU2Bytes(p)));
		Arrays.stream(radix).forEach(p -> l.addAll(getU1Bytes((short) p)));
		Arrays.stream(pgmChar).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(rtnChar).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(pgmChal).forEach(p -> l.addAll(getCnBytes(p)));
		Arrays.stream(rtnChal).forEach(p -> l.addAll(getCnBytes(p)));
		bytes = l.toArray();
	}
	
	public PinListRecord(int sequenceNumber, int devNum,
	    int[] pinIndex,
	    int[] mode,
	    int[] radix,
	    String[] pgmChar,
	    String[] rtnChar,
	    String[] pgmChal,
	    String[] rtnChal)
	{
		super(Record_t.PLR, sequenceNumber, devNum, null);
		this.pinIndex = Arrays.copyOf(pinIndex, pinIndex.length);
		this.mode = Arrays.copyOf(mode, mode.length);
		this.radix = Arrays.copyOf(radix, radix.length);
		this.pgmChar = Arrays.copyOf(pgmChar, pgmChar.length);
		this.rtnChar = Arrays.copyOf(rtnChar, pgmChar.length);
		this.pgmChal = Arrays.copyOf(pgmChal, pgmChar.length);
		this.rtnChal = Arrays.copyOf(rtnChal, pgmChar.length);
	}

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(":").append(Log.eol);
        sb.append("    pin index:");
        Arrays.stream(pinIndex).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    pin mode:");
        Arrays.stream(mode).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    pin radix:");
        Arrays.stream(radix).forEach(p -> sb.append(" " + p));
        sb.append(Log.eol);
        sb.append("    program-state encoding characters-R:");
        Arrays.stream(pgmChar).forEach(p -> sb.append(" ").append(p));
        sb.append(Log.eol);
        sb.append("    return-state encoding characters-R:");
        Arrays.stream(rtnChar).forEach(p -> sb.append(" ").append(p));
        sb.append(Log.eol);
        sb.append("    program-state encoding characters-L:");
        Arrays.stream(pgmChal).forEach(p -> sb.append(" ").append(p));
        sb.append(Log.eol);
        sb.append("    return-state encoding characters-L:");
        Arrays.stream(rtnChal).forEach(p -> sb.append(" ").append(p));
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
    public int[] getRadix()
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
