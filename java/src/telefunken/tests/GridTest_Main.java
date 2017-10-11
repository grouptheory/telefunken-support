/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.generators.GridGenerator;
import telefunken.generators.IGraphGenerator;
import telefunken.utils.Visualization;

/**
 *
 * @author Bilal Khan
 */
public class GridTest_Main {
  
    public static void mainXXX(String [] args)
    {
        makeGrid();
    }
    
    public static void makeGrid() {
        for (int size=2;size<=6;size++) {
            IGraphGenerator ggen = new GridGenerator(size);
            DirectedSparseGraph<Vertex,Edge> g = ggen.generateGraph();
            Visualization.showGraph(g);
        }
    }  
}
