package rt;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import rt.renderers.DebuggingRenderer;
import rt.renderers.MultiThreadedRenderer;
import rt.renderers.Renderer;
<<<<<<< HEAD
import rt.renderers.SingleThreadedRenderer;
import rt.testscenes.Presentation;
=======
import rt.testscenes.InstancingTeapots;
import rt.testscenes.InstancingTest;
import rt.testscenes.Plane2D;
import rt.testscenes.Presentation;
import rt.testscenes.RefractiveSphere;
import rt.testscenes.TeapotShadowTest;
>>>>>>> branch 'master' of https://github.com/sjf2015/sjf-raytracer

/**
 * The main rendering loop. Provides multi-threading support. The {@link Main#scene} to be rendered
 * is hard-coded here, so you can easily change it. The {@link Main#scene} contains 
 * all configuration information for the renderer.
 */
public class Main {

	/** 
	 * The scene to be rendered.
	 */

<<<<<<< HEAD
	public static Scene scene = new Presentation();
=======
	public static Scene scene = new Plane2D();
>>>>>>> branch 'master' of https://github.com/sjf2015/sjf-raytracer

	
	public static void main(String[] args) throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException
	{			
		scene.prepare();
		Renderer renderer = new SingleThreadedRenderer(scene);
		renderer.render();
		renderer.writeImageToFile();
	}
	
}
