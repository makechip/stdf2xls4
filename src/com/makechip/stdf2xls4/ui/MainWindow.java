package com.makechip.stdf2xls4.ui;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.makechip.util.Log;
import com.makechip.util.widgets.console.CmdStatus_t;
import com.makechip.util.widgets.console.widget.ConsoleListener;
import com.makechip.util.widgets.console.widget.ConsolePanel;
import com.makechip.util.widgets.console.widget.GuiConsole;

public class MainWindow implements Runnable, ConsoleListener
{
	private MenuBar mb;
	private ConsolePanel console;
	private GuiConsole io;

	public MainWindow()
	{
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
		mb = new MenuBar(frame, console);
        frame.setJMenuBar(mb);
 
        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
        frame.getContentPane().add(console);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) 
    {
    	MainWindow mw = new MainWindow();
        javax.swing.SwingUtilities.invokeLater(mw);
    }

	@Override
	public CmdStatus_t consoleCommand(String cmd)
	{
		if (!cmd.equals("")) io.echo(cmd);
		return null;
	}

}
