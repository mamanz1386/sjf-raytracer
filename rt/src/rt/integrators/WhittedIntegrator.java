package rt.integrators;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
import rt.LightGeometry;
import rt.LightList;
import rt.Main;
import rt.Material.ShadingSample;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;
import rt.util.StaticVecmath;

public class WhittedIntegrator implements Integrator{
	
	LightList lightList;
	Intersectable root;
	Scene scene;
	
	public WhittedIntegrator(Scene scene){
		this.lightList = scene.getLightList();
		this.root = scene.getIntersectable();
		this.scene=scene;
	}

	@Override
	public Spectrum integrate(Ray r) {
		
		return followRay(r, null, 15);
		
	}
	
	public Spectrum followRay(Ray r, HitRecord last, int remaining){
		if(remaining<=0)return new Spectrum(0,0,1);
		HitRecord hit = root.intersect(r);
		//if(remaining<15)System.out.println(hit.intersectable.getClass()+":"+hit.t+":"+remaining);
		if(hit!=null){
	 		if(hit.material.hasSpecularReflection()&&hit.material.hasSpecularRefraction()){
	 			//System.out.println(remaining);
				float n1;
				float n2;
				if(StaticVecmath.negate(hit.w).dot(hit.normal)>=0){
					n1=scene.getStartN();
					n2=hit.material.getRefractionIndex();
				}else{
					n2=scene.getStartN();
					n1=hit.material.getRefractionIndex();
				}
				float f=(float) (Math.pow((1-n1/n2),2)/Math.pow((1+n1/n2),2));
				float F=f+(1-f)*(1-hit.w.dot(hit.normal));
				
				//Reflection
				ShadingSample reflectionSample=hit.material.evaluateSpecularReflection(hit);
				Point3f hitEpsRefl=new Point3f(StaticVecmath.add(StaticVecmath.scale(reflectionSample.w,0.001F),hit.position));
				Spectrum reflSpectrum=followRay(new Ray(new Vector3f(hitEpsRefl),hit.material.evaluateSpecularReflection(hit).w),hit,--remaining);
				reflSpectrum.mult(F);
				//Refraction
				ShadingSample refractionSample=hit.material.evaluateSpecularRefraction(hit);
				Point3f hitEpsRefr=new Point3f(StaticVecmath.add(StaticVecmath.scale(refractionSample.w,0.001F),hit.position));
				Spectrum refrSpectrum=followRay(new Ray(new Vector3f(hitEpsRefr),hit.material.evaluateSpecularRefraction(hit).w),hit,--remaining);
				refrSpectrum.mult(1-F);
				reflSpectrum.add(refrSpectrum);
				return reflSpectrum;
				
			}else if(hit.material.hasSpecularReflection()){
				ShadingSample reflectionSample=hit.material.evaluateSpecularReflection(hit);
				Point3f hitEps=new Point3f(StaticVecmath.add(StaticVecmath.scale(reflectionSample.w,0.001F),hit.position));
				//HitRecord h=root.intersect(r);
				//System.out.println(hitEps+":"+reflectionSample.w+":"+h.intersectable.getClass());
				Spectrum flw=followRay(new Ray(hitEps,reflectionSample.w),hit,--remaining);
				if(flw.equals(new Spectrum(0.0F, 1.0F, 0.0F))){
					HitRecord h=root.intersect(r);
					//System.out.println(hitEps+":"+reflectionSample.w+":"+h.intersectable.getClass()+":"+h.t+":"+remaining);
				}
				return flw;
				
			}else if(hit.material.hasSpecularRefraction()){
				ShadingSample refractionSample=hit.material.evaluateSpecularRefraction(hit);
				Point3f hitEps=new Point3f(StaticVecmath.add(StaticVecmath.scale(refractionSample.w,0.001F),hit.position));
				return followRay(new Ray(new Vector3f(hitEps),refractionSample.w),hit,--remaining);
				
			}else{
				return getSpectrumForHitRecord(hit);
			}
		}else{
			return new Spectrum(0,1,0);
		}
 		
	}


	public Spectrum getSpectrumForHitRecord(HitRecord h){
		Vector3f w = h.w;
		Vector3f n = h.normal;

		Spectrum rLC=new Spectrum();
		
		for(LightGeometry l:lightList){
			
			HitRecord lightHit = l.sample(null);
			Point3f lightPoint = lightHit.position;
			Vector3f lightVec = new Vector3f(h.position.x-lightPoint.x, h.position.y-lightPoint.y, h.position.z-lightPoint.z);
			lightVec.negate();
			float lightLenght= lightVec.lengthSquared();
			
			lightVec.normalize();
			Point3f hitEpsilon=new Point3f(StaticVecmath.add(h.position,StaticVecmath.scale(lightVec,0.0001f)));
			
			
			Ray lightRay=new Ray(new Vector3f(hitEpsilon),lightVec);
			HitRecord shadow=root.intersect(lightRay);
			//if(shadow!=null)System.out.println(hitEpsilon+":"+shadow.position);
			//if(shadow!=null&&shadow.intersectable instanceof Sphere)System.out.println(!shadow.material.castsShadows());
			//if(shadow!=null)System.out.println(StaticVecmath.sub(shadow.position,hitEpsilon).lengthSquared()+":"+lightLenght);
			
			/*if(shadow!=null){
				System.out.println(shadow.intersectable.getClass());
				return new Spectrum((shadow.position.x+1)/2, (shadow.position.y+1)/2, (shadow.position.z+1)/2);
			}
			else return new Spectrum();*/
			if(shadow==null||(StaticVecmath.sub(shadow.position,hitEpsilon).lengthSquared()>lightLenght-0.0001F||shadow.t<0)||!shadow.material.castsShadows()||false){
				Spectrum lightColor=lightHit.material.evaluateEmission(lightHit, lightVec);
				n.normalize();

				Spectrum mColor = h.material.evaluateBRDF(h, w, lightVec);

				double theta = Math.max(0f, n.dot(lightVec));
				lightColor.mult(mColor);
				lightColor.mult((float)theta);

				lightColor.mult((float) (1f/lightLenght));
				rLC.add(lightColor);
			}else {
				//System.out.println(lightVec.dot(StaticVecmath.negate(shadow.w)));
				//System.out.println(shadow.intersectable.getClass()+":"+shadow.material.getClass()+":"+shadow.t);
			}
		}
		
		return rLC;
	}
	
	@Override
	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}
}
