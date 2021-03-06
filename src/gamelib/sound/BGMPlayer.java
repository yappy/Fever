package gamelib.sound;

import gamelib.resource.ResourceManager;
import gamelib.resource.ResourceSystem;
import gamelib.util.FileInputStreamEx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * BGM Manager.<br>
 * It seems that {@link SourceDataLine#write(byte[], int, int)} etc consume interrupted flag. So I used private volatile boolean flag.
 * 
 * @author yappy
 */
public final class BGMPlayer {

	private static final int BUFFER_SIZE = 1024;
	private SoundThread currentThread = null;
	private volatile boolean stopFlag = false;

	/**
	 * Play BGM. This method calls stop automatically.
	 * @param intro played once(null if no intro)
	 * @param loop played after intro <b>Must implement mark/reset</b>
	 * @throws SoundException failed to start playing
	 */
	public void play(InputStream intro, InputStream loop) throws SoundException {
		stop();
		try{
			currentThread = new SoundThread(intro, loop);
		}
		catch(Exception e){
			throw new SoundException(e);
		}
		currentThread.start();
	}

	/**
	 * Play BGM from {@link ResourceSystem}.
	 * @see #play(InputStream, InputStream)
	 * @param introName resource name(nullable)
	 * @param loopName resource name
	 */
	public void play(String introName, String loopName) throws SoundException {
		ResourceManager manager = ResourceManager.getInstance();
		try{
			InputStream introIn = introName != null ? manager.getInputStream(introName) : null;
			InputStream loopIn = manager.getInputStream(loopName);
			play(introIn, loopIn);
		}
		catch(IOException e){
			throw new SoundException(e);
		}
	}

	/**
	 * Stop playing BGM. If BGM is not playing, this method does nothing.
	 */
	public void stop() {
		if(currentThread != null){
			stopFlag = true;
			currentThread.interrupt();
			try{
				currentThread.join();
			}
			catch(InterruptedException e){
				throw new SoundError(e);
			}
			stopFlag = false;
			currentThread = null;
		}
	}

	private class SoundThread extends Thread {
		private AudioInputStream introIn;
		private AudioInputStream loopIn;
		private SourceDataLine introLine;
		private SourceDataLine loopLine;

		private SoundThread(InputStream intro, InputStream loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			super("BGM Thread");
			setDaemon(true);
			if(intro != null){
				introIn = AudioSystem.getAudioInputStream(intro);
				introLine = AudioSystem.getSourceDataLine(introIn.getFormat());
			}
			loopIn = AudioSystem.getAudioInputStream(loop);
			loopLine = AudioSystem.getSourceDataLine(loopIn.getFormat());
		}

		@Override
		public void run() {
			int size;
			byte[] buf = new byte[BUFFER_SIZE];
			try{
				if(introLine != null){
					introLine.open();
				}
				loopLine.open();

				if(introLine != null){
					introLine.start();
					while((size = introIn.read(buf, 0, buf.length)) != -1){
						introLine.write(buf, 0, size);
						if(stopFlag)
							throw new InterruptedException();
					}
					introLine.drain();
					introLine.close();
				}

				loopLine.start();
				loopIn.mark(Integer.MAX_VALUE);
				while(true){
					while((size = loopIn.read(buf, 0, buf.length)) != -1){
						loopLine.write(buf, 0, size);
						if(stopFlag)
							throw new InterruptedException();
					}
					loopIn.reset();
					loopLine.drain();
				}
			}
			catch(InterruptedException e){
				return;
			}
			catch(Exception e){
				throw new SoundError(e);
			}
			finally{
				if(introLine != null){
					introLine.close();
				}
				loopLine.close();
			}
		}
	}

	/*
	 * Singleton
	 */
	private static final BGMPlayer instance = new BGMPlayer();

	private BGMPlayer() {}

	/**
	 * Get the instance.
	 * 
	 * @return instance.
	 */
	public static BGMPlayer getInstance() {
		return instance;
	}

	public static void main(String[] args) throws Exception {
		BGMPlayer player = BGMPlayer.getInstance();
		player.play(new FileInputStream("chimes.wav"), new FileInputStreamEx(new File("chord.wav")));
		Thread.sleep(5000);
		player.stop();
		Thread.sleep(3000);
	}

}
