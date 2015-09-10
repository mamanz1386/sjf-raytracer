package rt.testscenes;

import java.io.IOException;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import rt.LightGeometry;
import rt.LightList;
import rt.ObjReader;
import rt.Scene;
import rt.Spectrum;
import rt.accelerators.BSPAccelerator;
import rt.cameras.DOFCamera;
import rt.films.BoxFilterFilm;
import rt.integrators.WhittedIntegratorFactory;
import rt.intersectables.Instance;
import rt.intersectables.IntersectableList;
import rt.intersectables.Mesh;
import rt.intersectables.Plane;
import rt.lightsources.PointLight;
import rt.materials.BlinnPhong;
import rt.materials.Diffuse;
import rt.samplers.RandomSamplerFactory;
import rt.tonemappers.ClampTonemapper;

public class Presentation extends Scene{
	public Presentation() {
		outputFilename = new String("../output/testscenes/Presentation");
		width = 1280;
		height = 720;
		
		SPP = 10;
		
		Vector3f eye = new Vector3f(0.5f, 0.5f, 3.f);
		Vector3f lookAt = new Vector3f(0f, 0.5f, 0.f);
		Vector3f up = new Vector3f(0f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new DOFCamera(eye, lookAt, up, fov, aspect, width, height,0F,0);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		integratorFactory = new WhittedIntegratorFactory();
		samplerFactory = new RandomSamplerFactory();
		
		
		Plane groundPlane=new Plane(new Vector3f(0,0,1),0);
		groundPlane.material=new Diffuse(new Spectrum(1, 0, 0));//new Gitterstruktur(new Spectrum(), new Spectrum(1, 1, 1), 0.125F, 0.25F);
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		t.rotX((float) (-Math.PI/2));
		Instance ground=new Instance(groundPlane, t);
		
		Mesh dragon;
		BSPAccelerator dragonAccelerator;
		
		Mesh glas;
		BSPAccelerator glasAccelerator;
		
		IntersectableList iList = new IntersectableList();
		try{
			dragon = ObjReader.read("../obj/sphere.obj", 1.f);
			dragon.material = new BlinnPhong(new Spectrum(0.8F,0,0.2F),new Spectrum(1, 1, 1),3);
			dragonAccelerator = new BSPAccelerator(dragon);
			//iList.add(dragonAccelerator); 	
		} catch(IOException e){
			System.out.printf("Could not read .obj file\n");
			return;
		}
		
		try{
			dragon = ObjReader.read("../obj/sphere.obj", 1.f);
			dragon.material = new BlinnPhong(new Spectrum(0.8F,0,0.2F),new Spectrum(1, 1, 1),3);
			dragonAccelerator = new BSPAccelerator(dragon);
			//iList.add(dragonAccelerator); 	
		} catch(IOException e){
			System.out.printf("Could not read .obj file\n");
			return;
		}
		iList.add(groundPlane);
		
		this.root = iList;
		
		// Light sources
		LightGeometry l1 = new PointLight(new Vector3f(0.f, 5.f, 0.f), new Spectrum(50.f, 50.f, 50.f));
		lightList = new LightList();
		lightList.add(l1);
	}
}
