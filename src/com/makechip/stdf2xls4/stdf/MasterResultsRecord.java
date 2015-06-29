/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4.stdf;

import gnu.trove.list.array.TByteArrayList;

import java.util.Date;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;

/**
 *  This class holds the fields for a Master Results Record.
 *  @author eric
 */
public class MasterResultsRecord extends StdfRecord
{
	/**
	 *  This is the FINISH_T field of the MasterResultsRecord.
	 */
    public final long finishDate;
    /**
     *  This is the DISP_COD field of the MasterResultsRecord.
     */
    public final String dispCode;
    /**
     * This is the USR_DESC field of the MasterResultsRecord.
     */
    public final String lotDesc;
    /**
     *  This is the EXC_DESC field of the MasterResultsRecord.
     */
    public final String execDesc;
    
    /**
     *  Constructor used by the STDF reader to load binary data into this class.
     *  @param tdb The TestIdDatabase.  This value is not used by the HardwareBinRecord.
     *         It is provided so that all StdfRecord classes have the same argument signatures,
     *         so that function references can be used to refer to the constructors of StdfRecords.
     *  @param dvd The DefaultValueDatabase is used to access the CPU type.
     *  @param data The binary stream data for this record. Note that the REC_LEN, REC_TYP, and
     *         REC_SUB values are not included in this array.
     */
   public MasterResultsRecord(TestIdDatabase tdb, DefaultValueDatabase dvd, byte[] data)
    {
        super(Record_t.MRR, dvd.getCpuType(), data);
        finishDate = getU4(0);
        String s = getFixedLengthString(1);
        if (s.equals(MISSING_STRING)) dispCode = " "; else dispCode = s;
        lotDesc = getCn();
        execDesc = getCn();
    }
    
   /**
    * This constructor is used to generate binary stream data from the field values.
    * @param tdb The TestIdDatabase. This value is not used, but is needed so that
     * this constructor can call the previous constructor to avoid code duplication.
    * @param dvd The DefaultValueDatabase is used to access the CPU type.
    * @param finishDate The FINISH_T field.
    * @param dispCode The DISP_COD field.
    * @param lotDesc The LOT_DESC field.
    * @param execDesc The EXC_DESC field.
    */
    public MasterResultsRecord(
    	TestIdDatabase tdb,
    	DefaultValueDatabase dvd,
    	long finishDate, 
    	char dispCode, 
    	String lotDesc, 
    	String execDesc)
    {
    	this(tdb, dvd, toBytes(dvd.getCpuType(), finishDate, "" + dispCode, lotDesc, execDesc));
    }
    
	/* (non-Javadoc)
	 * @see com.makechip.stdf2xls4.stdf#toBytes()
	 */
	@Override
	protected void toBytes()
	{
	    bytes = toBytes(cpuType, finishDate, dispCode, lotDesc, execDesc);	
	}
	
	private static byte[] toBytes(Cpu_t cpuType, long finishDate, String dispCode, String lotDesc, String execDesc)
	{
		TByteArrayList l = new TByteArrayList();
		l.addAll(cpuType.getU4Bytes(finishDate));
		l.addAll(getFixedLengthStringBytes(dispCode));
		l.addAll(getCnBytes(lotDesc));
		l.addAll(getCnBytes(execDesc));
		return(l.toArray());
	}

    /**
     * This method returns the String form of the FINISH_T date.
     * @return the finishDate
     */
    public String getFinishDate()
    {
        return(new Date(1000L * finishDate).toString());
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MasterResultsRecord [finishDate=").append(getFinishDate());
		builder.append(", dispCode=").append(dispCode);
		builder.append(", lotDesc=").append(lotDesc);
		builder.append(", execDesc=").append(execDesc);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + dispCode.hashCode();
		result = prime * result + execDesc.hashCode();
		result = prime * result + (int) (finishDate ^ (finishDate >>> 32));
		result = prime * result + lotDesc.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof MasterResultsRecord)) return false;
		MasterResultsRecord other = (MasterResultsRecord) obj;
		if (!dispCode.equals(other.dispCode)) return false;
		if (!execDesc.equals(other.execDesc)) return false;
		if (finishDate != other.finishDate) return false;
		if (!lotDesc.equals(other.lotDesc)) return false;
		if (!super.equals(obj)) return false;
		return true;
	}

}
