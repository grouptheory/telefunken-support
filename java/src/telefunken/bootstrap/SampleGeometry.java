/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.estimators.S_rS_M_Computer;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public abstract class SampleGeometry {
    
    protected static final int DIGITS=10;

    protected final AbstractSample _sample;
    protected final IHasher _hasher;
    
    protected S_rS_M_Computer _helper;
    protected final double _actualMatches;
    
    protected final double _SS;  // sample size
    protected final double _K;  // free ends in S
    protected final double _2F; // total internal ends in S
    protected final double _Kplus2F;
    protected final double _Kplus2Fsquared;
    
    protected final double[] _dG;            // population
    protected final double[] _dS;         // sample
    protected final double[] _dS_Harmonic;         // sample
    
    // maps to look up free degree
    protected final CacheByVertex<Double> _fd = new CacheByVertex<Double>();
    protected final CacheByVertex<Integer>    _fd_int = new CacheByVertex<Integer>();
    
    protected SampleGeometry(AbstractSample sample, IHasher hasher) 
    {
        _hasher = hasher;
        _sample = sample;
        
        _helper = new S_rS_M_Computer(sample, _hasher);
        _actualMatches = actualMatches();
        
        _dG = _sample.dG_All_Moments();
        _dS = _sample.dSample_All_Moments();
        _dS_Harmonic = _sample.dSample_All_Harmonic_Moments();
        
        _2F = _sample.get_SampleEndsCount();
        
        // accumulate free ends
        double tmpK = 0;
        for (Vertex u : sample.getVertices()) {
            int fdu_int = (int)_sample.dFree(u);
            double fdu = fdu_int;
            _fd_int.save(u, fdu_int);
            _fd.save(u, fdu);
            // free ends count
            tmpK = tmpK + fdu;
        }
        _K = tmpK;

        _Kplus2F = (double)_K + (double)_2F;
        _Kplus2Fsquared = (double)_Kplus2F * (double)_Kplus2F;
        
        _SS = sample.getVertices().size();
    }
        
    public final double actualMatches() 
    {
        return _helper.M_codes.size();
    }
    
    public double trueK() {
        return _K;
    }
    
    public double true2F() {
        return _2F;
    }

    public abstract void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex,Edge> g);
}
