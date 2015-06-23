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
package com.makechip.stdf2xls4.xls;

import java.util.IdentityHashMap;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import com.makechip.util.Identity;
import com.makechip.util.Immutable;
import com.makechip.util.factory.IdentityFactoryEESE;

public class FontType implements Immutable, Identity
{
	static IdentityFactoryEESE<FontName_t, FontStyle_t, IndexedColors, FontType> map =
		new IdentityFactoryEESE<FontName_t, FontStyle_t, IndexedColors, FontType>(FontName_t.class, FontStyle_t.class, IndexedColors.class, FontType.class);
	static IdentityHashMap<FontType, Font> fonts = 
		new IdentityHashMap<FontType, Font>();
	
	private final FontName_t fontName;
	private final FontStyle_t style;
	private final short fontSize;
	private final IndexedColors color;
	
	private FontType(FontName_t fontName, FontStyle_t style, short fontSize, IndexedColors color)
	{
		this.fontName = fontName;
		this.style = style;
		this.fontSize = fontSize;
		this.color = color;
	}
	
	public static Font getFont(Workbook wb, FontName_t fontName, short size, FontStyle_t style, IndexedColors color)
	{
		FontType f = map.getValue(fontName, style, size, color);
		Font ff = fonts.get(f);
		if (ff == null)
		{
			ff = style.createFont(wb, size);
			ff.setFontName(fontName.getFont());
			ff.setColor(color.getIndex());
			fonts.put(f, ff);
		}
		return(ff);
	}
	
	

	@Override
	public int getInstanceCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public FontName_t getFontName()
	{
		return fontName;
	}

	public FontStyle_t getStyle()
	{
		return style;
	}

	public IndexedColors getColor()
	{
		return color;
	}

	public short getFontSize()
	{
		return fontSize;
	}

}
