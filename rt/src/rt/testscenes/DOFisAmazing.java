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
import rt.materials.Gitterstruktur;
import rt.samplers.RandomSamplerFactory;
import rt.tonemappers.ClampTonemapper;

public class DOFisAmazing extends Scene{
	public DOFisAmazing() {
		outputFilename = new String("../output/testscenes/DOFisAmazing-1");
		width = 1280;
		height = 720;
		
		SPP = 512;
		
		Vector3f eye = new Vector3f(0f, 3f, 1.f);
		Vector3f lookAt = new Vector3f(0f, 2.5f, 0.f);
		Vector3f up = new Vector3f(0f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new DOFCamera(eye, lookAt, up, fov, aspect, width, height,0.1f,5);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		integratorFactory = new WhittedIntegratorFactory();
		samplerFactory = new RandomSamplerFactory();
		
		
		Plane groundPlane=new Plane(new Vector3f(0,0,1),0);
		groundPlane.material=new Gitterstruktur(new Spectrum(0,0,0), new Spectrum(1, 1, 1), 0.05F, 0.5F);
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
			dragon = ObjReader.read("../obj/skyscraper.obj", 1.f);
			dragon.material = new BlinnPhong(new Spectrum(1F,0,0.2F),new Spectrum(1, 1, 1),3);
			dragonAccelerator = new BSPAccelerator(dragon, 5);
		} catch(IOException e){
			System.out.printf("Could not read .obj file\n");
			return;
		}
		for(int i=0; i<30; i++){
			Matrix4f trans=new Matrix4f();
			trans.setIdentity();
			if(i%2==0)trans.setTranslation(new Vector3f(i,1,-i*2));
			else trans.setTranslation(new Vector3f(-i,1,-i*2));
			Instance dragonInstance=new Instance(dragonAccelerator, trans);
			iList.add(dragonInstance); 	
		}
			
				
				
			
		/*try{
				dragon = ObjReader.read("../obj/dragon.obj", 1.f);
				dragon.material = new BlinnPhong(new Spectrum(1F,0,0.2F),new Spectrum(1, 1, 1),3);
				dragonAccelerator = new BSPAccelerator(dragon);
				Matrix4f trans=new Matrix4f();
				trans.setIdentity();
				trans.setTranslation(new Vector3f(0,1,-5));
				Instance dragonInstance=new Instance(dragonAccelerator, trans);
				iList.add(dragonInstance); 	
			} catch(IOException e){
				System.out.printf("Could not read .obj file\n");
				return;
			}*/
		
		/*try{
			glas = ObjReader.read("../obj/sphere.obj", 1.f);
			glas.material = new BlinnPhong(new Spectrum(0.8F,0,0.2F),new Spectrum(1, 1, 1),3);
			glasAccelerator = new BSPAccelerator(glas);
			//iList.add(dragonAccelerator); 	
		} catch(IOException e){
			System.out.printf("Could not read .obj file\n");
			return;
		}*/
		iList.add(ground);
		
		this.root = iList;
		
		// Light sources
		LightGeometry l1 = new PointLight(new Vector3f(0.f, 10.f, 0.f), new Spectrum(300.f, 300.f, 300.f));
		lightList = new LightList();
		lightList.add(l1);
	}
}