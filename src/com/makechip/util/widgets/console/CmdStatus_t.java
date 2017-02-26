// ******************************************************************************
// ** **
// ** CmdStatus_t.java **
// ** Author: eric **
// ** Copyright 2004 Eric West **
// ** **
// ******************************************************************************
package com.makechip.util.widgets.console;

/**
 * ** Enumeration of return status codes from the commands **
 * 
 * @version $Id: CmdStatus_t.java 1 2007-09-15 21:50:34Z eric $
 */
public enum CmdStatus_t
{
    /** @enum CMD return if the command was successful */
    CMD,
    /** @enum ERROR return if the command was valid, but there was a problem */
    ERROR,
    /** @enum NOMATCH return if the command is not a valid command */
    NOMATCH;
}
