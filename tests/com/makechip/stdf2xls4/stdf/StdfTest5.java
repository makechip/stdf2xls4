package com.makechip.stdf2xls4.stdf;

import static org.junit.Assert.*;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdfapi.DefaultValueDatabase;
import com.makechip.stdf2xls4.stdfapi.TestID;
import com.makechip.stdf2xls4.stdfapi.TestIdDatabase;
import com.makechip.util.Log;

/**
 * @author eric
 *
 */
public class StdfTest5
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
		//List<AuditTrailRecord> atrs = new ArrayList<AuditTrailRecord>();
		//atrs.add(new AuditTrailRecord(tdb, dvd, 100000000L, "cmdline"));
		MasterInformationRecord mir = new MasterInformationRecord(tdb, dvd, 1000L, 2000L, (short) 1,
			'A', 'B', 'C', 100, 'D', "lotID",
			"partType", "nodeName", "testerType", "jobName", "jobRevisionNumber",  "sublotID", "operatorName",
			"execSoftware", "execSoftwareVersion", "stepCode", "temperature", "userText", "auxDataFile",
			"packageType", "familyID", "dateCode", "facilityID", "floorID","fabID", "frequency", "specName",
			"specVersion", "flowID", "setupID", "designRevision", "engLotID", "romCodeID", "testerSerialNumber",
			"supervisorID", 1234L);
		//RetestDataRecord srdr = new RetestDataRecord(tdb, dvd, new int[] { 1, 2, 3, 4 });
		//List<SiteDescriptionRecord> sdrs = new ArrayList<SiteDescriptionRecord>();
		//sdrs.add(new SiteDescriptionRecord(tdb, dvd, (short) 1, (short) 2, new int[] { 0 },
	//		"handlerType", "handlerID", "probeCardType", "probeCardID", "loadboardType", "loadboardID",
	//		"dibBoardType", "dibBoardID", "ifaceCableType", "ifaceCableID", "contactorType", "contactorID",
	//		"laserType", "laserID", "equipType", "equipID"));
		stdf = new StdfWriter(far, null, mir, null, null);
		Path p = FileSystems.getDefault().getPath("x.stdf");
		stdf.write(p.toFile());
        Files.delete(p);
		rdr = new StdfReader(tdb, p.toFile());
		rdr.read(stdf.getBytes());
		list = rdr.getRecords();
		Log.msg("list.size(); = " + list.size());
	}
	
	@Test
	public void testA()
	{
		assertEquals(2, list.size());
		String s = stdf.toString();
	    assertTrue(s.contains("StdfWriter ["));	
	    assertTrue(s.contains("far="));	
	    assertFalse(s.contains("atrs="));	
	    assertTrue(s.contains("mir="));	
	    assertFalse(s.contains("rdr="));	
	    assertFalse(s.contains("sdrs="));	
	    assertTrue(s.contains("records="));	
	}

	@Test
	public void testB()
	{
		TestID id1 = TestID.createTestID(tdb, 1, "x");
		assertEquals(-1, id1.getInstanceCount());
		TestID.PinTestID id2 = TestID.PinTestID.getTestID(tdb, id1, "pin1");
		assertEquals(-1, id2.getInstanceCount());
		String s = id2.toString();
		assertTrue(s.contains("PinTestID ["));
		assertTrue(s.contains("pin=pin1"));
		assertTrue(s.contains("id="));
		assertTrue(s.contains(id1.toString()));
	}

}
