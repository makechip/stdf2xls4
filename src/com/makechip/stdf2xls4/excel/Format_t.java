package com.makechip.stdf2xls4.excel;

import static com.makechip.stdf2xls4.excel.BorderStyle_t.*;
import static com.makechip.stdf2xls4.excel.Color_t.BLACK;
import static com.makechip.stdf2xls4.excel.Color_t.BLUE;
import static com.makechip.stdf2xls4.excel.Color_t.AQUA;
import static com.makechip.stdf2xls4.excel.Color_t.LIGHT_BLUE;
import static com.makechip.stdf2xls4.excel.Color_t.MC_BLUE;
import static com.makechip.stdf2xls4.excel.Color_t.PINK;
import static com.makechip.stdf2xls4.excel.Color_t.RED;
import static com.makechip.stdf2xls4.excel.Color_t.SKY_BLUE;
import static com.makechip.stdf2xls4.excel.Color_t.TURQUOISE;
import static com.makechip.stdf2xls4.excel.Color_t.WHITE;
import static com.makechip.stdf2xls4.excel.Color_t.YELLOW;
import static com.makechip.stdf2xls4.excel.Font_t.*;
import static jxl.format.VerticalAlignment.*;

import com.makechip.stdf2xls4.excel.Color_t;

import jxl.format.Alignment;
import jxl.format.VerticalAlignment;

public enum Format_t
{
    TEST_NUMBER_FMT(ARIAL_NORMAL,         10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT(ARIAL_NORMAL,           10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    TEST_NAME_FMT_WRAP(ARIAL_NORMAL,      10, Alignment.JUSTIFY,CENTRE, true,  WHITE,        BLACK, THIN, BLACK),
    TITLE_FMT(ARIAL_BOLD,                 20, Alignment.LEFT,   CENTRE, false, SKY_BLUE,     WHITE, THIN, BLACK),
    LOGO_FMT(ARIAL_BOLD,                  20, Alignment.CENTRE, CENTRE, false, WHITE,      MC_BLUE, THIN, BLACK),
    LO_LIMIT_FMT(ARIAL_NORMAL,            10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HI_LIMIT_FMT(ARIAL_NORMAL,            10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    UNIT_FMT(ARIAL_NORMAL,                10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    DATA_FMT(ARIAL_NORMAL,                10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER1_FMT(ARIAL_BOLD,                8, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER2_FMT(ARIAL_BOLD,                8, Alignment.RIGHT,  CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER3_FMT(ARIAL_NORMAL,             10, Alignment.LEFT,   CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER4_FMT(ARIAL_BOLD,                8, Alignment.RIGHT,  CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    HEADER4_FMTR(ARIAL_BOLD,               8, Alignment.CENTRE, BOTTOM, false, WHITE,        BLACK, THIN, BLACK),
    HEADER5_FMT(ARIAL_BOLD,                8, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    PASS_VALUE_FMT(COURIER_NORMAL,        10, Alignment.RIGHT,  CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    FAIL_VALUE_FMT(COURIER_NORMAL,        10, Alignment.RIGHT,  CENTRE, false, RED,          BLACK, THIN, BLACK),
    INVALID_VALUE_FMT(COURIER_NORMAL,     10, Alignment.RIGHT,  CENTRE, false, AQUA,         BLACK, THIN, BLACK),
    UNRELIABLE_VALUE_FMT(COURIER_NORMAL,  10, Alignment.RIGHT,  CENTRE, false, LIGHT_BLUE,   BLACK, THIN, BLACK),
    ALARM_VALUE_FMT(COURIER_NORMAL,       10, Alignment.RIGHT,  CENTRE, false, YELLOW,       BLACK, THIN, BLACK),
    TIMEOUT_VALUE_FMT(COURIER_NORMAL,     10, Alignment.RIGHT,  CENTRE, false, PINK,         BLACK, THIN, BLACK),
    ABORT_VALUE_FMT(COURIER_NORMAL,       10, Alignment.RIGHT,  CENTRE, false, TURQUOISE,    BLACK, THIN, BLACK),
    PASS_VALUE_FMTC(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    FAIL_VALUE_FMTC(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, RED,          BLACK, THIN, BLACK),
    INVALID_VALUE_FMTC(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, AQUA,         BLACK, THIN, BLACK),
    UNRELIABLE_VALUE_FMTC(COURIER_NORMAL, 10, Alignment.CENTRE, CENTRE, false, LIGHT_BLUE,   BLACK, THIN, BLACK),
    ALARM_VALUE_FMTC(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, YELLOW,       BLACK, THIN, BLACK),
    TIMEOUT_VALUE_FMTC(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, PINK,         BLACK, THIN, BLACK),
    ABORT_VALUE_FMTC(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, TURQUOISE,    BLACK, THIN, BLACK),
    STATUS_PASS_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, WHITE,        BLACK, THIN, BLACK),
    STATUS_FAIL_FMT(COURIER_NORMAL,       10, Alignment.CENTRE, CENTRE, false, RED,          BLACK, THIN, BLACK),
    STATUS_INVALID_FMT(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, AQUA,         BLACK, THIN, BLACK),
    STATUS_UNRELIABLE_FMT(COURIER_NORMAL, 10, Alignment.CENTRE, CENTRE, false, BLUE,         BLACK, THIN, BLACK),
    STATUS_ALARM_FMT(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, YELLOW,       BLACK, THIN, BLACK),
    STATUS_TIMEOUT_FMT(COURIER_NORMAL,    10, Alignment.CENTRE, CENTRE, false, PINK,         BLACK, THIN, BLACK),
    STATUS_ABORT_FMT(COURIER_NORMAL,      10, Alignment.CENTRE, CENTRE, false, TURQUOISE,    BLACK, THIN, BLACK);
    
    /**
	 * @return the font
	 */
	public Font_t getFont()
	{
		return font;
	}

	/**
	 * @return the hAlign
	 */
	public Alignment getHAlign()
	{
		return hAlign;
	}

	/**
	 * @return the vAlign
	 */
	public VerticalAlignment getVAlign()
	{
		return vAlign;
	}

	/**
	 * @return the bgColor
	 */
	public Color_t getBgColor()
	{
		return bgColor;
	}

	/**
	 * @return the fgColor
	 */
	public Color_t getFgColor()
	{
		return fgColor;
	}

	/**
	 * @return the wrap
	 */
	public boolean isWrap()
	{
		return wrap;
	}

	/**
	 * @return the borderStyle
	 */
	public BorderStyle_t getBorderStyle()
	{
		return borderStyle;
	}

	/**
	 * @return the borderColor
	 */
	public Color_t getBorderColor()
	{
		return borderColor;
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize()
	{
		return fontSize;
	}

	private final Font_t font;
    private final Alignment hAlign;
    private final VerticalAlignment vAlign;
    private final Color_t bgColor;
    private final Color_t fgColor;
    private final boolean wrap;
    private final BorderStyle_t borderStyle;
    private final Color_t borderColor;
    private final int fontSize;
    
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
    
    
}
