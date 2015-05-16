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

import com.makechip.util.Log;

/**
*** @author eric
*** @version $Id: MasterInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterInformationRecord extends StdfRecord
{
    private final long jobDate;
    private final long testDate;
    private final short stationNumber;
    private final char testModeCode;
    private final char lotRetestCode;
    private final char dataProtectionCode;
    private final int burnInTime;
    private final char cmdModeCode;
    private final String lotID;
    private final String partType;
    private final String nodeName;
    private final String testerType;
    private final String jobName;
    private final String jobRevisionNumber;
    private final String sublotID;
    private final String operatorName;
    private final String execSoftware;
    private final String execSoftwareVersion;
    private final String stepCode;
    private final String temperature;
    private final String userText;
    private final String auxDataFile;
    private final String packageType;
    private final String familyID;
    private final String dateCode;
    private final String facilityID;
    private final String floorID;
    private final String fabID;
    private final String frequency;
    private final String specName;
    private final String specVersion;
    private final String flowID;
    private final String setupID;
    private final String designRevision;
    private final String engLotID;
    private final String romCodeID;
    private final String testerSerialNumber;
    private final String supervisorID;
    private long fileTimeStamp; // not an STDF value; for filename timestamp tracking 
    
    public MasterInformationRecord(int sequenceNumber, int devNum,
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
        long fileTimeStamp)
    {
        super(Record_t.MIR, sequenceNumber, devNum, null);
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
        this.fileTimeStamp = -1L;
    }
    
    /**
    *** @param p1
    *** @param p2
    **/
    public MasterInformationRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MIR, sequenceNumber, devNum, data);
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
        fileTimeStamp = -1L;
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

    public void setFileTimeStamp(long value) { fileTimeStamp = value; }
    public long getFileTimeStamp() { return(fileTimeStamp); }

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
        sb.append("    supervisor ID: ").append(supervisorID).append(Log.eol).append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the jobDate
     */
    public String getJobDate()
    {
        return(new Date(1000L * jobDate).toString());
    }


    /**
     * @return the testDate
     */
    public String getTestDate()
    {
        return(new Date(1000L * testDate).toString());
    }


    /**
     * @return the stationNumber
     */
    public short getStationNumber()
    {
        return stationNumber;
    }


    /**
     * @return the testModeCode
     */
    public String getTestModeCode()
    {
        return "" + testModeCode;
    }


    /**
     * @return the lotRetestCode
     */
    public String getLotRetestCode()
    {
        return "" + lotRetestCode;
    }


    /**
     * @return the dataProtectionCode
     */
    public String getDataProtectionCode()
    {
        return "" + dataProtectionCode;
    }


    /**
     * @return the burnInTime
     */
    public int getBurnInTime()
    {
        return burnInTime;
    }


    /**
     * @return the cmdModeCode
     */
    public String getCmdModeCode()
    {
        return "" + cmdModeCode;
    }


    /**
     * @return the lotID
     */
    public String getLotID()
    {
        return lotID;
    }


    /**
     * @return the partType
     */
    public String getPartType()
    {
        return partType;
    }


    /**
     * @return the nodeName
     */
    public String getNodeName()
    {
        return nodeName;
    }


    /**
     * @return the testerType
     */
    public String getTesterType()
    {
        return testerType;
    }


    /**
     * @return the jobName
     */
    public String getJobName()
    {
        return jobName;
    }


    /**
     * @return the jobRevisionNumber
     */
    public String getJobRevisionNumber()
    {
        return jobRevisionNumber;
    }


    /**
     * @return the sublotID
     */
    public String getSublotID()
    {
        return sublotID;
    }


    /**
     * @return the operatorName
     */
    public String getOperatorName()
    {
        return operatorName;
    }


    /**
     * @return the execSoftware
     */
    public String getExecSoftware()
    {
        return execSoftware;
    }


    /**
     * @return the execSoftwareVersion
     */
    public String getExecSoftwareVersion()
    {
        return execSoftwareVersion;
    }


    /**
     * @return the stepCode
     */
    public String getStepCode()
    {
        return stepCode;
    }


    /**
     * @return the temperature
     */
    public String getTemperature()
    {
        return temperature;
    }


    /**
     * @return the userText
     */
    public String getUserText()
    {
        return userText;
    }


    /**
     * @return the auxDataFile
     */
    public String getAuxDataFile()
    {
        return auxDataFile;
    }


    /**
     * @return the packageType
     */
    public String getPackageType()
    {
        return packageType;
    }


    /**
     * @return the familyID
     */
    public String getFamilyID()
    {
        return familyID;
    }


    /**
     * @return the dateCode
     */
    public String getDateCode()
    {
        return dateCode;
    }


    /**
     * @return the facilityID
     */
    public String getFacilityID()
    {
        return facilityID;
    }


    /**
     * @return the floorID
     */
    public String getFloorID()
    {
        return floorID;
    }


    /**
     * @return the fabID
     */
    public String getFabID()
    {
        return fabID;
    }


    /**
     * @return the frequency
     */
    public String getFrequency()
    {
        return frequency;
    }


    /**
     * @return the specName
     */
    public String getSpecName()
    {
        return specName;
    }


    /**
     * @return the specVersion
     */
    public String getSpecVersion()
    {
        return specVersion;
    }


    /**
     * @return the flowID
     */
    public String getFlowID()
    {
        return flowID;
    }


    /**
     * @return the setupID
     */
    public String getSetupID()
    {
        return setupID;
    }


    /**
     * @return the designRevision
     */
    public String getDesignRevision()
    {
        return designRevision;
    }


    /**
     * @return the engLotID
     */
    public String getEngLotID()
    {
        return engLotID;
    }


    /**
     * @return the romCodeID
     */
    public String getRomCodeID()
    {
        return romCodeID;
    }


    /**
     * @return the testerSerialNumber
     */
    public String getTesterSerialNumber()
    {
        return testerSerialNumber;
    }


    /**
     * @return the supervisorID
     */
    public String getSupervisorID()
    {
        return supervisorID;
    }

}
