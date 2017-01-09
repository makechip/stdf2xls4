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
 * 
 * 
 * 
 * 
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
	public static final String VERSION = "4.0.3";
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
