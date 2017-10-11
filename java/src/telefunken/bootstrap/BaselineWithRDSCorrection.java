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
import telefunken.samplers.FixedSampler;

/**
 *
 * @author Bilal Khan
 */
public class BaselineWithRDSCorrection extends Baseline {
    
    protected final double _correctionForRDS;

    public BaselineWithRDSCorrection(AbstractSample sample, 
            IHasher losslessHasher) {
        super(sample, losslessHasher);
        
        if (_actualMatches <= 0.0) {
            _correctionForRDS = 0.0;
        }
        else {
            double alphaS = 1.0;
            double alphaK = 1.0;
            double alphaM = 1.0;
            
            if (sample instanceof FixedSampler.FixedSample) 
            {
                _correctionForRDS = 1.0;
            }
            else {
                
                double dH = _dS_Harmonic[1];
                double dS = _dS[1];
                
                double dH_over_dS = dH/dS;
                
                double dH4 = Math.pow(dH, 4.0);
                double dS5 = Math.pow(dS, 5.0);
                double dH4_over_dS5 = dH4/dS5;
                
                double dH1p5 = Math.pow(dH, 1.5);
                double dS2 = Math.pow(dS, 2.0);
                double dH1p5_over_dS2 = dH1p5/dS2;
                
                double CM2 = _dS[2] - Math.pow(_dS[1], 2.0);
                double CM1 = _dS[1];
                
                double CM2_over_CM1 = CM2/CM1;
                
                alphaS = -0.09551 + 1.08498*dH_over_dS;
                alphaK = -0.09565 + 1.08516*dH_over_dS;
                
                // when CM2 < CM1
                double alphaM1 = 1.0 + 0.006131*Math.exp(11.7 * dH1p5_over_dS2);
                
                // when CM1 < CM2
                double alphaM2 = 0.3405 + 10.15*dH4_over_dS5;
                //double alphaM2 = 0.3476 + 10.04*dH4_over_dS5;

                // when CM2<CM1 then delta<0 -> epowminusx>>0 -> sigmoid~0 -> alphaM=alphaM1
                // when CM2>CM1 then delta>0 -> epowminusx~0 -> sigmoid~1 -> alphaM=alphaM2
                
                double delta = CM2_over_CM1 - 1.0;
                double epowminusx = Math.exp(-5.0 * delta);
                double sigmoid = 1.0 / (1.0 + epowminusx);
                alphaM = sigmoid * (alphaM2-alphaM1) + alphaM1;
                
//                alphaM = CM2_over_CM1 >= 1.0 ? alphaM2 : alphaM1;

                _correctionForRDS = (1.0/alphaS) * Math.pow(alphaK, 2.0) * (1.0/alphaM);
            }
        }
    }

    public void diagnostics(AbstractSample S, IHasher hash, 
            DirectedSparseGraph<Vertex,Edge> g) {
        super.diagnostics(S, hash, g);
        
        System.out.println("      Base estimate="+_estimatedVertexCount);
        System.out.println("      Correction for RDS="+_correctionForRDS);
    }
    
    public Double getPopulationEstimate(AbstractSample sample) 
    {
        return _estimatedVertexCount==null ? null : 
                _estimatedVertexCount * _correctionForRDS;
    }
}
