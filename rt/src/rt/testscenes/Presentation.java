package rt.testscenes;

import java.io.IOException;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.LightGeometry;
import rt.LightList;
import rt.ObjReader;
import rt.Scene;
import rt.Spectrum;
import rt.accelerators.BSPAccelerator;
import rt.cameras.DOFCamera;
import rt.films.BoxFilterFilm;
import rt.films.BrightEdgeFilm;
import rt.integrators.WhittedIntegratorFactory;
import rt.intersectables.Instance;
import rt.intersectables.IntersectableList;
import rt.intersectables.Mesh;
import rt.intersectables.Plane;
import rt.intersectables.Sphere;
import rt.lightsources.PointLight;
import rt.materials.BlinnPhong;
import rt.materials.Diffuse;
import rt.materials.Gitterstruktur;
import rt.materials.NormalMaterial;
import rt.samplers.RandomSamplerFactory;
import rt.tonemappers.ClampTonemapper;

public class Presentation extends Scene{
	public Presentation() {
		outputFilename = new String("../output/testscenes/PresentationTest");
		width = 1280;
		height = 720;
		
		SPP = 10;
		Vector3f eye = new Vector3f(0f, 3f, -4.f);
		Vector3f lookAt = new Vector3f(0f, 1f, -8.f);
		Vector3f up = new Vector3f(0f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new DOFCamera(eye, lookAt, up, fov, aspect, width, height,0,3);
		//camera = new MovableCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BrightEdgeFilm(width, height,0.3f);
		tonemapper = new ClampTonemapper();
		
		integratorFactory = new WhittedIntegratorFactory();
		samplerFactory = new RandomSamplerFactory();
		
		
		Plane groundPlane=new Plane(new Vector3f(0,0,1),0);
		groundPlane.material=new Diffuse(new Spectrum(1,1,1)); //Gitterstruktur(new Spectrum(0,0,0), new Spectrum(1, 1, 1), 0.05F, 0.5F);
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		t.rotX((float) (-Math.PI/2));
		Instance ground=new Instance(groundPlane, t);
		
		Mesh teapot;
		BSPAccelerator teapotAccelerator;
				
		IntersectableList iList = new IntersectableList();
		try{
			teapot = ObjReader.read("../obj/teapot.obj", 1.f);
			teapot.material = new NormalMaterial(new Spectrum(0, 0.2f, 0),(float) Math.PI/2.2f,0);
			teapotAccelerator = new BSPAccelerator(teapot, 5);
			Matrix4f trans=new Matrix4f();
			trans.setIdentity();
			trans.setTranslation(new Vector3f(0,1,-8));
			Instance dragonInstance=new Instance(teapotAccelerator, trans);
			iList.add(dragonInstance); 	
		} catch(IOException e){
			System.out.printf("Could not read .obj file\n");
			return;
		}
		
		skyColor=new Spectrum(0,0,0);
		
		Sphere refract=new Sphere(new Point3f(-2f,1.5f,-7), 1);
		refract.material=new NormalMaterial(new Spectrum(1f,0,0f),(float)Math.PI/2.2f,0);
		
		iList.add(refract);
		
		iList.add(ground);
		
		this.root = iList;
		
		// Light sources
		LightGeometry l1 = new PointLight(new Vector3f(0.f, 10.f, 0.f), new Spectrum(300.f, 300.f, 300.f));
		lightList = new LightList();
		lightList.add(l1);
	}
}
