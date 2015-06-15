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

import java.util.Date;

import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: MasterInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterInformationRecord extends StdfRecord
{
    public final long jobDate;
    public final long testDate;
    public final short stationNumber;
    public final char testModeCode;
    public final char lotRetestCode;
    public final char dataProtectionCode;
    public final int burnInTime;
    public final char cmdModeCode;
    public final String lotID;
    public final String partType;
    public final String nodeName;
    public final String testerType;
    public final String jobName;
    public final String jobRevisionNumber;
    public final String sublotID;
    public final String operatorName;
    public final String execSoftware;
    public final String execSoftwareVersion;
    public final String stepCode;
    public final String temperature;
    public final String userText;
    public final String auxDataFile;
    public final String packageType;
    public final String familyID;
    public final String dateCode;
    public final String facilityID;
    public final String floorID;
    public final String fabID;
    public final String frequency;
    public final String specName;
    public final String specVersion;
    public final String flowID;
    public final String setupID;
    public final String designRevision;
    public final String engLotID;
    public final String romCodeID;
    public final String testerSerialNumber;
    public final String supervisorID;
    private long timeStamp; // not an STDF value; for filename timestamp tracking 
    
    public MasterInformationRecord(int sequenceNumber,
        long jobDate,
        long testDate,
        short stationNumber,
        char testModeCode,
        char lotRetestCode,
        char dataProtectionCode,
        int burnInTime,
        char cmdModeCode,
        String lotID,
        String partType,
        String nodeName,
        String testerType,
        String jobName,
        String jobRevisionNumber,
        String sublotID,
        String operatorName,
        String execSoftware,
        String execSoftwareVersion,
        String stepCode,
        String temperature,
        String userText,
        String auxDataFile,
        String packageType,
        String familyID,
        String dateCode,
        String facilityID,
        String floorID,
        String fabID,
        String frequency,
        String specName,
        String specVersion,
        String flowID,
        String setupID,
        String designRevision,
        String engLotID,
        String romCodeID,
        String testerSerialNumber,
        String supervisorID,
        long timeStamp)
    {
        super(Record_t.MIR, sequenceNumber, null);
        this.jobDate = jobDate;
        this.testDate = testDate;
        this.stationNumber = stationNumber;
        this.testModeCode = testModeCode;
        this.lotRetestCode = lotRetestCode;
        this.dataProtectionCode = dataProtectionCode;
        this.burnInTime = burnInTime;
        this.cmdModeCode = cmdModeCode;
        this.lotID = lotID;
        this.partType = partType;
        this.nodeName = nodeName;
        this.testerType = testerType;
        this.jobName = jobName;
        this.jobRevisionNumber = jobRevisionNumber;
        this.sublotID = sublotID;
        this.operatorName = operatorName;
        this.execSoftware = execSoftware;
        this.execSoftwareVersion = execSoftwareVersion;
        this.stepCode = stepCode;
        this.temperature = temperature;
        this.userText = userText;
        this.auxDataFile = auxDataFile;
        this.packageType = packageType;
        this.familyID = familyID;
        this.dateCode = dateCode;
        this.facilityID = facilityID;
        this.floorID = floorID;
        this.fabID = fabID;
        this.frequency = frequency;
        this.specName = specName;
        this.specVersion = specVersion;
        this.flowID = flowID;
        this.setupID = setupID;
        this.designRevision = designRevision;
        this.engLotID = engLotID;
        this.romCodeID = romCodeID;
        this.testerSerialNumber = testerSerialNumber;
        this.supervisorID = supervisorID;
        this.timeStamp = timeStamp;
    }
    
    /**
    *** @param p1
    *** @param p2
    **/
    public MasterInformationRecord(int sequenceNumber, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MIR, sequenceNumber, data);
        jobDate = getU4(0);
        testDate = getU4(0);
        stationNumber = getU1((byte) 0);
        testModeCode = getFixedLengthString(1).charAt(0);
        lotRetestCode = getFixedLengthString(1).charAt(0);
        dataProtectionCode = getFixedLengthString(1).charAt(0);
        burnInTime = getU2(0);
        cmdModeCode = getFixedLengthString(1).charAt(0);
        lotID = getCn();
        partType = getCn();
        nodeName = getCn();
        testerType = getCn();
        jobName = getCn();
        jobRevisionNumber = getCn();
        sublotID = getCn();
        operatorName = getCn();
        execSoftware = getCn();
        execSoftwareVersion = getCn();
        stepCode = getCn();
        temperature = getCn();
        userText = getCn();
        auxDataFile = getCn();
        packageType = getCn();
        familyID = getCn();
        dateCode = getCn();
        facilityID = getCn();
        floorID = getCn();
        fabID = getCn();
        frequency = getCn();
        specName = getCn();
        specVersion = getCn();
        flowID = getCn();
        setupID = getCn();
        designRevision = getCn();
        engLotID = getCn();
        romCodeID = getCn();
        testerSerialNumber = getCn();
        supervisorID = getCn();
    }
    
	@Override
	protected void toBytes()
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(getU4Bytes(jobDate));
		l.addAll(getU4Bytes(testDate));
		l.addAll(getU1Bytes(stationNumber));
		l.addAll(getFixedLengthStringBytes("" + testModeCode));
		l.addAll(getFixedLengthStringBytes("" + lotRetestCode));
		l.addAll(getFixedLengthStringBytes("" + dataProtectionCode));
		l.addAll(getU2Bytes(burnInTime));
		l.addAll(getFixedLengthStringBytes("" + cmdModeCode));
		l.addAll(getCnBytes(lotID));
        l.addAll(getCnBytes(partType));
        l.addAll(getCnBytes(nodeName));
        l.addAll(getCnBytes(testerType));
        l.addAll(getCnBytes(jobName));
        l.addAll(getCnBytes(jobRevisionNumber));
        l.addAll(getCnBytes(sublotID));
        l.addAll(getCnBytes(operatorName));
        l.addAll(getCnBytes(execSoftware));
        l.addAll(getCnBytes(execSoftwareVersion));
        l.addAll(getCnBytes(stepCode));
        l.addAll(getCnBytes(temperature));
        l.addAll(getCnBytes(userText));
        l.addAll(getCnBytes(auxDataFile));
        l.addAll(getCnBytes(packageType));
        l.addAll(getCnBytes(familyID));
        l.addAll(getCnBytes(dateCode));
        l.addAll(getCnBytes(facilityID));
        l.addAll(getCnBytes(floorID));
        l.addAll(getCnBytes(fabID));
        l.addAll(getCnBytes(frequency));
        l.addAll(getCnBytes(specName));
        l.addAll(getCnBytes(specVersion));
        l.addAll(getCnBytes(flowID));
        l.addAll(getCnBytes(setupID));
        l.addAll(getCnBytes(designRevision));
        l.addAll(getCnBytes(engLotID));
        l.addAll(getCnBytes(romCodeID));
        l.addAll(getCnBytes(testerSerialNumber));
        l.addAll(getCnBytes(supervisorID));
        bytes = l.toArray();
	}

    @Override
    public String toString()
    {
    	String jdate = new Date(1000L * jobDate).toString();
    	String tdate = new Date(1000L * testDate).toString();
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    job date: ").append(jdate).append(Log.eol);
        sb.append("    test date: ").append(tdate).append(Log.eol);
        sb.append("    station number: " + stationNumber).append(Log.eol);
        sb.append("    test mode code: ").append(testModeCode).append(Log.eol);
        sb.append("    lot retest code: ").append(lotRetestCode).append(Log.eol);
        sb.append("    data protection code: ").append(dataProtectionCode).append(Log.eol);
        sb.append("    burn-in time: " + burnInTime).append(Log.eol);
        sb.append("    command mode code: ").append(cmdModeCode).append(Log.eol);
        sb.append("    lot ID: ").append(lotID).append(Log.eol);
        sb.append("    part type: ").append(partType).append(Log.eol);
        sb.append("    node name: ").append(nodeName).append(Log.eol);
        sb.append("    tester type: ").append(testerType).append(Log.eol);
        sb.append("    job name: ").append(jobName).append(Log.eol);
        sb.append("    job revision number: ").append(jobRevisionNumber).append(Log.eol);
        sb.append("    sublot ID: ").append(sublotID).append(Log.eol);
        sb.append("    operatorName: ").append(operatorName).append(Log.eol);
        sb.append("    exec software: ").append(execSoftware).append(Log.eol);
        sb.append("    exec software revision: ").append(execSoftwareVersion).append(Log.eol);
        sb.append("    step code: ").append(stepCode).append(Log.eol);
        sb.append("    temperature: ").append(temperature).append(Log.eol);
        sb.append("    user text: ").append(userText).append(Log.eol);
        sb.append("    auxilliary data file: ").append(auxDataFile).append(Log.eol);
        sb.append("    package type: ").append(packageType).append(Log.eol);
        sb.append("    family ID: ").append(familyID).append(Log.eol);
        sb.append("    date code: ").append(dateCode).append(Log.eol);
        sb.append("    facility ID: ").append(facilityID).append(Log.eol);
        sb.append("    floor ID: ").append(floorID).append(Log.eol);
        sb.append("    fab ID: ").append(fabID).append(Log.eol);
        sb.append("    frequency: ").append(frequency).append(Log.eol);
        sb.append("    spec name: ").append(specName).append(Log.eol);
        sb.append("    spec version: ").append(specVersion).append(Log.eol);
        sb.append("    flow ID: ").append(flowID).append(Log.eol);
        sb.append("    setup ID: ").append(setupID).append(Log.eol);
        sb.append("    design revision: ").append(designRevision).append(Log.eol);
        sb.append("    engineering lot ID: ").append(engLotID).append(Log.eol);
        sb.append("    ROM code ID: ").append(romCodeID).append(Log.eol);
        sb.append("    tester serial number: ").append(testerSerialNumber).append(Log.eol);
        sb.append("    supervisor ID: ").append(supervisorID).append(Log.eol);
        return(sb.toString());
    }
    
    void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
    
    public long getTimeStamp() { return(timeStamp); }

}
