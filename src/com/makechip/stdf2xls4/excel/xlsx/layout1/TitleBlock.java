package com.makechip.stdf2xls4.excel.xlsx.layout1;

import java.io.File;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout1.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout1.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.LegendBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.LogoBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout1.PageTitleBlock;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

public class TitleBlock implements Block
{
	private final HeaderBlock hb;
	private final PageTitleBlock ptb;
	private final LegendBlock lb;
	private final LogoBlock logo;
	private final CornerBlock cb;
	private final DataHeader dh;
	private final int testWidth;
	
	public TitleBlock(PageHeader hdr, 
			          File logoFile, 
			          String pageTitle, 
			          boolean wafersort, 
			          CliOptions options,
			          List<TestHeader> hdrs)
	{
	    hb = new HeaderBlock(options, hdr);
	    testWidth = hdrs == null ? 0 : hdrs.size();
	    ptb = new PageTitleBlock(pageTitle, testWidth, hb);
	    lb = new LegendBlock(hb);
	    logo = new LogoBlock(logoFile, hb);
	    cb = new CornerBlock(wafersort, options.onePage, hb);
	    dh = new DataHeader(hb, options.noWrapTestNames, options.precision, hdrs);
	}
    
	@Override
    public void addBlock(XSSFWorkbook wb, XSSFSheet ws)
    {
		hb.addBlock(wb, ws);
		ptb.addBlock(wb, ws);
		lb.addBlock(wb, ws);
		logo.addBlock(wb, ws);
		cb.addBlock(wb, ws);
		dh.addBlock(wb, ws);
    }
	
	public int getFirstDataCol()   { return(hb.getWidth());          }
	public int getFirstDataRow()   { return(cb.getFirstDataRow());   }
	public int getWaferOrStepCol() { return(cb.getWaferOrStepCol()); }
	public int getSnOrYCol()       { return(cb.getSnOrYCol());       }
	public int getXCol()           { return(cb.getXCol());           }
	public int getYCol()           { return(cb.getYCol());           }
	public int getHwBinCol()       { return(cb.getHwBinCol());       }
	public int getSwBinCol()       { return(cb.getSwBinCol());       }
	public int getResultCol()      { return(cb.getResultCol());      }
	public int getTempCol()        { return(cb.getTempCol());        }
	public int getTestNameRow()    { return(cb.getTestNameRow());    }
	public int getTestNumberRow()  { return(cb.getTestNumberRow());  }
	public int getDupNumRow()      { return(cb.getDupNumRow());      }
	public int getLoLimitRow()     { return(cb.getLoLimitRow());     }
	public int getHiLimitRow()     { return(cb.getHiLimitRow());     }
	public int getPinNameRow()     { return(cb.getPinNameRow());     }
	public int getUnitsRow()       { return(cb.getUnitsRow());       }
	public int getOptionsRow()     { return(hb.getHeight() - 1);     }

	@Override
	public int getWidth()
	{
		return(hb.getWidth() + testWidth);
	}

	@Override
	public int getHeight()
	{
		return(hb.getHeight() + lb.getHeight() + 1);
	}

}
