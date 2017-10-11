/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

import java.util.Random;
import telefunken.utils.Log;

/**
 *
 * @author Bilal Khan
 */
public final class Randomness {

    // random number seed
    static int RNGSEED = 0;

    private static boolean _initialized = false;
    private static Random _rng = null;

    public static void initialize(int seed) {
	if (!_initialized) {
	    _initialized = true;
	    Log.diag(Randomness.class.getName(), Log.DEBUG, "RNG initialized with seed = "+seed);
	    _rng = new Random(seed);
	}
	else {
	    Log.diag(Randomness.class.getName(), Log.WARN, "Cannot reinitialize RNG!");
	}
    }

    public static Double getDouble() {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
	return _rng.nextDouble();
    }
    
    public static int getInteger() {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
	return _rng.nextInt(Integer.MAX_VALUE - 1);
    }
    
    public static double getExponential(double lambda) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        return  Math.log(1-getDouble())/(-lambda);
    }
    
    public static double getPoisson(double lambda) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
          k++;
          p *= getDouble();
        } while (p > L);

        return k - 1;
    }
    
    public static double getBinomial(double n, double p) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        int x = 0;
        for(int i = 0; i < n; i++) {
          if(getDouble() < p)
            x++;
        }
        return x;
    }
    
    public static double getGaussian(double mu, double std) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        return _rng.nextGaussian()*std + mu;
    }
    
    public static double getLogNormal(double mu, double std) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        double mu2 = Math.log(mu*mu / Math.sqrt(mu*mu + std*std));
        double std2 = Math.sqrt(Math.log((mu*mu+std*std)/(mu*mu)));
        double norm=getGaussian(mu2,std2);
        return Math.exp(norm);
    }
    
    public static int getSample(double[] cumul) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        int maxindex = cumul.length;
        double toss=Randomness.getDouble();
        double prev=0.0;
        int answer=-1;
        for (int i=0; i<maxindex; i++) {
            if (toss>=prev && toss<cumul[i]) {
                answer=i;
                break;
            }
            prev=cumul[i];
        }
        return answer;
    }
    
    public static double[] convertToCumulativeDistribution(double[] bias) {
	if (!_initialized) {
	    Log.diag(Randomness.class.getName(), Log.INFO, "Using specified RNG seed");
	    initialize(Randomness.RNGSEED);
	}
        int maxindex = bias.length;
        double[] p = new double[maxindex];
        double sum=0.0;
        for (int i=0; i<maxindex; i++) {
            p[i]=bias[i];
            sum+=p[i];
        }
        for (int i=0; i<maxindex; i++) {
            p[i]/=sum;
        }
        double[] cumul = new double[maxindex];
        cumul[0]=p[0];
        for (int i=1; i<maxindex; i++) {
            cumul[i]=cumul[i-1]+p[i];
        }
        return cumul;
    }
}
