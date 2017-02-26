/**
 * 
 */
package com.makechip.util.widgets.console;

/**
 * @author eric
 *
 */
public interface Console
{
    
    public CmdStatus_t exec(String cmd);
    
    public void echo(String msg);
    
    public void flushTheLog();

    public void closeTheLog();

}
