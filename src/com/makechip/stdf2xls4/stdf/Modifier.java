package com.makechip.stdf2xls4.stdf;

import com.makechip.stdf2xls4.stdf.enums.Condition_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.descriptors.FieldDescriptor;
import com.makechip.util.Log;

public class Modifier
{
	public final Record_t record;
	public final FieldDescriptor field;
	public final Condition_t condition;
	public final Object oldValue;
	public final Object newValue;
	
	@Override
	public String toString()
	{
		String s = "R:" + record + " F:" + field + " C:" + condition + " V:" + oldValue + " N:" + newValue;
		return(s);
	}

	/**
	 * A modifier string has the form of:
	 * R:Record_t F:FieldDescriptor C:Condition_t V:oldValue N:newValue
	 * Example:
	 * "R=DTR F=TEXT_DAT C=EQUALS V='STEP #: 2.00\\' N='STEP #: 2.00'"
	 * @param modString
	 */
	public Modifier(String mod)
	{
	    Log.msg("modifier = " + mod);
	    int l = mod.length();
	    if (mod.charAt(0) != 'R' || mod.charAt(1) != '=')
	    {
	        error("Invalid modifier string: Expected R=, got '" + mod.charAt(0) + mod.charAt(1) + "'");
	    }
	    int j = 2;
	    String r = "";
	    while (mod.charAt(j) != ' ' && mod.charAt(j) != '\t' && j < l)
	    {
	        r += mod.charAt(j);
	        j++;
	    }
	    Log.msg("R = " + r);
		record = Enum.valueOf(Record_t.class, r);
		if (record == null) error("Invalid modifier string: Invalid record type: " + r);
		while (mod.charAt(j) != 'F' && j < l) j++;
	    if (mod.charAt(j) != 'F' || mod.charAt(j+1) != '=')
	    {
	        error("Invalid modifier string: Expected F=, got '" + mod.charAt(j) + mod.charAt(j+1) + "'");
	    }
	    j+=2;
	    String f = "";
	    while (mod.charAt(j) != ' ' && mod.charAt(j) != '\t' && j< l)
	    {
	        f += mod.charAt(j);
	        j++;
	    }
	    Log.msg("F = " + f);
		field = FieldDescriptor.getDescriptor(record, f);
		if (field == null) error("Invalid modifier String: Invalid field name: " + f);
		while (mod.charAt(j) != 'C' && j < l) j++;
	    if (mod.charAt(j) != 'C' || mod.charAt(j+1) != '=')
	    {
	        error("Invalid modifier string: Expected C=, got '" + mod.charAt(j) + mod.charAt(j+1) + "'");
	    }
	    j+=2;
	    String c = "";
	    while (mod.charAt(j) != ' ' && mod.charAt(j) != '\t' && j< l)
	    {
	        c += mod.charAt(j);
	        j++;
	    }
	    Log.msg("C = " + c);
		condition = Enum.valueOf(Condition_t.class, c);
		if (condition == null) error("Invalid modifier string: Invalid condition: " + c);
		while (mod.charAt(j) != 'V' && j < l) j++;
	    if (mod.charAt(j) != 'V' || mod.charAt(j+1) != '=')
	    {
	        error("Invalid modifier string: Expected V=, got '" + mod.charAt(j) + mod.charAt(j+1) + "'");
	    }
	    j+=2;
	    if (mod.charAt(j) != '\'') error("Expected \"'\", got '" + mod.charAt(j) + "'");
	    j++;
	    String v = "";
	    while (mod.charAt(j) != '\'' && j < l)
	    {
	        v += mod.charAt(j);
	        j++;
	    }
		Data_t dt = field.getType();
		oldValue = getValue(dt, v);
		Log.msg("oldValue = " + oldValue);
		while (mod.charAt(j) != 'N' && j < l) j++;
	    if (mod.charAt(j) != 'N' || mod.charAt(j+1) != '=')
	    {
	        error("Invalid modifier string: Expected N=, got '" + mod.charAt(j) + mod.charAt(j+1) + "'");
	    }
	    j+=2;
	    if (mod.charAt(j) != '\'') error("Expected \"'\", got '" + mod.charAt(j) + "'");
	    j++;
	    String n = "";
	    while (mod.charAt(j) != '\'' && j < l)
	    {
	        n += mod.charAt(j);
	        j++;
	    }
		newValue = getValue(dt, n);
		Log.msg("newValue = " + newValue);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result + ((oldValue == null) ? 0 : oldValue.hashCode());
		result = prime * result + ((record == null) ? 0 : record.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Modifier)) return false;
		Modifier other = (Modifier) obj;
		if (condition != other.condition) return false;
		if (field == null)
		{
			if (other.field != null) return false;
		} 
		else if (!field.equals(other.field)) return false;
		if (newValue == null)
		{
			if (other.newValue != null) return false;
		} 
		else if (!newValue.equals(other.newValue)) return false;
		if (oldValue == null)
		{
			if (other.oldValue != null) return false;
		} 
		else if (!oldValue.equals(other.oldValue)) return false;
		if (record != other.record) return false;
		return true;
	}

	public Modifier(Record_t record, FieldDescriptor field, Condition_t condition, String oldValue, String newValue)
	{
		this("R:" + record + " F:" + field + " C:" + condition + " V:" + oldValue + " N:" + newValue);
	}

	private void error(String s)
	{
		throw new RuntimeException(s);
	}
	
	public static Object getValue(Data_t dt, String val)
	{
		switch (dt)
		{
		case U1: return(new Short(val));
		case U2: return(new Integer(val));
		case U4: return(new Long(val));
		case I1: return(new Byte(val));
		case I2: return(new Short(val));
		case I4: return(new Integer(val));
		case R4: return(new Float(val));
		case CN: return(val);
		case N1: return(new Byte(val));
		case C1: return(new Character(val.charAt(0)));
		case B1: return(new Byte(val));
	    default:	
		}
		return(null);
	}
	
}
