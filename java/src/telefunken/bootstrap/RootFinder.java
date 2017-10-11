/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

/**
 *
 * @author Bilal Khan
 */


public interface RootFinder 
{
    public double root(double eps, int maxIterations, double[] initialGuesses, AbstractMatchingEstimator f);
}
