/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.estimators.IPopulationEstimator;
import telefunken.generators.IGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.ISampler;
import telefunken.utils.Visualization;

/**
 *
 * @author Bilal Khan
 */
public class ExperimentByGraphGenerator implements IExperiment {
    
    private int _gtrials;
    private IGraphGenerator _ggen;
    private int _strials;
    private ISampler _sampler;
    private IHasher _hash;
    private IPopulationEstimator _estimator;
                    
    public ExperimentByGraphGenerator(
            int gtrials, 
            IGraphGenerator ggen, 
            int strials, 
            ISampler sampler, 
            IHasher hash, 
            IPopulationEstimator estimator) 
    {
        _gtrials = gtrials;
        _ggen = ggen;
        _strials = strials;
        _sampler = sampler;
        _hash = hash;
        _estimator = estimator;
    }
    
    public Results execute() 
    {
        double [] results = new double [_gtrials*_strials];
        
        int good=0;
        int bad=0;
        for (int i=0; i<_gtrials;i++) {
            DirectedSparseGraph<Vertex,Edge> g = _ggen.generateGraph();
            
            Visualization.showGraph(g);
            
            _hash.initialize(g.getVertices());
            
            for (int j=0; j<_strials;j++) {
                AbstractSample S = _sampler.generateSample(g);
                double value = _estimator.computeEstimate(S, _hash);
                if (Double.isNaN(value)) {
                    bad++;
                }
                else {
                    results[good]=value;
                    good++;
                }
            }
        }
        double perc_fail = (double)bad/(double)(_gtrials*_strials);
        
        double sum=0.0;
        for (int i=0; i<good;i++) {
            sum+=results[i];
        }
        double mean = sum/(double)good;
        
        double var=0.0;
        for (int i=0; i<good;i++) {
            var+=(results[i]-mean)*(results[i]-mean);
        }
        double std=Math.sqrt(var);
        
        return new Results(mean, std, perc_fail);
    }
}
