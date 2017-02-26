/**
 * 
 */
package com.makechip.util.widgets.console.widget;
import com.makechip.util.*;

/**
 * @author eric
 *
 */
public class DisplayMemory
{
    private FiniteStack<String> lines;
    
    public DisplayMemory(int capacity)
    {
        lines = new FiniteStack<String>(String[].class, String.class, capacity);
    }
    
    public int getCapacity()
    {
        return(lines.getSize());
    }
    
    public boolean isFull()
    {
        return(lines.isFull());
    }
    
    public int getNumLines()
    {
        return(lines.getNumItems());
    }
    
    public String getLine(int line)
    {
        //assert (line >= 0) && (line < lines.getNumItems()) : "Invalid line number";
        if (line < 0 || line >= lines.getNumItems()) return(null);
        int l = lines.getNumItems() - (line + 1);
        return(lines.peek(l).toString());
    }

    public void addLine(String line)
    {
        lines.push(line);
    }
}
