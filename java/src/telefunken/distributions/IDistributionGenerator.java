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
public interface IDistributionGenerator {
    String getHumanReadableName();
    
    int[] generate();
}
