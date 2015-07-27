package com.makechip.stdf2xls4.excel.xls.layout2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.Block;
import com.makechip.stdf2xls4.excel.xls.layout2.DataHeader;
import com.makechip.stdf2xls4.excel.xls.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.LegendBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.LogoBlock;
import com.makechip.stdf2xls4.excel.xls.layout2.PageTitleBlock;
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
			          int numDevices,
			          List<TestHeader> hdrs)
	{
	    hb = new HeaderBlock(options, hdr);
	    testWidth = hdrs == null ? 0 : hdrs.size();
	    ptb = new PageTitleBlock(pageTitle, numDevices);
	    lb = new LegendBlock();
	    logo = new LogoBlock(logoFile);
	    cb = new CornerBlock(wafersort, options.onePage, hb);
	    dh = new DataHeader(cb, options.precision, hdrs);
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
	public int getFirstDataRow()   { return(cb.getFirstDataCol());   }
	public int getWaferOrStepCol() { return(cb.getWaferOrStepRow()); }
	public int getSnOrYCol()       { return(cb.getSnOrYRow());       }
	public int getXCol()           { return(cb.getXRow());           }
	public int getYCol()           { return(cb.getYRow());           }
	public int getHwBinCol()       { return(cb.getHwBinRow());       }
	public int getSwBinCol()       { return(cb.getSwBinRow());       }
	public int getResultCol()      { return(cb.getResultRow());      }
	public int getTempCol()        { return(cb.getTempRow());        }
	public int getTestNameRow()    { return(cb.getTestNameCol());    }
	public int getTestNumberRow()  { return(cb.getTestNumberCol());  }
	public int getDupNumRow()      { return(cb.getDupNumCol());      }
	public int getLoLimitRow()     { return(cb.getLoLimitCol());     }
	public int getHiLimitRow()     { return(cb.getHiLimitCol());     }
	public int getPinNameRow()     { return(cb.getPinNameCol());     }
	public int getUnitsRow()       { return(cb.getUnitsCol());       }
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
