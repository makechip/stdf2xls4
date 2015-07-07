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

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
*** @author eric
*** @version $Id: MasterInformationRecord.java 258 2008-10-22 01:22:44Z ericw $
**/
public class MasterInformationRecord extends StdfRecord
{
	/**
	 *  This is the SETUP_T field of the MasterInformationRecord.
	 */
    public final long jobDate;
	/**
	 *  This is the START_T field of the MasterInformationRecord.
	 */
    public final long testDate;
	/**
	 *  This is the STAT_NUM field of the MasterInformationRecord.
	 */
    public final short stationNumber;
	/**
	 *  This is the MODE_COD field of the MasterInformationRecord.
	 */
    public final char testModeCode;
	/**
	 *  This is the RTST_COD field of the MasterInformationRecord.
	 */
    public final char lotRetestCode;
	/**
	 *  This is the PROT_COD field of the MasterInformationRecord.
	 */
    public final char dataProtectionCode;
	/**
	 *  This is the BURN_TIM field of the MasterInformationRecord.
	 */
    public final int burnInTime;
	/**
	 *  This is the CMOD_COD field of the MasterInformationRecord.
	 */
    public final char cmdModeCode;
	/**
	 *  This is the LOT_ID field of the MasterInformationRecord.
	 */
    public final String lotID;
	/**
	 *  This is the PART_TYP field of the MasterInformationRecord.
	 */
    public final String partType;
	/**
	 *  This is the NODE_NAM field of the MasterInformationRecord.
	 */
    public final String nodeName;
	/**
	 *  This is the TSTR_TYP field of the MasterInformationRecord.
	 */
    public final String testerType;
	/**
	 *  This is the JOB_NAM field of the MasterInformationRecord.
	 */
    public final String jobName;
	/**
	 *  This is the JOB_REV field of the MasterInformationRecord.
	 */
    public final String jobRevisionNumber;
	/**
	 *  This is the SBLOT_ID field of the MasterInformationRecord.
	 */
    public final String sublotID;
	/**
	 *  This is the OPER_NAM field of the MasterInformationRecord.
	 */
    public final String operatorName;
	/**
	 *  This is the EXEC_TYP field of the MasterInformationRecord.
	 */
    public final String execSoftware;
	/**
	 *  This is the EXEC_VER field of the MasterInformationRecord.
	 */
    public final String execSoftwareVersion;
	/**
	 *  This is the TEST_COD field of the MasterInformationRecord.
	 */
    public final String stepCode;
	/**
	 *  This is the TST_TEMP field of the MasterInformationRecord.
	 */
    public final String temperature;
	/**
	 *  This is the USER_TXT field of the MasterInformationRecord.
	 */
    public final String userText;
	/**
	 *  This is the AUX_FILE field of the MasterInformationRecord.
	 */
    public final String auxDataFile;
	/**
	 *  This is the PKG_TYP field of the MasterInformationRecord.
	 */
    public final String packageType;
	/**
	 *  This is the FAMILY_ID field of the MasterInformationRecord.
	 */
    public final String familyID;
	/**
	 *  This is the DATE_COD field of the MasterInformationRecord.
	 */
    public final String dateCode;
	/**
	 *  This is the FACIL_ID field of the MasterInformationRecord.
	 */
    public final String facilityID;
	/**
	 *  This is the FLOOR_ID field of the MasterInformationRecord.
	 */
    public final String floorID;
	/**
	 *  This is the PROC_ID field of the MasterInformationRecord.
	 */
    public final String fabID;
	/**
	 *  This is the OPER_FRQ field of the MasterInformationRecord.
	 */
    public final String frequency;
	/**
	 *  This is the SPEC_NAME field of the MasterInformationRecord.
	 */
    public final String specName;
	/**
	 *  This is the SPEC_VER field of the MasterInformationRecord.
	 */
    public final String specVersion;
	/**
	 *  This is the FLOW_ID field of the MasterInformationRecord.
	 */
    public final String flowID;
	/**
	 *  This is the SETUP_ID field of the MasterInformationRecord.
	 */
    public final String setupID;
	/**
	 *  This is the DSGN_REV field of the MasterInformationRecord.
	 */
    public final String designRevision;
	/**
	 *  This is the ENG_ID field of the MasterInformationRecord.
	 */
    public final String engLotID;
	/**
	 *  This is the ROM_COD field of the MasterInformationRecord.
	 */
    public final String romCodeID;
	/**
	 *  This is the SERL_NUM field of the MasterInformationRecord.
	 */
    public final String testerSerialNumber;
	/**
	 *  This is the SUPR_NAM field of the MasterInformationRecord.
	 */
    public final String supervisorID;
	/**
	 *  This is not an STDF field.  It is used for tracking file timestamps
	 *  when one STDF file per device is used with timestamped filenames.
	 */
    private long timeStamp; // not an STDF value; for filename timestamp tracking 
    
