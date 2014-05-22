package helper;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import debug.debug;

public class customFont extends Font{
	/**
	 * 
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
