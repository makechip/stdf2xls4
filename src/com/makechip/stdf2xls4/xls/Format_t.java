package com.makechip.stdf2xls4.xls;

import static com.makechip.stdf2xls4.xls.Alignment_t.CENTER;
import static com.makechip.stdf2xls4.xls.Alignment_t.LEFT;
import static com.makechip.stdf2xls4.xls.Alignment_t.RIGHT;
import static com.makechip.stdf2xls4.xls.Color_t.BLACK;
import static com.makechip.stdf2xls4.xls.Color_t.BLUE;
import static com.makechip.stdf2xls4.xls.Color_t.BRIGHT_GREEN;
import static com.makechip.stdf2xls4.xls.Color_t.LIGHT_BLUE;
import static com.makechip.stdf2xls4.xls.Color_t.PINK;
import static com.makechip.stdf2xls4.xls.Color_t.RED;
import static com.makechip.stdf2xls4.xls.Color_t.SKY_BLUE;
import static com.makechip.stdf2xls4.xls.Color_t.TURQUOISE;
import static com.makechip.stdf2xls4.xls.Color_t.WHITE;
import static com.makechip.stdf2xls4.xls.Color_t.YELLOW;
import static com.makechip.stdf2xls4.xls.FontStyle_t.BOLD;
import static com.makechip.stdf2xls4.xls.FontStyle_t.NORMAL;
import static com.makechip.stdf2xls4.xls.Font_t.ARIAL;
import static com.makechip.stdf2xls4.xls.Font_t.COURIER;
import jxl.write.WritableCellFormat;

public enum Format_t
{
    TEST_NUMBER_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(LEFT, WHITE, 10, ARIAL, NORMAL, BLACK))),
    TEST_NAME_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(LEFT, WHITE, 10, ARIAL, NORMAL, BLACK))),
    TITLE_FMT(FormatFactory.getFormat(FormatFactory.getMiddleAlignedFormatHandle(LEFT, SKY_BLUE, 20, ARIAL, BOLD, WHITE))),
    LO_LIMIT_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, ARIAL, NORMAL, BLACK, "0.000"))),
    HI_LIMIT_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, ARIAL, NORMAL, BLACK, "0.000"))),
    UNIT_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, ARIAL, NORMAL, BLACK))),
    DATA_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, ARIAL, NORMAL, BLACK))),
    HEADER1_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 8, ARIAL, BOLD, BLACK))),
    HEADER2_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(RIGHT, WHITE, 10, ARIAL, BOLD, BLACK))),
    HEADER3_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(LEFT, WHITE, 10, ARIAL, NORMAL, BLACK))),
    HEADER4_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(RIGHT, WHITE, 8, ARIAL, BOLD, BLACK))),
    HEADER5_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 8, ARIAL, BOLD, BLACK,"0.000"))),
    PASS_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    FAIL_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, RED, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    INVALID_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, BRIGHT_GREEN, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    UNRELIABLE_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, LIGHT_BLUE, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    ALARM_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, YELLOW, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    TIMEOUT_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, PINK, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    ABORT_VALUE_HP_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, TURQUOISE, 10, COURIER, NORMAL, BLACK, "0.0000"))),
    PASS_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, COURIER, NORMAL, BLACK, "0.000"))),
    FAIL_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, RED, 10, COURIER, NORMAL, BLACK, "0.000"))),
    INVALID_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, BRIGHT_GREEN, 10, COURIER, NORMAL, BLACK, "0.000"))),
    UNRELIABLE_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, LIGHT_BLUE, 10, COURIER, NORMAL, BLACK, "0.000"))),
    ALARM_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, YELLOW, 10, COURIER, NORMAL, BLACK, "0.000"))),
    TIMEOUT_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, PINK, 10, COURIER, NORMAL, BLACK, "0.000"))),
    ABORT_VALUE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, TURQUOISE, 10, COURIER, NORMAL, BLACK, "0.000"))),
    STATUS_PASS_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, WHITE, 10, COURIER, NORMAL, BLACK))),
    STATUS_FAIL_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, RED, 10, COURIER, NORMAL, BLACK))),
    STATUS_INVALID_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, BRIGHT_GREEN, 10, COURIER, NORMAL, BLACK))),
    STATUS_UNRELIABLE_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, BLUE, 10, COURIER, NORMAL, BLACK))),
    STATUS_ALARM_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, YELLOW, 10, COURIER, NORMAL, BLACK))),
    STATUS_TIMEOUT_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, PINK, 10, COURIER, NORMAL, BLACK))),
    STATUS_ABORT_FMT(FormatFactory.getFormat(FormatFactory.getFormatHandle(CENTER, TURQUOISE, 10, COURIER, NORMAL, BLACK)));
    
    private WritableCellFormat fmt;
    
    private Format_t(WritableCellFormat fmt)
    {
        this.fmt = fmt;
    }
    
    public WritableCellFormat getFormat() { return(fmt); }
    
}
