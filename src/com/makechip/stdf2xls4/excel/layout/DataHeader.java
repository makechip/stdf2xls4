package com.makechip.stdf2xls4.excel.layout;

import static com.makechip.stdf2xls4.excel.Format_t.HEADER1_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.HEADER5_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT;
import static com.makechip.stdf2xls4.excel.Format_t.TEST_NAME_FMT_WRAP;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.makechip.stdf2xls4.excel.Coord;
import com.makechip.stdf2xls4.excel.Spreadsheet;
import com.makechip.stdf2xls4.excel.layout.CornerBlock;
import com.makechip.stdf2xls4.stdfapi.MultiParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.ParametricTestHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;

public class DataHeader
{
	private final int precision;
	private final boolean rot;
	private final boolean noWrapTestNames;
	private final List<TestHeader> hdrs;
	private int maxLength;
	private int maxPinLength;
	private final CornerBlock cb;
	private Map<String, TLongObjectHashMap<HashMap<String, TIntIntHashMap>>> idlocs;
	
	public DataHeader(CornerBlock cb, int precision, boolean rot, boolean noWrapTestNames, List<TestHeader> hdrs)
	{
		this.cb = cb;
		maxLength = 0;
		maxPinLength = 0;
		this.precision = precision;
		this.hdrs = hdrs;
		this.rot = rot;
		this.noWrapTestNames = noWrapTestNames;
		idlocs = new HashMap<>();
	}
	
	private void setMap(String tname, long tnum, String pin, int dupNum, Coord xy)
	{
	    TLongObjectHashMap<HashMap<String, TIntIntHashMap>> m1 = idlocs.get(tname);
	    if (m1 == null)
	    {
	        m1 = new TLongObjectHashMap<>();
	        idlocs.put(tname, m1);
	    }
	    HashMap<String, TIntIntHashMap> m2 = m1.get(tnum);
	    if (m2 == null)
	    {
	        m2 = new HashMap<String, TIntIntHashMap>();
	        m1.put(tnum, m2);
	    }
	    TIntIntHashMap m3 = m2.get(pin);
	    if (m3 == null)
	    {
	        m3 = new TIntIntHashMap(500, 0.7f, -1, -1);
	        m2.put(pin, m3);
	    }
	    if (rot) m3.put(dupNum, xy.r);
	    else m3.put(dupNum, xy.c);
	}
	
	public int getRC(String testName, long tnum, String pin, int dupNum)
	{
	    TLongObjectHashMap<HashMap<String, TIntIntHashMap>> m1 = idlocs.get(testName);
	    if (m1 == null) return(-1);
	    HashMap<String, TIntIntHashMap> m2 = m1.get(tnum);
	    if (m2 == null) return(-1);
	    TIntIntHashMap m3 = m2.get(pin);
	    if (m3 == null) return(-1);
	    int rc = m3.get(dupNum);
	    return(rc);
	}
	
