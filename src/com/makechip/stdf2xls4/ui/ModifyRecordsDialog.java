package com.makechip.stdf2xls4.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;
import javax.swing.border.LineBorder;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.Modifier;

public class ModifyRecordsDialog extends JDialog
{
	private static final long serialVersionUID = 4732351878549186603L;
	private AddModifierDialog addDialog;
	private JList<Modifier> modifiers;
	private JButton addModifier;
	private JButton removeModifier;
	private JButton cancel;
	private JButton apply;
	private MutableComboBoxModel<Modifier> model;
	
	List<Modifier> getModifiers()
	{
		List<Modifier> l = new ArrayList<>();
		if (model != null)
		{
		    for (int i=0; i<model.getSize(); i++) l.add(model.getElementAt(i));
		}
		return(l);
	}
	
	public ModifyRecordsDialog(Frame parent, CliOptions options)
	{
		super(parent, "STDF Options", true);
		JPanel panel = new JPanel(new GridBagLayout());
		addDialog = new AddModifierDialog(parent, options);
		
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.NONE;
		model = new DefaultComboBoxModel<Modifier>();

		modifiers = new JList<Modifier>(model);
		modifiers.setPreferredSize(new Dimension(400, 200));
		cs.gridx = 0;
		cs.gridy = 0;
		cs.anchor = GridBagConstraints.CENTER;
		cs.gridwidth = 2;
		panel.add(modifiers, cs);

		addModifier = new JButton("Add Modifier");
		addModifier.addActionListener(e -> getModifier());
		addModifier.setPreferredSize(new Dimension(200, 26));
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(addModifier, cs);

		removeModifier = new JButton("Remove Modifier");
		removeModifier.addActionListener(e -> delModifier());
		removeModifier.setPreferredSize(new Dimension(200, 26));
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(removeModifier, cs);

		panel.setBorder(new LineBorder(Color.BLACK));
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(e -> dispose());
		apply = new JButton("Apply");
		apply.addActionListener(e -> dispose());

		JPanel bp = new JPanel();
		bp.add(apply);
		bp.add(cancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	private void getModifier()
	{
		addDialog.setVisible(true);
	    Modifier m = addDialog.getModifier();	
	    if (m != null) model.addElement(m);
	}
	
	private void delModifier()
	{
		Modifier m = (Modifier) model.getSelectedItem();
		model.removeElement(m);
	}
	
	public static void main(String[] args)
	{
		CliOptions options = new CliOptions(args);
		final JFrame frame = new JFrame("JDialog Demo");
		ModifyRecordsDialog dlg = new ModifyRecordsDialog(frame, options);
		dlg.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
	}

}
