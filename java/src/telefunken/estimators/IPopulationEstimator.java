/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.estimators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public interface IPopulationEstimator {
    String getHumanReadableName();
    
    public String toString();
    
    public double computeEstimate(AbstractSample S, IHasher hash);
    
    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex,Edge> g);
    
    public double getK();
    public double getM();
    public double get2F();
}
