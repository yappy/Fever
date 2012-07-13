package mj.game;

import java.awt.Color;
import java.awt.Graphics2D;

import gamelib.LoadingRenderer;

public class LoadingRendererImpl implements LoadingRenderer {

	private int count = 0;

	@Override
	public void init() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void render(Graphics2D g) {
		int c = count <= 255 ? count : 255 * 2 - count;
		g.setColor(new Color(c, c, c));
		g.drawString("Now Loading...", 0, 480);
		g.setColor(Color.WHITE);
		count++;
		count %= 255 * 2;
	}

}
