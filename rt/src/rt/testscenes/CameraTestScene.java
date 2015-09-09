package rt.testscenes;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.LightGeometry;
import rt.LightList;
import rt.Scene;
import rt.Spectrum;
import rt.cameras.MovableCamera;
import rt.films.BoxFilterFilm;
import rt.integrators.PointLightIntegratorFactory;
import rt.intersectables.IntersectableList;
import rt.intersectables.Plane;
import rt.intersectables.Sphere;
import rt.lightsources.PointLight;
import rt.materials.BlinnPhong;
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
		outputFilename = new String("../output/testscenes/CameraShade");
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
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
		
		// Define some objects to be added to the scene. 
		// 5 planes can be used to define a box (with never ending walls).
		Plane p1 = new Plane(new Vector3f(1.f, 0.f, 0.f), 1.f);
		//p1.material = new BlinnPhong();
		Plane p2 = new Plane(new Vector3f(-1.f, 0.f, 0.f), 1.f);
		p2.material = new Diffuse(new Spectrum(.8f, 0.f, 0.f));
		Plane p3 = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
		Plane p4 = new Plane(new Vector3f(0.f, -1.f, 0.f), 1.f);
		p4.material = new Diffuse(new Spectrum(1, 1, 0));
		Plane p5 = new Plane(new Vector3f(0.f, 0.f, 1.f), 1.f);
		Sphere sCenter = new Sphere(new Point3f(0, 0, 0), 1f);
		sCenter.material = new BlinnPhong(new Spectrum(0.8f, 0.8f, 0.8f), new Spectrum(.4f, .4f, .4f), 50.f);
		
		IntersectableList iList = new IntersectableList();
		// Some planes are left out
		iList.add(p1);
		iList.add(p2);
		iList.add(p3);
		iList.add(p4);
		iList.add(p5);
		iList.add(sCenter);
		
		this.root = iList;
		
		// Light sources
		LightGeometry l1 = new PointLight(new Vector3f(0.f, 0.f, 5.f), new Spectrum(5.f, 5.f, 5.f));
		LightGeometry l2 = new PointLight(new Vector3f(0.9f, 0.9f, 0f), new Spectrum(1f, 1f, 1f));
		lightList = new LightList();
		lightList.add(l1);
		lightList.add(l2);
	}
}
