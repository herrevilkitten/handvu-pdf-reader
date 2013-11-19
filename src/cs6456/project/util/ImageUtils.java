package cs6456.project.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * This class loads images from the classpath and then caches
 * them for later use.  A few of the button icons are used
 * more than once, so they only need to be loaded once.
 */
public class ImageUtils {
	static Map<String,Image> imageCache = new HashMap<String,Image>();
	static Map<String,ImageIcon> iconCache = new HashMap<String,ImageIcon>();
	
	public static Image getImage(String path) {
		if ( imageCache.containsKey(path) ) {
			return imageCache.get(path);
		}
		Image image = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getResource(path));
		imageCache.put(path, image);
		return image;
	}
	
	public static ImageIcon getIcon(String path) {
		if ( iconCache.containsKey(path) ) {
			return iconCache.get(path);
		}
		Image image = getImage(path);
		ImageIcon icon = new ImageIcon(image);
		iconCache.put(path, icon);
		return icon;
	}
	
	public static BufferedImage getBufferedImage(String path) throws IOException {
		URL url = ImageUtils.class.getResource(path);
		return ImageIO.read(url);
	}
}
