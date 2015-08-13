package com.makechip.stdf2xls4.stdf.enums.descriptors;

import static com.makechip.stdf2xls4.stdf.enums.Data_t.C1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.CN;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U1;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U2;
import static com.makechip.stdf2xls4.stdf.enums.Data_t.U4;

import com.makechip.stdf2xls4.stdf.enums.types.*;
import com.makechip.stdf2xls4.stdf.enums.Data_t;

public enum MIR_t implements FieldDescriptor
{
    SETUP_T(U4,   null, true, U4_t.SETUP_T, "Date and time of job setup"),
    START_T(U4,   null, true, U4_t.START_T, "Data and time first part tested"),
    STAT_NUM(U1,  null, true, U1_t.STAT_NUM, "Test station number"),
    MODE_COD(C1,  null, true, C1_t.MODE_COD, "Test mode code (e.g. prod, dev)"),
    RTST_COD(C1,  null, true, C1_t.RTST_COD, "Lot retest code"),
    PROT_COD(C1,  null, true, C1_t.PROT_COD, "Data protection code"),
    BURN_TIM(U2,  null, true, U2_t.BURN_TIM, "Burn-in time (in minutes)"),
    CMOD_COD(C1,  null, true, C1_t.CMOD_COD, "Command mode code"),
    LOT_ID(CN,    null, true, CN_t.LOT_ID, "Lot ID (customer specified)"),
    PART_TYP(CN,  null, true, CN_t.PART_TYP, "Part type (or product ID)"),
    NODE_NAM(CN,  null, true, CN_t.NODE_NAM, "Name of node that generated data"),
    TSTR_TYP(CN,  null, true, CN_t.TSTR_TYP, "Tester type"),
    JOB_NAM(CN,   null, true, CN_t.JOB_NAM, "Job name (test program name)"),
    JOB_REV(CN,   null, false, CN_t.JOB_REV, "Job (test program) revision number"),
    SBLOT_ID(CN,  null, false, CN_t.SBLOT_ID, "Sublot ID"),
    OPER_NAM(CN,  null, false, CN_t.OPER_NAM, "Operator name or ID (at setup time)"),
    EXEC_TYP(CN,  null, false, CN_t.EXEC_TYP, "Tester executive software type"),
    EXEC_VER(CN,  null, false, CN_t.EXEC_VER, "Tester exec software version number"),
    TEST_COD(CN,  null, false, CN_t.TEST_COD, "Test phase or step code"),
    TST_TEMP(CN,  null, false, CN_t.TST_TEMP, "Temperature"),
    USER_TXT(CN,  null, false, CN_t.USER_TXT, "Generic user text"),
    AUX_FILE(CN,  null, false, CN_t.AUX_FILE, "Name of auxilliary data file"),
    PKG_TYP(CN,   null, false, CN_t.PKG_TYP, "Package type"),
    FAMLY_ID(CN,  null, false, CN_t.FAMLY_ID, "Product family ID"),
    DATE_COD(CN,  null, false, CN_t.DATE_COD, "Date code"),
    FACIL_ID(CN,  null, false, CN_t.FACIL_ID, "Test facility ID"),
    FLOOR_ID(CN,  null, false, CN_t.FLOOR_ID, "Test Floor ID"),
    PROC_ID(CN,   null, false, CN_t.PROC_ID, "Fabrication process ID"),
    OPER_FRQ(CN,  null, false, CN_t.OPER_FRQ, "Operation frequency or step"),
    SPEC_NAM(CN,  null, false, CN_t.SPEC_NAM, "Test specification name"),
    SPEC_VER(CN,  null, false, CN_t.SPEC_VER, "Test specification version number"),
    FLOW_ID(CN,   null, false, CN_t.FLOW_ID, "Test flow ID"),
    SETUP_ID(CN,  null, false, CN_t.SETUP_ID, "Test setup ID"),
    DSGN_REV(CN,  null, false, CN_t.DSGN_REV, "Device design revision"),
    ENG_ID(CN,    null, false, CN_t.ENG_ID, "Engineering lot ID"),
    ROM_COD(CN,   null, false, CN_t.ROM_COD, "ROM code ID"),
    SERL_NUM(CN,  null, false, CN_t.SERL_NUM, "Tester serial number"),
    SUPR_NAM(CN,  null, false, CN_t.SUPR_NAM, "Supervisor name or ID");

    private final Data_t type;
    private final boolean required;
    private final String description;
    private final FieldDescriptor countField;
    private final FieldType kind;

    private MIR_t(Data_t type, FieldDescriptor countField, boolean required, FieldType kind, String description)
    {
        this.type = type;
        this.countField = countField;
        this.required = required;
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

