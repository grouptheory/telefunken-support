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
import telefunken.math.Lambert;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.FixedSampler;

/**
 *
 * @author Bilal Khan
 */
public class BaselineWithHashingCorrection extends BaselineWithRDSCorrection {

    
    private double _correctionForHashing;
      
    public BaselineWithHashingCorrection(AbstractSample sample, IHasher hasher) 
    {
        super(sample, hasher);

        if (_actualMatches <= 0.0) {
            _correctionForHashing = 0.0;
        }
        else {
            if ( ! hasher.isLossy()) {
                _correctionForHashing = 1.0;
            }
            else {
                // we have a lossy hasher
                
                double H = hasher.getHashspaceSize();
                double n2 = _estimatedVertexCount * _correctionForRDS;
                
                // uniform sample
                if (sample instanceof FixedSampler.FixedSample) 
                {
                    double c = H;
                    double b = _SS;
                    double a = n2;
                    
                    double exponent = -5.37346 * c / a;
                    double Wargs = -5.37346 * c * Math.exp(exponent) / b;
                    double Wval = Lambert.branch0(Wargs);
                    
                    double n3 = -5.37346 * c / Wval;
                            
                    _correctionForHashing = n3 / n2;
                }
                else // RDS sample
                { 
                    double CM2 = _dS[2] - Math.pow(_dS[1], 2.0);
                    double CM1 = _dS[1];

                    double CM2_over_CM1 = CM2/CM1;
                    
                    //double logM = Math.log(_actualMatches);
                    //double HlogM = H * logM;
                    double n3 = (n2 * H)/(H - n2*(1.0 - 0.5*Math.exp(-0.7942 * CM2_over_CM1)));
                    
                    _correctionForHashing = n3 / n2;
                            
//                    // when CM2 < CM1
//                    double corr1 = HlogM / (HlogM - 5.0 * n2);
//                    // when CM1 < CM2
//                    double corr2 = HlogM / (HlogM - 4.0 * n2);
//                    
//                    // when CM2<CM1 then delta<0 -> epowminusx>>0 -> sigmoid~0 -> alphaM=alphaM1
//                    // when CM2>CM1 then delta>0 -> epowminusx~0 -> sigmoid~1 -> alphaM=alphaM2
//
//                    double delta = CM2_over_CM1 - 1.0;
//                    double epowminusx = Math.exp(-5.0 * delta);
//                    double sigmoid = 1.0 / (1.0 + epowminusx);
//                    
//                    _correctionForHashing = sigmoid * (corr2-corr1) + corr1;
                }
            }
        }
    }

    public void diagnostics(AbstractSample S, IHasher hash, 
            DirectedSparseGraph<Vertex,Edge> g) {
        super.diagnostics(S, hash, g);
        
        System.out.println("      Base estimate="+_estimatedVertexCount);
        System.out.println("      Correction for RDS="+_correctionForRDS);
        System.out.println("      Correction for Hashing="+_correctionForHashing);
    }
    
    public Double getPopulationEstimate(AbstractSample sample) 
    { 
        if (_estimatedVertexCount==null)
            return null;
        
        if (_correctionForHashing<=0.0)
            return null;
        
        return (_estimatedVertexCount * _correctionForRDS) * _correctionForHashing;
    }
}
