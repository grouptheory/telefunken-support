/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.distributions.Exponential;
import telefunken.distributions.IDistributionGenerator;
import telefunken.distributions.LogNormal;
import telefunken.distributions.Poisson;
import telefunken.distributions.Uniform;
import telefunken.generators.ConfigurationGenerator;
import telefunken.generators.IGraphGenerator;

/**
 *
 * @author Bilal Khan
 */
public class ConfigurationTest {
    
    @Test
    public void test1() {
        
        dotest(10000, true);
    }
    
    void dotest(int CT, boolean print) {
        if (print) System.out.println("=============================UNIFORM");
        int param1=5;
        IDistributionGenerator dg1 = new Uniform(CT,param1);
        int [] deg1 = dg1.generate();
        IGraphGenerator ggen1 = new ConfigurationGenerator(deg1, dg1.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g1 = ggen1.generateGraph();
        
        if (print) System.out.println("=============================POISSON");
        double param2=5.0;
        IDistributionGenerator dg2 = new Poisson(CT,param2);
        int [] deg2 = dg2.generate();
        IGraphGenerator ggen2 = new ConfigurationGenerator(deg2, dg2.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g2 = ggen2.generateGraph();
        
        if (print) System.out.println("=============================EXPONENTIAL");
        double param3=1.0/5.0;
        IDistributionGenerator dg3 = new Exponential(CT,param3);
        int [] deg3 = dg3.generate();
        IGraphGenerator ggen3 = new ConfigurationGenerator(deg3, dg3.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g3 = ggen3.generateGraph();
        
        if (print) System.out.println("=============================LOGNORMAL");
        double param4=5.0;
        IDistributionGenerator dg4 = new LogNormal(CT,param4,1.0);
        int [] deg4 = dg4.generate();
        IGraphGenerator ggen4 = new ConfigurationGenerator(deg4, dg4.getHumanReadableName());
        DirectedSparseGraph<Vertex,Edge> g4 = ggen4.generateGraph();
    }
}
