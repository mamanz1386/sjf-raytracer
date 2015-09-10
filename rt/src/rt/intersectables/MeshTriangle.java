package rt.intersectables;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Ray;
import rt.accelerators.AxisAlignedBox;
import rt.util.StaticVecmath;

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
		// Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];
		
		// Access x,y,z coordinates for each vertex
		float x0 = vertices[v0*3];
		float x1 = vertices[v1*3];
		float x2 = vertices[v2*3];
		float y0 = vertices[v0*3+1];
		float y1 = vertices[v1*3+1];
		float y2 = vertices[v2*3+1];
		float z0 = vertices[v0*3+2];
		float z1 = vertices[v1*3+2];
		float z2 = vertices[v2*3+2];
		
		// Triangle vertices
		Vector3f verc1 = new Vector3f(x0,y0,z0);
		Vector3f verc2 = new Vector3f(x1,y1,z1);
		Vector3f verc3 = new Vector3f(x2,y2,z2);
		
		Vector3f p = new Vector3f(0,0,0);
		Vector3f q = new Vector3f(0,0,0);
		Vector3f t = new Vector3f(0,0,0);
		
		//Find vectors for two edges
		Vector3f e1 = new Vector3f(0,0,0);
		e1.sub(verc2, verc1);
		Vector3f e2 = new Vector3f(0,0,0);
		e2.sub(verc3, verc1);
		
		Vector3f o = r.origin; 		//Ray origin
		Vector3f d = r.direction; 	//Ray direction
		
		float epsilon = 0.000001f;
		float det, inv_det, u, v;
		float lambda;
		
		//Begin calculating determinant
		p.cross(d, e2);
		
		//if determinant is near zero, ray lies in plane of triangle
		det = p.dot(e1);
		
		//NOT CULLING
		if(det > -epsilon && det < epsilon) return null;
		inv_det = 1.f / det;
		
		//calculate distance from V1 to ray origin
		t.sub(o, verc1);
		
		//Calculate u parameter
		u = t.dot(p) * inv_det;
		
		//The intersection lies outside of the triangle
		if(u <= 0.f || u >= 1.f) return null;
		
		//Prepare to test v parameter
		q.cross(t, e1);
		
		//Calculate v parameter
		v = d.dot(q) * inv_det;
		  
		//The intersection lies outside of the triangle
		if(v <= 0.f || u + v  >= 1.f) return null;
		
		lambda = e2.dot(q) * inv_det;
				
		if(lambda >= epsilon){ //ray intersection
			
		//Position
		Point3f position = r.pointAt(lambda);
		
		//Calculation the normal
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
		
		float a = (float) (1 -u -v);
		Vector3f normal = new Vector3f(a*nx0 + u*nx1 + v*nx2,
									   a*ny0 + u*ny1 + v*ny2,
									   a*nz0 + u*nz1 + v*nz2);
		normal.normalize();
		
		return new HitRecord(lambda, position, normal, r.direction, this, mesh.material, u, v);
			
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

	//@SuppressWarnings("null")
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
