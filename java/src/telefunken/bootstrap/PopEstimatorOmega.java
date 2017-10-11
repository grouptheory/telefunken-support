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
public class PopEstimatorOmega extends SampleGeometry {

    private static final int DIGITS=10;
    
    protected final Double _estimatedVertexCount;
      
    private double _A, _B;
    private double _alpha;
    private double _slope;
    private double _predictedM;
    private double _errorM;
    private double _trueL;
    
    public PopEstimatorOmega(AbstractSample sample, IHasher losslessHasher) {
        super(sample, losslessHasher);
        
        double dSampMean = _dS[1];
        
        _A = 0.3239;
        _B = 0.2003;

        if (_actualMatches <= 0) {
            _estimatedVertexCount=null;
        }
        else {
            double one_over_dS = 1.0 / dSampMean;

            double _exteriorV;
            if (sample instanceof FixedSample) 
            {
                _exteriorV = one_over_dS * _Kplus2Fsquared / _actualMatches;
            }
            else {
                double m_minus_bk = _actualMatches - _B * _Kplus2Fsquared;
                double ak2 = _A * _Kplus2Fsquared;
                _exteriorV = one_over_dS * ak2 / m_minus_bk;
            }
            _estimatedVertexCount = _exteriorV; 
        }
    }

    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex,Edge> g) 
    {
        double dSampMean = _dS[1];
        double one_over_dS = 1.0 / dSampMean;

        double baseline = 
                one_over_dS * _Kplus2Fsquared / _actualMatches;
                
        _trueL = S.get_TrueL();
        _alpha = _Kplus2F / _trueL;
        _slope = _A * _alpha * _alpha + _B * _alpha;
        _predictedM = _slope * _trueL;
        _errorM = _actualMatches / _predictedM;
        
        System.out.println("      True L="+_trueL);
        System.out.println("      Alpha="+_alpha);
        System.out.println("      PredictedSlope="+_slope);
        System.out.println("      PredictedM="+_predictedM);
        System.out.println("      Error(ActualM / PredictedM)="+_errorM);
        System.out.println("      Baseline prediction="+baseline);
    }
    
    public double getPopulationEstimate(AbstractSample sample) 
    {
        return _estimatedVertexCount;
    }
}
