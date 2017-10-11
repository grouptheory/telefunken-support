/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public interface ISampler {    
    String getHumanReadableName();
    
    AbstractSample generateSample(DirectedSparseGraph<Vertex,Edge> g);
}
