/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.distributions;

/**
 *
 * @author Bilal Khan
 */
public abstract class AbstractDistribution implements IDistributionGenerator {
    
    protected final int _n;
    
    protected AbstractDistribution(int n) {
        _n = n;
    }
}
