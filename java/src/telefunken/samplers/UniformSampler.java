/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public final class UniformSampler implements ISampler {

    private double _prob;
    
    public UniformSampler(double prob) {
        _prob=prob;
    }

    public AbstractSample generateSample(DirectedSparseGraph<Vertex,Edge> g) {
        UniformSample S = new UniformSample(g);
        for (Vertex v : g.getVertices()) {
            if (Randomness.getDouble()<=_prob) {
                S.addVertex(v);
            }
        }
        return S;
    }

    @Override
    public String getHumanReadableName() {
        return "UniformProp";
    }
    
    public static class UniformSample extends AbstractSample {
        UniformSample(DirectedSparseGraph<Vertex,Edge> g) {
            super(g, "UniformProp");
        }
    }
}
