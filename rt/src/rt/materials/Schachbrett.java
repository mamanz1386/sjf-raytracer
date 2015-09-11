package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Main;
import rt.Material;
import rt.Spectrum;
import rt.Material.ShadingSample;
import rt.util.StaticVecmath;

public class Schachbrett implements Material{

Spectrum kd;
Material refractive = new Refractive(1.3f);
Material spiegel = new Refractive(0f);
	
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */

	public Schachbrett(Spectrum kd)
	{
		this.kd = new Spectrum(kd);
		// Normalize
		this.kd.mult(1/(float)Math.PI);
	}
	
	/**
	 * Default diffuse material with reflectance (1,1,1).
	 */
	public float refractionIndex;
	
	public Schachbrett(float refractionIndex)
	{
		this(new Spectrum(1.f, 1.f, 1.f));
		this.refractionIndex=refractionIndex;
	}

	/**
	 * Returns diffuse BRDF value, that is, a constant.
	 * 
	 *  @param wOut outgoing direction, by convention towards camera
	 *  @param wIn incident direction, by convention towards light
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		
		/*boolean isPositive = hitRecord.position.x>0;
		boolean condition = false;
		if(isPositive)
			condition = 10*hitRecord.position.x%2 < 1;
		else
			condition = Math.abs(10*hitRecord.position.x%2) >= 1;*/
		
		float restx= 10*hitRecord.position.x % 2;
		if(restx<0)
			restx+=2;
		
		float resty= 10*hitRecord.position.y % 2;
		if(resty<0)
			resty+=2;
		
		float restz= 10*hitRecord.position.z % 2;
		if(restz<0)
			restz+=2;
		
		restx= (int)restx;
		resty= (int)resty;
		
		
		//Schachbrett
		if((restx+resty)%2==0)
			hitRecord.material=refractive;
		hitRecord.material=spiegel;
		return (new Spectrum(1,1,1));
		
		// 6 gestreift
		/*switch((int)restx){
		case 0: return new Spectrum(0.6f, 0.2f, 0.8f);
		case 1: return new Spectrum(0,0,1);
		case 2: return new Spectrum(0,1,0);
		case 3: return new Spectrum(1,1,0);
		case 4: return new Spectrum(1,0.6f,0);
		case 5: return new Spectrum(1,0,0);
		default: return new Spectrum(1,1,1);
		}*/
		
		//return new Spectrum(kd);
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

	public boolean hasSpecularReflection()
	{
		return false;
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
