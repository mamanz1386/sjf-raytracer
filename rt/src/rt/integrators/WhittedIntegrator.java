package rt.integrators;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
import rt.LightGeometry;
import rt.LightList;
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
	final float EVALEPS=0.1f;
	
	public WhittedIntegrator(Scene scene){
		this.lightList = scene.getLightList();
		this.root = scene.getIntersectable();
		this.scene=scene;
	}

	@Override
	public Spectrum integrate(Ray r) {
		return followRay(r, 10);
	}
	
	public Spectrum followRay(Ray r, int remaining){
		if(remaining<=0)return new Spectrum(0,0,0);
		HitRecord hit = root.intersect(r);
		if(hit!=null){
	 		if(hit.material.hasSpecularReflection()&&hit.material.hasSpecularRefraction()){
				float n1;
				float n2;
				boolean entering=false;
				if(hit.w.dot(hit.normal)>=0){
					n1=scene.getStartN();
					n2=hit.material.getRefractionIndex();
					entering=true;
				}else{
					n2=scene.getStartN();
					n1=hit.material.getRefractionIndex();
				}
				Vector3f lot=new Vector3f(hit.normal);
				if(!entering)lot.negate();
				float a1=(float) Math.acos(hit.w.dot(lot));
				float f,F;
				//System.out.println(Math.sin(a1)*(n1/n2));
				if(Math.sin(a1)*(n1/n2)>1){
					F=1;
				}else{
					f=(float) (Math.pow((1-n1/n2),2)/Math.pow((1+n1/n2),2));
					F=(float) (f+(1-f)*Math.pow((1-hit.w.dot(lot)),5F));
				}
				
				//Reflection
				Spectrum reflSpectrum=new Spectrum();
				if(F>EVALEPS){
					ShadingSample reflectionSample=hit.material.evaluateSpecularReflection(hit);
					Point3f hitEpsRefl=new Point3f(StaticVecmath.add(StaticVecmath.scale(reflectionSample.w,0.001F),hit.position));
					reflSpectrum=followRay(new Ray(new Vector3f(hitEpsRefl),hit.material.evaluateSpecularReflection(hit).w),--remaining);
					reflSpectrum.mult(F);
				}
				
				
				//Refraction
				Spectrum refrSpectrum=new Spectrum();
				if(F<1-EVALEPS){
					ShadingSample refractionSample=hit.material.evaluateSpecularRefraction(hit);
					Point3f hitEpsRefr=new Point3f(StaticVecmath.add(StaticVecmath.scale(refractionSample.w,0.001F),hit.position));
					refrSpectrum=followRay(new Ray(hitEpsRefr,refractionSample.w),--remaining);
					refrSpectrum.mult(1-F);
				}
				reflSpectrum.add(refrSpectrum);
				return reflSpectrum;
				
			}else if(hit.material.hasSpecularReflection()){
				ShadingSample reflectionSample=hit.material.evaluateSpecularReflection(hit);
				Point3f hitEps=new Point3f(StaticVecmath.add(StaticVecmath.scale(reflectionSample.w,0.001F),hit.position));
				return followRay(new Ray(hitEps,reflectionSample.w),--remaining);
				
			}else if(hit.material.hasSpecularRefraction()){
				ShadingSample refractionSample=hit.material.evaluateSpecularRefraction(hit);
				Point3f hitEps=new Point3f(StaticVecmath.add(StaticVecmath.scale(refractionSample.w,0.001F),hit.position));
				return followRay(new Ray(hitEps,refractionSample.w),--remaining);
				
			}else{
				return getSpectrumForHitRecord(hit);
			}
		}else{
			return new Spectrum(scene.getSkyColor());
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
			
			if(shadow==null||(StaticVecmath.sub(shadow.position,hitEpsilon).lengthSquared()>lightLenght-0.0001F||shadow.t<0)||!shadow.material.castsShadows()||false){
				Spectrum lightColor=lightHit.material.evaluateEmission(lightHit, lightVec);
				n.normalize();

				Spectrum mColor = h.material.evaluateBRDF(h, w, lightVec);

				double theta = Math.max(0f, n.dot(lightVec));
				lightColor.mult(mColor);
				lightColor.mult((float)theta);

				lightColor.mult((float) (1f/lightLenght));
				rLC.add(lightColor);
			}else {}
		}
		
		return rLC;
	}
	
	@Override
	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}
}
