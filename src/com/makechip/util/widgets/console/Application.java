// ******************************************************************************
// ** 
// ** Application.java 
// ** Author: eric 
// ** Copyright 2004 Eric West 
// ** 
// ******************************************************************************
package com.makechip.util.widgets.console;

/**
 * ** **
 * 
 * @version $Id: Application.java 1 2007-09-15 21:50:34Z eric $ ** ** Any
 *          application using the generic console must implement the **
 *          Application interface.
 */
public interface Application
{
    /**
     * ** This method is required so that commands may write to the screen. **
     * 
     * @return The console used by the application.
     */
    public Console getConsole();
}
