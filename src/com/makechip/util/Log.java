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
/**
 * Log.java Created on August 10, 2003, 11:07 AM
 */
package com.makechip.util;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * This class provides various logging methods for sending messages to a
 * file and/or stdout. It also provides a means to redirect stdout and/or
 * stdeer to some other output stream. The com.makechip.util package provides
 * a WindowStream class that when used in conjunction with this class allows
 * you to redirect your logging messages to a JTextComponent.
 * 
 * @author eric
 * @version $Id: Log.java 226 2008-09-01 17:39:41Z eric $
 */
public final class Log
{
    // debug level constants
    public static final int A = 1;
    public static final int B = 2;
    public static final int C = 4;
    public static final int D = 8;
    public static final int E = 2^4;
    public static final int F = 2^5;
    public static final int G = 2^6;
    public static final int H = 2^7;
    public static final int I = 2^8;
    public static final int J = 2^9;
    public static final int K = 2^10;
    public static final int L = 2^11;
    public static final int M = 2^12;
    public static final int N = 2^13;
    public static final int O = 2^14;
    public static final int P = 2^15;
    public static final int Q = 2^16;
    public static final int R = 2^17;
    public static final int S = 2^18;
    public static final int T = 2^19;
    public static final int U = 2^20;
    public static final int V = 2^21;
    public static final int W = 2^22;
    public static final int X = 2^23;
    public static final int Y = 2^24;
    public static final int Z = 2^25;

    /**
     * platform independent EOL 
     */
    public static final String eol    = System.getProperty("line.separator");
    /**
     * platform independent path separator ':' on unix
     */
    public static final String ps     = System.getProperty("path.separator");
    /**
     * platform independent file separator '/' on unix
     */
    public static final String fs       = System.getProperty("file.separator");
    private static FileWriter  fw       = null;
    private static boolean     _debug   = false;
    private static int _debugLevel      = 0;
    private static boolean _exitOnError = true;

    /**
     * Open the logfile, and set the debug flag
     * @param logFile
     * @param debug
     */
    public static void init(final String logFile, final boolean debug)
    {
        _debug = debug;
        if (logFile != null)
        {
            try { fw = new FileWriter(logFile, false); }
            catch (Exception e) { Log.fatal(e); }
        }
    }

    /**
     * Open the logfile, and set the debug level.  If debugLevel
     * is greater than zero, then the debug flag is also set.
     * @param logFile
     * @param debugLevel
     * @param exitOnError
     */
    public static void init(final String logFile, final int debugLevel, boolean exitOnError)
    {
        _debugLevel = debugLevel;
        _exitOnError = exitOnError;
        _debug = (_debugLevel > 0) ? true : false;
        init(logFile, _debug);
    }

    /**
     * Set the debug level.
     * @param debugLevel Any value from 0 to 2^26
     */
    public static void setDebugLevel(int debugLevel)
    {
        _debugLevel = debugLevel;
    }

    public static int getDebugLevel()
    {
        return(_debugLevel);
    }

    /**
     * This method is for use with a command line argument
     * that specifies the debug level.  The debug level
     * is set based on the characters in the supplied String.
     * @param argVal A string containing alpha letters only.
     * Each character represents a power of two with A = 1,
     * B = 2, C = 4, D = 8, etc. Letters may be upper or lower
     * case, but upper and lower case letters represent the same value.
     * Any other characters result in a fatal error.
     */
    public static void setDebugLevel(String argVal)
    {
        for (int i=0; i<argVal.length(); i++)
        {
            switch (argVal.toUpperCase().charAt(i))
            {
                case 'A': _debugLevel |= A; _debug = true; break;
                case 'B': _debugLevel |= B; _debug = true; break;
                case 'C': _debugLevel |= C; _debug = true; break;
                case 'D': _debugLevel |= D; _debug = true; break;
                case 'E': _debugLevel |= E; _debug = true; break;
                case 'F': _debugLevel |= F; _debug = true; break;
                case 'G': _debugLevel |= G; _debug = true; break;
                case 'H': _debugLevel |= H; _debug = true; break;
                case 'I': _debugLevel |= I; _debug = true; break;
                case 'J': _debugLevel |= J; _debug = true; break;
                case 'K': _debugLevel |= K; _debug = true; break;
                case 'L': _debugLevel |= L; _debug = true; break;
                case 'M': _debugLevel |= M; _debug = true; break;
                case 'N': _debugLevel |= N; _debug = true; break;
                case 'O': _debugLevel |= O; _debug = true; break;
                case 'P': _debugLevel |= P; _debug = true; break;
                case 'Q': _debugLevel |= Q; _debug = true; break;
                case 'R': _debugLevel |= R; _debug = true; break;
                case 'S': _debugLevel |= S; _debug = true; break;
                case 'T': _debugLevel |= T; _debug = true; break;
                case 'U': _debugLevel |= U; _debug = true; break;
                case 'V': _debugLevel |= V; _debug = true; break;
                case 'W': _debugLevel |= W; _debug = true; break;
                case 'X': _debugLevel |= X; _debug = true; break;
                case 'Y': _debugLevel |= Y; _debug = true; break;
                case 'Z': _debugLevel |= Z; _debug = true; break;
                default: fatal("Invalid debug Level");
            }
        }
    }

