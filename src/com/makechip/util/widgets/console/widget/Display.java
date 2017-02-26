/**
 * 
 */
package com.makechip.util.widgets.console.widget;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import com.makechip.util.*;


public class Display extends JPanel implements Scrollable,
        MouseListener, ActionListener, AdjustmentListener, ComponentListener, AncestorListener
{
    private static final long serialVersionUID = -6014541877334450455L;
    private Font font;
    private DisplayMemory text;
    private boolean init;
    private int delta;
    private int margin;
    private int charsPerLine;
    private StringBuffer currentLine;
    private int lineIndex;
    private boolean insertMode;
    private int currentLineY;
    /**
     * rowHeight can only change if the font changes
     */
    private int rowHeight;
    
    /**
     * charWidth can only change if the font changes
     */
    private int charWidth;

    /**
     * The current position of the cursor
     */
    private Point pos;
    
    /**
     * viewSize can only change if the visibleRect changes, which means
     * only if the window changes which will be indicated by a ComponentEvent.
     */
    private Dimension viewSize;
        
    
    /**
     * The number of lines that will fit in the viewPort.  Should
     * only change when the window size changes, or the font size changes.
     */
    private int visibleLines;
    

    public Display(JScrollPane scroll, ConsolePane window, int memorySize)
    {
        super(new BorderLayout());
        scroll.setFocusable(false);
        scroll.getVerticalScrollBar().addAdjustmentListener(this);
        text = new DisplayMemory(memorySize);
        window.addComponentListener(this);
        addMouseListener(this);
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.INPUT_METHOD_EVENT_MASK);
        scroll.enableInputMethods(false);
        setBackground(Color.WHITE);
        font = Font.decode("courier-12");
        enableInputMethods(true);
        setOpaque(true);
        setFocusable(true);
        pos = new Point();
        margin = 4;
        insertMode = true;
        currentLine = new StringBuffer();
        init = false;
    }

    private void initialize(Graphics g)
    {
        if (init) return;
        if (viewSize == null) return;
        if (getSize().height == 0 || getVisibleRect().height == 0) return;
        delta = getPreferredSize().height - getVisibleRect().height;
        Rectangle r = new Rectangle(0, delta, viewSize.width, viewSize.height);
        scrollRectToVisible(r);
        setFont(font, g);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        rowHeight = fm.getHeight();
        charWidth = fm.charWidth('X');
        currentLineY = delta + rowHeight;
        charsPerLine = (viewSize.width - (2 * margin)) / charWidth; 
        pos = new Point(0, delta + fm.getHeight());
        windowSizeChanged();
        init = true;
        repaint();
    }     
    
    void setCurrentLine(String line)
    {
        int l = currentLine.length();
        currentLine.replace(0, l, line);
        scrollToEnd();
        repaint();
    }
    
    public void setInsertMode(boolean insert)
    {
        insertMode = insert;
        repaint();
    }
    
    public boolean getInsertMode()
    {
        return(insertMode);
    }
    
    public int getDelta()
    {
        if (!init) return(3);
        return(delta);
    }
    
    public int getRowHeight()
    {
        if (rowHeight == 0) return(17);
        return(rowHeight);
    }
    
    public void setPreferredSize(Dimension d)
    {
        if (!init)
        {
            super.setPreferredSize(d);
        }
        else
        {
            Dimension dd = null;
            if (d.height > getPreferredSize().height) dd = d;
            else dd = new Dimension(d.width, getPreferredSize().height);
            super.setPreferredSize(dd);
        }
        revalidate();
    }
            
    public void scrollToEnd()
    {        
        Rectangle r = getVisibleRect();
        r.y = getPreferredSize().height - r.height;
        scrollRectToVisible(r);
    }
    
    public void newline()
    {
        String s = currentLine.toString();
        while (s.length() > charsPerLine)
        {
            String ss = s.substring(0, charsPerLine);
            text.addLine(ss);
            s = s.substring(charsPerLine);
        }
        if (s.length() > 0) text.addLine(s);
        currentLine = new StringBuffer();
        increaseSize(1);
        lineIndex = 0;
        repaint();
    }
    
    public StringBuffer getCurrentLine()
    {
        return(currentLine);
    }
    
    public void insertString(String s)
    {
        scrollToEnd();
        //com.makechip.util.Log.msg("inserting string: " + s);
        String ss = s;
        while (ss.indexOf(Log.eol) >= 0)
        {
            String sss = ss.substring(0, ss.indexOf(Log.eol));
            int w1 = getNumSublines();
            currentLine.insert(lineIndex, sss);
            lineIndex += sss.length();
            int w2 = getNumSublines();
            if (w2 > w1) increaseSize(w2 - w1);
            newline();
            increaseSize(1);
            ss = ss.substring(ss.indexOf(Log.eol) + 1);
            repaint();
        }
        if (ss.length() > 0) 
        {
            int w1 = getNumSublines();
            currentLine.append(ss);
            int w2 = getNumSublines();
            if (w2 > w1) increaseSize(w2 - w1);
            lineIndex += ss.length();
        }
        else lineIndex = 0;
        repaint();
    }
       
    public void setFont(Font f)
    {
        setFont(f, getGraphics());
    }

    public Font getFont()
    {
        return(font);
    }
    
    private void setFont(Font f, Graphics g)
    {
        super.setFont(f);
        font = f;
        if (g == null) return;
        FontMetrics fm = g.getFontMetrics(f);
        charWidth = fm.getMaxAdvance();
        rowHeight = fm.getHeight();
        int charWidth = fm.charWidth('X');
        if (viewSize != null) charsPerLine = (viewSize.width - (2 * margin)) / charWidth;
        if (viewSize != null) windowSizeChanged();
    }
     
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        //if (e.getValueIsAdjusting()) return;
        repaint();
    }
    
    public void setMargin(int margin)
    {
        this.margin = margin;
    }
    
    public int getMargin()
    {
        return(margin);
    }
    
    public void insertChar(char c)
    {
        Graphics g = getGraphics();
        eraseCursor(g);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int wrap1 = 1 + currentLine.length() / charsPerLine;
        if ((lineIndex < currentLine.length()) && !insertMode) removeNextChar();
        currentLine.insert(lineIndex, c);
        lineIndex++;
        if (wrap1 <= currentLine.length() / charsPerLine)
        {
            increaseSize(1);
        }
        repaint();
        paintCursor(g);
    }

    private void increaseSize(int rows)
    {
        if (rowHeight == 0 || charsPerLine == 0) return;
        if ((getPreferredSize().height - delta)/rowHeight > (text.getNumLines() + (currentLine.length()/charsPerLine))) return;
        Dimension d = getPreferredSize();
        d.height += rowHeight;
        setPreferredSize(d);
        revalidate();
        scrollToEnd();
    }
    
    private int getNumSublines()
    {
        if (charsPerLine == 0) charsPerLine = Integer.MAX_VALUE;
        return(currentLine.length()/charsPerLine + 1);        
    }
    
    public void paintCursor()
    {
        paintCursor(getGraphics());
    }

    private void paintCursor(Graphics g)
    {
        if (currentLineY == -1 || charsPerLine == 0) return;
        setCursorPosition();
        g.setColor(Color.BLACK);
        if (insertMode)
        {
            g.drawLine(pos.x, pos.y, pos.x-2, pos.y+3);
            g.drawLine(pos.x, pos.y, pos.x+2, pos.y+3);
            g.setColor(getBackground());
            g.drawLine(pos.x, pos.y+1, pos.x-1, pos.y+3);
            g.drawLine(pos.x, pos.y+1, pos.x+1, pos.y+3);
            g.drawLine(pos.x-1, pos.y+3, pos.x+1, pos.y+3);
        }
        else
        {
            g.drawLine(pos.x, pos.y, pos.x-2, pos.y+3);
            g.drawLine(pos.x, pos.y, pos.x+2, pos.y+3);
            g.drawLine(pos.x, pos.y+1, pos.x-1, pos.y+3);
            g.drawLine(pos.x, pos.y+1, pos.x+1, pos.y+3);
            g.drawLine(pos.x-2, pos.y+3, pos.x+2, pos.y+3);
        }
    }
    
    public void eraseCursor()
    {
        eraseCursor(getGraphics());
    }
    
    private void eraseCursor(Graphics g)
    {
        if (currentLineY == -1 || charsPerLine == 0) return;
        setCursorPosition();
        g.setColor(getBackground());
        g.drawLine(pos.x, pos.y, pos.x-2, pos.y+3);
        g.drawLine(pos.x, pos.y, pos.x+2, pos.y+3);
        g.drawLine(pos.x, pos.y+1, pos.x-1, pos.y+3);
        g.drawLine(pos.x, pos.y+1, pos.x+1, pos.y+3);
        g.drawLine(pos.x-1, pos.y+3, pos.x+1, pos.y+3);        
    }
    
    private void setCursorPosition()
    {
        int wraps = lineIndex / charsPerLine;
        int ws = (wraps >= visibleLines) ? visibleLines-1 : wraps;
        pos.y = currentLineY + rowHeight * ws;
        pos.x = 1 + (lineIndex - wraps * charsPerLine) * charWidth;        
    }
    
    /**
     * Just paints the current visible rectangle
     * 
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        currentLineY = -1;
        initialize(g);
        if (rowHeight == 0) return;
        g.setFont(font);
        g.setColor(Color.BLACK);
        Rectangle r = getVisibleRect();
        int currentLines = getNumSublines();
        int numLinesBeyondWindow = (getPreferredSize().height - (r.y + r.height))/rowHeight;
        int totalLines = text.getNumLines() + currentLines;
        int phyline = totalLines - (numLinesBeyondWindow + visibleLines);
        if (phyline < 0) phyline = 0;
        int x = margin;
        //int y = delta + rowHeight + ((int) Math.ceil(((double) (r.y - delta))/((double) rowHeight))) * rowHeight;
        int y = r.y + rowHeight;
        if (numLinesBeyondWindow >= currentLines) // just display visibleLines starting from phyline
        {
            for (int i=phyline; i<phyline+visibleLines && text.getLine(i) != null; i++)
            {
                g.drawString(text.getLine(i), x, y);
                x = margin;
                y += rowHeight;
            }
        }
        else
        {
            if (phyline < 0) phyline = 0;
            int n = 0;
            while (phyline < text.getNumLines())
            {
                g.drawString(text.getLine(phyline), x, y);
                phyline++;
                n++;
                x = margin;
                y += rowHeight;
            }
            //String s = currentLine.toString();
            int l = (getNumSublines()-numLinesBeyondWindow) > visibleLines ? (getNumSublines() - visibleLines) * charsPerLine : 0;
            int m = currentLine.length();
            while (n < visibleLines)
            {
                if (currentLineY == -1) currentLineY = y;
                int end = l+charsPerLine < m ? l+charsPerLine : m;
                g.drawString(currentLine.substring(l, end), x, y);
                if (end == m) break;
                l += charsPerLine;
                x = margin;
                y += rowHeight;
            }
        }
        paintCursor(g);
    }
    
    public void advanceCursor(int spaces)
    {
        eraseCursor();
        lineIndex += spaces;
        if (lineIndex > currentLine.length() || lineIndex < 0) lineIndex = currentLine.length();
        paintCursor();
    }
    
    public void backupCursor(int spaces)
    {
        eraseCursor();
        lineIndex -= spaces;
        if (lineIndex < GuiConsole.PROMPT.length()) lineIndex = GuiConsole.PROMPT.length();
        paintCursor();
    }
    
    public void removeNextChar()
    {
        if (lineIndex == currentLine.length()) return;
        currentLine.deleteCharAt(lineIndex);
        repaint();
    }
    
    public void removePreviousChar()
    {
        if (lineIndex == GuiConsole.PROMPT.length()) return;
        backupCursor(1);
        removeNextChar();
    }
    
    public void keyReleased(KeyEvent arg0)
    {
    }

    public void mouseClicked(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void mousePressed(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void mouseReleased(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void mouseEntered(MouseEvent arg0)
    {
        requestFocus();     
    }

    public void mouseExited(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return(getPreferredSize());
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        if (orientation == SwingConstants.VERTICAL) return(rowHeight);
        return 0;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        if (orientation == SwingConstants.VERTICAL) return(getHeight());
        return 0;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void actionPerformed(ActionEvent arg0)
    {
        requestFocusInWindow();
    }

    private void windowSizeChanged()
    {
        if (viewSize == null) viewSize = new Dimension(0, 0);
        viewSize.width = getVisibleRect().width;
        viewSize.height = getVisibleRect().height;
        if (rowHeight > 0) visibleLines = (viewSize.height - 1) / rowHeight;
        if (charWidth == 0) charWidth = Integer.MAX_VALUE;
        charsPerLine = (viewSize.width - (2*margin)) / charWidth;
        scrollToEnd();
        repaint();
    }
    
    /**
     * Only a ComponentEvent can cause the viewSize to change.
     */
    public void componentResized(ComponentEvent arg0)
    {
        windowSizeChanged();
    }

    /**
     * Only a ComponentEvent can cause the viewSize to change.
     */
    public void componentMoved(ComponentEvent arg0)
    {
        windowSizeChanged();
    }

    /**
     * Only a ComponentEvent can cause the viewSize to change.
     */
    public void componentShown(ComponentEvent arg0)
    {
        windowSizeChanged();
    }

    /**
     * Only a ComponentEvent can cause the viewSize to change.
     */
    public void componentHidden(ComponentEvent arg0)
    {
    }

    public void ancestorAdded(AncestorEvent e)
    {   
        repaint();
    }

    public void ancestorMoved(AncestorEvent e)
    {
        repaint();
    }

    public void ancestorRemoved(AncestorEvent e)
    {
    }

}
