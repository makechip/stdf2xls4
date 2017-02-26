// ******************************************************************************
// ** **
// ** CmdlineParser.java **
// ** Author: eric **
// ** Copyright 2004 Eric West **
// ** **
// ******************************************************************************
package com.makechip.util.widgets.console;

import java.util.*;

import com.makechip.util.*;

import java.lang.reflect.*;

/**
 * **
 * 
 * @param <T> The type of the application that this console is to be used for. **
 * @version $Id: CmdlineParser.java 1 2007-09-15 21:50:34Z eric $
 */
public class CmdlineParser<T extends Application>
{
    protected Hashtable<String, Command> commands;
    protected Hashtable<String, String>  alii;

    /**
     * ** Constructor **
     * 
     * @param cmdPkgName The name of the package that holds ** the command
     *            classes for the given application. **
     * @param application The application program that will ** use the commands
     *            from the console.
     */
    public CmdlineParser(String cmdPkgName, T application)
    {
        alii = new Hashtable<String, String>();
        commands = new Hashtable<String, Command>();
        initParser(cmdPkgName, application);
    }

    /**
     * ** Parse and execute a command from the console command line. **
     * 
     * @param cmdline A single line command **
     * @return CMD, ERROR, or NOMATCH ** Where CMD =&gt; A valid command was
     *         executed ** ERROR =&gt; A valaid command was passed, but there
     *         was a problem in execution ** NOMATCH =&gt; The command was not
     *         executed by the parser and should be passed on to the shell.
     */
    public CmdStatus_t execute(String cmdline)
    {
        if (cmdline == null) return(CmdStatus_t.CMD);
        if (cmdline.equals("")) return(CmdStatus_t.CMD);
        StringTokenizer st = new StringTokenizer(cmdline);
        String cmd = st.nextToken();
        String args = "";
        String args2 = "";
        if (alii.get(cmd) != null)
        {
            StringTokenizer st2 = new StringTokenizer(alii.get(cmd));
            String cmd1 = st2.nextToken();
            if (st2.hasMoreTokens())
            {
                args2 = alii.get(cmd).substring(cmd1.length() + 1);
            }
            cmd = cmd1;
        }
        if (st.hasMoreTokens())
        {
            if (args2.length() > 0) args = args2 + " " + cmdline.substring(cmd.length() + 1);
            else args = cmdline.substring(cmd.length() + 1);
            if (cmd.equals("alias"))
            {
                String newcmd = st.nextToken();
                if (!st.hasMoreTokens())
                {
                    Log.msg("Error: usage: alias <new_command> <replacement_string>");
                    return (CmdStatus_t.ERROR);
                }
                String replacement = st.nextToken();
                while (st.hasMoreTokens())
                {
                    replacement = replacement + " " + st.nextToken();
                }
                alii.put(newcmd, replacement);
                return (CmdStatus_t.CMD);
            }
            else if (cmd.equals("unalias"))
            {
                if (st.countTokens() != 1)
                {
                    Log.msg("Error: usage: unalias <command>");
                    return (CmdStatus_t.ERROR);
                }
                String a = st.nextToken();
                if (!alii.containsKey(a))
                {
                    Log.msg("Error: unknown alias: " + a);
                    return (CmdStatus_t.ERROR);
                }
                alii.remove(a);
                return (CmdStatus_t.CMD);
            }
        }
        else args = args2;
        Command command = commands.get(cmd);
        if (command == null) return(CmdStatus_t.NOMATCH);
        if (args == null) args = "";
        CmdStatus_t rslt = command.exec(args);
        return (rslt);
    }

    private void initParser(String cmdPkgName, T application)
    {
        Help<T> help = new Help<T>(application, commands);
        commands.put(help.getName(), help);
        ClassFinder<Command> ccf = new ClassFinder<Command>(cmdPkgName, Command.class, true);
        loadCommands(ccf, application);
    }

    private void loadCommands(Iterable<Class<? extends Command>> ccf, T application)
    {
        for (Class<?> c : ccf)
        {
            try
            {
                @SuppressWarnings("rawtypes")
				Constructor ctor = c.getConstructor(application.getClass());
                Command cmd = (Command) ctor.newInstance(application);
                commands.put(cmd.getName(), cmd);
            }
            catch (Exception e)
            {
                Log.fatal(e);
            }
        }
    }
}
