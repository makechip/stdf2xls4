package com.makechip.stdf2xls4.stdf;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;
import com.makechip.util.Log;

/**
 * @author eric
 *
 */
public class StdfTest1
{
	public static final Cpu_t cpu = Cpu_t.PC;
    static StdfWriter stdf;
    static List<StdfRecord> list;
    static StdfReader rdr;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		FileAttributesRecord far = new FileAttributesRecord(cpu, 4);
		List<AuditTrailRecord> atrs = new ArrayList<AuditTrailRecord>();
		atrs.add(new AuditTrailRecord(cpu, 100000000L, "cmdline"));
		MasterInformationRecord mir = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID",
			"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
			"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
			"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
			"supervisorID");
		RetestDataRecord srdr = new RetestDataRecord(cpu, new int[] { 1, 2, 3, 4 });
		List<SiteDescriptionRecord> sdrs = new ArrayList<SiteDescriptionRecord>();
		sdrs.add(new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 },
			"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
			"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
			"laserType", "laserID", "equipType", "equipID"));
		stdf = new StdfWriter(cpu, far, atrs, mir, srdr, sdrs);
		stdf.add(new PinMapRecord(cpu, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(cpu, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(cpu, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(cpu, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
		stdf.add(new BeginProgramSectionRecord(cpu, "beginProgramSectionRecord"));
		stdf.add(new DatalogTextRecord(cpu, "datalogTextRecord"));
		stdf.add(new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 0,
			(byte) 0, 1234L, 111L, 222L, 55L, 4, 5, (short) 6, new int[] { 1, 2, 3, 4 },
			new byte[] { (byte) 0x03, (byte) 0x0C, (byte) 1, (byte) 0x08 }, new int[] { 3, 4, 5, 6 },
			new byte[] { (byte) 0x0C, (byte) 0x03, (byte) 0x08, (byte) 1 }, 32, new byte[] { (byte) 3, (byte) 2, (byte) 1, (byte) 0 },
			"vecName", "timeSetName", "vecOpCode", "label", "alarmName", "progTxt", "rsltTxt", (short) 5,  24,
			new byte[] { (byte) 6, (byte) 7, (byte) 8 }));
		GenericDataRecord.Data d1 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I4, 0), new Integer(33));
		GenericDataRecord.Data d2 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R8, 0), new Double(44.0));
		List<GenericDataRecord.Data> lgd = new ArrayList<GenericDataRecord.Data>(); 
		lgd.add(d1);
		lgd.add(d2);
		stdf.add(new GenericDataRecord(lgd));
		stdf.add(new HardwareBinRecord(cpu, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
		stdf.add(new MasterResultsRecord(cpu, 1000L, 'C', "lotDesc", "execDesc"));
		stdf.add(new MultipleResultParametricRecord(cpu, 22L, (short) 1, (short) 0, (byte) 0,
			(byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
		stdf.add(new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, (byte) 0,
			(byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
			(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
		stdf.add(new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
		stdf.add(new PartInformationRecord(cpu, (short) 1, (short) 0));
		stdf.add(new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
		stdf.add(new PinGroupRecord(cpu, 1, "group1", new int[] { 1, 2, }));
		stdf.add(new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
			new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
		stdf.add(new SoftwareBinRecord(cpu, (short) 1, (short) 0, 5, 45, 'F', "binName"));
	    stdf.add(new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	    stdf.add(new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	    stdf.add(new WaferInformationRecord(cpu, (short) 1, (short) 0, 1000L, "waferID"));
		stdf.add(new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
		stdf.add(new EndProgramSectionRecord(cpu));
		stdf.add(new FunctionalTestRecord(cpu, 3L, (short) 2, (short) 1, (byte) 0, null, null, null, null,
				                          null, null, null, null, null, null, null, null, null, null,
				                          null, null, null, null, null, null, null, null, null, null));
		GenericDataRecord.Data d3 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U1, 0), new Short((short)33));
		GenericDataRecord.Data d4 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U2, 0), new Integer(33));
		GenericDataRecord.Data d5 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U4, 0), new Long(33L));
		GenericDataRecord.Data d6 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I1, 0), new Byte((byte) 33));
		GenericDataRecord.Data d7 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I2, 0), new Short((short) 33));
		GenericDataRecord.Data d8 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R4, 0), new Float(33.0f));
		GenericDataRecord.Data d9 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.CN, 0), "string");
		List<GenericDataRecord.Data> lgd2 = new ArrayList<GenericDataRecord.Data>(); 
		lgd2.add(d3);
		lgd2.add(d4);
		lgd2.add(d5);
		lgd2.add(d6);
		lgd2.add(d7);
		lgd2.add(d8);
		lgd2.add(d9);
		stdf.add(new GenericDataRecord(lgd2));
		stdf.add(new DatalogTextRecord(cpu, "TEXT_DATA : S/N : 30"));
		Path p = FileSystems.getDefault().getPath("x.stdf");
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(p.toFile())))
		{
		    stdf.write(os);
		    os.close();
		}
		catch (Exception e) { Log.msg(e.getMessage()); }
		rdr = new StdfReader();
		byte[] b = stdf.getBytes();
		Log.msg("b.length = " + b.length + " b[2] = " + b[2] + " b[3] = " + b[3]);
		try (DataInputStream is = new DataInputStream(new ByteArrayInputStream(stdf.getBytes())))
		{
		    rdr.read(is);
		}
		list = rdr.getRecords();
		Log.msg("list.size(); = " + list.size());
        //Files.delete(p);
	}
	
	@Test
	public void testA()
	{
		assertEquals(31, list.size());
		String s = stdf.toString();
	    assertTrue(s.contains("StdfWriter ["));	
	    assertTrue(s.contains("far="));	
	    assertTrue(s.contains("atrs="));	
	    assertTrue(s.contains("mir="));	
	    assertTrue(s.contains("rdr="));	
	    assertTrue(s.contains("sdrs="));	
	    assertTrue(s.contains("records="));	
	}

    @Test
    public void testB() throws IOException, StdfException
    {
    	//Log.msg("stack = " + stack);
    	StdfRecord r = list.get(0);
    	assertTrue(r instanceof FileAttributesRecord);
	    FileAttributesRecord far = (FileAttributesRecord) r;
	    assertEquals(Cpu_t.PC, far.cpuType);
	    assertEquals(4, far.stdfVersion);
	    FileAttributesRecord far1 = new FileAttributesRecord(Cpu_t.VAX, 4);
	    assertFalse(far.equals(far1));
	    FileAttributesRecord far2 = new FileAttributesRecord(Cpu_t.PC, 4);
	    assertTrue(far.equals(far2));
	    assertTrue(far.equals(far));
	    assertFalse(far.equals("A"));
	    assertEquals(far.hashCode(), far2.hashCode());
	    FileAttributesRecord far3 = new FileAttributesRecord(Cpu_t.PC, 5);
	    assertFalse(far.equals(far3));
    }
    
	// atrs.add(new AuditTrailRecord(snum++, dnum, 100000000L, "cmdline"));
	@Test
	public void testC() throws IOException, StdfException
	{
        StdfRecord r = list.get(1);
        assertTrue(r instanceof AuditTrailRecord);
        AuditTrailRecord atr = (AuditTrailRecord) r;
        assertEquals(100000000L, atr.date);
        assertEquals("cmdline", atr.cmdLine);
		AuditTrailRecord atr1 = new AuditTrailRecord(Cpu_t.PC, 100000000L, "cmdline");
		AuditTrailRecord atr2 = new AuditTrailRecord(Cpu_t.PC, 100000000L, "xmdline");
		AuditTrailRecord atr3 = new AuditTrailRecord(Cpu_t.PC, 100000001L, "cmdline");
		assertEquals(atr.hashCode(), atr1.hashCode());
		assertTrue(atr.equals(atr1));
		assertFalse(atr.equals(atr2));
		assertFalse(atr.equals(null));
		assertFalse(atr.equals(atr3));
		assertFalse(atr.equals("x"));
		assertTrue(atr.equals(atr));
	}

	//MasterInformationRecord mir = new MasterInformationRecord(snum++, dnum, 1000L, 2000L, (short) 1,
	//	'A', 'B', 'C', 100, 'D', "lotID",
	//	"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
	//	"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
	//	"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
	//	"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
	//	"supervisorID", 1234L);
	@Test
	public void testD() throws IOException, StdfException
	{
		StdfRecord r = list.get(2);
		assertTrue(r instanceof MasterInformationRecord);
		MasterInformationRecord mir = (MasterInformationRecord) r;
		assertEquals(1000L, mir.jobDate);
		assertEquals(2000L, mir.testDate);
		assertEquals((short) 1, mir.stationNumber);
		assertEquals('A', mir.testModeCode);
		assertEquals('B', mir.lotRetestCode);
		assertEquals('C', mir.dataProtectionCode);
		assertEquals(100, mir.burnInTime);
		assertEquals('D', mir.cmdModeCode);
		assertEquals("lotID", mir.lotID);
		assertEquals("partType", mir.partType);
		assertEquals("nodeName", mir.nodeName);
		assertEquals("testerType", mir.testerType);
		assertEquals("jobName", mir.jobName);
		assertEquals("jobRevisionNumber", mir.jobRevisionNumber);
		assertEquals("sublotID", mir.sublotID);
		assertEquals("operatorName", mir.operatorName);
		assertEquals("execSoftware", mir.execSoftware);
		assertEquals("execSoftwareVersion", mir.execSoftwareVersion);
		assertEquals("stepCode", mir.stepCode);
		assertEquals("temperature", mir.temperature);
		assertEquals("userText", mir.userText);
		assertEquals("auxDataFile", mir.auxDataFile);
		assertEquals("packageType", mir.packageType);
		assertEquals("familyID", mir.familyID);
		assertEquals("dateCode", mir.dateCode);
		assertEquals("facilityID", mir.facilityID);
		assertEquals("floorID", mir.floorID);
		assertEquals("fabID", mir.fabID);
		assertEquals("frequency", mir.frequency);
		assertEquals("specName", mir.specName);
		assertEquals("specVersion", mir.specVersion);
		assertEquals("flowID", mir.flowID);
		assertEquals("setupID", mir.setupID);
		assertEquals("designRevision", mir.designRevision);
		assertEquals("engLotID", mir.engLotID);
		assertEquals("romCodeID", mir.romCodeID);
		assertEquals("testerSerialNumber", mir.testerSerialNumber);
		assertEquals("supervisorID", mir.supervisorID);
		
		MasterInformationRecord mir1 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir2 = new MasterInformationRecord(cpu, 1001L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir3 = new MasterInformationRecord(cpu, 1000L, 2001L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir4 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 0,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir5 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'a', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir6 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'b', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir7 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'b', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir8 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 101, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mir9 = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'r', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mira = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "rotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirb = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "rartType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirc = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "rodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mird = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "resterType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mire = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "robName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirf = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "robRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirg = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"rublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirh = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "rperatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord miri = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "rxecSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirj = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "rxecSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirk = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "rtepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirl = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "remperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirm = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "rserText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirn = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"ruxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord miro = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "rackageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirp = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "ramilyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirq = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "rateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirr = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "racilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirs = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "rloorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirt = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","rabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord miru = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "rrequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirv = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "rpecName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirw = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"rpecVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirx = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "rlowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord miry = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "retupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirz = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "resignRevision", "engLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirA = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "rngLotID", "romCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirB = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "xomCodeID", "testerSerialNumber", "supervisorID");
		MasterInformationRecord mirC = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "resterSerialNumber", "supervisorID");
		MasterInformationRecord mirD = new MasterInformationRecord(cpu, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID", "partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  
			"sublotID", "operatorName", "execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", 
			"auxDataFile", "packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber", "rupervisorID");
		assertEquals(mir1.hashCode(), mir.hashCode());
		assertTrue(mir.equals(mir1));
		assertTrue(mir.equals(mir));
		assertFalse(mir.equals(null));
		assertFalse(mir.equals("A"));
		assertFalse(mir1.equals(mir2));
		assertFalse(mir1.equals(mir3));
		assertFalse(mir1.equals(mir4));
		assertFalse(mir1.equals(mir5));
		assertFalse(mir1.equals(mir6));
		assertFalse(mir1.equals(mir7));
		assertFalse(mir1.equals(mir8));
		assertFalse(mir1.equals(mir9));
		assertFalse(mir1.equals(mira));
		assertFalse(mir1.equals(mirb));
		assertFalse(mir1.equals(mirc));
		assertFalse(mir1.equals(mird));
		assertFalse(mir1.equals(mire));
		assertFalse(mir1.equals(mirf));
		assertFalse(mir1.equals(mirg));
		assertFalse(mir1.equals(mirh));
		assertFalse(mir1.equals(miri));
		assertFalse(mir1.equals(mirj));
		assertFalse(mir1.equals(mirk));
		assertFalse(mir1.equals(mirl));
		assertFalse(mir1.equals(mirm));
		assertFalse(mir1.equals(mirn));
		assertFalse(mir1.equals(miro));
		assertFalse(mir1.equals(mirp));
		assertFalse(mir1.equals(mirq));
		assertFalse(mir1.equals(mirr));
		assertFalse(mir1.equals(mirs));
		assertFalse(mir1.equals(mirt));
		assertFalse(mir1.equals(miru));
		assertFalse(mir1.equals(mirv));
		assertFalse(mir1.equals(mirw));
		assertFalse(mir1.equals(mirx));
		assertFalse(mir1.equals(miry));
		assertFalse(mir1.equals(mirz));
		assertFalse(mir1.equals(mirA));
		assertFalse(mir1.equals(mirB));
		assertFalse(mir1.equals(mirC));
		assertFalse(mir1.equals(mirD));
	}
	
	// RetestDataRecord rdr = new RetestDataRecord(new int[] { 1, 2, 3, 4 });
	@Test
	public void testE() throws IOException, StdfException
	{
		StdfRecord r = list.get(3);
		assertTrue(r instanceof RetestDataRecord);
		RetestDataRecord rdr = (RetestDataRecord) r;
		int[] bins = rdr.getRetestBins();
		assertEquals(1, bins[0]);
		assertEquals(2, bins[1]);
		assertEquals(3, bins[2]);
		assertEquals(4, bins[3]);
		RetestDataRecord rdr1 = new RetestDataRecord(cpu, new int[] { 1, 2, 3, 4 });
		RetestDataRecord rdr2 = new RetestDataRecord(cpu, new int[] { 0, 2, 3, 4 });
	    assertTrue(rdr.equals(rdr));	
	    assertTrue(rdr.equals(rdr1));	
	    assertEquals(rdr.hashCode(), rdr1.hashCode());
	    assertFalse(rdr.equals(null));
	    assertFalse(rdr.equals("A"));
	    assertFalse(rdr1.equals(rdr2));
	    String s = rdr.toString();
	    assertTrue(s.contains("RetestDataRecord"));
	    assertTrue(s.contains("retestBins ="));
	}
	
	//sdrs.add(new SiteDescriptionRecord((short) 1, (short) 2, (short) 1, new int[] { 0 },
	//	"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
	//	"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
	//	"laserType", "laserID", "equipType", "equipID"));
	@Test
	public void testF() throws IOException, StdfException
	{
		StdfRecord r = list.get(4);
		assertTrue(r instanceof SiteDescriptionRecord);
		SiteDescriptionRecord sdr = (SiteDescriptionRecord) r;
		assertEquals(1, sdr.headNumber);
		assertEquals(2, sdr.siteGroupNumber);
		assertEquals(0, sdr.getSiteNumbers()[0]);
		assertEquals("handlerType", sdr.handlerType);
		assertEquals("handlerID", sdr.handlerID);
		assertEquals("probeCardType", sdr.probeCardType);
		assertEquals("probeCardID", sdr.probeCardID);
		assertEquals("loadboardType", sdr.loadBoardType);
		assertEquals("loadboardID", sdr.loadBoardID);
		assertEquals("dibBoardType", sdr.dibBoardType);
		assertEquals("dibBoardID", sdr.dibBoardID);
		assertEquals("ifaceCableType", sdr.ifaceCableType);
		assertEquals("ifaceCableID", sdr.ifaceCableID);
		assertEquals("contactorType", sdr.contactorType);
		assertEquals("contactorID", sdr.contactorID);
		assertEquals("laserType", sdr.laserType);
		assertEquals("laserID", sdr.laserID);
		assertEquals("equipType", sdr.equipType);
		assertEquals("equipID", sdr.equipID);
		SiteDescriptionRecord sdr1 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr2 = new SiteDescriptionRecord(cpu, (short) 0, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr3 = new SiteDescriptionRecord(cpu, (short) 1, (short) 0, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr4 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 1 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr5 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "xandlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr6 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "xandlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr7 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "xrobeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr8 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "xrobeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdr9 = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "xoadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdra = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "xoadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrb = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "xibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrc = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "xibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrd = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "xfaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdre = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "xfaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrf = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "xontactorType", "contactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrg = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "xontactorID", "laserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdrh = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "xaserType", "laserID", "equipType", "equipID");
		SiteDescriptionRecord sdri = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "xaserID", "equipType", "equipID");
		SiteDescriptionRecord sdrj = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "xquipType", "equipID");
		SiteDescriptionRecord sdrk = new SiteDescriptionRecord(cpu, (short) 1, (short) 2, new short[] { 0 }, "handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID", "dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID", "laserType", "laserID", "equipType", "xquipID");
		assertTrue(sdr.equals(sdr));
		assertTrue(sdr.equals(sdr1));
		assertEquals(sdr.hashCode(), sdr1.hashCode());
		assertFalse(sdr.equals(null));
		assertFalse(sdr.equals("A"));
		assertFalse(sdr1.equals(sdr2));
		assertFalse(sdr1.equals(sdr3));
		assertFalse(sdr1.equals(sdr4));
		assertFalse(sdr1.equals(sdr5));
		assertFalse(sdr1.equals(sdr6));
		assertFalse(sdr1.equals(sdr7));
		assertFalse(sdr1.equals(sdr8));
		assertFalse(sdr1.equals(sdr9));
		assertFalse(sdr1.equals(sdra));
		assertFalse(sdr1.equals(sdrb));
		assertFalse(sdr1.equals(sdrc));
		assertFalse(sdr1.equals(sdrd));
		assertFalse(sdr1.equals(sdre));
		assertFalse(sdr1.equals(sdrf));
		assertFalse(sdr1.equals(sdrg));
		assertFalse(sdr1.equals(sdrh));
		assertFalse(sdr1.equals(sdri));
		assertFalse(sdr1.equals(sdrj));
		assertFalse(sdr1.equals(sdrk));
		String s = sdr.toString();
		assertTrue(s.contains("SiteDescriptionRecord"));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteGroupNumber ="));
		assertTrue(s.contains("handlerType ="));
		assertTrue(s.contains("handlerID ="));
		assertTrue(s.contains("probeCardType ="));
		assertTrue(s.contains("probeCardID ="));
		assertTrue(s.contains("loadBoardType ="));
		assertTrue(s.contains("loadBoardID ="));
		assertTrue(s.contains("dibBoardType ="));
		assertTrue(s.contains("dibBoardID ="));
		assertTrue(s.contains("ifaceCableType ="));
		assertTrue(s.contains("ifaceCableID ="));
		assertTrue(s.contains("contactorType ="));
		assertTrue(s.contains("contactorID ="));
		assertTrue(s.contains("laserType ="));
		assertTrue(s.contains("laserID ="));
		assertTrue(s.contains("equipType ="));
		assertTrue(s.contains("equipID ="));
		assertTrue(s.contains("siteNumbers ="));
	}
	
	// stdf.add(new PinMapRecord(false, snum++, dnum, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
	@Test
	public void testG() throws IOException, StdfException
	{
		StdfRecord r1 = list.get(5);
		assertTrue(r1 instanceof PinMapRecord);
		PinMapRecord pmr1 = (PinMapRecord) r1;
	    assertEquals(0, pmr1.pmrIdx);
	    assertEquals(3, pmr1.channelType.intValue());
	    assertEquals("channelName0", pmr1.channelName);
	    assertEquals("physicalPinName0", pmr1.physicalPinName);
	    assertEquals("logicalPinName0", pmr1.logicalPinName);
	    assertEquals(1, pmr1.headNumber.shortValue());
	    assertEquals(0, pmr1.siteNumber.shortValue());
	    r1 = list.get(6);
	    assertTrue(r1 instanceof PinMapRecord);
	    PinMapRecord pmr = (PinMapRecord) r1;
	    assertEquals(1, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType.intValue());
	    assertEquals("channelName1", pmr.channelName);
	    assertEquals("physicalPinName1", pmr.physicalPinName);
	    assertEquals("logicalPinName1", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber.shortValue());
	    assertEquals(0, pmr.siteNumber.shortValue());
	    r1 = list.get(7);
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
	    assertEquals(2, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType.intValue());
	    assertEquals("channelName2", pmr.channelName);
	    assertEquals("physicalPinName2", pmr.physicalPinName);
	    assertEquals("logicalPinName2", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber.shortValue());
	    assertEquals(0, pmr.siteNumber.shortValue());
	    r1 = list.get(8);
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
	    assertEquals(3, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType.intValue());
	    assertEquals("channelName3", pmr.channelName);
	    assertEquals("physicalPinName3", pmr.physicalPinName);
	    assertEquals("logicalPinName3", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber.shortValue());
	    assertEquals(0, pmr.siteNumber.shortValue());
		PinMapRecord pmr2 = new PinMapRecord(cpu, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr3 = new PinMapRecord(cpu, 1, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr4 = new PinMapRecord(cpu, 0, 2, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr5 = new PinMapRecord(cpu, 0, 3, "xhannelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr6 = new PinMapRecord(cpu, 0, 3, "channelName0", "xhysicalPinName0", "logicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr7 = new PinMapRecord(cpu, 0, 3, "channelName0", "physicalPinName0", "xogicalPinName0", (short) 1, (short) 0);
		PinMapRecord pmr8 = new PinMapRecord(cpu, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 0, (short) 0);
		PinMapRecord pmr9 = new PinMapRecord(cpu, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 1);
		assertTrue(pmr2.equals(pmr1));
		assertEquals(pmr1.hashCode(), pmr2.hashCode());
		assertTrue(pmr1.equals(pmr1));
		assertFalse(pmr1.equals(null));
		assertFalse(pmr1.equals("A"));
		assertFalse(pmr2.equals(pmr3));
		assertFalse(pmr2.equals(pmr4));
		assertFalse(pmr2.equals(pmr5));
		assertFalse(pmr2.equals(pmr6));
		assertFalse(pmr2.equals(pmr7));
		assertFalse(pmr2.equals(pmr8));
		assertFalse(pmr2.equals(pmr9));
		String s = pmr2.toString();
		assertTrue(s.contains("PinMapRecord"));
		assertTrue(s.contains("pmrIdx ="));
		assertTrue(s.contains("channelType ="));
		assertTrue(s.contains("channelName ="));
		assertTrue(s.contains("physicalPinName ="));
		assertTrue(s.contains("logicalPinName ="));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
	}
	
	//stdf.add(new BeginProgramSelectionRecord(snum++, dnum, "beginProgramSectionRecord"));
	@Test
	public void testH() throws IOException, StdfException
	{
	    StdfRecord r = list.get(9);
	    assertTrue(r instanceof BeginProgramSectionRecord);
	    BeginProgramSectionRecord bpr = (BeginProgramSectionRecord) r;
	    assertEquals("beginProgramSectionRecord", bpr.seqName);
		BeginProgramSectionRecord bpr1 = new BeginProgramSectionRecord(cpu, "beginProgramSectionRecord");
		BeginProgramSectionRecord bpr2 = new BeginProgramSectionRecord(cpu, "beginProgramSection");
        assertEquals(bpr, bpr1);
        assertEquals(bpr.hashCode(), bpr1.hashCode());
        assertTrue(bpr.equals(bpr1));
        assertTrue(bpr.equals(bpr));
        assertFalse(bpr.equals(bpr2));
        assertFalse(bpr.equals(""));
        assertFalse(bpr.equals(null));
	}
	
	//stdf.add(new DatalogTextRecord(snum++, dnum, "datalogTextRecord"));
	@Test
	public void testI() throws IOException, StdfException
	{
		StdfRecord r = list.get(10);
		assertTrue(r instanceof DatalogTextRecord);
		DatalogTextRecord dtr = (DatalogTextRecord) r;
		assertEquals("datalogTextRecord", dtr.text);
		DatalogTextRecord dtr1 = new DatalogTextRecord(cpu, "datalogTextRecord");
		DatalogTextRecord dtr2 = new DatalogTextRecord(cpu, "TEXT_DATA : 55.0");
		assertTrue(dtr.equals(dtr));
		assertTrue(dtr.equals(dtr1));
		assertFalse(dtr1.equals(dtr2));
		assertFalse(dtr.equals(null));
		assertEquals(dtr.hashCode(), dtr1.hashCode());
		//DatalogTextRecord dtr3 = new DatalogTextRecord(cpu, "TEXT_DATA : S/N : 55.0");
		//DatalogTextRecord dtr4 = new DatalogTextRecord(cpu, "TEXT_DATA   S/N   55.0");
	}
	
	//stdf.add(new FunctionalTestRecord(snum++, dnum, 3, (short) 2, (short) 1, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(FTROptFlag_t.class), 1234L, 111L, 222L, 55L, 4, 5, (short) 6, new int[] { 1, 2, 3, 4 },
	//	new byte[] { (byte) 4, (byte) 3, (byte) 2, (byte) 1 }, new int[] { 3, 4, 5, 6 },
	//	new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3 }, new byte[] { (byte) 3, (byte) 2, (byte) 1, (byte) 0 },
	//	"vecName", "timeSetName", "vecOpCode", "label", "alarmName", "progTxt", "rsltTxt", (short) 5, 
	//	new byte[] { (byte) 6, (byte) 7, (byte) 8 }));
	@Test
	public void testJ()
	{
		StdfRecord r = list.get(11);
		assertTrue(r instanceof FunctionalTestRecord);
		FunctionalTestRecord ftr = (FunctionalTestRecord) r;
		assertEquals(3, ftr.testNumber);
		assertEquals(2, ftr.headNumber);
		assertEquals(1, ftr.siteNumber);
		assertTrue(ftr.testFlags.size() == 0);
		assertTrue(ftr.optFlags.size() == 0);
		assertEquals(1234L, ftr.cycleCount.longValue());
		assertEquals(111L, ftr.relVaddr.longValue());
		assertEquals(222L, ftr.rptCnt.longValue());
		assertEquals(55L, ftr.numFail.longValue());
		assertEquals(4, ftr.xFailAddr.intValue());
		assertEquals(5, ftr.yFailAddr.intValue());
		assertEquals(6, ftr.vecOffset.intValue());
		assertEquals(1, ftr.getRtnIndex()[0]);
		assertEquals(2, ftr.getRtnIndex()[1]);
		assertEquals(3, ftr.getRtnIndex()[2]);
		assertEquals(4, ftr.getRtnIndex()[3]);
		assertEquals(3, ftr.getRtnState()[0]);
		assertEquals(12, ftr.getRtnState()[1]);
		assertEquals(1, ftr.getRtnState()[2]);
		assertEquals(8, ftr.getRtnState()[3]);
		assertEquals(3, ftr.getPgmIndex()[0]);
		assertEquals(4, ftr.getPgmIndex()[1]);
		assertEquals(5, ftr.getPgmIndex()[2]);
		assertEquals(6, ftr.getPgmIndex()[3]);
		assertEquals(12, ftr.getPgmState()[0]);
		assertEquals(3, ftr.getPgmState()[1]);
		assertEquals(8, ftr.getPgmState()[2]);
		assertEquals(1, ftr.getPgmState()[3]);
		assertEquals(3, ftr.getFailPin()[0]);
		assertEquals(2, ftr.getFailPin()[1]);
		assertEquals(1, ftr.getFailPin()[2]);
		assertEquals(0, ftr.getFailPin()[3]);
		assertEquals("vecName", ftr.vecName);
		assertEquals("timeSetName", ftr.timeSetName);
		assertEquals("vecOpCode", ftr.vecOpCode);
		assertEquals("label", ftr.testName);
		assertEquals("alarmName", ftr.alarmName);
		assertEquals("progTxt", ftr.progTxt);
		assertEquals("rsltTxt", ftr.rsltTxt);
		assertEquals(5, ftr.patGenNum.byteValue());
		assertEquals(6, ftr.getEnComps()[0]);
		assertEquals(7, ftr.getEnComps()[1]);
		assertEquals(8, ftr.getEnComps()[2]);
	}
	
	//GenericDataRecord.Data d1 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I_4, 0), new Integer(33));
	//GenericDataRecord.Data d2 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R_8, 0), new Double(44.0));
	//List<GenericDataRecord.Data> lgd = new ArrayList<GenericDataRecord.Data>(); 
	//lgd.add(d1);
	//lgd.add(d2);
	//stdf.add(new GenericDataRecord(snum++, dnum, lgd));
	@Test
	public void testK()
	{
		StdfRecord r = list.get(12);
		assertTrue(r instanceof GenericDataRecord);
		GenericDataRecord gdr = (GenericDataRecord) r;
		assertTrue(gdr.equals(gdr));
		assertFalse(gdr.equals(null));
		assertFalse(gdr.equals("A"));
		assertEquals(gdr.hashCode(), gdr.hashCode());
		List<GenericDataRecord.Data> l = gdr.list; 
		GenericDataRecord.Data d = l.get(0);
		assertEquals("Data [type=I4, value=33, padCnt=0]", d.toString());
		GenericDataRecord.PadData p1 = new GenericDataRecord.PadData(Data_t.I4, 0);
		GenericDataRecord.PadData p2 = new GenericDataRecord.PadData(Data_t.I4, 0);
		GenericDataRecord.PadData p3 = new GenericDataRecord.PadData(Data_t.I4, 1);
		GenericDataRecord.PadData p4 = new GenericDataRecord.PadData(null, 1);
		GenericDataRecord.PadData p5 = new GenericDataRecord.PadData(Data_t.I2, 0);
		assertEquals("PadData [type=I4, padCnt=0]", p1.toString());
		assertTrue(p4.equals(p4));
		assertFalse(p1.equals(p5));
		assertEquals(p4.hashCode(), p4.hashCode());
		assertTrue(p1.equals(p2));
		assertEquals(p1.hashCode(), p2.hashCode());
		assertFalse(p1.equals(p3));
		assertFalse(p1.equals(null));
		assertFalse(p1.equals("A"));
		GenericDataRecord.Data d1 = new GenericDataRecord.Data(p1, 33);
		GenericDataRecord.Data d2 = new GenericDataRecord.Data(p1, 33);
		GenericDataRecord.Data d3 = new GenericDataRecord.Data(p3, 33);
		GenericDataRecord.Data d4 = new GenericDataRecord.Data(p3, 34);
		GenericDataRecord.Data d5 = new GenericDataRecord.Data(p4, 34);
		GenericDataRecord.Data d6 = new GenericDataRecord.Data(p4, 34);
		GenericDataRecord.Data d7 = new GenericDataRecord.Data(p3, null);
		GenericDataRecord.Data d8 = new GenericDataRecord.Data(p3, null);
		GenericDataRecord.Data d9 = new GenericDataRecord.Data(p5, 33);
		assertFalse(d9.equals(d8));
		assertFalse(d9.equals(d2));
		assertTrue(d1.equals(d1));
		assertFalse(d8.equals(d4));
		assertEquals(d1.hashCode(), d2.hashCode());
		assertTrue(d5.equals(d6));
		assertEquals(d5.hashCode(), d6.hashCode());
		assertTrue(d7.equals(d8));
		assertEquals(d7.hashCode(), d8.hashCode());
		assertTrue(d1.equals(d2));
		assertFalse(d1.equals(d3));
		assertFalse(d1.equals(null));
		assertFalse(d1.equals("A"));
		assertFalse(d3.equals(d4));
		assertEquals(Data_t.I4, d.type);
		assertEquals(33, d.value);
		d = l.get(1);
		assertEquals(Data_t.R8, d.type);
		assertEquals(44.0, d.value);
		GenericDataRecord.BitData b1 = new GenericDataRecord.BitData(p1, 1, (short) 22);
		GenericDataRecord.BitData b2 = new GenericDataRecord.BitData(p1, 1, (short) 22);
		GenericDataRecord.BitData b3 = new GenericDataRecord.BitData(p1, 2, (short) 22);
		assertTrue(b1.equals(b1));
		assertTrue(b1.equals(b2));
		assertFalse(b1.equals(b3));
		assertFalse(b1.equals(null));
		assertEquals(b1.hashCode(), b2.hashCode());
		assertEquals("BitData [numBits=1, type=I4, value=22, padCnt=0]", b1.toString());

		r = list.get(29);
		assertTrue(r instanceof GenericDataRecord);
		GenericDataRecord gdr1 = (GenericDataRecord) r;
		l = gdr1.list; 
		GenericDataRecord.Data d0 = l.get(0);
		assertEquals(Data_t.U1, d0.type);
		assertEquals((short) 33, d0.value);
		assertEquals(0, d0.padCnt);
		
		GenericDataRecord.Data x1 = l.get(1);
		assertEquals(Data_t.U2, x1.type);
		assertEquals((int) 33, x1.value);
		assertEquals(0, x1.padCnt);
		
		GenericDataRecord.Data x2 = l.get(2);
		assertEquals(Data_t.U4, x2.type);
		assertEquals(33L, x2.value);
		assertEquals(0, x2.padCnt);
		
		GenericDataRecord.Data x3 = l.get(3);
		assertEquals(Data_t.I1, x3.type);
		assertEquals((byte) 33, x3.value);
		assertEquals(0, x3.padCnt);
		
		GenericDataRecord.Data x4 = l.get(4);
		assertEquals(Data_t.I2, x4.type);
		assertEquals((short) 33, x4.value);
		assertEquals(0, x4.padCnt);
		
		GenericDataRecord.Data x5 = l.get(5);
		assertEquals(Data_t.R4, x5.type);
		assertEquals(33.0f, x5.value);
		assertEquals(0, x5.padCnt);
		
		GenericDataRecord.Data x6 = l.get(6);
		assertEquals(Data_t.CN, x6.type);
		assertEquals("string", x6.value);
		assertEquals(0, x6.padCnt);
		assertFalse(gdr.equals(gdr1));
	}
	
	//stdf.add(new HardwareBinRecord(snum++, dnum, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
	@Test
	public void testL() throws IOException, StdfException
	{
		StdfRecord r = list.get(13);
		assertTrue(r instanceof HardwareBinRecord);
		HardwareBinRecord hbr = (HardwareBinRecord) r;
		assertEquals(1, hbr.headNumber);
		assertEquals(0, hbr.siteNumber);
		assertEquals(1, hbr.hwBin);
		assertEquals(10L, hbr.binCnt);
		assertEquals('P', hbr.pf.charValue());
		assertEquals("binName", hbr.binName);
		HardwareBinRecord hbr1 = new HardwareBinRecord(cpu, (short) 1, (short) 0, 1, 10L, 'P', "binName");
		HardwareBinRecord hbr2 = new HardwareBinRecord(cpu, (short) 0, (short) 0, 1, 10L, 'P', "binName");
		HardwareBinRecord hbr3 = new HardwareBinRecord(cpu, (short) 1, (short) 1, 1, 10L, 'P', "binName");
		HardwareBinRecord hbr4 = new HardwareBinRecord(cpu, (short) 1, (short) 0, 2, 10L, 'P', "binName");
		HardwareBinRecord hbr5 = new HardwareBinRecord(cpu, (short) 1, (short) 0, 1, 11L, 'P', "binName");
		HardwareBinRecord hbr6 = new HardwareBinRecord(cpu, (short) 1, (short) 0, 1, 10L, 'F', "binName");
		HardwareBinRecord hbr7 = new HardwareBinRecord(cpu, (short) 1, (short) 0, 1, 10L, 'P', "pinName");
		assertTrue(hbr.equals(hbr1));
		assertFalse(hbr.equals(null));
		assertFalse(hbr.equals("A"));
		assertTrue(hbr.equals(hbr));
		assertEquals(hbr.hashCode(), hbr1.hashCode());
		assertFalse(hbr.equals(hbr2));
		assertFalse(hbr.equals(hbr3));
		assertFalse(hbr.equals(hbr4));
		assertFalse(hbr.equals(hbr5));
		assertFalse(hbr.equals(hbr6));
		assertFalse(hbr.equals(hbr7));
	}
	
	//stdf.add(new MasterResultsRecord(snum++, dnum, 1000L, 'C', "lotDesc", "execDesc"));
	@Test
	public void testM() throws IOException, StdfException
	{
		StdfRecord r = list.get(14);
		assertTrue(r instanceof MasterResultsRecord);
		MasterResultsRecord mrr = (MasterResultsRecord) r;
		assertEquals(1000L, mrr.finishDate);
		assertEquals('C', mrr.dispCode.charValue());
		assertEquals("lotDesc", mrr.lotDesc);
		assertEquals("execDesc", mrr.execDesc);

		MasterResultsRecord mrr1 = new MasterResultsRecord(cpu, 1000L, 'C', "lotDesc", "execDesc");
		MasterResultsRecord mrr2 = new MasterResultsRecord(cpu, 1000L, 'D', "lotDesc", "execDesc");
		MasterResultsRecord mrr3 = new MasterResultsRecord(cpu, 1000L, 'C', "", "execDesc");
		MasterResultsRecord mrr4 = new MasterResultsRecord(cpu, 1000L, 'C', "lotDesc", "");
		MasterResultsRecord mrr5 = new MasterResultsRecord(cpu, 1100L, 'C', "lotDesc", "execDesc");
		assertTrue(mrr.equals(mrr));
		assertTrue(mrr.equals(mrr1));
		assertEquals(mrr.hashCode(), mrr1.hashCode());
		assertFalse(mrr.equals(mrr2));
		assertFalse(mrr.equals(mrr3));
		assertFalse(mrr.equals(mrr4));
		assertFalse(mrr.equals(mrr5));
		assertFalse(mrr.equals(""));
	}
	
	//stdf.add(new MultipleResultParametricRecord(snum++, dnum, 22, 1, 0, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(ParamFlag_t.class), 2, 4, new byte[] { 1, 2 }, new double[] { 1.0, 2.0, 3.0, 4.0 },
	//	"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
	//	0.0f, 0.0f, new int[] { 5, 6 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
	@Test
	public void testN() throws IOException, StdfException
	{
		StdfRecord r = list.get(15);
		assertTrue(r instanceof MultipleResultParametricRecord);
		MultipleResultParametricRecord mpr = (MultipleResultParametricRecord) r;
	    assertEquals(22, mpr.testNumber);
	    assertEquals(1, mpr.headNumber);
	    assertEquals(0, mpr.siteNumber);
	    assertEquals(0, mpr.testFlags.size());
	    assertEquals(0, mpr.paramFlags.size());
	    assertEquals(1, mpr.getRtnState()[0]);
	    assertEquals(2, mpr.getRtnState()[1]);
	    assertEquals(1.0, mpr.getResults()[0], 5);
	    assertEquals(2.0, mpr.getResults()[1], 5);
	    assertEquals(3.0, mpr.getResults()[2], 5);
	    assertEquals(4.0, mpr.getResults()[3], 5);
	    assertEquals("text", mpr.testName);
	    assertEquals("alarmName", mpr.alarmName);
	    assertEquals(0, mpr.optFlags.size());
	    assertEquals(0, mpr.resScal.floatValue(), 5);
	    assertEquals(1, mpr.getLlmScal().floatValue(), 5);
	    assertEquals(2, mpr.getHlmScal().floatValue(), 5);
	    assertEquals(1.0f, mpr.getLoLimit().floatValue(), 5);
	    assertEquals(3.0f, mpr.getHiLimit().floatValue(), 5);
	    assertEquals(0.0f, mpr.startIn.floatValue(), 5);
	    assertEquals(0.0f, mpr.incrIn, 5);
	    assertEquals(0, mpr.getRtnIndex()[0]);
	    assertEquals(1, mpr.getRtnIndex()[1]);
	    assertEquals("units", mpr.units);
	    assertEquals("unitsIn", mpr.unitsIn);
	    assertEquals("resFmt", mpr.resFmt);
	    assertEquals("llmFmt", mpr.llmFmt);
	    assertEquals("hlmFmt", mpr.hlmFmt);
	    assertEquals(3.0f, mpr.loSpec, 5);
	    assertEquals(4.0f, mpr.hiSpec, 5);

		MultipleResultParametricRecord mpr1 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr2 = new MultipleResultParametricRecord(cpu, 23L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr3 = new MultipleResultParametricRecord(cpu, 22L, (short) 2, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr4 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 1, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr5 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 1, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr6 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 1, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr7 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 2, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr8 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.3f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr9 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"xext", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr10 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "xlarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr11 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.of(OptFlag_t.NO_LO_LIMIT), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f, // dddd
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr12 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr13 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 2, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr14 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 3, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr15 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 2.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr16 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 4.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr17 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			1.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr18 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 2.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr19 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, //
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 1, 2 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr20 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "xnits", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr21 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "xnitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr22 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "xesFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr23 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "xlmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr24 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "xlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr25 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 4.0f, 4.0f);
		MultipleResultParametricRecord mpr26 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 5.0f);
		assertTrue(mpr.equals(mpr));
		assertTrue(mpr.equals(mpr1));
		assertEquals(mpr.hashCode(), mpr1.hashCode());
		assertFalse(mpr.equals(null));
		assertFalse(mpr.equals("A"));
		assertFalse(mpr.equals(mpr2));
		assertFalse(mpr.equals(mpr3));
		assertFalse(mpr.equals(mpr4));
		assertFalse(mpr.equals(mpr5));
		assertFalse(mpr.equals(mpr6));
		assertFalse(mpr.equals(mpr7));
		assertFalse(mpr.equals(mpr8));
		assertFalse(mpr.equals(mpr9));
		assertFalse(mpr.equals(mpr10));
		assertFalse(mpr.equals(mpr11));
		assertFalse(mpr.equals(mpr12));
		assertFalse(mpr.equals(mpr13));
		assertFalse(mpr.equals(mpr14));
		assertFalse(mpr.equals(mpr15));
		assertFalse(mpr.equals(mpr16));
		assertFalse(mpr.equals(mpr17));
		assertFalse(mpr.equals(mpr18));
		assertFalse(mpr.equals(mpr19));
		assertFalse(mpr.equals(mpr20));
		assertFalse(mpr.equals(mpr21));
		assertFalse(mpr.equals(mpr22));
		assertFalse(mpr.equals(mpr23));
		assertFalse(mpr.equals(mpr24));
		assertFalse(mpr.equals(mpr25));
		assertFalse(mpr.equals(mpr26));
		assertEquals("alarmName", mpr.getAlarmName());
		assertEquals((byte) 0, mpr.getResScal().byteValue());
		assertEquals((byte) 1, mpr.getLlmScal().byteValue());
		assertEquals((byte) 2, mpr.getHlmScal().byteValue());
		assertEquals(1.0f, mpr.getLoLimit(), 3);
		assertEquals(3.0f, mpr.getHiLimit(), 3);
		assertEquals("units", mpr.getUnits());
		MultipleResultParametricRecord mpr41 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.of(OptFlag_t.NO_HI_LIMIT), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f, // dddd
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr51 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.of(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f, // dddd
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		MultipleResultParametricRecord mpr61 = new MultipleResultParametricRecord(cpu, 22L, (short) 1, 
				(short) 0, (byte) 0, (byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.of(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f, // dddd
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f);
		assertEquals(null, mpr11.getLoLimit());
		assertEquals(null, mpr41.getHiLimit());
		assertEquals(null, mpr51.getLoLimit());
		assertEquals(null, mpr61.getHiLimit());
		assertEquals(null, mpr51.getLlmScal());
		assertEquals(null, mpr61.getHlmScal());
		assertTrue(mpr.hasLoLimit());
		assertTrue(mpr.hasHiLimit());
		assertFalse(mpr11.hasLoLimit());
		assertFalse(mpr51.hasLoLimit());
		assertFalse(mpr41.hasHiLimit());
		assertFalse(mpr61.hasHiLimit());
	}
	
//		stdf.add(new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, (byte) 0,
//			(byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
//			(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
	@Test
	public void testO() throws IOException, StdfException
	{
		StdfRecord r = list.get(16);
		assertTrue(r instanceof ParametricTestRecord);
		ParametricTestRecord ptr = (ParametricTestRecord) r;
	    assertEquals(44, ptr.testNumber);
	    assertEquals(1, ptr.headNumber);
	    assertEquals(0, ptr.siteNumber);
	    assertEquals(0, ptr.testFlags.size());
	    assertEquals(0, ptr.paramFlags.size());
	    assertEquals(5.5f, ptr.result, 5);
	    assertEquals("text", ptr.testName);
	    assertEquals("alarmName", ptr.alarmName);
	    assertEquals(0, ptr.optFlags.size());
	    assertEquals(1, ptr.resScal.byteValue());
	    assertEquals(2, ptr.getLlmScal().byteValue());
	    assertEquals(3, ptr.getHlmScal().byteValue());
	    assertEquals(1.0f, ptr.getLoLimit().floatValue(), 5);
	    assertEquals(10.0f, ptr.getHiLimit().floatValue(), 5);
	    assertEquals("units", ptr.units);
	    assertEquals("resFmt", ptr.resFmt);
	    assertEquals("llmFmt", ptr.llmFmt);
	    assertEquals("hlmFmt", ptr.hlmFmt);
	    assertEquals(1.0f, ptr.loSpec, 5);
	    assertEquals(2.0f, ptr.hiSpec, 5);

		ParametricTestRecord ptr1 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		String s = ptr.toString();
		assertTrue(s.contains("ParametricTestRecord"));
		assertTrue(s.contains("result ="));
		assertTrue(s.contains("alarmName ="));
		assertTrue(s.contains("optFlags ="));
		assertTrue(s.contains("resScal ="));
		assertTrue(s.contains("llmScal ="));
		assertTrue(s.contains("hlmScal ="));
		assertTrue(s.contains("loLimit ="));
		assertTrue(s.contains("hiLimit ="));
		assertTrue(s.contains("units ="));
		assertTrue(s.contains("resFmt ="));
		assertTrue(s.contains("llmFmt ="));
		assertTrue(s.contains("hlmFmt ="));
		assertTrue(s.contains("loSpec ="));
		assertTrue(s.contains("hiSpec ="));
		assertTrue(s.contains("testFlags ="));
		assertTrue(s.contains("paramFlags ="));
		assertTrue(s.contains("testNumber ="));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
		ParametricTestRecord ptr2 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.of(OptFlag_t.NO_LO_LIMIT), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr3 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.of(OptFlag_t.NO_HI_LIMIT), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr4 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.of(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr5 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.of(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		assertFalse(ptr.equals(ptr2));
		assertEquals("alarmName", ptr.getAlarmName());
		assertEquals((byte) 1, ptr.getResScal().byteValue());
		assertEquals((byte) 2, ptr.getLlmScal().byteValue());
		assertEquals(null, ptr4.getLlmScal());
		assertEquals((byte) 3, ptr.getHlmScal().byteValue());
		assertEquals(null, ptr5.getHlmScal());
		assertEquals(1.0f, ptr.getLoLimit(), 3);
		assertEquals(10.0f, ptr.getHiLimit(), 3);
		assertEquals(null, ptr2.getLoLimit());
		assertEquals(null, ptr4.getLoLimit());
		assertEquals(null, ptr3.getHiLimit());
		assertEquals(null, ptr5.getHiLimit());
		assertEquals("units", ptr.getUnits());
		ParametricTestRecord ptr10 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "xlarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr11 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 11.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr12 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.1f);
		ParametricTestRecord ptr13 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "xlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr14 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 4, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr15 = new ParametricTestRecord(cpu, 54L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr16 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "xlmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr17 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 1, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr18 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.1f, 2.0f);
		ParametricTestRecord ptr19 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.of(OptFlag_t.NO_LO_SPEC_LIMIT), (byte) 1,  // set OPT flags
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr20 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "xesFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr21 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.6f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr22 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 1, 
			(byte) 2, (byte) 3, 1.0f, 10.0f, "xnits", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		ParametricTestRecord ptr23 = new ParametricTestRecord(cpu, 44L, (short) 1, (short) 0, 
			(byte) 0, (byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 9,  // set OPT flags
			(byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f);
		assertFalse(ptr.equals(ptr10));
		assertFalse(ptr.equals(ptr11));
		assertFalse(ptr.equals(ptr12));
		assertFalse(ptr.equals(ptr13));
		assertFalse(ptr.equals(ptr14));
		assertFalse(ptr.equals(ptr15));
		assertFalse(ptr.equals(ptr16));
		assertFalse(ptr.equals(ptr17));
		assertFalse(ptr.equals(ptr18));
		assertFalse(ptr.equals(ptr19));
		assertFalse(ptr.equals(ptr20));
		assertFalse(ptr.equals(ptr21));
		assertFalse(ptr.equals(ptr22));
		assertFalse(ptr.equals(ptr23));
	}
	
	//stdf.add(new PartCountRecord(snum++, dnum, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
	@Test
	public void testP() throws IOException, StdfException
	{
		StdfRecord r = list.get(17);
		assertTrue(r instanceof PartCountRecord);
		PartCountRecord ptr = (PartCountRecord) r;
        assertEquals(1, ptr.headNumber); 
        assertEquals(0, ptr.siteNumber); 
        assertEquals(2L, ptr.partsTested); 
        assertEquals(1L, ptr.partsReTested.longValue()); 
        assertEquals(0L, ptr.aborts.longValue()); 
        assertEquals(2L, ptr.good.longValue()); 
        assertEquals(1L, ptr.functional.longValue()); 
		PartCountRecord ptr1 = new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L);
		PartCountRecord ptr2 = new PartCountRecord(cpu, (short) 0, (short) 0, 2L, 1L, 0L, 2L, 1L);
		PartCountRecord ptr3 = new PartCountRecord(cpu, (short) 1, (short) 1, 2L, 1L, 0L, 2L, 1L);
		PartCountRecord ptr4 = new PartCountRecord(cpu, (short) 1, (short) 0, 1L, 1L, 0L, 2L, 1L);
		PartCountRecord ptr5 = new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 0L, 0L, 2L, 1L);
		PartCountRecord ptr6 = new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 1L, 1L, 2L, 1L);
		PartCountRecord ptr7 = new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 1L, 0L, 1L, 1L);
		PartCountRecord ptr8 = new PartCountRecord(cpu, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 0L);
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		assertFalse(ptr1.equals(ptr2));
		assertFalse(ptr1.equals(ptr3));
		assertFalse(ptr1.equals(ptr4));
		assertFalse(ptr1.equals(ptr5));
		assertFalse(ptr1.equals(ptr6));
		assertFalse(ptr1.equals(ptr7));
		assertFalse(ptr1.equals(ptr8));
		String s = ptr.toString();
		assertTrue(s.contains("PartCountRecord"));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
		assertTrue(s.contains("partsTested ="));
		assertTrue(s.contains("partsReTested ="));
		assertTrue(s.contains("aborts ="));
		assertTrue(s.contains("good ="));
		assertTrue(s.contains("functional ="));
	}
	
	//stdf.add(new PartInformationRecord(snum++, dnum, (short) 1, (short) 0));
	@Test
	public void testQ() throws IOException, StdfException
	{
		StdfRecord r = list.get(18);
		assertTrue(r instanceof PartInformationRecord);
		PartInformationRecord ptr = (PartInformationRecord) r;
		assertEquals(1, ptr.headNumber);
		assertEquals(0, ptr.siteNumber);
		PartInformationRecord ptr1 = new PartInformationRecord(cpu, (short) 1, (short) 0);
		PartInformationRecord ptr2 = new PartInformationRecord(cpu, (short) 0, (short) 0);
		PartInformationRecord ptr3 = new PartInformationRecord(cpu, (short) 1, (short) 1);
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		assertFalse(ptr.equals(ptr2));
		assertFalse(ptr.equals(ptr3));
		String s = ptr.toString();
		assertTrue(s.contains("PartInformationRecord"));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
	}
	
	//stdf.add(new PartResultsRecord(snum++, dnum, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
	//	10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
	@Test
	public void testR() throws IOException, StdfException
	{
		StdfRecord r = list.get(19);
		assertTrue(r instanceof PartResultsRecord);
		PartResultsRecord ptr = (PartResultsRecord) r;
		assertEquals(1, ptr.headNumber);
		assertEquals(0, ptr.siteNumber);
		assertEquals(0, ptr.partInfoFlags.size());
		assertEquals(1, ptr.numExecs);
		assertEquals(2, ptr.hwBinNumber);
		assertEquals(3, ptr.swBinNumber.intValue());
		assertEquals(1, ptr.xCoord.intValue());
		assertEquals(2, ptr.yCoord.intValue());
		assertEquals(10L, ptr.testTime.longValue());
		assertEquals("partID", ptr.partID);
		assertEquals("partDescription", ptr.partDescription);
		assertEquals(0, ptr.getRepair()[0]);
		assertEquals(1, ptr.getRepair()[1]);
		assertEquals(2, ptr.getRepair()[2]);
		PartResultsRecord ptr1 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr2 = new PartResultsRecord(cpu, (short) 0, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr3 = new PartResultsRecord(cpu, (short) 1, (short) 1, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr4 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 1, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr5 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 2, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr6 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 3, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr7 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 4, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr8 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 0, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptr9 = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 1,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptra = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          11L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptrb = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "xartID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptrc = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "xartDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 });
		PartResultsRecord ptrd = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "partID", "partDescription", new byte[] { (byte) 1, (byte) 1, (byte) 2 });
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		String s = ptr.toString();
		assertTrue(s.contains("PartResultsRecord"));
		assertTrue(s.contains("siteNumber ="));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("partInfoFlags ="));
		assertTrue(s.contains("numExecs ="));
		assertTrue(s.contains("hwBinNumber ="));
		assertTrue(s.contains("swBinNumber ="));
		assertTrue(s.contains("xCoord ="));
		assertTrue(s.contains("yCoord ="));
		assertTrue(s.contains("testTime ="));
		assertTrue(s.contains("partID ="));
		assertTrue(s.contains("partDescription ="));
		assertTrue(s.contains("repair ="));
		assertFalse(ptr1.equals(ptr2));
		assertFalse(ptr1.equals(ptr3));
		assertFalse(ptr1.equals(ptr4));
		assertFalse(ptr1.equals(ptr5));
		assertFalse(ptr1.equals(ptr6));
		assertFalse(ptr1.equals(ptr7));
		assertFalse(ptr1.equals(ptr8));
		assertFalse(ptr1.equals(ptr9));
		assertFalse(ptr1.equals(ptra));
		assertFalse(ptr1.equals(ptrb));
		assertFalse(ptr1.equals(ptrc));
		assertFalse(ptr1.equals(ptrd));
		PartResultsRecord ptre = new PartResultsRecord(cpu, (short) 1, (short) 0, (byte) 28, 1, 2, 3, (short) 1, (short) 2,
			                                          10L, "", "partDescription", new byte[] { (byte) 1, (byte) 1, (byte) 2 });
		assertFalse(ptr.abnormalEOT());
		assertFalse(ptr.failed());
		assertFalse(ptr.noPassFailIndication());
		assertTrue(ptre.abnormalEOT());
		assertTrue(ptre.failed());
		assertTrue(ptre.noPassFailIndication());
	}
	
	@Test
	public void testR1() throws IOException, StdfException
	{
		StdfRecord r = list.get(20);
		assertTrue(r instanceof PinGroupRecord);
		PinGroupRecord pgr = (PinGroupRecord) r;
		assertEquals(pgr.groupIndex, 1);
		assertEquals(pgr.groupName, "group1");
		int[] idx = pgr.getPmrIdx();
		assertEquals(idx.length, 2);
		assertEquals(idx[0], 1);
		assertEquals(idx[1], 2);
		PinGroupRecord pgr1 = new PinGroupRecord(cpu, 1, "group1", new int[] { 1, 2, });
		PinGroupRecord pgr2 = new PinGroupRecord(cpu, 0, "group1", new int[] { 1, 2, });
		PinGroupRecord pgr3 = new PinGroupRecord(cpu, 1, "xroup1", new int[] { 1, 2, });
		PinGroupRecord pgr4 = new PinGroupRecord(cpu, 1, "group1", new int[] { 0, 2, });
		assertEquals(pgr.hashCode(), pgr1.hashCode());
		assertTrue(pgr.equals(pgr));
		assertTrue(pgr.equals(pgr1));
		assertFalse(pgr.equals(null));
		assertFalse(pgr.equals("A"));
		assertFalse(pgr1.equals(pgr2));
		assertFalse(pgr1.equals(pgr3));
		assertFalse(pgr1.equals(pgr4));
		String s = pgr.toString();
		assertTrue(s.contains("PinGroupRecord"));
		assertTrue(s.contains("groupIndex ="));
		assertTrue(s.contains("groupName ="));
		assertTrue(s.contains("pmrIdx ="));
	}
	
	//stdf.add(new PinListRecord(snum++, dnum, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
	//	new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
	@Test
	public void testS() throws IOException, StdfException
	{
		StdfRecord r = list.get(21);
		assertTrue(r instanceof PinListRecord);
		PinListRecord ptr = (PinListRecord) r;
	    assertEquals(1, ptr.getPinIndex()[0]);
	    assertEquals(2, ptr.getPinIndex()[1]);
	    assertEquals(3, ptr.getMode()[0]);
	    assertEquals(4, ptr.getMode()[1]);
	    assertEquals(2, ptr.getRadix()[0]);
	    assertEquals(2, ptr.getRadix()[1]);
	    assertEquals("a", ptr.getPgmChar()[0]);
	    assertEquals("b", ptr.getPgmChar()[1]);
	    assertEquals("c", ptr.getRtnChar()[0]);
	    assertEquals("d", ptr.getRtnChar()[1]);
	    assertEquals("e", ptr.getPgmChal()[0]);
	    assertEquals("f", ptr.getPgmChal()[1]);
	    assertEquals("g", ptr.getRtnChal()[0]);
	    assertEquals("h", ptr.getRtnChal()[1]);
		PinListRecord ptr1 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr2 = new PinListRecord(cpu, new int[] { 0, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr3 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 1, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr4 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 1, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr5 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "b", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr6 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "b", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr7 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "b", "f" }, 
				                               new String[] { "g", "h" });
		PinListRecord ptr8 = new PinListRecord(cpu, new int[] { 1, 2 }, new int[] { 3, 4 }, 
				                               new int[] { 2, 2 }, new String[] { "a", "b" }, 
				                               new String[] { "c", "d" }, new String[] { "e", "f" }, 
				                               new String[] { "b", "h" });
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		assertFalse(ptr1.equals(ptr2));
		assertFalse(ptr1.equals(ptr3));
		assertFalse(ptr1.equals(ptr4));
		assertFalse(ptr1.equals(ptr5));
		assertFalse(ptr1.equals(ptr6));
		assertFalse(ptr1.equals(ptr7));
		assertFalse(ptr1.equals(ptr8));
		String s = ptr.toString();
		assertTrue(s.contains("PinListRecord"));
		assertTrue(s.contains("pinIndex ="));
		assertTrue(s.contains("mode ="));
		assertTrue(s.contains("radix ="));
		assertTrue(s.contains("pgmChar ="));
		assertTrue(s.contains("rtnChar ="));
		assertTrue(s.contains("pgmChal ="));
		assertTrue(s.contains("rtnChal ="));
	}
	
	//stdf.add(new SoftwareBinRecord(snum++, dnum, (short) 1, (short) 0, 5, 45, "F", "binName"));
	@Test
	public void testT() throws IOException, StdfException
	{
		StdfRecord r = list.get(22);
		assertTrue(r instanceof SoftwareBinRecord);
		SoftwareBinRecord ptr = (SoftwareBinRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteNumber);	
        assertEquals(5, ptr.swBinNumber);	
        assertEquals(45, ptr.count);	
        assertEquals('F', ptr.pf.charValue());	
        assertEquals("binName", ptr.binName);	
		SoftwareBinRecord ptr1 = new SoftwareBinRecord(cpu, (short) 1, (short) 0, 5, 45, 'F', "binName");
		SoftwareBinRecord ptr2 = new SoftwareBinRecord(cpu, (short) 0, (short) 0, 5, 45, 'F', "binName");
		SoftwareBinRecord ptr3 = new SoftwareBinRecord(cpu, (short) 1, (short) 1, 5, 45, 'F', "binName");
		SoftwareBinRecord ptr4 = new SoftwareBinRecord(cpu, (short) 1, (short) 0, 6, 45, 'F', "binName");
		SoftwareBinRecord ptr5 = new SoftwareBinRecord(cpu, (short) 1, (short) 0, 5, 46, 'F', "binName");
		SoftwareBinRecord ptr6 = new SoftwareBinRecord(cpu, (short) 1, (short) 0, 5, 45, 'P', "binName");
		SoftwareBinRecord ptr7 = new SoftwareBinRecord(cpu, (short) 1, (short) 0, 5, 45, 'F', "xinName");
		String s = ptr.toString();
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		assertFalse(ptr1.equals(ptr2));
		assertFalse(ptr1.equals(ptr3));
		assertFalse(ptr1.equals(ptr4));
		assertFalse(ptr1.equals(ptr5));
		assertFalse(ptr1.equals(ptr6));
		assertFalse(ptr1.equals(ptr7));
		assertTrue(s.contains("SoftwareBinRecord"));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
		assertTrue(s.contains("swBinNumber ="));
		assertTrue(s.contains("count ="));
		assertTrue(s.contains("pf ="));
		assertTrue(s.contains("binName ="));
	}
	
    //stdf.add(new TestSynopsisRecord(snum++, dnum, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
    //	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	@Test
	public void testU() throws IOException, StdfException
	{
		StdfRecord r = list.get(23);
		assertTrue(r instanceof TestSynopsisRecord);
		TestSynopsisRecord ptr = (TestSynopsisRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteNumber);	
        assertEquals('T', ptr.testType);	
        assertEquals(10L, ptr.testNumber);	
        assertEquals(11L, ptr.numExecs.longValue());	
        assertEquals(12L, ptr.numFailures.longValue());	
        assertEquals(13L, ptr.numAlarms.longValue());	
        assertEquals("testName", ptr.testName);	
        assertEquals("sequencerName", ptr.sequencerName);	
        assertEquals("testLabel", ptr.testLabel);	
        assertEquals(0, ptr.optFlags.size());	
        assertEquals(3.0f, ptr.testTime, 5);	
        assertEquals(1.0f, ptr.testMin, 5);	
        assertEquals(2.2f, ptr.testMax, 5);	
        assertEquals(3.3f, ptr.testSum, 5);	
        assertEquals(4.4f, ptr.testSumSquares, 5);	
	    TestSynopsisRecord ptr1 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr2 = new TestSynopsisRecord(cpu, (short) 0, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr3 = new TestSynopsisRecord(cpu, (short) 1, (short) 1, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr4 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'F', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr5 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 11L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr6 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 12L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr7 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 13L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr8 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 14L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptr9 = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "xestName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptra = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "xequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptrb = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "xestLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptrc = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.of(TestOptFlag_t.TEST_MAX_INVALID), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptrd = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 4.0f, 1.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptre = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 5.0f, 2.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptrf = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 6.2f, 3.3f, 4.4f);	
	    TestSynopsisRecord ptrg = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 7.3f, 4.4f);	
	    TestSynopsisRecord ptrh = new TestSynopsisRecord(cpu, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	                 "sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 8.4f);	
	    String s = ptr.toString();
	    assertTrue(ptr.equals(ptr));
	    assertTrue(ptr.equals(ptr1));
	    assertEquals(ptr.hashCode(), ptr1.hashCode());
	    assertFalse(ptr.equals(null));
	    assertFalse(ptr.equals("A"));
	    assertFalse(ptr1.equals(ptr2));
	    assertFalse(ptr1.equals(ptr3));
	    assertFalse(ptr1.equals(ptr4));
	    assertFalse(ptr1.equals(ptr5));
	    assertFalse(ptr1.equals(ptr6));
	    assertFalse(ptr1.equals(ptr7));
	    assertFalse(ptr1.equals(ptr8));
	    assertFalse(ptr1.equals(ptr9));
	    assertFalse(ptr1.equals(ptra));
	    assertFalse(ptr1.equals(ptrb));
	    assertFalse(ptr1.equals(ptrc));
	    assertFalse(ptr1.equals(ptrd));
	    assertFalse(ptr1.equals(ptre));
	    assertFalse(ptr1.equals(ptrf));
	    assertFalse(ptr1.equals(ptrg));
	    assertFalse(ptr1.equals(ptrh));
	    assertTrue(s.contains("TestSynopsisRecord"));
	    assertTrue(s.contains("headNumber ="));
	    assertTrue(s.contains("siteNumber ="));
	    assertTrue(s.contains("testType ="));
	    assertTrue(s.contains("testNumber ="));
	    assertTrue(s.contains("numExecs ="));
	    assertTrue(s.contains("numFailures ="));
	    assertTrue(s.contains("numAlarms ="));
	    assertTrue(s.contains("testName ="));
	    assertTrue(s.contains("sequencerName ="));
	    assertTrue(s.contains("testLabel ="));
	    assertTrue(s.contains("optFlags ="));
	    assertTrue(s.contains("testTime ="));
	    assertTrue(s.contains("testMin ="));
	    assertTrue(s.contains("testMax ="));
	    assertTrue(s.contains("testSum ="));
	    assertTrue(s.contains("testSumSquares ="));
	}
	
    //stdf.add(new WaferConfigurationRecord(snum++, dnum, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	@Test
	public void testV() throws IOException, StdfException
	{
		StdfRecord r = list.get(24);
		assertTrue(r instanceof WaferConfigurationRecord);
		WaferConfigurationRecord ptr = (WaferConfigurationRecord) r;
        assertEquals(6.0f, ptr.waferSize, 5);	
        assertEquals(3.3f, ptr.dieHeight, 5);	
        assertEquals(4.4f, ptr.dieWidth, 5);	
        assertEquals(1, ptr.units.shortValue());	
        assertEquals('L', ptr.flatOrient.charValue());	
        assertEquals(1, ptr.centerX.shortValue());	
        assertEquals(2, ptr.centerY.shortValue());	
        assertEquals('5', ptr.posX.charValue());	
        assertEquals('5', ptr.posY.charValue());	
	    WaferConfigurationRecord ptr1 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr2 = new WaferConfigurationRecord(cpu, 1.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr3 = new WaferConfigurationRecord(cpu, 6.0f, 1.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr4 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 1.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr5 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 0, 'L', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr6 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, '0', (short) 1, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr7 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 0, (short) 2, '5', '5');
	    WaferConfigurationRecord ptr8 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 0, '5', '5');
	    WaferConfigurationRecord ptr9 = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '0', '5');
	    WaferConfigurationRecord ptra = new WaferConfigurationRecord(cpu, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '0');
	    String s = ptr.toString();
	    assertTrue(ptr.equals(ptr));
	    assertTrue(ptr.equals(ptr1));
	    assertEquals(ptr.hashCode(), ptr1.hashCode());
	    assertFalse(ptr.equals(null));
	    assertFalse(ptr.equals("A"));
	    assertFalse(ptr1.equals(ptr2));
	    assertFalse(ptr1.equals(ptr3));
	    assertFalse(ptr1.equals(ptr4));
	    assertFalse(ptr1.equals(ptr5));
	    assertFalse(ptr1.equals(ptr6));
	    assertFalse(ptr1.equals(ptr7));
	    assertFalse(ptr1.equals(ptr8));
	    assertFalse(ptr1.equals(ptr9));
	    assertFalse(ptr1.equals(ptra));
	    assertTrue(s.contains("WaferConfigurationRecord"));
	    assertTrue(s.contains("waferSize ="));
	    assertTrue(s.contains("dieHeight ="));
	    assertTrue(s.contains("dieWidth ="));
	    assertTrue(s.contains("units ="));
	    assertTrue(s.contains("flatOrient ="));
	    assertTrue(s.contains("centerX ="));
	    assertTrue(s.contains("centerY ="));
	    assertTrue(s.contains("posX ="));
	    assertTrue(s.contains("posY ="));
	}
	
    //stdf.add(new WaferInformationRecord(snum++, dnum, (short) 1, (short) 0, 1000L, "waferID"));
	@Test
	public void testW() throws IOException, StdfException
	{
		StdfRecord r = list.get(25);
		assertTrue(r instanceof WaferInformationRecord);
		WaferInformationRecord ptr = (WaferInformationRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteGroupNumber);	
        assertEquals(1000L, ptr.startDate);	
        assertEquals("waferID", ptr.waferID);	
	    WaferInformationRecord ptr1 = new WaferInformationRecord(cpu, (short) 1, (short) 0, 1000L, "waferID");
	    WaferInformationRecord ptr2 = new WaferInformationRecord(cpu, (short) 2, (short) 0, 1000L, "waferID");
	    WaferInformationRecord ptr3 = new WaferInformationRecord(cpu, (short) 1, (short) 2, 1000L, "waferID");
	    WaferInformationRecord ptr4 = new WaferInformationRecord(cpu, (short) 1, (short) 0, 1002L, "waferID");
	    WaferInformationRecord ptr5 = new WaferInformationRecord(cpu, (short) 1, (short) 0, 1000L, "2aferID");
	    assertTrue(ptr.equals(ptr));
	    assertTrue(ptr.equals(ptr1));
	    assertEquals(ptr.hashCode(), ptr1.hashCode());
	    assertFalse(ptr.equals(null));
	    assertFalse(ptr.equals("A"));
	    assertFalse(ptr1.equals(ptr2));
	    assertFalse(ptr1.equals(ptr3));
	    assertFalse(ptr1.equals(ptr4));
	    assertFalse(ptr1.equals(ptr5));
	    String s = ptr.toString();
	    assertTrue(s.contains("WaferInformationRecord"));
	    assertTrue(s.contains("headNumber ="));
	    assertTrue(s.contains("siteGroupNumber ="));
	    assertTrue(s.contains("startDate ="));
	    assertTrue(s.contains("waferID ="));
	}
	
	//stdf.add(new WaferResultsRecord(snum++, dnum, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
	//	"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
	@Test
	public void testX() throws IOException, StdfException
	{
		StdfRecord r = list.get(26);
		assertTrue(r instanceof WaferResultsRecord);
		WaferResultsRecord ptr = (WaferResultsRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteGroupNumber);	
        assertEquals(1000L, ptr.finishDate);	
        assertEquals(1L, ptr.partCount);	
        assertEquals(2L, ptr.retestCount.longValue());	
        assertEquals(0L, ptr.abortCount.longValue());	
        assertEquals(1L, ptr.passCount.longValue());	
        assertEquals(0L, ptr.functionalCount.longValue());	
        assertEquals("waferID", ptr.waferID);	
        assertEquals("fabWaferID", ptr.fabWaferID);	
        assertEquals("waferFrameID", ptr.waferFrameID);	
        assertEquals("waferMaskID", ptr.waferMaskID);	
        assertEquals("userWaferDesc", ptr.userWaferDesc);	
        assertEquals("execWaferDesc", ptr.execWaferDesc);	
		WaferResultsRecord ptr1 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr2 = new WaferResultsRecord(cpu, (short) 5, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr3 = new WaferResultsRecord(cpu, (short) 1, (short) 5, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr4 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1500L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr5 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 5L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr6 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 5L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr7 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 5L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr8 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 5L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptr9 = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 5L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptra = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"xaferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptrb = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "xabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptrc = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "xaferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptrd = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "xaferMaskID", "userWaferDesc", "execWaferDesc");
		WaferResultsRecord ptre = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "xserWaferDesc", "execWaferDesc");
		WaferResultsRecord ptrf = new WaferResultsRecord(cpu, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "xxecWaferDesc");
		assertTrue(ptr.equals(ptr));
		assertTrue(ptr.equals(ptr1));
		assertEquals(ptr.hashCode(), ptr1.hashCode());
		assertFalse(ptr.equals(null));
		assertFalse(ptr.equals("A"));
		assertFalse(ptr1.equals(ptr2));
		assertFalse(ptr1.equals(ptr3));
		assertFalse(ptr1.equals(ptr4));
		assertFalse(ptr1.equals(ptr5));
		assertFalse(ptr1.equals(ptr6));
		assertFalse(ptr1.equals(ptr7));
		assertFalse(ptr1.equals(ptr8));
		assertFalse(ptr1.equals(ptr9));
		assertFalse(ptr1.equals(ptra));
		assertFalse(ptr1.equals(ptrb));
		assertFalse(ptr1.equals(ptrc));
		assertFalse(ptr1.equals(ptrd));
		assertFalse(ptr1.equals(ptre));
		assertFalse(ptr1.equals(ptrf));
		String s = ptr.toString();
		assertTrue(s.contains("WaferResultsRecord"));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteGroupNumber ="));
		assertTrue(s.contains("finishDate ="));
		assertTrue(s.contains("partCount ="));
		assertTrue(s.contains("retestCount ="));
		assertTrue(s.contains("abortCount ="));
		assertTrue(s.contains("passCount ="));
		assertTrue(s.contains("functionalCount ="));
		assertTrue(s.contains("waferID ="));
		assertTrue(s.contains("fabWaferID ="));
		assertTrue(s.contains("waferFrameID ="));
		assertTrue(s.contains("waferMaskID ="));
		assertTrue(s.contains("userWaferDesc ="));
		assertTrue(s.contains("execWaferDesc ="));
	}
	
    @Test
    public void testY() throws IOException, StdfException
    {
    	StdfRecord r = list.get(27);
    	assertTrue(r instanceof EndProgramSectionRecord);
    	EndProgramSectionRecord esr1 = new EndProgramSectionRecord(cpu);
    	EndProgramSectionRecord esr2 = new EndProgramSectionRecord(cpu);
    	assertTrue(r.equals(r));
    	assertTrue(r.equals(esr1));
    	assertEquals(r.hashCode(), esr1.hashCode());
    	assertEquals(esr1.hashCode(), esr2.hashCode());
    	assertFalse(r.equals("A"));
    }
		
	@Test
	public void testZ() throws IOException, StdfException
	{
		StdfRecord r = list.get(28);
		assertTrue(r instanceof FunctionalTestRecord);
		FunctionalTestRecord ftr = (FunctionalTestRecord) r;
		assertEquals(3, ftr.testNumber);
		assertEquals(2, ftr.headNumber);
		assertEquals(1, ftr.siteNumber);
		assertEquals(null, ftr.cycleCount);
		assertEquals(null, ftr.relVaddr);
		assertEquals(null, ftr.rptCnt);
		assertEquals(null, ftr.numFail);
		assertEquals(null, ftr.xFailAddr);
		assertEquals(null, ftr.yFailAddr);
		assertEquals(null, ftr.vecOffset);
		assertTrue(null == ftr.getRtnIndex());
		assertEquals(null, ftr.getRtnState());
		assertEquals(null, ftr.getPgmIndex());
		assertEquals(null, ftr.getPgmState());
		assertEquals(null, ftr.getFailPin());
		assertEquals(null, ftr.vecName);
		assertEquals(null, ftr.timeSetName);
		assertEquals(null, ftr.vecOpCode);
		assertEquals(null, ftr.testName);
		assertEquals(null, ftr.alarmName);
		assertEquals(null, ftr.progTxt);
		assertEquals(null, ftr.rsltTxt);
		assertEquals(null, ftr.patGenNum);
		assertEquals(null, ftr.getEnComps());
		FunctionalTestRecord ftr0 = new FunctionalTestRecord(cpu, 3L, (short) 2, (short) 1, (byte) 0, null, null, null, null,
				                                             null, null, null, null, null, null, null, null, null, null,
				                                             null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr1 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 0, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr2 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 1, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr3 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 4, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr4 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 8, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr5 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 16, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr6 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 32, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr7 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 64, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		FunctionalTestRecord ftr8 = new FunctionalTestRecord(cpu, 3, (short) 2, (short) 1, (byte) 128, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
		assertTrue(ftr0.equals(ftr1));
		assertEquals(ftr0.hashCode(), ftr1.hashCode());
		assertTrue(ftr2.alarm());
		assertTrue(ftr3.unreliable());
		assertTrue(ftr4.timeout());
		assertTrue(ftr5.notExecuted());
		assertTrue(ftr6.abort());
		assertTrue(ftr7.noPassFailIndication());
		assertTrue(ftr8.fail());
		String s = ftr.toString();
		assertTrue(s.contains("FunctionalTestRecord"));
		assertTrue(s.contains("testFlags ="));
		assertTrue(s.contains("optFlags ="));
		assertTrue(s.contains("cycleCount ="));
		assertTrue(s.contains("relVaddr ="));
		assertTrue(s.contains("rptCnt ="));
		assertTrue(s.contains("numFail ="));
		assertTrue(s.contains("xFailAddr ="));
		assertTrue(s.contains("yFailAddr ="));
		assertTrue(s.contains("vecOffset ="));
		assertTrue(s.contains("vecName ="));
		assertTrue(s.contains("timeSetName ="));
		assertTrue(s.contains("vecOpCode ="));
		assertTrue(s.contains("alarmName ="));
		assertTrue(s.contains("progTxt ="));
		assertTrue(s.contains("rsltTxt ="));
		assertTrue(s.contains("patGenNum ="));
		assertTrue(s.contains("numFailPinBits ="));
		assertTrue(s.contains("numEnCompBits ="));
		assertTrue(s.contains("rtnIndex ="));
		assertTrue(s.contains("rtnState ="));
		assertTrue(s.contains("pgmIndex ="));
		assertTrue(s.contains("pgmState ="));
		assertTrue(s.contains("failPin ="));
		assertTrue(s.contains("enComps ="));
		assertTrue(s.contains("testNumber ="));
		assertTrue(s.contains("headNumber ="));
		assertTrue(s.contains("siteNumber ="));
	}
	
	@Test
	public void testB2()
	{
		StdfRecord r = list.get(30);
		assertTrue(r instanceof DatalogTextRecord);
		DatalogTextRecord dtr = (DatalogTextRecord) r;
		assertEquals("TEXT_DATA : S/N : 30", dtr.text);
	}

		

}
