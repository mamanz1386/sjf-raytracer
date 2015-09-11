package rt;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import rt.renderers.DebuggingRenderer;
import rt.renderers.MultiThreadedRenderer;
import rt.renderers.Renderer;
import rt.testscenes.InstancingTeapots;
import rt.testscenes.InstancingTest;
import rt.testscenes.Plane2D;
import rt.testscenes.Presentation;
import rt.testscenes.RefractiveSphere;
import rt.testscenes.TeapotShadowTest;

/**
 * The main rendering loop. Provides multi-threading support. The {@link Main#scene} to be rendered
 * is hard-coded here, so you can easily change it. The {@link Main#scene} contains 
 * all configuration information for the renderer.
 */
public class Main {

	/** 
	 * The scene to be rendered.
	 */

	public static Scene scene = new Plane2D();

	
	public static void main(String[] args) throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException
	{			
		scene.prepare();
		Renderer renderer = new MultiThreadedRenderer(scene);
		renderer.render();
		renderer.writeImageToFile();
	}
	
}
