package rt.integrators;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
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
			// Only works with diffuse material
			Vector3f w = hitRecord.w;
			Vector3f n = hitRecord.normal;
			Vector3f scaled = new Vector3f(n);
			scaled.scale(2);
			scaled.scale(w.dot(n));
			Spectrum color = hitRecord.material.evaluateBRDF(hitRecord, StaticVecmath.negate(w),
					StaticVecmath.sub(w, scaled));
			return color;
		} else {
			return new Spectrum(0.f, 0.f, 0.f);
		}
		
	}

	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}

}
