package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
import rt.util.StaticVecmath;

public class BlinnPhong implements Material{
Spectrum kd,ks;//ka;
float shinyness;
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public BlinnPhong(Spectrum kd, Spectrum ks, float shinyness) //Spectrum ka)
	{
		/*this.s = new Spectrum(s);
		// Normalize
		this.s.mult(1/(float)Math.PI);*/
		this.kd=kd;
		this.ks=ks;
		//this.ka=ka;
		
		//this.kd.mult(1/(float)Math.PI);
		//this.ks.mult(1/(float)Math.PI);
		//this.ka.mult(1/(float)Math.PI);
		this.shinyness=shinyness;
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public BlinnPhong()
	{
		this(new Spectrum(1F, 1F, 1F),new Spectrum(0.01F, 0.01F, 0.01F),9F);
	}

	/**
	 *  @param wOut outgoing direction, by convention towards camera
	 *  @param wIn incident direction, by convention towards light
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		Spectrum sR=new Spectrum(kd);
		//wIn.normalize();
		Vector3f h=StaticVecmath.add(wOut, wIn);
		h.normalize();
		Spectrum ksC=new Spectrum(ks);
		//System.out.println(h.dot(hitRecord.normal));
		ksC.mult((float) Math.pow(h.dot(hitRecord.normal),shinyness));
		//System.out.println(ksC.r+":"+sR.r);
		sR.add(ksC);
		return sR;
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
