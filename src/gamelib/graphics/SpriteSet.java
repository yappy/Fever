package gamelib.graphics;

import gamelib.LoadingTask;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yappy
 */
public class SpriteSet implements LoadingTask {

	private List<LoadElement> elements;
	private List<Image[]> images;

	public SpriteSet() {
		elements = new ArrayList<LoadElement>();
		images = new ArrayList<Image[]>();
	}

	public int add(String name, int width, int height) {
		elements.add(new LoadElement(name, width, height));
		return elements.size() - 1;
	}

	public Image getImage(int id) {
		return getImages(id)[0];
	}

	public Image getImage(int id, int index) {
		return getImages(id)[index];
	}

	public Image[] getImages(int id) {
		return images.get(id);
	}

	@Override
	public void unload() {
		for(Image[] a : images){
			for(Image img : a){
				img.flush();
			}
		}
		images.clear();
	}

	@Override
	public Object call() throws Exception {
		assert images.isEmpty();
		ImageLoader loader = ImageLoader.getInstance();
		for(LoadElement elem : elements){
			images.add(loader.load(elem.name, elem.width, elem.height));
			Thread.sleep(1);
		}
		return null;
	}

	private class LoadElement {
		public String name;
		public int width;
		public int height;

		public LoadElement(String name, int width, int height) {
			this.name = name;
			this.width = width;
			this.height = height;
		}
	}

}
