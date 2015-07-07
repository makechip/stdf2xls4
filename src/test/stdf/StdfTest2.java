package test.stdf;

import static org.junit.Assert.*;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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
public class StdfTest2
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
		sdrs.add(new SiteDescriptionRecord(tdb, dvd, (short) 1, (short) 2, new int[] { 0 },
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
		stdf.add(new FunctionalTestRecord(tdb, dvd, 3, (short) 2, (short) 1, (byte) 0,
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
		stdf.add(new MultipleResultParametricRecord(tdb, dvd, 22L, (short) 1, (short) 0, (byte) 0,
			(byte) 0, new byte[] { 1, 2 }, new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
			"text", "alarmName", EnumSet.noneOf(OptFlag_t.class), (byte) 0, (byte) 1, (byte) 2, 1.0f, 3.0f,
			0.0f, 0.0f, new int[] { 0, 1 }, "units", "unitsIn", "resFmt", "llmFmt", "hlmFmt", 3.0f, 4.0f));
		stdf.add(new ParametricTestRecord(tdb, dvd, 44L, (short) 1, (short) 0, (byte) 0,
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
		stdf.add(new EndProgramSectionRecord(tdb, dvd));
		stdf.add(new FunctionalTestRecord(tdb, dvd, 3, (short) 2, (short) 1, (byte) 0));
		GenericDataRecord.Data d3 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U_1, 0), new Short((short)33));
		GenericDataRecord.Data d4 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U_2, 0), new Integer(33));
		GenericDataRecord.Data d5 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.U_4, 0), new Long(33L));
		GenericDataRecord.Data d6 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I_1, 0), new Byte((byte) 33));
		GenericDataRecord.Data d7 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.I_2, 0), new Short((short) 33));
		GenericDataRecord.Data d8 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.R_4, 0), new Float(33.0f));
		GenericDataRecord.Data d9 = new GenericDataRecord.Data(new GenericDataRecord.PadData(Data_t.C_N, 0), "string");
		List<GenericDataRecord.Data> lgd2 = new ArrayList<GenericDataRecord.Data>(); 
		lgd2.add(d3);
		lgd2.add(d4);
		lgd2.add(d5);
		lgd2.add(d6);
		lgd2.add(d7);
		lgd2.add(d8);
		lgd2.add(d9);
		stdf.add(new GenericDataRecord(tdb, dvd, lgd2));
		stdf.add(new DatalogTextRecord(tdb, dvd, "TEXT_DATA : testName : 30.0 (fA) : 12 : 1 : 3"));
		stdf.add(new DatalogTextRecord(tdb, dvd, "TEXT_DATA : S/N : 30"));
		Path p = FileSystems.getDefault().getPath("x.stdf");
		stdf.write(p.toFile());
        Files.delete(p);
		rdr = new StdfReader(tdb, 20150707123333L);
		rdr.read(stdf.getBytes());
		list = rdr.getRecords();
	}
	
	@Test
	public void testA()
	{
		StdfRecord r = list.get(2);
		assertTrue(r instanceof MasterInformationRecord);
		MasterInformationRecord mir = (MasterInformationRecord) r;
		assertEquals(20150707123333L, mir.timeStamp);
	}
	
	@Test
	public void testB()
	{
		StdfRecord r = list.get(31);
		assertTrue(r instanceof DatalogTextRecord);
		DatalogTextRecord dtr = (DatalogTextRecord) r;
		assertEquals("TEXT_DATA : S/N : 30", dtr.text);
	}


}
