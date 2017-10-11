/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.ISampler;
import telefunken.samplers.RDSSampler;

/**
 *
 * @author Bilal Khan
 */
public class RDSSampleTest {
    
    @Test
    public void test1() {
        int size=10;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        int n=size*size;
        
        for (double p=0.1; p<=0.1; p+=0.1) {
            int numseeds = 2;
            int samplesize = (int)Math.ceil(n * p);
            double[] referrals = new double [3];
            referrals[0]=0.0;
            referrals[1]=1.00;
            referrals[2]=0.00;

            ISampler sampler1 = new RDSSampler(numseeds, samplesize, referrals);
            AbstractSample s1 = sampler1.generateSample(g);
            
            int q1=samplesize;
            int q2=s1.getVertices().size();
            String err = (q1==q2?"":"!!!!!"); 
            System.out.println(err+" p="+p+" .. RDS Sample should be size:"+q1+"  and is size:"+q2);
            assertEquals(q1,q2);
            
            System.out.println(s1.toString());
        }
    }
}
