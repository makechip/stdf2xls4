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
import java.awt.geom.*;

/**
*** @author eric
*** @version $Id: Arrow.java 1 2007-09-15 21:50:34Z eric $
**/
public class Arrow
{

    public static void draw(Graphics2D g, Color c, int x, int y, double scale, double angle)
    {
        Shape sh1 = null;
        Shape sh2 = null;
        g.setPaint(c);
        Polygon p = new Polygon(new int[] { 0, -8, -8 }, new int[] { 0, 3, -3 }, 3);
        if (scale != 1.0)
        {
            AffineTransform s = AffineTransform.getScaleInstance(scale, scale);
            sh1 = s.createTransformedShape(p);
        }
        else sh1 = p;
        if (angle != 0.0)
        {
            AffineTransform s = AffineTransform.getRotateInstance(angle);
            sh2 = s.createTransformedShape(sh1);
        }
        else sh2 = sh1;
        AffineTransform s = AffineTransform.getTranslateInstance(x, y);
        sh1 = s.createTransformedShape(sh2);
        g.fill(sh1);
    }

}
