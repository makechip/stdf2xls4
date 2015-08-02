package com.makechip.stdf2xls4.excel.xlsx.layout;

import static com.makechip.stdf2xls4.excel.xlsx.BorderStyle_t.*;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.BLACK;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.BLUE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.BRIGHT_GREEN;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.LIGHT_BLUE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.MC_BLUE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.PINK;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.RED;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.SKY_BLUE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.TURQUOISE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.WHITE;
import static com.makechip.stdf2xls4.excel.xlsx.Color_t.YELLOW;
import static com.makechip.stdf2xls4.excel.xlsx.Font_t.*;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.excel.xlsx.BorderStyle_t;
import com.makechip.stdf2xls4.excel.xlsx.CellFormat;
import com.makechip.stdf2xls4.excel.xlsx.Color_t;
import com.makechip.stdf2xls4.excel.xlsx.Font_t;

public enum Format_t implements CellFormat
{
    TEST_NUMBER_FMT(ARIAL_NORMAL,         10, CellStyle.ALIGN_LEFT,   CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT(ARIAL_NORMAL,           10, CellStyle.ALIGN_LEFT,   CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT_WRAP(ARIAL_NORMAL,      10, CellStyle.ALIGN_JUSTIFY,CellStyle.VERTICAL_CENTER, true,  WHITE,        BLACK, THIN, BLACK),
    TITLE_FMT(ARIAL_BOLD,                 20, CellStyle.ALIGN_LEFT,   CellStyle.VERTICAL_CENTER, false, SKY_BLUE,     WHITE, THIN, BLACK),
    LOGO_FMT(ARIAL_BOLD,                  24, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,      MC_BLUE, THIN, BLACK),
    LO_LIMIT_FMT(ARIAL_NORMAL,            10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HI_LIMIT_FMT(ARIAL_NORMAL,            10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    UNIT_FMT(ARIAL_NORMAL,                10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    DATA_FMT(ARIAL_NORMAL,                10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HEADER1_FMT(ARIAL_BOLD,                8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HEADER2_FMT(ARIAL_BOLD,                8, CellStyle.ALIGN_RIGHT,  CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HEADER3_FMT(ARIAL_NORMAL,             10, CellStyle.ALIGN_LEFT,   CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HEADER4_FMT(ARIAL_BOLD,                8, CellStyle.ALIGN_RIGHT,  CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    HEADER4_FMTR(ARIAL_BOLD,               8, CellStyle.ALIGN_RIGHT,  CellStyle.VERTICAL_BOTTOM, false, WHITE,        BLACK, THIN, BLACK),
    HEADER5_FMT(ARIAL_BOLD,                8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    PASS_VALUE_FMT(COURIER_NORMAL,        10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    FAIL_VALUE_FMT(COURIER_NORMAL,        10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, RED,          BLACK, THIN, BLACK),
    INVALID_VALUE_FMT(COURIER_NORMAL,     10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, BRIGHT_GREEN, BLACK, THIN, BLACK),
    UNRELIABLE_VALUE_FMT(COURIER_NORMAL,  10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, LIGHT_BLUE,   BLACK, THIN, BLACK),
    ALARM_VALUE_FMT(COURIER_NORMAL,       10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, YELLOW,       BLACK, THIN, BLACK),
    TIMEOUT_VALUE_FMT(COURIER_NORMAL,     10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, PINK,         BLACK, THIN, BLACK),
    ABORT_VALUE_FMT(COURIER_NORMAL,       10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, TURQUOISE,    BLACK, THIN, BLACK),
    STATUS_PASS_FMT(COURIER_NORMAL,       10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, WHITE,        BLACK, THIN, BLACK),
    STATUS_FAIL_FMT(COURIER_NORMAL,       10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, RED,          BLACK, THIN, BLACK),
    STATUS_INVALID_FMT(COURIER_NORMAL,    10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, BRIGHT_GREEN, BLACK, THIN, BLACK),
    STATUS_UNRELIABLE_FMT(COURIER_NORMAL, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, BLUE,         BLACK, THIN, BLACK),
    STATUS_ALARM_FMT(COURIER_NORMAL,      10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, YELLOW,       BLACK, THIN, BLACK),
    STATUS_TIMEOUT_FMT(COURIER_NORMAL,    10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, PINK,         BLACK, THIN, BLACK),
    STATUS_ABORT_FMT(COURIER_NORMAL,      10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, false, TURQUOISE,    BLACK, THIN, BLACK);
    
    public static final String[] PRECISION = { "0.000", "0.0", "0.00", "0.000", "0.0000", "0.00000", "0.000000", "0.0000000",
                                               "0.00000000", "0.000000000", "0.0000000000", "0.00000000000", "0.00000000000" };
    public final Font_t font;
    public final short hAlign;
    public final short vAlign;
    public final Color_t bgColor;
    public final Color_t fgColor;
    public final boolean wrap;
    public final BorderStyle_t borderStyle;
    public final Color_t borderColor;
    public final int fontSize;
    
    private static Map<Format_t, TIntObjectHashMap<CellStyle>> map = new EnumMap<>(Format_t.class);
    
    public static void clear() { map.clear(); }
    
    private Format_t(Font_t font, int fontSize, short hAlign, short vAlign, boolean wrap, 
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
    
    public CellStyle getFormat(XSSFWorkbook wb)
    {
        TIntObjectHashMap<CellStyle> m = map.get(this);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(this, m);
        }
        CellStyle f = m.get(3);
        if (f == null)
        {
        	f = wb.createCellStyle();
            f.setFont(font.getFont(wb, fontSize, fgColor.color));
        	f.setAlignment(hAlign);
        	f.setVerticalAlignment(vAlign);
        	f.setFillForegroundColor(bgColor.index);
        	f.setFillPattern(CellStyle.SOLID_FOREGROUND);
        	f.setFillBackgroundColor(bgColor.index);
        	f.setBorderBottom(borderStyle.getStyle());
        	f.setBorderTop(borderStyle.getStyle());
        	f.setBorderLeft(borderStyle.getStyle());
        	f.setBorderRight(borderStyle.getStyle());
            f.setBottomBorderColor(borderColor.index);
            f.setTopBorderColor(borderColor.index);
            f.setLeftBorderColor(borderColor.index);
            f.setRightBorderColor(borderColor.index);
            m.put(3, f);
        }
        return(f);
    }
    
    public CellStyle getFormat(XSSFWorkbook wb, int precision)
    {
        TIntObjectHashMap<CellStyle> m = map.get(this);
        if (m == null)
        {
        	m = new TIntObjectHashMap<>();
        	map.put(this, m);
        }
        CellStyle f = m.get(3);
        if (f == null)
        {
        	f = wb.createCellStyle();
            f.setFont(font.getFont(wb, fontSize, fgColor.color));
        	f.setAlignment(hAlign);
        	f.setVerticalAlignment(vAlign);
        	f.setFillForegroundColor(bgColor.index);
        	f.setFillPattern(CellStyle.SOLID_FOREGROUND);
        	f.setFillBackgroundColor(bgColor.index);
        	f.setBorderBottom(borderStyle.getStyle());
        	f.setBorderTop(borderStyle.getStyle());
        	f.setBorderLeft(borderStyle.getStyle());
        	f.setBorderRight(borderStyle.getStyle());
            f.setBottomBorderColor(borderColor.index);
            f.setTopBorderColor(borderColor.index);
            f.setLeftBorderColor(borderColor.index);
            f.setRightBorderColor(borderColor.index);
            XSSFDataFormat df = wb.createDataFormat();
            f.setDataFormat(df.getFormat(PRECISION[precision]));
            m.put(3, f);
        }
        return(f);
    }
    
    
}
