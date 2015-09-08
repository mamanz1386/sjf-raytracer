package rt.util;

import static org.junit.Assert.*;

import org.junit.Test;

import rt.intersectables.Mesh;
import rt.intersectables.MeshTriangle;

public class TestMeshTriangle {

	@Test
	public void test() {
		float[] vertices = {0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 0.f, 1.f, 0.f};
		float[] normals = {0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f};
		int[] indices = {0, 1, 2};
		
		Mesh mesh = new Mesh(vertices, normals, indices);
		MeshTriangle t = mesh.triangles[0];
		float gottenSurfaceArea = t.surfaceArea();
		float expectedSurfaceArea = 0.5f;
		assertEquals(expectedSurfaceArea, gottenSurfaceArea, 0.00001);
	}

}
