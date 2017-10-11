/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import telefunken.core.Vertex;
import telefunken.estimators.S_rS_M_Computer;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public abstract class AbstractMatchingEstimator extends SampleGeometry
{
    public AbstractMatchingEstimator(AbstractSample sample, IHasher hasher) {
        super(sample, hasher);
    }
    
    public abstract double f(double x);
    
    public abstract double expMatches(double guessV, AbstractSample sample);
}
