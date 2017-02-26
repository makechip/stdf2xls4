// ******************************************************************************
//
// Console.java
// Author: eric
// Copyright 2004 Eric West
//  
// ****************************************************************************
package com.makechip.util.widgets.console;

import com.makechip.util.*;
import java.io.*;

/**
 * This class implements a command line console that most of the features
 * supplied by GNU readline. Commands are added to the console parser by
 * implementing classes that implement the Command interface. The command
 * classes must all reside in one package and will be automatically detected on
 * startup.
 * 
 * @param <T> The type of the application that this console is to be used for.
 * @version $Id: CmdLineConsole.java 1 2007-09-15 21:50:34Z eric $
 */
public class CmdLineConsole<T extends Application> implements Console
{
    static
    {
        System.loadLibrary("shell");
    }
    private CmdlineParser<T>    parser;
    private static final String PROMPT = "cmd-> ";
    private BufferedWriter      logfile;

    private native void shellCmd(String cmd);

    private native String getLine(String prompt);

    /**
     * Constructor
     * 
     * @param cmdPkgName the name of the java package containing the command
     *            classes for this application.
     * @param application A reference to the application using this console.
     */
    public CmdLineConsole(String cmdPkgName, T application)
    {
        parser = new CmdlineParser<T>(cmdPkgName, application);
        try
        {
            logfile = new BufferedWriter(new FileWriter("console.log"), 1000000);
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }

    /**
     * Starts the command line parser. Does not return until the command 'quit',
     * 'exit', or 'q' is given.
     */
    public void run()
    {
        String line = "";
        while (true)
        {
            line = getLine(PROMPT);
            if (line.equals("quit") || line.equals("exit") || line.equals("q")) break;
            else if (line.equals("\\h")) line = "h";
            else if (line.equals("\\history")) line = "history";
            CmdStatus_t status = parser.execute(line);
            switch (status) {
            case NOMATCH:
                shellCmd(line);
                break;
            case ERROR:
            default:
            }
        }
        closeTheLog();
    }

    /**
     * Pass the entire command line to this method to have the parser execute
     * it.
     * 
     * @param line a command line entered by the user.
     */
    public CmdStatus_t exec(String line)
    {
        return(parser.execute(line));
    }

    /**
     * Flush the log file
     */
    public void flushTheLog()
    {
        try
        {
            logfile.flush();
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }

    public void closeTheLog()
    {
        try
        {
            logfile.close();
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }

    /**
     * Print something on the console screen.
     * 
     * @param msg what to print on the screen.
     */
    public void echo(String msg)
    {
        String s = msg.replace(Log.eol, Log.eol + PROMPT);
        System.out.println(PROMPT + s);
        try
        {
            logfile.write(s);
            logfile.write(Log.eol);
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }
}
