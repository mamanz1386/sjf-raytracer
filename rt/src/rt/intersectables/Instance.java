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
		// TODO: Create instance ray, intersect enclosed intersectable with it.
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
		//t.transform(hitRecord.normal);
		}
		
		// If a hitRecord is created, transform it back and return it.
		return hitRecord;
	}


	@Override
	public AxisAlignedBox getBoundingBox() {
		// TODO Auto-generated method stub
		t.transform(intersectable.getBoundingBox().min);
		t.transform(intersectable.getBoundingBox().max);
		
		return intersectable.getBoundingBox();
	}

	@Override
	public float surfaceArea() {
		// TODO Auto-generated method stub
		intersectable.surfaceArea();
		float scale = t.getScale();
		
		return intersectable.surfaceArea()*scale;
	}
}
