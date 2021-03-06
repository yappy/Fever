package gamelib;

import java.util.Formatter;

/**
 * Frame synchronizer.
 * @author yappy
 */
public class FrameSync {

	private long nanoInterval;
	private int maxFrameSkip;
	private long baseTime;
	private int skipCount = 0;
	private int frameSkip = 0;
	private double fps;

	public FrameSync(int fps, int maxFrameSkip) {
		this.nanoInterval = (1000L * 1000L * 1000L) / fps;
		this.maxFrameSkip = maxFrameSkip;
	}

	public void beginGame() {
		baseTime = System.nanoTime();
	}

	/**
	 * 
	 * @return true if you should render.
	 */
	public boolean beginFrame() {
		boolean skip;
		long cur = System.nanoTime();
		skipCount++;
		// too early
		if(cur - baseTime < nanoInterval * skipCount){
			do{
				cur = System.nanoTime();
			}
			while(cur - baseTime < nanoInterval * skipCount);
			skip = false;
		}
		// too late, but render by limit
		else if(skipCount > maxFrameSkip){
			skip = false;
		}
		// too late and skip
		else{
			skip = true;
		}
		if(!skip){
			// information for display
			fps = 1e9 / (System.nanoTime() - baseTime);
			frameSkip = skipCount - 1;

			skipCount = 0;
			baseTime = System.nanoTime();
			return true;
		}
		else{
			return false;
		}
	}

	public double getFPS() {
		return fps;
	}

	public int getFrameSkip() {
		return frameSkip;
	}

	private StringBuilder buffer = new StringBuilder();
	private Formatter formatter = new Formatter(buffer);

	@Override
	public String toString() {
		buffer.setLength(0);
		return formatter.format("FPS=%.1f Skip=%d", fps, frameSkip).toString();
	}

}
