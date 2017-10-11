/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.ArrayList;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public final class FixedSampler implements ISampler {

    private int _size;
    
    public FixedSampler(int size) {
        _size=size;
    }

    public AbstractSample generateSample(DirectedSparseGraph<Vertex,Edge> g) {
        
        FixedSample S = new FixedSample(g);
        ArrayList<Vertex> eligible = new ArrayList<Vertex>();
        eligible.addAll(g.getVertices());
        
        while (S.getVertices().size() < _size) {
            int index = Randomness.getInteger() % eligible.size();
            Vertex picked = eligible.get( index );
            eligible.remove( index );
            S.addVertex(picked);
        }
        return S;
    }

    @Override
    public String getHumanReadableName() {
        return "UniformFixed";
    }
    
    public static class FixedSample extends AbstractSample {
        FixedSample(DirectedSparseGraph<Vertex,Edge> g) {
            super(g, "UniformFixed");
        }
    }
}
