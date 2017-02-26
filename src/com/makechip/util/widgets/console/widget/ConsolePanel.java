/**
 * 
 */
package com.makechip.util.widgets.console.widget;

import java.awt.Color;

import javax.swing.border.*;
import com.makechip.util.*;
import com.makechip.util.widgets.*;
import java.awt.*;
import java.awt.event.*;
/**
 * @author eric
 *
 */
public class ConsolePanel extends DropShadowPanel implements ComponentListener
{
    /**
     * 
     */
    private static final long serialVersionUID = -4981152494401050762L;
    private ConsolePane console;
    private CompoundBorder c;
    
    public ConsolePanel()
    {
        super();
        TitledBorder tb = new TitledBorder(new LineBorder(Color.BLACK, 2), "Console", TitledBorder.LEFT, TitledBorder.TOP);            
        c = new CompoundBorder(tb, new EmptyBorder(4, 4, 4, 4));
        setBorder(c);
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        addComponentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        console = new ConsolePane();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        gb.setConstraints(console, c);
        add(console);
        setOpaque(true);
    }
    
    public GuiConsole getConsole() { return(console.getConsole()); }
    
    public void setPreferredSize(Dimension d)
    {
        super.setPreferredSize(d);
        if (c != null && console != null)
        {
            int rh = console.getRowHeight();
            int t = c.getBorderInsets(this).top;
            int l = c.getBorderInsets(this).left;
            int b = c.getBorderInsets(this).bottom;
            int r = c.getBorderInsets(this).right;
            int h = d.height - (t + b);
            int rows = h / rh;
            int newh = console.getDelta() + 2*console.getMargin() + (rows+1) * rh;
            Dimension d1 = new Dimension(d.width-(l+r), newh);
            console.setPreferredSize(d1);
            console.setMinimumSize(d1);
            revalidate();
        }
    }
    
    public void setBackground(Color bg)
    {
        super.setBackground(bg);
        if (console != null) console.setBackground(bg);
    }
    
    public void setFont(Font f)
    {
        if (console == null) super.setFont(f);
        else console.setFont(f);
    }

    public Font getFont()
    {
        if (console == null) return(super.getFont());
        return(console.getFont());
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ConsolePanel p = new ConsolePanel();
        p.setPreferredSize(new Dimension(600, 300));
        p.setMinimumSize(new Dimension(600, 300));
        p.console.getConsole().echo("xxxx");
        GenericFrame gf = new GenericFrame(p);
        gf.pack();
        gf.setVisible(true);
    }

    public void componentResized(ComponentEvent arg0)
    {
        setPreferredSize(getSize());
    }

    public void componentMoved(ComponentEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void componentShown(ComponentEvent arg0)
    {
    }

    public void componentHidden(ComponentEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

}
