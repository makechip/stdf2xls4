package com.makechip.stdf2xls4;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.xlsx.SpreadSheetWriter1;
import com.makechip.stdf2xls4.xlsx.SpreadSheetWriter2;
import com.makechip.util.Log;

public class Stdf2xls4
{
	private CliOptions options;

	public Stdf2xls4(CliOptions options)
	{
		this.options = options;
	}
	
	public void run() throws RowsExceededException, WriteException
	{
		StdfAPI api = new StdfAPI(options);
		api.initialize();
		if (options.dumpTests)
		{
			api.initialize();
			Log.msg(api.toString());
		}
		SpreadSheetWriter ssw = null;
		if (options.xlsName != null)
		{
		    if (options.rotate) ssw = new SpreadSheetWriter2(options, api);	
		    else ssw = new SpreadSheetWriter1(options, api);
		}
		ssw.generate();
	}

	public static void main(String[] args)
	{
		CliOptions options = new CliOptions(args);
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
        Stdf2xls4 pgm = new Stdf2xls4(options);
        try { pgm.run(); }
        catch (Exception e) { Log.fatal(e); }
        System.exit(0);
	}

}