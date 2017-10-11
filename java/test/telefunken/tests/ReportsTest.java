/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import com.google.common.collect.Multiset;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.text.DecimalFormat;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
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
public class ReportsTest {
    
    DecimalFormat df = new DecimalFormat("#.00"); 
    
    @Test
    public void test1() {
        System.out.println("================== Uniform");
        int size=100;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        
        IHasher hash = new LosslessHasher();
        hash.initialize(g.getVertices());
        
        for (double p=0.1; p<=1.0; p+=0.1) {
            UniformSampler sampler3 = new UniformSampler(p);
            AbstractSample s3 = sampler3.generateSample(g);
            Multiset<Vertex> reports_V = s3.getOutset(s3.getVertices());
            Multiset<Integer> reports = hash.getCodes(reports_V);
            
            System.out.println("UNIFORM |S|="+s3.size()+" <rS>="+reports.size()+" |rS*|="+reports.elementSet().size()+
                    " rep/subj="+df.format((double)reports.size()/(double)s3.size())+
                    " avemult="+df.format((double)reports.size()/(double)reports.elementSet().size()));
        }
    }
    
    @Test
    public void test2() {
        System.out.println("================== RDS");
        int size=100;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        int n = size*size;
        
        IHasher hash = new LosslessHasher();
        hash.initialize(g.getVertices());
        
        for (double p=0.1; p<=1.0; p+=0.1) {
            int numseeds = 1;
            int samplesize = (int)Math.ceil(n * p);
            double[] referrals = new double [4];
            referrals[0]=0.0;
            referrals[1]=0.05;
            referrals[2]=0.10;
            referrals[3]=0.85;

            ISampler sampler3 = new RDSSampler(numseeds, samplesize, referrals);
            AbstractSample s3 = sampler3.generateSample(g);
            Multiset<Vertex> reports_V = s3.getOutset(s3.getVertices());
            Multiset<Integer> reports = hash.getCodes(reports_V);
            
            System.out.println("RDS |S|="+s3.size()+" <rS>="+reports.size()+" |rS*|="+reports.elementSet().size()+
                    " rep/subj="+df.format((double)reports.size()/(double)s3.size())+
                    " avemult="+df.format((double)reports.size()/(double)reports.elementSet().size()));
        }
    }
        
    @Test
    public void test3() {
        System.out.println("================== Uniform with Hashing");
        int size=100;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        
        IHasher hash = new UniformRandomHasher(1000);
        hash.initialize(g.getVertices());
        
        for (double p=0.1; p<=1.0; p+=0.1) {
            UniformSampler sampler3 = new UniformSampler(p);
            AbstractSample s3 = sampler3.generateSample(g);
            Multiset<Vertex> reports_V = s3.getOutset(s3.getVertices());
            Multiset<Integer> reports = hash.getCodes(reports_V);
            
            System.out.println("psi-UNIFORM |S|="+s3.size()+" <psi-rS>="+reports.size()+" |psi-rS*|="+reports.elementSet().size()+
                    " rep/subj="+df.format((double)reports.size()/(double)s3.size())+
                    " avemult="+df.format((double)reports.size()/(double)reports.elementSet().size()));
        }
    }
    
    @Test
    public void test4() {
        System.out.println("================== RDS with Hashing");
        int size=100;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        int n = size*size;
        
        IHasher hash = new UniformRandomHasher(1000);
        hash.initialize(g.getVertices());
        
        for (double p=0.1; p<=1.0; p+=0.1) {
            int numseeds = 1;
            int samplesize = (int)Math.ceil(n * p);
            double[] referrals = new double [4];
            referrals[0]=0.0;
            referrals[1]=0.05;
            referrals[2]=0.10;
            referrals[3]=0.85;

            ISampler sampler3 = new RDSSampler(numseeds, samplesize, referrals);
            AbstractSample s3 = sampler3.generateSample(g);
            Multiset<Vertex> reports_V = s3.getOutset(s3.getVertices());
            Multiset<Integer> reports = hash.getCodes(reports_V);
            
            System.out.println("psi-RDS |S|="+s3.size()+" <psi-rS>="+reports.size()+" |psi-rS*|="+reports.elementSet().size()+
                    " rep/subj="+df.format((double)reports.size()/(double)s3.size())+
                    " avemult="+df.format((double)reports.size()/(double)reports.elementSet().size()));
        }
    }
}
