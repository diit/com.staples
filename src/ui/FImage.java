package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class FImage extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5138998743557176574L;

	public FImage(String img){
		super(new ImageIcon(getImage(img)));
	}
	
	public static BufferedImage getImage(String img){
		BufferedImage lblPic = null;
		try {
			lblPic = ImageIO.read(new File(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lblPic;
	}
}
