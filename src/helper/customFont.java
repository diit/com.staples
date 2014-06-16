package helper;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import debug.debug;

public class customFont extends Font{
	/**
	 * The MIT License (MIT)
	 * 
	 * Copyright (c) 2014 Dale Inverarity
	 * Permission is hereby granted, free of charge, to any person obtaining a copy
	 * of this software and associated documentation files (the "Software"), to deal
	 * in the Software without restriction, including without limitation the rights
	 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	 * copies of the Software, and to permit persons to whom the Software is
	 * furnished to do so, subject to the following conditions:
	 * 
	 * The above copyright notice and this permission notice shall be included in
	 * all copies or substantial portions of the Software.
	 * 
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	 * THE SOFTWARE.
	 */
	private static final long serialVersionUID = 6217157820534999561L;

	public customFont(String name, int style, int size) {
		super(load(name, style, size));
	}

	public static Font load(String name, int style, int size){
		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/"+name+".ttf"));
			debug.notify("FONT_GOOD");
		} catch (IOException e) {
			debug.notify("IO_ERROR");
			//e.printStackTrace();
		}
		catch(FontFormatException e)
		{
			debug.notify("FORMAT_ERROR");
			e.printStackTrace();
		}
		return customFont.deriveFont(style, size);
	}

}
