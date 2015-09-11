package rt.films;

import rt.Film;
import rt.HitRecord;
import rt.Spectrum;

/**
 * Uses a box filter when accumulating samples on a film. A box filter means
 * that samples contribute only to the pixel that they lie in. Sample values
 * are simply averaged.
 */
public class BrightEdgeFilm implements Film {
	
	private Spectrum[][] image;
	public int width, height;
	private Spectrum[][] unnormalized;
	private Spectrum[][] edge;
	private float nEdgeSamples[][];
	private float nSamples[][];
	public float criticalDelta;
	
	public BrightEdgeFilm(int width, int height, float criticalDelta)
	{
		this.width = width;
		this.height = height;
		this.criticalDelta=criticalDelta;
		image = new Spectrum[width][height];
		unnormalized = new Spectrum[width][height];
		edge=new Spectrum[width][height];
		
		nEdgeSamples = new float[width][height];
		nSamples = new float[width][height];
		
		for(int i=0; i<width; i++)
		{
			for(int j=0; j<height; j++)
			{
				image[i][j] = new Spectrum();
				unnormalized[i][j] = new Spectrum();
				edge[i][j] = new Spectrum();
			}
		}
	}
	
	public void addSample(double x, double y, Spectrum s)
	{
		if((int)x>=0 && (int)x<width && (int)y>=0 && (int)y<height)
		{
			int idx_x = (int)x;
			int idx_y = (int)y;
			if(nSamples[idx_x][idx_y]>0){
				Spectrum norm=new Spectrum(unnormalized[idx_x][idx_y]);
				Spectrum avgSpect=norm;
				avgSpect.mult(1f/nSamples[idx_x][idx_y]);
				float deltaR=Math.abs(s.r-avgSpect.r);
				float deltaG=Math.abs(s.g-avgSpect.g);
				float deltaB=Math.abs(s.b-avgSpect.b);
				if(deltaR>criticalDelta||deltaG>criticalDelta||deltaB>criticalDelta){
					//System.out.println(idx_x+":"+idx_y);
					s=new Spectrum(0, 0, 0);
					
					for(int i=-2; i<3; i++){
						for(int j=-2; j<3; j++){
							if(idx_x+i>=0&&idx_x+i<width&&idx_y+j>=0&&idx_y+j<height){
								if(nSamples[idx_x+i][idx_y+j]>0){
									edge[idx_x+i][idx_y+j].add(new Spectrum(-1f,-1f,-1f));
								}
							}
						}
					}
					return;
				}
			}
			unnormalized[idx_x][idx_y].add(s);
			nSamples[idx_x][idx_y]++;
		}
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public Spectrum[][] getImage()
	{
		for(int i=0; i<width; i++)
		{
			for(int j=0; j<height; j++)
			{
				image[i][j].r = unnormalized[i][j].r+edge[i][j].r/nSamples[i][j]+nEdgeSamples[i][j];
				image[i][j].g = unnormalized[i][j].g+edge[i][j].g/nSamples[i][j]+nEdgeSamples[i][j];
				image[i][j].b = unnormalized[i][j].b+edge[i][j].b/nSamples[i][j]+nEdgeSamples[i][j];
			}
		}
		return image;
	}
}
