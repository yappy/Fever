package gamelib.graphics;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author yappy
 */
public final class GraphicsUtil {

	private GraphicsUtil() {}

	public static void drawImageCenter(Graphics2D g, Image image, int x, int y) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		g.drawImage(image, x + w / 2, y + h / 2, null);
	}

	/**
	 * This method doesn't use acsent/descent.
	 */
	public static void drawStringCenter(Graphics2D g, String str, int x, int y) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(str);
		int h = metrics.getAscent();
		g.drawString(str, x - w / 2, y + h / 2);
	}

	public static void drawStringCenterH(Graphics2D g, String str, int x1, int x2, int y) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(str);
		g.drawString(str, (x1 + x2 - w) / 2, y);
	}

}
