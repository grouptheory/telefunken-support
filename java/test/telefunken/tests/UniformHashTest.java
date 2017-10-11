/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.hashers.UniformRandomHasher;

/**
 *
 * @author Bilal Khan
 */
public class UniformHashTest {
    
    @Test
    public void test1() {
        for (int H=100;H<10000;H+=1000) {
            System.out.println("=================== hashsize="+H);
            for (int size=10; size<=100; size+=40) {
                IGraphGenerator ggen = new GridGenerator(size);
                DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();

                IHasher hash = new UniformRandomHasher(H);
                hash.initialize(g.getVertices());

                Multiset<Vertex> verticesAsMultiset = HashMultiset.create();
                verticesAsMultiset.addAll(g.getVertices());

                Multiset<Integer> codes = hash.getCodes(verticesAsMultiset);

                int q1=g.getVertices().size();
                int q2=codes.size();
                int q3=codes.elementSet().size();
                String err = (q1==q2?"":"!!!!!"); 
                System.out.println(err+" hashsize="+H+
                                   " graphsize="+size+
                                   " vertex set size:"+q1+
                                   " code multiset size:"+q2+
                                   " code set size:"+q3);
                assertEquals(q1,q2);
                assertTrue(q3<=q1);
                assertTrue(q3<=H);
            }
        }
    }
}
