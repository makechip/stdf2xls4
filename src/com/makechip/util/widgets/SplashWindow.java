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
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
***
**/
public class SplashWindow extends Frame 
{   
    private static final long serialVersionUID = -8301245315778166288L;
    private Image image;
    private boolean run;
    private MediaTracker tracker;
    private TestComp tc;

    public static void main(String args[])
    {
        SplashWindow s = new SplashWindow(com.makechip.util.widgets.SplashWindow.class.getResource("splash1.gif"), "E-Fuse ROM Compiler", "Version 1.1.1.1", "Copyright 2006 Eric West");
        s.splash();
    }

    public SplashWindow(String programName, String version, String copyright)
    {
        this(com.makechip.util.widgets.SplashWindow.class.getResource("splash1.gif"), programName, version, copyright);
    }

    public SplashWindow(URL imgSource, String programName, String version, String copyright)
    {
        super();
        run = true;
        loadImage(imgSource);
        setUndecorated(true);
        setAlwaysOnTop(false);
        setSize(image.getWidth(this), image.getHeight(this));
        Dimension windowSize = new Dimension(image.getWidth(this), image.getHeight(this) + 100);
        setMinimumSize(windowSize);
        setMaximumSize(windowSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width > 3000) ? (screenSize.width - image.getWidth(this))/2 - 2*image.getWidth(this) : (screenSize.width - image.getWidth(this))/2;
        int y = (screenSize.height - image.getHeight(this))/2;
        int w = image.getWidth(this);
        int h = image.getHeight(this) + 100;
        Rectangle windowBounds = new Rectangle(x, y, w, h);
        setBounds(windowBounds);
        setBackground(Color.WHITE);
        MouseAdapter disposeOnClick = new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent evt) 
            {
                setVisible(false);
                run = false;
            }
        };
        addMouseListener(disposeOnClick);
        tc = new TestComp(new Rectangle(0, image.getHeight(this), windowSize.width, 100), programName, version, copyright);
        add(tc);
        cnt = 0;
    }

    private static class TestComp extends Component
    {
        private static final long serialVersionUID = -83012778166288L;
        private Rectangle r;
        private String programName;
        private String version;
        private String copyright;
        private static final Font f1 = new Font(Font.SANS_SERIF, Font.BOLD, 36);
        private static final Font f2 = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        private static final Font f3 = new Font(Font.SANS_SERIF, Font.BOLD, 8);

        public TestComp(Rectangle r, String programName, String version, String copyright)
        {
            super();
            this.r = r;
            this.programName = programName;
            this.version = version;
            this.copyright = copyright;
            setBounds(r);
        }

        @Override
        public void paint(Graphics g)
        {
            setBounds(r);
            g.setColor(Color.WHITE);
            g.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
            g.setColor(Color.BLACK);
            g.setFont(f1);
            FontMetrics fm = g.getFontMetrics();
            int h = fm.getHeight();
            int l = fm.stringWidth(programName);
            g.drawString(programName, getBounds().x + getBounds().width/2 - l/2, getBounds().y + h + 1);
            g.setFont(f2);
            fm = g.getFontMetrics();
            int h1 = fm.getHeight();
            l = fm.stringWidth(version);
            g.drawString(version, getBounds().x + getBounds().width/2 - l/2, getBounds().y + h + 1 + h1 + 6);
            g.setFont(f3);
            fm = g.getFontMetrics();
            int h2 = fm.getHeight();
            l = fm.stringWidth(copyright);
            g.drawString(copyright, getBounds().x + getBounds().width/2 - l/2, getBounds().y + h + 1 + h1 + 6 + h2 + 6);
        }

        
    }

    private void loadImage(URL imgSource)
    {
        try
        {
            tracker = new MediaTracker(this);
            image = Toolkit.getDefaultToolkit().getImage(imgSource);
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        }
        catch (Exception e) { com.makechip.util.Log.fatal(e); }
    }


    private int cnt;

    public void update(Graphics g)
    {
        super.update(g);
        paint(g);
    }

    public void paint(Graphics g)
    {
        Shape r = g.getClip();
        g.setClip(new Rectangle(0, 0, image.getWidth(this), image.getHeight(this)));
        g.drawImage(image, 0, 0, this);
        g.setClip(r);
        if (!upd || cnt == 0) tc.paint(g);
    }

    private boolean upd;

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
    {
        upd = true;
        paint(getGraphics());
        upd = false;
        cnt++;
        if (cnt == 32) cnt = 0;
        return(run);
    }

    public void splash()
    {
        pack();
        validate();
        setVisible(true);
        run = true;
        paint(getGraphics());
    }
}
