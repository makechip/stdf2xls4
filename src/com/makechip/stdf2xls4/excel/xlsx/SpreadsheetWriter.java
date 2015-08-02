package com.makechip.stdf2xls4.excel.xlsx;

import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_ABORT_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_ALARM_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_FAIL_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_INVALID_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_PASS_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_TIMEOUT_FMT;
import static com.makechip.stdf2xls4.excel.xlsx.layout.Format_t.STATUS_UNRELIABLE_FMT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.CellFormat;
import com.makechip.stdf2xls4.excel.Cell_t;
import com.makechip.stdf2xls4.excel.SheetName;
import com.makechip.stdf2xls4.excel.xlsx.layout.CornerBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout.PageTitleBlock;
import com.makechip.stdf2xls4.stdf.StdfException;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.stdfapi.TestResult;
import com.makechip.util.Log;

public abstract class SpreadsheetWriter
{
	public static final int MAX_ROWS = 1000000;
	public static final int COLS_PER_PAGE = 200;
	protected final CliOptions options;
	protected final StdfAPI api;
	protected boolean noOverwrite;
	protected XSSFWorkbook wb = null;
	protected XSSFSheet[] ws;
	protected Map<PageHeader, Integer> versionMap;
	protected Map<SheetName, XSSFSheet> sheetMap;

	public SpreadsheetWriter(CliOptions options, StdfAPI api) throws InvalidFormatException, IOException
	{
		this.options = options;
		this.api = api;
        versionMap = new HashMap<>();
        sheetMap = new IdentityHashMap<>();
        wb = null; 
        if (options.xlsName.exists()) wb = new XSSFWorkbook(options.xlsName);
        else wb = new XSSFWorkbook();
        Iterator<XSSFSheet> it = wb.iterator();
        while (it.hasNext())
        {
        	XSSFSheet sh = it.next();
        	String s = sh.getSheetName();
        	SheetName sn = SheetName.getSheet(s);
        	if (!s.equals(sn.toString())) 
        	{
        		throw new RuntimeException("Incorrectly formatted sheet name: " + s + " : " + sn);
        	}
        	sheetMap.put(sn, sh);
        }
        noOverwrite = options.noOverwrite;
	}

    public void generate() throws StdfException, IOException
    {
    	if (options.onePage)
    	{
    		openSheet(api.getPageHeaders().stream().findFirst().orElse(new PageHeader(new HashMap<String, String>())));
    		for (PageHeader hdr : api.getPageHeaders()) writeData(hdr);
    	}
    	else
    	{
    	    for (PageHeader hdr : api.getPageHeaders())
    		{
    	    	Log.msg("HDR: " + hdr.toString());
    			openSheet(hdr);
    		    writeData(hdr);
    		}
    	}
        close();
    }
    
    protected PageHeader getPageHeader(XSSFSheet s)
    {
    	String key = "";
    	int row = PageTitleBlock.HEIGHT;
    	Map<String, String> header  = new LinkedHashMap<>();
    	while (true)
    	{
    		Row r = s.getRow(row);
    		if (r == null) break;
    		Log.msg("r.getCell(0) = " + r.getCell(0));
    		if (r.getCell(0).getCellType() == Cell_t.BLANK.type) break;
    	    key = r.getCell(0).getStringCellValue();
    	    if (key.trim().equals(HeaderBlock.OPTIONS_LABEL)) break;
    	    Cell c = r.getCell(HeaderBlock.VALUE_COL);
    	    String value = (c == null) ? "" : c.getStringCellValue();
    	    header.put(key, value);
    	    row++;
    	    if (row > 100) throw new RuntimeException("Error header in spreadsheet is not compatible with this verison");
    	}
    	return(new PageHeader(header));
    }
    
    protected Cell getExistingCell(XSSFSheet s, int col, int row)
    {
    	Row r = s.getRow(row);
    	return(r.getCell(col));
    }
    
    protected StdfException optionError(boolean oldHas, String opt)
    {
    	String e = null;
    	if (oldHas)
    	{
    	    e = "Existing spreadsheet created with " + opt + " option - try adding " + opt + " command-line switch.";	
    	}
    	else
    	{
    		e = "Existing spreadsheet not created with " + opt + " option - try removing " + opt + " command-line switch.";
    	}
    	return(new StdfException(e));
    }
    
    protected int getOptRow(XSSFSheet wsi, int startRow)
    {
    	int optRow = -1;
    	for (int row=startRow; row<100; row++)
    	{
    		Row r = wsi.getRow(row);
    		Cell c = r.getCell(0);
    		if (c.getCellType() == Cell_t.STRING.type)
    		{
    			String s = c.getStringCellValue();
    			if (s.equals("OPTIONS:"))
    			{
    				optRow = row;
    				break;
    			}
    		}
    	}
    	return(optRow);
    }
	
