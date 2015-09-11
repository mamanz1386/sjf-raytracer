package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Main;
import rt.Material;
import rt.Spectrum;
import rt.util.StaticVecmath;

public class Refractive implements Material{

	public float refractionIndex;
	
	public Refractive(float refractionIndex) {
		this.refractionIndex=refractionIndex;
	}
	
	@Override
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		return new Spectrum();
	}

	@Override
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		
		return null;
	}

	@Override
	public boolean hasSpecularReflection() {
		return true;
	}

	@Override
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord) {
		Vector3f d=StaticVecmath.negate(hitRecord.w);
		Vector3f reflVec=StaticVecmath.sub(d,StaticVecmath.scale(hitRecord.normal,2*(d.dot(hitRecord.normal))));
		reflVec.normalize();
		return new ShadingSample(new Spectrum(1, 1, 1), new Spectrum(), reflVec, false, 0);
	}

	@Override
	public boolean hasSpecularRefraction() {
		return true;
	}

	@Override
	public ShadingSample evaluateSpecularRefraction(HitRecord hR) {
		boolean entering=false;
		float n1;
		float n2;
		if(hR.w.dot(hR.normal)>=0){
			n1=Main.scene.getStartN();
			n2=hR.material.getRefractionIndex();
			entering=true;
		}else{
			n2=Main.scene.getStartN();
			n1=hR.material.getRefractionIndex();
		}
		Vector3f lot=new Vector3f(hR.normal);
		if(!entering)lot.negate();
		float a1=(float)Math.acos(hR.w.dot(lot));
		float a2=(float)Math.asin((n1*Math.sin(a1)/n2));
		
		//System.out.println(a1+":"+a2+":"+n1+":"+n2+":"+entering);
		
		Vector3f r=StaticVecmath.add(StaticVecmath.scale(StaticVecmath.negate(hR.w), n1/n2), StaticVecmath.scale(lot, (float) ((n1/n2)*Math.cos(a1)-Math.cos(a2))));
		
		return new ShadingSample(new Spectrum(1, 1, 1), new Spectrum(), r, false, 0);
	}
	
	@Override
	public float getRefractionIndex() {
		return refractionIndex;
	}

	@Override
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample) {
		
		return null;
	}

	@Override
	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return new ShadingSample();
	}

	@Override
	public boolean castsShadows() {
		return false;
	}

}