    /**
     * Utility method to capitalize a String
     * @param s a string tha you want capitalized
     * @return The string s with the first letter captilized
     */
    public static String capitalize(String s)
    {
        return (s.substring(0, 1).toUpperCase() + s.substring(1));
    }

    /**
     * Utility method to un-capitalize a String
     * @param s the string to be un-capitalized
     * @return the un-capitalized string
     */
    public static String deCapitalize(String s)
    {
        return (s.substring(0, 1).toLowerCase() + s.substring(1));
    }

    public static void rte(String msg)
    {
        throw new RuntimeException(msg);
    }

    /**
     * Get the state of the global debug flag
     * @return the debug flag
     */
    public static boolean debug()
    {
        return (_debug);
    }

    /**
     * Set the state of the debug flag
     * @param debug the desired state of the debug flag
     */
    public static void setDebug(boolean debug)
    {
        _debug = debug;
    }

    /**
     * Method to redirect the stdout stream
     * @param output the stream that stdout will be redirected to.
     */
    public static void redirectStdOut(OutputStream output)
    {
        System.setOut(new PrintStream(output));
    }

    /**
     * Method to redirect the stderr stream
     * @param error the stream that stderr will be redirected to.
     */
    public static void redirectStdErr(OutputStream error)
    {
        System.setErr(new PrintStream(error));
    }

    /**
     * Print a msg to stdout (and the log file if enabled)
     * An EOL is appended to the string
     * @param msg
     */
    public static void msg(final String msg)
    {
        System.out.println(msg);
        logfileOnly(msg + eol);
    }

    public static Graphics2D getDefaultGraphics()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return(ge.createGraphics(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
    }

    /**
     * Print a msg to stdout if the debug flag is set (and the log file if enabled)
     * An EOL is appended to the string
     * @param msg
     */
    public static void debug(final String msg)
    {
        if (_debug)
        {
            System.out.println(msg);
            logfileOnly(msg + eol);
        }
    }

    /**
     * Print a message to stdout if the debug flag is set.  No EOL
     * is appended to the string
     * @param msg
     */
    public static void debug_(final String msg)
    {
        if (_debug)
        {
            System.out.print(msg);
            logfileOnly(msg);
        }
    }

    public static void debug(int level, String msg)
    {  
        if (_debug)
        {
            if ((level & _debugLevel) == level)
            {
                System.out.println(msg);
                logfileOnly(msg + eol);
            }
        }
    }

    public static void debug_(int level, String msg)
    {
        if (_debug)
        {
            if ((level & _debugLevel) == level)
            {
                System.out.print(msg);
                logfileOnly(msg);
            }
        }
    }

    /**
     * Print a message to stdout. No EOL is appended to the string.
     * @param msg
     */
    public static void msg_(final String msg)
    {
        System.out.print(msg);
        logfileOnly(msg);
    }

    /**
     * Print a warning message to stdout.  The string is prepended
     * with "Warning: " and post-pended with EOL.
     * @param warn
     */
    public static void warning(final String warn)
    {
        String s = "Warning: " + warn;
        System.out.println(s);
        logfileOnly(s + eol);
    }

    /**
     * Print an error message to stdout.  The string is prepended with
     * "Error: " and post-pended with EOL
     * @param err
     */
    public static void error(final String err)
    {
        String s = "Error: " + err;
        System.out.println(s);
        logfileOnly(s + eol);
    }

    /**
     * Print a stack trace and throw an exception with the error message.
     * @param err
     */
    public static void fatal(final String err)
    {
        fatal(new RuntimeException(err));
    }

    /**
     * Print the exception information
     * @param e
     */
    public static void fatal(final Exception e)
    {
    	System.out.println("msg = " + e.getMessage());
        printException(e);
        if (_exitOnError) System.exit(1);
    }

    public static void close()
    {
        try { fw.close(); }
        catch (Exception e) { fatal(e); }
    }

    private static void logfileOnly(final String msg)
    {
        if (fw != null)
        {
            try
            {
                fw.write(msg);
                fw.flush();
            }
            catch (Exception e)
            {
                fatal(e);
            }
        }
    }

    private static void printException(Throwable e)
    {
        msg("Log.fatal(): --------------------------------------------------------------");
        if (e.getMessage() != null) 
        {
            msg(e.getMessage());
        }
        msg("Exception in thread \"" + Thread.currentThread().getName() + "\" " + e.getClass().getName());
        for (StackTraceElement s : e.getStackTrace()) 
        {
            msg("    at " + s.toString());
        }
        if (e.getCause() != null)
        {
            msg("");
            msg("Caused by:");
            printException(e.getCause());
        }
        msg("END Log.fatal() -----------------------------------------------------------");
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        init("dog.log", true);
        for (int i = 0; i < 10000; i++)
        {
            msg("this is an info message: " + i);
            warning("this is a warning message: " + i);
            error("this is an error message: " + i);
            debug("this is a debug message");
        }
        fatal("Bailing out on intentional error: success!");
    }
}
