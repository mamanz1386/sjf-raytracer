package rt.cameras;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import rt.Ray;
import rt.util.StaticVecmath;

public class MovableCamera {

	Vector3f eye;
	float fov, aspect;
	int width,height;
	Matrix4f m;
	
	/**
	 * Makes a camera with fixed position and view frustum. The position is at [0,0,3] in world space. 
	 * The camera looks down the negative z-axis towards the origin. The view frustum of the camera
	 * goes through the points [-1,-1,-1], [1,-1,-1], [-1,1,-1],[1,1,-1] in camera coordinates.
	 * 
	 * @param width width of the image in pixels
	 * @param height height of the image in pixels
	 */
	public MovableCamera(Vector3f eye, Vector3f lookAt, Vector3f up, float fov, float aspect, int width, int height)
	{
		// Movable eye position in world coordinates
		this.eye = eye;
		this.fov=fov;
		this.aspect=aspect;
		this.width=width;
		this.height=height;
		
		//Make Camera-World transformation matrix
		Vector3f w=StaticVecmath.sub(eye, lookAt);
		w.scale(1/w.length());
		up.cross(w, up);
		Vector3f u=new Vector3f();
		u.scale(1/up.length());
		Vector3f v=new Vector3f();
		v.cross(w, u);
		
		m=new Matrix4f();
		m.setIdentity();
		m.m00=u.x;
		m.m10=u.y;
		m.m20=u.z;
		m.m30=0;
		
		m.m01=v.x;
		m.m11=v.y;
		m.m21=v.z;
		m.m31=0;
		
		m.m02=w.x;
		m.m12=w.y;
		m.m22=w.z;
		m.m32=0;
		
		m.m03=eye.x;
		m.m13=eye.y;
		m.m23=eye.z;
		m.m33=1;
	}

	/**
	 * Make a world space ray. The method receives a sample that 
	 * the camera uses to generate the ray. It uses the first two
	 * sample dimensions to sample a location in the current 
	 * pixel. The samples are assumed to be in the range [0,1].
	 * 
	 * @param i column index of pixel through which ray goes (0 = left boundary)
	 * @param j row index of pixel through which ray goes (0 = bottom boundary)
	 * @param sample random sample that the camera can use to generate a ray   
	 */
	public Ray makeWorldSpaceRay(int i, int j, float[] sample) {
		// Make point on image plane in viewport coordinates, that is range [0,width-1] x [0,height-1]
		// The assumption is that pixel [i,j] is the square [i,i+1] x [j,j+1] in viewport coordinates
		float t=(float)Math.tan(fov/2);
		float r=aspect*t;
		Vector4f d=new Vector4f((float)(2*r*(i+0.5)/width-r), (float)(2*t*(j+0.5)/height-t), -1F, 1F);
		//Vector4f d = new Vector4f((float)i+sample[0],(float)j+sample[1],-1.f,1.f);
		
		// Transform it back to world coordinates
		m.transform(d);
		
		// Make ray consisting of origin and direction in world coordinates
		Vector3f dir = new Vector3f();
		dir.sub(new Vector3f(d.x, d.y, d.z), eye);
		Ray ray = new Ray(new Vector3f(eye), dir);
		return ray;
	} 
}
