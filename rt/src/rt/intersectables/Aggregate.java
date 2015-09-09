package rt.intersectables;

import java.util.Iterator;

import rt.HitRecord;
import rt.Intersectable;
import rt.Ray;

/**
 * A group of {@link Intersectable} objects.
 */
public abstract class Aggregate implements Intersectable {

	/**
	 * Tests all intersectables in the aggregate and returns first hit.
	 */
	public HitRecord intersect(Ray r) {

		HitRecord hitRecord = null;
		float t = Float.MAX_VALUE;
		
		// Intersect all objects in group, return closest hit
		Iterator<Intersectable> it = iterator();
		while(it.hasNext())
		{
			Intersectable o = it.next();
			HitRecord tmp = o.intersect(r);
			if(tmp!=null && tmp.t<t && tmp.t>=0)// && (tmp.intersectable instanceof Plane))
			{
				t = tmp.t;
				hitRecord = tmp;
			}
		}
		return hitRecord;
	}
	
	/**
	 * Adds up surface areas of all intersectables in the aggregate.
	 */
	public float surfaceArea()
	{
		Iterator<Intersectable> it = iterator();
		float a=0;
		while(it.hasNext())
		{
			a += it.next().surfaceArea();
		}
		return a;
	}
	
	public abstract Iterator<Intersectable> iterator();

}
