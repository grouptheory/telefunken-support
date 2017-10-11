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
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Bilal Khan
 */
public class GridGraphTest {
    
    @Test
    public void test1() {
        for (int size=2;size<=100;size++) {
            IGraphGenerator ggen = new GridGenerator(size);
            DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
            System.out.println("Testing vertex and edge count for side "+size+" grid.");
            assertEquals(g.getVertexCount(), size*size);
            assertEquals(g.getEdgeCount(), 2*((size-1)+(size-1)+2*(size-1)*(size-1)));
        }
    }
}
