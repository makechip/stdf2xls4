// ******************************************************************************
// ** **
// Help.java
// Author: eric
// Copyright 2004 Eric West
//
// ****************************************************************************
package com.makechip.util.widgets.console;

import java.util.*;
import com.makechip.util.*;

/**
 * The built in help command. This class lists all of the commands available to
 * the application specific console.
 * 
 * @param <T> The type of the application that this console is to be used for.
 * @version $Id: Help.java 1 2007-09-15 21:50:34Z eric $
 */
public class Help<T extends Application> implements Command
{
    private Hashtable<String, Command> commands;
    private Application                application;

    /**
     * Constructor
     * 
     * @param application A reference to the application using the consile.
     * @param commands A lookup table of all the commands, with the command name
     *            being the key.
     */
    public Help(Application application, Hashtable<String, Command> commands)
    {
        this.application = application;
        this.commands = commands;
    }

    /**
     * Get the name of this command.
     */
    public String getName()
    {
        return ("help");
    }

    /**
     * Get help on the help command.
     * 
     * @return a string containing the help information.
     */
    public String help()
    {
        String s = Log.eol + "Without an argument help returns a list of commands" + Log.eol;
        s = s + "Optionally a name of a command may be given as an argument" + Log.eol;
        s = s + "to get help on a specific command" + Log.eol;
        return (s);
    }

    /**
     * Execute this help command.
     * 
     * @param args help can take a command name as an argument.
     * @return the completion status of this command.
     */
    public CmdStatus_t exec(String args)
    {
        Console console = application.getConsole();
        if (args.equals(""))
        {
            console.echo("Commands:");
            console.echo("");
            Enumeration<String> keys = commands.keys();
            while (keys.hasMoreElements()) console.echo(keys.nextElement());
        }
        else
        {
            Command cmd = commands.get(args);
            if (cmd == null)
            {
                console.echo("Error: invalid command: " + args);
                return (CmdStatus_t.ERROR);
            }
            console.echo(Log.eol + cmd.help() + Log.eol);
        }
        return (CmdStatus_t.CMD);
    }
}
