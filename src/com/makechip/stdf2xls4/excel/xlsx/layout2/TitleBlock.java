package com.makechip.stdf2xls4.excel.xlsx.layout2;

import java.io.File;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.makechip.stdf2xls4.CliOptions;
import com.makechip.stdf2xls4.excel.xlsx.Block;
import com.makechip.stdf2xls4.excel.xlsx.layout2.DataHeader;
import com.makechip.stdf2xls4.excel.xlsx.layout2.HeaderBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.LegendBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.LogoBlock;
import com.makechip.stdf2xls4.excel.xlsx.layout2.PageTitleBlock;
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
			          boolean timeStampedFiles,
			          CliOptions options,
			          int numDevices,
			          List<TestHeader> hdrs)
	{
	    hb = new HeaderBlock(options, hdr);
	    testWidth = hdrs == null ? 0 : hdrs.size();
	    ptb = new PageTitleBlock(pageTitle, numDevices);
	    lb = new LegendBlock();
	    logo = new LogoBlock(logoFile);
	    cb = new CornerBlock(wafersort, options.onePage, timeStampedFiles, hb);
	    dh = new DataHeader(cb, options.precision, hdrs);
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
	
	public int getFirstDataRow()   { return(PageTitleBlock.HEIGHT + cb.getHeight()); }
	public int getFirstDataCol()   { return(cb.getFirstDataCol());   }
	public int getTimeStampRow()   { return(cb.getTimeStampRow());   }
	public int getWaferOrStepRow() { return(cb.getWaferOrStepRow()); }
	public int getSnOrYRow()       { return(cb.getSnOrYRow());       }
	public int getXRow()           { return(cb.getXRow());           }
	public int getYRow()           { return(cb.getYRow());           }
	public int getHwBinRow()       { return(cb.getHwBinRow());       }
	public int getSwBinRow()       { return(cb.getSwBinRow());       }
	public int getResultRow()      { return(cb.getResultRow());      }
	public int getTempRow()        { return(cb.getTempRow());        }
	public int getTestNameCol()    { return(cb.getTestNameCol());    }
	public int getTestNumberCol()  { return(cb.getTestNumberCol());  }
	public int getDupNumCol()      { return(cb.getDupNumCol());      }
	public int getLoLimitCol()     { return(cb.getLoLimitCol());     }
	public int getHiLimitCol()     { return(cb.getHiLimitCol());     }
	public int getPinNameCol()     { return(cb.getPinNameCol());     }
	public int getUnitsCol()       { return(cb.getUnitsCol());       }
	public int getOptionsRow()     { return(hb.getHeight() - 1);     }

	@Override
	public int getWidth()
	{
		return(hb.getWidth() + cb.getWidth() + testWidth);
	}

	@Override
	public int getHeight()
	{
		return(hb.getHeight() + lb.getHeight() + 1);
	}

}
