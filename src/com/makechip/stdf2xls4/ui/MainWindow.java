package com.makechip.stdf2xls4.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.HSSFSpreadsheet;
import com.makechip.stdf2xls4.excel.JxlSpreadsheet;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.SpreadsheetWriter;
import com.makechip.stdf2xls4.excel.XSSFSpreadsheet;
import com.makechip.stdf2xls4.stdf.Modifier;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.util.Log;
import com.makechip.util.widgets.console.CmdStatus_t;
import com.makechip.util.widgets.console.widget.ConsoleListener;
import com.makechip.util.widgets.console.widget.ConsolePanel;
import com.makechip.util.widgets.console.widget.GuiConsole;

public class MainWindow implements Runnable, ConsoleListener, ActionListener
{
	private MenuBar mb;
	private ConsolePanel console;
	private GuiConsole io;
	private CliOptions options;
	private CliOptions newOptions;

	public MainWindow(CliOptions options)
	{
		this.options = options;
        console = new ConsolePanel();
        console.setPreferredSize(new Dimension(600, 400));
        io = console.getConsole();
        io.addConsoleListener(this);
	}

    public void run() 
    {
    	Arrays.stream(UIManager.getInstalledLookAndFeels()).forEach(s -> Log.msg(s.toString()));
    	try 
    	{ 
    		// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); 
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
        }
    	catch (Exception e) { Log.fatal(e); }
        //Create and set up the window.
        JFrame frame = new JFrame("Stdf2xls4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mb = new MenuBar(frame, this, console, options);
        frame.setJMenuBar(mb);
        frame.getContentPane().add(console);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) 
    {
    	CliOptions options = new CliOptions(args);
    	MainWindow mw = new MainWindow(options);
        javax.swing.SwingUtilities.invokeLater(mw);
    }

	@Override
	public CmdStatus_t consoleCommand(String cmd)
	{
		if (!cmd.equals("")) io.echo(cmd);
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		File xlsFile = mb.getXlsFile();
		List<Modifier> mods = mb.getModifiers();
		boolean useJxl = mb.useJxl();
		File dumpFile = mb.getDumpFile();
		boolean noWrap = mb.getNoWrap();
		boolean noOverwrite = mb.getNoOverwrite();
		boolean sort = mb.getSort();
		boolean onePage = mb.getOnePage();
		int precision = mb.getPrecision();
		boolean noSkip = mb.getNoSkip();
		boolean rotate = mb.getRotate();
		boolean dynLims = mb.getDynamicLimits();
		Character delim = mb.getDelimiter();
		File logoFile = mb.getLogoFile();
		List<String> l = new ArrayList<>();
	    if (xlsFile != null)
	    {
	    	l.add("-x");
	    	l.add(xlsFile.toString());
	    }
	    mods.stream().forEach(m -> { l.add("-m"); l.add("\"" + m.toString() + "\""); });
	    if (useJxl) l.add("-j");
	    if (dumpFile != null) 
	    {
	    	l.add("-d");
	    	l.add(dumpFile.toString());
	    }
	    if (noWrap) l.add("-n");
	    if (noOverwrite) l.add("-o");
	    if (sort) l.add("-s");
	    if (onePage) l.add("-b");
	    l.add("-p");
	    l.add("" + precision);
	    if (noSkip) l.add("-v");
	    if (rotate) l.add("-r");
	    if (dynLims) l.add("-y");
	    if (delim != null) 
	    {
	    	l.add("-a");
	    	l.add("" + delim);
	    }
	    if (logoFile != null) 
	    {
	    	l.add("-l");
	    	l.add(logoFile.toString());
	    }
	    mb.getStdfFiles().stream().forEach(f -> l.add(f.toString()));
	    try
	    {
	    	newOptions = new CliOptions(l.toArray(new String[4]));
	    	Log.msg("allocating API");
	    	StdfAPI api = new StdfAPI(newOptions);
	    	Log.msg("initializing API");
	    	api.initialize();
	    	Spreadsheet ss = null;
	    	Log.msg("Creating spreadsheet xlsName = " + newOptions.xlsName);
	    	if (newOptions.xlsName != null)
	    	{
	    		if (newOptions.xlsName.toString().toUpperCase().endsWith(".XLSX")) ss = new XSSFSpreadsheet();
	    		else
	    		{
	    			if (newOptions.useJxl) ss = new JxlSpreadsheet();
	    			else ss = new HSSFSpreadsheet();
	    		}
	    		Log.msg("creating STDFWriter");
	    		SpreadsheetWriter ssw = new SpreadsheetWriter(newOptions, api, ss);
	    		Log.msg("generating...");
	    		ssw.generate();
	    		Log.msg("Done");
	    	}
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    	console.getConsole().echo(e.getMessage()); 
	    }
	}

}
