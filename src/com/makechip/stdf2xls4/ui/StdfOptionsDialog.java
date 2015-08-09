package com.makechip.stdf2xls4.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.makechip.stdf2xls4.CliOptions;

public class StdfOptionsDialog extends JDialog
{
	private static final long serialVersionUID = 4732351878549186603L;
	private JTextField dumpName;
	private JCheckBox dontSkip;
	private JCheckBox dynamicLimits;
	private JCheckBox pinSuffix;
	private JCheckBox dump;
	private JButton browse;
	private JComboBox<Character> suffixChar;
	private JFileChooser fileChooser;
	private JButton cancel;
	private JButton apply;
	
	public boolean dontSkipSearches() { return(dontSkip.isSelected()); }
	
	public boolean useDynamicLimits() { return(dynamicLimits.isSelected()); }
	
	public boolean usePinSuffix() { return(pinSuffix.isSelected()); }
	
	public char getDelimiter() { return((char) suffixChar.getSelectedItem()); }
	
	public boolean dumpStdf() { return(dump.isSelected()); }
	
	public File getDumpFile() { return(fileChooser.getSelectedFile()); }
	
	public StdfOptionsDialog(Frame parent, CliOptions options)
	{
		super(parent, "STDF Options", true);
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.addActionListener(e -> processFile(e));
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.NONE;

		dontSkip = new JCheckBox("Don't skip search fails", options.dontSkipSearchFails);
		cs.gridx = 0;
		cs.gridy = 0;
		cs.anchor = GridBagConstraints.WEST;
		cs.gridwidth = 1;
		panel.add(dontSkip, cs);

		dynamicLimits = new JCheckBox("Dynamic Limits", options.dynamicLimits);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(dynamicLimits, cs);

		pinSuffix = new JCheckBox("Pin-suffix for ParametricTestRecords", options.pinSuffix);
		pinSuffix.addActionListener(e -> suffixChar.setEnabled(((JCheckBox) e.getSource()).isSelected()));
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		panel.add(pinSuffix, cs);

		suffixChar = new JComboBox<>(CliOptions.suffixChars);
		suffixChar.setSelectedIndex(getIndex(options.delimiter));
		suffixChar.setEnabled(pinSuffix.isSelected());
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 1;
		panel.add(suffixChar, cs);
		
	    dump = new JCheckBox("Dump ASCII STDF Records to file:");
	    dump.addActionListener(e -> browseEnable(e));
	    cs.gridx = 0;
	    cs.gridy = 3;
	    cs.gridwidth = 1;
	    panel.add(dump, cs);
	
	    dumpName = new JTextField(32);
	    dumpName.setEnabled(dump.isSelected());
	    cs.gridx = 0;
	    cs.gridy = 4;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 10, 0, 10);
	    panel.add(dumpName, cs);
	
	    browse = new JButton("Browse");
	    browse.setEnabled(dump.isSelected());
	    browse.addActionListener(e -> fileChooser.showOpenDialog(parent));
	    cs.gridx = 1;
	    cs.gridy = 4;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 0, 0, 0);
	    panel.add(browse, cs);

		
		panel.setBorder(new LineBorder(Color.BLACK));
		

		browse.addActionListener(e -> browseAction(e));

		cancel = new JButton("Cancel");
		cancel.addActionListener(e -> dispose());
		apply = new JButton("Apply");
		apply.addActionListener(e -> getFileName());

		JPanel bp = new JPanel();
		bp.add(apply);
		bp.add(cancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	private void browseAction(ActionEvent e)
	{
	
	}
	
	private void processFile(ActionEvent e)
	{
		if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
		{
			File f = fileChooser.getSelectedFile();
			File cwd = fileChooser.getCurrentDirectory();
			fileChooser.setCurrentDirectory(cwd);
			dumpName.setText(f.getAbsolutePath());
		}
	}
	
	private void browseEnable(ActionEvent e)
	{
		JCheckBox cb = (JCheckBox) e.getSource();
		dumpName.setEnabled(cb.isSelected());
		browse.setEnabled(cb.isSelected());
	}

	private void getFileName()
	{
	    dispose();	
	}
	
	private int getIndex(char c)
	{
		for (int i=0; i<CliOptions.suffixChars.length; i++)
		{
			if (c == CliOptions.suffixChars[i]) return(i);
		}
		return(2);
	}
	
	public static void main(String[] args)
	{
		CliOptions options = new CliOptions(args);
		final JFrame frame = new JFrame("JDialog Demo");
		StdfOptionsDialog dlg = new StdfOptionsDialog(frame, options);
		dlg.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

}
