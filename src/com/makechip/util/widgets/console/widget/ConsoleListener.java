/**
 * 
 */
package com.makechip.util.widgets.console.widget;

import com.makechip.util.widgets.console.*;
/**
 * @author eric
 *
 */
public interface ConsoleListener
{
    
    public CmdStatus_t consoleCommand(String cmd);

}
