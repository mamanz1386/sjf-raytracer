package rt.samplers;

import rt.Sampler;

public class UniformSampler implements Sampler {

	@Override
	public float[][] makeSamples(int n, int d) {
		// TODO Auto-generated method stub
		float samples[][] = new float[n][d];
		int nsqrt = (int) Math.sqrt(n);
		for(int i=0; i<n; i++){
			samples[i][1]= (i/nsqrt+1)/(2*nsqrt);
			samples[i][0]= (i%nsqrt+1)/(2*nsqrt);
		}
		return samples;
	}

}
