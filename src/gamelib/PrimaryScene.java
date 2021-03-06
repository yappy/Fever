package gamelib;

import gamelib.graphics.SpriteSet;
import gamelib.sound.SoundSet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public abstract class PrimaryScene extends AbstractScene {

	/** Renderer while loading. */
	private LoadingRenderer loadingRenderer;

	private ExecutorService loadService;
	private List<LoadingTask> loadingTasks = new ArrayList<LoadingTask>();
	private List<Future<?>> futures = new ArrayList<Future<?>>();

	private SpriteSet spriteSet = new SpriteSet();
	private SoundSet soundSet = new SoundSet();

	protected PrimaryScene(String name, LoadingRenderer loadingRenderer) {
		super(name);
		this.loadingRenderer = loadingRenderer;
	}

	/**
	 * Add resources to load.
	 * @param spriteSet
	 * @param soundSet
	 */
	protected abstract void setupLoadResource(SpriteSet spriteSet, SoundSet soundSet);

	/**
	 * Add loading tasks. Override if additional asynchronous tasks exist.
	 * @param loadingTasks
	 */
	protected void setupLoadTask(List<LoadingTask> loadingTasks) {
		loadingTasks.add(spriteSet);
		loadingTasks.add(soundSet);
	}

	protected Image getImage(int id) {
		return spriteSet.getImage(id);
	}

	protected Image getImage(int id, int index) {
		return spriteSet.getImage(id, index);
	}

	@Override
	public void init(SceneController sceneController) throws GameLibException, GameException {
		super.init(sceneController);
		setupLoadResource(spriteSet, soundSet);
		setupLoadTask(loadingTasks);
		loadService = Executors.newCachedThreadPool();
		futures.clear();
		for(LoadingTask task : loadingTasks){
			futures.add(loadService.submit(task));
		}
		loadService.shutdown();
		sceneController.pushScene(new LoadingScene());
	}

	@Override
	public void destroy() {
		loadService.shutdownNow();
		for(LoadingTask task : loadingTasks){
			task.unload();
		}
		super.destroy();
	}

	private boolean checkAllTasks() throws GameLibException {
		for(Future<?> future : futures){
			if(future.isDone()){
				try{
					future.get();
				}
				catch(ExecutionException e){
					throw new GameLibException("Exception occured in loading task thread", e);
				}
				catch(InterruptedException e){
					throw new GameLibError(e);
				}
			}
			else{
				return false;
			}
		}
		return true;
	}

	private final class LoadingScene extends AbstractScene {

		protected LoadingScene() {
			super("LoadingScene");
			loadingRenderer.init();
		}
		@Override
		public void doFrame(SceneController sceneController) throws GameLibException, GameException {
			if(checkAllTasks()){
				loadingRenderer.destroy();
				sceneController.popScene();
			}
		}

		@Override
		public void render(Graphics2D g) throws GameLibException, GameException {
			loadingRenderer.render(g);
		}

	}

}
