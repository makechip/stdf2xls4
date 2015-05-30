package com.makechip.stdf2xls4.stdf;

import com.makechip.stdf2xls4.stdf.enums.Record_t;

public class RecordBytes implements Comparable<RecordBytes>
{
    public static final String TEXT_DATA = "TEXT_DATA";
    public static final String SERIAL_MARKER = "S/N";
    private IdentityDatabase idb;
    private byte[] bytes;
	private final Record_t type;
	private final int sequenceNumber;
	private final long timeStamp;

	public RecordBytes(byte[] bytes, int sequenceNumber, Record_t type, IdentityDatabase idb, long timeStamp) throws StdfException
	{
		this.bytes = bytes;
		this.sequenceNumber = sequenceNumber;
		this.type = type;
		this.idb = idb;
		this.timeStamp = timeStamp;
		if (type == null) throw new StdfException("Unknown record type: " + bytes[0] + bytes[1]);
	}
	
	byte[] getBytes() { return(bytes); }
	
	Record_t getType() { return(type); }
	
	public long getSequenceNumber() { return(sequenceNumber); }

	@Override
	public int compareTo(RecordBytes arg0)
	{
		return((int) (sequenceNumber - arg0.getSequenceNumber()));
	}
	
	public StdfRecord createRecord()
	{
		switch (type)
		{
		case DTR: String text = new String(bytes);
		          if (text.contains(TEXT_DATA) && text.contains(":") && !text.contains(SERIAL_MARKER))
		          {
		        	  return(new DatalogTestRecord(sequenceNumber, idb, bytes));
		          }
				  return(new DatalogTextRecord(sequenceNumber, bytes));
		case EPS: return(new EndProgramSelectionRecord(sequenceNumber, bytes));
		case FAR: return(new FileAttributesRecord(sequenceNumber, bytes));
		case ATR: return(new AuditTrailRecord(sequenceNumber, bytes));
		case BPS: return(new BeginProgramSelectionRecord(sequenceNumber, bytes));
		case FTR: return(new FunctionalTestRecord(sequenceNumber, idb, bytes));
		case GDR: return(new GenericDataRecord(sequenceNumber, bytes));
		case HBR: return(new HardwareBinRecord(sequenceNumber, bytes));
		case MIR: return(new MasterInformationRecord(sequenceNumber, timeStamp, bytes));
		case MPR: return(new MultipleResultParametricRecord(sequenceNumber, idb, bytes));
		case MRR: return(new MasterResultsRecord(sequenceNumber, bytes));
		case PCR: return(new PartCountRecord(sequenceNumber, bytes));
		case PGR: return(new PinGroupRecord(sequenceNumber, bytes));
		case PIR: return(new PartInformationRecord(sequenceNumber, bytes));
		case PLR: return(new PinListRecord(sequenceNumber, bytes));
		case PMR: return(new PinMapRecord(sequenceNumber, bytes));
		case PRR: return(new PartResultsRecord(sequenceNumber, bytes));
		case PTR: return(new ParametricTestRecord(sequenceNumber, idb, bytes));
		case RDR: return(new RetestDataRecord(sequenceNumber, bytes));
		case SBR: return(new SoftwareBinRecord(sequenceNumber, bytes));
		case SDR: return(new SiteDescriptionRecord(sequenceNumber, bytes));
		case TSR: return(new TestSynopsisRecord(sequenceNumber, bytes));
		case WCR: return(new WaferConfigurationRecord(sequenceNumber, bytes));
		case WIR: return(new WaferInformationRecord(sequenceNumber, bytes));
		case WRR: return(new WaferResultsRecord(sequenceNumber, bytes));
		default:
		}
		return(null);
	}

}
