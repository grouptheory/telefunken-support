/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

import telefunken.distributions.IDistributionGenerator;
import telefunken.distributions.LogNormal;
import telefunken.estimators.IPopulationEstimator;
import telefunken.estimators.HashingEstimate;
import telefunken.hashers.IHasher;
import telefunken.hashers.UniformRandomHasher;
import telefunken.samplers.ISampler;
import telefunken.samplers.RDSSampler;

/**
 *
 * @author Bilal Khan
 */
public class Exp1 {
    
    public static void main(String [] args)
    {
        int n = 4000;
        
        int gtrials = 1;
        
        IDistributionGenerator dgen = new LogNormal(500, 10.0, 1.0);
        
        int strials = 1;
        
        int numseeds = 7;
        int samplesize = (int)Math.ceil(n * 0.05);
        double[] referrals = new double [3];
        referrals[0]=0.0;
        referrals[1]=0.90;
        referrals[2]=0.10;
        ISampler sampler = new RDSSampler(numseeds, samplesize, referrals);
        
        int H=5000;
        IHasher hash = new UniformRandomHasher(H);
        
        IPopulationEstimator est = new HashingEstimate();
        
        IExperiment exp = new ExperimentByDistribution(
                                    gtrials, 
                                    dgen, 
                                    strials, 
                                    sampler, 
                                    hash, 
                                    est);
        
        Results res = exp.execute();
        
        System.out.println("Results = "+res.toString());
    }

}
