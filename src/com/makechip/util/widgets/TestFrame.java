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
*** ******************************************************************
***  Copyright Open-Silicon, Inc 2007
*** ******************************************************************
**/
package com.makechip.util.widgets;


import java.awt.*;

/**
*** @author ericw
*** @version $Id: TestFrame.java 1 2007-09-15 21:50:34Z eric $
**/
public class TestFrame extends Frame
{
    private static final long serialVersionUID = -23453L;
    public TestFrame()
    {
        super();
    }

    public void paint(Graphics g)
    {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.setColor(Color.BLACK);
        g.drawString("HELLO", 100, 150);
    }

    public static void main(String[] args)
    {
        Dimension d1 = new Dimension(400, 300);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        TestFrame f = new TestFrame();
        f.setUndecorated(true);
        f.setAlwaysOnTop(true);
        f.setPreferredSize(d1);
        f.setMinimumSize(d1);
        f.setMaximumSize(d1);
        f.setBounds(new Rectangle((d.width - d1.width)/2, (d.height - d1.height)/2, d1.width, d1.height));
        f.pack();
        f.setVisible(true);
    }

}
