package rt.testscenes;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.PointLight;
import rt.samplers.*;
import rt.tonemappers.*;

/**
 * A very simple scene for testing sphere intersections.
 *
 */
public class SphereTest extends Scene {

	public SphereTest()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/SphereTest");
		
		// Image width and height in pixels
		width = 1280;
		height = 720;
		
		// Number of samples per pixel
		SPP = 1;
		
		// Specify which camera, film, and tonemapper to use
		Vector3f eye = new Vector3f(0.5f, 0.5f, 5.f);
		Vector3f lookAt = new Vector3f(0.5f, 0.f, 0.f);
		Vector3f up = new Vector3f(0.2f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new MovableCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
					
		root = new Sphere(new Point3f(0, 0, 0), 2f);
		
		// Light sources
		LightGeometry pointLight = new PointLight(new Vector3f(0.f, 3.f, 2.f), new Spectrum(10.f, 10.f, 10.f));
		lightList = new LightList();
		lightList.add(pointLight);
	}

}
