/**
 * 
 */
package com.makechip.util.widgets.console.widget;

import javax.swing.*;

import java.awt.*;
import com.makechip.util.widgets.*;
import com.makechip.util.*;
/**
 * @author eric
 *
 */
public class ConsolePane extends DropShadowPanel
{
    private static final long serialVersionUID = -6606367424005141165L;
    private GuiConsole console;
    private Display display;
    private JScrollPane scroll;
    
    public ConsolePane()
    {
        super();
        setLayout(new BorderLayout());
        scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        display = new Display(scroll, this, 1000);
        scroll.setViewportView(display);
        setFocusable(false);
        add(scroll, BorderLayout.CENTER);
        setOpaque(true);
        setBackground(new Color(219, 209, 161));
        console = new GuiConsole(this, display, 1000);
    }

    public void setFont(Font f)
    {
        if (display == null) super.setFont(f);
        else display.setFont(f);
    }

    public Font getFont()
    {
        if (display == null) return(super.getFont());
        return(display.getFont());
    }
    
    public GuiConsole getConsole() { return(console); }
    
    public void setPreferredSize(Dimension d)
    {
        super.setPreferredSize(d);
        scroll.getViewport().setExtentSize(d);
        display.setPreferredSize(d);
    }
    
    public int getDelta()
    {
        return(display.getDelta());
    }
    
    public int getRowHeight()
    {
        return(display.getRowHeight());
    }
    
    public int getMargin()
    {
        return(display.getMargin());
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ConsolePane c = new ConsolePane();
        GenericFrame gf = new GenericFrame(c);
        gf.pack();
        gf.setVisible(true);

    }

}
