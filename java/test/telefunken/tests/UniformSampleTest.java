/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.UniformSampler;

/**
 *
 * @author Bilal Khan
 */
public class UniformSampleTest {
    
    @Test
    public void test1() {
        int size=6;
        IGraphGenerator ggen = new GridGenerator(size);
        DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
        
        UniformSampler sampler1 = new UniformSampler(1.0);
        AbstractSample s1 = sampler1.generateSample(g);
        assertEquals(s1.getVertices().size(), size*size);
        
        UniformSampler sampler2 = new UniformSampler(0.0);
        AbstractSample s2 = sampler2.generateSample(g);
        assertEquals(s2.getVertices().size(), 0);
        
        for (double p=0.0; p<=1.0; p+=0.1) {
            double sum=0.0;
            int trials=1000;
            for (int i=0; i<trials;i++) {
                UniformSampler sampler3 = new UniformSampler(p);
                AbstractSample s3 = sampler3.generateSample(g);
                sum+=s3.getVertices().size();
            }
            double ave=sum/(double)trials;
            double epsi=0.05;
            double lower=(1.0-epsi)*p*size*size;
            double upper=(1.0+epsi)*p*size*size;
            System.out.println("Testing p="+p+"  ave="+ave+"  bounds=["+lower+","+upper+"]");
            assertTrue(ave>=lower && ave<=upper);
        }
    }
}
