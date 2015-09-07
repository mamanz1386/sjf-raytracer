package rt.testscenes;

import javax.vecmath.Vector3f;

import rt.LightGeometry;
import rt.LightList;
import rt.Scene;
import rt.Spectrum;
import rt.cameras.MovableCamera;
import rt.films.BoxFilterFilm;
import rt.integrators.TrivialIntegratorFactory;
import rt.intersectables.IntersectableList;
import rt.intersectables.Plane;
import rt.lightsources.PointLight;
import rt.materials.Diffuse;
import rt.samplers.OneSamplerFactory;
import rt.tonemappers.ClampTonemapper;

/**
 * Test scene for pinhole camera specifications.
 */
public class CameraTestScene extends Scene {

	public CameraTestScene()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/CameraMovable");
		
		// Image width and height in pixels
		width = 1280;
		height = 720;
		
		// Number of samples per pixel
		SPP = 1;
		
		// Specify which camera, film, and tonemapper to use
		Vector3f eye = new Vector3f(0.5f, 0.5f, 3.f);
		Vector3f lookAt = new Vector3f(0.5f, 0.f, 0.f);
		Vector3f up = new Vector3f(0.2f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new MovableCamera(eye, lookAt, up, fov, aspect, width, height);
		//camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		integratorFactory = new TrivialIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
		
		// Define some objects to be added to the scene. 
		// 5 planes can be used to define a box (with never ending walls).
		Plane p1 = new Plane(new Vector3f(1.f, 0.f, 0.f), 1.f);
		Plane p2 = new Plane(new Vector3f(-1.f, 0.f, 0.f), 1.f);
		p2.material = new Diffuse(new Spectrum(1f, 0f, 0f));
		Plane p3 = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
		Plane p4 = new Plane(new Vector3f(0.f, -1.f, 0.f), 1.f);
		Plane p5 = new Plane(new Vector3f(0.f, 0.f, 1.f), 1.f);
		
		IntersectableList iList = new IntersectableList();
		// Some planes are left out
		//iList.add(p1);
		iList.add(p2);
		iList.add(p3);
		//iList.add(p4);
		//iList.add(p5);
		
		this.root = iList;
		
		// Light sources
		LightGeometry pointLight = new PointLight(new Vector3f(0.f, 0.f, 3.f), new Spectrum(10.f, 10.f, 10.f));
		lightList = new LightList();
		lightList.add(pointLight);
	}
}
