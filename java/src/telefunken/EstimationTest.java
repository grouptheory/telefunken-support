/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Arrays;
import java.util.List;
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
public class EstimationTest {
    
    public void test1(int graphFamily, String testname) {
        //int NUMGRAPHS = 30;
        //int NUMTRIALS = 30;
        int NUMGRAPHS = 30;
        int NUMTRIALS = 30;
        
        Randomness.initialize(1);
        
        ResultsDatabase DB = new ResultsDatabase(testname);
                                
        List<Integer> nlist = Arrays.asList(5000,10000,20000,40000); //, 5000, 10000, 20000); //, 50000); //,50000); // 6250, 12500, 25000, 5000, 10000, 25000, 50000
        for (int n : nlist) {
            
            // 0=grid, 1=lognormal, 2=poisson, 3=exponential, 4=uniform, 5=Barabasi-Albert, 6=Erdos-Renyi
            List<Integer> glist = Arrays.asList(graphFamily); //,4);  
            for (int gi : glist) {
                
                List<Double> dlist = Arrays.asList(3.0, 5.0, 10.0); //3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0); //3.0, 5.0, 10.0); // 0=lossless, 1=500, 2=1000, 3=2000, 4=4000, 5=8000
                for (double deg : dlist) {

                    for (int gnum=0; gnum<NUMGRAPHS; gnum++) {
                
                        IGraphGenerator ggen = getGraphGenerator(gi, n, deg);
                        DirectedSparseGraph<Vertex,Edge> g = getGraph(ggen);

                        // 0=lossless, 1=500, 2=1000, 3=2000, 4=4000, 5=8000
                        // 6=16K, 7=32K, 8=64K, 9=128K, 10=256K
                        List<Integer> hlist = Arrays.asList(10); 
                        for (int hi : hlist) {
                            IHasher hash = getHasher(hi, g, 500);

                            int ss;
                            List<Double> sslist_double = Arrays.asList(0.01, 0.1, 0.5, 0.75, 0.90);
                            //List<Integer> sslist = Arrays.asList(2000, 4000); //,1000,2000); //100,200,400,800); // 250, 500, 750
                            for (double ss_double : sslist_double) {

                                ss = (int)(ss_double * (double)n);
                                
                                for (int trial=0; trial<NUMTRIALS; trial++) {

                                    List<Integer> slist = Arrays.asList(1); //, 2); // 0=Uniform, 1=RDS/w1, 2=RDS/w2, 3=RDS/w3
                                    for (int si : slist) 
                                    {
                                        ISampler samp = getConstantSizeSampler(si, g, ss);

                                        //System.out.println("============ G="+gnum+" Type:" + ggen.getHumanReadableName() + " SS:"+ss+" Trial:"+trial +" Sampler:" + samp.getHumanReadableName());

                                        AbstractSample S = samp.generateSample(g);

                                        double[] dG = S.dG_All_Moments();
                                        double[] dS = S.dSample_All_Moments();
                                        double[] dH = S.dSample_All_Harmonic_Moments();
                                        double L = S.get_TrueL();

                                          List<Integer> elist = Arrays.asList(3); //,4); //0,1); // 0=closed form, 1=RDS-corrected, 2=ohash-corrected
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
                    } // graphnumber
                } // degree 
            } // graph type
        } // graph size
        
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
    
    public IGraphGenerator getGraphGenerator(int type, int n, double param) {
        IDistributionGenerator dg;
        int [] deg;
        IGraphGenerator ggen = null;
        switch (type) {
            case 0:
                ggen = new GridGenerator((int)Math.ceil(Math.sqrt(n)));
                break;
            case 1:
                dg = new LogNormal(n,param - 1.0,1.0); // because distribution is shifted up by 1 to avoid isolated vertices
                deg = dg.generate();
                ggen = new ConfigurationGenerator(deg, dg.getHumanReadableName());
                break;
            case 2:
                dg = new Poisson(n,param - 1.0); // because distribution is shifted up by 1 to avoid isolated vertices
                deg = dg.generate();
                ggen = new ConfigurationGenerator(deg, dg.getHumanReadableName());
                break;
            case 3:
                dg = new Exponential(n,1.0/(param - 1.0)); // because distribution is shifted up by 1 to avoid isolated vertices
                deg = dg.generate();
                ggen = new ConfigurationGenerator(deg, dg.getHumanReadableName());
                break;
            case 4:
                dg = new Uniform(n, (int)Math.round(param));
                deg = dg.generate();
                ggen = new ConfigurationGenerator(deg, dg.getHumanReadableName());
                break;
            case 5:
                ggen = new BarabasiAlbertGenerator(n, param/2.0, 1.0);
                break;
            case 6:
                ggen = new ErdosRenyiGenerator(n, param/(double)n); // param*Math.log((double)n)/(double)n);
                break;
            default:
                break;
        }
        return ggen;
    }
    
    public DirectedSparseGraph<Vertex,Edge> getGraph(IGraphGenerator ggen) {
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