    /**
     * This constructor is used to generate binary Stream data.  It can be used to convert
     * the field values back into binary stream data.
     * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
     * @param dvd The DefaultValueDatabase is used to access the CPU type.
     * @param jobDate The SETUP_T field.
     * @param testDate The START_T field.
     * @param stationNumber The STAT_NUM field.
     * @param testModeCode The MODE_COD field.
     * @param lotRetestCode The RTST_COD field.
     * @param dataProtectionCode The PROT_COD field.
     * @param burnInTime The BURN_TIM field.
     * @param cmdModeCode The CMOD_COD field.
     * @param lotID The LOT_ID field.
     * @param partType The PART_TYP field.
     * @param nodeName The NODE_NAM field.
     * @param testerType The TSTR_TYP field.
     * @param jobName The JOB_NAM field.
     * @param jobRevisionNumber The JOB_REV field.
     * @param sublotID The SBLOT_ID field.
     * @param operatorName The OPER_NAM field.
     * @param execSoftware The EXEC_TYP field.
     * @param execSoftwareVersion The EXEC_VER field.
     * @param stepCode The TEST_COD field.
     * @param temperature The TST_TEMP field.
     * @param userText The USER_TXT field.
     * @param auxDataFile The AUX_FILE field.
     * @param packageType The PKG_TYP field.
     * @param familyID The FAMILY_ID field.
     * @param dateCode The DATE_COD field.
     * @param facilityID The FACIL_ID field.
     * @param floorID The FLOOR_ID field.
     * @param fabID The PROC_ID field.
     * @param frequency The OPER_FRQ field.
     * @param specName The SPEC_NAM field.
     * @param specVersion The SPEC_VER field.
     * @param flowID The FLOW_ID field.
     * @param setupID The SETUP_ID field.
     * @param designRevision The DSGN_REV field.
     * @param engLotID The ENG_ID field.
     * @param romCodeID The ROM_COD field
     * @param testerSerialNumber The SERL_NUM field.
     * @param supervisorID The SUPR_NAM field.
     * @param timeStamp The time stamp field.  Not used unless times-stamped files are being tracked.
     */
    public MasterInformationRecord(
    	TestIdDatabase tdb,
    	DefaultValueDatabase dvd,
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
    	this(tdb, dvd, toBytes(dvd.getCpuType(), jobDate, testDate, stationNumber, testModeCode, 
    			               lotRetestCode, dataProtectionCode, burnInTime, cmdModeCode, lotID, 
    			               partType, nodeName, testerType, jobName, jobRevisionNumber,
		                       sublotID, operatorName, execSoftware, execSoftwareVersion, 
		                       stepCode, temperature, userText, auxDataFile, packageType, 
		                       familyID, dateCode, facilityID, floorID, fabID, frequency, 
		                       specName, specVersion, flowID, setupID, designRevision, 
		                       engLotID, romCodeID, testerSerialNumber, supervisorID));
    	this.timeStamp = timeStamp;
    }
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the MasterInformationRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
    public MasterInformationRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MIR, dvd.getCpuType(), data);
        timeStamp = dvd.timeStamp;
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
        if (testerType.equals("Fusion_CX") || testerType.equals("CTX")) dvd.setFusionCx();
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
		bytes = toBytes(cpuType, jobDate, testDate, stationNumber, testModeCode, lotRetestCode, dataProtectionCode,
		                burnInTime, cmdModeCode, lotID, partType, nodeName, testerType, jobName, jobRevisionNumber,
		                sublotID, operatorName, execSoftware, execSoftwareVersion, stepCode, temperature, userText,
		                auxDataFile, packageType, familyID, dateCode, facilityID, floorID, fabID, frequency, specName,
		                specVersion, flowID, setupID, designRevision, engLotID, romCodeID, testerSerialNumber, supervisorID);
	}
	
	private static byte[] toBytes(
    	Cpu_t cpuType,
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
        String supervisorID)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU4Bytes(jobDate));
		l.addAll(cpuType.getU4Bytes(testDate));
		l.addAll(getU1Bytes(stationNumber));
		l.addAll(getFixedLengthStringBytes("" + testModeCode));
		l.addAll(getFixedLengthStringBytes("" + lotRetestCode));
		l.addAll(getFixedLengthStringBytes("" + dataProtectionCode));
		l.addAll(cpuType.getU2Bytes(burnInTime));
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
        return(l.toArray());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MasterInformationRecord [jobDate=").append(jobDate);
		builder.append(", testDate=").append(testDate);
		builder.append(", stationNumber=").append(stationNumber);
		builder.append(", testModeCode=").append(testModeCode);
		builder.append(", lotRetestCode=").append(lotRetestCode);
		builder.append(", dataProtectionCode=").append(dataProtectionCode);
		builder.append(", burnInTime=").append(burnInTime);
		builder.append(", cmdModeCode=").append(cmdModeCode);
		builder.append(", lotID=").append(lotID);
		builder.append(", partType=").append(partType);
		builder.append(", nodeName=").append(nodeName);
		builder.append(", testerType=").append(testerType);
		builder.append(", jobName=").append(jobName);
		builder.append(", jobRevisionNumber=").append(jobRevisionNumber);
		builder.append(", sublotID=").append(sublotID);
		builder.append(", operatorName=").append(operatorName);
		builder.append(", execSoftware=").append(execSoftware);
		builder.append(", execSoftwareVersion=").append(execSoftwareVersion);
		builder.append(", stepCode=").append(stepCode);
		builder.append(", temperature=").append(temperature);
		builder.append(", userText=").append(userText);
		builder.append(", auxDataFile=").append(auxDataFile);
		builder.append(", packageType=").append(packageType);
		builder.append(", familyID=").append(familyID);
		builder.append(", dateCode=").append(dateCode);
		builder.append(", facilityID=").append(facilityID);
		builder.append(", floorID=").append(floorID);
		builder.append(", fabID=").append(fabID);
		builder.append(", frequency=").append(frequency);
		builder.append(", specName=").append(specName);
		builder.append(", specVersion=").append(specVersion);
		builder.append(", flowID=").append(flowID);
		builder.append(", setupID=").append(setupID);
		builder.append(", designRevision=").append(designRevision);
		builder.append(", engLotID=").append(engLotID);
		builder.append(", romCodeID=").append(romCodeID);
		builder.append(", testerSerialNumber=").append(testerSerialNumber);
		builder.append(", supervisorID=").append(supervisorID);
		builder.append(", timeStamp=").append(timeStamp);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + auxDataFile.hashCode();
		result = prime * result + burnInTime;
		result = prime * result + cmdModeCode;
		result = prime * result + dataProtectionCode;
		result = prime * result + dateCode.hashCode();
		result = prime * result + designRevision.hashCode();
		result = prime * result + engLotID.hashCode();
		result = prime * result + execSoftware.hashCode();
		result = prime * result + execSoftwareVersion.hashCode();
		result = prime * result + fabID.hashCode();
		result = prime * result + facilityID.hashCode();
		result = prime * result + familyID.hashCode();
		result = prime * result + floorID.hashCode();
		result = prime * result + flowID.hashCode();
		result = prime * result + frequency.hashCode();
		result = prime * result + (int) (jobDate ^ (jobDate >>> 32));
		result = prime * result + jobName.hashCode();
		result = prime * result + jobRevisionNumber.hashCode();
		result = prime * result + lotID.hashCode();
		result = prime * result + lotRetestCode;
		result = prime * result + nodeName.hashCode();
		result = prime * result + operatorName.hashCode();
		result = prime * result + packageType.hashCode();
		result = prime * result + partType.hashCode();
		result = prime * result + romCodeID.hashCode();
		result = prime * result + setupID.hashCode();
		result = prime * result + specName.hashCode();
		result = prime * result + specVersion.hashCode();
		result = prime * result + stationNumber;
		result = prime * result + stepCode.hashCode();
		result = prime * result + sublotID.hashCode();
		result = prime * result + supervisorID.hashCode();
		result = prime * result + temperature.hashCode();
		result = prime * result + (int) (testDate ^ (testDate >>> 32));
		result = prime * result + testModeCode;
		result = prime * result + testerSerialNumber.hashCode();
		result = prime * result + testerType.hashCode();
		result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
		result = prime * result + userText.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof MasterInformationRecord)) return false;
		MasterInformationRecord other = (MasterInformationRecord) obj;
		if (!auxDataFile.equals(other.auxDataFile)) return false;
		if (burnInTime != other.burnInTime) return false;
		if (cmdModeCode != other.cmdModeCode) return false;
		if (dataProtectionCode != other.dataProtectionCode) return false;
		if (!dateCode.equals(other.dateCode)) return false;
		if (!designRevision.equals(other.designRevision)) return false;
		if (!engLotID.equals(other.engLotID)) return false;
		if (!execSoftware.equals(other.execSoftware)) return false;
		if (!execSoftwareVersion.equals(other.execSoftwareVersion)) return false;
		if (!fabID.equals(other.fabID)) return false;
		if (!facilityID.equals(other.facilityID)) return false;
		if (!familyID.equals(other.familyID)) return false;
		if (!floorID.equals(other.floorID)) return false;
		if (!flowID.equals(other.flowID)) return false;
		if (!frequency.equals(other.frequency)) return false;
		if (jobDate != other.jobDate) return false;
		if (!jobName.equals(other.jobName)) return false;
		if (!jobRevisionNumber.equals(other.jobRevisionNumber)) return false;
		if (!lotID.equals(other.lotID)) return false;
		if (lotRetestCode != other.lotRetestCode) return false;
		if (!nodeName.equals(other.nodeName)) return false;
		if (!operatorName.equals(other.operatorName)) return false;
		if (!packageType.equals(other.packageType)) return false;
		if (!partType.equals(other.partType)) return false;
		if (!romCodeID.equals(other.romCodeID)) return false;
		if (!setupID.equals(other.setupID)) return false;
		if (!specName.equals(other.specName)) return false;
		if (!specVersion.equals(other.specVersion)) return false;
		if (stationNumber != other.stationNumber) return false;
		if (!stepCode.equals(other.stepCode)) return false;
		if (!sublotID.equals(other.sublotID)) return false;
		if (!supervisorID.equals(other.supervisorID)) return false;
		if (!temperature.equals(other.temperature)) return false;
		if (testDate != other.testDate) return false;
		if (testModeCode != other.testModeCode) return false;
		if (!testerSerialNumber.equals(other.testerSerialNumber)) return false;
		if (!testerType.equals(other.testerType)) return false;
		if (timeStamp != other.timeStamp) return false;
		if (!userText.equals(other.userText)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

	/**
	 * Get the timestamp that was on the STDF file.  Note than time-stamped
	 * files only work when there is one device per STDF file, and the
	 * timestamp is in the correct format.  See the stdf2xls user manual
	 * for the timestamp format.
	 * @return The timestamp for the STDF file that this record came from.
	 */
	public long getTimeStamp() { return(timeStamp); }

}
