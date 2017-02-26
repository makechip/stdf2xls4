package com.makechip.stdf2xls4.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.stdf.Modifier;
import com.makechip.stdf2xls4.stdf.enums.Condition_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.descriptors.ATR_t;
import com.makechip.stdf2xls4.stdf.enums.descriptors.FieldDescriptor;

import static com.makechip.stdf2xls4.stdf.enums.Record_t.EPS;
import static com.makechip.stdf2xls4.stdf.enums.Record_t.DTRX;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.R8;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.BN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.DN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.VN;

public class AddModifierDialog extends JDialog implements ItemListener
{
	private static final long serialVersionUID = 4732351878549186603L;
	private static final Condition_t[] cond = new Condition_t[] { Condition_t.EQUALS, Condition_t.TRUE };
	private static final Condition_t[] stringCond = Condition_t.class.getEnumConstants();
	private static final DefaultComboBoxModel<Condition_t> ncond = new DefaultComboBoxModel<>(cond);
	private static final DefaultComboBoxModel<Condition_t> scond = new DefaultComboBoxModel<>(stringCond);
	private JComboBox<Record_t> record;
	private JComboBox<FieldDescriptor> field;
	private JComboBox<Condition_t> condition;
	private JTextField oldValue;
	private JLabel changeTo;
	private JTextField newValue;
	private JButton cancel;
	private JButton apply;
	private Modifier modifier;
	
	public Modifier getModifier() { return(modifier); }
	
	public AddModifierDialog(Frame parent, CliOptions options)
	{
		super(parent, "Define Modifier", true);
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.NONE;
		
		JLabel l1 = new JLabel("For Record:");
		l1.setPreferredSize(new Dimension(100, 26));
		cs.gridx = 0;
		cs.gridy = 0;
		cs.insets = new Insets(10, 10, 10, 10);
		cs.anchor = GridBagConstraints.WEST;
		cs.gridwidth = 1;
		panel.add(l1, cs);
		
		List<Record_t> l = Arrays.stream(Record_t.class.getEnumConstants())
			.filter(r -> r != EPS && r != DTRX).collect(Collectors.toList());
		record = new JComboBox<>(l.toArray(new Record_t[20]));
		record.addItemListener(this);
		record.setPreferredSize(new Dimension(100, 26));
		cs.gridx = 1;
		cs.gridy = 0;
		cs.anchor = GridBagConstraints.WEST;
		cs.gridwidth = 1;
		panel.add(record, cs);
		
		JLabel l2 = new JLabel("and Field:");
		l2.setPreferredSize(new Dimension(100, 26));
		cs.gridx = 2;
		cs.gridy = 0;
		cs.anchor = GridBagConstraints.WEST;
		cs.gridwidth = 1;
		panel.add(l2, cs);
		
	    List<FieldDescriptor> lx = Arrays.stream(ATR_t.class.getEnumConstants())
	    	.filter(r -> r.getType() != R8 && r.getType() != BN && r.getType() != DN && r.getType() != VN)
	    	.collect(Collectors.toList());
		field = new JComboBox<>(new DefaultComboBoxModel<FieldDescriptor>(lx.toArray(new FieldDescriptor[5])));
		field.setPreferredSize(new Dimension(100, 26));
		field.addItemListener(this);
		cs.gridx = 3;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(field, cs);
		
		JLabel l3 = new JLabel("If");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(l3, cs);

		condition = new JComboBox<>(cond);
		condition.addItemListener(this);
		cs.insets = new Insets(10, 10, 10, 10);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(condition, cs);

		oldValue = new JTextField(24);
		cs.gridx = 2;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(oldValue, cs);
		
	    changeTo = new JLabel("Change To:");
	    cs.gridx = 0;
	    cs.gridy = 2;
	    cs.gridwidth = 1;
	    panel.add(changeTo, cs);
	
	    newValue = new JTextField(24);
	    cs.gridx = 1;
	    cs.gridy = 2;
	    cs.gridwidth = 1;
	    panel.add(newValue, cs);
	
		panel.setBorder(new LineBorder(Color.BLACK));

		cancel = new JButton("Cancel");
		cancel.addActionListener(e -> dispose());
		apply = new JButton("Apply");
		apply.addActionListener(e -> createModifier());

		JPanel bp = new JPanel();
		bp.add(apply);
		bp.add(cancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	public static void main(String[] args)
	{
		CliOptions options = new CliOptions(args);
		final JFrame frame = new JFrame("JDialog Demo");
		AddModifierDialog dlg = new AddModifierDialog(frame, options);
		dlg.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
	
	@Override
	public void setVisible(boolean b)
	{
		modifier = null;
		super.setVisible(b);
	}
	
	private void createModifier()
	{
		if (condition.getSelectedItem() != Condition_t.TRUE)
		{
			if (oldValue.getDocument().getLength() == 0) 
			{
				JOptionPane.showMessageDialog(this, "Missing value for condition.");
				return;
			}
		}
		if (newValue.getDocument().getLength() == 0) 
		{
			JOptionPane.showMessageDialog(this, "Missing new replacement value");
			return;
		}
		Record_t rt = (Record_t) record.getSelectedItem();
		FieldDescriptor fd = (FieldDescriptor) field.getSelectedItem();
		Condition_t c = (Condition_t) condition.getSelectedItem();
		String oldv = oldValue.getText();
		String newv = newValue.getText();
		modifier = new Modifier(rt, fd, c, oldv, newv);
		dispose();
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
	    if (e.getStateChange() == 1)
	    {
	    	if (e.getSource() == condition)
	    	{
	    	    Condition_t c = (Condition_t) condition.getSelectedItem();	
	    	    oldValue.setEnabled(c != Condition_t.TRUE);
	    	}
	    	else
	    	{
	    		if (e.getSource() == record)
	    		{
	    			Record_t rec = (Record_t) record.getSelectedItem();
	    			FieldDescriptor[] fd = FieldDescriptor.getFields(rec);
	    			List<FieldDescriptor> l2 = Arrays.stream(fd)
	    					.filter(r -> r.getType() != R8 && r.getType() != BN && r.getType() != DN && r.getType() != VN)
	    					.collect(Collectors.toList());
	    			field.setModel(new DefaultComboBoxModel<FieldDescriptor>(l2.toArray(new FieldDescriptor[5])));
	    		}
	    		FieldDescriptor fd = (FieldDescriptor) field.getSelectedItem();
	    		if (fd.getType() == Data_t.CN) condition.setModel(scond);
	    		else condition.setModel(ncond);
	    	}
	    }
	}

}
