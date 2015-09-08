package rt.intersectables;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Ray;
import rt.accelerators.AxisAlignedBox;

/**
 * Defines a triangle by referring back to a {@link Mesh}
 * and its vertex and index arrays. 
 */
public class MeshTriangle implements Intersectable {

	private Mesh mesh;
	private int index;
	
	/**
	 * Make a triangle.
	 * 
	 * @param mesh the mesh storing the vertex and index arrays
	 * @param index the index of the triangle in the mesh
	 */
	public MeshTriangle(Mesh mesh, int index)
	{
		this.mesh = mesh;
		this.index = index;		
	}
	
	public HitRecord intersect(Ray r)
	{
		float vertices[] = mesh.vertices;
		
		// Access the triangle vertices as follows (same for the normals):		
		// 1. Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];
		
		// 2. Access x,y,z coordinates for each vertex
		float x0 = vertices[v0*3];
		float x1 = vertices[v1*3];
		float x2 = vertices[v2*3];
		float y0 = vertices[v0*3+1];
		float y1 = vertices[v1*3+1];
		float y2 = vertices[v2*3+1];
		float z0 = vertices[v0*3+2];
		float z1 = vertices[v1*3+2];
		float z2 = vertices[v2*3+2];
		
		Point3f a = new Point3f(x0,y0,z0);
		Point3f b = new Point3f(x1,y1,z1);
		Point3f c = new Point3f(x2,y2,z2);
		
		Vector3f e = r.origin;
		Vector3f d = r.direction;
		
		Matrix3f me = new Matrix3f();
		me.m00 = a.x - b.x;
		me.m10 = a.y - b.y;
		me.m20 = a.z - b.z;
		me.m01 = a.x - c.x;
		me.m11 = a.y - c.y;
		me.m21 = a.z - c.z;
		me.m02 = d.x;
		me.m12 = d.y;
		me.m22 = d.z;
		
		Vector3f m = new Vector3f(a.x - e.x, a.y - e.y, a.z - e.z);
		
		me.invert();
		
		me.transform(m);
		
		float t = m.z;
		float beta = m.x;
		float gamma = m.y;
		float alpha = 1 -beta -gamma;
		if(beta>0 && gamma>0 && beta+gamma<1){
			Point3f p = r.pointAt(t);
			
			float normals[] = mesh.normals;
			
			float nx0 = normals[v0*3];
			float nx1 = normals[v1*3];
			float nx2 = normals[v2*3];
			float ny0 = normals[v0*3+1];
			float ny1 = normals[v1*3+1];
			float ny2 = normals[v2*3+1];
			float nz0 = normals[v0*3+2];
			float nz1 = normals[v1*3+2];
			float nz2 = normals[v2*3+2];
			
			Vector3f normal = new Vector3f(alpha*nx0 + beta*nx1 + gamma*nx2, alpha*ny0 + beta*ny1 + gamma*ny2, alpha*nz0 + beta*nz1 + gamma*nz2);
			
			normal.normalize();
			return new HitRecord(t, p, normal, r.direction, this, mesh.material, 0, 0);
		}
		
		return null;
	}

	@Override
	public AxisAlignedBox getBoundingBox() {

		float vertices[] = mesh.vertices;
		
		// Access the triangle vertices as follows (same for the normals):		
		// 1. Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];
		
		// 2. Access x,y,z coordinates for each vertex
		float x0 = vertices[v0*3];
		float x1 = vertices[v1*3];
		float x2 = vertices[v2*3];
		float y0 = vertices[v0*3+1];
		float y1 = vertices[v1*3+1];
		float y2 = vertices[v2*3+1];
		float z0 = vertices[v0*3+2];
		float z1 = vertices[v1*3+2];
		float z2 = vertices[v2*3+2];
		
		Point3f a = new Point3f(x0,y0,z0);
		Point3f b = new Point3f(x1,y1,z1);
		Point3f c = new Point3f(x2,y2,z2);
		
		float sx = smallest(a.x, b.x, c.x);
		float sy = smallest(a.y, b.y, c.y);
		float sz = smallest(a.z, b.z, c.z);
		
		Point3f smallest = new Point3f(sx, sy, sz);
		
		float bx = biggest(a.x, b.x, c.x);
		float by = biggest(a.y, b.y, c.y);
		float bz = biggest(a.z, b.z, c.z);
		
		Point3f biggest = new Point3f(bx, by, bz);
		
		return new AxisAlignedBox(smallest, biggest);
		
		//return AxisAlignedBox.INFINITE_BOUNDING_BOX;
	}
	
	public float smallest(float a, float b, float c){
		float smallest;
		smallest = a;
		
		if(b<smallest)
			smallest = b;
		if(c<smallest)
			smallest = c;
		
		return smallest;
	}
	
	public float biggest(float a, float b, float c){
		float biggest;
		biggest = a;
		
		if(b>biggest)
			biggest = b;
		if(c>biggest)
			biggest = c;
		
		return biggest;
	}

	@SuppressWarnings("null")
	@Override
	public float surfaceArea() {
		float vertices[] = mesh.vertices;
		
		// Access the triangle vertices as follows (same for the normals):		
		// 1. Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];
		
		// 2. Access x,y,z coordinates for each vertex
		float x0 = vertices[v0*3];
		float x1 = vertices[v1*3];
		float x2 = vertices[v2*3];
		float y0 = vertices[v0*3+1];
		float y1 = vertices[v1*3+1];
		float y2 = vertices[v2*3+1];
		float z0 = vertices[v0*3+2];
		float z1 = vertices[v1*3+2];
		float z2 = vertices[v2*3+2];
		
		Point3f a = new Point3f(x0,y0,z0);
		Point3f b = new Point3f(x1,y1,z1);
		Point3f c = new Point3f(x2,y2,z2);
		
		Vector3f bVec = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);
		Vector3f cVec = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
		Vector3f crossP = new Vector3f(0,0,0);
		crossP.cross(bVec, cVec);

		return crossP.length()/2;
	}

}
