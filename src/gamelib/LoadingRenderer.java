package gamelib;

import java.awt.Graphics2D;

/**
 * Render loading screen.<br>
 * So to say, 'Now Loading'.
 * @author yappy
 */
public interface LoadingRenderer {

	void init();
	
	void destroy();

	void render(Graphics2D g);

}
