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
import telefunken.samplers.RDSSample;

/**
 *
 * @author Bilal Khan
 */
public class CrossSeedEstimator extends AbstractMatchingEstimator {

    public CrossSeedEstimator(AbstractSample sample, IHasher hasher) {
        super(sample, hasher);
    }
    
    @Override
    public double f(double x) {
        return x - getNextN(x);
    }

    private double getNextN(double N) {
        double numer = _helper.getCrossSeedNumerator(N, (RDSSample)_sample);
        double denom = _helper.getCrossSeedDenominator(N, (RDSSample)_sample);
        double estimatedN = numer/denom;
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
