package rt.intersectables;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Material;
import rt.Ray;
import rt.accelerators.AxisAlignedBox;
import rt.materials.Diffuse;

public class Instance implements Intersectable {


	private Intersectable intersectable;
	private Matrix4f t;
	private Material material;

	public Instance(Intersectable i, Matrix4f t) {
		this.intersectable = i;
		this.t = t;
		this.material = new Diffuse(); //default material
	}

	@Override
	public HitRecord intersect(Ray r) {
		// TODO: Create instance ray, intersect enclosed intersectable with it.
		// If a hitRecord is created, transform it back and return it.
		return new HitRecord();
	}


	@Override
	public AxisAlignedBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float surfaceArea() {
		// TODO Auto-generated method stub
		return 0;
	}
}
