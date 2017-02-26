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
package com.makechip.stdf2xls4.excel;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;

import jxl.CellType;

public enum Cell_t
{
	BLANK(CellType.EMPTY, Cell.CELL_TYPE_BLANK),
	BOOLEAN(CellType.BOOLEAN, Cell.CELL_TYPE_BOOLEAN),
	ERROR(CellType.ERROR, Cell.CELL_TYPE_ERROR),
	FORMULA(CellType.NUMBER_FORMULA, Cell.CELL_TYPE_FORMULA),
	NUMERIC(CellType.NUMBER, Cell.CELL_TYPE_NUMERIC),
	STRING(CellType.LABEL, Cell.CELL_TYPE_STRING);
	
	public final CellType jxlType;
    public final int poiType;
	
	private Cell_t(CellType jxlType, int poiType)
	{
		this.jxlType = jxlType;
		this.poiType = poiType;
	}
	
	public static Cell_t getCellType(int poitype)
	{
		Cell_t ct = Arrays.stream(Cell_t.class.getEnumConstants())
				        .filter(type -> type.poiType == poitype)
				        .findFirst().orElse(null);
		return(ct);
	}
	
	public static Cell_t getCellType(CellType jxltype)
	{
		Cell_t ct = Arrays.stream(Cell_t.class.getEnumConstants())
				        .filter(type -> type.jxlType.equals(jxltype))
				        .findFirst().orElse(null);
		return(ct);
	}
	
}

