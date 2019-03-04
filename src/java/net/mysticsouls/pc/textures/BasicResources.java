package net.mysticsouls.pc.textures;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class BasicResources {
	
	public static Image getBasicImage(String path) {
		URL pathToImg = BasicResources.class.getResource("/src/net/mysticsouls/pc/textures/"+path);
		ImageIcon icon = new ImageIcon(pathToImg);
		return icon.getImage();
	}

}
