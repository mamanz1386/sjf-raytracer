package rt.integrators;

import java.util.Vector;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
import rt.LightGeometry;
import rt.LightList;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;
import rt.util.StaticVecmath;

/**
 * Integrator for Whitted style ray tracing. This is a basic version that needs to be extended!
 */
public class PointLightIntegrator implements Integrator {

	LightList lightList;
	Intersectable root;

	public PointLightIntegrator(Scene scene)
	{
		this.lightList = scene.getLightList();
		this.root = scene.getIntersectable();
	}

	/**
	 * Basic integrator that simply iterates over the light sources and accumulates
	 * their contributions. No shadow testing, reflection, refraction, or
	 * area light sources, etc. supported.
	 */
	public Spectrum integrate(Ray r) {
		Spectrum outgoing = new Spectrum(0.f, 0.f, 0.f);

		HitRecord hitRecord = root.intersect(r);

		if (hitRecord != null) {
			Vector3f w = hitRecord.w;
			Vector3f n = hitRecord.normal;

			Spectrum rLC=new Spectrum();

			for(LightGeometry l:lightList){

				HitRecord lightHit = l.sample(null);
				Point3f lightPoint = lightHit.position;
				Vector3f lightVec = new Vector3f(hitRecord.position.x-lightPoint.x, hitRecord.position.y-lightPoint.y, hitRecord.position.z-lightPoint.z);
				lightVec.negate();
				float lightLenght= lightVec.lengthSquared();

				lightVec.normalize();
				Point3f hitEpsilon=new Point3f(StaticVecmath.add(hitRecord.position,StaticVecmath.scale(lightVec,0.0001f)));


				Ray lightRay=new Ray(new Vector3f(hitEpsilon),lightVec);
				HitRecord shadow=root.intersect(lightRay);

				//if(shadow!=null)System.out.println(StaticVecmath.sub(shadow.position,hitEpsilon).lengthSquared()+":"+lightLenght);

				if(shadow==null||(StaticVecmath.sub(shadow.position,hitEpsilon).lengthSquared()>lightLenght-0.0001F||shadow.t<0)||false){
					Spectrum lightColor= lightHit.material.evaluateEmission(lightHit, lightVec);
					n.normalize();

					Spectrum mColor = hitRecord.material.evaluateBRDF(hitRecord, w, lightVec);

					double theta = Math.max(0f, n.dot(lightVec));
					lightColor.mult(mColor);
					lightColor.mult((float)theta);

					lightColor.mult((float) (1f/lightLenght));
					rLC.add(lightColor);
				}
			}


			return rLC;


		} else {
			return new Spectrum(0.f, 0.f, 0.f);
		}

	}

	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}

}
