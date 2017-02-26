package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum SDR_t implements FieldDescriptor
{
    HEAD_NUM(U1, null, true, U1_t.HEAD_NUM, "Test head number"),
    SITE_GRP(U1, null, true, U1_t.SITE_NUM, "Site group number"),
    SITE_CNT(U1, null, true, U1_t.SITE_CNT, "Number (k) of test sites in site group"),
    SITE_NUM(U1, SITE_CNT, true, U1Array_t.SITE_NUM, "Array of test site numbers"),
    HAND_TYP(CN, null, false, CN_t.HAND_TYP, "Handler or prober type"),
    HAND_ID(CN,  null, false, CN_t.HAND_ID, "Handler or prober ID"),
    CARD_TYP(CN, null, false, CN_t.CARD_TYP, "Probe card type"),
    CARD_ID(CN,  null, false, CN_t.CARD_ID, "Probe card ID"),
    LOAD_TYP(CN, null, false, CN_t.LOAD_TYP, "Loadboard type"),
    LOAD_ID(CN,  null, false, CN_t.LOAD_ID, "Loadboard ID"),
    DIB_TYP(CN,  null, false, CN_t.DIB_TYP, "DIB board type"),
    DIB_ID(CN,   null, false, CN_t.DIB_ID, "DIB board ID"),
    CABL_TYP(CN, null, false, CN_t.CABL_TYP, "Interface cable type"),
    CABL_ID(CN,  null, false, CN_t.CABL_ID, "Interface cable ID"),
    CONT_TYP(CN, null, false, CN_t.CONT_TYP, "Handler contactor type"),
    CONT_ID(CN,  null, false, CN_t.CONT_ID, "Handler contactor ID"),
    LASR_TYP(CN, null, false, CN_t.LASR_TYP, "Laser type"),
    LASR_ID(CN,  null, false, CN_t.LASR_ID, "Laser ID"),
    EXTR_TYP(CN, null, false, CN_t.EXTR_TYP, "Extra equipment type field"),
    EXTR_ID(CN,  null, false, CN_t.EXTR_ID, "Extra equipment ID");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private SDR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
    {
        this.type = type;
        this.required = required;
        this.countField = countField;
        this.description = description;
        this.kind = kind;
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

