// Copyright 2011,2012 makechip.com
// This file is part of stdf2xls.
// 
// stdf2xls is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// stdf2xls is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with stdf2xls.  If not, see <http://www.gnu.org/licenses/>.

package com.makechip.stdf2xls4.xls;

import jxl.format.Alignment;

public enum Alignment_t  
{
    CENTER(Alignment.CENTRE),
    LEFT(Alignment.LEFT),
    RIGHT(Alignment.RIGHT),
    JUSTIFY(Alignment.JUSTIFY),
    DEFAULT(Alignment.CENTRE);

    private Alignment alignment;

    private Alignment_t(Alignment alignment)
    {
        this.alignment = alignment;
    }

    public Alignment getAlignment() { return(alignment); }

    public static Alignment_t getAlignment(int ordinal)
    {
        switch (ordinal)
        {
            case 0: return(CENTER); 
            case 1: return(LEFT);
            case 2: return(RIGHT);
            case 3: return(JUSTIFY);
            default:
        }
        return(DEFAULT);
    }


}
