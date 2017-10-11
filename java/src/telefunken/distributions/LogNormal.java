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
public class LogNormal extends AbstractDistribution {

    private double _mu;
    private double _std;
    
    public LogNormal(int n, double mu, double std) {
        super(n);
        _mu=mu;
        _std=std;
    }
    
    public int[] generate() {
        int [] d = new int[_n];
        for (int i =0; i<_n; i++) {
            d[i]=1+(int)Math.round(Randomness.getLogNormal(_mu,_std));
        }
        return d;
    }
    @Override
    public String getHumanReadableName() {
        return "LogNormal";
    }
}