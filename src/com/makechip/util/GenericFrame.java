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
package com.makechip.util;

import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * @author eric
 * @version $Id: GenericFrame.java 1 2007-09-15 21:50:34Z eric $
 *
 */
public class GenericFrame extends JFrame
{
    static final long   serialVersionUID = 12039485L;

    /**
     * 
     * @param panel
     */
    public GenericFrame(final JPanel panel)
    {
        super("Generic Frame");
        try
        {
            UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.out.println(e.toString());
            System.exit(1);
        }
        if (panel != null) getContentPane().add(panel);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    /**
     * 
     * @param panel
     */
    public GenericFrame(JComponent panel)
    {
        super("Generic Frame");
        try
        {
            UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.out.println(e.toString());
            System.exit(1);
        }
        getContentPane().add(panel);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args)
    {
        GenericFrame f = new GenericFrame(new JPanel());
        for (int i=0; i<100000000; i++)
        {
            int x = 0;
            x = x + 1;
            int y = x/5 + 4;
            int z = y*y / x*x;
            Log.msg("z = " + z);
        }
        f.pack();
        f.setVisible(true);
    }

    public String toString()
    {
        return ("GenericFrame");
    }
}
