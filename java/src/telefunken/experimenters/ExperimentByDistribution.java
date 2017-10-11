/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

import telefunken.distributions.IDistributionGenerator;
import telefunken.estimators.IPopulationEstimator;
import telefunken.generators.ConfigurationGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.samplers.ISampler;

/**
 *
 * @author Bilal Khan
 */
public class ExperimentByDistribution implements IExperiment {
    
    private int _gtrials;
    private IDistributionGenerator _dgen;
    private int _strials;
    private ISampler _sampler;
    private IHasher _hash;
    private IPopulationEstimator _estimator;
                    
    public ExperimentByDistribution(
            int gtrials, 
            IDistributionGenerator dgen, 
            int strials, 
            ISampler sampler, 
            IHasher hash, 
            IPopulationEstimator estimator) 
    {
        _gtrials = gtrials;
        _dgen = dgen;
        _strials = strials;
        _sampler = sampler;
        _hash = hash;
        _estimator = estimator;
    }
    
    public Results execute() 
    {
        int [] degrees = _dgen.generate();
        IGraphGenerator ggen = new ConfigurationGenerator(degrees, _dgen.getHumanReadableName());
        IExperiment exp = 
                new ExperimentByGraphGenerator(_gtrials, ggen, _strials, _sampler, _hash, _estimator);
        return exp.execute();
    }
}
