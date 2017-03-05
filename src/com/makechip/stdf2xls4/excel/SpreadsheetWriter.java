package com.makechip.stdf2xls4.excel;

import static com.makechip.stdf2xls4.excel.Format_t.STATUS_ABORT_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_ALARM_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_FAIL_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_INVALID_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_PASS_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_TIMEOUT_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.STATUS_UNRELIABLE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.ABORT_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.ALARM_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.FAIL_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.INVALID_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.PASS_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TIMEOUT_VALUE_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.UNRELIABLE_VALUE_FMT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.layout.CornerBlock;
import com.makechip.stdf2xls4.excel.layout.HeaderBlock;
import com.makechip.stdf2xls4.excel.layout.PageTitleBlock;
import com.makechip.stdf2xls4.excel.layout.TitleBlock;
import com.makechip.stdf2xls4.excel.layout.LegendBlock;
import com.makechip.stdf2xls4.stdfapi.DatalogTestResult;
import com.makechip.stdf2xls4.stdfapi.DeviceHeader;
import com.makechip.stdf2xls4.stdfapi.HeaderUtil;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestResult;
import com.makechip.stdf2xls4.stdfapi.SnOrXy;
import com.makechip.stdf2xls4.stdfapi.StdfAPI;
import com.makechip.stdf2xls4.stdfapi.TestHeader;
import com.makechip.stdf2xls4.stdfapi.TestResult;
import com.makechip.stdf2xls4.stdfapi.TimeSN;
import com.makechip.stdf2xls4.stdfapi.TimeXY;
import com.makechip.stdf2xls4.stdfapi.XY;
import com.makechip.util.Log;

import gnu.trove.map.hash.TIntObjectHashMap;
import jxl.read.biff.BiffException;

public final class SpreadsheetWriter
{
	public static final int MAX_ROWS = 1000000;
	public static final String DUMMY_SHEET_NAME = "STEP QXU Page 0 V0";
	public static int COLS_PER_PAGE = 1024;
	private final CliOptions options;
	private final StdfAPI api;
	private TIntObjectHashMap<TitleBlock> titles;
	private final Spreadsheet ss;
	private int currentRC;

	public SpreadsheetWriter(CliOptions options, StdfAPI api, Spreadsheet ss) throws InvalidFormatException, IOException, BiffException
	{
		this.options = options;
		this.api = api;
		this.ss = ss;
		titles = new TIntObjectHashMap<>();
        ss.openWorkbook(options.xlsName);
        for (int i=0; i<ss.getNumberOfSheets(); i++)
        {
            String sname = ss.getSheetName(i);
            SheetName.getSheet(sname);
        }
        if (options.maxExcelColumns) COLS_PER_PAGE = 16384;
	}
	
