package rt.util;

import javax.vecmath.*;

public class StaticVecmath {

	public static float dist2(Tuple3f v1, Tuple3f v2)
	{
		Vector3f tmp = new Vector3f(v1);
		tmp.sub(v2);
		return tmp.lengthSquared();
	}
	
	public static Vector3f sub(Tuple3f v1, Tuple3f v2)
	{
		Vector3f r = new Vector3f(v1);
		r.sub(v2);
		return r;
	}
	
	public static Vector3f scale(Tuple3f v, float scalar){
		return new Vector3f(v.x*scalar, v.y*scalar, v.z*scalar);
	}
	
	public static Vector3f negate(Tuple3f v)
	{
		Vector3f r = new Vector3f(v);
		r.negate();
		return r;
	}
	
	public static Matrix4f invert(Matrix4f m)
	{
		Matrix4f r = new Matrix4f(m);
		r.invert();
		return r;
	}
	
	public static Vector3f unitDirection(Tuple3f start, Tuple3f end)
	{
		Vector3f d = new Vector3f(end);
		d.sub(start);
		d.normalize();
		return d;
	}
	
	public static enum Axis{
		x(new Vector3f(1, 0, 0)), 
		y(new Vector3f(0, 1, 0)), 
		z(new Vector3f(0, 0, 1));
		
		private final Vector3f normal;

		Axis(Vector3f normal){
			this.normal = normal;
		}
		
		public Vector3f getNormal() {
			return normal;
		}
		
		public Axis getNext() {
			int ordinalNext = (this.ordinal() + 1) % Axis.values().length;
			return Axis.values()[ordinalNext];
		}
	}
	
	/**
	 * A array-like accessor for tuples.
	 * @param tuple
	 * @param dimension
	 * @return
	 */
	public static float getDimension(Tuple3f tuple, Axis dimension) {
		return dimension.normal.x * tuple.x + 
				dimension.normal.y * tuple.y + 
				dimension.normal.z * tuple.z;
	}
	
	/**
	 * An array-like setter for tuples.
	 * @param tuple
	 * @param dimension
	 * @param f
	 */
	public static void setDimension(Tuple3f tuple, Axis dimension, float f) {
		switch (dimension) {
		case x:
			tuple.x = f;
			break;
		case y:
			tuple.y = f;
			break;
		case z:
			tuple.z = f;
			break;
		}
	}

	/**
	 * Sets min to the minimum of min and other (on a per element basis
	 * @param min
	 * @param other
	 */
	public static void elementwiseMin(Tuple3f min, Tuple3f other) {
		min.x = Math.min(min.x, other.x);
		min.y = Math.min(min.y, other.y);
		min.z = Math.min(min.z, other.z);
	}

	/**
	 * Sets max to the maximum of max and other (on a per element basis
	 * @param max
	 * @param other
	 */
	public static void elementwiseMax(Tuple3f max, Tuple3f other) {
		max.x = Math.max(max.x, other.x);
		max.y = Math.max(max.y, other.y);
		max.z = Math.max(max.z, other.z);
	}
}
