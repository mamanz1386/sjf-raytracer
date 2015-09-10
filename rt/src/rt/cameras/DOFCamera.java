package rt.cameras;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import rt.Camera;
import rt.Ray;
import rt.util.StaticVecmath;

public class DOFCamera implements Camera{

	Vector3f eye;
	float fov, aspect;
	int width,height;
	float t,r;
	float apertureSize, focusLenght;
	Matrix4f m;
	Matrix4f p;
	
	/**
	 * Makes a camera with variable position.
	 * @param eye position of camera in world
	 * @param lookAt point camera is looking at
	 * @param up upwards direction of the camera
	 * @param fov field of view(in degrees)
	 * @param aspect ratio right/top
	 * @param width width of the image in pixels
	 * @param height height of the image in pixels
	 */
	public DOFCamera(Vector3f eye, Vector3f lookAt, Vector3f up, float fov, float aspect, int width, int height, float apertureSize, float focusLenght)
	{
		// Movable eye position in world coordinates
		this.eye = eye;
		this.fov=fov;
		this.aspect=aspect;
		this.width=width;
		this.height=height;
		this.focusLenght=focusLenght;
		this.apertureSize=apertureSize;
		
		t=(float)Math.tan(Math.toRadians(fov/2));
		r=aspect*t;
		
		//Create Viewpoint matrix
		p=new Matrix4f();
		p.m00=2*r/width;
		p.m11=2*t/height;
		p.m02=r;
		p.m12=t;
		p.m22=1;
		p.m33=1;
		
		//System.out.println(eye+":"+lookAt+":"+up);
		
		//Create Camera-World transformation matrix
		Vector3f w=StaticVecmath.sub(eye, lookAt);
		w.scale(1/w.length());
		
		up.cross(up, w);
		up.scale(1/up.length());
		Vector3f u=up;
		
		Vector3f v=new Vector3f();
		v.cross(w, u);
		
		//System.out.println(u+":"+v+":"+w);
		
		m=new Matrix4f();
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
		
		//m.mul(p);
		//System.out.println(m);
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
		
		
		//Vector4f d=new Vector4f((float)i, (float)j, -1F, 1F);
		Point4f d = new Point4f((float)i+sample[0],(float)j+sample[1],-1.f,1F);
		
		p.transform(d);
		
		if(apertureSize<=0){
			return new Ray(eye,StaticVecmath.normalize(StaticVecmath.sub(new Vector3f(d.x,d.y,d.z),eye)));
		}
		
		Point4f origin=new Point4f(0,0,0,1);
		origin.x+=(sample[0]-0.5)*apertureSize;
		origin.y+=(sample[1]-0.5)*apertureSize;
		Point3f P=new Point3f(StaticVecmath.scale(new Vector3f(d.x,d.y,d.z), focusLenght));
		Vector4f dir = new Vector4f(StaticVecmath.sub(P, new Point3f(origin.x,origin.y,0)));
		
		// Transform it back to world coordinates
		m.transform(dir);
		m.transform(origin);
		
		dir.normalize();
		
		// Make ray consisting of origin and direction in world coordinates
		return new Ray(new Point3f(origin.x,origin.y,origin.z), new Vector3f(dir.x,dir.y,dir.z));
	} 
}
