package gamelib.sound;

import gamelib.resource.ResourceManager;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


/**
 * @author yappy
 */
public final class SoundLoader {

	public Clip load(String name) throws SoundException {
		try{
			InputStream in = ResourceManager.getInstance().getInputStream(name);
			if(in == null)
				throw new FileNotFoundException(name);
			AudioInputStream ain = AudioSystem.getAudioInputStream(in);
			AudioFormat audioFormat = ain.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(ain);
			return clip;
		}
		catch(Exception e){
			throw new SoundException(e);
		}
	}

	/*
	 * Singleton
	 */
	private static final SoundLoader instance = new SoundLoader();

	private SoundLoader() {}

	/**
	 * Get the instance.
	 * 
	 * @return instance.
	 */
	public static SoundLoader getInstance() {
		return instance;
	}

}
