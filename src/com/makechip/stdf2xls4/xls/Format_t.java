package com.makechip.stdf2xls4.xls;

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
import static com.makechip.stdf2xls4.xls.Color_t.MC_BLUE;
import static com.makechip.stdf2xls4.xls.Font_t.*;
import static com.makechip.stdf2xls4.xls.BorderStyle_t.*;
import static jxl.format.VerticalAlignment.*;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;
import java.util.Map;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.VerticalAlignment;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public enum Format_t
{
    TEST_NUMBER_FMT(ARIAL_NORMAL,         10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT(ARIAL_NORMAL,           10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT_WRAP(ARIAL_NORMAL,      10, Alignment.JUSTIFY,CENTRE, true,  WHITE,        BLACK, THIN, BLACK),
    TITLE_FMT(ARIAL_BOLD,                 20, Alignment.LEFT,   CENTRE, false, SKY_BLUE,     BLACK, THIN, WHITE),
    LOGO_FMT(ARIAL_BOLD,                  24, Alignment.LEFT,   CENTRE, false, MC_BLUE,      BLACK, THIN, WHITE),
    LO_LIMIT_FMT(ARIAL_NORMAL,            10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HI_LIMIT_FMT(ARIAL_NORMAL,            10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    UNIT_FMT(ARIAL_NORMAL,                10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    DATA_FMT(ARIAL_NORMAL,                10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER1_FMT(ARIAL_BOLD,                8, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER2_FMT(ARIAL_BOLD,                8, Alignment.RIGHT,  CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER3_FMT(ARIAL_NORMAL,             10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER4_FMT(ARIAL_BOLD,                8, Alignment.RIGHT,  CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER5_FMT(ARIAL_BOLD,                8, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    PASS_VALUE_FMT(COURIER_NORMAL,        10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    FAIL_VALUE_FMT(COURIER_NORMAL,        10, Alignment.CENTRE, CENTRE, false, RED,          BLACK, THIN, BLACK),
    INVALID_VALUE_FMT(COURIER_NORMAL,     10, Alignment.CENTRE, CENTRE, false, BRIGHT_GREEN, BLACK, THIN, BLACK),
    UNRELIABLE_VALUE_FMT(COURIER_NORMAL,  10, Alignment.CENTRE, CENTRE, false, LIGHT_BLUE,   BLACK, THIN, BLACK),
    ALARM_VALUE_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, YELLOW,       BLACK, THIN, BLACK),
    TIMEOUT_VALUE_FMT(COURIER_NORMAL,     10, Alignment.CENTRE, CENTRE, false, PINK,         BLACK, THIN, BLACK),
    ABORT_VALUE_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, TURQUOISE,    BLACK, THIN, BLACK),
    STATUS_PASS_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    STATUS_FAIL_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, RED,          BLACK, THIN, BLACK),
    STATUS_INVALID_FMT(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, BRIGHT_GREEN, BLACK, THIN, BLACK),
    STATUS_UNRELIABLE_FMT(COURIER_NORMAL, 10, Alignment.CENTRE, CENTRE, false, BLUE,         BLACK, THIN, BLACK),
    STATUS_ALARM_FMT(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, YELLOW,       BLACK, THIN, BLACK),
    STATUS_TIMEOUT_FMT(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, PINK,         BLACK, THIN, BLACK),
    STATUS_ABORT_FMT(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, TURQUOISE,    BLACK, THIN, BLACK);
    
    public static final String[] PRECISION = { "0.000", "0.0", "0.00", "0.000", "0.0000", "0.00000", "0.000000", "0.0000000",
                                               "0.00000000", "0.000000000", "0.0000000000", "0.00000000000", "0.00000000000" };
    public final Font_t font;
    public final Alignment hAlign;
    public final VerticalAlignment vAlign;
    public final Color_t bgColor;
    public final Color_t fgColor;
    public final boolean wrap;
    public final BorderStyle_t borderStyle;
    public final Color_t borderColor;
    public final int fontSize;
    
    private static Map<Format_t, TIntObjectHashMap<WritableCellFormat>> map = new EnumMap<>(Format_t.class);
    
    private Format_t(Font_t font, int fontSize, Alignment hAlign, VerticalAlignment vAlign, boolean wrap, 
    		         Color_t bgColor, Color_t fgColor, BorderStyle_t borderStyle, Color_t borderColor)
    {
    	this.font = font;
    	this.fontSize = fontSize;
    	this.hAlign = hAlign;
    	this.vAlign = vAlign;
    	this.wrap = wrap;
    	this.bgColor = bgColor;
    	this.fgColor = fgColor;
    	this.borderStyle = borderStyle;
    	this.borderColor = borderColor;
    }
    
    public WritableCellFormat getFormat() throws WriteException 
    {
        TIntObjectHashMap<WritableCellFormat> m = map.get(this);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(this, m);
        }
        WritableCellFormat f = m.get(3);
        if (f == null)
        {
        	WritableFont wf = font.getFont(fontSize, fgColor);
        	f = new WritableCellFormat(wf);
        	f.setAlignment(hAlign);
        	f.setBackground(bgColor.color);
        	f.setBorder(Border.ALL, borderStyle.style, borderColor.color);
        	f.setVerticalAlignment(vAlign);
        	f.setWrap(wrap);
        }
        return(f);
    }
    
    public WritableCellFormat getFormat(int precision) throws WriteException
    {
        TIntObjectHashMap<WritableCellFormat> m = map.get(this);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(this, m);
        }
        WritableCellFormat f = m.get(3);
        if (f == null)
        {
        	WritableFont wf = font.getFont(fontSize, fgColor);
        	f = new WritableCellFormat(wf, new NumberFormat(PRECISION[precision]));
        	f.setAlignment(hAlign);
        	f.setBackground(bgColor.color);
        	f.setBorder(Border.ALL, borderStyle.style, borderColor.color);
        	f.setVerticalAlignment(vAlign);
        	f.setWrap(wrap);
        }
        return(f);
    }
    
}
