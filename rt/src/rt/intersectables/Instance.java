package rt.intersectables;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Material;
import rt.Ray;
import rt.accelerators.AxisAlignedBox;
import rt.materials.Diffuse;

public class Instance implements Intersectable {


	private Intersectable intersectable;
	private Matrix4f t;
	private Matrix4f inv = new Matrix4f();
	
	private Material material;
	private Ray r;

	public Instance(Intersectable i, Matrix4f t) {
		this.intersectable = i;
		
	//	Matrix4f inv = new Matrix4f();
	//	inv.invert(t);	
		this.t = t;
		inv.invert(t);
		this.material = new Diffuse(); //default material
	}

	@Override
	public HitRecord intersect(Ray r) {
		this.r = r;
		Point3f origin = new Point3f(r.origin);
		Vector3f direction = r.direction;
		
		//direction.normalize();
		
		inv.transform(origin);
		inv.transform(direction);
		
		Ray rObj = new Ray(origin, direction);
		
		HitRecord hitRecord = intersectable.intersect(rObj);
		
		if(hitRecord != null){
			
		t.transform(hitRecord.position);
		t.transform(hitRecord.w);
		//t.transform(hitRecord.t1);
		//t.transform(hitRecord.t2);
		t.transform(hitRecord.normal);
		}
		
		// If a hitRecord is created, transform it back and return it.
		return hitRecord;
	}

	public AxisAlignedBox getBoundingBox() {		
		AxisAlignedBox bb = intersectable.getBoundingBox();
		Point3f instanceMin = new Point3f(bb.min);
		Point3f instanceMax = new Point3f(bb.max);
		
		t.transform(instanceMin);
		t.transform(instanceMax);
		return new AxisAlignedBox(instanceMin, instanceMax);
	}

	@Override
	public float surfaceArea() {
		intersectable.surfaceArea();
		float scale = t.getScale();
		return intersectable.surfaceArea()*scale;
	}
}
