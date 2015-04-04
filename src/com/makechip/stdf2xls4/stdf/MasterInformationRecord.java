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
*** @version $Id: MasterInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterInformationRecord extends StdfRecord
{
    private final String jobDate;
    private final String testDate;
    private final short stationNumber;
    private final String testModeCode;
    private final String lotRetestCode;
    private final String dataProtectionCode;
    private final int burnInTime;
    private final String cmdModeCode;
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
    
    /**
    *** @param p1
    *** @param p2
    **/
    public MasterInformationRecord(int sequenceNumber, int devNum, byte[] data)
    {
        super(Record_t.MIR, sequenceNumber, devNum, data);
        long d = getU4(0);
        jobDate = new Date(d * 1000L).toString();
        d = getU4(0);
        testDate = new Date(d * 1000L).toString();
        stationNumber = getU1((byte) 0);
        testModeCode = getFixedLengthString(1);
        lotRetestCode = getFixedLengthString(1);
        dataProtectionCode = getFixedLengthString(1);
        burnInTime = getU2(0);
        cmdModeCode = getFixedLengthString(1);
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
    
    public void setFileTimeStamp(long value) { fileTimeStamp = value; }
    public long getFileTimeStamp() { return(fileTimeStamp); }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(":");
        sb.append(Log.eol);
        sb.append("    job date: "); sb.append(jobDate); sb.append(Log.eol);
        sb.append("    test date: "); sb.append(testDate); sb.append(Log.eol);
        sb.append("    station number: " + stationNumber); sb.append(Log.eol);
        sb.append("    test mode code: "); sb.append(testModeCode); sb.append(Log.eol);
        sb.append("    lot retest code: "); sb.append(lotRetestCode); sb.append(Log.eol);
        sb.append("    data protection code: "); sb.append(dataProtectionCode); sb.append(Log.eol);
        sb.append("    burn-in time: " + burnInTime); sb.append(Log.eol);
        sb.append("    command mode code: "); sb.append(cmdModeCode); sb.append(Log.eol);
        sb.append("    lot ID: "); sb.append(lotID); sb.append(Log.eol);
        sb.append("    part type: "); sb.append(partType); sb.append(Log.eol);
        sb.append("    node name: "); sb.append(nodeName); sb.append(Log.eol);
        sb.append("    tester type: "); sb.append(testerType); sb.append(Log.eol);
        sb.append("    job name: "); sb.append(jobName); sb.append(Log.eol);
        sb.append("    job revision number: "); sb.append(jobRevisionNumber); sb.append(Log.eol);
        sb.append("    sublot ID: "); sb.append(sublotID); sb.append(Log.eol);
        sb.append("    operatorName: "); sb.append(operatorName); sb.append(Log.eol);
        sb.append("    exec software: "); sb.append(execSoftware); sb.append(Log.eol);
        sb.append("    exec software revision: "); sb.append(execSoftwareVersion); sb.append(Log.eol);
        sb.append("    step code: "); sb.append(stepCode); sb.append(Log.eol);
        sb.append("    temperature: "); sb.append(temperature); sb.append(Log.eol);
        sb.append("    user text: "); sb.append(userText); sb.append(Log.eol);
        sb.append("    auxilliary data file: "); sb.append(auxDataFile); sb.append(Log.eol);
        sb.append("    package type: "); sb.append(packageType); sb.append(Log.eol);
        sb.append("    family ID: "); sb.append(familyID); sb.append(Log.eol);
        sb.append("    date code: "); sb.append(dateCode); sb.append(Log.eol);
        sb.append("    facility ID: "); sb.append(facilityID); sb.append(Log.eol);
        sb.append("    floor ID: "); sb.append(floorID); sb.append(Log.eol);
        sb.append("    fab ID: "); sb.append(fabID); sb.append(Log.eol);
        sb.append("    frequency: "); sb.append(frequency); sb.append(Log.eol);
        sb.append("    spec name: "); sb.append(specName); sb.append(Log.eol);
        sb.append("    spec version: "); sb.append(specVersion); sb.append(Log.eol);
        sb.append("    flow ID: "); sb.append(flowID); sb.append(Log.eol);
        sb.append("    setup ID: "); sb.append(setupID); sb.append(Log.eol);
        sb.append("    design revision: "); sb.append(designRevision); sb.append(Log.eol);
        sb.append("    engineering lot ID: "); sb.append(engLotID); sb.append(Log.eol);
        sb.append("    ROM code ID: "); sb.append(romCodeID); sb.append(Log.eol);
        sb.append("    tester serial number: "); sb.append(testerSerialNumber); sb.append(Log.eol);
        sb.append("    supervisor ID: "); sb.append(supervisorID); sb.append(Log.eol);
        return(sb.toString());
    }

    /**
     * @return the jobDate
     */
    public String getJobDate()
    {
        return jobDate;
    }


    /**
     * @return the testDate
     */
    public String getTestDate()
    {
        return testDate;
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
        return testModeCode;
    }


    /**
     * @return the lotRetestCode
     */
    public String getLotRetestCode()
    {
        return lotRetestCode;
    }


    /**
     * @return the dataProtectionCode
     */
    public String getDataProtectionCode()
    {
        return dataProtectionCode;
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
        return cmdModeCode;
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