    protected boolean checkRegistration(XSSFSheet wsi, int startRow, int tnameCol, int tnameRow) throws StdfException
    {
    	// Check for option compatibility:
    	int optRow = getOptRow(wsi, startRow); 
    	if (optRow < 0) throw new StdfException("Existing spreadsheet is incompatible");
    	Row r = wsi.getRow(optRow);
    	String oldOpts = r.getCell(3).getStringCellValue();
    	Log.msg("oldOpts = " + oldOpts);
    	// -b onePage mismatch = error
    	if (oldOpts.contains("-b") && !options.onePage) throw optionError(true, "-b");
    	if (!oldOpts.contains("-b") && options.onePage) throw optionError(false, "-b");
    	// -v dontSkipSearchFails mismatch = error
    	if (oldOpts.contains("-v") && !options.dontSkipSearchFails) throw optionError(true, "-v");
        if (!oldOpts.contains("-v") && options.dontSkipSearchFails) throw optionError(false, "-v");	
    	// -r rotate mismatch = error
        if (oldOpts.contains("-r") && !options.rotate) throw optionError(true, "-r");
        if (!oldOpts.contains("-r") && options.rotate) throw optionError(false, "-r");
    	// -y dynamicLimits = error
        if (oldOpts.contains("-y") && !options.rotate) throw optionError(true, "-r");
        if (!oldOpts.contains("-y") && options.rotate) throw optionError(false, "-r");
    	// -n noWrapTestNames mismatch = warning
        if (oldOpts.contains("-n") && !options.noWrapTestNames)
        {
        	Log.warning("Existing spreadsheet does not wrap test names - will assume -n option is set.");
        }
        if (!oldOpts.contains("-n") && options.noWrapTestNames)
        {
        	Log.warning("Existing spreadsheet wraps test names - ignoring -n option.");
        }
    	// -p precision mismatch = warning
        String oldp = oldOpts.substring(oldOpts.lastIndexOf(' ')).trim();
        if (Character.isDigit(oldp.charAt(0)))
        {
        	Integer n = new Integer(oldp);
        	if (options.precision != n.intValue())
        	{
        		Log.warning("Existing spreadsheet uses different precision.");
        	}
        }
    	// -a pinSuffix mismatch = warning
        if (oldOpts.contains("-a") && !options.pinSuffix)
        {
        	Log.warning("Existing spreadsheet uses -a option, while current run does not.");
        }
        if (!oldOpts.contains("-a") && options.pinSuffix)
        {
            Log.warning("Existing spreadsheet does not use -a option, while current run does.");
        }
    	r = wsi.getRow(tnameRow);
    	Cell c = r.getCell(tnameCol);
    	if (c.getCellType() == Cell_t.STRING.type)
    	{
    		String text = c.getStringCellValue();
    		if (text.equals(CornerBlock.LABEL_TEST_NAME)) return(true);
    	}
    	return(false);
    }
    
    protected void setStatus(XSSFSheet wsi, int col, int row, TestResult r)
    {
    	Row rw = wsi.getRow(row);
    	if (rw == null) rw = wsi.createRow(row);
        switch (r.error)
        {
        case PASS:       Block.setCell(rw, col, STATUS_PASS_FMT.getFormat(wb), "PASS"); break;
        case FAIL:       Block.setCell(rw, col, STATUS_FAIL_FMT.getFormat(wb), "FAIL"); break;
        case INVALID:    Block.setCell(rw, col, STATUS_INVALID_FMT.getFormat(wb), "FAIL"); break;
        case UNRELIABLE: Block.setCell(rw, col, STATUS_UNRELIABLE_FMT.getFormat(wb), "FAIL"); break;
        case ALARM:      Block.setCell(rw, col, STATUS_ALARM_FMT.getFormat(wb), "FAIL"); break;
        case TIMEOUT:    Block.setCell(rw, col, STATUS_TIMEOUT_FMT.getFormat(wb), "FAIL"); break;
        default:         Block.setCell(rw, col, STATUS_ABORT_FMT.getFormat(wb), "FAIL"); break;
        }
    }
    
   protected void setText(XSSFSheet wsi, int col, int row, CellFormat format, String text)
    {
        String s = text.trim();
        int size = wsi.getColumnWidth(col);
        if (size < (s.length() * 256))
        {   
            wsi.setColumnWidth(col, 256*(14 * s.length())/10);
        }   
        Block.setCell(wsi, col, row, format.getFormat(wb), text.trim());
    }
    
    public void close() throws IOException
    {
        if (ws == null) return;
        //for (int i=0; i<ws.length; i++)
        //{
        //    Row r1 = ws[i].getRow(20);
        //    Cell c = r1.getCell(8);
        //    if (c != null) c.setAsActiveCell();
        //}
        options.xlsName.delete();
        FileOutputStream fos = new FileOutputStream(options.xlsName);
        wb.write(fos);
        wb.close();
        fos.close();
    }                                 
    
    protected abstract void writeData(PageHeader hdr);
    protected abstract void openSheet(PageHeader hdr) throws IOException, StdfException;
        
}
