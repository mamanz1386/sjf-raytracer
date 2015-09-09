package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
import rt.Material.ShadingSample;

public class PerlinNoisWood implements Material{

Spectrum kd;
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public PerlinNoisWood(Spectrum kd)
	{
		this.kd = new Spectrum(kd);
		// Normalize
		this.kd.mult(1/(float)Math.PI);
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public PerlinNoisWood()
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
		Spectrum color = new Spectrum();
		float noiseHeight = 400;
		float noiseWidth = 600;
		   
	    float xyPeriod = 30.0f; //number of rings
	    float turbPower = 0.5f; //makes twists
	    float turbSize = 32.0f; //initial size of the turbulence
	     
	    float x= hitRecord.position.x;
	    float y= hitRecord.position.y;
	    
	    float xValue = (x - noiseHeight / 2) / (float)(noiseHeight);
	    float yValue = (y - noiseWidth / 2) / (float)(noiseWidth);
	    float distValue = (float) (Math.sqrt(xValue * xValue + yValue * yValue) + turbPower * turbulence(x, y, turbSize) / 256.0);
	    float sineValue = (float) (128.0 * Math.abs(Math.sin(2 * xyPeriod * distValue * 3.14159)));
	    color.r = Math.max(80 + sineValue, 100)/100;
	    color.g = Math.max(30 + sineValue, 100)/100;
	    color.b = 30/100;

	    return new Spectrum(color.r, color.g, color.b);
	}
	
	public float turbulence(float x, float y, float turbSize){
		return (float) (256 * Math.sin(Math.sqrt(x*x + y*y)));
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

	@Override
	public float getRefractionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
}
