/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import telefunken.math.Stats;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.text.DecimalFormat;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.distributions.IDistributionGenerator;
import telefunken.distributions.LogNormal;
import telefunken.estimators.IPopulationEstimator;
import telefunken.estimators.HashingEstimate;
import telefunken.generators.ConfigurationGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.hashers.LosslessHasher;
import telefunken.hashers.UniformRandomHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.ISampler;
import telefunken.samplers.RDSSampler;
import telefunken.samplers.UniformSampler;

/**
 *
 * @author Bilal Khan
 */
public class NaiveLogNormalTest {
   
    DecimalFormat df = new DecimalFormat("#.00"); 
    int size=1000;
    double paramval=5.0;
    double pmin=0.01;
    double pmax=0.25;
    double pstep=0.01;
    int trials=30;
    
    @Test
    public void test1() {
        System.out.println("================== Uniform sampling");
        double param4=paramval;
        IDistributionGenerator dg4 = new LogNormal(size,param4,1.0);
        int [] deg4 = dg4.generate();
        IGraphGenerator ggen = new ConfigurationGenerator(deg4, dg4.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        
        IHasher hash = new LosslessHasher();
        hash.initialize(g.getVertices());
        
        for (double p=pmin; p<=pmax; p+=pstep) {
            UniformSampler sampler3 = new UniformSampler(p);
            Double [] vals = new Double [trials];
            for (int i=1;i<trials;i++) {
                AbstractSample s3 = sampler3.generateSample(g);
                IPopulationEstimator est = new HashingEstimate();
                vals[i] = est.computeEstimate(s3, hash);
            }
            Stats.Moments m=Stats.process(vals);
                System.out.println("UNIFORM p="+df.format(p)+" N="+g.getVertexCount()+
                                   " EST=("+df.format(m.mean)+","+df.format(m.std)+")");
        }
    }
    
    @Test
    public void test2() {
        System.out.println("================== RDS");
        double param4=paramval;
        IDistributionGenerator dg4 = new LogNormal(size,param4,1.0);
        int [] deg4 = dg4.generate();
        IGraphGenerator ggen = new ConfigurationGenerator(deg4, dg4.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        int n = size;
        
        IHasher hash = new LosslessHasher();
        hash.initialize(g.getVertices());
        
        for (double p=pmin; p<=pmax; p+=pstep) {
            int numseeds = 1;
            int samplesize = (int)Math.ceil(n * p);
            double[] referrals = new double [4];
            referrals[0]=0.0;
            referrals[1]=0.9;
            referrals[2]=0.1;
            referrals[3]=0.0;

            ISampler sampler3 = new RDSSampler(numseeds, samplesize, referrals);
            Double [] vals = new Double [trials];
            for (int i=1;i<trials;i++) {
                AbstractSample s3 = sampler3.generateSample(g);
                IPopulationEstimator est = new HashingEstimate();
                vals[i] = est.computeEstimate(s3, hash);
            } 
           Stats.Moments m=Stats.process(vals);
                System.out.println("RDS p="+df.format(p)+" N="+g.getVertexCount()+
                                   " EST=("+df.format(m.mean)+","+df.format(m.std)+")");
        }
    }
        
    @Test
    public void test3() {
        System.out.println("================== Uniform with Hashing");
        double param4=paramval;
        IDistributionGenerator dg4 = new LogNormal(size,param4,1.0);
        int [] deg4 = dg4.generate();
        IGraphGenerator ggen = new ConfigurationGenerator(deg4, dg4.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        
        IHasher hash = new UniformRandomHasher(1000);
        hash.initialize(g.getVertices());
        
        for (double p=pmin; p<=pmax; p+=pstep) {
            UniformSampler sampler3 = new UniformSampler(p);
            Double [] vals = new Double [trials];
            for (int i=1;i<trials;i++) {
                AbstractSample s3 = sampler3.generateSample(g);
                IPopulationEstimator est = new HashingEstimate();
                vals[i] = est.computeEstimate(s3, hash);
            }
            Stats.Moments m=Stats.process(vals);
                System.out.println("psi-UNIFORM p="+df.format(p)+" N="+g.getVertexCount()+
                                   " EST=("+df.format(m.mean)+","+df.format(m.std)+")");
        }
    }
    
    @Test
    public void test4() {
        System.out.println("================== RDS with Hashing");
        double param4=paramval;
        IDistributionGenerator dg4 = new LogNormal(size,param4,1.0);
        int [] deg4 = dg4.generate();
        IGraphGenerator ggen = new ConfigurationGenerator(deg4, dg4.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        int n = size;
        
        IHasher hash = new UniformRandomHasher(1000);
        hash.initialize(g.getVertices());
        
        for (double p=pmin; p<=pmax; p+=pstep) {
            int numseeds = 1;
            int samplesize = (int)Math.ceil(n * p);
            double[] referrals = new double [4];
            referrals[0]=0.0;
            referrals[1]=0.05;
            referrals[2]=0.10;
            referrals[3]=0.85;

            ISampler sampler3 = new RDSSampler(numseeds, samplesize, referrals);
            Double [] vals = new Double [trials];
            for (int i=1;i<trials;i++) {
                AbstractSample s3 = sampler3.generateSample(g);
                IPopulationEstimator est = new HashingEstimate();
                vals[i] = est.computeEstimate(s3, hash);
            }
            Stats.Moments m=Stats.process(vals);
                System.out.println("psi-RDS p="+df.format(p)+" N="+g.getVertexCount()+
                                   " EST=("+df.format(m.mean)+","+df.format(m.std)+")");
        }
    } 
}
