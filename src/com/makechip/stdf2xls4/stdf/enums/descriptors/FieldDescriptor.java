package com.makechip.stdf2xls4.stdf.enums.descriptors;

import com.makechip.stdf2xls4.stdf.enums.types.FieldType;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;



/**
*** @author ericw
*** @version $Id: StubGen.java 24 2007-12-29 21:27:59Z eric $
**/
public interface FieldDescriptor
{

    public Data_t getType();
    public String getDescription();
    public boolean isRequired();
    public FieldDescriptor getCountField();
    public FieldType getKind();

    public static FieldDescriptor getDescriptor(Record_t r, String fieldName)
    {
    	switch (r)
    	{
    	case ATR: return(Enum.valueOf(ATR_t.class, fieldName));
    	case BPS: return(Enum.valueOf(BPS_t.class, fieldName));
    	case DTR: return(Enum.valueOf(DTR_t.class, fieldName));
    	case FAR: return(Enum.valueOf(FAR_t.class, fieldName));
    	case FTR: return(Enum.valueOf(FTR_t.class, fieldName));
    	case GDR: return(Enum.valueOf(GDR_t.class, fieldName));
    	case HBR: return(Enum.valueOf(HBR_t.class, fieldName));
    	case MIR: return(Enum.valueOf(MIR_t.class, fieldName));
    	case MPR: return(Enum.valueOf(MPR_t.class, fieldName));
    	case MRR: return(Enum.valueOf(MRR_t.class, fieldName));
    	case PCR: return(Enum.valueOf(PCR_t.class, fieldName));
    	case PGR: return(Enum.valueOf(PGR_t.class, fieldName));
    	case PIR: return(Enum.valueOf(PIR_t.class, fieldName));
    	case PLR: return(Enum.valueOf(PLR_t.class, fieldName));
    	case PMR: return(Enum.valueOf(PMR_t.class, fieldName));
    	case PRR: return(Enum.valueOf(PRR_t.class, fieldName));
    	case PTR: return(Enum.valueOf(PTR_t.class, fieldName));
    	case RDR: return(Enum.valueOf(RDR_t.class, fieldName));
    	case SBR: return(Enum.valueOf(SBR_t.class, fieldName));
    	case SDR: return(Enum.valueOf(SDR_t.class, fieldName));
    	case TSR: return(Enum.valueOf(TSR_t.class, fieldName));
    	case WCR: return(Enum.valueOf(WCR_t.class, fieldName));
    	case WIR: return(Enum.valueOf(WIR_t.class, fieldName));
    	case WRR: return(Enum.valueOf(WRR_t.class, fieldName));
        default:	
    	}
    	return(null);
    }
    
    public static FieldDescriptor[] getFields(Record_t r)
    {
    	switch (r)
    	{
    	case ATR: return(ATR_t.class.getEnumConstants());    
    	case BPS: return(BPS_t.class.getEnumConstants());    
    	case DTR: return(DTR_t.class.getEnumConstants());    
    	case FAR: return(FAR_t.class.getEnumConstants());    
    	case FTR: return(FTR_t.class.getEnumConstants());    
    	case GDR: return(GDR_t.class.getEnumConstants());    
    	case HBR: return(HBR_t.class.getEnumConstants());    
    	case MIR: return(MIR_t.class.getEnumConstants());    
    	case MPR: return(MPR_t.class.getEnumConstants());    
    	case MRR: return(MRR_t.class.getEnumConstants());    
    	case PCR: return(PCR_t.class.getEnumConstants());    
    	case PGR: return(PGR_t.class.getEnumConstants());    
    	case PIR: return(PIR_t.class.getEnumConstants());    
    	case PLR: return(PLR_t.class.getEnumConstants());    
    	case PMR: return(PMR_t.class.getEnumConstants());    
    	case PRR: return(PRR_t.class.getEnumConstants());    
    	case PTR: return(PTR_t.class.getEnumConstants());    
    	case RDR: return(RDR_t.class.getEnumConstants());    
    	case SBR: return(SBR_t.class.getEnumConstants());    
    	case SDR: return(SDR_t.class.getEnumConstants());    
    	case TSR: return(TSR_t.class.getEnumConstants());    
    	case WCR: return(WCR_t.class.getEnumConstants());    
    	case WIR: return(WIR_t.class.getEnumConstants());    
    	case WRR: return(WRR_t.class.getEnumConstants());    
        default:	
    	}
    	return(null);
    }
    
    public boolean equals(Object o);
    
    public int hashCode();
}
