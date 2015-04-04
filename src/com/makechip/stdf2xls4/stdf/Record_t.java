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

/**
*** @author eric
*** @version $Id: Record_t.java 261 2008-10-23 00:14:14Z ericw $
**/
public enum Record_t
{
    ATR("Audit Trail Record", (short) 0, (short) 20),
    BPS("Begin Program Selection Record", (short) 20, (short) 10),
    DTR("Datalog Text Record", (short) 50, (short) 30),
    EPS("End Program Selection Record", (short) 20, (short) 20),
    FAR("File Attributes Record", (short) 0, (short) 10),
    FTR("Functional Test Record", (short) 15, (short) 20),
    GDR("Generic Data Record", (short) 50, (short) 10),
    HBR("Hardware Bin Record", (short) 1, (short) 40),
    MIR("Master Information Record", (short) 1, (short) 10),
    MPR("Multiple-Result Parametric Record", (short) 15, (short) 15),
    MRR("Master Results Record", (short) 1, (short) 20),
    PCR("Part Count Record", (short) 1, (short) 30),
    PGR("Pin Group Record", (short) 1, (short) 62),
    PIR("Part Information Record", (short) 5, (short) 10),
    PLR("Pin List Record", (short) 1, (short) 63),
    PMR("Pin Map Record", (short) 1, (short) 60),
    PRR("Part Results Record", (short) 5, (short) 20),
    PTR("Parametric Test Record", (short) 15, (short) 10),
    RDR("Retest Data Record", (short) 1, (short) 70),
    SBR("Software Bin Record", (short) 1, (short) 50),
    SDR("Site Description Record", (short) 1, (short) 80),
    TSR("Test Synopsis Record", (short) 10, (short) 30),
    WCR("Wafer Configuration Record", (short) 2, (short) 30),
    WIR("Wafer Information Record", (short) 2, (short) 10),
    WRR("Wafer Results Record", (short) 2, (short) 20);

    private String description;
    private short recordType;
    private short recordSubType;

    /**
    **/
    private Record_t(String description, short recordType, short recordSubType)
    {
        this.description = description;
        this.recordType = recordType;
        this.recordSubType = recordSubType;
    }

    public String getDescription() { return(description); }

    public short getRecordType() { return(recordType); }

    public short getRecordSubType() { return(recordSubType); }

    public static Record_t getRecordType(int type, int subType)
    {
        for (Record_t r : Record_t.class.getEnumConstants())
        {
            if (r.getRecordType() == type && r.getRecordSubType() == subType) 
            {
            	return(r);
            }
        }
        return(null);
    }


}
