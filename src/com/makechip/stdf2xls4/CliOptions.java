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

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.makechip.util.Log;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import static java.util.Arrays.*;

public class CliOptions 
{
	public final File xlsName;
	public final boolean dump;
	public final boolean wrapTestNames;
	public final boolean noOverwrite;
	public final boolean showDuplicates;
	public final boolean onePage;
	public final int precision;
	public final boolean forceHdr;
	public final boolean dontSkipSearchFails;
	public final boolean rotate;
	public final boolean msMode;
	public final boolean dynamicLimits;
	public final List<File> stdfFiles;
	
	public CliOptions(String[] args)
	{
	    OptionParser op = new OptionParser();	
	    OptionSpec<Void> F = op.acceptsAll(asList("f", "force-header"), "Force STS header even if not in datalog");
	    OptionSpec<Void> D = op.acceptsAll(asList("d", "dump"), "Make ascii dump of STDF file(s) to stdout"); // .requiredUnless("x", "xls-name");
	    OptionSpec<File> X = op.acceptsAll(asList("x", "xls-name"), "Specify the spreadsheet filename").requiredUnless("d", "dump").withRequiredArg().ofType(File.class);
	    OptionSpec<Void> C = op.acceptsAll(asList("c", "msmode"), "Allow up to 16000 columns for xlsx spreadsheets");
	    OptionSpec<Void> N = op.acceptsAll(asList("n", "no-wrap-testnames"), "Don't wrap test names - gives really wide columns");
	    OptionSpec<Integer> P = op.acceptsAll(asList("p", "precision"), "Specify precision used in result and limit values").withRequiredArg().ofType(int.class);
	    OptionSpec<Void> S = op.acceptsAll(asList("s", "no-overwrite"), "Don't overwrite duplicate serial numbers or XY-coords");
	    OptionSpec<Void> B = op.acceptsAll(asList("b", "one-page"), "Put all steps/wafers on one page - add column for step# or wafer#");
	    OptionSpec<Void> T = op.acceptsAll(asList("t", "show-duplicates"), "Don't suppress duplicates when using timestamped files");
	    OptionSpec<Void> V = op.acceptsAll(asList("v", "dont-skip-search-fails"), "Don't skip bogus verigy(AKA Advantest) search fails");
	    OptionSpec<Void> R = op.acceptsAll(asList("r", "rotate"), "Rotate the spreadsheet so test names go vertically instead of horizontally");
	    OptionSpec<Void> Y = op.acceptsAll(asList("y", "dynamic-limits"), "If a test has non-constant limits, show the limits on either side of each result");
	    OptionSpec<Void> H = op.acceptsAll(asList("h", "help"), "Show this help text").forHelp();
	    OptionSpec<File> files = op.nonOptions().ofType(File.class);
	    
	    OptionSet options = op.parse(args);
	    
	    forceHdr = options.has(F); 
	    wrapTestNames = !options.has(N);
	    noOverwrite = options.has(S);
	    showDuplicates = options.has(T);
	    onePage = options.has(B);
	    dontSkipSearchFails = options.has(V);
	    rotate = options.has(R);
	    msMode = options.has(C);
	    dynamicLimits = options.has(Y);
	    dump = options.has(D);
	    xlsName = options.has(X) ? options.valueOf(X) : null;
	    precision = options.has(P) ? options.valueOf(P) : 3;
	    stdfFiles = files.values(options);
	    
	    if (options.has(H))
	    {
	    	try { op.printHelpOn(System.out); }
	    	catch (Exception e) { Log.fatal(e); }
	    	System.exit(0);
	    }
	    
	    if (options.has(P))
	    {
	    	if (precision < 1 || precision > 12)
	    	{
	    		Log.msg("Error: precision must be greater that zero and less than, or equal to 12");
	    		System.exit(1);
	    	}
	    }
	    
	    if (stdfFiles == null | stdfFiles.size() == 0)
	    {
            Log.msg("Error: No STDF files have been specified");
            System.exit(1);
	    }
	    
	    List<File> missingFiles = stdfFiles.stream().filter(f -> !f.exists()).collect(Collectors.toList());
	    if (missingFiles.size() > 0)
	    {
	    	Log.msg("Error: The following STDF files are not found:");
	    	missingFiles.stream().forEach(f -> Log.msg(f.toString()));
	    	System.exit(1);
	    }
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		//CliOptions p = new CliOptions(new String[] { "-x", "x.xls", "a.stdf", "b.stdf", "c.stdf" });
		CliOptions p = new CliOptions(new String[] { "-h" });
	}
	
}
