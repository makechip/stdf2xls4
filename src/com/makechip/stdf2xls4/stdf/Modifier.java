package com.makechip.stdf2xls4.stdf;

import java.util.StringTokenizer;

import com.makechip.stdf2xls4.stdf.enums.Condition_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.descriptors.FieldDescriptor;

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
	 * @param modString
	 */
	public Modifier(String modString)
	{
		StringTokenizer st = new StringTokenizer(modString, ": ");
		int numToks = st.countTokens();
		if (numToks < 10) throw new RuntimeException("Invalid modifier string: " + modString);
		String s = st.nextToken();
		if (!s.equals("R")) error("Invalid modifier string: Expected 'R', got " + s);
		s = st.nextToken();
		record = Enum.valueOf(Record_t.class, s);
		if (record == null) error("Invalid modifier string: Invalid record type: " + s);
		s = st.nextToken();
		if (!s.equals("F")) error("Invalid modifier string: Expected 'F', got " + s);
		s = st.nextToken();
		field = FieldDescriptor.getDescriptor(record, s);
		if (field == null) error("Invalid modifier String: Invalid field name: " + s);
		s = st.nextToken();
		if (!s.equals("C")) error("Invalid modifier string: Expected 'C', got " + s);
		s = st.nextToken();
		condition = Enum.valueOf(Condition_t.class, s);
		if (condition == null) error("Invalid modifier string: Invalid condition: " + s);
		s = st.nextToken();
		if (!s.equals("V")) error("Invalid modifier string: Expected 'V', got " + s);
		s = st.nextToken();
		Data_t dt = field.getType();
		oldValue = getValue(dt, s);
		s = st.nextToken();
		if (!s.equals("N")) error("Invalid modifier String: Expected 'N', got " + s);
		s = st.nextToken();
		if (st.hasMoreTokens())
		{
			s = s + st.nextToken("");
		}
		newValue = getValue(dt, s);
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
