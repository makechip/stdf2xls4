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


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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
     * @throws StdfException 
     * @throws IOException 
     */
    public MasterInformationRecord(
        Cpu_t cpu,
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
        String supervisorID) throws IOException, StdfException
    {
    	this(cpu, 
    		 getRecLen(lotID, partType, nodeName, testerType, jobName, jobRevisionNumber, sublotID, operatorName, 
    				   execSoftware, execSoftwareVersion, stepCode, temperature, userText, auxDataFile, packageType, 
    				   familyID, dateCode, facilityID, floorID, fabID, frequency, specName, specVersion, 
		               flowID, setupID, designRevision, engLotID, romCodeID, testerSerialNumber, supervisorID),
    		 new DataInputStream(new ByteArrayInputStream(toBytes(cpu, jobDate, testDate, stationNumber, 
    				             testModeCode, lotRetestCode, dataProtectionCode, burnInTime, cmdModeCode, lotID, 
    			                 partType, nodeName, testerType, jobName, jobRevisionNumber, sublotID, operatorName, 
    			                 execSoftware, execSoftwareVersion, stepCode, temperature, userText, auxDataFile, 
    			                 packageType, familyID, dateCode, facilityID, floorID, fabID, frequency, specName, 
    			                 specVersion, flowID, setupID, designRevision, engLotID, romCodeID, testerSerialNumber, supervisorID))));
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
    public MasterInformationRecord(Cpu_t cpu, int recLen, DataInputStream is) throws IOException, StdfException
    {
        super();
        jobDate = cpu.getU4(is);
        testDate = cpu.getU4(is);
        stationNumber = cpu.getU1(is);
        testModeCode = (char) cpu.getI1(is);
        lotRetestCode = (char) cpu.getI1(is);
        dataProtectionCode = (char) cpu.getI1(is);
        burnInTime = cpu.getU2(is);
        cmdModeCode = (char) cpu.getI1(is);
        int l = 15;
        lotID = cpu.getCN(is); l += 1 + lotID.length();
        partType = cpu.getCN(is); l += 1 + partType.length();
        nodeName = cpu.getCN(is); l += 1 + nodeName.length();
        testerType = cpu.getCN(is); l += 1 + testerType.length();
        jobName = cpu.getCN(is); l += 1 + jobName.length();
        jobRevisionNumber = getCN(cpu, recLen, l, is);
        l = updateL(l, jobRevisionNumber);
        sublotID = getCN(cpu, recLen, l, is);
        l = updateL(l, sublotID);
        operatorName = getCN(cpu, recLen, l, is);
        l = updateL(l, operatorName);
        execSoftware = getCN(cpu, recLen, l, is);
        l = updateL(l, execSoftware);
        execSoftwareVersion = getCN(cpu, recLen, l, is);
        l = updateL(l, execSoftwareVersion);
        stepCode = getCN(cpu, recLen, l, is);
        l = updateL(l, stepCode);
        temperature = getCN(cpu, recLen, l, is);
        l = updateL(l, temperature);
        userText = getCN(cpu, recLen, l, is);
        l = updateL(l, userText);
        auxDataFile = getCN(cpu, recLen, l, is);
        l = updateL(l, auxDataFile);
        packageType = getCN(cpu, recLen, l, is);
        l = updateL(l, packageType);
        familyID = getCN(cpu, recLen, l, is);
        l = updateL(l, familyID);
        dateCode = getCN(cpu, recLen, l, is);
        l = updateL(l, dateCode);
        facilityID = getCN(cpu, recLen, l, is);
        l = updateL(l, facilityID);
        floorID = getCN(cpu, recLen, l, is);
        l = updateL(l, floorID);
        fabID = getCN(cpu, recLen, l, is);
        l = updateL(l, fabID);
        frequency = getCN(cpu, recLen, l, is);
        l = updateL(l, frequency);
        specName = getCN(cpu, recLen, l, is);
        l = updateL(l, specName);
        specVersion = getCN(cpu, recLen, l, is);
        l = updateL(l, specVersion);
        flowID = getCN(cpu, recLen, l, is);
        l = updateL(l, flowID);
        setupID = getCN(cpu, recLen, l, is);
        l = updateL(l, setupID);
        designRevision = getCN(cpu, recLen, l, is);
        l = updateL(l, designRevision);
        engLotID = getCN(cpu, recLen, l, is);
        l = updateL(l, engLotID);
        romCodeID = getCN(cpu, recLen, l, is);
        l = updateL(l, romCodeID);
        testerSerialNumber = getCN(cpu, recLen, l, is);
        l = updateL(l, testerSerialNumber);
        supervisorID = getCN(cpu, recLen, l, is);
        l = updateL(l, supervisorID);
        if (l != recLen) throw new StdfException("Record length error in MasterInformationRecord.");
    }
    
    private static String getCN(Cpu_t cpu, int recLen, int l, DataInputStream is) throws IOException
    {
    	String s = null;
    	if (l < recLen) s = cpu.getCN(is);	
    	return(s);
    }
    
    private static int updateL(int l, String s)
    {
    	if (s != null) return(l + 1 + s.length());
    	return(l);
    }
    
	private static byte[] toBytes(
    	Cpu_t cpu,
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
		l.addAll(cpu.getU4Bytes(jobDate));
		l.addAll(cpu.getU4Bytes(testDate));
		l.addAll(cpu.getU1Bytes(stationNumber));
		l.addAll(cpu.getI1Bytes((byte) testModeCode));
		l.addAll(cpu.getI1Bytes((byte) lotRetestCode));
		l.addAll(cpu.getI1Bytes((byte) dataProtectionCode));
		l.addAll(cpu.getU2Bytes(burnInTime));
		l.addAll(cpu.getI1Bytes((byte) cmdModeCode));
		l.addAll(cpu.getCNBytes(lotID));
        l.addAll(cpu.getCNBytes(partType));
        l.addAll(cpu.getCNBytes(nodeName));
        l.addAll(cpu.getCNBytes(testerType));
        l.addAll(cpu.getCNBytes(jobName));
        if (jobRevisionNumber != null) l.addAll(cpu.getCNBytes(jobRevisionNumber)); else return(l.toArray());
        if (sublotID != null) l.addAll(cpu.getCNBytes(sublotID)); else return(l.toArray());
        if (operatorName != null) l.addAll(cpu.getCNBytes(operatorName)); else return(l.toArray());
        if (execSoftware != null) l.addAll(cpu.getCNBytes(execSoftware)); else return(l.toArray());
        if (execSoftwareVersion != null) l.addAll(cpu.getCNBytes(execSoftwareVersion)); else return(l.toArray());
        if (stepCode != null) l.addAll(cpu.getCNBytes(stepCode)); else return(l.toArray());
        if (temperature != null) l.addAll(cpu.getCNBytes(temperature)); else return(l.toArray());
        if (userText != null) l.addAll(cpu.getCNBytes(userText)); else return(l.toArray());
        if (auxDataFile != null) l.addAll(cpu.getCNBytes(auxDataFile)); else return(l.toArray());
        if (packageType != null) l.addAll(cpu.getCNBytes(packageType)); else return(l.toArray());
        if (familyID != null) l.addAll(cpu.getCNBytes(familyID)); else return(l.toArray());
        if (dateCode != null) l.addAll(cpu.getCNBytes(dateCode)); else return(l.toArray());
        if (facilityID != null) l.addAll(cpu.getCNBytes(facilityID)); else return(l.toArray());
        if (floorID != null) l.addAll(cpu.getCNBytes(floorID)); else return(l.toArray());
        if (fabID != null) l.addAll(cpu.getCNBytes(fabID)); else return(l.toArray());
        if (frequency != null) l.addAll(cpu.getCNBytes(frequency)); else return(l.toArray());
        if (specName != null) l.addAll(cpu.getCNBytes(specName)); else return(l.toArray());
        if (specVersion != null) l.addAll(cpu.getCNBytes(specVersion)); else return(l.toArray());
        if (flowID != null) l.addAll(cpu.getCNBytes(flowID)); else return(l.toArray());
        if (setupID != null) l.addAll(cpu.getCNBytes(setupID)); else return(l.toArray());
        if (designRevision != null) l.addAll(cpu.getCNBytes(designRevision)); else return(l.toArray());
        if (engLotID != null) l.addAll(cpu.getCNBytes(engLotID)); else return(l.toArray());
        if (romCodeID != null) l.addAll(cpu.getCNBytes(romCodeID)); else return(l.toArray());
        if (testerSerialNumber != null) l.addAll(cpu.getCNBytes(testerSerialNumber)); else return(l.toArray());
        if (supervisorID != null) l.addAll(cpu.getCNBytes(supervisorID));
        return(l.toArray());
	}

	@Override
	public byte[] getBytes(Cpu_t cpu)
	{
		byte[] b = toBytes(cpu, jobDate, testDate, stationNumber, testModeCode, lotRetestCode, 
				   dataProtectionCode, burnInTime, cmdModeCode, lotID, partType, nodeName, testerType, 
				   jobName, jobRevisionNumber, sublotID, operatorName, execSoftware, execSoftwareVersion, 
				   stepCode, temperature, userText, auxDataFile, packageType, familyID, dateCode, 
				   facilityID, floorID, fabID, frequency, specName, specVersion, flowID, setupID, 
				   designRevision, engLotID, romCodeID, testerSerialNumber, supervisorID);
		TByteArrayList l = getHeaderBytes(cpu, Record_t.MIR, b.length);
		l.addAll(b);
		return(l.toArray());
	}

	private static int getRecLen(
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
	    int l = 15;	
	    l += 1 + lotID.length();
	    l += 1 + partType.length();
	    l += 1 + nodeName.length();
	    l += 1 + testerType.length();
	    l += 1 + jobName.length();
        if (jobRevisionNumber != null) l += 1 + jobRevisionNumber.length(); else return(l);
        if (sublotID != null) l += 1 + sublotID.length(); else return(l);
        if (operatorName != null) l += 1 + operatorName.length(); else return(l);
        if (execSoftware != null) l += 1 + execSoftware.length(); else return(l);
        if (execSoftwareVersion != null) l += 1 + execSoftwareVersion.length(); else return(l);
        if (stepCode != null) l += 1 + stepCode.length(); else return(l);
        if (temperature != null) l += 1 + temperature.length(); else return(l);
        if (userText != null) l += 1 + userText.length(); else return(l);
        if (auxDataFile != null) l += 1 + auxDataFile.length(); else return(l);
        if (packageType != null) l += 1 + packageType.length(); else return(l);
        if (familyID != null) l += 1 + familyID.length(); else return(l);
        if (dateCode != null) l += 1 + dateCode.length(); else return(l);
        if (facilityID != null) l += 1 + facilityID.length(); else return(l);
        if (floorID != null) l += 1 + floorID.length(); else return(l);
        if (fabID != null) l += 1 + fabID.length(); else return(l);
        if (frequency != null) l += 1 + frequency.length(); else return(l);
        if (specName != null) l += 1 + specName.length(); else return(l);
        if (specVersion != null) l += 1 + specVersion.length(); else return(l);
        if (flowID != null) l += 1 + flowID.length(); else return(l);
        if (setupID != null) l += 1 + setupID.length(); else return(l);
        if (designRevision != null) l += 1 + designRevision.length(); else return(l);
        if (engLotID != null) l += 1 + engLotID.length(); else return(l);
        if (romCodeID != null) l += 1 + romCodeID.length(); else return(l);
        if (testerSerialNumber != null) l += 1 + testerSerialNumber.length(); else return(l);
        if (supervisorID != null) l += 1 + supervisorID.length(); else return(l);
	    return(l);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobDate ^ (jobDate >>> 32));
		result = prime * result + (int) (testDate ^ (testDate >>> 32));
		result = prime * result + stationNumber;
		result = prime * result + testModeCode;
		result = prime * result + lotRetestCode;
		result = prime * result + dataProtectionCode;
		result = prime * result + burnInTime;
		result = prime * result + cmdModeCode;
		result = prime * result + lotID.hashCode();
		result = prime * result + partType.hashCode();
		result = prime * result + nodeName.hashCode();
		result = prime * result + testerType.hashCode();
		result = prime * result + jobName.hashCode();
		result = prime * result + ((auxDataFile == null) ? 0 : auxDataFile.hashCode());
		result = prime * result + ((dateCode == null) ? 0 : dateCode.hashCode());
		result = prime * result + ((designRevision == null) ? 0 : designRevision.hashCode());
		result = prime * result + ((engLotID == null) ? 0 : engLotID.hashCode());
		result = prime * result + ((execSoftware == null) ? 0 : execSoftware.hashCode());
		result = prime * result + ((execSoftwareVersion == null) ? 0 : execSoftwareVersion.hashCode());
		result = prime * result + ((fabID == null) ? 0 : fabID.hashCode());
		result = prime * result + ((facilityID == null) ? 0 : facilityID.hashCode());
		result = prime * result + ((familyID == null) ? 0 : familyID.hashCode());
		result = prime * result + ((floorID == null) ? 0 : floorID.hashCode());
		result = prime * result + ((flowID == null) ? 0 : flowID.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((jobRevisionNumber == null) ? 0 : jobRevisionNumber.hashCode());
		result = prime * result + ((operatorName == null) ? 0 : operatorName.hashCode());
		result = prime * result + ((packageType == null) ? 0 : packageType.hashCode());
		result = prime * result + ((romCodeID == null) ? 0 : romCodeID.hashCode());
		result = prime * result + ((setupID == null) ? 0 : setupID.hashCode());
		result = prime * result + ((specName == null) ? 0 : specName.hashCode());
		result = prime * result + ((specVersion == null) ? 0 : specVersion.hashCode());
		result = prime * result + ((stepCode == null) ? 0 : stepCode.hashCode());
		result = prime * result + ((sublotID == null) ? 0 : sublotID.hashCode());
		result = prime * result + ((supervisorID == null) ? 0 : supervisorID.hashCode());
		result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
		result = prime * result + ((testerSerialNumber == null) ? 0 : testerSerialNumber.hashCode());
		result = prime * result + ((userText == null) ? 0 : userText.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof MasterInformationRecord)) return false;
		MasterInformationRecord other = (MasterInformationRecord) obj;
		if (jobDate != other.jobDate) return false;
		if (testDate != other.testDate) return false;
		if (stationNumber != other.stationNumber) return false;
		if (testModeCode != other.testModeCode) return false;
		if (lotRetestCode != other.lotRetestCode) return false;
		if (dataProtectionCode != other.dataProtectionCode) return false;
		if (burnInTime != other.burnInTime) return false;
		if (cmdModeCode != other.cmdModeCode) return false;
		if (!lotID.equals(other.lotID)) return false;
		if (!partType.equals(other.partType)) return false;
		if (!nodeName.equals(other.nodeName)) return false;
		if (!testerType.equals(other.testerType)) return false;
		if (!jobName.equals(other.jobName)) return false;
		if (auxDataFile == null)
		{
			if (other.auxDataFile != null) return false;
		} 
		else if (!auxDataFile.equals(other.auxDataFile)) return false;
		if (dateCode == null)
		{
			if (other.dateCode != null) return false;
		} 
		else if (!dateCode.equals(other.dateCode)) return false;
		if (designRevision == null)
		{
			if (other.designRevision != null) return false;
		} 
		else if (!designRevision.equals(other.designRevision)) return false;
		if (engLotID == null)
		{
			if (other.engLotID != null) return false;
		} 
		else if (!engLotID.equals(other.engLotID)) return false;
		if (execSoftware == null)
		{
			if (other.execSoftware != null) return false;
		} 
		else if (!execSoftware.equals(other.execSoftware)) return false;
		if (execSoftwareVersion == null)
		{
			if (other.execSoftwareVersion != null) return false;
		} 
		else if (!execSoftwareVersion.equals(other.execSoftwareVersion)) return false;
		if (fabID == null)
		{
			if (other.fabID != null) return false;
		} 
		else if (!fabID.equals(other.fabID)) return false;
		if (facilityID == null)
		{
			if (other.facilityID != null) return false;
		} 
		else if (!facilityID.equals(other.facilityID)) return false;
		if (familyID == null)
		{
			if (other.familyID != null) return false;
		} 
		else if (!familyID.equals(other.familyID)) return false;
		if (floorID == null)
		{
			if (other.floorID != null) return false;
		} 
		else if (!floorID.equals(other.floorID)) return false;
		if (flowID == null)
		{
			if (other.flowID != null) return false;
		} 
		else if (!flowID.equals(other.flowID)) return false;
		if (frequency == null)
		{
			if (other.frequency != null) return false;
		} 
		else if (!frequency.equals(other.frequency)) return false;
		if (jobRevisionNumber == null)
		{
			if (other.jobRevisionNumber != null) return false;
		} 
		else if (!jobRevisionNumber.equals(other.jobRevisionNumber)) return false;
		if (operatorName == null)
		{
			if (other.operatorName != null) return false;
		} 
		else if (!operatorName.equals(other.operatorName)) return false;
		if (packageType == null)
		{
			if (other.packageType != null) return false;
		} 
		else if (!packageType.equals(other.packageType)) return false;
		if (romCodeID == null)
		{
			if (other.romCodeID != null) return false;
		} 
		else if (!romCodeID.equals(other.romCodeID)) return false;
		if (setupID == null)
		{
			if (other.setupID != null) return false;
		} 
		else if (!setupID.equals(other.setupID)) return false;
		if (specName == null)
		{
			if (other.specName != null) return false;
		} 
		else if (!specName.equals(other.specName)) return false;
		if (specVersion == null)
		{
			if (other.specVersion != null) return false;
		} 
		else if (!specVersion.equals(other.specVersion)) return false;
		if (stepCode == null)
		{
			if (other.stepCode != null) return false;
		} 
		else if (!stepCode.equals(other.stepCode)) return false;
		if (sublotID == null)
		{
			if (other.sublotID != null) return false;
		} 
		else if (!sublotID.equals(other.sublotID)) return false;
		if (supervisorID == null)
		{
			if (other.supervisorID != null) return false;
		} 
		else if (!supervisorID.equals(other.supervisorID)) return false;
		if (temperature == null)
		{
			if (other.temperature != null) return false;
		} 
		else if (!temperature.equals(other.temperature)) return false;
		if (testerSerialNumber == null)
		{
			if (other.testerSerialNumber != null) return false;
		} 
		else if (!testerSerialNumber.equals(other.testerSerialNumber)) return false;
		if (userText == null)
		{
			if (other.userText != null) return false;
		} 
		else if (!userText.equals(other.userText)) return false;
		return true;
	}

}
