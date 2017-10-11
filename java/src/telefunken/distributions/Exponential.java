/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.distributions;

import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public class Exponential extends AbstractDistribution {

    private double _lambda;
    
    public Exponential(int n, double lambda) {
        super(n);
        _lambda=lambda;
    }
    
    public int[] generate() {
        int [] d = new int[_n];
        for (int i =0; i<_n; i++) {
            d[i]=1+(int)Math.round(Randomness.getExponential(_lambda));
        }
        return d;
    }

    @Override
    public String getHumanReadableName() {
        return "Exponential";
    }
}
