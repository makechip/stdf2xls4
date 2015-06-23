/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.makechip.stdf2xls4;

import be.ugent.twijug.jclops.annotations.Option;

public class CliOptions 
{
	private boolean help;					// -h
	
	private String xlsName;
	private boolean dump = false;
	private boolean dumpId = false;
	private boolean forceWaferMode = false;
	private boolean wrapTestNames = true;
	private boolean noOverwrite = false;
	private boolean showDuplicates = false;
	private boolean onePage = false;
	private boolean hiP = false;
	private boolean forceHdr = false;
	private boolean skipSearchFails = false;
	private boolean rotate = false;
	private boolean msMode = false;
	
	private boolean forceHdrSet = false;
	private boolean xlsNameSet = false;
	private boolean xlsxFormat = true;
	private boolean dumpSet = false;
	private boolean forceWaferModeSet = false;
	private boolean wrapTestNamesSet = false;
	private boolean hiPSet = false;
	private boolean showDuplicatesSet = false;
	private boolean onePageSet = false;
	private boolean dumpIdSet = false;
	private boolean noOverwriteSet = false;
	private boolean skipSearchFailsSet = false;
	private boolean rotateSet = false;
	private boolean msModeSet = false;
	
	@Option(shortName='f', longName="forceHdr", usageRank=2, description="Force STS header even if not in datalog")
	public void setForceHdr()
	{
		if (!forceHdrSet) forceHdr = true;
		forceHdrSet = true;
	}
	
	@Option(shortName='x', longName="xlsName", usageRank=3, description="Specify the spreadsheet filename")
	public void setXlsName(String xlsName)
	{
		if (!xlsNameSet) 
		{
			if (xlsName.endsWith(".xls"))
			{
				xlsxFormat = false;
			}
			else if (!(xlsName.endsWith(".xlsx")))
			{
				xlsName = xlsName + ".xlsx";
			}
			this.xlsName = xlsName;
		}
		xlsNameSet = true;
	}
	
	@Option(shortName='c', longName="msmode", usageRank=3, description="Use 16000 columns per page instead of 1000 (xlsx mode only)")
	public void setMsMode()
	{
		if (!msModeSet) this.msMode = true;
		msModeSet = true;
	}
	
	@Option(shortName='d', longName="dump", usageRank=4, description="Perform ascii dump of STDF file(s)")
	public void setDump() 
	{ 
		if (!dumpSet) this.dump = true;
		dumpSet = true;
	}
	
	@Option(shortName='w', longName="forceWaferMode", usageRank=6, description="Force wafer mode")
	public void setForceWaferMode()
	{
		if (!forceWaferModeSet) this.forceWaferMode = true;
		forceWaferModeSet = true;
	}
	
	@Option(shortName='n', longName="noWrapTestNames", usageRank=8, description="Don't wrap test names - give really wide columns")
	public void setWrapTestNames() 
	{ 
		if (!wrapTestNamesSet) this.wrapTestNames = false;
		wrapTestNamesSet = true;
	}
	
	@Option(shortName='p', longName="hiPrecision", usageRank=10, description="Use four digits of precision instead of 3")
	public void setHiP()
	{
		if (!hiPSet) hiP = true;
		hiPSet = true;
	}
	
	@Option(shortName='s', longName="noOverwrite", usageRank=12, description="Don't overwrite duplicate serial numbers or XY-coords")
	public void setNoOverwrite()
	{
		if (!noOverwriteSet) noOverwrite = true;
		noOverwriteSet = true;
	}
	
	@Option(shortName='b', longName="onePage", usageRank=16, description="Put all steps/wafers on one page; add column for step/wafer")
	public void setMultiModule()
	{
		if (!onePageSet) onePage = true;
		onePageSet = true;
	}
	
	@Option(shortName='i', longName="dumpIds", usageRank=18, description="Dump Test IDs only")
	public void setDumpId()
	{
		if (!dumpIdSet) dumpId = true;
		dumpIdSet = true;
	}
	
	@Option(shortName='t', longName="showDuplicates", usageRank=20, description="Don't suppress duplicates when using timestamped files")
	public void setShowDuplicates() 
	{ 
		if (!showDuplicatesSet) showDuplicates = true; 
		showDuplicatesSet = true;
	}
	
	@Option(shortName='v', longName="skipSearchFails", usageRank=22, description="Skip search fail results for Verigy parametric test results")
	public void setSkipSearchFails() 
	{ 
		if (!skipSearchFailsSet) skipSearchFails = false; 
		skipSearchFailsSet = true;
	}
	
	@Option(shortName='r', longName="rotate", usageRank=22, description="Transpose spreadsheet so test names go vertically instead of horizontally")
	public void setRotate()
	{
		if (!rotateSet) rotate = true;
		rotateSet = true;
	}
	
	@Option(shortName='h', longName="help", usageRank=100, description="Display command-line options.")
	public void setHelp()
	{
		help = true;
	}

	public boolean getHelp() 
	{ 
		return help; 
	}
	
	public CliOptions()
	{
	}
	
	public boolean getMsMode()
	{
		return msMode;
	}

	public boolean getDump()
	{
		return dump;
	}

	public boolean getDumpId()
	{
		return dumpId;
	}

	public boolean getForceWaferMode()
	{
		return forceWaferMode;
	}

	public boolean getWrapTestNames()
	{
		return wrapTestNames;
	}

	public boolean getNoOverwrite()
	{
		return noOverwrite;
	}

	public boolean getShowDuplicates()
	{
		return showDuplicates;
	}

	public boolean getOnePage()
	{
		return onePage;
	}

	public boolean getHiP()
	{
		return hiP;
	}

	public boolean getForceHdr()
	{
		return forceHdr;
	}

	public boolean getSkipSearchFails()
	{
		return skipSearchFails;
	}

	public String getXlsName()
	{
		return xlsName;
	}
	
	public boolean getXlsxFormat()
	{
		return xlsxFormat;
	}
	
	public boolean getRotate()
	{
		return rotate;
	}
	
}
