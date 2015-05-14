package test.stdf;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.makechip.stdf2xls4.stdf.*;

/**
 * @author eric
 *
 */
public class StdfTest1
{
    static StdfWriter stdf;
    static Stack<StdfRecord> stack;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		int snum = 1;
		int dnum = 1;
		FileAttributesRecord far = new FileAttributesRecord(snum++, dnum, 4, Cpu_t.PC);
		List<AuditTrailRecord> atrs = new ArrayList<AuditTrailRecord>();
		atrs.add(new AuditTrailRecord(snum++, dnum, 100000000L, "cmdline"));
		MasterInformationRecord mir = new MasterInformationRecord(snum++, dnum, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID",
			"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
			"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
			"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
			"supervisorID", 1234L);
		RetestDataRecord rdr = new RetestDataRecord(snum++, dnum, new int[] { 1, 2, 3, 4 });
		List<SiteDescriptionRecord> sdrs = new ArrayList<SiteDescriptionRecord>();
		sdrs.add(new SiteDescriptionRecord(snum++, dnum, (short) 1, (short) 2, (short) 1, new int[] { 0 },
			"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
			"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
			"laserType", "laserID", "equipType", "equipID"));
		stdf = new StdfWriter(far, atrs, mir, rdr, sdrs);
		stdf.add(new PinMapRecord(false, snum++, dnum, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(false, snum++, dnum, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(false, snum++, dnum, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(false, snum++, dnum, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
		stdf.add(new BeginProgramSelectionRecord(snum++, dnum, "beginProgramSelectionRecord"));
		stdf.add(new DatalogTextRecord(snum++, dnum, "datalogTextRecord"));
		stdf.add(new FunctionalTestRecord(snum++, dnum, 3, (short) 2, (short) 1, EnumSet.noneOf(TestFlag_t.class),
			EnumSet.noneOf(FTROptFlag_t.class), 1234L, 111L, 222L, 55L, 4, 5, (short) 6, new int[] { 1, 2, 3, 4 },
			new byte[] { (byte) 4, (byte) 3, (byte) 2, (byte) 1 }, new int[] { 3, 4, 5, 6 },
			new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3 }, new byte[] { (byte) 3, (byte) 2, (byte) 1, (byte) 0 },
			"vecName", "timeSetName", "vecOpCode", "label", "alarmName", "progTxt", "rsltTxt", (short) 5, 
			new byte[] { (byte) 6, (byte) 7, (byte) 8 }));
		GenericDataRecord.Data d1 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I_4, 0), new Integer(33));
		GenericDataRecord.Data d2 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R_8, 0), new Double(44.0));
		List<GenericDataRecord.Data> lgd = new ArrayList<GenericDataRecord.Data>(); 
		lgd.add(d1);
		lgd.add(d2);
		stdf.add(new GenericDataRecord(snum++, dnum, lgd));
		stdf.add(new HardwareBinRecord(snum++, dnum, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
		stdf.add(new MasterResultsRecord(snum++, dnum, 1000L, 'C', "lotDesc", "execDesc"));
		stdf.add(new MultipleResultParametricRecord(snum++, dnum, 22, 1, 0, EnumSet.noneOf(TestFlag_t.class),
			EnumSet.noneOf(ParamFlag_t.class), 2, 4, new byte[] { 1, 2 }, new double[] { 1.0, 2.0, 3.0, 4.0 },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 5, 6 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
		stdf.add(new ParametricTestRecord(snum++, dnum, 44, 1, 0, EnumSet.noneOf(TestFlag_t.class),
			EnumSet.noneOf(ParamFlag_t.class), 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
			(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
		stdf.add(new PartCountRecord(snum++, dnum, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
		stdf.add(new PartInformationRecord(snum++, dnum, (short) 1, (short) 0));
		stdf.add(new PartResultsRecord(snum++, dnum, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
		stdf.add(new PinListRecord(snum++, dnum, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
			new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
		stdf.add(new SoftwareBinRecord(snum++, dnum, (short) 1, (short) 0, 5, 45, "F", "binName"));
	    stdf.add(new TestSynopsisRecord(snum++, dnum, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	    stdf.add(new WaferConfigurationRecord(snum++, dnum, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	    stdf.add(new WaferInformationRecord(snum++, dnum, (short) 1, (short) 0, 1000L, "waferID"));
		stdf.add(new WaferResultsRecord(snum++, dnum, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
		stdf.write("x.stdf");
	}
	
	@Test
	public void testA()
	{
		StdfReader rdr = new StdfReader();
		rdr.read(stdf.getBytes());
		List<StdfRecord> list = rdr.stream().map(RecordBytes::createRecord).collect(Collectors.toList());
		assertEquals(26, list.size());
		stack = new Stack<StdfRecord>();
		Stack<StdfRecord> tmp = new Stack<StdfRecord>();
		list.stream().forEach(p -> tmp.push(p));
		while (!tmp.empty()) stack.push(tmp.pop());
	}

    @Test
    public void testB()
    {
    	//Log.msg("stack = " + stack);
    	StdfRecord r = stack.pop();
    	assertTrue(r instanceof FileAttributesRecord);
	    FileAttributesRecord far = (FileAttributesRecord) r;
	    assertEquals(Cpu_t.PC, far.getCpuType());
	    assertEquals(4, far.getStdfVersion());
	    assertEquals(0, r.getSequenceNumber());
    }
    
	// atrs.add(new AuditTrailRecord(snum++, dnum, 100000000L, "cmdline"));
	@Test
	public void testC()
	{
        StdfRecord r = stack.pop();
        assertTrue(r instanceof AuditTrailRecord);
        AuditTrailRecord atr = (AuditTrailRecord) r;
        assertEquals(1, atr.getSequenceNumber());
        assertEquals((new Date(100000000L * 1000L).toString()), atr.getDate());
        assertEquals("cmdline", atr.getCmdLine());
	}

	//MasterInformationRecord mir = new MasterInformationRecord(snum++, dnum, 1000L, 2000L, (short) 1,
	//	'A', 'B', 'C', 100, 'D', "lotID",
	//	"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
	//	"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
	//	"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
	//	"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
	//	"supervisorID", 1234L);
	@Test
	public void testD()
	{
		StdfRecord r = stack.pop();
		assertTrue(r instanceof MasterInformationRecord);
		MasterInformationRecord mir = (MasterInformationRecord) r;
		assertEquals(2, mir.getSequenceNumber());
		assertEquals(-1, mir.getDeviceNumber());
		assertTrue((new Date(1000000L).toString().equals(mir.getJobDate())));
		assertTrue((new Date(2000000L).toString().equals(mir.getTestDate())));
		assertEquals((short) 1, mir.getStationNumber());
		assertEquals("A", mir.getTestModeCode());
		assertEquals("B", mir.getLotRetestCode());
		assertEquals("C", mir.getDataProtectionCode());
		assertEquals(100, mir.getBurnInTime());
		assertEquals("D", mir.getCmdModeCode());
		assertEquals("lotID", mir.getLotID());
		assertEquals("partType", mir.getPartType());
		assertEquals("nodeName", mir.getNodeName());
		assertEquals("testerType", mir.getTesterType());
		assertEquals("jobName", mir.getJobName());
		assertEquals("jobRevisionNumber", mir.getJobRevisionNumber());
		assertEquals("sublotID", mir.getSublotID());
		assertEquals("operatorName", mir.getOperatorName());
		assertEquals("execSoftware", mir.getExecSoftware());
		assertEquals("execSoftwareVersion", mir.getExecSoftwareVersion());
		assertEquals("stepCode", mir.getStepCode());
		assertEquals("temperature", mir.getTemperature());
		assertEquals("userText", mir.getUserText());
		assertEquals("auxDataFile", mir.getAuxDataFile());
		assertEquals("packageType", mir.getPackageType());
		assertEquals("familyID", mir.getFamilyID());
		assertEquals("dateCode", mir.getDateCode());
		assertEquals("facilityID", mir.getFacilityID());
		assertEquals("floorID", mir.getFloorID());
		assertEquals("fabID", mir.getFabID());
		assertEquals("frequency", mir.getFrequency());
		assertEquals("specName", mir.getSpecName());
		assertEquals("specVersion", mir.getSpecVersion());
		assertEquals("flowID", mir.getFlowID());
		assertEquals("setupID", mir.getSetupID());
		assertEquals("designRevision", mir.getDesignRevision());
		assertEquals("engLotID", mir.getEngLotID());
		assertEquals("romCodeID", mir.getRomCodeID());
		assertEquals("testerSerialNumber", mir.getTesterSerialNumber());
		assertEquals("supervisorID", mir.getSupervisorID());
		assertEquals(-1, mir.getFileTimeStamp());
	}
	
	// RetestDataRecord rdr = new RetestDataRecord(snum++, dnum, new int[] { 1, 2, 3, 4 });
	@Test
	public void testE()
	{
		StdfRecord r = stack.pop();
		assertTrue(r instanceof RetestDataRecord);
		RetestDataRecord rdr = (RetestDataRecord) r;
		assertEquals(3, rdr.getSequenceNumber());
		assertEquals(-1, rdr.getDeviceNumber());
		int[] bins = rdr.getRetestBins();
		assertEquals(1, bins[0]);
		assertEquals(2, bins[1]);
		assertEquals(3, bins[2]);
		assertEquals(4, bins[3]);
		
	}
	
	//sdrs.add(new SiteDescriptionRecord(snum++, dnum, (short) 1, (short) 2, (short) 1, new int[] { 0 },
	//	"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
	//	"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
	//	"laserType", "laserID", "equipType", "equipID"));
	@Test
	public void testF()
	{
		StdfRecord r = stack.pop();
		assertTrue(r instanceof SiteDescriptionRecord);
		SiteDescriptionRecord sdr = (SiteDescriptionRecord) r;
		assertEquals(4, sdr.getSequenceNumber());
		assertEquals(1, sdr.getHeadNumber());
		assertEquals(2, sdr.getSiteGroupNumber());
		assertEquals(1, sdr.getNumSites());
		assertEquals(0, sdr.getSiteNumbers()[0]);
		assertEquals("handlerType", sdr.getHandlerType());
		assertEquals("handlerID", sdr.getHandlerID());
		assertEquals("probeCardType", sdr.getProbeCardType());
		assertEquals("probeCardID", sdr.getProbeCardID());
		assertEquals("loadboardType", sdr.getLoadBoardType());
		assertEquals("loadboardID", sdr.getLoadBoardID());
		assertEquals("dibBoardType", sdr.getDibBoardType());
		assertEquals("dibBoardID", sdr.getDibBoardID());
		assertEquals("ifaceCableType", sdr.getIfaceCableType());
		assertEquals("ifaceCableID", sdr.getIfaceCableID());
		assertEquals("contactorType", sdr.getContactorType());
		assertEquals("contactorID", sdr.getContactorID());
		assertEquals("laserType", sdr.getLaserType());
		assertEquals("laserID", sdr.getLaserID());
		assertEquals("equipType", sdr.getEquipType());
		assertEquals("equipID", sdr.getEquipID());
	}
	
	// stdf.add(new PinMapRecord(false, snum++, dnum, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
	@Test
	public void testG()
	{
		StdfRecord r1 = stack.pop();
		assertTrue(r1 instanceof PinMapRecord);
		PinMapRecord pmr = (PinMapRecord) r1;
		assertEquals(5, pmr.getSequenceNumber());
	    assertEquals(0, pmr.getPmrIdx());
	    assertEquals(3, pmr.getChannelType());
	    assertEquals("channelName0", pmr.getChannelName());
	    assertEquals("physicalPinName0", pmr.getPhysicalPinName());
	    assertEquals("logicalPinName0", pmr.getLogicalPinName());
	    assertEquals(1, pmr.getHeadNumber());
	    assertEquals(0, pmr.getSiteNumber());
	    r1 = stack.pop();
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
		assertEquals(6, pmr.getSequenceNumber());
	    assertEquals(1, pmr.getPmrIdx());
	    assertEquals(3, pmr.getChannelType());
	    assertEquals("channelName1", pmr.getChannelName());
	    assertEquals("physicalPinName1", pmr.getPhysicalPinName());
	    assertEquals("logicalPinName1", pmr.getLogicalPinName());
	    assertEquals(1, pmr.getHeadNumber());
	    assertEquals(0, pmr.getSiteNumber());
	    r1 = stack.pop();
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
		assertEquals(7, pmr.getSequenceNumber());
	    assertEquals(2, pmr.getPmrIdx());
	    assertEquals(3, pmr.getChannelType());
	    assertEquals("channelName2", pmr.getChannelName());
	    assertEquals("physicalPinName2", pmr.getPhysicalPinName());
	    assertEquals("logicalPinName2", pmr.getLogicalPinName());
	    assertEquals(1, pmr.getHeadNumber());
	    assertEquals(0, pmr.getSiteNumber());
	    r1 = stack.pop();
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
		assertEquals(8, pmr.getSequenceNumber());
	    assertEquals(3, pmr.getPmrIdx());
	    assertEquals(3, pmr.getChannelType());
	    assertEquals("channelName3", pmr.getChannelName());
	    assertEquals("physicalPinName3", pmr.getPhysicalPinName());
	    assertEquals("logicalPinName3", pmr.getLogicalPinName());
	    assertEquals(1, pmr.getHeadNumber());
	    assertEquals(0, pmr.getSiteNumber());
	}
	
	//stdf.add(new BeginProgramSelectionRecord(snum++, dnum, "beginProgramSelectionRecord"));
	@Test
	public void testH()
	{
	    StdfRecord r = stack.pop();
	    assertTrue(r instanceof BeginProgramSelectionRecord);
	    assertEquals(9, r.getSequenceNumber());
	}
	
	//stdf.add(new DatalogTextRecord(snum++, dnum, "datalogTextRecord"));
	@Test
	public void testI()
	{
		StdfRecord r = stack.pop();
		assertTrue(r instanceof DatalogTextRecord);
		assertEquals(10, r.getSequenceNumber());
		DatalogTextRecord dtr = (DatalogTextRecord) r;
		assertEquals("datalogTextRecord", dtr.getText());
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
		StdfRecord r = stack.pop();
		assertTrue(r instanceof FunctionalTestRecord);
		assertEquals(11, r.getSequenceNumber());
		FunctionalTestRecord ftr = (FunctionalTestRecord) r;
		assertEquals(3, ftr.getTestNumber());
		assertEquals(2, ftr.getHeadNumber());
		assertEquals(1, ftr.getSiteNumber());
		assertTrue(ftr.getTestFlags().size() == 0);
		assertTrue(ftr.getOptFlags().size() == 0);
		assertEquals(1234L, ftr.getCycleCount());
		assertEquals(111L, ftr.getRelVaddr());
		assertEquals(222L, ftr.getRptCnt());
		assertEquals(55L, ftr.getNumFail());
		assertEquals(4, ftr.getxFailAddr());
		assertEquals(5, ftr.getyFailAddr());
		assertEquals(6, ftr.getVecOffset());
		assertEquals(1, ftr.getRtnIndex()[0]);
		assertEquals(2, ftr.getRtnIndex()[1]);
		assertEquals(3, ftr.getRtnIndex()[2]);
		assertEquals(4, ftr.getRtnIndex()[3]);
		assertEquals(4, ftr.getRtnState()[0]);
		assertEquals(3, ftr.getRtnState()[1]);
		assertEquals(2, ftr.getRtnState()[2]);
		assertEquals(1, ftr.getRtnState()[3]);
		assertEquals(3, ftr.getPgmIndex()[0]);
		assertEquals(4, ftr.getPgmIndex()[1]);
		assertEquals(5, ftr.getPgmIndex()[2]);
		assertEquals(6, ftr.getPgmIndex()[3]);
		assertEquals(0, ftr.getPgmState()[0]);
		assertEquals(1, ftr.getPgmState()[1]);
		assertEquals(2, ftr.getPgmState()[2]);
		assertEquals(3, ftr.getPgmState()[3]);
		assertEquals(3, ftr.getFailPin()[0]);
		assertEquals(2, ftr.getFailPin()[1]);
		assertEquals(1, ftr.getFailPin()[2]);
		assertEquals(0, ftr.getFailPin()[3]);
		assertEquals("vecName", ftr.getVecName());
		assertEquals("timeSetName", ftr.getTimeSetName());
		assertEquals("vecOpCode", ftr.getVecOpCode());
		assertEquals("label",ftr.getTestName());
		assertEquals("alarmName", ftr.getAlarmName());
		assertEquals("progTxt", ftr.getProgTxt());
		assertEquals("rsltTxt", ftr.getRsltTxt());
		assertEquals(5, ftr.getPatGenNum());
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
	
	}
	
	//stdf.add(new HardwareBinRecord(snum++, dnum, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
	@Test
	public void testL()
	{
	
	}
	
	//stdf.add(new MasterResultsRecord(snum++, dnum, 1000L, 'C', "lotDesc", "execDesc"));
	@Test
	public void testM()
	{
	
	}
	
	//stdf.add(new MultipleResultParametricRecord(snum++, dnum, 22, 1, 0, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(ParamFlag_t.class), 2, 4, new byte[] { 1, 2 }, new double[] { 1.0, 2.0, 3.0, 4.0 },
	//	"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
	//	0.0f, 0.0f, new int[] { 5, 6 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
	@Test
	public void testN()
	{
	
	}
	
	//stdf.add(new ParametricTestRecord(snum++, dnum, 44, 1, 0, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(ParamFlag_t.class), 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
	//	(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
	@Test
	public void testO()
	{
	
	}
	
	//stdf.add(new PartCountRecord(snum++, dnum, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
	@Test
	public void testP()
	{
	
	}
	
	//stdf.add(new PartInformationRecord(snum++, dnum, (short) 1, (short) 0));
	@Test
	public void testQ()
	{
	
	}
	
	//stdf.add(new PartResultsRecord(snum++, dnum, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
	//	10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
	@Test
	public void testR()
	{
	
	}
	
	//stdf.add(new PinListRecord(snum++, dnum, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
	//	new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
	@Test
	public void testS()
	{
	
	}
	
	//stdf.add(new SoftwareBinRecord(snum++, dnum, (short) 1, (short) 0, 5, 45, "F", "binName"));
	@Test
	public void testT()
	{
	
	}
	
    //stdf.add(new TestSynopsisRecord(snum++, dnum, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
    //	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	@Test
	public void testU()
	{
	
	}
	
    //stdf.add(new WaferConfigurationRecord(snum++, dnum, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	@Test
	public void testV()
	{
	
	}
	
    //stdf.add(new WaferInformationRecord(snum++, dnum, (short) 1, (short) 0, 1000L, "waferID"));
	@Test
	public void testW()
	{
	
	}
	
	//stdf.add(new WaferResultsRecord(snum++, dnum, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
	//	"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
	@Test
	public void testX()
	{
	
	}
	

		
		
		
		
		
		
		
		

}
