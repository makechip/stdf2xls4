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
*** @version $Id: Anchor_t.java 1 2007-09-15 21:50:34Z eric $
**/
public enum Anchor_t
{
    CENTER(GridBagConstraints.CENTER),
    NORTH(GridBagConstraints.NORTH),
    NORTHEAST(GridBagConstraints.NORTHEAST),
    EAST(GridBagConstraints.EAST),
    SOUTHEAST(GridBagConstraints.SOUTHEAST),
    SOUTH(GridBagConstraints.SOUTH),
    SOUTHWEST(GridBagConstraints.SOUTHWEST),
    WEST(GridBagConstraints.WEST),
    NORTHWEST(GridBagConstraints.NORTHWEST),
    PAGE_START(GridBagConstraints.PAGE_START),
    PAGE_END(GridBagConstraints.PAGE_END),
    LINE_START(GridBagConstraints.LINE_START),
    LINE_END(GridBagConstraints.LINE_END),
    FIRST_LINE_START(GridBagConstraints.FIRST_LINE_START),
    FIRST_LINE_END(GridBagConstraints.FIRST_LINE_END),
    LAST_LINE_START(GridBagConstraints.LAST_LINE_START),
    LAST_LINE_END(GridBagConstraints.LAST_LINE_END),
    BASELINE(GridBagConstraints.BASELINE),
    BASELINE_LEADING(GridBagConstraints.BASELINE_LEADING),
    BASELINE_TRAILING(GridBagConstraints.BASELINE_TRAILING),
    ABOVE_BASELINE(GridBagConstraints.ABOVE_BASELINE),
    ABOVE_BASELINE_LEADING(GridBagConstraints.ABOVE_BASELINE_LEADING),
    ABOVE_BASELINE_TRAILING(GridBagConstraints.ABOVE_BASELINE_TRAILING),
    BELOW_BASELINE(GridBagConstraints.BELOW_BASELINE),
    BELOW_BASELINE_LEADING(GridBagConstraints.BELOW_BASELINE_LEADING),
    BELOW_BASELINE_TRAILING(GridBagConstraints.BELOW_BASELINE_TRAILING);

    private int constant;

    private Anchor_t(int constant)
    {
        this.constant = constant;
    }

    public int getConstant()
    {
        return(constant);
    }

}
