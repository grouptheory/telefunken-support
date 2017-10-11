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
import telefunken.samplers.FixedSampler.FixedSample;

/**
 *
 * @author Bilal Khan
 */
public class Baseline extends SampleGeometry {

    
    protected final Double _estimatedVertexCount;

    public Baseline(AbstractSample sample, IHasher losslessHasher) {
        super(sample, losslessHasher);
        
        double dSampMean = _dS[1];
        
        if (_actualMatches <= 0.0) {
            _estimatedVertexCount=null;
        }
        else {
            double one_over_dS;
            one_over_dS = 1.0 / dSampMean;

            double _exteriorV;
            if (sample instanceof FixedSample) 
            {
                _exteriorV = one_over_dS * (double)
                        _Kplus2Fsquared / (double)_actualMatches;
            }
            else {
                _exteriorV =  one_over_dS * (double)
                        _Kplus2Fsquared / (double)_actualMatches;
            }
            _estimatedVertexCount = _exteriorV; 
        }
    }
    
    public Double getPopulationEstimate(AbstractSample sample) 
    {
        return _estimatedVertexCount;
    }
    
    public double estimatedM(double trueL) 
    {
        return 0.0;
    }

    @Override
    public void diagnostics(AbstractSample S, IHasher hash, 
            DirectedSparseGraph<Vertex, Edge> g) {
        // no op
    }
}
