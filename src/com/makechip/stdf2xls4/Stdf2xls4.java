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
	public static final String VERSION = "4.1.8";
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
