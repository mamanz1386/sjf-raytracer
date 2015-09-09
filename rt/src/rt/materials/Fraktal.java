package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
import rt.Material.ShadingSample;

public class Fraktal implements Material{

Spectrum kd;
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public Fraktal(Spectrum kd)
	{
		this.kd = new Spectrum(kd);
		// Normalize
		this.kd.mult(1/(float)Math.PI);
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public Fraktal()
	{
		this(new Spectrum(1.f, 1.f, 1.f));
	}

	/**
	 * Returns diffuse BRDF value, that is, a constant.
	 * 
	 *  @param wOut outgoing direction, by convention towards camera
	 *  @param wIn incident direction, by convention towards light
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		
	    float zx = (float) ((0.1*hitRecord.position.x-0.3));
	    float zy = (float) ((0.1*hitRecord.position.y-0.3));
	    
	   float cx = 0.3425f;
	   float cy = 0.41f;

	    
	    int i;
	    int iter = 100;
	    for(i=0; i<iter; i++) {
	        float x = (zx * zx - zy * zy) + cx;
	        float y = (zy * zx + zx * zy) + cy;

	        if((x * x + y * y) > 4.0) break;
	        zx = x;
	        zy = y;
	    }
	    
	    float c =(float)i/(float)iter;
	    
	    float g = (float) (1-2*Math.abs(c-0.5));
	    float r = (float) (1-2*c);
	    float b = (float) (2*c-1);
	    
	    //System.out.println((float)i);
	    return new Spectrum(r,g,b);
	}

	public boolean hasSpecularReflection()
	{
		return false;
	}
	
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord)
	{
		return null;
	}
	public boolean hasSpecularRefraction()
	{
		return false;
	}

	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord)
	{
		return null;
	}
	
	// To be implemented for path tracer!
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample)
	{
		return null;	
	}
		
	public boolean castsShadows()
	{
		return true;
	}
	
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		return new Spectrum(0.f, 0.f, 0.f);
	}

	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return new ShadingSample();
	}
}
