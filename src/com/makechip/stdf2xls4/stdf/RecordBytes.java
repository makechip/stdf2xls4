package com.makechip.stdf2xls4.stdf;

import com.makechip.stdf2xls4.stdf.enums.Record_t;

public class RecordBytes implements Comparable<RecordBytes>
{
	private byte[] bytes;
	private Record_t type;
	private int sequenceNumber;
	private int devNum;

	public RecordBytes(byte[] bytes, int sequenceNumber, Record_t type, int devNum) throws StdfException
	{
		this.bytes = bytes;
		this.sequenceNumber = sequenceNumber;
		this.type = type;
		this.devNum = devNum;
		if (type == null) throw new StdfException("Unknown record type: " + bytes[0] + bytes[1]);
	}
	
	public RecordBytes(byte[] bytes, int sequenceNumber, Record_t type) throws StdfException
	{
		this(bytes, sequenceNumber, type, -1);
	}
	
	byte[] getBytes() { return(bytes); }
	
	Record_t getType() { return(type); }
	
	public int getDeviceNumber() { return(devNum); }
	
	public long getSequenceNumber() { return(sequenceNumber); }

	@Override
	public int compareTo(RecordBytes arg0)
	{
		return((int) (sequenceNumber - arg0.getSequenceNumber()));
	}
	
	public StdfRecord createRecord()
	{
		//Log.msg("Record type = " + type + " length = " + bytes.length);
		switch (type)
		{
		case DTR: return(new DatalogTextRecord(sequenceNumber, devNum, bytes));
		case EPS: return(new EndProgramSelectionRecord(sequenceNumber, devNum, bytes));
		case FAR: return(new FileAttributesRecord(sequenceNumber, devNum, bytes));
		case ATR: return(new AuditTrailRecord(sequenceNumber, devNum, bytes));
		case BPS: return(new BeginProgramSelectionRecord(sequenceNumber, devNum, bytes));
		case FTR: return(new FunctionalTestRecord(sequenceNumber, devNum, bytes)); // ***
		case GDR: return(new GenericDataRecord(sequenceNumber, devNum, bytes));
		case HBR: return(new HardwareBinRecord(sequenceNumber, devNum, bytes));
		case MIR: return(new MasterInformationRecord(sequenceNumber, devNum, bytes));
		case MPR: return(new MultipleResultParametricRecord(sequenceNumber, devNum, bytes)); // ***
		case MRR: return(new MasterResultsRecord(sequenceNumber, devNum, bytes));
		case PCR: return(new PartCountRecord(sequenceNumber, devNum, bytes));
		case PGR: return(new PinGroupRecord(sequenceNumber, devNum, bytes));
		case PIR: return(new PartInformationRecord(sequenceNumber, devNum, bytes));
		case PLR: return(new PinListRecord(sequenceNumber, devNum, bytes));
		case PMR: return(new PinMapRecord(sequenceNumber, devNum, bytes)); // ***
		case PRR: return(new PartResultsRecord(sequenceNumber, devNum, bytes));
		case PTR: return(new ParametricTestRecord(sequenceNumber, devNum, bytes)); // ***
		case RDR: return(new RetestDataRecord(sequenceNumber, devNum, bytes));
		case SBR: return(new SoftwareBinRecord(sequenceNumber, devNum, bytes));
		case SDR: return(new SiteDescriptionRecord(sequenceNumber, devNum, bytes));
		case TSR: return(new TestSynopsisRecord(sequenceNumber, devNum, bytes));
		case WCR: return(new WaferConfigurationRecord(sequenceNumber, devNum, bytes));
		case WIR: return(new WaferInformationRecord(sequenceNumber, devNum, bytes));
		case WRR: return(new WaferResultsRecord(sequenceNumber, devNum, bytes));
		default:
		}
		return(null);
	}

}
