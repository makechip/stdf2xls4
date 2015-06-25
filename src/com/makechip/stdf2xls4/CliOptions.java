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

import joptsimple.OptionParser;

public class CliOptions 
{
	private boolean help;					// -h
	
	private String xlsName;
	private boolean dump = false;
	private boolean forceWaferMode = false;
	private boolean wrapTestNames = true;
	private boolean noOverwrite = false;
	private boolean showDuplicates = false;
	private boolean onePage = false;
	private boolean hiP = false;
	private boolean forceHdr = false;
	private boolean dontSkipSearchFails = true;
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
	private boolean noOverwriteSet = false;
	private boolean dontSkipSearchFailsSet = true;
	private boolean rotateSet = false;
	private boolean msModeSet = false;
	
	public CliOptions()
	{
	    OptionParser op = new OptionParser();	
	    op.accepts("f"); op.accepts("force-header");
	    op.accepts("d"); op.accepts("dump");
	    op.accepts("x").withRequiredArg().ofType(String.class);
	    op.accepts("xls-name").withRequiredArg().ofType(String.class);
	    op.accepts("c"); op.accepts("msmode");
	    op.accepts("d"); op.accepts("dump");
	    op.accepts("n"); op.accepts("no-wrap-testnames");
	    op.accepts("p").withRequiredArg().ofType(int.class);
	    op.accepts("precision").withRequiredArg().ofType(int.class);
	    op.accepts("s"); op.accepts("no-overwrite");
	    op.accepts("b"); op.accepts("one-page");
	    op.accepts("t"); op.accepts("show-duplicates");
	    op.accepts("V"); op.accepts("dont-skip-search-fails");
	    op.accepts("r"); op.accepts("rotate");
	    op.accepts("h"); op.accepts("help");
	}
	
}
