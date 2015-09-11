package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
import rt.Material.ShadingSample;
import rt.util.PerlinNoiseGenerator;

public class PerlinNoise implements Material{

float a; // The parameter "a" controls how rough the final noise will be
float b; // A harmonic will be Noise(b x) where "b" is some positive number greater than 1, most commonly it will be powers of 2
float n; // n is typically between 6 and 10

private PerlinNoiseGenerator generator;

	public PerlinNoise(float a, float b, float n)
	{
		this.a = a;
		this.b = b;
		this.n = n;

		this.generator = new PerlinNoiseGenerator();
	}
	
	/**
	 * Default values for a, b and n
	 */
	public PerlinNoise()
	{
		this(3,4,8);
	}

	/**
	 * Returns noise BRDF value, that is, a constant.
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		
		float perNoise = 0;
		
		for(int i = 0; i < n; i++) {
	        
	      perNoise += 8*(generator.noise3((float) Math.pow(b,i)*hitRecord.position.x , (float) Math.pow(b,i)*hitRecord.position.y, (float) Math.pow(b,i)*hitRecord.position.z))/Math.pow(a,i);
	     
		}

		/* a*perNoise + b*(1-perNoise)
		Spectrum blue = new Spectrum(1,1,1);
		Spectrum white = new Spectrum(0,0.6f,1);
		white.mult(perNoise);
		blue.mult(1-perNoise);
		white.add(blue);
		return white; */
		
		return new Spectrum(perNoise,perNoise,perNoise);
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
		return 0;
	}
	
}
