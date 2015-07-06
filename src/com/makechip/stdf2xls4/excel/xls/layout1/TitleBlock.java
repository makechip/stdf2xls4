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
	private final int testWidth;
	private final DataHeader dh;
	
	public TitleBlock(PageHeader hdr, 
			          File logoFile, 
			          int testWidth, 
			          String pageTitle, 
			          boolean wafersort, 
			          CliOptions options,
			          List<TestHeader> hdrs)
	{
		this.testWidth = testWidth;
	    hb = new HeaderBlock(hdr);
	    ptb = new PageTitleBlock(pageTitle, testWidth, hb);
	    lb = new LegendBlock(hb);
	    logo = new LogoBlock(logoFile, hb);
	    cb = new CornerBlock(wafersort, options.onePage, hb);
	    dh = new DataHeader(hb, options.wrapTestNames, options.pinSuffix, options.precision, hdrs);
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
	public int getLoLimitRow()     { return(cb.getLoLimitRow());     }
	public int getHiLimitRow()     { return(cb.getHiLimitRow());     }
	public int getPinNameRow()     { return(cb.getPinNameRow());     }
	public int getUnitsRow()       { return(cb.getUnitsRow());       }

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
