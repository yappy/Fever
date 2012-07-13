package gamelib.sound;

import gamelib.LoadingTask;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;


/**
 * @author yappy
 */
public class SoundSet implements LoadingTask {

	private List<String> names;
	private List<Clip> clips;

	public SoundSet() {
		names = new ArrayList<String>();
		clips = new ArrayList<Clip>();
	}

	public int add(String name) {
		names.add(name);
		return names.size() - 1;
	}

	public void play(int id) {
		Clip clip = clips.get(id);
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	@Override
	public void unload() {
		for(Clip clip : clips){
			clip.close();
		}
		clips.clear();
	}

	@Override
	public Object call() throws Exception {
		assert clips.isEmpty();
		SoundLoader loader = SoundLoader.getInstance();
		for(String name : names){
			clips.add(loader.load(name));
			Thread.sleep(1);
		}
		return null;
	}

}
