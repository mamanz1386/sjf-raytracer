package rt.materials;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;
/**
 * TODO: Possibly add bump map support
 */
public class Textured implements Material {

	private BufferedImage texture;

	/**
	 * Creates a textured material of the given image.
	 * @param textureFileName
	 */
	public Textured(String textureFileName) {
		try {
			texture = ImageIO.read(new File(textureFileName));
			System.out.println("h: "+texture.getHeight()+"     w: "+texture.getWidth());
		} catch (IOException e) {
			System.err.println("Could not load texture: ");
			e.printStackTrace();
		}
	}
	@Override
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut,
			Vector3f wIn) {
		int texture_x = (int) Math.max(Math.min((texture.getWidth() * hitRecord.v), texture.getWidth()-1),0);
		int texture_y = (int) Math.max(Math.min((texture.getHeight() * hitRecord.u), texture.getHeight()-1),0);
		
		if(texture.getWidth() * hitRecord.v < 0 || texture.getHeight() * hitRecord.u < 0|| texture.getWidth() * hitRecord.v > texture.getWidth()|| texture.getHeight() * hitRecord.u > texture.getHeight())
			System.out.println("v: "+ texture.getWidth() * hitRecord.v+ "     u: "+ texture.getHeight() * hitRecord.u);
		
		int rgb = texture.getRGB(texture_x, texture_y);
		Color color = new Color(rgb);
		float r = color.getRed()/255f;
		float g = color.getGreen()/255f;
		float b = color.getBlue()/255f;

		return new Spectrum(r,g,b);
	}

	@Override
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		return null;
	}

	@Override
	public boolean hasSpecularReflection() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord) {
		return null;
	}

	@Override
	public boolean hasSpecularRefraction() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord) {
		return null;
	}

	@Override
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample) {
		return null;
	}

	@Override
	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return new ShadingSample();
	}

	@Override
	public boolean castsShadows() {
		return true;
	}

	@Override
	public float getRefractionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}
