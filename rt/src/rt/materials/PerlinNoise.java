package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
<<<<<<< HEAD
import rt.util.PerlinNoiseGenerator;

public class PerlinNoise implements Material{

Spectrum kd;
private PerlinNoiseGenerator generator;
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public PerlinNoise(Spectrum kd)
	{
		this.kd = new Spectrum(kd);
		// Normalize
		this.kd.mult(1/(float)Math.PI);
		this.generator = new PerlinNoiseGenerator();
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public PerlinNoise()
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
		
		generator.noise3((float) hitRecord.position.x , (float) hitRecord.position.y, (float) hitRecord.position.z);
		
		return generator.noise3;
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
=======
import rt.Material.ShadingSample;
import rt.util.PerlinNoiseGenerator;

public class PerlinNoise implements Material{

Spectrum kd;
private PerlinNoiseGenerator generator;
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public PerlinNoise(Spectrum kd)
	{
		this.kd = new Spectrum(kd);
		// Normalize
		this.kd.mult(1/(float)Math.PI);
		this.generator = new PerlinNoiseGenerator();
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public PerlinNoise()
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

		// float perNoise = 10*generator.noise3((float) hitRecord.position.x , (float) hitRecord.position.y, (float) hitRecord.position.z);
		// perNoise += 5*generator.noise3((float) 80*hitRecord.position.x , (float) 70*hitRecord.position.y, (float) 60*hitRecord.position.z);
		// perNoise += 2*generator.noise3((float) 70*hitRecord.position.x , (float) 90*hitRecord.position.y, (float) 30*hitRecord.position.z);
		
		// float perNoise = generator.noise3((float) hitRecord.position.x , (float) hitRecord.position.y, (float) hitRecord.position.z);
		
		float perNoise = 0;
        
		float a = 2; // The parameter "a" controls how rough the final noise will be
        float b = 2; // A harmonic will be Noise(b x) where "b" is some positive number greater than 1, most commonly it will be powers of 2
        float n = 8; // n is typically between 6 and 10
		
		for(int i = 0; i < n; i++) {
	        
	      perNoise += 8*(generator.noise3((float) Math.pow(b,i)*hitRecord.position.x , (float) Math.pow(b,i)*hitRecord.position.y, (float) Math.pow(b,i)*hitRecord.position.z))/Math.pow(a,i);
	     
		}

		// a*perNoise + b*(1-perNoise)
		Spectrum blue = new Spectrum(0,0,1);
		Spectrum white = new Spectrum(1,1,1);
		white.mult(perNoise);
		blue.mult(1-perNoise);
		white.add(blue);
		return white;
		
		//return new Spectrum(perNoise,perNoise,perNoise);
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
>>>>>>> branch 'master' of https://github.com/sjf2015/sjf-raytracer
	}
	
}
