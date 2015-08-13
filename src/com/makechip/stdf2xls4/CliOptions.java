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
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import com.makechip.stdf2xls4.stdf.Modifier;
import com.makechip.util.Log;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import static java.util.Arrays.*;

public class CliOptions 
{
	private final OptionSet options;
	private static final String[] X_OPT = { "x", "xls-name", "Specify the spreadsheet filename" };
	private static final String[] M_OPT = { "m", "modifier", "Specify an STDF field modifier" };
	private static final String[] J_OPT = { "j", "jxl-xls-name", "Specify the spreadsheet filename for JXL format" };
	private static final String[] D_OPT = { "d", "dump", "Make ascii dump of STDF file(s) to file" };
	private static final String[] N_OPT = { "n", "no-wrap-test-names", "Don't wrap test names - gives really wide columns" };
	private static final String[] O_OPT = { "o", "no-overwrite", "Don't overwrite duplicate serial numbers or XY-coords" };
	private static final String[] S_OPT = { "s", "sort", "sort by serial number or XY coordinates" };
	private static final String[] B_OPT = { "b", "one-page", "Put all steps/wafers on one page - add column for step# or wafer#" };
	private static final String[] P_OPT = { "p", "precision", "Specify precision used in result and limit values - must be > 1 and < 13" };
	private static final String[] V_OPT = { "v", "dont-skip-search-fails", "Don't skip bogus verigy(AKA Advantest) search fails" };
	private static final String[] R_OPT = { "r", "rotate", "Rotate the spreadsheet so test names go vertically instead of horizontally" };
	private static final String[] Y_OPT = { "y", "dynamic-limits", "If a test has non-constant limits, show the limits on either side of each result" };
	private static final String[] G_OPT = { "g", "gui", "Enable graphical user interface" }; 
	private static final String[] H_OPT = { "h", "help", "show this help text" }; 
	private static final String[] A_OPT = { "a", "pin-suffix", "Assume test names for ParametricTestRecords have the pin name following the character delimiter" };
	private static final String[] L_OPT = { "l", "logo", "Specify a logo file for the spreadsheet" };
	private OptionSpec<String>  A;
	private OptionSpec<File>    J;
	private OptionSpec<Void>    F;
	private OptionSpec<File>    D;
	private OptionSpec<Void>    E;
	private OptionSpec<Void>    C;
	private OptionSpec<Void>    N;
	private OptionSpec<Void>    O;
	private OptionSpec<Void>    S;
	private OptionSpec<Void>    B;
	private OptionSpec<Void>    T;
	private OptionSpec<Void>    V;
	private OptionSpec<Void>    R;
	private OptionSpec<Void>    Y;
	private OptionSpec<Void>    G;
	private OptionSpec<String>  M;
	private OptionSpec<Integer> P;
	public final File xlsName;
	public final File logoFile;
	public final File dumpFile;
	public final List<Modifier> modifiers;
	public final boolean dump;
	public final boolean sort;
	public final boolean noWrapTestNames;
	public final boolean useJxl;
	public final boolean noOverwrite;
	public final boolean onePage;
	public final int precision;
	public final boolean dontSkipSearchFails;
	public final boolean rotate;
	public final boolean dynamicLimits;
	public final boolean gui;
	public final boolean pinSuffix;
	public final char delimiter;
	public final List<File> stdfFiles;
	private boolean success;
	private StringWriter sout;
	
