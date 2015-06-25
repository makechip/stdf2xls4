package test.stdf;

import static org.junit.Assert.*;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.makechip.stdf2xls4.stdf.*;
import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;

/**
 * @author eric
 *
 */
public class StdfTest1
{
	static DefaultValueDatabase dvd = new DefaultValueDatabase(0L);
	static TestIdDatabase tdb = new TestIdDatabase();
    static StdfWriter stdf;
    static List<StdfRecord> list;
    static StdfReader rdr;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		FileAttributesRecord far = new FileAttributesRecord(tdb, dvd, 4, Cpu_t.PC);
		List<AuditTrailRecord> atrs = new ArrayList<AuditTrailRecord>();
		atrs.add(new AuditTrailRecord(tdb, dvd, 100000000L, "cmdline"));
		MasterInformationRecord mir = new MasterInformationRecord(tdb, dvd, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID",
			"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
			"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
			"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
			"supervisorID", 1234L);
		RetestDataRecord srdr = new RetestDataRecord(tdb, dvd, new int[] { 1, 2, 3, 4 });
		List<SiteDescriptionRecord> sdrs = new ArrayList<SiteDescriptionRecord>();
		sdrs.add(new SiteDescriptionRecord(tdb, dvd, (short) 1, (short) 2, (short) 1, new int[] { 0 },
			"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
			"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
			"laserType", "laserID", "equipType", "equipID"));
		stdf = new StdfWriter(far, atrs, mir, srdr, sdrs);
		stdf.add(new PinMapRecord(tdb, dvd, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(tdb, dvd, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(tdb, dvd, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
		stdf.add(new PinMapRecord(tdb, dvd, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
		stdf.add(new BeginProgramSectionRecord(tdb, dvd, "beginProgramSectionRecord"));
		stdf.add(new DatalogTextRecord(tdb, dvd, "datalogTextRecord"));
		stdf.add(new FunctionalTestRecord(tdb, dvd, Cpu_t.PC, 3, (short) 2, (short) 1, (byte) 0,
			(byte) 0, 1234L, 111L, 222L, 55L, 4, 5, (short) 6, new int[] { 1, 2, 3, 4 },
			new byte[] { (byte) 4, (byte) 3, (byte) 2, (byte) 1 }, new int[] { 3, 4, 5, 6 },
			new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3 }, 32, new byte[] { (byte) 3, (byte) 2, (byte) 1, (byte) 0 },
			"vecName", "timeSetName", "vecOpCode", "label", "alarmName", "progTxt", "rsltTxt", (short) 5,  24,
			new byte[] { (byte) 6, (byte) 7, (byte) 8 }));
		GenericDataRecord.Data d1 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I_4, 0), new Integer(33));
		GenericDataRecord.Data d2 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R_8, 0), new Double(44.0));
		List<GenericDataRecord.Data> lgd = new ArrayList<GenericDataRecord.Data>(); 
		lgd.add(d1);
		lgd.add(d2);
		stdf.add(new GenericDataRecord(tdb, dvd, lgd));
		stdf.add(new HardwareBinRecord(tdb, dvd, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
		stdf.add(new MasterResultsRecord(tdb, dvd, 1000L, 'C', "lotDesc", "execDesc"));
		stdf.add(new MultipleResultParametricRecord(tdb, dvd, Cpu_t.PC, 22L, (short) 1, (short) 0, (byte) 0,
			(byte) 0, 2, 4, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 5, 6 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
		stdf.add(new ParametricTestRecord(tdb, dvd, Cpu_t.PC, 44L, (short) 1, (short) 0, (byte) 0,
			(byte) 0, 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
			(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
		stdf.add(new PartCountRecord(tdb, dvd, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
		stdf.add(new PartInformationRecord(tdb, dvd, (short) 1, (short) 0));
		stdf.add(new PartResultsRecord(tdb, dvd, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
			10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
		stdf.add(new PinGroupRecord(tdb, dvd, 1, "group1", new int[] { 1, 2, }));
		stdf.add(new PinListRecord(tdb, dvd, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
			new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
		stdf.add(new SoftwareBinRecord(tdb, dvd, (short) 1, (short) 0, 5, 45, 'F', "binName"));
	    stdf.add(new TestSynopsisRecord(tdb, dvd, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
	    	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	    stdf.add(new WaferConfigurationRecord(tdb, dvd, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	    stdf.add(new WaferInformationRecord(tdb, dvd, (short) 1, (short) 0, 1000L, "waferID"));
		stdf.add(new WaferResultsRecord(tdb, dvd, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
			"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
		Path p = FileSystems.getDefault().getPath("x.stdf");
		stdf.write(p.toFile());
        Files.delete(p);
		rdr = new StdfReader(tdb, p.toFile().getAbsolutePath());
		rdr.read(stdf.getBytes());
		list = rdr.stream().collect(Collectors.toList());
	}
	
	@Test
	public void testA()
	{
		assertEquals(27, list.size());
	}

    @Test
    public void testB()
    {
    	//Log.msg("stack = " + stack);
    	StdfRecord r = list.get(0);
    	assertTrue(r instanceof FileAttributesRecord);
	    FileAttributesRecord far = (FileAttributesRecord) r;
	    assertEquals(Cpu_t.PC, far.cpuType);
	    assertEquals(4, far.stdfVersion);
	    assertFalse(r.isTestRecord());
    }
    
	// atrs.add(new AuditTrailRecord(snum++, dnum, 100000000L, "cmdline"));
	@Test
	public void testC()
	{
        StdfRecord r = list.get(1);
	    assertFalse(r.isTestRecord());
        assertTrue(r instanceof AuditTrailRecord);
        AuditTrailRecord atr = (AuditTrailRecord) r;
        assertEquals(100000000L, atr.date);
        assertEquals("cmdline", atr.cmdLine);
        assertEquals(atr.toString(), "AuditTrailRecord [date=100000000, cmdLine=cmdline]");
		AuditTrailRecord atr1 = new AuditTrailRecord(tdb, dvd, 100000000L, "cmdline");
		AuditTrailRecord atr2 = new AuditTrailRecord(tdb, dvd, 100000000L, "xmdline");
		AuditTrailRecord atr3 = new AuditTrailRecord(tdb, dvd, 100000001L, "cmdline");
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
	public void testD()
	{
		StdfRecord r = list.get(2);
	    assertFalse(r.isTestRecord());
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
		assertEquals(0, mir.timeStamp);
	}
	
	// RetestDataRecord rdr = new RetestDataRecord(new int[] { 1, 2, 3, 4 });
	@Test
	public void testE()
	{
		StdfRecord r = list.get(3);
	    assertFalse(r.isTestRecord());
		assertTrue(r instanceof RetestDataRecord);
		RetestDataRecord rdr = (RetestDataRecord) r;
		int[] bins = rdr.getRetestBins();
		assertEquals(1, bins[0]);
		assertEquals(2, bins[1]);
		assertEquals(3, bins[2]);
		assertEquals(4, bins[3]);
		
	}
	
	//sdrs.add(new SiteDescriptionRecord((short) 1, (short) 2, (short) 1, new int[] { 0 },
	//	"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
	//	"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
	//	"laserType", "laserID", "equipType", "equipID"));
	@Test
	public void testF()
	{
		StdfRecord r = list.get(4);
	    assertFalse(r.isTestRecord());
		assertTrue(r instanceof SiteDescriptionRecord);
		SiteDescriptionRecord sdr = (SiteDescriptionRecord) r;
		assertEquals(1, sdr.headNumber);
		assertEquals(2, sdr.siteGroupNumber);
		assertEquals(1, sdr.numSites);
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
	}
	
	// stdf.add(new PinMapRecord(false, snum++, dnum, 0, 3, "channelName0", "physicalPinName0", "logicalPinName0", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 1, 3, "channelName1", "physicalPinName1", "logicalPinName1", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 2, 3, "channelName2", "physicalPinName2", "logicalPinName2", (short) 1, (short) 0));
	// stdf.add(new PinMapRecord(false, snum++, dnum, 3, 3, "channelName3", "physicalPinName3", "logicalPinName3", (short) 1, (short) 0));
	@Test
	public void testG()
	{
		StdfRecord r1 = list.get(5);
	    assertFalse(r1.isTestRecord());
		assertTrue(r1 instanceof PinMapRecord);
		PinMapRecord pmr = (PinMapRecord) r1;
	    assertEquals(0, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType);
	    assertEquals("channelName0", pmr.channelName);
	    assertEquals("physicalPinName0", pmr.physicalPinName);
	    assertEquals("logicalPinName0", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber);
	    assertEquals(0, pmr.siteNumber);
	    r1 = list.get(6);
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
	    assertEquals(1, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType);
	    assertEquals("channelName1", pmr.channelName);
	    assertEquals("physicalPinName1", pmr.physicalPinName);
	    assertEquals("logicalPinName1", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber);
	    assertEquals(0, pmr.siteNumber);
	    r1 = list.get(7);
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
	    assertEquals(2, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType);
	    assertEquals("channelName2", pmr.channelName);
	    assertEquals("physicalPinName2", pmr.physicalPinName);
	    assertEquals("logicalPinName2", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber);
	    assertEquals(0, pmr.siteNumber);
	    r1 = list.get(8);
	    assertTrue(r1 instanceof PinMapRecord);
	    pmr = (PinMapRecord) r1;
	    assertEquals(3, pmr.pmrIdx);
	    assertEquals(3, pmr.channelType);
	    assertEquals("channelName3", pmr.channelName);
	    assertEquals("physicalPinName3", pmr.physicalPinName);
	    assertEquals("logicalPinName3", pmr.logicalPinName);
	    assertEquals(1, pmr.headNumber);
	    assertEquals(0, pmr.siteNumber);
	}
	
	//stdf.add(new BeginProgramSelectionRecord(snum++, dnum, "beginProgramSectionRecord"));
	@Test
	public void testH()
	{
	    StdfRecord r = list.get(9);
	    assertFalse(r.isTestRecord());
	    assertTrue(r instanceof BeginProgramSectionRecord);
	    BeginProgramSectionRecord bpr = (BeginProgramSectionRecord) r;
	    assertEquals("beginProgramSectionRecord", bpr.seqName);
	    assertEquals("BeginProgramSectionRecord [seqName=beginProgramSectionRecord]", bpr.toString());
		BeginProgramSectionRecord bpr1 = new BeginProgramSectionRecord(tdb, dvd, "beginProgramSectionRecord");
		BeginProgramSectionRecord bpr2 = new BeginProgramSectionRecord(tdb, dvd, "beginProgramSection");
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
	public void testI()
	{
		StdfRecord r = list.get(10);
	    assertFalse(r.isTestRecord());
		assertTrue(r instanceof DatalogTextRecord);
		DatalogTextRecord dtr = (DatalogTextRecord) r;
		assertEquals("datalogTextRecord", dtr.text);
		DatalogTextRecord dtr1 = new DatalogTextRecord(tdb, dvd, "datalogTextRecord");
		DatalogTextRecord dtr2 = new DatalogTextRecord(tdb, dvd, "TEXT_DATA : 55.0");
		assertTrue(dtr2.isTestRecord());
		assertEquals("DatalogTextRecord [text=datalogTextRecord]", dtr.toString());
		assertTrue(dtr.equals(dtr));
		assertTrue(dtr.equals(dtr1));
		assertFalse(dtr1.equals(dtr2));
		assertFalse(dtr.equals(null));
		assertEquals(dtr.hashCode(), dtr1.hashCode());
		DatalogTextRecord dtr3 = new DatalogTextRecord(tdb, dvd, "TEXT_DATA : S/N : 55.0");
		assertFalse(dtr3.isTestRecord());
		DatalogTextRecord dtr4 = new DatalogTextRecord(tdb, dvd, "TEXT_DATA   S/N   55.0");
		assertFalse(dtr4.isTestRecord());
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
	    assertTrue(r.isTestRecord());
		FunctionalTestRecord ftr = (FunctionalTestRecord) r;
		assertTrue(ftr.isTestRecord());
		assertEquals(3, ftr.testNumber);
		assertEquals(2, ftr.headNumber);
		assertEquals(1, ftr.siteNumber);
		assertTrue(ftr.testFlags.size() == 0);
		assertTrue(ftr.optFlags.size() == 0);
		assertEquals(1234L, ftr.cycleCount);
		assertEquals(111L, ftr.relVaddr);
		assertEquals(222L, ftr.rptCnt);
		assertEquals(55L, ftr.numFail);
		assertEquals(4, ftr.xFailAddr);
		assertEquals(5, ftr.yFailAddr);
		assertEquals(6, ftr.vecOffset);
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
		assertEquals("vecName", ftr.vecName);
		assertEquals("timeSetName", ftr.timeSetName);
		assertEquals("vecOpCode", ftr.vecOpCode);
		assertEquals("label", ftr.getTestId().testName);
		assertEquals("alarmName", ftr.alarmName);
		assertEquals("progTxt", ftr.progTxt);
		assertEquals("rsltTxt", ftr.rsltTxt);
		assertEquals(5, ftr.patGenNum);
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
		assertFalse(r.isTestRecord());
		GenericDataRecord gdr = (GenericDataRecord) r;
		List<GenericDataRecord.Data> l = gdr.list; 
		GenericDataRecord.Data d = l.get(0);
		assertEquals(Data_t.I_4, d.getType());
		assertEquals(33, d.getValue());
		d = l.get(1);
		assertEquals(Data_t.R_8, d.getType());
		assertEquals(44.0, d.getValue());
	}
	
	//stdf.add(new HardwareBinRecord(snum++, dnum, (short) 1, (short) 0, 1, 10L, 'P', "binName"));
	@Test
	public void testL()
	{
		StdfRecord r = list.get(13);
		assertTrue(r instanceof HardwareBinRecord);
		assertFalse(r.isTestRecord());
		HardwareBinRecord hbr = (HardwareBinRecord) r;
		assertEquals(1, hbr.headNumber);
		assertEquals(0, hbr.siteNumber);
		assertEquals(1, hbr.hwBin);
		assertEquals(10L, hbr.binCnt);
		assertEquals('P', hbr.pf);
		assertEquals("binName", hbr.binName);
	}
	
	//stdf.add(new MasterResultsRecord(snum++, dnum, 1000L, 'C', "lotDesc", "execDesc"));
	@Test
	public void testM()
	{
		StdfRecord r = list.get(14);
		assertTrue(r instanceof MasterResultsRecord);
		assertFalse(r.isTestRecord());
		MasterResultsRecord mrr = (MasterResultsRecord) r;
		assertEquals(1000L, mrr.finishDate);
		assertEquals("C", mrr.dispCode);
		assertEquals("lotDesc", mrr.lotDesc);
		assertEquals("execDesc", mrr.execDesc);
	}
	
	//stdf.add(new MultipleResultParametricRecord(snum++, dnum, 22, 1, 0, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(ParamFlag_t.class), 2, 4, new byte[] { 1, 2 }, new double[] { 1.0, 2.0, 3.0, 4.0 },
	//	"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
	//	0.0f, 0.0f, new int[] { 5, 6 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
	@Test
	public void testN()
	{
		StdfRecord r = list.get(15);
		assertTrue(r instanceof MultipleResultParametricRecord);
	    assertTrue(r.isTestRecord());
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
	    assertEquals("text", mpr.getTestId().testName);
	    assertEquals("alarmName", mpr.alarmName);
	    assertEquals(0, mpr.optFlags.size());
	    assertEquals(0, mpr.resScal);
	    assertEquals(1, mpr.llmScal);
	    assertEquals(2, mpr.hlmScal);
	    assertEquals(1.0f, mpr.loLimit, 5);
	    assertEquals(3.0f, mpr.hiLimit, 5);
	    assertEquals(0.0f, mpr.startIn, 5);
	    assertEquals(0.0f, mpr.incrIn, 5);
	    assertEquals(5, mpr.getRtnIndex()[0]);
	    assertEquals(6, mpr.getRtnIndex()[1]);
	    assertEquals("units", mpr.units);
	    assertEquals("unitsIn", mpr.unitsIn);
	    assertEquals("resFmt", mpr.resFmt);
	    assertEquals("llmFmt", mpr.llmFmt);
	    assertEquals("hlmFmt", mpr.hlmFmt);
	    assertEquals(3.0f, mpr.loSpec, 5);
	    assertEquals(4.0f, mpr.hiSpec, 5);
	}
	
	//stdf.add(new ParametricTestRecord(snum++, dnum, 44, 1, 0, EnumSet.noneOf(TestFlag_t.class),
	//	EnumSet.noneOf(ParamFlag_t.class), 5.5f, "text", "alarmName", EnumSet.noneOf(OptFlag_t.class),
	//	(byte) 1, (byte) 2, (byte) 3, 1.0f, 10.0f, "units", "resFmt", "llmFmt", "hlmFmt", 1.0f, 2.0f));
	@Test
	public void testO()
	{
		StdfRecord r = list.get(16);
		assertTrue(r instanceof ParametricTestRecord);
	    assertTrue(r.isTestRecord());
		ParametricTestRecord ptr = (ParametricTestRecord) r;
	    assertEquals(44, ptr.testNumber);
	    assertEquals(1, ptr.headNumber);
	    assertEquals(0, ptr.siteNumber);
	    assertEquals(0, ptr.testFlags.size());
	    assertEquals(0, ptr.paramFlags.size());
	    assertEquals(5.5f, ptr.result, 5);
	    assertEquals("text", ptr.getTestId().testName);
	    assertEquals("alarmName", ptr.alarmName);
	    assertEquals(0, ptr.optFlags.size());
	    assertEquals(1, ptr.resScal);
	    assertEquals(2, ptr.llmScal);
	    assertEquals(3, ptr.hlmScal);
	    assertEquals(1.0f, ptr.loLimit, 5);
	    assertEquals(10.0f, ptr.hiLimit, 5);
	    assertEquals("units", ptr.units);
	    assertEquals("resFmt", ptr.resFmt);
	    assertEquals("llmFmt", ptr.llmFmt);
	    assertEquals("hlmFmt", ptr.hlmFmt);
	    assertEquals(1.0f, ptr.loSpec, 5);
	    assertEquals(2.0f, ptr.hiSpec, 5);
	}
	
	//stdf.add(new PartCountRecord(snum++, dnum, (short) 1, (short) 0, 2L, 1L, 0L, 2L, 1L));
	@Test
	public void testP()
	{
		StdfRecord r = list.get(17);
		assertTrue(r instanceof PartCountRecord);
		assertFalse(r.isTestRecord());
		PartCountRecord ptr = (PartCountRecord) r;
        assertEquals(1, ptr.headNumber); 
        assertEquals(0, ptr.siteNumber); 
        assertEquals(2L, ptr.partsTested); 
        assertEquals(1L, ptr.partsReTested); 
        assertEquals(0L, ptr.aborts); 
        assertEquals(2L, ptr.good); 
        assertEquals(1L, ptr.functional); 
	}
	
	//stdf.add(new PartInformationRecord(snum++, dnum, (short) 1, (short) 0));
	@Test
	public void testQ()
	{
		StdfRecord r = list.get(18);
		assertTrue(r instanceof PartInformationRecord);
		assertFalse(r.isTestRecord());
		PartInformationRecord ptr = (PartInformationRecord) r;
		assertEquals(1, ptr.headNumber);
		assertEquals(0, ptr.siteNumber);
	}
	
	//stdf.add(new PartResultsRecord(snum++, dnum, (short) 1, (short) 0, (byte) 0, 1, 2, 3, (short) 1, (short) 2,
	//	10L, "partID", "partDescription", new byte[] { (byte) 0, (byte) 1, (byte) 2 }));
	@Test
	public void testR()
	{
		StdfRecord r = list.get(19);
		assertTrue(r instanceof PartResultsRecord);
		assertFalse(r.isTestRecord());
		PartResultsRecord ptr = (PartResultsRecord) r;
		assertEquals(1, ptr.headNumber);
		assertEquals(0, ptr.siteNumber);
		assertEquals(0, ptr.partInfoFlags.size());
		assertEquals(1, ptr.numExecs);
		assertEquals(2, ptr.hwBinNumber);
		assertEquals(3, ptr.swBinNumber);
		assertEquals(1, ptr.xCoord);
		assertEquals(2, ptr.yCoord);
		assertEquals(10L, ptr.testTime);
		assertEquals("partID", ptr.getPartID());
		assertEquals("partDescription", ptr.partDescription);
		assertEquals(0, ptr.getRepair()[0]);
		assertEquals(1, ptr.getRepair()[1]);
		assertEquals(2, ptr.getRepair()[2]);
	}
	
	@Test
	public void testR1()
	{
		StdfRecord r = list.get(20);
		assertTrue(r instanceof PinGroupRecord);
		assertFalse(r.isTestRecord());
		PinGroupRecord pgr = (PinGroupRecord) r;
		assertEquals(pgr.toString(), "PinGroupRecord [groupIndex=1, groupName=group1, pmrIdx=[1, 2]]");
		assertEquals(pgr.groupIndex, 1);
		assertEquals(pgr.groupName, "group1");
		int[] idx = pgr.getPmrIdx();
		assertEquals(idx.length, 2);
		assertEquals(idx[0], 1);
		assertEquals(idx[1], 2);
	}
	
	//stdf.add(new PinListRecord(snum++, dnum, new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 2, 2 }, 
	//	new String[] { "a", "b" }, new String[] { "c", "d" }, new String[] { "e", "f" }, new String[] { "g", "h" }));
	@Test
	public void testS()
	{
		StdfRecord r = list.get(21);
		assertTrue(r instanceof PinListRecord);
		assertFalse(r.isTestRecord());
		PinListRecord ptr = (PinListRecord) r;
	    //assertEquals(1, ptr.getPinIndex()[0]);
	    //assertEquals(2, ptr.getPinIndex()[1]);
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
	}
	
	//stdf.add(new SoftwareBinRecord(snum++, dnum, (short) 1, (short) 0, 5, 45, "F", "binName"));
	@Test
	public void testT()
	{
		StdfRecord r = list.get(22);
		assertTrue(r instanceof SoftwareBinRecord);
		assertFalse(r.isTestRecord());
		SoftwareBinRecord ptr = (SoftwareBinRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteNumber);	
        assertEquals(5, ptr.swBinNumber);	
        assertEquals(45, ptr.count);	
        assertEquals("F", ptr.pf);	
        assertEquals("binName", ptr.binName);	
	}
	
    //stdf.add(new TestSynopsisRecord(snum++, dnum, (short) 1, (short) 0, 'T', 10L, 11L, 12L, 13L, "testName",   
    //	"sequencerName", "testLabel", EnumSet.noneOf(TestOptFlag_t.class), 3.0f, 1.0f, 2.2f, 3.3f, 4.4f));	
	@Test
	public void testU()
	{
		StdfRecord r = list.get(23);
		assertTrue(r instanceof TestSynopsisRecord);
		assertFalse(r.isTestRecord());
		TestSynopsisRecord ptr = (TestSynopsisRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteNumber);	
        assertEquals('T', ptr.testType);	
        assertEquals(10L, ptr.testNumber);	
        assertEquals(11L, ptr.numExecs);	
        assertEquals(12L, ptr.numFailures);	
        assertEquals(13L, ptr.numAlarms);	
        assertEquals("testName", ptr.testName);	
        assertEquals("sequencerName", ptr.sequencerName);	
        assertEquals("testLabel", ptr.testLabel);	
        assertEquals(0, ptr.optFlags.size());	
        assertEquals(3.0f, ptr.testTime, 5);	
        assertEquals(1.0f, ptr.testMin, 5);	
        assertEquals(2.2f, ptr.testMax, 5);	
        assertEquals(3.3f, ptr.testSum, 5);	
        assertEquals(4.4f, ptr.testSumSquares, 5);	
	}
	
    //stdf.add(new WaferConfigurationRecord(snum++, dnum, 6.0f, 3.3f, 4.4f, (short) 1, 'L', (short) 1, (short) 2, '5', '5'));
	@Test
	public void testV()
	{
		StdfRecord r = list.get(24);
		assertTrue(r instanceof WaferConfigurationRecord);
		assertFalse(r.isTestRecord());
		WaferConfigurationRecord ptr = (WaferConfigurationRecord) r;
        assertEquals(6.0f, ptr.waferSize, 5);	
        assertEquals(3.3f, ptr.dieHeight, 5);	
        assertEquals(4.4f, ptr.dieWidth, 5);	
        assertEquals(1, ptr.units);	
        assertEquals('L', ptr.flatOrient);	
        assertEquals(1, ptr.centerX);	
        assertEquals(2, ptr.centerY);	
        assertEquals('5', ptr.posX);	
        assertEquals('5', ptr.posY);	
	}
	
    //stdf.add(new WaferInformationRecord(snum++, dnum, (short) 1, (short) 0, 1000L, "waferID"));
	@Test
	public void testW()
	{
		StdfRecord r = list.get(25);
		assertTrue(r instanceof WaferInformationRecord);
		assertFalse(r.isTestRecord());
		WaferInformationRecord ptr = (WaferInformationRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteGroupNumber);	
        assertEquals(1000L, ptr.startDate);	
        assertEquals("waferID", ptr.waferID);	
	}
	
	//stdf.add(new WaferResultsRecord(snum++, dnum, (short) 1, (short) 0, 1000L, 1L, 2L, 0L, 1L, 0L,
	//	"waferID", "fabWaferID", "waferFrameID", "waferMaskID", "userWaferDesc", "execWaferDesc"));
	@Test
	public void testX()
	{
		StdfRecord r = list.get(26);
		assertTrue(r instanceof WaferResultsRecord);
		assertFalse(r.isTestRecord());
		WaferResultsRecord ptr = (WaferResultsRecord) r;
        assertEquals(1, ptr.headNumber);	
        assertEquals(0, ptr.siteGroupNumber);	
        assertEquals(1000L, ptr.finishDate);	
        assertEquals(1L, ptr.partCount);	
        assertEquals(2L, ptr.retestCount);	
        assertEquals(0L, ptr.abortCount);	
        assertEquals(1L, ptr.passCount);	
        assertEquals(0L, ptr.functionalCount);	
        assertEquals("waferID", ptr.waferID);	
        assertEquals("fabWaferID", ptr.fabWaferID);	
        assertEquals("waferFrameID", ptr.waferFrameID);	
        assertEquals("waferMaskID", ptr.waferMaskID);	
        assertEquals("userWaferDesc", ptr.userWaferDesc);	
        assertEquals("execWaferDesc", ptr.execWaferDesc);	
	}
	

		
		
		
		
		
		
		
		

}
