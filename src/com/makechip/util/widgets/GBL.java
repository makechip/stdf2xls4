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
*** Copyright (C) 2007 Eric West
*** This program is free software; you can redistribute it and/or modify
*** it under the terms of the GNU General Public License as published by
*** the Free Software Foundation; either version 2 of the License, or
*** (at your option) any later version.
*** This program is distributed in the hope that it will be useful,
*** but WITHOUT ANY WARRANTY; without even the implied warranty of
*** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*** GNU General Public License for more details.
*** You should have received a copy of the GNU General Public License
*** along with this program; if not, write to the Free Software
*** Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
**/
package com.makechip.util.widgets;


import java.awt.*;

/**
*** @author eric
*** @version $Id: GBL.java 1 2007-09-15 21:50:34Z eric $
**/
public class GBL extends GridBagLayout
{
    private static final long serialVersionUID = -7243488985550067493L;
    private GridBagConstraints gbc;
    private Dimension panelSize;
    private int unitWidth;
    private int unitHeight;
    private boolean[][] grid;
    private int curX;
    private int curY;

    public GBL()
    {
        super();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;

    }

    public GBL(Dimension panelSize, int unitWidth, int unitHeight)
    {   
        this();
        this.panelSize = new Dimension(panelSize);
        if (panelSize.width % unitWidth != 0)
        {
            throw new RuntimeException("Invalid unitWidth for GBL panelSize: " + panelSize + " unitWidth = " + unitWidth);
        }
        if (panelSize.height % unitHeight != 0)
        {
            throw new RuntimeException("Invalid unitHeight for GBL panelSize: " + panelSize + " unitHeight = " + unitHeight);
        }
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
        grid = new boolean[panelSize.width / unitWidth][panelSize.height / unitHeight];
        for (int i=0; i<grid.length; i++)
        {
            for (int j=0; j<grid[i].length; j++)
            {
                grid[i][j] = false;
            }
        }
        curX = 0;
        curY = 0;
    }

    public void set(Component c)
    {
        setConstraints(c, gbc);
    }

    public void autoSet(Component c)
    {
        if (panelSize.width < c.getPreferredSize().width)
        {
            throw new RuntimeException("Invalid component width: " + c);
        }
        if (c.getPreferredSize().width % unitWidth != 0)
        {
            throw new RuntimeException("Invalid component width: " + c);
        }
        if (panelSize.height < c.getPreferredSize().height)
        {
            throw new RuntimeException("Invalid component height: " + c);
        }
        if (c.getPreferredSize().height % unitHeight != 0)
        {
            throw new RuntimeException("Invalid component height: " + c);
        }
        if (panelSize == null)
        {
            throw new RuntimeException("autoSet may not be used without initializing panelSize");
        }
        gbc.gridx = curX;
        gbc.gridy = curY;
        gbc.gridwidth = c.getPreferredSize().width / unitWidth;
        gbc.gridheight = c.getPreferredSize().height / unitHeight;
        setConstraints(c, gbc);
        com.makechip.util.Log.debug("curX = " + curX + " curY = " + curY);
        for (int i=curX; i<curX + gbc.gridwidth; i++)
        {
            for (int j=curY; j<curY + gbc.gridheight; j++)
            {
                grid[i][j] = true;
                com.makechip.util.Log.debug("    i = " + i + " j = " + j + " : true");
            }
        }
        boolean b = false;
        for (int j=0; j<grid[0].length; j++)
        {
            for (int i=0; i<grid.length; i++)
            {
                com.makechip.util.Log.debug("        grid[" + i + "][" + j + "] = " + grid[i][j]);
                if (!grid[i][j])
                {
                    curX = i;
                    curY = j;
                    b = true;
                    break;
                }
            }
            if (b) break;
        }
    }

    public void updatePosition(int gridX, int gridY)
    {
        gbc.gridx = gridX;
        gbc.gridy = gridY;
    }

    public void updateSize(int width, int height)
    {
        gbc.gridwidth = width;
        gbc.gridheight = height;
    }

    public void updateWeights(double wx, double wy)
    {
        gbc.weightx = wx;
        gbc.weighty = wy;
    }

    public void updateAnchor(Anchor_t anchor)
    {
        gbc.anchor = anchor.getConstant();
    }

    public void updateFill(Fill_t fill)
    {
        gbc.fill = fill.getConstant();
    }

    public void updateInsets(int top, int left, int bottom, int right)
    {
        gbc.insets = new Insets(top, left, bottom, right);
    }

    public void updatePads(int ipadx, int ipady)
    {
        gbc.ipadx = ipadx;
        gbc.ipady = ipady;
    }

}
