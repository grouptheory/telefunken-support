/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections15.Factory;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.distributions.Exponential;
import telefunken.distributions.IDistributionGenerator;
import telefunken.distributions.LogNormal;
import telefunken.distributions.Poisson;
import telefunken.distributions.Uniform;
import telefunken.estimators.BaselineWithRDSCorrectionEstimate;
import telefunken.estimators.BaselineEstimate;
import telefunken.estimators.CrossSeedEstimate;
import telefunken.estimators.IPopulationEstimator;
import telefunken.estimators.HashingEstimate;
import telefunken.estimators.DegreeCorrectedEstimate;
import telefunken.experimenters.ResultsDatabase;
import telefunken.generators.BarabasiAlbertGenerator;
import telefunken.generators.ConfigurationGenerator;
import telefunken.generators.ErdosRenyiGenerator;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.generators.PajekFileGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.hashers.LosslessHasher;
import telefunken.hashers.UniformRandomHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.ISampler;
import telefunken.samplers.RDSSampler;
import telefunken.samplers.UniformSampler;
import telefunken.math.Randomness;
import telefunken.samplers.FixedSampler;

/**
 *
 * @author Bilal Khan
 */
public class RealDataTest {
    
    public void test1(String fileName, String testname) {
        int NUMTRIALS = 900;
        
        Randomness.initialize(1);
        
        ResultsDatabase DB = new ResultsDatabase(testname);
             
        DirectedSparseGraph<Vertex,Edge> g = getGraph(fileName);
        int n = g.getVertexCount();
        int gnum = 1;
        int gi = 999;
        
        // 0=lossless, 1=500, 2=1000, 3=2000, 4=4000, 5=8000
        // 6=16K, 7=32K, 8=64K, 9=128K, 10=256K
        List<Integer> hlist = Arrays.asList(2,7,10); //2,7,10); 
        for (int hi : hlist) {
            IHasher hash = getHasher(hi, g, 500);

            List<Integer> sslist = Arrays.asList(250,500,750); //,1000,2000); //100,200,400,800); // 250, 500, 750
            for (int ss : sslist) {

                for (int trial=0; trial<NUMTRIALS; trial++) {

                    List<Integer> slist = Arrays.asList(1); //, 2); // 0=Uniform, 1=RDS/w7
                    for (int si : slist) 
                    {
                        ISampler samp = getConstantSizeSampler(si, g, ss);

                        //System.out.println("============ G="+gnum+" Type:" + ggen.getHumanReadableName() + " SS:"+ss+" Trial:"+trial +" Sampler:" + samp.getHumanReadableName());

                        AbstractSample S = samp.generateSample(g);

                        double[] dG = S.dG_All_Moments();
                        double[] dS = S.dSample_All_Moments();
                        double[] dH = S.dSample_All_Harmonic_Moments();
                        double L = S.get_TrueL();

                          List<Integer> elist = Arrays.asList(3,4); //0,1); // 0=closed form, 1=RDS-corrected, 2=ohash-corrected
                          for (int ei : elist) {
                            IPopulationEstimator est = getEstimator(ei);
                            //System.out.println("====================== Estimator " + est.getHumanReadableName());


                            long start = System.currentTimeMillis();  
                            double val = est.computeEstimate(S, hash);

                            long elapsedTime = (System.currentTimeMillis() - start);

                            // est.diagnostics(S, hash, g);

                            DB.store(n, gi, gnum, hi, ss, si, trial, ei, 
                                        val, dG, L, dS, dH, elapsedTime/1000.0,
                                        est.getK(), est.getM(), est.get2F());
                        }
                    } 
                } // sampler
            } // sample size
        } // hasher
        
        DB.dump();
    }
    
    public ISampler getConstantSizeSampler(int type, DirectedSparseGraph<Vertex,Edge> g, int param) {
        ISampler samp=null;
        double[] referrals;
        int seeds;
        switch (type) {
            case 0:
                samp = new FixedSampler(param);
                break;
            case 1:
                referrals = new double [4];
                referrals[0]=0.0;
                referrals[1]=0.1;
                referrals[2]=0.9;
                referrals[3]=0.0;
                seeds = 7;
                samp = new RDSSampler(seeds, param, referrals);
                break;
            case 2:
                referrals = new double [4];
                referrals[0]=0.0;
                referrals[1]=0.0;
                referrals[2]=1.0;
                referrals[3]=0.0;
                seeds = 10;
                samp = new RDSSampler(seeds, param, referrals);
                break;
            case 3:
                referrals = new double [4];
                referrals[0]=0.0;
                referrals[1]=0.0;
                referrals[2]=0.0;
                referrals[3]=1.0;
                seeds = 1;
                samp = new RDSSampler(seeds, param, referrals);
                break;
            default:
                break;
        }
        return samp;
    }
    
    public ISampler getProbabilisticSampler(int type, DirectedSparseGraph<Vertex,Edge> g, double param) {
        ISampler samp=null;
        switch (type) {
            case 0:
                samp = new UniformSampler(param);
                break;
            case 1:
            case 2:
            case 3:
                double[] referrals = new double [4];
                referrals[0]=0.3;
                referrals[1]=0.3;
                referrals[2]=0.3;
                referrals[3]=0.1;
                int seeds = 3 * (type - 1) + 1;
                samp = new RDSSampler(seeds, (int)Math.ceil(param*g.getVertexCount()), referrals);
                break;
            default:
                break;
        }
        return samp;
    }
    
    public IHasher getHasher(int type, DirectedSparseGraph<Vertex,Edge> g, int param) {
        IHasher hash=null;
        switch (type) {
            case 0:
                hash = new LosslessHasher();
                hash.initialize(g.getVertices());
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                int H = (int)Math.ceil(param * Math.pow(2.0, type-1.0));
                hash = new UniformRandomHasher(H);
                hash.initialize(g.getVertices());
                break;
        }
        return hash;
    }
    
    public DirectedSparseGraph<Vertex,Edge> getGraph(String fileName) 
    {
            Factory<Vertex> vertexFactory = 
                new Factory<Vertex>() {
                    int _count = 0;
                    public Vertex create() {
                        Vertex v = new Vertex();
                        v.setUserDatum(Vertex.LABEL, ""+_count);
                        _count++;
                        return v;
                }};
                
            Factory<Edge> edgeFactory = 
                new Factory<Edge>() {
                    int count;
                    public Edge create() {
                        return new Edge();
                }};
                        
        PajekFileGraphGenerator ggen = new PajekFileGraphGenerator(fileName, vertexFactory, edgeFactory);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        return g;
    }
    
    public IPopulationEstimator getEstimator(int type) {
        IPopulationEstimator est=null;
        switch (type) {
            case 0:
                est = new BaselineEstimate();
                break;
            case 1:
                est = new BaselineWithRDSCorrectionEstimate();
                break;
            case 2:
                est = new HashingEstimate();
                break;
            case 3:
                est = new DegreeCorrectedEstimate();
                break;
            case 4:
                est = new CrossSeedEstimate();
                break;
            default:
                break;
        }
        return est;
    }
    
}
