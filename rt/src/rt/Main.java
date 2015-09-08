package rt;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

<<<<<<< HEAD
import rt.basicscenes.Mandelbrot;
=======
import rt.renderers.MultiThreadedRenderer;
>>>>>>> origin/master
import rt.renderers.Renderer;
import rt.renderers.SingleThreadedRenderer;
import rt.testscenes.CameraTestScene;
import rt.testscenes.InstancingTeapots;
import rt.testscenes.InstancingTest;
import rt.testscenes.TeapotShadowTest;
import rt.testscenes.TriangleTest;

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
	public static Scene scene = new Mandelbrot();
=======
	public static Scene scene = new InstancingTeapots();
>>>>>>> origin/master
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException
	{			
		scene.prepare();
		Renderer renderer = new MultiThreadedRenderer(scene);
		renderer.render();
		renderer.writeImageToFile();
	}
	
}
