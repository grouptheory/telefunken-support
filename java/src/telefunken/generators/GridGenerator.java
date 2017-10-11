/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.generators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.HashMap;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public final class GridGenerator implements IGraphGenerator {

    private final int _n;
    
    public GridGenerator(int n) {
        _n=n;
    }
    
    @Override
    public DirectedSparseGraph<Vertex,Edge> generateGraph() {
        
	DirectedSparseGraph<Vertex,Edge> g = new DirectedSparseGraph<Vertex,Edge>();
        
        HashMap<Integer,HashMap<Integer,Vertex>> map =
            new HashMap<Integer,HashMap<Integer,Vertex>>();
        
        for (int x=0;x<_n;x++) {
            HashMap<Integer,Vertex> submap = new HashMap<Integer,Vertex>();
            map.put(x, submap);
            for (int y=0;y<_n;y++) {
                Vertex v = new Vertex();
                // copy over the label
                v.setUserDatum(Vertex.LABEL, "x="+x+",y="+y);
                
                g.addVertex(v);
                submap.put(y,v);
                
                if (x>0) {
                    Vertex vleft = map.get(x-1).get(y);
                    g.addEdge(new Edge(), v, vleft);
                    g.addEdge(new Edge(), vleft, v);
                }
                if (y>0) {
                    Vertex vup = map.get(x).get(y-1);
                    g.addEdge(new Edge(), v, vup);
                    g.addEdge(new Edge(), vup, v);
                }
            }
        }   
        return g;
    } 

    @Override
    public String getHumanReadableName() {
        return "Grid";
    }
}
