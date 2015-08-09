package com.makechip.stdf2xls4.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.makechip.stdf2xls4.CliOptions;
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

	public MenuBar(JFrame parent, ConsolePanel console, CliOptions options)
	{
		this.console = console;
		this.parent = parent;
		stdfDialog = new StdfOptionsDialog(parent, options);
        io = console.getConsole();
		fileMenu = new FileMenu(parent);
		optionsMenu = new OptionsMenu();
		fileMenu.addActionListener(this);
		optionsMenu.addActionListener(this);
		add(fileMenu);
		add(optionsMenu);
		run = new JButton("Run");
		run.setActionCommand("RUN");
		run.addActionListener(e -> io.echo(e.getActionCommand()));
		add(run);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    io.echo(e.getActionCommand());	
	}

	class FileMenu extends JMenu implements ActionListener
	{
		private static final long serialVersionUID = -7632766581746312459L;
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
		    selectStdf = new JMenuItem("Select STDF Files");	
		    selectStdf.addActionListener(e -> fileChooser.showOpenDialog(parent));
		    launchLibreOffice = new JMenuItem("Launch Libreoffice");
		    launchMsOffice = new JMenuItem("Launch MS Office");
		    selectStdf.setActionCommand("SELECT STDF");
		    launchLibreOffice.setActionCommand("LAUNCH LIBREOFFICE");
		    launchMsOffice.setActionCommand("LAUNCH MS OFFICE");
		    exit = new JMenuItem("Exit");
		    exit.addActionListener(e -> parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING)));
		    add(selectStdf);
		    add(launchLibreOffice);
		    add(launchMsOffice);
		    add(exit);
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
			spreadsheetOptions = new JMenuItem("Spreadsheet Options");
			stdfOptions.addActionListener(e -> stdfDialog.setVisible(true));
			spreadsheetOptions.setActionCommand("SPREADSHEET OPTIONS");
			modifyRecords = new JMenuItem("Modify Records");
			modifyRecords.setActionCommand("MODIFY RECORDS");
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
}
