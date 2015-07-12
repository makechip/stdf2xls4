package com.makechip.stdf2xls4.excel.xls.layout1;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.stdfapi.PageHeader;
import com.makechip.stdf2xls4.stdfapi.TestHeader;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
	    ptb = new PageTitleBlock(pageTitle, hdrs.size(), hb);
	    lb = new LegendBlock(hb);
	    logo = new LogoBlock(logoFile, hb);
	    cb = new CornerBlock(wafersort, options.onePage, hb);
	    dh = new DataHeader(hb, options.noWrapTestNames, options.precision, hdrs);
	    testWidth = hdrs.size();
	}
    
	@Override
    public void addBlock(WritableSheet ws) throws RowsExceededException, WriteException, IOException
    {
		hb.addBlock(ws);
		ptb.addBlock(ws);
		lb.addBlock(ws);
		logo.addBlock(ws);
		cb.addBlock(ws);
		dh.addBlock(ws);
    }
	
	public int getFirstDataCol()   { return(hb.getWidth());          }
	public int getFirstDataRow()   { return(cb.getFirstDataRow());   }
	public int getWaferOrStepCol() { return(cb.getWaferOrStepCol()); }
	public int getSnOrYCol()       { return(cb.getSnOrYCol());       }
	public int getXCol()           { return(cb.getXCol());           }
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
