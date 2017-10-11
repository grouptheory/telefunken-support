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
 * @author Bilal Khan
 */
public class MatchingEstimatorV0 extends AbstractMatchingEstimator {

    double _A, _B, _C;
    
    public MatchingEstimatorV0(AbstractSample sample, IHasher losslessHasher) {
        super(sample, losslessHasher);
        
        _A = 1.636;
        _B = 1.173;
        _C = 0.416;
    }

    private double computeSlope(double Kplus2F, double L) {
        double alpha = _Kplus2F / L;
        
        double num = _A * alpha * alpha;
        double exponent = _B * Math.pow(alpha, _C);
        double den = Math.exp(exponent);
        
        double slope = num / den;
        return slope;
    }
    
    @Override
    public double expMatches(double guessV, AbstractSample sample) 
    {
        //BigDecimal guessL = new BigDecimal(""+guessV).multiply(_dS_Harmonic).subtract(_2F);
        double guessL = guessV * _dG[1] - _2F;
     
        double slope = computeSlope(_Kplus2F, guessL);
        
        double expectedM = slope * guessL;
        return expectedM;
    }
    
    public double f(double x) 
    {    
        double guessV = x;
        double expectedM = expMatches(guessV, _sample);
        double actualM = this.actualMatches();
        double error = actualM - expectedM;
        return error;
    }

    @Override
    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex, Edge> g) {
        
        double dSampMean = _dS[1];
        double one_over_dS;
            one_over_dS = 1.0 / dSampMean;

        double baseline = 
                one_over_dS * _Kplus2Fsquared / _actualMatches;
                
        double trueL = S.get_TrueL();
        double alpha = _Kplus2F / trueL;
        
        double slope = computeSlope(_Kplus2F, trueL);
        
        double predictedM = slope * trueL;
        double errorM = _actualMatches / predictedM;
        
        System.out.println("      True L="+trueL);
        System.out.println("      Alpha="+alpha);
        System.out.println("      PredictedSlope="+slope);
        System.out.println("      PredictedM="+predictedM);
        System.out.println("      Error(ActualM / PredictedM)="+errorM);
        System.out.println("      Baseline prediction="+baseline);
    }
    
}
