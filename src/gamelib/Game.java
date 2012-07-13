package gamelib;

import gamelib.util.Trace;

import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import javax.swing.JFrame;

/**
 * Base class of game main class.<br>
 * This class provides basic game framework.
 * 
 * @author yappy
 */
public abstract class Game {

	/** Frame synchronizer. */
	protected FrameSync frameSync;

	private int width;
	private int height;
	private int fps;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Insets insets;
	private boolean fullScreen;
	private volatile boolean closeRequested = false;
	private Deque<Scene> sceneStack = new ArrayDeque<Scene>();

	/**
	 * Initialize and return initial scene.<br>
	 * Called at first of game.
	 * 
	 * @return Initial Scene
	 */
	protected abstract Scene initialize() throws GameLibException,
			GameException;

	/**
	 * Called at last of game.
	 */
	protected abstract void terminate() throws GameLibException, GameException;

	protected abstract void frame() throws GameLibException, GameException;

	protected abstract void renderFinish(Graphics2D g) throws GameLibException,
			GameException;

	/**
	 * Set basic information of game.
	 * 
	 * @param title
	 *            Title to be displayed at title bar
	 * @param icons
	 *            Window icon list
	 * @param width
	 *            Screen width
	 * @param height
	 *            Screen height
	 * @param fps
	 *            FPS
	 */
	protected Game(String title, List<Image> icons, int width, int height,
			int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;
		frameSync = new FrameSync(fps, 2);

		frame = new JFrame(title);
		frame.setResizable(false);
		frame.setIconImages(icons);
		frame.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeRequested = true;
			}
		});
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
	}

	private class GameThread extends Thread {
		public GameThread() {
			super("MainLoopThread");
		}

		@Override
		public void run() {
			try {
				frameSync.beginGame();
				final SceneController sceneController = new SceneControllerImpl();
				Scene initialScene = initialize();
				sceneController.pushScene(initialScene);

				GAMELOOP: while (!sceneStack.isEmpty() && !closeRequested) {
					boolean render = frameSync.beginFrame();
					frame();

					// frame process
					Scene scene;
					do {
						scene = sceneStack.peek();
						scene.doFrame(sceneController);
						if (sceneStack.isEmpty())
							break GAMELOOP;
					} while (scene != sceneStack.peek());

					// rendering
					if (render) {
						do {
							do {
								Graphics2D g = (Graphics2D) bufferStrategy
										.getDrawGraphics();
								if (!fullScreen) {
									g.translate(insets.left, insets.top);
								}
								// clear
								g.setColor(Color.BLACK);
								g.fillRect(0, 0, width, height);
								// render by scene
								scene.render(g);
								// for FPS etc.
								renderFinish(g);
								g.dispose();
							} while (bufferStrategy.contentsRestored());
							bufferStrategy.show();
						} while (bufferStrategy.contentsLost());
					}
				}
				while (!sceneStack.isEmpty()) {
					sceneStack.pop().destroy();
				}
				terminate();
			} catch (Exception e) {
				throw new GameLibError(e);
			} finally {
				frame.dispose();
			}
		}
	}

	private class SceneControllerImpl implements SceneController {
		@Override
		public void jumpScene(Scene scene) throws GameLibException,
				GameException {
			while (!sceneStack.isEmpty()) {
				popScene();
			}
			pushScene(scene);
		}

		@Override
		public void popScene() throws GameLibException, GameException {
			sceneStack.pop().destroy();
		}

		@Override
		public void popScene(int n) throws GameLibException, GameException {
			for (int i = 0; i < n; i++) {
				popScene();
			}
		}

		@Override
		public void pushScene(Scene scene) throws GameLibException,
				GameException {
			sceneStack.push(scene);
			scene.init(this);
		}

		@Override
		public void quit() throws GameLibException, GameException {
			while (!sceneStack.isEmpty()) {
				popScene();
			}
		}
	}

	/**
	 * Please call after construction only once.
	 * 
	 * @param fullScreen
	 *            true if full screen
	 */
	public void startGame(boolean fullScreen) {
		setScreenMode(fullScreen);
		new GameThread().start();
	}

	/**
	 * Change screen mode.
	 * 
	 * @param fullScreen
	 *            true if full screen
	 */
	public void setScreenMode(boolean fullScreen) {
		frame.dispose();
		frame.setUndecorated(fullScreen);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		if (fullScreen) {
			GraphicsDevice device = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			device.setFullScreenWindow(frame);
			try {
				device.setDisplayMode(new DisplayMode(width, height, 32, fps));
			} catch (Exception e) {
				try {
					device.setDisplayMode(new DisplayMode(width, height, 32,
							DisplayMode.REFRESH_RATE_UNKNOWN));
				} catch (Exception e2) {
					try {
						device.setDisplayMode(new DisplayMode(width, height,
								DisplayMode.BIT_DEPTH_MULTI,
								DisplayMode.REFRESH_RATE_UNKNOWN));
					} catch (Exception e3) {
						throw new GameLibError(e3);
					}
				}
			}
		} else {
			insets = frame.getInsets();
		}
		frame.createBufferStrategy(2);
		bufferStrategy = frame.getBufferStrategy();
		this.fullScreen = fullScreen;

		Trace.info("FullScreen: " + fullScreen);
		printCapabilities();
		Trace.info("");
	}

	/**
	 * full {@literal <->} window
	 */
	public void toggleScreenMode() {
		setScreenMode(!fullScreen);
	}

	/**
	 * @return true if fullscreen
	 */
	public boolean isFullScreen() {
		return fullScreen;
	}

	private void printCapabilities() {
		BufferCapabilities cap = bufferStrategy.getCapabilities();
		ImageCapabilities front = cap.getFrontBufferCapabilities();
		ImageCapabilities back = cap.getBackBufferCapabilities();

		Trace.info("BufferStrategy Caps");
		Trace.info("MultiBuffer: " + cap.isMultiBufferAvailable());
		Trace.info("Flip: " + cap.isPageFlipping());
		Trace.info("FullScreenRequired: " + cap.isFullScreenRequired());

		Trace.info("FrontBuffer Caps");
		Trace.info("Acceleration: " + front.isAccelerated());
		Trace.info("VideoMemory: " + front.isTrueVolatile());

		Trace.info("BackBuffer Caps");
		Trace.info("Acceleration: " + back.isAccelerated());
		Trace.info("VideoMemory: " + back.isTrueVolatile());
	}

}
