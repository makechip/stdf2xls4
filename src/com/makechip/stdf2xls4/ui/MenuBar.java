package com.makechip.stdf2xls4.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.Modifier;
import com.makechip.util.Log;
import com.makechip.util.widgets.console.widget.ConsolePanel;
import com.makechip.util.widgets.console.widget.GuiConsole;

public class MenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = -7935982234009232527L;
	private FileMenu fileMenu;
	private OptionsMenu optionsMenu;
	private JButton run;
	private GuiConsole io;
	private ConsolePanel console;
	private JFrame parent;
	private StdfOptionsDialog stdfDialog;
	private SpreadsheetOptionsDialog ssDialog;
	private ModifyRecordsDialog modDialog;
	private CliOptions options;
	
	List<File> getStdfFiles()
	{
	    File[] fs = fileMenu.getStdfFiles();
	    List<File> l = new ArrayList<File>();
	    Arrays.stream(fs).forEach(f -> l.add(f));
	    return(l);
	}

	File getXlsFile()
	{
	    return(ssDialog.getXlsFile());	
	}
	
	List<Modifier> getModifiers()
	{
	    return(modDialog.getModifiers());	
	}
	
	boolean useJxl()
	{
	    return(ssDialog.useJxl());	
	}
	
	File getDumpFile()
	{
	    return(stdfDialog.getDumpFile());	
	}
	
	boolean getNoWrap()
	{
	    return(ssDialog.getNoWrap());	
	}
	
	boolean getNoOverwrite()
	{
	    return(ssDialog.getNoOverwrite()); 	
	}
	
	boolean getSort()
	{
	    return(ssDialog.getSort());	
	}
	
	boolean getOnePage()
	{
	    return(ssDialog.getOnePage());	
	}
	
	int getPrecision()
	{
	    return(ssDialog.getPrecision());	
	}
	
	boolean getNoSkip()
	{
	    return(stdfDialog.dontSkipSearches());	
	}
	
	boolean getRotate()
	{
	    return(ssDialog.getRotate());	
	}
	
	boolean getDynamicLimits()
	{
	    return(stdfDialog.useDynamicLimits());	
	}
	
	Character getDelimiter()
	{
	    return(stdfDialog.getDelimiter());	
	}
	
	File getLogoFile()
	{
	    return(ssDialog.getLogoFile());	
	}
	
	public MenuBar(JFrame parent, MainWindow main, ConsolePanel console, CliOptions options)
	{
		this.console = console;
		this.parent = parent;
		this.options = options;
		stdfDialog = new StdfOptionsDialog(parent, options);
		ssDialog = new SpreadsheetOptionsDialog(parent, options);
		modDialog = new ModifyRecordsDialog(parent, options);
        io = console.getConsole();
		fileMenu = new FileMenu(parent);
		optionsMenu = new OptionsMenu();
		fileMenu.addActionListener(this);
		optionsMenu.addActionListener(this);
		add(fileMenu);
		add(optionsMenu);
		run = new JButton("Run");
		run.addActionListener(main);
		run.setEnabled(stdfDialog.getDumpFile() != null || ssDialog.getXlsFile() != null);
		run.addActionListener(e -> io.echo(e.getActionCommand()));
		if (options.xlsName == null && options.dumpFile == null) run.setEnabled(false);
		add(run);
	}
	
	class StdfFileFilter extends FileFilter
	{
		@Override
		public boolean accept(File f)
		{
			String s = f.toString();
			return(s.endsWith(".std") || s.endsWith(".STD") || s.endsWith(".stdf") || s.endsWith(".STDF"));
		}

		@Override
		public String getDescription()
		{
			return("All STDF files");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    io.echo(e.getActionCommand());	
	}

	class FileMenu extends JMenu implements ActionListener
	{
		private static final long serialVersionUID = -7632766581746312459L;
	    private Process p1;
	    private Process p2;
		JMenuItem selectStdf;
		JMenuItem launchLibreOffice;
		JMenuItem launchMsOffice;
		JMenuItem exit;
		private JFileChooser fileChooser;

		public FileMenu(JFrame parent)
		{
			super("File");
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.addActionListener(this);
			fileChooser.setFileFilter(new StdfFileFilter());
		    selectStdf = new JMenuItem("Select STDF Files");	
		    selectStdf.addActionListener(e -> fileChooser.showOpenDialog(parent));
		    launchLibreOffice = new JMenuItem("Launch Libreoffice");
		    launchLibreOffice.addActionListener(e -> runLibreOffice());
		    launchLibreOffice.setEnabled(options.xlsName != null && options.xlsName.exists());
		    launchMsOffice = new JMenuItem("Launch MS Office");
		    launchMsOffice.addActionListener(e -> runMsOffice());
		    launchMsOffice.setEnabled(options.xlsName != null && options.xlsName.exists() && isWindoze());
		    selectStdf.setActionCommand("SELECT STDF");
		    exit = new JMenuItem("Exit");
		    exit.addActionListener(e -> parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING)));
		    add(selectStdf);
		    add(launchLibreOffice);
		    add(launchMsOffice);
		    add(exit);
		}
		
		public File[] getStdfFiles()
		{
			return(fileChooser.getSelectedFiles());
		}
		
		private void runLibreOffice()
		{
			try 
			{ 
				if (p1 == null || !p1.isAlive())
				{
				    p1 = Runtime.getRuntime().exec("/usr/bin/libreoffice5.0 " + options.xlsName);
				}
		    }
		    catch (Exception e) { throw new RuntimeException(e); }	
		}

		private void runMsOffice()
		{
			try 
			{ 
				if (p2 == null || !p2.isAlive())
				{
				    p2 = Runtime.getRuntime().exec("cmd /c start " + options.xlsName);
				}
		    }
		    catch (Exception e) { throw new RuntimeException(e); }	
		}

		@Override
		public void addActionListener(ActionListener a)
		{
			launchLibreOffice.addActionListener(a);
			launchMsOffice.addActionListener(a);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
			{
				File[] fs = fileChooser.getSelectedFiles();
				Arrays.stream(fs).forEach(s -> io.echo(s.toString()));
			}
		}

	}

	class OptionsMenu extends JMenu
	{
		private static final long serialVersionUID = 4859293688041251934L;
		private JMenuItem stdfOptions;
		private JMenuItem spreadsheetOptions;
		private JMenuItem modifyRecords;
		private JMenuItem setSize1200x800;
		private JMenuItem setSize800x533;
		private JMenuItem setSize600x400;
		private JMenu lafSubmenu;

		public OptionsMenu()
		{
			super("Options");
			stdfOptions = new JMenuItem("STDF Options");
			stdfOptions.addActionListener(e -> stdfDialog.setVisible(true));
			spreadsheetOptions = new JMenuItem("Spreadsheet Options");
			spreadsheetOptions.addActionListener(e -> ssDialog.setVisible(true));
			modifyRecords = new JMenuItem("Modify Records");
			modifyRecords.addActionListener(e -> modDialog.setVisible(true));
			setSize1200x800 = new JMenuItem("Set Size Large");
			setSize1200x800.addActionListener(e -> changeSize(new Dimension(1200, 800)));
			setSize800x533 = new JMenuItem("Set Size Medium");
			setSize800x533.addActionListener(e -> changeSize(new Dimension(800, 533)));
			setSize600x400 = new JMenuItem("Set Size Small");
			setSize600x400.addActionListener(e -> changeSize(new Dimension(600, 400)));
			add(stdfOptions);
			add(spreadsheetOptions);
			add(modifyRecords);
			add(setSize1200x800);
			add(setSize800x533);
			add(setSize600x400);
			lafSubmenu = new JMenu("LAF");
			add(lafSubmenu);
			Arrays.stream(UIManager.getInstalledLookAndFeels()).forEach(laf ->
			{
				JMenuItem m = new JMenuItem(laf.getName());
				m.addActionListener(e -> setLAF(laf.getClassName()));
				lafSubmenu.add(m);
			});
		}
		
		private void setLAF(String lafClassName)
		{
			try 
			{ 
				UIManager.setLookAndFeel(lafClassName);  
				SwingUtilities.updateComponentTreeUI(getRootPane());
			}
			catch (Exception e) { Log.msg(e.getMessage()); }
		}
		
		private void changeSize(Dimension size)
		{
			console.setPreferredSize(size);
		    console.invalidate();
		    parent.revalidate();
		    parent.pack();
		}

		@Override
		public void addActionListener(ActionListener a)
		{
			stdfOptions.addActionListener(a);
			spreadsheetOptions.addActionListener(a);
		}

	}
	
	private boolean isWindoze()
	{
		String s = System.getProperty("os.name");
		return(s.contains("Windows"));
	}
}