	public static final Character[] suffixChars = {
			'~',
			'`',
			'@',
			'#',
			'$',
			'%',
			'^',
			'&',
			'*',
			'_',
			'-',
			'+',
			'=',
			':',
			';',
			'\'',
			',',
			'.',
			'?',
			'/',
			'|',
			'\\'
	};

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
	    sb.append(options.has(A) ? "-a " : ""); 	
	    sb.append(options.has(J) ? "-j " : "");
	    sb.append(options.has(F) ? "-f " : ""); 	
	    sb.append(options.has(D) ? "-d " : ""); 	
	    sb.append(options.has(E) ? "-e " : ""); 	
	    sb.append(options.has(C) ? "-c " : ""); 	
	    sb.append(options.has(N) ? "-n " : ""); 	
	    sb.append(options.has(O) ? "-s " : ""); 	
	    sb.append(options.has(B) ? "-b " : ""); 	
	    sb.append(options.has(T) ? "-t " : ""); 	
	    sb.append(options.has(V) ? "-v " : ""); 	
	    sb.append(options.has(R) ? "-r " : ""); 	
	    sb.append(options.has(Y) ? "-y " : ""); 	
	    sb.append(options.has(P) ? "-p " + options.valueOf(P) : "-p 3"); 	
		return(sb.toString());
	}
	
	public CliOptions(String[] args)
	{
	    OptionParser op = new OptionParser();	
	    sout = new StringWriter();
	    A = op.acceptsAll(asList(A_OPT[0], A_OPT[1]), A_OPT[2]).withOptionalArg().ofType(String.class);
	    //J = op.acceptsAll(asList(J_OPT[0], J_OPT[1]), J_OPT[2]);
	    D = op.acceptsAll(asList(D_OPT[0], D_OPT[1]), D_OPT[2]).withRequiredArg().ofType(File.class);
	    N = op.acceptsAll(asList(N_OPT[0], N_OPT[1]), N_OPT[2]);
	    O = op.acceptsAll(asList(O_OPT[0], O_OPT[1]), O_OPT[2]);
	    S = op.acceptsAll(asList(S_OPT[0], S_OPT[1]), S_OPT[2]);
	    B = op.acceptsAll(asList(B_OPT[0], B_OPT[1]), B_OPT[2]);
	    V = op.acceptsAll(asList(V_OPT[0], V_OPT[1]), V_OPT[2]);
	    R = op.acceptsAll(asList(R_OPT[0], R_OPT[1]), R_OPT[2]);
	    Y = op.acceptsAll(asList(Y_OPT[0], Y_OPT[1]), Y_OPT[2]);
	    G = op.acceptsAll(asList(G_OPT[0], G_OPT[1]), G_OPT[2]);
	    M = op.acceptsAll(asList(M_OPT[0], M_OPT[1]), M_OPT[2]).withRequiredArg().ofType(String.class);
	    P = op.acceptsAll(asList(P_OPT[0], P_OPT[1]), P_OPT[2]).withRequiredArg().ofType(int.class);
	    
	    OptionSpec<Void>    H = op.acceptsAll(asList(H_OPT[0], H_OPT[1]), H_OPT[2]).forHelp();

	    OptionSpec<File>    L = op.acceptsAll(asList(L_OPT[0], L_OPT[1]), L_OPT[2]).withRequiredArg().ofType(File.class);
	    OptionSpec<File>    X = op.acceptsAll(asList(X_OPT[0], X_OPT[1]), X_OPT[2]).
	    		requiredUnless(D_OPT[0], D_OPT[1], H_OPT[0], H_OPT[1], G_OPT[0], G_OPT[1]).withRequiredArg().ofType(File.class);
	    OptionSpec<File>    J = op.acceptsAll(asList(J_OPT[0], J_OPT[1]), J_OPT[2]).
	    		requiredUnless(D_OPT[0], D_OPT[1], H_OPT[0], H_OPT[1], X_OPT[0], X_OPT[1], G_OPT[0], G_OPT[1]).withRequiredArg().ofType(File.class);

	    OptionSpec<File> files = op.nonOptions().describedAs("list of STDF files").ofType(File.class);
	    
	    options = op.parse(args);
	    
	    pinSuffix = options.has(A);
	    String delim = (options.valueOf(A) == null) ? "@" : options.valueOf(A);
	    noWrapTestNames = options.has(N);
	    noOverwrite = options.has(O);
	    sort = options.has(S);
	    onePage = options.has(B);
	    dontSkipSearchFails = options.has(V);
	    rotate = options.has(R);
	    dynamicLimits = options.has(Y);
	    dump = options.has(D);
	    if (dump) dumpFile = options.valueOf(D); else dumpFile = null;
	    gui = options.has(G);
	    Log.msg("gui = " + gui);
	    xlsName = options.has(X) ? options.valueOf(X) : (options.has(J) ? options.valueOf(J) : null);
	    useJxl = options.has(J);
	    precision = options.has(P) ? options.valueOf(P) : 3;
	    stdfFiles = files.values(options);
	    if (stdfFiles == null || stdfFiles.size() == 0) throw new RuntimeException("Error: no STDF files specified");
	    String defaultFile = System.getenv("STDF2XLS_LOGO_FILE");
	    logoFile = options.has(L) ? options.valueOf(L) : (defaultFile != null ? new File(defaultFile) : null);
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
	    
	    if (!options.has(H) && !gui)
	    {
	    	Log.msg("gui = " + gui);
	    	if (stdfFiles == null || stdfFiles.size() == 0)
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
	    
	    List<Modifier> l = new ArrayList<Modifier>();
	    if (options.has(M))
	    {
	    	List<String> list = options.valuesOf(M);
	    	list.stream().forEach(s -> l.add(new Modifier(s.substring(1, s.length()-1))));
	    }
	    modifiers = Collections.unmodifiableList(l);
	    
	    if (delim.length() != 1) throw new RuntimeException("Error: pin suffix can only be on character: " + delim);
	    delimiter = delim.charAt(0);
	    boolean found = false;
	    for (char c : suffixChars)
	    {
	        if (c == delimiter)
	        {
	        	found = true;
	        	break;
	        }
	    }
	    if (!found)
	    {
	    	Log.msg("Invalid suffix character. Valid choices are:");
	    	for (char c : suffixChars) Log.msg_("" + c + " ");
	    	throw new RuntimeException("Invalid arguments");
	    }
	   
	}
	
	public String getMessage() { return(sout.toString()); }
	
	public boolean isOptionsValid() { return(success); }
	
	public static void main(String[] args)
	{
	    CliOptions options = new CliOptions(new String[] { "-x", "x.xlsx", "-m", "\"R:DTR F:TEXT_DAT C:EQUALS: V:XXX N:YYY\"", "x.stdf" });	
	    Log.msg("options = " + options);
	    Log.msg("mods = " + options.modifiers);
	}
	
}
