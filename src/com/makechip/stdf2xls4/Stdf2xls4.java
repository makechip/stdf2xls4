package com.makechip.stdf2xls4;
/**
 * Version history:
 * 
 * 4.0.2 12/21/16:
 * Made modifiers work for Datalog Text Records.
 * 
 * 4.0.3 1/5/17:
 * Fixed bug in DatalogTestRecord where a date-string was interpreted
 * to be an integer, but was too large to fit in an integer.
 * 
 * 4.0.4 1/12/17:
 * Improve row height for test names when test names go horizontally across the page.
 *
 * 4.0.5 1/24/17:
 * Add legend for invalid value
 * Fix column width for overly large values
 * Increase row height for test names (when they go horizontally).
 * 
 * 4.0.6 1/29/17:
 * Fixed null pointer bug due to missing temperature (StdfAPI.java line 282) 
 * Fixed more null pointer bugs in StdfAPI, and also MultiParametricTestHeader.
 * 
 * 4.0.7 2/1/17:
 * Fixed bug that caused DatalogTestRecords to have ever-increasing duplicate numbers.
 * 
 * 4.0.8 2/2/17:
 * Fixed null-pointer issue caused by incorrect handling of default values
 * 
 * 4.0.9 2/4/17:
 * Fixed units where scaling created new units like "kUA"
 * Improved column widths for non-rotated sheets.
 * 
 * 4.1.0 2/13/17:
 * Improved help message when running with no command line options
 * Added -c option to allow up to 16384 columns.
 *
 * 4.1.1 2/17/17:
 * Fixed major bug that caused only one pin to be output for MPRs.
 * 
 * 4.1.2 2/20/17:
 * Fixed bug that showed up in 4.1.1 where values did not show up 
 * for each pin in MPRs because only one spreadsheet coordinate
 * was being used per MPR.
 * Added start and stop dates to page header
 * Fixed label typo for "Y" header on wafersort spreadsheets. 
 * Removed all debug print statements
 * 
 * 4.1.3 3/5/17:
 * Fixed --no-overwrite option
 * 
 * 4.1.4 3/6/17:
 * Fixed bug in units scaling algorithm.
 * 
 * 4.1.5 3/13/17
 * Fixed missing initialization of IdentityFactory in XY.java
 * 
 * 4.1.6 3/22/17
 * Fixed deletion of dummy sheets when sheet index was less than 0.
 * 
 * 4.1.7 4/26/17
 * Fixed another bug in units scaling
 *
 * 4.1.8 5/18/17
 * Fixed Missing data issue with MRR and PMR records; Fixed all known multi-site issues. 
 * 
 * 4.1.9 5/25/17
 * Fixed issue with serial number not being used when it is in a datalog text record.
 * 
 * 4.1.10 5/25/17
 * Fixed issue with differing number of tests in multiple STDF files, and some tests missing high and low limits.
 * 
 * 4.1.11 5/25/17 
 * Fixed version error in 4.1.10 jar file
 * 
 * 4.1.12 6/23/17
 * Added feature to handle case when MPR has no rtnIndex array, and there is no default value for this field.
 * 
 * 4.2.0 8/27/17
 * Removed timestamped files option, and replaced with using the test
 * date in the MIR.  If there is one STDF file per device, this will
 * give the test date for each device.
 * 
 * 4.2.1 8/27/17
 * Fixed two bugs in the timestamp.  The month was off by one because
 * Calandar.java starts counting months from 0, not 1.  Also the hour
 * was derived from a 12-hour clock, and now it is derived from a 24-hour clock.
 * Also corrected timezone issue.
 * 
 * 4.2.2 8/29/17
 * Fixed timezone issue with start and stop dates in the page header.
 * 
 * 4.2.3 8/30/17
 * Removed printing of static fields from StdfRecord toString method.
 * Fixed broken DatalogTestRecords. Due to an incorrect default site number
 * the datalog test records did not show up in the spreadsheet.
 * Fixed bug in TestID creation.  Duplicates greater than 1 were
 * getting assigned a duplicate value of 1. 
 * 
 * 4.2.4 8/31/17
 * Fixed bug in data header row height calculation.
 * 
 * 4.2.5 9/26/17
 * Fixed bug where individual MPR failures did not get highlighted in red.
 * 
 * 4.2.6 10/5/17
 * When -t option is used, data is sorted by timestamp
 * 
 * 4.2.7 10/17/17
 * Made legacy header parsing more forgiving to silently
 * ignore headers with incorrect format.
 * 
 * 4.2.8 11/23/17
 * Fixed some bugs with dynamic limits
 * 
 * 4.2.9 12/4/17
 * Used MIR TEST_TEMP field to set the default temperature in case the datalog does not specify one.
 * Used MIR TEST_COD field to ste the default step in case the datalog does not specify one.
 * 
 * 4.3.0 12/19/17
 * Fixed spreadsheet corruption error caused by multiple cell merges in the header block.
 * 
 * 4.3.1 12/21/17
 * 
 * 4.3.2 1/8/18
 *  Fix regression where test names cells are not merged.
 *  
 * 4.3.3 1/18/18
 *  Fix another regresion (page title block not correct) introduced in 4.3.0
 * 
 * 4.3.4 1/19/18
 *  Fix another regresion (page title block not correct) introduced in 4.3.0
 *  
 * 4.3.5
 * 
 * 4.3.6
 * 
 * 4.3.7 11/14/18 Added -z option to force all test numbers to zero.
 * Fixed Text record serial number override to work with multiple sites.
 * 
 */

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jxl.read.biff.BiffException;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.SpreadsheetWriter;
import com.makechip.stdf2xls4.excel.XSSFSpreadsheet;
import com.makechip.stdf2xls4.excel.HSSFSpreadsheet;
import com.makechip.stdf2xls4.excel.JxlSpreadsheet;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.ui.MainWindow;
import com.makechip.util.Log;

public class Stdf2xls4
{
	public static final String VERSION = "4.3.7";
	private CliOptions options;

	public Stdf2xls4(CliOptions options)
	{
		this.options = options;
	}
	
	public void run() throws IOException, InvalidFormatException, BiffException
	{
		StdfAPI api = new StdfAPI(options);
		api.initialize();
		Spreadsheet ss = null;
		if (options.xlsName != null)
		{
			if (options.xlsName.toString().endsWith(".xlsx"))
			{
				ss = new XSSFSpreadsheet();
			}
			else
			{
				if (options.useJxl) ss = new JxlSpreadsheet();
				else ss = new HSSFSpreadsheet();
			}
			SpreadsheetWriter ssw = new SpreadsheetWriter(options, api, ss);
			ssw.generate();
		}
	}

	public static void main(String[] args)
	{
		CliOptions options = null;
		try { options = new CliOptions(args); }
		catch (Exception e)
		{
			Log.msg(e.getMessage());
			System.exit(1);
		}
        if (!options.isOptionsValid())
        {
        	Log.msg(options.getMessage());
        	System.exit(1);
        }
        if (options.getMessage() != null && !options.getMessage().equals(""))
        {
        	Log.msg(options.getMessage());
        	System.exit(0);
        }
        if (options.gui)
        {
			MainWindow mw = new MainWindow(options);
			javax.swing.SwingUtilities.invokeLater(mw);
			//mw.run();
        }
        else
        {
            Stdf2xls4 pgm = new Stdf2xls4(options);
            try { pgm.run(); }
            catch (Exception e) { Log.fatal(e); }
            System.exit(0);
        }
	}

}
