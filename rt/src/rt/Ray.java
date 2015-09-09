package rt;

import javax.vecmath.*;

/**
 * A ray represented by an origin and a direction.
 */
public class Ray {

	public Vector3f origin;
	public Vector3f direction;
	public float originN=1;
	
	public Ray(Tuple3f origin2, Vector3f direction)
	{
		this.origin = new Vector3f(origin2); 
		this.direction = new Vector3f(direction);
	}

	public Point3f pointAt(float t) 
	{
		Point3f p = new Point3f(direction);
		p.scaleAdd(t, origin);
		return p;
	}
}
