package com.makechip.stdf2xls4;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.makechip.stdf2xls4.CliOptions;

public class OptionsTest
{

	@Test
	public void test1()
	{
		CliOptions options = new CliOptions(new String[] { "-x", "x.xls", "tests/com/makechip/stdf2xls4/stdf/resources/d10_1.std" });
		assertTrue(options.isOptionsValid());
		assertEquals(new File("x.xls"), options.xlsName);
		assertFalse(options.dump);
		assertTrue(options.noWrapTestNames);
		assertFalse(options.noOverwrite);
		assertFalse(options.showDuplicates);
		assertFalse(options.onePage);
		assertFalse(options.forceHdr);
		assertEquals(3, options.precision);
		assertFalse(options.dontSkipSearchFails);
		assertFalse(options.rotate);
		assertFalse(options.msMode);
		assertFalse(options.dynamicLimits);
		assertEquals(1, options.stdfFiles.size());
	}
	
	@Test
	public void test2()
	{
		CliOptions options = new CliOptions(new String[] { "-d", "-p", "5", "-n", "tests/com/makechip/stdf2xls4/stdf/resources/d10_1.std" });
		assertTrue(options.isOptionsValid());
	    assertEquals(null, options.xlsName);
	    assertTrue(options.dump);
	    assertFalse(options.noWrapTestNames);
		assertEquals(5, options.precision);
	}
	
	@Test 
	public void test3()
	{
		CliOptions options = new CliOptions(new String[] { "-h" });
		assertTrue(options.isOptionsValid());
	}
	
	@Test
	public void test4()
	{
		CliOptions options = new CliOptions(new String[] { "-d", "-p", "0", "tests/com/makechip/stdf2xls4/stdf/resources/d10_1.std" });
		assertFalse(options.isOptionsValid());
		assertEquals("Error: precision must be greater that zero and less than, or equal to 12", options.getMessage());
		CliOptions options2 = new CliOptions(new String[] { "-d", "-p", "13", "tests/com/makechip/stdf2xls4/stdf/resources/d10_1.std" });
		assertFalse(options2.isOptionsValid());
		assertEquals("Error: precision must be greater that zero and less than, or equal to 12", options2.getMessage());
		
	}
	
	@Test
	public void test5()
	{
		CliOptions options = new CliOptions(new String[] { "-d" });
		assertFalse(options.isOptionsValid());
		assertEquals("Error: No STDF files have been specified", options.getMessage());
		
	}
	
	@Test
	public void test6()
	{
		CliOptions options = new CliOptions(new String[] { "-d", "x.std" });
		assertFalse(options.isOptionsValid());
		assertEquals("Error: The following STDF files are not found: x.std", options.getMessage());
		
		CliOptions.main(new String[] { } );
	}
	
}
