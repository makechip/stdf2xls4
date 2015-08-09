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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.makechip.stdf2xls4.CliOptions;

public class SpreadsheetOptionsDialog extends JDialog
{
	public static final Integer[] prec = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	private static final long serialVersionUID = 4732351878549186603L;
	private JCheckBox noWrap;
	private JCheckBox noOverwrite;
	private JCheckBox onePage;
	private JCheckBox rotate;
    private JCheckBox output;
	private JCheckBox logo;
	private JCheckBox jxlFormat;
	private JTextField outputFile;
	private JTextField logoFile;
	private JButton browse1;
	private JButton browse2;
	private JComboBox<Integer> precision;
	private JFileChooser ssFileChooser;
	private JFileChooser logoFileChooser;
	private JButton cancel;
	private JButton apply;
	
	public SpreadsheetOptionsDialog(Frame parent, CliOptions options)
	{
		super(parent, "STDF Options", true);
		ssFileChooser = new JFileChooser();
		ssFileChooser.setMultiSelectionEnabled(false);
		ssFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ssFileChooser.addActionListener(e -> processFile1(e));
		logoFileChooser = new JFileChooser();
		logoFileChooser.setMultiSelectionEnabled(false);
		logoFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		logoFileChooser.addActionListener(e -> processFile2(e));
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.NONE;
		cs.anchor = GridBagConstraints.WEST;

		noWrap = new JCheckBox("Don't wrap long test names", options.noWrapTestNames);
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(noWrap, cs);

		noOverwrite = new JCheckBox("Don't overwrite duplicates", options.noOverwrite);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(noOverwrite, cs);

		onePage = new JCheckBox("Keep all steps/wafers on one page", options.onePage);
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		panel.add(onePage, cs);

		rotate = new JCheckBox("Transpose spreadsheet by 90 degrees", options.rotate);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		panel.add(rotate, cs);

		output = new JCheckBox("Output Spreadsheet", options.xlsName != null);
		output.addActionListener(e -> browse1Enable(e));
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 1;
		panel.add(output, cs);

	    outputFile = new JTextField(32);
	    outputFile.setEnabled(output.isSelected());
	    cs.gridx = 1;
	    cs.gridy = 4;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 10, 0, 10);
	    panel.add(outputFile, cs);
	
	    browse1 = new JButton("Browse");
	    browse1.setEnabled(output.isSelected());
	    browse1.addActionListener(e -> ssFileChooser.showOpenDialog(parent));
	    cs.gridx = 2;
	    cs.gridy = 4;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 0, 0, 0);
	    panel.add(browse1, cs);

		logo = new JCheckBox("Use Custom Logo", options.logoFile != null);
		logo.addActionListener(e -> browse2Enable(e));
		cs.gridx = 0;
		cs.gridy = 5;
		cs.gridwidth = 1;
		panel.add(logo, cs);

	    logoFile = new JTextField(32);
	    logoFile.setEnabled(logo.isSelected());
	    cs.gridx = 1;
	    cs.gridy = 5;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 10, 0, 10);
	    panel.add(logoFile, cs);
	
	    browse2 = new JButton("Browse");
	    browse2.setEnabled(logo.isSelected());
	    browse2.addActionListener(e -> logoFileChooser.showOpenDialog(parent));
	    cs.gridx = 2;
	    cs.gridy = 5;
	    cs.gridwidth = 1;
	    cs.insets = new Insets(0, 0, 0, 0);
	    panel.add(browse2, cs);

		jxlFormat = new JCheckBox("Use JXL library (xls format only)", options.useJxl);
		cs.gridx = 0;
		cs.gridy = 6;
		cs.gridwidth = 1;
	    cs.insets = new Insets(0, 0, 0, 0);
		panel.add(jxlFormat, cs);
		
		JLabel p = new JLabel("Decimal Places:");
		cs.gridx = 1;
		cs.gridy = 6;
		cs.insets = new Insets(0, 0, 0, 10);
		cs.anchor = GridBagConstraints.EAST;
		cs.gridwidth = 1;
		panel.add(p, cs);

		precision = new JComboBox<>(prec);
		precision.setSelectedIndex(getIndex(options.precision));
		precision.setEnabled(true);
		cs.anchor = GridBagConstraints.WEST;
		cs.insets = new Insets(0, 0, 0, 0);
		cs.gridx = 2;
		cs.gridy = 6;
		cs.gridwidth = 1;
		panel.add(precision, cs);
		
		panel.setBorder(new LineBorder(Color.BLACK));
		
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
	
	private void processFile1(ActionEvent e)
	{
		if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
		{
			File f = ssFileChooser.getSelectedFile();
			File cwd = ssFileChooser.getCurrentDirectory();
			ssFileChooser.setCurrentDirectory(cwd);
			outputFile.setText(f.getAbsolutePath());
		}
	}
	
	private void processFile2(ActionEvent e)
	{
		if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
		{
			File f = logoFileChooser.getSelectedFile();
			File cwd = logoFileChooser.getCurrentDirectory();
			logoFileChooser.setCurrentDirectory(cwd);
			logoFile.setText(f.getAbsolutePath());
		}
	}
	
	private void browse1Enable(ActionEvent e)
	{
		JCheckBox cb = (JCheckBox) e.getSource();
		outputFile.setEnabled(cb.isSelected());
		browse1.setEnabled(cb.isSelected());
	}

	private void browse2Enable(ActionEvent e)
	{
		JCheckBox cb = (JCheckBox) e.getSource();
		logoFile.setEnabled(cb.isSelected());
		browse2.setEnabled(cb.isSelected());
	}

	private void getFileName()
	{
	    dispose();	
	}
	
	private int getIndex(int c)
	{
		for (int i=0; i<prec.length; i++)
		{
			if (c == prec[i]) return(i);
		}
		return(3);
	}
	
	public static void main(String[] args)
	{
		CliOptions options = new CliOptions(args);
		final JFrame frame = new JFrame("JDialog Demo");
		SpreadsheetOptionsDialog dlg = new SpreadsheetOptionsDialog(frame, options);
		dlg.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

}
