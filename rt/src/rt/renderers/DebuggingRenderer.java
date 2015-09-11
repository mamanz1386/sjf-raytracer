package rt.renderers;

import java.util.concurrent.ExecutionException;

import rt.Scene;

public class DebuggingRenderer extends Renderer {

	private final int x, y;

	public DebuggingRenderer(Scene scene, int x, int y) {
		super(scene);
		this.x = x;
		this.y = y;
	}

	@Override
	protected void renderInternally() throws InterruptedException,
			ExecutionException {
		RenderTask wholeImageTask = new RenderTask(scene, x, x+50, y, y+50);
		wholeImageTask.run();
	}
	
	protected String rendererMessage() {
		return "Only the pixel (" + x + ", " + y + ")";
	}

}
