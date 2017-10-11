/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public class DegreeCorrectedEstimator extends AbstractMatchingEstimator {

    public DegreeCorrectedEstimator(AbstractSample sample, IHasher losslessHasher) {
        super(sample, losslessHasher);
    }
    
    @Override
    public double f(double x) {
        return x - getNextN(x);
    }

    private double getNextN(double N) {
        double m = _helper.getWeightedM(N, this._sample, this._hasher);
        double estimatedN = ((_helper.dSample - 1.0) / _helper.dPopulation) * _helper.S_size * _helper.rS_size / m;
        return estimatedN;
    }
    
    @Override
    public double expMatches(double guessV, AbstractSample sample) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex, Edge> g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
