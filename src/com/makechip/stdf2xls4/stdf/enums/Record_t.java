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

package com.makechip.stdf2xls4.stdf.enums;

import java.util.EnumSet;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.*;

/**
*** @author eric
*** @version $Id: Record_t.java 261 2008-10-23 00:14:14Z ericw $
**/
public enum Record_t
{
    ATR("Audit Trail Record",                (short) 0,  (short) 20, AuditTrailRecord::new),
    BPS("Begin Program Selection Record",    (short) 20, (short) 10, BeginProgramSectionRecord::new),
    DTR("Datalog Text Record",               (short) 50, (short) 30, DatalogTextRecord::new),
    DTRX("Datalog Test Record",              (short) 50, (short) 31, DatalogTestRecord::new),
    EPS("End Program Selection Record",      (short) 20, (short) 20, EndProgramSectionRecord::new),
    FAR("File Attributes Record",            (short) 0,  (short) 10, FileAttributesRecord::new),
    FTR("Functional Test Record",            (short) 15, (short) 20, FunctionalTestRecord::new),
    GDR("Generic Data Record",               (short) 50, (short) 10, GenericDataRecord::new),
    HBR("Hardware Bin Record",               (short) 1,  (short) 40, HardwareBinRecord::new),
    MIR("Master Information Record",         (short) 1,  (short) 10, MasterInformationRecord::new),
    MPR("Multiple-Result Parametric Record", (short) 15, (short) 15, MultipleResultParametricRecord::new),
    MRR("Master Results Record",             (short) 1,  (short) 20, MasterResultsRecord::new),
    PCR("Part Count Record",                 (short) 1,  (short) 30, PartCountRecord::new),
    PGR("Pin Group Record",                  (short) 1,  (short) 62, PinGroupRecord::new),
    PIR("Part Information Record",           (short) 5,  (short) 10, PartInformationRecord::new),
    PLR("Pin List Record",                   (short) 1,  (short) 63, PinListRecord::new),
    PMR("Pin Map Record",                    (short) 1,  (short) 60, PinMapRecord::new),
    PRR("Part Results Record",               (short) 5,  (short) 20, PartResultsRecord::new),
    PTR("Parametric Test Record",            (short) 15, (short) 10, ParametricTestRecord::new),
    RDR("Retest Data Record",                (short) 1,  (short) 70, RetestDataRecord::new),
    SBR("Software Bin Record",               (short) 1,  (short) 50, SoftwareBinRecord::new),
    SDR("Site Description Record",           (short) 1,  (short) 80, SiteDescriptionRecord::new),
    TSR("Test Synopsis Record",              (short) 10, (short) 30, TestSynopsisRecord::new),
    WCR("Wafer Configuration Record",        (short) 2,  (short) 30, WaferConfigurationRecord::new),
    WIR("Wafer Information Record",          (short) 2,  (short) 10, WaferInformationRecord::new),
    WRR("Wafer Results Record",              (short) 2,  (short) 20, WaferResultsRecord::new);

    public final String description;
    public final short recordType;
    public final short recordSubType;
    private FuncIf ctor;
    
    @FunctionalInterface
    private interface FuncIf 
    { 
    	StdfRecord getRecord(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options);
    }

    /**
    **/
    private Record_t(String description, short recordType, short recordSubType, FuncIf ctor)
    {
        this.description = description;
        this.recordType = recordType;
        this.recordSubType = recordSubType;
        this.ctor = ctor;
    }

    public static Record_t getRecordType(int type, int subType)
    {
        EnumSet<Record_t> set = EnumSet.allOf(Record_t.class);
        Record_t r = set.stream().filter(p -> p.recordType == type && p.recordSubType == subType).findFirst().orElse(null);
        return(r);
    }

    public StdfRecord getInstance(Cpu_t cpu, TestIdDatabase tdb, int recLen, ByteInputStream is, CliOptions options)
    {
    	return(ctor.getRecord(cpu, tdb, recLen, is, options));
    }

}
