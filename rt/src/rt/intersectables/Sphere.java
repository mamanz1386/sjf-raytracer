package rt.intersectables;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Material;
import rt.Ray;
import rt.Spectrum;
import rt.accelerators.AxisAlignedBox;
import rt.materials.Diffuse;
import rt.util.StaticVecmath;

public class Sphere implements Intersectable {

	private Point3f center;
	private float radius;
	private float surfaceArea;
	private AxisAlignedBox boundingBox;
	public Material material;
	
	public Sphere(Point3f center, float radius)
	{
		material = new Diffuse(new Spectrum(1.f, 1.f, 1.f));
		
		this.center = center;
		this.radius = radius;

		boundingBox = new AxisAlignedBox(center.x-radius,center.x+radius,center.y-radius,center.y+radius,center.z-radius,center.z+radius);
		surfaceArea = (float) (4*Math.PI*radius*radius);
	}
	
	public Sphere()
	{
		this(new Point3f(0.f, 0.f, 0.f), 1.f);
	}
	
	public float surfaceArea()
	{
		return surfaceArea;
	}
	
	public HitRecord intersect(Ray r) {
		Vector3f ce=StaticVecmath.sub(r.origin, center);
		float a=StaticVecmath.dot(r.direction, r.direction),b=StaticVecmath.dot(StaticVecmath.scale(r.direction, 2),ce),c=StaticVecmath.dot(ce, ce)-radius*radius;
		float determinant=b*b-4*a*c;
		float determinantR=(float)Math.sqrt(determinant);
		float t=0;
		if(determinant<0)return null;
		if(determinant==0)t=-b/(2*a);
		if(determinant>0){
			float t1=(-b+determinantR)/(2*a);
			float t2=(-b-determinantR)/(2*a);
			if(t1<0&&t2>=0)t=t2;
			else if(t2<0&&t1>=0)t=t1;
			else t=Math.min(t1, t2);
		}
		
		
		Point3f isec=r.pointAt(t);
		Vector3f norm=StaticVecmath.sub(isec, center);
		norm.normalize();
		Vector3f back=r.direction;
		back.negate();
		back.normalize();
		return new HitRecord(t, isec, norm, back, this, material, 0F, 0F);
	}
	
	public AxisAlignedBox getBoundingBox()
	{
		return boundingBox;
	}
}
