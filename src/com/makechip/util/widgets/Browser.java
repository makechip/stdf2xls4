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
package com.makechip.util.widgets;

import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

/**
   A simple Web Browser with minimal functionality.
   @author Jose M. Vidal
*/
public class Browser 
{
    private JFrame f;
    /** Set the page.
     *  @param jep the pane on which to display the url
     *  @param url the url to display 
     */
    protected static void setPage(JEditorPane jep, URL url)
    {
        try { jep.setPage(url); }
        catch (IOException e) 
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    /** An inner class which listens for keypresses on the Back button. */
    class backButtonListener implements ActionListener 
    {
        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector<URL> history;
        public backButtonListener(JEditorPane jep, JButton backButton, Vector<URL> history, JLabel label)
        {
            this.jep = jep;
            this.backButton = backButton;
            this.history = history;
            this.label = label;
        }

        /** The action is to show the last url in the history.
         @param e the event*/
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                //the current page is the last, remove it
                URL curl = history.lastElement();
                history.removeElement(curl);
                curl = history.lastElement();
                System.out.println("Back to " + curl);
                setPage(jep,curl);
                label.setText("<html><b>URL:</b> "+ curl);
                if (history.size() == 1) backButton.setEnabled(false);
            }
            catch (Exception ex) { System.out.println("Exception " + ex); }
        }
    }

    /** An inner class that listens for hyperlinkEvent.*/
    class LinkFollower implements HyperlinkListener 
    {
        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector<URL> history;
        public LinkFollower(JEditorPane jep, JButton backButton, Vector<URL> history, JLabel label)
        {
            this.jep = jep;
            this.backButton = backButton; 
            this.history = history;
            this.label = label;
        }
        /** The action is to show the page of the URL the user clicked on.
         *  @param evt the event. We only care when its type is ACTIVATED. 
         */
        public void hyperlinkUpdate(HyperlinkEvent evt)
        {
            if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            {
                try 
                {
                    URL currentURL = evt.getURL();
                    history.add(currentURL);
                    backButton.setEnabled(true);
                    System.out.println("Going to " + currentURL);
                    setPage(jep, currentURL);
                    label.setText("<html><b>URL:</b> "+ currentURL);
                }
                catch (Exception e) { System.out.println("ERROR: Trouble fetching url"); }
            }
        }

    }

    /** The contructor runs the browser. It displays the main frame with the
     *  fetched initialPage
     *  @param initialPage the first page to show 
     */
    public Browser(String title, URL initialPage)
    {
        /** A vector of String containing the past urls */
        Vector<URL> history = new Vector<URL>();
        history.add(initialPage);
        
        // set up the editor pane
        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        setPage(jep, initialPage);

        // set up the window
        JScrollPane scrollPane = new JScrollPane(jep);   
        f = new JFrame(title);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Exit the program when user closes window.
        f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e)
                {
                    setVisible(false);
                }
            });

        //Label where we show the url
        JLabel label = new JLabel("<html><b>URL:</b> "+ initialPage);
        
        JButton backButton = new JButton ("Back");
        backButton.setActionCommand("back");
        backButton.setToolTipText("Go to previous page");
        backButton.setEnabled(false);
        backButton.addActionListener(new backButtonListener(jep, backButton, history, label));

        JButton exitButton = new JButton ("Exit");
        exitButton.setActionCommand("exit");
        exitButton.setToolTipText("Quit this application");
        exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    System.exit(0);
                }
            });

        //A toolbar to hold all our buttons
        JToolBar toolBar = new JToolBar();
        toolBar.add(backButton);
        toolBar.add(exitButton);

        jep.addHyperlinkListener(new LinkFollower(jep, backButton, history, label));

        //Set up the toolbar and scrollbar in the contentpane of the frame
        JPanel contentPane = JPanel.class.cast(f.getContentPane());
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(400, 100));
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(label, BorderLayout.SOUTH);

        f.pack();
        f.setSize(640, 360);
        f.setVisible(false);
    }

    public void setVisible(boolean visible)
    {
        f.setVisible(visible);
    }

    /** Create a Browser object. Use the command-line url if given 
     *
     */
    public static void main(String[] args) 
    {
        try
        {
            URL initialPage = new URL("file:///home/eric/workspaces/makechip/branches/jtagbr/com/makechip/util/widgets/test/index.html");
            if (args.length > 0) initialPage = new URL(args[0]);
            new Browser("Test", initialPage);
        }
        catch (Exception e) { com.makechip.util.Log.fatal(e); }
    }
    
}
    