	public void addBlock(Spreadsheet ss, int page, boolean merge)
	{
		cb.tstxy.reset();
		int lineCnt = 5;
		if (!rot && !noWrapTestNames)
		{
		    String maxTestName = "";
		    int maxLen = 0;
		    for (TestHeader h : hdrs)
		    {
		        if (h.testName.length() > maxLen)
		        {
		            maxLen = h.testName.length();
		            maxTestName = h.testName;
		        }
		    }
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    GraphicsDevice gd = ge.getDefaultScreenDevice();
		    GraphicsConfiguration gc = gd.getDefaultConfiguration();
		    BufferedImage bi = gc.createCompatibleImage(500, 50);
		    Graphics2D graphics = bi.createGraphics();
		    java.awt.Font currFont = new java.awt.Font("ARIAL", 0, 10);
		    graphics.setFont(currFont);
		    FontMetrics fm = graphics.getFontMetrics();
		    int zeroWidth = fm.charWidth('0');
		    int defColWidth = 10 * zeroWidth;
		    AttributedString attrStr = new AttributedString(maxTestName);
		    attrStr.addAttribute(TextAttribute.FONT, currFont);
		    FontRenderContext frc = graphics.getFontRenderContext();
		    LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
		    int nextPos = 0;
		    lineCnt = 0;
		    while (measurer.getPosition() < maxTestName.length())
		    {
		        nextPos = measurer.nextOffset(defColWidth); // mergedCellWidth is the max width of each line
		        lineCnt++;
		        measurer.setPosition(nextPos);
		    }
		    if (lineCnt < 5) lineCnt = 5;
			int defRowHeight = 255; //ss.getRowHeight(page, cb.tstxy.tnameLabel.r);
			int newHeight = ((1+lineCnt) * defRowHeight) / 5;
			for (int i=cb.tstxy.tnameLabel.r; i<cb.tstxy.tnumLabel.r; i++)
			{
			    ss.setRowHeight(page, i, newHeight);
			}
		}
		hdrs.stream().forEach(hdr ->
		{
			if (rot || noWrapTestNames) 
			{
				if (hdr.testName.length() > maxLength)
				{
					maxLength = hdr.testName.length();
				    ss.setColumnWidth(page, cb.tstxy.tname.c, maxLength);
				}
			}
			else 
			{
				for (int r=cb.tstxy.tnameLabel.r; r<cb.tstxy.tnumLabel.r; r++)
				{
				    ss.setCell(page, new Coord(cb.tstxy.tname.c, r), TEST_NAME_FMT_WRAP, "");
				}
				ss.setColumnWidth(page, cb.tstxy.tname.c, 10); // added
			}
			ss.setCell(page, cb.tstxy.tname, noWrapTestNames ? TEST_NAME_FMT : TEST_NAME_FMT_WRAP, hdr.testName);
			ss.setCell(page, cb.tstxy.tnum, HEADER1_FMT, hdr.testNumber);	
			ss.setCell(page, cb.tstxy.dupNum, HEADER1_FMT, hdr.dupNum);
			setMap(hdr.testName, hdr.testNumber, hdr.getPin(), hdr.dupNum, cb.tstxy.tname);
			if (hdr instanceof MultiParametricTestHeader)
			{
				MultiParametricTestHeader mhdr = (MultiParametricTestHeader) hdr;	
				if (mhdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.loLim, HEADER5_FMT, precision, mhdr.loLimit);
				if (mhdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.hiLim, HEADER5_FMT, precision, mhdr.hiLimit);
                if (mhdr.pin != null && mhdr.pin.length() > 1)
                {
                    int width = getPinColWidth(mhdr.pin);	
                    if (width > 10)
                    {
                        ss.setColumnWidth(page, cb.tstxy.pin.c, width);
                    }
                }
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, mhdr.pin);
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, mhdr.units);
			}
			else if (hdr instanceof ParametricTestHeader)
			{
				ParametricTestHeader phdr = (ParametricTestHeader) hdr;	
				if (phdr.loLimit == null) ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.loLim, HEADER5_FMT, precision, phdr.loLimit);
				if (phdr.hiLimit == null) ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				else ss.setCell(page, cb.tstxy.hiLim, HEADER5_FMT, precision, phdr.hiLimit);
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, phdr.units);
			}
			else
			{
				ss.setCell(page, cb.tstxy.loLim, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.hiLim, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.pin, HEADER1_FMT, "");
				ss.setCell(page, cb.tstxy.units, HEADER1_FMT, "");
			}
			if (!rot)
			{
				ss.setCell(page, new Coord(cb.tstxy.units.c, cb.tstxy.units.r+1), HEADER1_FMT, "");
			    if (merge) ss.mergeCells(page, cb.tstxy.units.r, cb.tstxy.units.r+1, cb.tstxy.units.c, cb.tstxy.units.c);
			}
			cb.tstxy.inc();
		});
	}
	
	private int getPinColWidth(String pin)
	{
	    double l = 0.0;
	    for (int i=0; i<pin.length(); i++) 
	    {
	    	if (Character.isUpperCase(pin.charAt(i))) l += 1.4; else l++;
	    }
	    int len = (int) l;
	    if (rot)
	    {
	    	if (len > maxPinLength) maxPinLength = len;
	    	return(maxPinLength);
	    }
	    return(len);
	}
	
	public int getWidth()
	{
		return(8);
	}

	public int getHeight()
	{
		return(hdrs.size());
	}

}
