package rt.testscenes;

import java.io.IOException;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import rt.LightGeometry;
import rt.LightList;
import rt.ObjReader;
import rt.Scene;
import rt.Spectrum;
import rt.cameras.MovableCamera;
import rt.films.BoxFilterFilm;
import rt.integrators.PointLightIntegratorFactory;
import rt.intersectables.Instance;
import rt.intersectables.IntersectableList;
import rt.intersectables.Mesh;
import rt.intersectables.Plane;
import rt.lightsources.PointLight;
import rt.materials.Diffuse;
import rt.samplers.OneSamplerFactory;
import rt.tonemappers.ClampTonemapper;

/**
 * Test scene for instancing and rendering triangle meshes.
 */
public class InstancingTeapots extends Scene {

	public IntersectableList objects;

	/**
	 * Timing: 8.5 sec on 12 core Xeon 2.5GHz, 24 threads
	 */
	public InstancingTeapots()
	{	
		outputFilename = new String("../output/testscenes/InstancingTeapotsTest");
		
		// Specify integrator to be used
		integratorFactory = new PointLightIntegratorFactory();
		
		// Specify pixel sampler to be used
		samplerFactory = new OneSamplerFactory();
		
		// Make camera and film
		Vector3f eye = new Vector3f(0.f,0.f,2.f);
		Vector3f lookAt = new Vector3f(0.f,0.f,0.f);
		Vector3f up = new Vector3f(0.f,1.f,0.f);
		float fov = 60.f;
		int width = 256;
		int height = 256;
		this.SPP = 1;
		float aspect = (float)width/(float)height;
		camera = new MovableCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);						
		tonemapper = new ClampTonemapper();
		
		// List of objects
		objects = new IntersectableList();	
				
		// Box
		Plane plane = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.f, 0.8f, 0.8f));
		objects.add(plane);		
		
		plane = new Plane(new Vector3f(0.f, 0.f, 1.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.3f, 0.8f, 0.8f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(-1.f, 0.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(1.f, 0.8f, 0.8f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(1.f, 0.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.f, 0.8f, 0.0f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(0.f, -1.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.8f, 0.8f, 0.8f));
		objects.add(plane);
		
		// Add objects
		Mesh mesh;
		try
		{
			mesh = ObjReader.read("../obj/teapot.obj", 1.f);
		} catch(IOException e) 
		{
			System.out.printf("Could not read .obj file\n");
			return;
		}
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		
		// Instance one
		t.setScale(0.5f);
		t.setTranslation(new Vector3f(0.f, -0.35f, 0.f));
		Instance instance = new Instance(mesh, t);
		objects.add(instance);	
		
		// Instance two
		t.setScale(0.5f);
		t.setTranslation(new Vector3f(0.f, 0.25f, 0.f));
		Matrix4f rot = new Matrix4f();
		rot.setIdentity();
		rot.rotX((float)Math.toRadians(30.f));
		t.mul(rot);
		instance = new Instance(mesh, t);
		objects.add(instance);
				
		root = objects;
		
		// List of lights
		lightList = new LightList();
		
		LightGeometry light = new PointLight(new Vector3f(0.f,0.8f,0.8f), new Spectrum(3.f, 3.f, 3.f));
		lightList.add(light);
		
		light = new PointLight(new Vector3f(-0.8f,0.2f,1.f), new Spectrum(1.5f, 1.5f, 1.5f));
		lightList.add(light);		
	}
}
