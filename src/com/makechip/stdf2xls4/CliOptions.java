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
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.makechip.util.Log;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import static java.util.Arrays.*;

public class CliOptions 
{
	private static final String[] X_OPT = { "x", "xls-name", "Specify the spreadsheet filename" };
	private static final String[] D_OPT = { "d", "dump", "Make ascii dump of STDF file(s) to stdout" };
	private static final String[] E_OPT = { "e", "dump-test-records", "Make ascii dump of test records" };
	private static final String[] N_OPT = { "n", "no-wrap-test-names", "Don't wrap test names - gives really wide columns" };
	private static final String[] S_OPT = { "s", "no-overwrite", "Don't overwrite duplicate serial numbers or XY-coords" };
	private static final String[] B_OPT = { "b", "one-page", "Put all steps/wafers on one page - add column for step# or wafer#" };
	private static final String[] P_OPT = { "p", "precision", "Specify precision used in result and limit values - must be > 1 and < 13" };
	private static final String[] F_OPT = { "f", "force-header", "Force basic header if header information not in datalog" }; 
	private static final String[] V_OPT = { "v", "dont-skip-search-fails", "Don't skip bogus verigy(AKA Advantest) search fails" };
	private static final String[] R_OPT = { "r", "rotate", "Rotate the spreadsheet so test names go vertically instead of horizontally" };
	private static final String[] C_OPT = { "c", "msmode", "Allow up to 16000 columns for xlsx spreadsheets" };
	private static final String[] Y_OPT = { "y", "dynamic-limits", "If a test has non-constant limits, show the limits on either side of each result" };
	private static final String[] G_OPT = { "g", "gui", "Enable graphical user interface" }; 
	private static final String[] T_OPT = { "t", "show-duplicates", "Don't suppress duplicates when using timestamped files" }; 
	private static final String[] H_OPT = { "h", "help", "show this help text" }; 
	private static final String[] M_OPT = { "m", "sort-by-device", "sort by serial-number or X-Y coordinate" }; 
	public final File xlsName;
	public final boolean dump;
	public final boolean dumpTests;
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
	public final boolean sort;
	public final boolean gui;
	public final List<File> stdfFiles;
	private boolean success;
	private StringWriter sout;
	
	public CliOptions(String[] args)
	{
	    OptionParser op = new OptionParser();	
	    sout = new StringWriter();
	    OptionSpec<Void>    F = op.acceptsAll(asList(F_OPT[0], F_OPT[1]), F_OPT[2]);
	    OptionSpec<Void>    D = op.acceptsAll(asList(D_OPT[0], D_OPT[1]), D_OPT[2]); // .requiredUnless("x", "xls-name");
	    OptionSpec<Void>    E = op.acceptsAll(asList(E_OPT[0], E_OPT[1]), E_OPT[2]); // .requiredUnless("x", "xls-name");
	    OptionSpec<Void>    C = op.acceptsAll(asList(C_OPT[0], C_OPT[1]), C_OPT[2]);
	    OptionSpec<Void>    N = op.acceptsAll(asList(N_OPT[0], N_OPT[1]), N_OPT[2]);
	    OptionSpec<Integer> P = op.acceptsAll(asList(P_OPT[0], P_OPT[1]), P_OPT[2]).withRequiredArg().ofType(int.class);
	    OptionSpec<Void>    S = op.acceptsAll(asList(S_OPT[0], S_OPT[1]), S_OPT[2]);
	    OptionSpec<Void>    B = op.acceptsAll(asList(B_OPT[0], B_OPT[1]), B_OPT[2]);
	    OptionSpec<Void>    T = op.acceptsAll(asList(T_OPT[0], T_OPT[1]), T_OPT[2]);
	    OptionSpec<Void>    V = op.acceptsAll(asList(V_OPT[0], V_OPT[1]), V_OPT[2]);
	    OptionSpec<Void>    R = op.acceptsAll(asList(R_OPT[0], R_OPT[1]), R_OPT[2]);
	    OptionSpec<Void>    Y = op.acceptsAll(asList(Y_OPT[0], Y_OPT[1]), Y_OPT[2]);
	    OptionSpec<Void>    G = op.acceptsAll(asList(G_OPT[0], G_OPT[1]), G_OPT[2]);
	    OptionSpec<Void>    M = op.acceptsAll(asList(M_OPT[0], M_OPT[1]), M_OPT[2]);
	    OptionSpec<Void>    H = op.acceptsAll(asList(H_OPT[0], H_OPT[1]), H_OPT[2]).forHelp();
	    OptionSpec<File>    X = op.acceptsAll(asList(X_OPT[0], X_OPT[1]), X_OPT[2]).
	    		requiredUnless(D_OPT[0], D_OPT[1], H_OPT[0], H_OPT[1]).withRequiredArg().ofType(File.class);
	    OptionSpec<File> files = op.nonOptions().describedAs("list of STDF files").ofType(File.class);
	    
	    OptionSet options = op.parse(args);
	    
	    forceHdr = options.has(F); 
	    sort = options.has(M);
	    wrapTestNames = !options.has(N);
	    noOverwrite = options.has(S);
	    showDuplicates = options.has(T);
	    onePage = options.has(B);
	    dontSkipSearchFails = options.has(V);
	    rotate = options.has(R);
	    msMode = options.has(C);
	    dynamicLimits = options.has(Y);
	    dump = options.has(D);
	    dumpTests = options.has(E);
	    gui = options.has(G);
	    xlsName = options.has(X) ? options.valueOf(X) : null;
	    precision = options.has(P) ? options.valueOf(P) : 3;
	    stdfFiles = files.values(options);
	    success = true;
	    
	    if (options.has(H))
	    {
	    	try { op.printHelpOn(sout); }
	    	catch (Exception e) { Log.fatal(e); }
	    }
	    
	    if (options.has(P))
	    {
	    	if (precision < 1 || precision > 12)
	    	{
	    		sout.write("Error: precision must be greater that zero and less than, or equal to 12");
	    		success = false;
	    	}
	    }
	    
	    if (!options.has(H))
	    {
	    	if (stdfFiles == null | stdfFiles.size() == 0)
	    	{
	    		sout.write("Error: No STDF files have been specified");
	    		success = false;
	    	}
	    }
	    
	    if (!options.has(H))
	    {
	    	List<File> missingFiles = stdfFiles.stream().filter(f -> !f.exists()).collect(Collectors.toList());
	    	if (missingFiles.size() > 0)
	    	{
	    		sout.write("Error: The following STDF files are not found: ");
	    		missingFiles.stream().forEach(f -> sout.write(f.toString()));
	    		success = false;
	    	}
	    }
	}
	
	public String getMessage() { return(sout.toString()); }
	
	public boolean isOptionsValid() { return(success); }
	
	public static void main(String[] args)
	{
		
	}
	
}
