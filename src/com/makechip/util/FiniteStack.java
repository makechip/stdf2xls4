/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
/**
 * 
 */
package com.makechip.util;
import java.lang.reflect.*;
/**
 * @author eric
 * This class maintains a stack of a fixed size.  This means
 * that when the stack is full, and another item is pushed
 * onto the stack, then the item at the bottom of the stack
 * (first in) dissapears completely.
 *
 */
public class FiniteStack<STACKTYPE>
{
    private STACKTYPE[] stack;
    private int top;
    private int bottom;
    private int items;
    private boolean empty;
    private boolean full;
    
    public FiniteStack(Class<STACKTYPE[]> arrayClass, Class<STACKTYPE> typeClass, int size)
    {
        stack = arrayClass.cast(Array.newInstance(typeClass, size));
        top = 0;
        bottom = 0;
        items = 0;
        empty = true;
        full = false;
    }
    
    public boolean isEmpty()
    {
        return(empty);
    }
    
    public boolean isFull()
    {
        return(full);
    }
    
    public int getSize()
    {
        return(stack.length);
    }
    
    public int getNumItems()
    {
        return(items);
    }
    
    public void push(STACKTYPE object)
    {
        //Log.msg("push: top = " + top);
        stack[top] = object;
        empty = false;
        incrementTop();
    }
    
    public STACKTYPE pop()
    {
        //Log.msg("pop");
        if (items == 0) return(null);
        STACKTYPE s = stack[top];
        full = false;
        decrementTop();
        return(s);
    }

    private void incrementTop()
    {
        if (items < stack.length) items++;
        if (items == stack.length) // stack is full; both top and bottom need to increment;
        {
            full = true;
            top++;
            bottom++;
            if (top == stack.length) top = 0;
            if (bottom == stack.length) bottom = 0;
        }
        else
        {
            top++;
        }
        //Log.msg("items = " + items + " top = " + top + " bottom = " + bottom);
    }
    
    private void decrementTop()
    {
        items--;
        if (items == 0)
        {
            top = 0;
            bottom = 0;
            empty = true;
        }
        else
        {
            top--;
            if (top < 0) top = stack.length - 1;
        }
    }
    
    public STACKTYPE peek(int topMinusN)
    {
        if (topMinusN > items - 1) return(null);
        int mostRecent = top == 0 ? stack.length - 1 : top - 1;
        int index = 0;
        if (topMinusN > mostRecent)
        {
            index = stack.length - (topMinusN - mostRecent);
        }
        else
        {
            index = mostRecent - topMinusN;
        }
        //Log.msg("topMinusN = " + topMinusN + " mostRecent = " + mostRecent + " top = " + top + " items = " + items);
        return(stack[index]);
    }
    
    
    public static void main(String[] args)
    {
        FiniteStack<String> f = new FiniteStack<String>(String[].class, String.class, 4);
        f.push("aa");
        f.push("bb");
        f.push("cc");
        f.push("dd");
        f.push("ee");
        f.push("ff");
        Log.msg("peak 0 = " + f.peek(0));
        Log.msg("peak 1 = " + f.peek(1));
    }
    
    
    
    
}
