/**
 * 
 */
package com.makechip.stdf2xls4.stdfapi;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import org.junit.Test;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdfapi.DeviceHeader;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestResult;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.stdfapi.TestHeader;
import com.makechip.stdf2xls4.stdfapi.TestResult;
import com.makechip.util.Log;

/**
 * @author eric
 *
 */
public class StdfApiTest1
{

	@Test
	public void testStdfAPI1()
	{
		CliOptions options = new CliOptions(new String[] { "-x", "x.xls", "tests/com/makechip/stdf2xls4/stdf/resources/d10_1.std" });
		StdfAPI api = new StdfAPI(options);
		try { api.initialize(); }
		catch (Exception e) { Log.fatal(e); }
		Set<PageHeader> ph = api.getPageHeaders();
		assertEquals(ph.size(), 1);
		PageHeader[] pha = ph.toArray(new PageHeader[1]);
		PageHeader phdr = pha[0];
		assertEquals(phdr.get("TEST PROGRAM:"), "Ad7226 Rev A");
		assertEquals(phdr.get("LOT #:"), "1005101A");
		assertEquals(phdr.get("STEP #:"), "3.00");
		assertEquals(phdr.get("TESTER_TYPE:"), "D-10");
		assertEquals(phdr.get("TESTER:"), "d10-1");
		assertEquals(phdr.get("CUSTOMER:"), "BAE");
		assertEquals(phdr.get("DEVICE NUMBER:"), "1360PD1196-001(5962-878201RA)AD7226TQ/883B");
		assertEquals(phdr.get("TEMPERATURE:"), "25c");
		assertEquals(phdr.get("LOADBOARD:"), "151341");
		Set<DeviceHeader> dh = api.getDeviceHeaders(phdr);
		assertEquals(dh.size(), 1);
		DeviceHeader[] dha = dh.toArray(new DeviceHeader[1]);
		DeviceHeader dhdr = dha[0];
		assertEquals(dhdr.snxy.getSerialNumber(), "6");
		assertEquals(dhdr.hwBin, 1);
		assertEquals(dhdr.swBin, 1);
		assertEquals(dhdr.temperature, "25c");
		assertEquals(dhdr.fail, false);
		List<TestHeader> thl = api.getTestHeaders(phdr, dhdr);
		TestHeader th1 = thl.get(0);
		TestResult tr1 = api.getRecord(phdr, dhdr, th1);
		assertEquals(tr1.pass(), true);
		assertFalse(tr1.alarm());
		assertFalse(tr1.unreliable());
		assertFalse(tr1.fail());
		assertFalse(tr1.timeout());
		assertFalse(tr1.abort());
		assertFalse(tr1.noPassFail());
		ParametricTestHeader th2 = (ParametricTestHeader) thl.get(1);
		ParametricTestResult tr2 = (ParametricTestResult) api.getRecord(phdr, dhdr, th2);
		assertEquals(th2.testNumber, 100500L);
		assertEquals(th2.testName, "VOUTA_Continuity");
		assertEquals(th2.units, "V");
		assertEquals(th2.loLimit, -1.300f, 3);
		assertEquals(th2.hiLimit, -0.400f, 3);
		assertEquals(tr2.result, -0.8085870742797852f, 9);
	    ParametricTestHeader thn = (ParametricTestHeader) thl.get(thl.size() - 1);	
	    ParametricTestResult trn = (ParametricTestResult) api.getRecord(phdr, dhdr, thn);
	    assertEquals(thn.testNumber, 213000);
	    assertEquals(thn.testName, "TAS_TAH_TDS_TDH_D7_CHD_MAX");
	    assertEquals(thn.units, "V");
	    assertEquals(thn.loLimit, 3.685f, 3);
	    assertEquals(thn.hiLimit, 9.375f, 3);
	    assertEquals(trn.result, 6.250508785247803f, 9);
		
	}
	
	

}
