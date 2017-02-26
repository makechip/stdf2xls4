// ******************************************************************************
// ** **
// ** Command.java **
// ** Author: eric **
// ** Copyright 2004 Eric West **
// ** **
// ******************************************************************************
package com.makechip.util.widgets.console;

/**
 * ** All commands that can automatically be interpreted ** by the console
 * parser must implelement this interface. ** To add a command to the parser
 * just write a class that ** implements this interface. The application program
 * using ** the console parser must implement the Application interface. ** By
 * implicit contract each command class must have a constructor ** that takes
 * one argument which is an 'Application'. **
 * 
 * @version $Id: Command.java 1 2007-09-15 21:50:34Z eric $
 */
public interface Command
{
    /**
     * ** Executes this command. **
     * 
     * @param args all of the arguments to the command ** except the command
     *            name itself. **
     * @return the status of the command.
     */
    public CmdStatus_t exec(String args);

    /**
     * ** Get help on this command. **
     * 
     * @return A string containing help information on this command.
     */
    public String help();

    /**
     * ** Get the name of this command. Should always ** return
     * this.getClass().getSimpleName().toLowerCase(). **
     * 
     * @return the name of this command.
     */
    public String getName();
}
