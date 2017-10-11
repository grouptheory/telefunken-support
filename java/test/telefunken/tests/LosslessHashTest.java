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
import org.junit.Test;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.hashers.IHasher;
import telefunken.hashers.LosslessHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.ISampler;
import telefunken.samplers.RDSSampler;

/**
 *
 * @author Bilal Khan
 */
public class LosslessHashTest {
    
    @Test
    public void test1() {
        for (int size=10; size<=100; size+=10) {
            IGraphGenerator ggen = new GridGenerator(size);
            DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();

            IHasher hash = new LosslessHasher();
            hash.initialize(g.getVertices());

            Multiset<Vertex> verticesAsMultiset = HashMultiset.create();
            verticesAsMultiset.addAll(g.getVertices());

            for (int trial=0; trial<5; trial++) {
                Multiset<Integer> codes = hash.getCodes(verticesAsMultiset);

                int q1=g.getVertices().size();
                int q2=codes.size();
                String err = (q1==q2?"":"!!!!!"); 
                System.out.println(err+" size="+size+" .. Codes should be size:"+q1+"  and is size:"+q2);
                assertEquals(q1,q2);
            }
        }
    }
}
