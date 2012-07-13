package gamelib.graphics;

import gamelib.resource.ResourceManager;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


/**
 * @author yappy
 */
public class ImageLoader {

	private static final GraphicsConfiguration conf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	public Image[] load(String name, int width, int height) throws GraphicsException {
		BufferedImage src;
		BufferedImage[] res;
		try{
			InputStream in = ResourceManager.getInstance().getInputStream(name);
			if(in == null)
				throw new FileNotFoundException(name);
			src = ImageIO.read(in);
			in.close();
		}
		catch(IOException e){
			throw new GraphicsException(e);
		}
		int xsize = src.getWidth() / width;
		int ysize = src.getHeight() / height;
		res = new BufferedImage[ysize * xsize];
		for(int y = 0; y < ysize; y++){
			for(int x = 0; x < xsize; x++){
				res[y * xsize + x] = conf.createCompatibleImage(width, height, Transparency.BITMASK);
				Graphics2D g = res[y * xsize + x].createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage(src, 0, 0, width, height, x * width, y * height, (x + 1) * width, (y + 1) * height, null);
				g.dispose();
			}
		}
		src.flush();
		return res;
	}

	public Image createInverseX(Image src) {
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		BufferedImage res = conf.createCompatibleImage(w, h, Transparency.BITMASK);
		Graphics2D g = res.createGraphics();
		g.scale(-1, 1);
		g.translate(-w, 0);
		g.setComposite(AlphaComposite.Src);
		g.drawImage(src, 0, 0, null);
		g.dispose();
		return res;
	}

	// Singleton
	private ImageLoader() {}

	public static final ImageLoader instance = new ImageLoader();

	public static ImageLoader getInstance() {
		return instance;
	}

}
