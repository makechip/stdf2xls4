/**
 * 
 */
package com.makechip.util.widgets.console.widget;
import com.makechip.util.widgets.console.CmdStatus_t;
import java.awt.event.*;
import com.makechip.util.*;
import java.util.*;
import java.awt.*;
import java.io.*;

public class GuiConsole implements KeyListener, com.makechip.util.widgets.console.Console
{
    public static String PROMPT = "-> ";
    private java.util.List<ConsoleListener> listeners;
    private Display display;
    private FiniteStack<StringBuffer> history;
    private int historyPointer;
    private BufferedWriter bw;
    private ConsolePane pane;

    public GuiConsole(ConsolePane pane, Display display, int historyDepth)
    {
        this.pane = pane;
        this.display = display;
        display.addKeyListener(this);
        display.insertString(PROMPT);
        listeners = new ArrayList<ConsoleListener>();
        history = new FiniteStack<StringBuffer>(StringBuffer[].class, StringBuffer.class, historyDepth);
        historyPointer = 0;
        try
        {
            bw = new BufferedWriter(new FileWriter("console.log"));
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }
    
    public void addConsoleListener(ConsoleListener listener)
    {
        listeners.add(listener);
    }
    
    public void removeConsoleListener(ConsoleListener listener)
    {
        listeners.remove(listener);
    }
    
    public void keyTyped(KeyEvent e)
    {
    }

    public void setFont(Font f)
    {
        pane.setFont(f);
    }

    public Font getFont()
    {
        return(pane.getFont());
    }
    
    public void keyPressed(KeyEvent e)
    {
        display.scrollToEnd();
        if (e.getKeyChar() >= ((int) ' ') && e.getKeyChar() <= ((int) '~')) // print the char
        {
            display.insertChar(e.getKeyChar());
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            StringBuffer s = display.getCurrentLine();
            display.newline();
            line = null;
            display.insertString(PROMPT);
            if (!(s.toString().substring(PROMPT.length()).trim()).equals("")) 
            {
                history.push(s);
            }
            historyPointer = 0;
            for (ConsoleListener c : listeners)
            {
                c.consoleCommand(s.substring(PROMPT.length()));
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            e.consume();
            if (historyPointer == history.getNumItems()) return;
            display.setCurrentLine(history.peek(historyPointer).toString());
            historyPointer++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if (historyPointer == 0) return;
            historyPointer--;
            display.setCurrentLine(history.peek(historyPointer).toString());
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            display.backupCursor(1);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            display.advanceCursor(1);
        }
        else if (e.getKeyCode() == KeyEvent.VK_INSERT)
        {
            boolean b = display.getInsertMode();
            display.setInsertMode(!b);
            display.paintCursor();
        }
        else if (e.getKeyCode() == KeyEvent.VK_END)
        {
            display.advanceCursor(Integer.MAX_VALUE);
        }
        else if (e.getKeyCode() == KeyEvent.VK_HOME)
        {
            display.backupCursor(Integer.MAX_VALUE);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DELETE)
        {
            display.removeNextChar();
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            display.removePreviousChar();
        }
    }

    
    public void keyReleased(KeyEvent arg0)
    {
    }

    public CmdStatus_t exec(String cmd)
    {
        String s = PROMPT + cmd;
        history.push(new StringBuffer(s));
        display.setCurrentLine(s);
        display.newline();
        line = null;
        display.insertString(PROMPT);
        CmdStatus_t stat = null;
        try
        {
            bw.write(cmd);
            bw.write(Log.eol);
        }
        catch (Exception e) 
        {
            Log.fatal(e);
        }
        for (ConsoleListener c : listeners)
        {
            stat = c.consoleCommand(s.substring(PROMPT.length()));
        }
        return(stat);
    }
    
    public void flushTheLog()
    {
        try { bw.flush(); }
        catch (Exception e) { Log.fatal(e); }
    }

    public void closeTheLog()
    {
        try { bw.close(); }
        catch (Exception e) { Log.fatal(e); }
    }

    public void echo(String string)
    {
    	if (string == null) return;
        String s = null;
        if (line == null) s = PROMPT + string;
        else s = line + string;
        while (s.indexOf(Log.eol) >= 0)
        {
            String t = s.substring(0, s.indexOf(Log.eol));
            display.setCurrentLine(t);
            display.newline();
            line = null;
            display.insertString(PROMPT);
            s = s.substring(s.indexOf(Log.eol) + 1);
        }
        if (s != null)
        {
            if (s.length() > 0)
            {
                display.setCurrentLine(s);
                display.newline();
                line = null;
                display.insertString(PROMPT);
            }
        }
        try
        {
            bw.write(string);
            bw.write(Log.eol);
        }
        catch (Exception e)
        {
            Log.fatal(e);
        }
    }
    
    private static String line = null;
    
    public void echo_(String string)
    {
    	if (string == null) return;
        if (line == null) line = PROMPT + string; else line = line + string;
        display.setCurrentLine(line);
    }
    
   
    public void print(String s)
    {
        display.insertString(s);
    }


}
