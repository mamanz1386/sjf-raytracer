package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;

public class NormalMaterial implements Material{

	Spectrum c;
	float critAng;
	float pow;
	
	public NormalMaterial(Spectrum c, float critAng, float pow){
		this.c=c;
		this.critAng=critAng;
		this.pow=pow;
	}
	
	@Override
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		Spectrum rS=new Spectrum(c);
		Vector3f lot=new Vector3f(hitRecord.normal);
		if(lot.dot(hitRecord.w)<0)lot.negate();
		if(Math.acos(lot.dot(hitRecord.w))>critAng)rS.mult((float) Math.pow(10, pow));
		return new Spectrum(rS);
	}

	@Override
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		return null;
	}

	@Override
	public boolean hasSpecularReflection() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord) {
		return null;
	}

	@Override
	public boolean hasSpecularRefraction() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord) {
		return null;
	}

	@Override
	public float getRefractionIndex() {
		return 0;
	}

	@Override
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample) {
		return null;
	}

	@Override
	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return null;
	}

	@Override
	public boolean castsShadows() {
		return false;
	}

}
