package rt.materials;

import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
import rt.Material.ShadingSample;

public class Gitterstruktur implements Material{

	Spectrum gridColor;
	Spectrum backColor;
	Spectrum intersectionColor;
	float thickness;
	float density;
	int i;
		
		/**
		 * Note that the parameter value {@param kd} is the diffuse reflectance,
		 * which should be in the range [0,1], a value of 1 meaning all light
		 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
		 * corresponding to {@param kd} is actually {@param kd}/pi.
		 * 
		 * @param kd the diffuse reflectance
		 */
		public Gitterstruktur(Spectrum gridColor, Spectrum backColor, float thickness, float density)
		{
			this.gridColor=gridColor;
			this.backColor=backColor;
			this.intersectionColor=gridColor;
			this.thickness=thickness;
			this.density=density;
		}
		
		public Gitterstruktur(Spectrum gridColor, Spectrum backColor, Spectrum intersectionColor, float thickness, float density)
		{
			this.gridColor=gridColor;
			this.backColor=backColor;
			this.intersectionColor=intersectionColor;
			this.thickness=thickness;
			this.density=density;
		}
		
		/**
		 * Default diffuse material with reflectance (1,1,1).
		 * set int to plane direction: z:0, y:1, x:2
		 */
		/*public Gitterstruktur(int i)
		{
			this.i = i;
		}*/

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
			
			float xStep=Math.abs(hitRecord.position.x) % density;
			float yStep=Math.abs(hitRecord.position.z) % density;
			//return new Spectrum(xStep, yStep, 0);
			
			if((xStep>density-thickness||xStep<thickness)&&(yStep>density-thickness||yStep<thickness))return intersectionColor;
			if(xStep>density-thickness||xStep<thickness)return gridColor;
			if(yStep>density-thickness||yStep<thickness)return gridColor;
			return backColor;
			
			/*float restx= 10*hitRecord.position.x % 5;
			if(restx<0)
				restx+=5;
			
			float resty= 10*hitRecord.position.y % 5;
			if(resty<0)
				resty+=5;
			
			float restz= 10*hitRecord.position.z % 5;
			if(restz<0)
				restz+=5;
			
			int resta;
			int restb;
			
			switch (i){
				case 0: resta=(int)restx; restb=(int)resty; break;
				case 1: resta=(int)restx; restb=(int)restz; break;
				case 2: resta=(int)resty; restb=(int)restz; break;
				default: return backColor;
			
			}
			
			if(resta%density==0 || restb%density==0)
				return gridColor;
			return backColor;
			
			//Schachbrett
			/*if((restx+resty)%2==0)
				return new Spectrum(1,1,1);
			return new Spectrum(0,0,0);*/
			
			
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
			
			//return new Spectrum(kd);*/
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
