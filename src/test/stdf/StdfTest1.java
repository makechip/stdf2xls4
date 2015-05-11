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
import com.makechip.util.Log;

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
		stdf.add(new PinMapRecord(false, snum++, dnum, 2, 3, "channelName", "physicalPinName", "logicalPinName", (short) 1, (short) 0));
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
	public void test1()
	{
		StdfReader rdr = new StdfReader();
		rdr.read(stdf.getBytes());
		List<StdfRecord> list = rdr.stream().map(RecordBytes::createRecord).collect(Collectors.toList());
		assertEquals(23, list.size());
		stack = new Stack<StdfRecord>();
		Stack<StdfRecord> tmp = new Stack<StdfRecord>();
		list.stream().forEach(p -> tmp.push(p));
		while (!tmp.empty()) stack.push(tmp.pop());
	}

    @Test
    public void test2()
    {
    	//Log.msg("stack = " + stack);
    	StdfRecord r = stack.pop();
    	assertTrue(r instanceof FileAttributesRecord);
	    FileAttributesRecord far = (FileAttributesRecord) r;
	    assertEquals(Cpu_t.PC, far.getCpuType());
	    assertEquals(4, far.getStdfVersion());
	    assertEquals(0, r.getSequenceNumber());
    }
    
	@Test
	public void test3()
	{
        StdfRecord r = stack.pop();
        assertTrue(r instanceof AuditTrailRecord);
        AuditTrailRecord atr = (AuditTrailRecord) r;
        assertEquals(1, atr.getSequenceNumber());
        assertEquals((new Date(100000000L * 1000L).toString()), atr.getDate());
        assertEquals("cmdline", atr.getCmdLine());
	}

   
		
		
		
		
		
		
		
		

}
