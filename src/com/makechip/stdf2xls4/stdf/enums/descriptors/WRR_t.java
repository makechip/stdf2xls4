package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum WRR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_GRP(U1, null, true, U1_t.SITE_GRP, "Site group number"),
    FINISH_T(U4, null, true, U4_t.FINISH_T, "Date and time last part tested"),
    PART_CNT(U4, null, true, U4_t.PART_CNT, "Number of parts tested"),
    RTST_CNT(U4, null, false, U4_t.RTST_CNT, "Number of parts retested"),
    ABRT_CNT(U4, null, false, U4_t.ABRT_CNT, "Number of aborts during testing"),
    GOOD_CNT(U4, null, false, U4_t.GOOD_CNT, "Number of good (passed) parts tested"),
    FUNC_CNT(U4, null, false, U4_t.FUNC_CNT, "Number of functional parts tested"),
    WAFER_ID(CN, null, false, CN_t.WAFER_ID, "Wafer ID"),
    FABWF_ID(CN, null, false, CN_t.FABWF_ID, "Fab wafer ID"),
    FRAME_ID(CN, null, false, CN_t.FRAME_ID, "Wafer frame ID"),
    MASK_ID(CN,  null, false, CN_t.MASK_ID, "Wafer mask ID"),
    USR_DESC(CN, null, false, CN_t.USR_DESC, "Wafer description supplied by user"),
    EXC_DESC(CN, null, false, CN_t.EXC_DESC, "Wafer description supplied by exec");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private WRR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
    {
        this.type = type;
        this.required = required;
        this.countField = countField;
        this.kind = kind;
        this.description = description;
    }

	@Override
    public Data_t getType()         { return(type); }
	@Override
    public boolean isRequired()     { return(required); }
	@Override
    public String getDescription()  { return(description); }
	@Override
    public FieldDescriptor getCountField() { return(countField); }
	@Override
	public FieldType getKind() { return(kind); }
}