    public void generate()
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
    			openSheet(hdr);
    		    writeData(hdr);
    		}
    	}
        ss.close(options.xlsName);
    }
    
    private PageHeader getPageHeader(int page)
    {
    	String key = "";
    	int row = PageTitleBlock.HEIGHT;
    	Map<String, String> header  = new LinkedHashMap<>();
    	while (true)
    	{
    		Cell_t ct = ss.getCellType(page, new Coord(0, row));
    		if (ct == Cell_t.BLANK) break;
    		key = ss.getCellContents(page, new Coord(0, row));
    	    if (key.trim().equals(HeaderBlock.OPTIONS_LABEL)) break;
    	    String val = ss.getCellContents(page, new Coord(HeaderBlock.VALUE_COL, row));
    	    if (val == null) val = "";
    	    header.put(key, val);
    	    row++;
    	    if (row > 100) throw new RuntimeException("Error header in spreadsheet is not compatible with this verison");
    	}
    	return(new PageHeader(header));
    }
    
    private RuntimeException optionError(boolean oldHas, String opt)
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
    	return(new RuntimeException(e));
    }
    
    private int getOptRow(int page, int startRow)
    {
    	int optRow = -1;
    	for (int row=startRow; row<100; row++)
    	{
    		Cell_t ct = ss.getCellType(page, new Coord(0, row));
    		if (ct == Cell_t.STRING)
    		{
    			String s = ss.getCellContents(page, new Coord(0, row));
    			if (s.equals("OPTIONS:"))
    			{
    				optRow = row;
    				break;
    			}
    		}
    	}
    	return(optRow);
    }
	
    private boolean checkRegistration(int page, int startRow, Coord tnamexy)
    {
    	// Check for option compatibility:
    	int optRow = getOptRow(page, startRow); 
    	if (optRow < 0) throw new RuntimeException("Existing spreadsheet is incompatible");
    	String oldOpts = ss.getCellContents(page, new Coord(1, optRow));
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
        if (oldOpts.contains("-y") && !options.dynamicLimits) throw optionError(true, "-y");
        if (!oldOpts.contains("-y") && options.dynamicLimits) throw optionError(false, "-y");
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
        TitleBlock titleBlock = titles.get(page);
    	Cell_t ct = ss.getCellType(page, titleBlock.tstxy.tnameLabel);
    	if (ct == Cell_t.STRING)
    	{
    		String text = ss.getCellContents(page, titleBlock.tstxy.tnameLabel);
    		if (text.equals(CornerBlock.LABEL_TEST_NAME)) return(true);
    	}
    	return(true);
    }
    
    private void setStatus(int page, Coord xy, TestResult r)
    {
        switch (r.error)
        {
        case PASS:       ss.setCell(page, xy, STATUS_PASS_FMT, "PASS"); break;
        case FAIL:       ss.setCell(page, xy, STATUS_FAIL_FMT, "FAIL"); break;
        case INVALID:    ss.setCell(page, xy, STATUS_INVALID_FMT, "FAIL"); break;
        case UNRELIABLE: ss.setCell(page, xy, STATUS_UNRELIABLE_FMT, "FAIL"); break;
        case ALARM:      ss.setCell(page, xy, STATUS_ALARM_FMT, "FAIL"); break;
        case TIMEOUT:    ss.setCell(page, xy, STATUS_TIMEOUT_FMT, "FAIL"); break;
        default:         ss.setCell(page, xy, STATUS_ABORT_FMT, "FAIL"); break;
        }
    }
    
   private void setText(int page, Coord xy, Format_t format, String text)
    {
        String s = text.trim();
        ss.setColumnWidth(page, xy.c, (14 * s.length())/10);
        ss.setCell(page, xy, format, text.trim());
    }
    
    private void writeData(PageHeader hdr)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	int size = options.rotate ? devs.size() : tests.size();
    	final int pages = (size % COLS_PER_PAGE == 0) ? size / COLS_PER_PAGE : 1 + size / COLS_PER_PAGE;
    	IntStream.range(0, pages).forEach(page -> writeResultsOnPage(hdr, page));
    }	
    	
    private void writeResultsOnPage(PageHeader hdr, int page)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
        final int size = devs.size();	
       	final int startIndex = options.rotate ? page * COLS_PER_PAGE : 0;
       	final int endIndex = options.rotate ? startIndex + COLS_PER_PAGE > size ? size : startIndex + COLS_PER_PAGE : size;
       	String waferOrStep = api.wafersort(hdr) ? hdr.get(HeaderUtil.WAFER_ID) : hdr.get(HeaderUtil.STEP);
        IntStream.range(startIndex, endIndex).forEach(devIndex -> writeDeviceResults(hdr, waferOrStep, page, devIndex));
    }
    
    private void writeDeviceResults(PageHeader hdr, String waferOrStep, int page, int devIndex)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	DeviceHeader dh = devs.get(devIndex);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	final int size = tests.size();
        locateRC(api.wafersort(hdr), waferOrStep, dh.snxy, page);
        setDevice(page, waferOrStep, devs.get(devIndex));
        final int startIndex = options.rotate ? 0 : page * COLS_PER_PAGE;
        final int endIndex = options.rotate ? size : (startIndex + COLS_PER_PAGE > size ? size : startIndex + COLS_PER_PAGE);
        IntStream.range(startIndex, endIndex).forEach(i -> setResult(i, page, tests.get(i), hdr, dh));
    } 
    
   private Coord getDevCoord(Coord devCoord)
    {
    	if (options.rotate) return(new Coord(currentRC, devCoord.r));
    	return(new Coord(devCoord.c, currentRC));
    }
    
    private void setDevice(int page, String waferOrStep, DeviceHeader dh)
    {
    	Format_t cs = STATUS_PASS_FMT;
    	TitleBlock titleBlock = titles.get(page);
    	if (dh.snxy instanceof XY || dh.snxy instanceof TimeXY) // wafersort
    	{
    		if (options.onePage)
    		{
    			if (dh.snxy instanceof TimeXY)
    			{
    				ss.setColumnWidth(page, 0, 15);	
    				ss.setCell(page, getDevCoord(titleBlock.devxy.tstamp), cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    			ss.setCell(page, getDevCoord(titleBlock.devxy.wafOrStep), cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeXY)
    			{
    				ss.setCell(page, getDevCoord(titleBlock.devxy.tstamp), cs, ((TimeXY) dh.snxy).getTimeStamp());
    			}
    		}
    		ss.setCell(page, getDevCoord(titleBlock.devxy.x), cs, dh.snxy.getX());
    		ss.setCell(page, getDevCoord(titleBlock.devxy.yOrSn), cs, dh.snxy.getY());
    	}
    	else // FT
    	{
    		if (options.onePage)
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				ss.setCell(page, getDevCoord(titleBlock.devxy.tstamp), cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    			ss.setCell(page, getDevCoord(titleBlock.devxy.wafOrStep), cs, waferOrStep);
    		}
    		else
    		{
    			if (dh.snxy instanceof TimeSN)
    			{
    				ss.setCell(page, getDevCoord(titleBlock.devxy.tstamp), cs, ((TimeSN) dh.snxy).getTimeStamp());
    			}
    		}
    		ss.setCell(page, getDevCoord(titleBlock.devxy.yOrSn), cs, dh.snxy.getSerialNumber());
    	}
    	ss.setCell(page, getDevCoord(titleBlock.devxy.hwBin), cs, dh.hwBin);
    	ss.setCell(page, getDevCoord(titleBlock.devxy.swBin), cs, dh.swBin);
    	ss.setCell(page, getDevCoord(titleBlock.devxy.temp), cs, dh.temperature);
    	if (options.rotate) ss.mergeCells(page, titleBlock.devxy.temp.r, titleBlock.devxy.temp.r+1, currentRC, currentRC);
    	if (dh.fail) ss.setCell(page, getDevCoord(titleBlock.devxy.rslt), STATUS_FAIL_FMT, "FAIL");
    	else ss.setCell(page, getDevCoord(titleBlock.devxy.rslt), cs, "PASS");
    }
    
    private int getRC(TitleBlock titleBlock)
    {
    	if (options.rotate) return(titleBlock.devxy.yOrSnLabel.c + 1);
    	return(titleBlock.devxy.tempLabel.r + 1);
    }
    
    private void locateRC(boolean wafersort, String waferOrStep, SnOrXy snOrXy, int page)
    {
        TitleBlock titleBlock = titles.get(page);
        final int MAX_COLS = titleBlock.devxy.yOrSnLabel.c + 1 + COLS_PER_PAGE;
   		titleBlock.devxy.reset();
        if (wafersort)
        {
        	if (!options.noOverwrite)
        	{
        		for (int rc=getRC(titleBlock); rc<=getRC(titleBlock) + (options.rotate ? MAX_COLS : MAX_ROWS); rc++)
        		{
        			Cell_t ct = ss.getCellType(page, titleBlock.devxy.x);
        			if (ct == Cell_t.BLANK)
        			{
        				currentRC = rc;
        				return;
        			}
        			short x = (short) ss.getCellValue(page, titleBlock.devxy.x);
        			short y = (short) ss.getCellValue(page, titleBlock.devxy.yOrSn);
        			if (options.onePage)
        			{
        				String waf = ss.getCellContents(page, titleBlock.devxy.wafOrStep);
                        if (waf.equals(waferOrStep) && x == snOrXy.getX() && y == snOrXy.getY())
                        {
                        	currentRC = rc;
                        	return;
                        }
        			}
        			else
        			{
        				if (x == snOrXy.getX() && y == snOrXy.getY())
        				{
        					currentRC = rc;
        					return;
        				}
        			}
        			titleBlock.devxy.inc();
        		}
        		throw new RuntimeException("Cannot locate blank or matching X-Y");
        	}
            for (int rc=getRC(titleBlock); rc<=getRC(titleBlock) + (options.rotate ? MAX_COLS : MAX_ROWS); rc++)
            {
                Cell_t ct = ss.getCellType(page, titleBlock.devxy.x);
                if (ct == Cell_t.BLANK)
                {
                    currentRC = rc;
                    break;
                }
       			titleBlock.devxy.inc();
            }
        }
        else // final test
        {
            for (int rc=getRC(titleBlock); rc<=getRC(titleBlock) + (options.rotate ? MAX_COLS : MAX_ROWS); rc++)
            {
            	Cell_t ct = ss.getCellType(page, titleBlock.devxy.yOrSn);
                if (ct == Cell_t.BLANK)
                {
                    currentRC = rc;
                    return;
                }
                if (!options.noOverwrite)
                {
                	String sn = ss.getCellContents(page, titleBlock.devxy.yOrSn);
                	if (options.onePage)
                	{
                		String step = ss.getCellContents(page, titleBlock.devxy.wafOrStep);
                	    if (step.equals(waferOrStep) && sn.equals(snOrXy.getSerialNumber()))
                	    {
                	    	currentRC = rc;
                	    	return;
                	    }
                	}
                	else
                	{
                	    if (sn.equals(snOrXy.getSerialNumber()))
                	    {
                    	    currentRC = rc;
                    	    break;
                	    }
                	}
                }
                titleBlock.devxy.inc();
            }
        }
    }
    
    private void setResult(int index, int page, TestHeader th, PageHeader hdr, DeviceHeader dh)
    {
		TestResult r = api.getRecord(hdr, dh, th);
   	    //int rc = options.rotate ? titleBlock.tstxy.unitsLabel.r + 1 + index : titleBlock.tstxy.unitsLabel.c + 1 + (index % COLS_PER_PAGE);
		TitleBlock titleBlock = titles.get(page);
		int rc = titleBlock.getRC(th.testName, th.testNumber, th.getPin(), th.dupNum);
		if (rc < 0) return; // returning here...
   	    Coord xy = options.rotate ? new Coord(currentRC, rc) : new Coord(rc, currentRC);
		try
		{
		    if (r != null)
		    {
			    if (r instanceof ParametricTestResult)
			    {
				    ParametricTestResult p = (ParametricTestResult) r;
				    setValue(page, xy, p);
			    }
			    else if (r instanceof DatalogTestResult)
			    {
				    DatalogTestResult d = (DatalogTestResult) r;
				    setText(page, xy, STATUS_PASS_FMT, d.result);
			    }
			    else // a functional test
			    {
				    setStatus(page, xy, r);
			    }
		    }
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
    }
    
    private void setValue(int page, Coord xy, ParametricTestResult p)
    {
    	if (p.pass()) ss.setCell(page, xy, PASS_VALUE_FMT, options.precision, p.result);
    	else if (p.noPassFail()) ss.setCell(page, xy, INVALID_VALUE_FMT, options.precision, p.result);
    	else if (p.unreliable()) ss.setCell(page, xy, UNRELIABLE_VALUE_FMT, options.precision, p.result);
    	else if (p.alarm()) ss.setCell(page, xy, ALARM_VALUE_FMT, options.precision, p.result);
    	else if (p.timeout()) ss.setCell(page, xy, TIMEOUT_VALUE_FMT, options.precision, p.result);
    	else if (p.abort()) ss.setCell(page, xy, ABORT_VALUE_FMT, options.precision, p.result);
    	else ss.setCell(page, xy, FAIL_VALUE_FMT, options.precision, p.result);
    	if (p.result >= 10000.0) 
    	{
    	    int digits = 5 + (int) (Math.log(p.result) / Math.log(10.0));
    	    ss.setColumnWidth(page, xy.c, (14 * digits)/10);
    	}
    	else if (p.result <= -1000.0) 
    	{
    	    int digits = 6 + (int) (Math.log(-p.result) / Math.log(10.0));
    	    ss.setColumnWidth(page, xy.c, (14 * digits)/10);
    	}
    }
    
    private void openSheet(PageHeader hdr)
    {
    	List<DeviceHeader> devs = api.getDeviceHeaders(hdr);
    	List<TestHeader> tests = api.getTestHeaders(hdr);
    	int numElems = options.rotate ? devs.size() : tests.size();
        int pages = numElems / COLS_PER_PAGE;
        if (numElems % COLS_PER_PAGE != 0) pages++;
        ss.createNewPages(pages);
        for (int page=0; page<pages; page++)
        {
        	int numDevices = (page == pages - 1) ? (numElems % COLS_PER_PAGE == 0 ? COLS_PER_PAGE : numElems % COLS_PER_PAGE) : COLS_PER_PAGE;
        	SheetName sname = null;
        	int version = 0;
        	while (true)
        	{
        	    sname = SheetName.getExistingSheetName(api.wafersort(hdr), hdr, page+1, version);
        	    if (sname == null) break;
        	    if (ss.initSheet(page, sname) != null) 
        	    {
        	        break;
        	    }
        	    PageHeader ph = getPageHeader(page);
        	    if (ph.equals(hdr)) break;
        	    version++;
        	}
        	if (ss.initSheet(page, sname) == null) 
        	{
        		sname = SheetName.getSheet(api.wafersort(hdr), hdr, page+1, version);
        		newSheet(page, sname, hdr, numDevices);
        	}
        	else // note: an existing sheet might have a titleblock that is incompatible with the current titleblock.
        	{
    	        //List<TestHeader> list = getTestHeaders(ws[page]);
        	    List<TestHeader> list = options.rotate ? api.getTestHeaders(hdr) : getTestHeaders(hdr, page);
    	        TitleBlock titleBlock = new TitleBlock(hdr, options.logoFile, sname.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, list);
    	        titles.put(page, titleBlock);
    	        titleBlock.addBlock(ss, page);
        		if (!checkRegistration(page, LegendBlock.HEIGHT, titleBlock.tstxy.tnameLabel)) 
        		{
        			ss.close(options.xlsName);
        			throw new RuntimeException("Incompatible spreadsheet");
        		}
        	}
        }
    }
        
    private void newSheet(int page, SheetName name, PageHeader hdr, int numDevices)
    {
    	ss.createSheet(page, name);
    	List<TestHeader> list = options.rotate ? api.getTestHeaders(hdr) : getTestHeaders(hdr, page);
    	TitleBlock titleBlock = new TitleBlock(hdr, options.logoFile, name.toString(), api.wafersort(hdr), api.timeStampedFiles, options, numDevices, list);
    	titles.put(page, titleBlock);
    	titleBlock.addBlock(ss, page);
    }
    
    private List<TestHeader> getTestHeaders(PageHeader hdr, int pageIndex)
    {
        List<TestHeader> plist = new ArrayList<>(COLS_PER_PAGE);
        List<TestHeader> list = api.getTestHeaders(hdr);
        int start = COLS_PER_PAGE * pageIndex;
        int end = (start + COLS_PER_PAGE < list.size()) ? start + COLS_PER_PAGE : list.size();
        IntStream.range(start, end).forEach(i -> plist.add(list.get(i)));
        return(plist);
    }
    
   /*
    private List<TestHeader> getTestHeaders(XSSFSheet existingSheet) throws StdfException
    {
        List<TestHeader> plist = new ArrayList<>(COLS_PER_PAGE);
        int cnt = 0;
        int row = titleBlock.tstxy.unitsLabel.r + 1;
        Row r = existingSheet.getRow(row);
        while (ss.getCellType(titleBlock.tstxy.tname.c).getCellType() != Cell_t.BLANK.type)
        {
        	r = existingSheet.getRow(row);
        	Cell tnameCell = r.getCell(titleBlock.tstxy.tname.c);
        	Cell tnumCell  = r.getCell(titleBlock.tstxy.tnum.c);
        	Cell dupCell   = r.getCell(titleBlock.tstxy.dupNum.c);
        	Cell loLimCell = r.getCell(titleBlock.tstxy.loLim.c);
        	Cell hiLimCell = r.getCell(titleBlock.tstxy.hiLim.c);
        	Cell pnameCell = r.getCell(titleBlock.tstxy.pin.c);
        	Cell unitsCell = r.getCell(titleBlock.tstxy.units.c);
        	String tname = tnameCell.getStringCellValue();
           	long tnum = (long) tnumCell.getNumericCellValue();
           	int dnum = (int) dupCell.getNumericCellValue();
        	if (pnameCell.getCellType() == Cell_t.STRING.type) 
        	{
               	String units = unitsCell.getStringCellValue();
                if (loLimCell.getCellType() == Cell_t.BLANK.type && hiLimCell.getCellType() == Cell_t.BLANK.type)
                {
                    if (pnameCell.getStringCellValue().equals(MultiParametricTestHeader.LL_HDR)) // A LoLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.LO_LIMIT));
                    }
                    else if (pnameCell.getStringCellValue().equals(MultiParametricTestHeader.HL_HDR)) // A HiLimit header
                    {
                    	plist.add(new MultiParametricTestHeader(tname, tnum, dnum, units, Limit_t.HI_LIMIT));
                    }
                    else throw new StdfException("Malformed test header: testName = " + tname);
                }
                else // either a ParametricTest with pin, or a MultiParametricTest
                {
                   	Float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) loLimCell.getNumericCellValue();
                   	Float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) hiLimCell.getNumericCellValue();
                    String pin = pnameCell.getStringCellValue();
                    plist.add(new MultiParametricTestHeader(tname, tnum, dnum, pin, units, lolim, hilim));
                }
        	}
        	else // no pin, so either a normal Parametric test or Functional or Datalog test
        	{
        	    if (loLimCell.getCellType() != Cell_t.BLANK.type || hiLimCell.getCellType() != Cell_t.BLANK.type)
        	    {
                   	Float lolim = (loLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) loLimCell.getNumericCellValue();
                   	Float hilim = (hiLimCell.getCellType() == Cell_t.BLANK.type) ? null : (float) hiLimCell.getNumericCellValue();
                   	String units = unitsCell.getStringCellValue();
                   	plist.add(new ParametricTestHeader(tname, tnum, dnum, units, lolim, hilim));
        	    }
        	    else
        	    {
        	    	plist.add(new TestHeader(tname, tnum, dnum));
        	    }
        	}
        	row++;
        }
        return(plist);
    }
    */
    
}
